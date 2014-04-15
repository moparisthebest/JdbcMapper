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

import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.CachedFacesBackingInfo;
import org.apache.beehive.netui.pageflow.internal.CachedSharedFlowRefInfo;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.util.internal.cache.ClassLevelCache;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.controls.api.context.ControlContainerContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.Collections;
import java.lang.reflect.Field;

/**
 * <p>
 * A JavaServer Faces backing bean.  An instance of this class will be created whenever a corresponding JSF path is
 * requested (e.g., an instance of foo.MyPage will be created for the webapp-relative path "/foo/MyPage.faces").  The
 * instance will be released (removed from the user session) when a non-matching path is requested.  A faces backing
 * bean can hold component references and event/command handlers, and it can raise actions with normal JSF command event
 * handlers that are annotated with {@link org.apache.beehive.netui.pageflow.annotations.Jpf.CommandHandler &#64;Jpf.CommandHandler}.
 * The bean instance can be bound to with a JSF-style expression like <code>#{backing.myComponent}</code>.
 * </p>
 * <p>
 * JSF backing beans are configured using the
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.FacesBacking &#64;Jpf.FacesBacking} annotation.
 * </p>
 */
public abstract class FacesBackingBean
        extends PageFlowManagedObject
{
    private static final String CACHED_INFO_KEY = "cachedInfo";
    private static final Logger _log = Logger.getInstance( FacesBackingBean.class );
    
    private Map _pageInputs;
    
    /**
     *  The bean context associated with this request
     */
    ControlContainerContext _beanContext;
    
    /**
     * Store this object in the user session, in the appropriate place.  Used by the framework; normally should not be
     * called directly.
     */ 
    public void persistInSession( HttpServletRequest request, HttpServletResponse response )
    {
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName =
                ScopedServletUtils.getScopedSessionAttrName( InternalConstants.FACES_BACKING_ATTR, unwrappedRequest );
        
        sh.setAttribute( rc, attrName, this );
    }

    /**
     * Remove this instance from the session.
     */ 
    public void removeFromSession( HttpServletRequest request )
    {
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName =
                ScopedServletUtils.getScopedSessionAttrName( InternalConstants.FACES_BACKING_ATTR, unwrappedRequest );
        
        sh.removeAttribute( rc, attrName );
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
        String attr =
                ScopedServletUtils.getScopedSessionAttrName( InternalConstants.FACES_BACKING_ATTR, unwrappedRequest );
        sh.ensureFailover( rc, attr, this );
    }

    /**
     * Get the display name for the bean.  For FacesBackingBeans, this is simply the class name.
     */ 
    public String getDisplayName()
    {
        return getClass().getName();
    }

    /**
     * Reinitialize the bean for a new request.  Used by the framework; normally should not be called directly.
     */ 
    public void reinitialize( HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
    {
        super.reinitialize( request, response, servletContext );
        
        if ( _pageInputs == null )
        {
            Map map = InternalUtils.getActionOutputMap( request, false );
            if ( map != null ) _pageInputs = Collections.unmodifiableMap( map );
        }
        
        //
        // Initialize the page flow field.
        //
        Field pageFlowMemberField = getCachedInfo().getPageFlowMemberField();
        
        // TODO: should we add a compiler warning if this field isn't transient?  All this reinitialization logic is
        // for the transient case.
        if ( fieldIsUninitialized( pageFlowMemberField ) )
        {
            PageFlowController pfc = PageFlowUtils.getCurrentPageFlow( request, servletContext );
            initializeField( pageFlowMemberField, pfc );
        }
        
        //
        // Initialize the shared flow fields.
        //
        CachedSharedFlowRefInfo.SharedFlowFieldInfo[] sharedFlowMemberFields =
                getCachedInfo().getSharedFlowMemberFields();
        
        if ( sharedFlowMemberFields != null )
        {
            for ( int i = 0; i < sharedFlowMemberFields.length; i++ )
            {
                CachedSharedFlowRefInfo.SharedFlowFieldInfo fi = sharedFlowMemberFields[i];
                Field field = fi.field;
                
                if ( fieldIsUninitialized( field ) )
                {
                    Map/*< String, SharedFlowController >*/ sharedFlows = PageFlowUtils.getSharedFlows( request );
                    String name = fi.sharedFlowName;
                    SharedFlowController sf =
                            name != null ? ( SharedFlowController ) sharedFlows.get( name ) : PageFlowUtils.getGlobalApp( request );
                    
                    if ( sf != null )
                    {
                        initializeField( field, sf );
                    }
                    else
                    {
                        _log.error( "Could not find shared flow with name \"" + fi.sharedFlowName
                                    + "\" to initialize field " + field.getName() + " in " + getClass().getName() );
                    }
                }
            }
        }
    }

    /**
     * Get a page input that was passed from a Page Flow action as an "action output".
     * 
     * @param pageInputName the name of the page input.  This is the same as the name of the "action output".
     * @return the value of the page input, or <code>null</code> if the given one does not exist.
     */ 
    protected Object getPageInput( String pageInputName )
    {
        return _pageInputs != null ? _pageInputs.get( pageInputName ) : null;
    }
    
    /**
     * Get the map of all page inputs that was passed from a Page Flow action as "action outputs".
     * 
     * @return a Map of page-input-name (String) to page-input-value (Object).
     */ 
    public Map getPageInputMap()
    {
        return _pageInputs;
    }
    
    private CachedFacesBackingInfo getCachedInfo()
    {
        ClassLevelCache cache = ClassLevelCache.getCache( getClass() );
        CachedFacesBackingInfo info = ( CachedFacesBackingInfo ) cache.getCacheObject( CACHED_INFO_KEY );
        
        if ( info == null )
        {
            info = new CachedFacesBackingInfo( getClass(), getServletContext() );
            cache.setCacheObject( CACHED_INFO_KEY, info );
        }
        
        return info;
    }
    
    /**
     * Callback that is invoked when this backing bean is restored as the page itself is restored through a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward} or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction} with
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward#navigateTo() navigateTo}=
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage Jpf.NavigateTo.currentPage}
     * or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward#navigateTo() navigateTo}=
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage Jpf.NavigateTo.previousPage}.
     */ 
    protected void onRestore()
    {
    }
    
    /**
     * Restore this bean (set it as the current one from some dormant state).  This is a framework-invoked method that
     * should not normally be called directly.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param servletContext the current ServletContext.
     */ 
    public void restore( HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
    {
        reinitialize( request, response, servletContext );
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, response );
        String attrName =
                ScopedServletUtils.getScopedSessionAttrName( InternalConstants.FACES_BACKING_ATTR, unwrappedRequest );
        sh.setAttribute( rc, attrName, this );
        Map newActionOutputs = InternalUtils.getActionOutputMap( request, false );
        if ( newActionOutputs != null ) _pageInputs = newActionOutputs;
        onRestore();
    }
}
