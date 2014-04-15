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

import java.lang.reflect.Field;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.ServletContext;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ControllerConfig;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.netui.pageflow.config.PageFlowControllerConfig;
import org.apache.beehive.netui.pageflow.config.PageFlowExceptionConfig;
import org.apache.beehive.netui.pageflow.internal.CachedPageFlowInfo;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.CachedSharedFlowRefInfo;
import org.apache.beehive.netui.pageflow.internal.ViewRenderer;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.internal.cache.ClassLevelCache;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * Base class for controller logic, exception handlers, and state associated with a particular web directory path. 
 * The class is configured through the
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller} annotation.
 * </p>
 * 
 * <p>
 * When a page flow request (the page flow URI itself, or any ".do" or page URI in the directory path), arrives, an
 * instance of the associated PageFlowController class is set as the <i>current page flow</i>, and remains stored in the
 * session until a different one becomes active ("long lived" page flows stay in the session indefinitely;
 * see {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#longLived longLived}
 * on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}).
 * </p>
 * 
 * <p>
 * The page flow class handles <i>actions</i> that are most commonly raised by user interaction with pages.  The actions
 * are handled by <i>action methods</i> or <i>action annotations</i> that determine the next URI to be displayed, after
 * optionally performing arbitrary logic.
 * </p>
 * 
 * <p>
 * If the PageFlowController is a "nested page flow"
 * ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#nested nested} is set to <code>true</code>
 * on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}), then this
 * is a reusable, modular flow that can be "nested" during other flows.  It has entry points (actions with optional form
 * bean arguments), and exit points ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward} annotations
 * that have <code>returnAction</code> attributes).
 * </p>
 *
 * <p>
 * The page flow class also handles exceptions thrown by actions or during page execution
 * (see {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#catches catches} on
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}).  Unhandled exceptions are
 * handled in order by declared {@link SharedFlowController}s
 * (see {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs} on
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}).
 * </p>
 * 
 * <p>
 * Properties in the current page flow instance can be accessed from JSP 2.0-style expressions like this one:
 * <code>${pageFlow.someProperty}</code>.
 * </p>
 * 
 * <p>
 * There may only be one page flow in any package.
 * </p>
 * 
 * @see SharedFlowController
 */
