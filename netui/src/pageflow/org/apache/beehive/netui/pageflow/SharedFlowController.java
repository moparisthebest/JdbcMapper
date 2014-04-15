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

import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.util.internal.cache.ClassLevelCache;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Base "shared flow" class for controller logic, exception handlers, and state that can be shared by any number of page
 * flows.  A shared flow is <i>not</i> a page flow; it is used by page flows, but never becomes the "current page flow"
 * (see {@link PageFlowController} for information on page flows and the "current page flow").
 * The class is configured through the
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller} annotation.
 * </p>
 * 
 * <p>
 * A shared flow comes into existance in one of two ways:
 * <ul>
 *     <li>
 *         A page flow is hit, and the page flow refers to the shared flow in its
 *         {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs}
 *         annotation attribute, or
 *     </li>
 *     <li>
 *         Any page flow is hit, and the <code>&lt;default-shared-flow-refs&gt;</code> element in
 *         /WEB-INF/beehive-netui-config.xml declares that this shared flow will be used by all page flows in the web
 *         application.
 *     </li>
 * </ul>
 * When a shared flow is created, it is stored in the user session.  It is only removed through a call to
 * {@link #remove} or through a call to {@link PageFlowUtils#removeSharedFlow}.
 * </p>
 * 
 * <p>
 * Shared flow actions are defined with <i>action methods</i> or <i>action annotations</i> that determine the next URI
 * to be displayed, after optionally performing arbitrary logic.  A page or page flow can raise a shared flow action
 * using the pattern <code>"</code><i>shared-flow-name</i><code>.</code><i>action-name</i><code>"</code>.  The shared
 * flow name is the one chosen by the page flow 
 * in {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SharedFlowRef#name name}
 * on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SharedFlowRef &#64;Jpf.SharedFlowRef}.
 * </p>
 * 
 * <p>
 * A referenced shared flow gets the chance to handle any uncaught page flow exception.  It declares its exception
 * handling through {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#catches catches}
 * on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}.
 * </p>
 * 
 * <p>
 * Properties in the current shared flow instance can be accessed from JSP 2.0-style expressions like this one:
 * <code>${sharedFlow.</code><i>sharedFlowName</i><code>.someProperty}</code>.
 * </p>
 * 
 * <p>
 * There may only be one shared flow in any package.
 * </p>
 * 
 * @see PageFlowController
 */
public abstract class SharedFlowController
        extends FlowController
        implements PageFlowConstants
{
    private static final String CACHED_INFO_KEY = "cachedInfo";
    
    /**
     * Get the Struts module path for actions in this shared flow.
     * 
     * @return the Struts module path for actions in this shared flow.
     */ 
    public String getModulePath()
    {
        ClassLevelCache cache = ClassLevelCache.getCache( getClass() );
        String modulePath = ( String ) cache.getCacheObject( CACHED_INFO_KEY );
        
        if ( modulePath == null )
        {
            modulePath = "/-";
            String className = getClass().getName();
            int lastDot = className.lastIndexOf( '.' );
            if (lastDot != -1) {
                className = className.substring( 0, lastDot );
                modulePath += className.replace( '.', '/' );
            }
            cache.setCacheObject( CACHED_INFO_KEY, modulePath );
        }
        
        return modulePath;
    }
    
    /**
     * Store this object in the user session, in the appropriate place.  Used by the framework; normally should not be
     * called directly.
     */ 
    public void persistInSession( HttpServletRequest request, HttpServletResponse response )
    {
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName(InternalConstants.SHARED_FLOW_ATTR_PREFIX
                                                                      + getClass().getName(), unwrappedRequest);
        sh.setAttribute( rc, attrName, this );
    }
    
    /**
     * Ensures that any changes to this object will be replicated in a cluster (for failover),
     * even if the replication scheme uses a change-detection algorithm that relies on
     * HttpSession.setAttribute to be aware of changes.  Note that this method is used by the framework
     * and does not need to be called explicitly in most cases.
     * 
     * @param request the current HttpServletRequest
     */ 
    public void ensureFailover( HttpServletRequest request )
    {
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName(InternalConstants.SHARED_FLOW_ATTR_PREFIX
                                                                      + getClass().getName(), request);
        sh.ensureFailover( rc, attrName, this );
    }
    
    /**
     * Get the URI.
     * @return <code>null</code>, as this object is not URL-addressible.
     */
    public String getURI()
    {
        return "/";
    }
    
    /**
     * Get the display name.  The display name for a shared flow is simply the class name.
     * @return the name of the shared flow class.
     */ 
    public String getDisplayName()
    {
        return getClass().getName();
    }
    
    /**
     * Get a legacy PreviousPageInfo.
     * @deprecated This method will be removed without replacement in a future release.
     */ 
    public PreviousPageInfo getPreviousPageInfoLegacy( PageFlowController curJpf, HttpServletRequest request )
    {
        assert curJpf != null;
        return curJpf.getCurrentPageInfo();        
    }
    
    /**
     * Store information about recent pages displayed.  This is a framework-invoked method that should not normally be
     * called directly.
     */ 
    public void savePreviousPageInfo( ActionForward forward, ActionForm form, ActionMapping mapping,
                                      HttpServletRequest request, ServletContext servletContext,
                                      boolean isSpecialForward )
    {
        //
        // Special case: if the given forward has a path to a page in the current pageflow, let that pageflow save
        // the info on this page.  Otherwise, don't ever save any info on what we're forwarding to.
        //
        if ( ! isSpecialForward && forward != null )  // i.e., it's a straight Forward to a path, not a navigateTo, etc.
        {
            PageFlowController currentJpf = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );
            
            if ( currentJpf != null )
            {
                if ( forward.getContextRelative() && forward.getPath().startsWith( currentJpf.getModulePath() ) )
                {
                    currentJpf.savePreviousPageInfo( forward, form, mapping, request, servletContext, isSpecialForward );
                }
            }
        }
    }

    /**
     * Store information about the most recent action invocation.  This is a framework-invoked method that should not
     * normally be called directly
     */ 
    void savePreviousActionInfo( ActionForm form, HttpServletRequest request, ActionMapping mapping,
                                 ServletContext servletContext )
    {
        //
        // Save this previous-action info in the *current page flow*.
        //
        PageFlowController currentJpf = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );
        if ( currentJpf != null ) currentJpf.savePreviousActionInfo( form, request, mapping, servletContext );
    }

    /**
     * Remove this instance from the session.  When inside a shared flow action, {@link #remove} may be called instead.
     */ 
    public synchronized void removeFromSession( HttpServletRequest request )
    {
        PageFlowUtils.removeSharedFlow( getClass().getName(), request );
    }
}
