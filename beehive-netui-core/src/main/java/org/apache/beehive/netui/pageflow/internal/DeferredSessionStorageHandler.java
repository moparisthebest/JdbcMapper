/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package org.apache.beehive.netui.pageflow.internal;

import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.RequestContext;
import org.apache.beehive.netui.pageflow.ServletContainerAdapter;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.internal.ServletUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This alternate session storage handler does not write any attribute into the session until the very end of a chain
 * of forwarded requests (i.e., not even at the end of an inner forwarded request).  This allows it to handle multiple
 * concurrent forwarded requests, each of which is modifying the same data, in a more reasonable way.  Basically,
 * each request works in its own snapshot of the session, and the last one to commit is the one whose snapshot wins.
 * This is a better alternative than allowing them to interfere with each other in the middle of the request chain.
 */
public class DeferredSessionStorageHandler
        extends DefaultHandler
        implements StorageHandler
{
    private static final Logger _log = Logger.getInstance(DeferredSessionStorageHandler.class);

    private static final String CHANGELIST_ATTR = InternalConstants.ATTR_PREFIX + "changedAttrs";
    private static final String FAILOVER_MAP_ATTR = InternalConstants.ATTR_PREFIX + "failoverAttrs";

    private static ThreadLocal _isCommittingChanges =
            new ThreadLocal()
            {
                public Object initialValue()
                {
                    return Boolean.FALSE;
                }
            };

    public DeferredSessionStorageHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );
    }

    private static final class SessionBindingEvent
        extends HttpSessionBindingEvent
    {
        public SessionBindingEvent( HttpSession httpSession, String attrName )
        {
            super( httpSession, attrName );
        }

        public SessionBindingEvent( HttpSession httpSession, String attrName, Object attrVal )
        {
            super( httpSession, attrName, attrVal );
        }
    }

    public void setAttribute( RequestContext context, String attrName, Object value )
    {
        if (_log.isTraceEnabled()) {
            _log.trace("setAttribute: " + attrName + "=" + value);
        }

        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        Object currentValue = request.getAttribute( attrName );

        //
        // Note that we unconditionally get/create the session.  We want to make sure that the session exists when
        // we set an attribute, since we will *not* create it in applyChanges, to prevent recreation of an
        // intentionally-invalidated session.
        //
        HttpSession session = request.getSession();

        //
        // Emulate a setAttribute on the session: if the value is an HttpSessionBindingListener, invoke its
        // valueUnbound().  Note that we don't currently care about calling valueBound().
        //
        if ( currentValue != null && currentValue != value && currentValue instanceof HttpSessionBindingListener )
        {
            HttpSessionBindingEvent event = new SessionBindingEvent( session, attrName, currentValue );
            ( ( HttpSessionBindingListener ) currentValue ).valueUnbound( event );
        }

        request.setAttribute( attrName, value );
        getChangedAttributesList( request, true, false ).add( attrName );

        //
        // For attributes that have changed and are now tracked in the changed attribute list, be sure to remove
        // them from the list of failover attributes.
        //
        HashMap failoverAttrs = getFailoverAttributesMap( request, false, false );
        if ( failoverAttrs != null )
            failoverAttrs.remove( attrName );
    }

    public void removeAttribute( RequestContext context, String attrName )
    {
        if (_log.isTraceEnabled()) {
            _log.trace("removeAttribute: " + attrName);
        }

        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        Object currentValue = request.getAttribute( attrName );

        //
        // Emulate a removeAttribute on the session: if the value is an HttpSessionBindingListener, invoke its
        // valueUnbound().
        //
        if ( currentValue != null && currentValue instanceof HttpSessionBindingListener )
        {
            HttpSessionBindingEvent event = new SessionBindingEvent( request.getSession(), attrName, currentValue );
            ( ( HttpSessionBindingListener ) currentValue ).valueUnbound( event );
        }

        request.removeAttribute( attrName );
        getChangedAttributesList( request, true, false ).add( attrName );

        HashMap failoverAttrs = getFailoverAttributesMap( request, false, false );
        if ( failoverAttrs != null ) failoverAttrs.remove( attrName );
    }

    public Object getAttribute( RequestContext context, String attributeName )
    {
        if (_log.isTraceEnabled()) {
            _log.trace("getAttribute: " + attributeName);
        }

        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        Object val = request.getAttribute( attributeName );
        if ( val != null ) return val;

        // If the attribute isn't present in the request and is in the list of changed attrs, then it was removed.
        // Don't fall back to the session attribute in that case.
        HashSet changedAttrs = getChangedAttributesList( request, false, false );
        if ( changedAttrs != null && changedAttrs.contains( attributeName ) ) return null;


        // Get the attribute out of the session, and put it into the request.  Until applyChanges is called, this is
        // the value we'll use.
        HttpSession session = request.getSession( false );
        if ( session != null )
        {
            val = session.getAttribute( attributeName );
            if ( val != null ) request.setAttribute( attributeName, val );
        }

        return val;
    }

    public void applyChanges( RequestContext context )
    {
        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        assert request != null;
        HttpSession session = request.getSession( false );

        //
        // If the session doesn't exist, we don't want to recreate it.  It has most likely been intentionally
        // invalidated, since we did ensure its existance in getAttribute.
        //
        if ( session == null )
            return;

        if (_log.isDebugEnabled())
            _log.debug("Applying changes for request " + request.getRequestURI() +
                ".  Identity: " + System.identityHashCode(request));

        Object sessionMutex = ServletUtils.getSessionMutex(session, ServletUtils.SESSION_MUTEX_ATTRIBUTE);

        //
        // Synchronize on the session mutex in order to make the operation of applying changes from a request into
        // the session atomic.  Atomicity is needed in order to ensure that the attributes set in the session via
        // "session.setAttribute(...)" are the same ones present when the "ensureFailover" call is made against the
        // ServletContainerAdapter.
        //
        synchronized(sessionMutex) {

            HashSet changedAttrs = getChangedAttributesList( request, false, true );
            if ( changedAttrs != null )
            {
                //
                // Ensure that the Page Flow in the request is run through its destroy lifecycle correctly
                //
                for(Iterator i = changedAttrs.iterator(); i.hasNext(); ) {
                    String attrName = (String)i.next();
                    Object val = request.getAttribute(attrName);

                    // Write it to the session, but only if the current value isn't already this value.
                    Object currSessValue = session.getAttribute( attrName );

                    //
                    // Here, the value in the session is different than the value about to be persisted to the
                    // session.  In some cases, this requires a PageFlowManagedObject lifecycle method to be
                    // invoked on that object.
                    //
                    if(currSessValue != null &&
                        val != currSessValue &&
                        val instanceof PageFlowController &&
                        val instanceof HttpSessionBindingListener) {
                        //
                        // In order to maintain the single-threaded semantics of PFMO destruction, it's necessary
                        // to synchronize on PFMO objects here.
                        //
                        HttpSessionBindingEvent event = new SessionBindingEvent(session, attrName, currSessValue);
                        synchronized(currSessValue) {
                            ((HttpSessionBindingListener)currSessValue).valueUnbound(event);
                        }
                    }
                }

                //
                // Go through each changed attribute, and either write it to the session or remove it from the session,
                // depending on whether or not it exists in the request.
                //
                for ( Iterator i = changedAttrs.iterator(); i.hasNext(); )
                {
                    String attrName = ( String ) i.next();
                    Object val = request.getAttribute( attrName );

                    if ( val != null )
                    {
                        // Write it to the session, but only if the current value isn't already this value.
                        Object currentValue = session.getAttribute( attrName );

                        if ( currentValue != val )
                        {
                            if (_log.isTraceEnabled()) 
                                _log.trace("Committing attribute " + attrName + " to the session.");

                            // This ThreadLocal value allows others (e.g., an HttpSessionBindingListener like
                            // PageFlowManagedObject) that we're in the middle of committing changes to the session.
                            _isCommittingChanges.set( Boolean.TRUE );

                            try
                            {
                                session.setAttribute( attrName, val );
                            }
                            finally
                            {
                                _isCommittingChanges.set( Boolean.FALSE );
                            }

                            request.removeAttribute( attrName );
                        }
                    }
                    else
                    {
                        if (_log.isTraceEnabled())
                            _log.trace("Removing attribute " + attrName + " from the session.");

                        //
                        // This ThreadLocal value allows others (e.g., an HttpSessionBindingListener like
                        // PageFlowManagedObject) to query whether the Framework is in the middle of committing
                        // changes to the session
                        //
                        _isCommittingChanges.set( Boolean.TRUE );

                        try
                        {
                            session.removeAttribute( attrName );
                        }
                        finally
                        {
                            _isCommittingChanges.set( Boolean.FALSE );
                        }
                    }
                }
            }

            //
            // Now, run the ensureFailover step to make sure that clusterable containers do the right thing
            // with attributes that need to be serialized for failover.
            //
            HashMap failoverAttrs = getFailoverAttributesMap( request, false, true );
            if ( failoverAttrs != null )
            {
                ServletContainerAdapter sa = AdapterManager.getServletContainerAdapter( getServletContext() );

                for ( Iterator i = failoverAttrs.entrySet().iterator(); i.hasNext(); )
                {
                    Map.Entry entry = ( Map.Entry ) i.next();
                    if (_log.isTraceEnabled())
                        _log.trace("Ensure failover for attribute " + entry.getKey());

                    // This ThreadLocal value allows others (e.g., an HttpSessionBindingListener like
                    // PageFlowManagedObject) that we're in the middle of committing changes to the session.
                    _isCommittingChanges.set(Boolean.TRUE);
                    try {
                        sa.ensureFailover( ( String ) entry.getKey(), entry.getValue(), request );
                    }
                    finally {
                        _isCommittingChanges.set(Boolean.FALSE);
                    }
                }
            }
        } // release lock on the HttpSession

        if (_log.isDebugEnabled())
            _log.debug("Completed applying changes for request " + request.getRequestURI() +
                ".  Identity: " + System.identityHashCode(request));
    }

    public void dropChanges(RequestContext context)
    {
        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        if (_log.isDebugEnabled()) {
            _log.debug("Dropping changes for request " + request.getRequestURI());
        }
        getChangedAttributesList( request, false, true );
        getFailoverAttributesMap( request, false, true );
    }

    public Enumeration getAttributeNames(RequestContext context) {
        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        HttpSession session = request.getSession( false );
        ArrayList attrNames = new ArrayList();

        // Start with the attribute names that are in the session.
        if (session != null) {
            for (Enumeration e = session.getAttributeNames(); e.hasMoreElements(); ) {
                attrNames.add(e.nextElement());
            }
        }

        // Add or remove as necessary, based on the list of changed attributes.
        HashSet changedAttrs = getChangedAttributesList( request, false, false );
        if (changedAttrs != null) {
            for (Iterator i = changedAttrs.iterator(); i.hasNext(); ) {
                String attrName = (String) i.next();

                if (request.getAttribute(attrName) != null) {
                    attrNames.add(attrName);
                } else {
                    // If the attribute isn't present in the request and is in the session, then it's scheduled for
                    // removal.
                    attrNames.remove(attrName);
                }
            }
        }

        return Collections.enumeration(attrNames);
    }

    public void ensureFailover( RequestContext context, String attributeName, Object value )
    {
        HttpServletRequest request = ScopedServletUtils.getOuterRequest( ( HttpServletRequest ) context.getRequest() );
        getFailoverAttributesMap( request, true, false ).put( attributeName, value );
    }

    public boolean allowBindingEvent( Object event )
    {
        return ! ( ( Boolean ) _isCommittingChanges.get() ).booleanValue();
    }

    private static HashSet getChangedAttributesList( ServletRequest request, boolean create, boolean remove )
    {
        HashSet set = ( HashSet ) request.getAttribute( CHANGELIST_ATTR );

        //
        // Create a new Set in which to store the changed attributes
        //
        if ( set == null && create )
        {
            set = new HashSet();
            request.setAttribute( CHANGELIST_ATTR, set );
        }

        //
        // Remove the list of changed attributes from the request
        //
        if ( set != null && remove )
            request.removeAttribute( CHANGELIST_ATTR );

        return set;
    }

    private static HashMap getFailoverAttributesMap( ServletRequest request, boolean create, boolean remove )
    {
        HashMap map = ( HashMap ) request.getAttribute( FAILOVER_MAP_ATTR );

        if ( map == null && create )
        {
            map = new HashMap();
            request.setAttribute( FAILOVER_MAP_ATTR, map );
        }

        if ( map != null && remove ) request.removeAttribute( FAILOVER_MAP_ATTR );

        return map;
    }
}