public abstract class PageFlowController
        extends FlowController
        implements InternalConstants
{
    /**
     * A 'return-to="page"' forward brings the user back to the previous page. This object
     * stores information about the current state of affairs, such as the origin URI and
     * its ActionForm.
     */
    private PreviousPageInfo _previousPageInfo = null;
    private PreviousPageInfo _currentPageInfo = null;

    /**
     * A 'return-to="action"' forward reruns the previous action. This object stores the previous
     * action URI and its ActionForm.
     */
    private PreviousActionInfo  _previousActionInfo;

    private boolean _isOnNestingStack = false;
    private ViewRenderer _returnActionViewRenderer = null;

    private static final String REMOVING_PAGEFLOW_ATTR = InternalConstants.ATTR_PREFIX + "removingPageFlow";
    private static final String SAVED_PREVIOUS_PAGE_INFO_ATTR = InternalConstants.ATTR_PREFIX + "savedPrevPageInfo";
    private static final String CACHED_INFO_KEY = "cachedInfo";
    private static final Logger _log = Logger.getInstance( PageFlowController.class );

    /**
     *  The bean context associated with this request
     */
    ControlContainerContext _beanContext;

    /**
     * Default constructor.
     */
    protected PageFlowController()
    {
    }

    /**
     * Get the Struts module path for this page flow.
     * 
     * @return a String that is the Struts module path for this controller, and which is also
     *         the directory path from the web application root to this PageFlowController
     *         (not including the action filename).
     */
    public String getModulePath()
    {
        return getCachedInfo().getModulePath();
    }

    /**
     * Get the URI for addressing this PageFlowController.
     * 
     * @return a String that is the URI which will execute the begin action on this
     *         PageFlowController.
     */
    public String getURI()
    {
        return getCachedInfo().getURI();
    }

    /**
     * Tell whether this PageFlowController can be "nested", i.e., if it can be invoked from another page
     * flow with the intention of returning to the original one.  Page flows are declared to be nested by specifying
     * <code>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#nested nested}=true</code> on the
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller} annotation.
     * 
     * @return <code>true</code> if this PageFlowController can be nested.
     */
    protected boolean isNestable()
    {
        return InternalUtils.isNestable( getModuleConfig() );
    }

    /**
     * Tell whether this is a "long lived" page flow.  Once it is invoked, a long lived page flow is never
     * removed from the session unless {@link #remove} is called.  Navigating to another page flow hides
     * the current long lived controller, but does not remove it.
     */
    protected boolean isLongLived()
    {
        return InternalUtils.isLongLived( getModuleConfig() );
    }

    /**
     * Remove this instance from the session.  When inside a page flow action, {@link #remove} may be called instead.
     * This method is synchronized in order to maintain single threaded semantics for the Page Flow's
     * {@link #onDestroy(javax.servlet.http.HttpSession)} lifecycle method.
     */
    public synchronized void removeFromSession( HttpServletRequest request )
    {
        // This request attribute is used in persistInSession to prevent re-saving of this instance.
        request.setAttribute( REMOVING_PAGEFLOW_ATTR, this );

        if ( isLongLived() )
        {
            PageFlowUtils.removeLongLivedPageFlow( getModulePath(), request );
        }
        else
        {
            InternalUtils.removeCurrentPageFlow( request, getServletContext() );
        }
    }

    /**
     * Tell whether this is a PageFlowController.
     * 
     * @return <code>true</code>.
     */
    public boolean isPageFlow()
    {
        return true;
    }

    /**
     * Store this object in the user session, in the appropriate place.  Used by the framework; normally should not be
     * called directly.
     */
    public void persistInSession( HttpServletRequest request, HttpServletResponse response )
    {
        PageFlowController currentPageFlow = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );

        //
        // This code implicitly destroys the current page flow.  In order to prevent multiple threads
        // from executing inside the JPF at once, synchronize on it in order to complete the "destroy"
        // atomically.
        //
        if ( currentPageFlow != null && ! currentPageFlow.isOnNestingStack() )
        {
            synchronized ( currentPageFlow )
            {
                InternalUtils.setCurrentPageFlow( this, request, getServletContext() );
            }
        }
        //
        // Here, there is no previous page flow to syncyronize upon before destruction
        //
        else
        {
            InternalUtils.setCurrentPageFlow( this, request, getServletContext() );
        }
    }

    /**
     * <p>
     * Ensure that any changes to this object will be replicated in a cluster (for failover), even if the
     * replication scheme uses a change-detection algorithm that relies on
     * {@link javax.servlet.http.HttpSession#setAttribute(String, Object)} to be aware of changes.  This method is
     * used by the framework and should not be called directly in most cases.
     * </p>
     * <p>
     * Note, this method ultimately causes the Page Flow to be persisted in the {@link StorageHandler} for this web application.
     * </p>
     *
     * @param request the current {@link HttpServletRequest}
     */
    public void ensureFailover( HttpServletRequest request )
    {
        //
        // remove() puts the pageflow instance into a request attribute.  Make sure not to re-save this
        // instance if it's being removed.  Also, if the session is null (after having been invalidated
        // by the user), don't recreate it.
        //
        if ( request.getAttribute( REMOVING_PAGEFLOW_ATTR ) != this && request.getSession( false ) != null )
        {
            StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
            HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
            RequestContext rc = new RequestContext( unwrappedRequest, null );

            //
            // If this is a long-lived page flow, there are two attributes to deal with, and ensure that
            // both failover correctly.
            //
            if ( isLongLived() )
            {
                String longLivedAttrName = InternalUtils.getLongLivedFlowAttr( getModulePath() );
                longLivedAttrName = ScopedServletUtils.getScopedSessionAttrName( longLivedAttrName, unwrappedRequest );
                String currentLongLivedAttrName =
                    ScopedServletUtils.getScopedSessionAttrName( CURRENT_LONGLIVED_ATTR, unwrappedRequest );
                sh.ensureFailover( rc, longLivedAttrName, this );
                sh.ensureFailover( rc, currentLongLivedAttrName, getModulePath() );
            }
            //
            // This Page Flow is not long lived, so just the Page Flow itself needs to be added to the session.
            //
            else
            {
                String attrName = ScopedServletUtils.getScopedSessionAttrName( CURRENT_JPF_ATTR, unwrappedRequest );
                sh.ensureFailover( rc, attrName, this );
            }
        }
    }

    /**
     * @exclude
     */
    protected ActionForward internalExecute( ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response )
        throws Exception
    {
        initializeSharedFlowFields( request );
        return super.internalExecute( mapping, form, request, response );
    }

    private void initializeSharedFlowFields( HttpServletRequest request )
    {
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
                            name != null ?
                            ( SharedFlowController ) sharedFlows.get( name ) :
                            PageFlowUtils.getGlobalApp( request );

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
     * Get the a map of shared flow name to shared flow instance.
     * @return a Map of shared flow name (string) to shared flow instance ({@link SharedFlowController}).
     */
    protected Map/*< String, SharedFlowController >*/ getSharedFlows()
    {
        return PageFlowUtils.getSharedFlows( getRequest() );
    }

    /**
      * This is a non-property public accessor that will return the property value <code>sharedFlows</code>.
      * This is a non-property method because properties are exposed to databinding and that would expose
      * internal data structures to attack.
      * @return a Map of shared flow name (string) to shared flow instance ({@link SharedFlowController}).
      */
    public final Map theSharedFlows()
    {
        return getSharedFlows();
    }

    /**
     * Get a shared flow, based on its name as defined in this page flow's
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs}
     * attribute on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}.
     * To retrieve any shared flow based on its class name, use {@link PageFlowUtils#getSharedFlow}.
     * 
     * @param sharedFlowName the name of the shared flow, as in this page flows's
     *        {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs}
     *        attribute on the {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}
     *        annotation.
     * @return the {@link SharedFlowController} with the given name.
     */
    public SharedFlowController getSharedFlow( String sharedFlowName )
    {
        return ( SharedFlowController ) PageFlowUtils.getSharedFlows( getRequest() ).get( sharedFlowName );
    }

    /**
     * Remove a shared flow from the session, based on its name as defined in this page flow's
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs}
     * attribute on {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}.
     * To remove any shared flow based on its class name, use {@link PageFlowUtils#removeSharedFlow}.
     * 
     * @param sharedFlowName the name of the shared flow, as in this page flows's
     *        {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller#sharedFlowRefs sharedFlowRefs}
     *        attribute on the {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}
     *        annotation.
     */
    public void removeSharedFlow( String sharedFlowName )
    {
        SharedFlowController sf = getSharedFlow( sharedFlowName );
        if ( sf != null )
            sf.removeFromSession( getRequest() );
    }

    /**
     * This is a framework method for initializing a newly-created page flow, and should not normally be called
     * directly.
     */
    public final synchronized void create( HttpServletRequest request,
                                           HttpServletResponse response,
                                           ServletContext servletContext )
    {
        reinitialize( request, response, servletContext );
        initializeSharedFlowFields( request );

        if ( isNestable() )
        {
            // Initialize a ViewRenderer for exiting the nested page flow.  This is used (currently) as part of popup
            // window support -- when exiting a popup nested page flow, a special view renderer writes out javascript
            // that maps output values to the original window and closes the popup window.
            String vrClassName = request.getParameter( InternalConstants.RETURN_ACTION_VIEW_RENDERER_PARAM );

            if ( vrClassName != null )
            {
                ViewRenderer vr =
                        ( ViewRenderer ) DiscoveryUtils.newImplementorInstance( vrClassName, ViewRenderer.class );

                if ( vr != null )
                {
                    vr.init( request );
                    PageFlowController nestingPageFlow = PageFlowUtils.getCurrentPageFlow(request, servletContext);
                    nestingPageFlow.setReturnActionViewRenderer(vr);
                }
            }
        }

        super.create( request, response, servletContext );
    }

    /**
     * Get the "resource taxonomy": a period-separated list that starts with the current
     * web application name, continues through all of this PageFlowController's parent directories,
     * and ends with this PageFlowController's class name.
     */
    protected String getTaxonomy()
    {
        assert getRequest() != null : "this method can only be called during execute()";
        String contextPath = getRequest().getContextPath();
        assert contextPath.startsWith( "/" ) : contextPath;
        return contextPath.substring( 1 ) + '.' + getClass().getName();
    }

    /**
     * Get the submitted form bean from the most recent action execution in this PageFlowController.
     * <p>
     * <i>Note: if the current page flow does not contain a
     * </i>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}<i> or a
     * </i>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}<i> with
     * </i><code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction Jpf.NavigateTo.previousAction}</code><i>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this
     * situation, add the following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousAction()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @deprecated This method may return an <code>ActionForm</code> wrapper when the form bean type does not extend
     *             <code>ActionForm</code>.  Use {@link #getPreviousFormBean} instead.
     * @return the ActionForm instance from the most recent action execution, or <code>null</code>
     *         if there was no form bean submitted.
     * @see #getPreviousPageInfo
     * @see #getCurrentPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousActionURI
     * @see #getPreviousForwardPath
     * @see #getCurrentForwardPath
     */
    protected ActionForm getPreviousForm()
    {
        checkPreviousActionInfoDisabled();
        return _previousActionInfo != null ? _previousActionInfo.getForm() : null;
    }

    /**
     * Get the submitted form bean from the most recent action execution in this PageFlowController.
     * <p>
     * <i>Note: if the current page flow does not contain a
     * </i>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}<i> or a
     * </i>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}<i> with
     * </i><code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction Jpf.NavigateTo.previousAction}</code><i>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this
     * situation, add the following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousAction()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @return the form bean instance from the most recent action execution, or <code>null</code>
     *         if there was no form bean submitted.
     * @see #getPreviousPageInfo
     * @see #getCurrentPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousActionURI
     * @see #getPreviousForwardPath
     * @see #getCurrentForwardPath
     */
    protected Object getPreviousFormBean()
    {
        checkPreviousActionInfoDisabled();
        return _previousActionInfo != null ? InternalUtils.unwrapFormBean( _previousActionInfo.getForm() ) : null;
    }

    /**
     * Get the URI for the most recent action in this PageFlowController.
     * <p>
     * <i>Note: if the current page flow does not use a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction previousAction}</code>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this situation, add the
     * following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousAction()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @return a String that is the most recent URI.
     * @see #getPreviousPageInfo
     * @see #getCurrentPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousFormBean
     * @see #getPreviousForwardPath
     * @see #getCurrentForwardPath
     */
    protected String getPreviousActionURI()
    {
        checkPreviousActionInfoDisabled();
        return _previousActionInfo != null ? _previousActionInfo.getActionURI() : null;
    }

    /**
     * Get the webapp-relative URI for the most recent page (in this page flow) shown to the user.
     * <p>
     * <i>Note: if the current page flow does not use a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage currentPage}</code>
     * or <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousPage previousPage}</code>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this situation, add the
     * following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousPage()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @return a String that is the URI path for the most recent page shown to the user.
     * @see #getPreviousPageInfo
     * @see #getCurrentPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousActionURI
     * @see #getPreviousFormBean
     * @see #getPreviousForwardPath
     */
    public String getCurrentForwardPath()
    {
        PreviousPageInfo curPageInfo = getCurrentPageInfo();
        String path = null;

        if ( curPageInfo != null )
        {
            ActionForward curForward = curPageInfo.getForward();
            if ( curForward != null )
            {
                if ( curForward.getContextRelative() )
                {
                    path = curForward.getPath();
                }
                else
                {
                    path = getModulePath() + curForward.getPath();
                }
            }
        }
        return path;
    }

    /**
     * Get the webapp-relative URI for the previous page (in this page flow) shown to the user.
     * The previous page is the one shown before the most recent page.
     * <p>
     * <i>Note: if the current page flow does not use a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage currentPage}</code>
     * or <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousPage previousPage}</code>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this situation, add the
     * following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousPage()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @return a String that is the URI path for the previous page shown to the user.
     * @see #getPreviousPageInfo
     * @see #getCurrentPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousActionURI
     * @see #getPreviousFormBean
     * @see #getCurrentForwardPath
     */
    protected String getPreviousForwardPath()
    {
        PreviousPageInfo prevPageInfo = getPreviousPageInfo();

        if ( prevPageInfo != null )
        {
            ActionForward prevForward = prevPageInfo.getForward();
            return prevForward != null ? prevForward.getPath() : null;
        }
        else
        {
            return null;
        }
    }

    /**
     * Get a legacy PreviousPageInfo.
     * @deprecated This method will be removed without replacement in the next release.
     */
    public final PreviousPageInfo getPreviousPageInfoLegacy( PageFlowController curJpf, HttpServletRequest request )
    {
        if (PageFlowRequestWrapper.get(request).isReturningFromNesting())
        {
            return getCurrentPageInfo();
        }
        else
        {
            return getPreviousPageInfo();
        }
    }

    /**
     * Get information about the most recent page (in this page flow) shown to the user.
     * <p>
     * <i>Note: if the current page flow does not use a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage currentPage}</code>
     * or <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousPage previousPage}</code>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this situation, add the
     * following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousPage()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @return a PreviousPageInfo with information about the most recent page shown to the user.
     * @see #getPreviousPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousActionURI
     * @see #getPreviousFormBean
     * @see #getPreviousForwardPath
     * @see #getCurrentForwardPath
     */
    protected final PreviousPageInfo getCurrentPageInfo()
    {
        checkPreviousPageInfoDisabled();

        if ( _currentPageInfo != null )
        {
            // Allows it to reconstruct transient members after session failover
            _currentPageInfo.reinitialize( this );
        }

        return _currentPageInfo;
    }

   /**
     * This is a non-property public accessor that will return the property value <code>currrentPageInfo</code>.
     * This is a non-property method because properties are exposed to databinding and that would expose
     * internal data structures to attack.
     * @return a PreviousPageInfo with information about the most recent page shown to the user.
     */
    public final PreviousPageInfo theCurrentPageInfo()
    {
        return getCurrentPageInfo();
    }

    /**
     * Get information about the previous page (in this page flow) shown to the user.  The previous
     * page is the one shown before the most recent page.
     * <p>
     * <i>Note: if the current page flow does not use a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage currentPage}</code>
     * or <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousPage previousPage}</code>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this situation, add the
     * following method to the page flow:</i><br>
     * </blockquote>
     * 
     * @return a PreviousPageInfo with information about the previous page shown to the user.
     * @see #getCurrentPageInfo
     * @see #getPreviousActionInfo
     * @see #getPreviousActionURI
     * @see #getPreviousFormBean
     * @see #getPreviousForwardPath
     * @see #getCurrentForwardPath
     */
    protected final PreviousPageInfo getPreviousPageInfo()
    {
        checkPreviousPageInfoDisabled();

        PreviousPageInfo ret = _previousPageInfo != null ? _previousPageInfo : _currentPageInfo;

        if ( ret != null )
        {
            ret.reinitialize( this ); // Allows it to reconstruct transient members after session failover
        }

        return ret;
    }

    /**
      * This is a non-property public accessor that will return the property value <code>previousPageInfo</code>.
      * This is a non-property method because properties are exposed to databinding and that would expose
      * internal data structures to attack.
      * @return a PreviousPageInfo with information about the previous page shown to the user.
      */
    public final PreviousPageInfo thePreviousPageInfo()
    {
        return getPreviousPageInfo();
    }

    /**
     * Get information about the most recent action run in this page flow.
     * <p>
     * <i>Note: if the current page flow does not use a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward},
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward}
     * with <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction previousAction}</code>,
     * then this method will always return </i><code>null</code><i> by default.  To enable it in this situation, add the
     * following method to the page flow:</i><br>
     * <blockquote>
     *     <code>
     *     protected boolean alwaysTrackPreviousAction()<br>
     *     {<br>
     *     &nbsp;&nbsp;&nbsp;&nbsp;return true;<br>
     *     }<br>
     *     </code>
     * </blockquote>
     * 
     * @return a PreviousActionInfo with information about the most recent action run in this page flow.
     * @see #getPreviousPageInfo
     * @see #getCurrentPageInfo
     * @see #getPreviousActionURI
     * @see #getPreviousFormBean
     * @see #getPreviousForwardPath
     * @see #getCurrentForwardPath
     */
    protected final PreviousActionInfo getPreviousActionInfo()
    {
        checkPreviousActionInfoDisabled();
        return _previousActionInfo;
    }

    /**
      * This is a non-property public accessor that will return the property value <code>previousActionInfo</code>.
      * This is a non-property method because properties are exposed to databinding and that would expose
      * internal data structures to attack.
      * @return a PreviousActionInfo with information about the most recent action run in this page flow.
      */
    public final PreviousActionInfo thePreviousActionInfo()
    {
        return getPreviousActionInfo();
    }


    private void checkPreviousActionInfoDisabled()
    {
        if ( isPreviousActionInfoDisabled() )
        {
            throw new IllegalStateException( "Previous action information has been disabled in this page flow.  Override alwaysTrackPreviousAction() to enable it." );
        }
    }

    private void checkPreviousPageInfoDisabled()
    {
        if ( isPreviousPageInfoDisabled() )
        {
            throw new IllegalStateException( "Previous page information has been disabled in this page flow.  Override alwaysTrackPreviousPage() to enable it." );
        }
    }

    /**
     * Get the display name of this page flow.
     * @return the display name (the URI) of this page flow.
     */
    public String getDisplayName()
    {
        return getURI();
    }

    public boolean isPreviousActionInfoDisabled()
    {
        if ( alwaysTrackPreviousAction() ) return false;

        ModuleConfig mc = getModuleConfig();
        ControllerConfig cc = mc.getControllerConfig();
        return cc instanceof PageFlowControllerConfig && ( ( PageFlowControllerConfig ) cc ).isReturnToActionDisabled();
    }

    public boolean isPreviousPageInfoDisabled()
    {
        if ( alwaysTrackPreviousPage() ) return false;

        ModuleConfig mc = getModuleConfig();
        ControllerConfig cc = mc.getControllerConfig();
        return cc instanceof PageFlowControllerConfig && ( ( PageFlowControllerConfig ) cc ).isReturnToPageDisabled();
    }

    /**
     * Called from {@link FlowController#execute}.
     */
    void savePreviousActionInfo( ActionForm form, HttpServletRequest request, ActionMapping mapping,
                                 ServletContext servletContext )
    {
        //
        // If previous-action is disabled (unused in this pageflow), just return.
        //
        if ( isPreviousActionInfoDisabled() ) return;
        String actionURI = InternalUtils.getDecodedServletPath( request );
        _previousActionInfo = new PreviousActionInfo( form, actionURI, request.getQueryString() );
    }

    /**
     * Store information about recent pages displayed.  This is a framework-invoked method that should not normally be
     * called directly.
     */
    public void savePreviousPageInfo( ActionForward forward, ActionForm form, ActionMapping mapping,
                                      HttpServletRequest request, ServletContext servletContext,
                                      boolean isSpecialForward )
    {
        if ( forward != null )
        {
            //
            // If previous-page is disabled (unused in this pageflow), or if we've already saved prevous-page info in
            // this request (for example, forward to foo.faces which forwards to foo.jsp), just return.
            //
            if ( request.getAttribute( SAVED_PREVIOUS_PAGE_INFO_ATTR ) != null || isPreviousPageInfoDisabled() ) return;

            String path = forward.getPath();
            int queryPos = path.indexOf( '?' );
            if ( queryPos != -1 ) path = path.substring( 0, queryPos );

            //
            // If a form bean was generated in this request, add it to the most recent PreviousPageInfo, so when we
            // go back to that page, the *updated* field values are restored (i.e., we don't revert to the values of
            // the form that was passed into the page originally).
            //
            if ( form != null && _currentPageInfo != null )
            {
                ActionForm oldForm = _currentPageInfo.getForm();
                if ( oldForm == null || oldForm.getClass().equals( form.getClass() ) )
                {
                    _currentPageInfo.setForm( form );
                    _currentPageInfo.setMapping( mapping );
                }
            }

            //
            // Only keep track of *pages* forwarded to -- not actions or pageflows.
            //
            if ( ! FileUtils.osSensitiveEndsWith( path, ACTION_EXTENSION ) )
            {
                //
                // Only save previous-page info if the page is within this pageflow.
                //
                if ( isLocalFile( forward ) ) // || PageFlowUtils.osSensitiveEndsWith( path, JPF_EXTENSION ) )
                {
                    _previousPageInfo = _currentPageInfo;
                    _currentPageInfo = new PreviousPageInfo( forward, form, mapping, request.getQueryString() );
                    request.setAttribute( SAVED_PREVIOUS_PAGE_INFO_ATTR, Boolean.TRUE );
                }
            }
        }
    }

    private boolean isLocalFile( ActionForward forward )
    {
        String path = forward.getPath();

        if ( ! forward.getContextRelative() )
        {
            return path.indexOf( '/', 1 ) == -1;     // all paths in Struts start with '/'
        }
        else
        {
            String modulePath = getModulePath();

            if ( ! path.startsWith( modulePath ) )
            {
                return false;
            }
            else
            {
                return path.indexOf( '/', modulePath.length() + 1 ) == -1;
            }
        }
    }

    private boolean isOnNestingStack()
    {
        return _isOnNestingStack;
    }

    /**
     * Callback when this object is removed from the user session.  Causes {@link #onDestroy} to be called.  This is a
     * framework-invoked method that should not be called directly.
     */
    public void valueUnbound( HttpSessionBindingEvent event )
    {
        //
        // Unless this pageflow has been pushed onto the nesting stack, do the onDestroy() callback.
        //
        if ( ! _isOnNestingStack )
        {
            super.valueUnbound( event );
        }
    }

    void setIsOnNestingStack( boolean isOnNestingStack )
    {
        _isOnNestingStack = isOnNestingStack;
    }

    ActionForward forwardTo( ActionForward fwd, ActionMapping mapping, PageFlowExceptionConfig exceptionConfig,
                             String actionName, ModuleConfig altModuleConfig, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
    {
        ActionForward superFwd = super.forwardTo( fwd, mapping, exceptionConfig, actionName, altModuleConfig, form,
                                                  request, response, servletContext );

        //
        // Special case: the *only* way for a nested pageflow to nest itself is for it
        // to forward to itself as a .jpf.  Simply executing an action in the .jpf isn't
        // enough, obviously, since it's impossible to tell whether it should be executed
        // in the current pageflow or a new nested one.
        //
        if ( superFwd != null && InternalUtils.isNestable( getModuleConfig() ) )
        {
            boolean selfNesting = false;

            if ( superFwd.getContextRelative() )
            {
                if ( superFwd.getPath().equals( getURI() ) )
                {
                    selfNesting = true;
                }
            }
            else
            {
                if ( superFwd.getPath().equals( getStrutsLocalURI() ) )
                {
                    selfNesting = true;
                }
            }

            if ( selfNesting )
            {
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "Self-nesting page flow " + getURI() );
                }

                try
                {
                    // This will cause the right pageflow stack stuff to happen.
                    RequestContext rc = new RequestContext( request, response );
                    FlowControllerFactory.get( getServletContext() ).createPageFlow( rc, getClass() );
                }
                catch ( IllegalAccessException e )
                {
                    // This should never happen -- if we successfully created this page flow once, we can do it again.
                    assert false : e;
                    _log.error( e );
                }
                catch ( InstantiationException e )
                {
                    _log.error( "Could not create PageFlowController instance of type " + getClass().getName(), e );
                }
            }
        }

        return superFwd;
    }

    private String getStrutsLocalURI()
    {
        String className = getClass().getName();
        int lastDot = className.lastIndexOf( '.' );
        InternalStringBuilder ret = new InternalStringBuilder( "/" );
        return ret.append( className.substring( lastDot + 1 ) ).append( PAGEFLOW_EXTENSION ).toString();
    }

    private CachedPageFlowInfo getCachedInfo()
    {
        ClassLevelCache cache = ClassLevelCache.getCache( getClass() );
        CachedPageFlowInfo info = ( CachedPageFlowInfo ) cache.getCacheObject( CACHED_INFO_KEY );

        if ( info == null )
        {
            info = new CachedPageFlowInfo( getClass(), getServletContext() );
            cache.setCacheObject( CACHED_INFO_KEY, info );
        }

        return info;
    }

    final void beforePage()
    {
        //
        // We may need to save the previous page info if the page was called directly (not forwarded through an action)
        // and we do not yet have the forward path in the page flow or it is a different path.
        //
        if ( ! isPreviousPageInfoDisabled() )
        {
            HttpServletRequest request = getRequest();
            String relativeUri = InternalUtils.getDecodedServletPath( request );

            String path = getCurrentForwardPath();
            if ( path == null || ! path.equals( relativeUri ) )
            {
                ActionForward actionForward = new ActionForward( relativeUri );
                actionForward.setContextRelative( true );
                actionForward.setRedirect( false );
                savePreviousPageInfo( actionForward, null, null, request, getServletContext(), false );
            }
        }
    }

    /**
     * @exclude
     */
    public ActionForward exitNesting( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping,
                                      ActionForm form )
    {
        // Get any return-action view renderer from the original ("nesting") page flow.  If there is one, we'll
        // use it later to render the view.
        PageFlowController nestingPageFlow = PageFlowUtils.getNestingPageFlow(request, getServletContext());
        ViewRenderer returnActionViewRenderer = nestingPageFlow.getReturnActionViewRenderer();

        if ( returnActionViewRenderer != null )
        {
            PageFlowRequestWrapper.get( request ).setViewRenderer( returnActionViewRenderer );
            nestingPageFlow.setReturnActionViewRenderer(null);  // we don't need it anymore
        }

        PerRequestState prevState = setPerRequestState( new PerRequestState( request, response, mapping ) );

        try
        {
            onExitNesting();
        }
        catch ( Throwable th )
        {
            try
            {
                return handleException( th, mapping, form, request, response );
            }
            catch ( Exception e )
            {
                _log.error( "Exception thrown while handling exception.", e );
            }
        }
        finally
        {
            setPerRequestState( prevState );
        }

        return null;
    }

    /**
     * Callback that is invoked when this controller instance is exiting nesting through a return action.
     * {@link FlowController#getRequest}, {@link FlowController#getResponse}, {@link FlowController#getSession}
     * may all be used during this method.
     */
    protected void onExitNesting()
            throws Exception
    {
    }

    private ViewRenderer getReturnActionViewRenderer() {
        return _returnActionViewRenderer;
    }

    private void setReturnActionViewRenderer(ViewRenderer returnActionViewRenderer) {
        _returnActionViewRenderer = returnActionViewRenderer;
    }
}