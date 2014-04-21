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
package org.apache.beehive.netui.pageflow;

import org.apache.beehive.netui.pageflow.internal.JavaControlUtils;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.ServletContext;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Base class for Page Flow managed objects (like page flows and JavaServer Faces backing beans).
 */ 
public abstract class PageFlowManagedObject
    implements Serializable, HttpSessionBindingListener
{
    private static final Logger _log = Logger.getInstance( PageFlowManagedObject.class );
    
    /**
     * Reference to the current ServletContext.
     */ 
    private transient ServletContext _servletContext;

    /**
     * Creation time.  This is non-transient, so it gets replicated in a cluster.
     */
    private long _createTime;

    /**
     * Flag used to detect multiple {@link #valueUnbound(javax.servlet.http.HttpSessionBindingEvent)} events.
     * Depending on the {@link org.apache.beehive.netui.pageflow.handler.StorageHandler} implementation used,
     * it is possible for the framework and the Servlet container to dispatch multiple unbinding events signaling
     * either the real or virtual removal of this object from the session.  If the removal was virtual with a
     * real removal to follow later, two (or more) {@link #valueUnbound(javax.servlet.http.HttpSessionBindingEvent)}
     * events could be received by this object.  This flag prevents this with the assumption that once detached
     * from the {@link HttpSession}, it is never reattached.
     */
    private boolean _isDestroyed = false;

    protected PageFlowManagedObject()
    {
        _createTime = System.currentTimeMillis();
    }
    
    /**
     * Reinitialize the object for a new request.  Used by the framework; normally should not be called directly.
     */ 
    public void reinitialize( HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
    {
        _servletContext = servletContext;
    }

    /**
     * Internal method to reinitialize only if necessary.  The test is whether the ServletContext
     * reference has been lost.
     */
    void reinitializeIfNecessary(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
    {
        if (_servletContext == null) {
            reinitialize(request, response, servletContext);
        }
    }

    /**
     * Initialize after object creation.  This is a framework-invoked method; it should not normally be called directly.
     */ 
    public synchronized void create( HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
        throws Exception
    {
        reinitialize( request, response, servletContext );
        JavaControlUtils.initJavaControls( request, response, servletContext, this );
        onCreate();
    }

    /**
     * Internal destroy method that is invoked when this object is being removed from the session.  This is a
     * framework-invoked method; it should not normally be called directly.
     */ 
    void destroy( HttpSession session )
    {
        onDestroy( session );
        JavaControlUtils.uninitJavaControls( session.getServletContext(), this );
    }
    
    /**
     * Create-time callback.  Occurs after internal initialization (e.g., Control fields) is done.
     * @throws Exception
     */ 
    protected void onCreate()
        throws Exception
    {
    }
    
    /**
     * Callback that occurs when this object is "destroyed", i.e., removed from the session.
     * @param session
     */ 
    protected void onDestroy( HttpSession session )
    {
    }
    
    /**
     * Callback when this object is added to the user session.
     */ 
    public void valueBound( HttpSessionBindingEvent event )
    {
    }

    /**
     * Callback when this object is removed from the user session.  Causes {@link #onDestroy} to be called.  This is a
     * framework-invoked method that should not be called directly.
     */
    public void valueUnbound( HttpSessionBindingEvent event )
    {
        //
        // We may have lost our transient ServletContext reference.  Try to get the ServletContext reference from the
        // HttpSession object if necessary.
        //
        ServletContext servletContext = getServletContext();
        HttpSession session = event.getSession();

        if ( servletContext == null && session != null )
        {
            servletContext = session.getServletContext();
        }

        if ( servletContext != null )
        {
            if ( !_isDestroyed && Handlers.get( servletContext ).getStorageHandler().allowBindingEvent( event ) )
            {
                destroy( session );
                _isDestroyed = true;
            }
        }
    }
    
    /**
     * Remove this instance from the session.
     */ 
    public abstract void removeFromSession( HttpServletRequest request );
    
    /**
     * Store this object in the user session, in the appropriate place.  Used by the framework; normally should not be
     * called directly.
     */ 
    public abstract void persistInSession( HttpServletRequest request, HttpServletResponse response );
    
    /**
     * Ensures that any changes to this object will be replicated in a cluster (for failover),
     * even if the replication scheme uses a change-detection algorithm that relies on
     * HttpSession.setAttribute to be aware of changes.  Note that this method is used by the framework
     * and does not need to be called explicitly in most cases.
     * 
     * @param request the current HttpServletRequest
     */ 
    public abstract void ensureFailover( HttpServletRequest request ); 
    
    /**
     * Get the current ServletContext.
     */ 
    protected ServletContext getServletContext()
    {
        return _servletContext;
    }
    
    /**
     * Get the display name for this managed object.
     */ 
    public abstract String getDisplayName();
    
    /**
     * Tell whether the given Field is uninitialized.
     * @return <code>true</code> if the field is non-<code>null</code> and its value is <code>null</code>.
     */ 
    protected boolean fieldIsUninitialized( Field field )
    {
        try
        {
            return field != null && field.get( this ) == null;
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Error initializing field " + field.getName() + " in " + getDisplayName(), e );
            return false;
        }
    }
    
    /**
     * Initialize the given field with an instance.  Mainly useful for the error handling.
     */ 
    protected void initializeField( Field field, Object instance )
    {
        if ( instance != null )
        {
            if ( _log.isTraceEnabled() )
            {
                _log.trace( "Initializing field " + field.getName() + " in " + getDisplayName() + " with " + instance );
            }
            
            try
            {
                field.set( this, instance );
            }
            catch ( IllegalArgumentException e )
            {
                _log.error( "Could not set field " + field.getName() + " on " + getDisplayName() +
                            "; instance is of type " + instance.getClass().getName() + ", field type is "
                            + field.getType().getName() );
            }
            catch ( IllegalAccessException e )
            {
                _log.error( "Error initializing field " + field.getName() + " in " + getDisplayName(), e );
            }
        }
    }

    /**
     * Get the time at which this object was created.
     * @return the system time, in milliseconds, at which this object was created.
     */
    public long getCreateTime()
    {
        return _createTime;
    }

    /**
     * Package protected getter that provides access to a read-only property about the current state
     * of this Page Flow.  If it has been destroyed in the middle of a request, this flag will return
     * <code>true</code> and <code>false</code> otherwise.
     *
     * @return <code>true</code> if this object has been destroyed; <code>false</code> otherwise.
     */
    boolean isDestroyed() {
        return _isDestroyed;
    }
}
