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

import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.pageflow.config.PageFlowExceptionConfig;
import org.apache.beehive.netui.pageflow.handler.ActionForwardHandler;
import org.apache.beehive.netui.pageflow.handler.ExceptionsHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.LoginHandler;
import org.apache.beehive.netui.pageflow.internal.*;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.internal.cache.ClassLevelCache;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.TokenProcessor;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.security.Principal;


/**
 * Base class for user-written flow controllers - {@link PageFlowController}s and {@link SharedFlowController}s.
 */
public abstract class FlowController extends PageFlowManagedObject
        implements PageFlowConstants, ActionResolver
{
    private static final Logger _log = Logger.getInstance( FlowController.class );

    private static final String ONCREATE_EXCEPTION_FORWARD = InternalConstants.ATTR_PREFIX + "onCreateException";
    private static final String CACHEID_ACTION_METHODS = InternalConstants.ATTR_PREFIX + "actionMethods";
    private static final int DEFAULT_MAX_CONCURRENT_REQUEST_COUNT = 4;
    private static final String MAX_CONCURRENT_REQUESTS_PARAM = "pageflow-max-concurrent-requests";
    private static final int EXCEEDED_MAX_CONCURRENT_REQUESTS_ERRORCODE = 503;
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final ActionForward NULL_ACTION_FORWARD = new ActionForward();
    private static final TokenProcessor TOKEN_PROCESSOR = TokenProcessor.getInstance();

    /**
     * The system default Locale.
     * 
     * @deprecated Use {@link #getDefaultLocale}.
     */
    protected static Locale defaultLocale = DEFAULT_LOCALE;


    static class PerRequestState
    {
        private HttpServletRequest _request;
        private HttpServletResponse _response;
        private ActionMapping _actionMapping;

        public PerRequestState( HttpServletRequest request, HttpServletResponse response, ActionMapping actionMapping )
        {
            _request = request;
            _response = response;
            _actionMapping = actionMapping;
        }

        public HttpServletRequest getRequest()
        {
            return _request;
        }

        public HttpServletResponse getResponse()
        {
            return _response;
        }

        public ActionMapping getActionMapping()
        {
            return _actionMapping;
        }
    }

    /**
     * Stores per-request state, which is <i>only valid during calls to {@link FlowController#execute} or {@link FlowController#handleException}</i>.
     */
    private transient PerRequestState _perRequestState;

    /**
     * Cached reference to the associated Struts ModuleConfig.
     */
    private transient ModuleConfig _moduleConfig = null;

    /**
     * @see #incrementRequestCount
     */
    private transient int _requestCount = 0;

    /**
     * @see #incrementRequestCount
     */
    private static int _maxConcurrentRequestCount = -1;

    /**
     * Default constructor.
     */
    protected FlowController()
    {
    }

    /**
     * Reinitialize the object for a new request.  Used by the framework; normally should not be called directly.
     */
    public void reinitialize( HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
    {
        //
        // Cache the associated ModuleConfig.  This is used throughout the code, in places where the request
        // isn't available to do a lazy initialization.
        //
        super.reinitialize( request, response, servletContext );
    }

    /**
     * Log in the user, using "weak" username/password authentication.  Goes through a custom {@link LoginHandler}, if
     * one has been configured in beehive-netui-config.xml.
     *
     * @param username the user's login name
     * @param password the user's password
     * 
     * @exception LoginException if the authentication failed
     */
    public void login( String username, String password )
        throws LoginException
    {
        LoginHandler lh = Handlers.get( getServletContext() ).getLoginHandler();
        lh.login( getHandlerContext(), username, password );
    }

    /**
     * Log out the current user.  Goes through a custom {@link LoginHandler}, if one has been configured in
     * beehive-netui-config.xml.
     * 
     * @param invalidateSessions if true, the session is invalidated (on all single-signon webapps); 
     *            otherwise the session and its data are left intact (except for authentication
     *            information used internally by the server).  To invalidate the session in only the
     *            current webapp, set this parameter to <code>false</code> and call
     *            {@link FlowController#getSession}.invalidate().
     */
    public void logout( boolean invalidateSessions )
    {
        LoginHandler lh = Handlers.get( getServletContext() ).getLoginHandler();
        lh.logout( getHandlerContext(), invalidateSessions );
    }

    /**
     * Get the current logged-in user.  Goes through a custom {@link LoginHandler}, if one has been configured in
     * beehive-netui-config.xml.
     * 
     * @return the current logged-in <code>Principal</code>, or <code>null</code> if there is no logged-in user.
     */
    public Principal getUserPrincipal()
    {
        LoginHandler lh = Handlers.get( getServletContext() ).getLoginHandler();
        return lh.getUserPrincipal(getHandlerContext());
    }

    /**
     * Tell whether the current logged-in user is a member of a given role.  Goes through a custom {@link LoginHandler},
     * if one has been configured in beehive-netui-config.xml.
     * 
     * @param roleName the name of the role to test.
     * @return <code>true</code> if there is a current logged-in user and the user is a member of the given role.
     * @see #getUserPrincipal
     */
    public boolean isUserInRole(String roleName)
    {
        LoginHandler lh = Handlers.get( getServletContext() ).getLoginHandler();
        return lh.isUserInRole(getHandlerContext(), roleName);
    }
    
    /**
     * Send a Page Flow error to the browser.
     * 
     * @deprecated Use {@link FlowController#sendError(String, HttpServletRequest, HttpServletResponse)} instead.
     * @param errText the error message to display.
     * @param response the current HttpServletResponse.
     */
    protected void sendError( String errText, HttpServletResponse response )
        throws IOException
    {
        sendError( errText, null, response );
    }

    /**
     * Send a Page Flow error to the browser.
     * 
     * @param errText the error message to display.
     * @param response the current HttpServletResponse.
     */
    protected void sendError( String errText, HttpServletRequest request, HttpServletResponse response )
        throws IOException
    {
        InternalUtils.sendError( "PageFlow_Custom_Error", null, request, response,
                                 new Object[]{ getDisplayName(), errText } );
    }

    /**
     * Handle the given exception - invoke user code if appropriate and return a destination URI.
     * 
     * @param ex the Exception to handle.
     * @param mapping the Struts action mapping for current Struts action being processed.
     * @param form the form-bean (if any) associated with the Struts action being processed.  May be null.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return a Struts ActionForward object that specifies the URI that should be displayed.
     * @throws ServletException if another Exception is thrown during handling of <code>ex</code>.
     */
    public synchronized ActionForward handleException( Throwable ex, ActionMapping mapping,
                                                       ActionForm form, HttpServletRequest request,
                                                       HttpServletResponse response )
        throws IOException, ServletException
    {
        PerRequestState prevState = setPerRequestState( new PerRequestState( request, response, mapping ) );

        try
        {
            ExceptionsHandler eh = Handlers.get( getServletContext() ).getExceptionsHandler();
            FlowControllerHandlerContext context = getHandlerContext();

            // First, put the exception into the request (or other applicable context).
            Throwable unwrapped = eh.unwrapException( context, ex );
            eh.exposeException( context, unwrapped, mapping );
            return eh.handleException( context, unwrapped, mapping, form );
        }
        finally
        {
            setPerRequestState( prevState );
        }
    }

    /**
     * Get the name of the current action being executed.  This call is only valid
     * during {@link FlowController#execute} (where any user action method is invoked), and during the lifecycle
     * methods {@link FlowController#beforeAction} and {@link FlowController#afterAction}.
     * 
     * @return the name of the current action being executed.
     * @throws IllegalStateException if this method is invoked outside of action method
     *             execution (i.e., outside of the call to {@link FlowController#execute}, and outside of
     *             {@link FlowController#onCreate}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */

    protected String getCurrentActionName()
    {
        return InternalUtils.getActionName( getActionMapping() );
    }

    /**
     * Perform decision logic to determine the next URI to be displayed.
     * 
     * @param mapping the Struts ActionMapping for the current action being processed.
     * @param form the form-bean (if any) associated with the Struts action being processed.  May be null.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return a Struts ActionForward object that specifies the next URI to be displayed.
     * @throws Exception if an Exception was thrown during user action-handling code.
     */
    public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                  HttpServletResponse response )
            throws Exception
    {
        //
        // Don't actually run the action (and perform the associated synchronization) if there are too many
        // concurrent requests to this instance.
        //
        if ( incrementRequestCount( request, response, getServletContext() ) )
        {
            try
            {
                // netui:sync-point
                synchronized ( this )
                {
                    ActionForward ret = null;

                    // establish the control context for running the beginAction, Action, afterAction code
                    PageFlowControlContainer pfcc = null;
                    try {
                        pfcc = PageFlowControlContainerFactory.getControlContainer(request,getServletContext());
                        pfcc.beginContextOnPageFlow(this,request,response, getServletContext());
                    }
                    catch (Exception e) {
                        return handleException(e, mapping, form, request, response);
                    }

                    try {
                        // execute the beginAction, Action, afterAction code
                        ret = internalExecute( mapping, form, request, response );
                    }
                    finally {
                        try {
                            pfcc.endContextOnPageFlow(this);
                        }
                        catch (Exception e) {
                            // if already handling an exception during execute, then just log
                            PageFlowRequestWrapper rw = PageFlowRequestWrapper.get(request);
                            Throwable alreadyBeingHandled = rw.getExceptionBeingHandled();
                            if (alreadyBeingHandled != null) {
                                _log.error( "Exception thrown while ending context on page flow in execute()", e );
                            }
                            else {
                                return handleException(e, mapping, form, request, response);
                            }
                        }
                    }
                    return ret;
                }
            }
            finally
            {
                decrementRequestCount( request );
            }
        }
        else
        {
            return null;    // error was written to the response by incrementRequestCount()
        }
    }

    /**
     * Internal method used to execute an action on a FlowController.  This is where the magic generally happens as
     * the {@link #beforeAction()}, login, action execution (simple and normal), and {@link #afterAction()} are
     * all executed by this method.  This method is considered <i>internal</i> and should not be invoked direclty.
     */
    protected ActionForward internalExecute( ActionMapping mapping,
                                             ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response )
        throws Exception
    {
        ServletContainerAdapter sca = AdapterManager.getServletContainerAdapter( getServletContext() );
        PageFlowEventReporter eventReporter = sca.getEventReporter();
        RequestContext requestContext = new RequestContext( request, response );
        eventReporter.actionRaised( requestContext, this, mapping, form );
        long startTime = System.currentTimeMillis();

        //
        // If we handled an exception in onCreate, just forward to the result of that.
        //
        ActionForward onCreateFwd = ( ActionForward ) request.getAttribute( ONCREATE_EXCEPTION_FORWARD );

        if ( onCreateFwd != null )
        {
            return onCreateFwd == NULL_ACTION_FORWARD ? null : onCreateFwd;
        }


        PageFlowUtils.setActionURI( request );

        //
        // First change the actionPath (path) so that it lines up with our naming convention
        // for action methods.
        //
        boolean gotPastBeforeAction = false;
        ServletContext servletContext = getServletContext();
        PerRequestState prevState = setPerRequestState( new PerRequestState( request, response, mapping ) );

        try
        {
            //
            // beforeAction callback
            //
            beforeAction();
            gotPastBeforeAction = true;

            PageFlowActionMapping pfActionMapping =
                    mapping instanceof PageFlowActionMapping ? ( PageFlowActionMapping ) mapping : null;
            Object unwrappedForm = InternalUtils.unwrapFormBean( form );

            //
            // mapping.isOverloaded() means it's the base mapping for a set of overloaded mappings.
            // Find the one appropriate to the passed-in form.
            //
            if ( unwrappedForm != null && pfActionMapping != null )
            {
                if ( pfActionMapping.isOverloaded() )
                {
                    String mappingPath = pfActionMapping.getPath();

                    //
                    // Try the form class and all superclasses to get an overloaded action path.
                    //
                    for ( Class i = unwrappedForm.getClass(); i != null; i = i.getSuperclass() )
                    {
                        String formQualifiedActionPath = getFormQualifiedActionPath( i, mappingPath );
                        ActionConfig cf = pfActionMapping.getModuleConfig().findActionConfig( formQualifiedActionPath );

                        if ( cf != null )
                        {
                            assert cf instanceof PageFlowActionMapping : cf.getClass().getName();

                            if ( _log.isDebugEnabled() )
                            {
                                _log.debug( "Found form-specific mapping " + cf.getPath() +
                                           " -- choosing this one over current mapping " + mappingPath );
                            }

                            pfActionMapping = ( PageFlowActionMapping ) cf;
                            mapping = pfActionMapping;
                            break;
                        }
                    }
                }
            }

            String actionName = InternalUtils.getActionName( mapping );

            //
            // Check whether isLoginRequired=true for this action.
            //
            LoginHandler loginHandler = Handlers.get( getServletContext() ).getLoginHandler();

            if ( pfActionMapping != null && pfActionMapping.isLoginRequired()
                 && loginHandler.getUserPrincipal( getHandlerContext() ) == null )
            {
                NotLoggedInException ex = createNotLoggedInException( actionName, request );
                return handleException( ex, mapping, form, request, response );
            }

            //
            // Now delegate to the appropriate action method, or if it's a simple action, handle it that way.
            //
            ActionForward retVal;
            if ( pfActionMapping != null && pfActionMapping.isSimpleAction() )
            {
                retVal = handleSimpleAction( pfActionMapping, form, request, servletContext );
            }
            else
            {
                retVal = getActionMethodForward( actionName, unwrappedForm, request, response, mapping );
            }

            ActionForward ret =
                    forwardTo(retVal, mapping, null, actionName, null, form, request, response, servletContext);
            long timeTaken = System.currentTimeMillis() - startTime;
            eventReporter.actionSuccess( requestContext, this, mapping, form, ret, timeTaken );
            return ret;
        }
        catch ( Exception e )
        {
            //
            // Even though we handle any Throwable thrown by the user's action method, we don't need
            // to catch Throwable here, because anything thrown by the action method will be wrapped
            // in an InvocationTargetException.  Any Error (or other Throwable) that appears here
            // should not be handled by handleException() -- it's probably a framework problem and
            // should bubble out to the container.
            //
            return handleException( e, mapping, form, request, response );
        }
        finally
        {
            try
            {
                ActionForward overrideReturn = null;

                if ( gotPastBeforeAction )
                {
                    //
                    // afterAction callback
                    //
                    try
                    {
                        afterAction();
                    }
                    catch ( Throwable th )
                    {
                        overrideReturn = handleException( th, mapping, form, request, response );
                    }
                }

                //
                // Store information on this action for use with navigateTo=Jpf.NavigateTo.previousAction.
                //
                savePreviousActionInfo( form, request, mapping, getServletContext() );

                if ( overrideReturn != null )
                {
                    return overrideReturn;
                }
            }
            finally
            {
                setPerRequestState( prevState );
            }
        }
    }

    ActionForward forwardTo( ActionForward fwd, ActionMapping mapping, PageFlowExceptionConfig exceptionConfig,
                             String actionName, ModuleConfig altModuleConfig, ActionForm form,
                             HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
    {
        // This method is overridden in PageFlowController. Even though we're just delegating here, we can't remove it.
        ActionForwardHandler handler = Handlers.get( servletContext ).getActionForwardHandler();
        FlowControllerHandlerContext context = new FlowControllerHandlerContext( request, response, this );
        return handler.processForward( context, fwd, mapping, exceptionConfig, actionName, altModuleConfig, form );
    }

    NotLoggedInException createNotLoggedInException( String actionName, HttpServletRequest request )
    {
        if ( InternalUtils.sessionExpired( request ) )
        {
            return new LoginExpiredException( actionName, this );
        }
        else
        {
            return new NotLoggedInException( actionName, this );
        }
    }

    /**
     * Initialize after object creation.  This is a framework-invoked method; it should not normally be called directly.
     */
    public synchronized void create( HttpServletRequest request,
                                     HttpServletResponse response,
                                     ServletContext servletContext )
    {
        PerRequestState prevState = setPerRequestState( new PerRequestState( request, response, null ) );

        try
        {
            try
            {
                // we start the context on the bean so user control can run against Controls in the onCreate() method
                super.create( request, response, servletContext );
            }
            catch ( Throwable th )
            {
                try
                {
                    _log.info( "Handling exception in onCreate(), FlowController " + this, th );
                    reinitialize(request, response, servletContext);
                    ActionForward fwd = handleException( th, null, null, request, response );
                    if ( fwd == null ) fwd = NULL_ACTION_FORWARD;
                    request.setAttribute( ONCREATE_EXCEPTION_FORWARD, fwd );
                }
                catch ( Exception e )
                {
                    _log.error( "Exception thrown while handling exception in onCreate(): " + e.getMessage(), th );
                }
            }
        }
        finally
        {
            if(prevState != null)
                setPerRequestState( prevState );

            PageFlowControlContainer pfcc = PageFlowControlContainerFactory.getControlContainer(request,getServletContext());
            pfcc.endContextOnPageFlow(this);
        }

        PageFlowEventReporter er = AdapterManager.getServletContainerAdapter( servletContext ).getEventReporter();
        RequestContext requestContext = new RequestContext( request, response );
        er.flowControllerCreated( requestContext, this );
    }

    /**
     * Internal destroy method that is invoked when this object is being removed from the session.  This is a
     * framework-invoked method; it should not normally be called directly.
     */
    void destroy( HttpSession session )
    {
        onDestroy();    // for backwards compatiblity
        super.destroy( session );

        //
        // We may have lost our transient ServletContext reference.  Try to get the ServletContext reference from the
        // HttpSession object if necessary.
        //
        ServletContext servletContext = getServletContext();
        if ( servletContext == null && session != null ) servletContext = session.getServletContext();

        if ( servletContext != null )
        {
            PageFlowEventReporter er = AdapterManager.getServletContainerAdapter( servletContext ).getEventReporter();
            er.flowControllerDestroyed( this, session );
        }
    }

    /**
     * Get the Struts module path for this controller.
     * 
     * @return a String that is the Struts module path for this controller.
     */
    public abstract String getModulePath();

    /**
     * Callback that occurs before any user action method is invoked.  {@link FlowController#getRequest},
     * {@link FlowController#getResponse}, {@link FlowController#getSession}, and
     * {@link FlowController#getActionMapping} may all be used during this method.  The action to be run can be
     * discovered by calling {@link ActionMapping#getPath} on the value returned from
     * {@link FlowController#getActionMapping}.
     */
    protected synchronized void beforeAction()
        throws Exception
    {
    }

    /**
     * Callback that occurs after any user action method is invoked.  {@link FlowController#getRequest},
     * {@link FlowController#getResponse}, {@link FlowController#getSession}, and 
     * {@link FlowController#getActionMapping} may all be used during this method.  The action that was run can be
     * discovered by calling {@link ActionMapping#getPath} on the value returned from
     * {@link FlowController#getActionMapping}.
     */
    protected synchronized void afterAction()
        throws Exception
    {
    }

    /**
     * Callback that is invoked when this controller instance is created.  {@link FlowController#getRequest},
     * {@link FlowController#getResponse}, {@link FlowController#getSession} may all be used during this method.
     */
    protected void onCreate()
        throws Exception
    {
    }

    /**
     * Callback that is invoked when this controller instance is "destroyed", i.e., removed from the user session. 
     * {@link FlowController#getRequest}, {@link FlowController#getResponse}, and {@link FlowController#getActionMapping}
     * may <i>not</i> be used during this method, since it may be called due to session termination outside of a
     * request.  {@link FlowController#getSession} also may not be used, but the session is passed as an argument
     * to {@link FlowController#onDestroy(HttpSession)}, which should be used in place of this method.
     * <br>
     * Note that this method is <strong>not synchronized</strong>.  It is dangerous to synchronize your override of
     * this method because it is invoked during a callback from the Servlet container.  Depending on the container,
     * synchronization here can cause deadlocks.
     *
     * @deprecated {@link FlowController#onDestroy(HttpSession)} should be used instead. 
     */
    protected void onDestroy()
    {
    }

    /**
     * Callback that is invoked when this controller instance is "destroyed", i.e., removed from the user session. 
     * {@link FlowController#getRequest}, {@link FlowController#getResponse}, and {@link FlowController#getActionMapping}
     * may <i>not</i> be used during this method, since it may be called due to session termination outside of a
     * request.  {@link FlowController#getSession} also may not be used, but the session is passed as an argument.
     * <br>
     * Note that this method is <strong>not synchronized</strong>.  It is dangerous to synchronize your override of
     * this method because it is invoked during a callback from the Servlet container.  Depending on the container,
     * synchronization here can cause deadlocks.
     */
    protected void onDestroy( HttpSession session )
    {
    }

    /**
     * Get a legacy PreviousPageInfo.
     * @deprecated This method will be removed without replacement in a future release.
     */
    public abstract PreviousPageInfo getPreviousPageInfoLegacy( PageFlowController curJpf, HttpServletRequest request );

    /**
     * Get an action handler method of the given name/signature.
     *  
     * @param methodName the name of the action handler method to query.
     * @param argType the type of the argument to the action handler method; if <code>null</code>,
     *            the method takes no arguments.
     * @return the desired Method, or <code>null</code> if it doesn't exist.
     */
    protected Method getActionMethod( String methodName, Class argType )
    {
        String cacheKey = argType != null ? methodName + '/' + argType.getName() : methodName;
        Class thisClass = getClass();
        ClassLevelCache cache = ClassLevelCache.getCache( thisClass );
        Method actionMethod = ( Method ) cache.get( CACHEID_ACTION_METHODS, cacheKey );

        if ( actionMethod != null )
        {
            return actionMethod;
        }
        else
        {
            //
            // We didn't find it in the cache.  Look for it reflectively.
            //
            if ( argType == null )
            {
                //
                // No form -- look for a method with no arguments.
                //
                actionMethod = InternalUtils.lookupMethod( thisClass, methodName, null );
            }
            else
            {
                //
                // Has a form.  Look for a method with a single argument -- either the given type
                // or any superclass.
                //
                while ( argType != null )
                {
                    actionMethod = InternalUtils.lookupMethod( thisClass, methodName, new Class[]{ argType } );

                    if ( actionMethod != null )
                    {
                        break;
                    }

                    argType = argType.getSuperclass();
                }
            }

            if ( actionMethod != null && actionMethod.getReturnType().equals( Forward.class ) )
            {
                if ( ! Modifier.isPublic( actionMethod.getModifiers() ) ) actionMethod.setAccessible( true );
                cache.put( CACHEID_ACTION_METHODS, cacheKey, actionMethod );
                return actionMethod;
            }
        }

        return null;
    }

    private Class getFormClass( Object form, ActionMapping mapping, HttpServletRequest request )
        throws ClassNotFoundException
    {
        if ( mapping instanceof PageFlowActionMapping )
        {
            String formClassName = ( ( PageFlowActionMapping ) mapping ).getFormClass();

            if ( formClassName != null )
            {
                return InternalUtils.getReloadableClass( formClassName, getServletContext() );
            }
        }

        return form != null ? form.getClass() : null;
    }

    /**
     * Get the ActionForward returned by the action handler method that corresponds to the
     * given action name and form-bean, or send an error to the browser if there is no
     * matching method.
     * 
     * @param actionName the name of the Struts action to handle.
     * @param inputForm the form-bean associated with the action.  May be <code>null</code>.
     * @param response the current HttpServletResponse.
     * @return the ActionForward returned by the action handler method, or <code>null</code> if
     *             there was no matching method (in which case an error was written to the
     *             browser.
     * @throws Exception if an Exception was raised in user code.
     */
    ActionForward getActionMethodForward( String actionName, Object inputForm,
                                          HttpServletRequest request, HttpServletResponse response,
                                          ActionMapping mapping  )
        throws Exception
    {
        //
        // Find the method.
        //
        Class formClass = getFormClass( inputForm, mapping, request );
        Method actionMethod = getActionMethod( actionName, formClass );

        //
        // Invoke the method.
        //
        if ( actionMethod != null )
        {
            return invokeActionMethod( actionMethod, inputForm, request, mapping );
        }

        if ( _log.isWarnEnabled() )
        {
            InternalStringBuilder msg = new InternalStringBuilder( "Could not find matching action method for action=" );
            msg.append( actionName ).append( ", form=" );
            msg.append( inputForm != null ? inputForm.getClass().getName() :"[none]" );
            _log.warn( msg.toString() );
        }

        PageFlowException ex = new NoMatchingActionMethodException( actionName, inputForm, this );
        InternalUtils.throwPageFlowException( ex, request );
        return null;
    }

    private static String getFormQualifiedActionPath( Class formClass, String actionPath )
    {
        InternalStringBuilder ret = new InternalStringBuilder( actionPath );
        ret.append( '_' );
        ret.append( formClass.getName().replace( '.', '_' ).replace( '$', '_' ) );
        return ret.toString();
    }

    /**
     * Invoke the given action handler method, passing it an argument if appropriate.
     * 
     * @param method the action handler method to invoke.
     * @param arg the form-bean to pass; may be <code>null</code>.
     * @return the ActionForward returned by the action handler method.
     * @throws Exception if an Exception was raised in user code.
     */
    protected ActionForward invokeActionMethod( Method method, Object arg )
        throws Exception
    {
        return invokeActionMethod( method, arg, getRequest(), getActionMapping() );
    }

    /**
     * Invoke the given action handler method, passing it an argument if appropriate.
     * 
     * @param method the action handler method to invoke.
     * @param arg the form-bean to pass; may be <code>null</code>.
     * @param request the current HttpServletRequest.
     * @return the ActionForward returned by the action handler method.
     * @throws Exception if an Exception was raised in user code.
     */
    ActionForward invokeActionMethod( Method method, Object arg, HttpServletRequest request, ActionMapping mapping )
        throws Exception
    {
        Class[] paramTypes = method.getParameterTypes();

        try
        {
            if ( paramTypes.length > 0 && paramTypes[0].isInstance( arg ) )
            {
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "Invoking action method " + method.getName() + '(' + paramTypes[0].getName() + ')' );
                }

                return ( ActionForward ) method.invoke( this, new Object[]{ arg } );
            }
            else if ( paramTypes.length == 0 )
            {
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "Invoking action method " + method.getName() + "()" );
                }

                return ( ActionForward ) method.invoke( this, null );
            }
        }
        finally
        {
            boolean readonly = false;

            if ( mapping instanceof PageFlowActionMapping )
            {
                PageFlowActionMapping pfam = ( PageFlowActionMapping ) mapping;
                readonly = pfam.isReadonly();
            }

            /*
            A read only Flow Controller is one that is marked as such via metadata.  If a FlowController
            is read only, no steps need to be taken to ensure that it fails over into the session for
            serialization in a cluster.
             */
            if ( ! readonly )
            {
                ensureFailover( getRequest() );
            }
        }

        if ( _log.isWarnEnabled() )
        {
            _log.warn( "Could not find action method " + method.getName() + " with appropriate signature." );
        }

        return null;
    }

    /**
     * Get the current HttpServletRequest.  This call is only valid during {@link FlowController#execute} (where
     * any user action method is invoked), and during the lifecycle methods {@link FlowController#onCreate},
     * {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     * 
     * @return the current HttpServletRequest.
     * @throws IllegalStateException if this method is invoked outside of action method
     *             execution (i.e., outside of the call to {@link FlowController#execute}, and outside of
     *             {@link FlowController#onCreate}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    protected final HttpServletRequest getRequest()
    {
        if ( _perRequestState == null )
        {
            throw new IllegalStateException( "getRequest was called outside of a valid context." );
        }

        return _perRequestState.getRequest();
    }

    /**
     * Get the current HttpServletResponse.  This call is only valid during {@link FlowController#execute} (where
     * any user action method is invoked), and during the lifecycle methods {@link FlowController#onCreate},
     * {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     * 
     * @return the current HttpServletResponse.
     * @throws IllegalStateException if this method is invoked outside of action method
     *             execution (i.e., outside of the call to {@link FlowController#execute}, and outside of
     *             {@link FlowController#onCreate}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    protected final HttpServletResponse getResponse()
    {
        if ( _perRequestState == null )
        {
            throw new IllegalStateException( "getResponse was called outside of a valid context." );
        }

        return _perRequestState.getResponse();
    }

    /**
     * Get the current Struts ActionMapping, which is information from the Struts-XML &lt;action&gt;
     * tag that corresponds to the current action being executed.  This call is only valid during
     * {@link FlowController#execute} (where any user action method is invoked), and during the lifecycle
     * methods {@link FlowController#beforeAction} and {@link FlowController#afterAction}.
     * @deprecated Use {@link FlowController#getActionMapping} instead.
     * 
     * @return the current Struts ActionMapping.
     * @throws IllegalStateException if this method is invoked outside of action method
     *             execution (i.e., outside of the call to {@link FlowController#execute}, and outside of
     *             {@link FlowController#onCreate}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    protected final ActionMapping getMapping()
    {
        return getActionMapping();
    }

    /**
     * Get the current Struts ActionMapping, which is information from the Struts-XML &lt;action&gt;
     * tag that corresponds to the current action being executed.  This call is only valid
     * during {@link FlowController#execute} (where any user action method is invoked), and during the lifecycle
     * methods {@link FlowController#beforeAction} and {@link FlowController#afterAction}.
     * 
     * @return the current Struts ActionMapping.
     * @throws IllegalStateException if this method is invoked outside of action method
     *             execution (i.e., outside of the call to {@link FlowController#execute}, and outside of
     *             {@link FlowController#onCreate}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    protected final ActionMapping getActionMapping()
    {
        if ( _perRequestState == null )
        {
            throw new IllegalStateException( "getActionMapping was called outside of a valid context." );
        }

        return _perRequestState.getActionMapping();
    }

    /**
     * Get the current user session.  This call is only valid during {@link FlowController#execute} (where
     * any user action method is invoked), and during the lifecycle methods {@link FlowController#onCreate},
     * {@link FlowController#onDestroy}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     * 
     * @return the HttpSession for the current user session.
     * @throws IllegalStateException if this method is invoked outside of action method
     *             execution (i.e., outside of the call to {@link FlowController#execute}, and outside of
     *             {@link FlowController#onCreate}, {@link FlowController#onDestroy}, {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    protected final HttpSession getSession()
    {
        if ( _perRequestState == null )
        {
            throw new IllegalStateException( "getSession was called outside of a valid context." );
        }

        return _perRequestState.getRequest().getSession( true );
    }

    PerRequestState setPerRequestState( PerRequestState state )
    {
        if ( state != null )
        {
            assert state.getRequest() != null;
            assert state.getResponse() != null;
        }

        PerRequestState prevState = _perRequestState;
        _perRequestState = state;
        return prevState;
    }

    /**
     * Get the Struts ModuleConfig object associated with this FlowController.
     */
    protected final ModuleConfig getModuleConfig()
    {
        if ( _moduleConfig == null )
        {
            _moduleConfig = InternalUtils.ensureModuleConfig(getModulePath(), getServletContext());
            assert _moduleConfig != null : getModulePath() + "; " + getClass().getName();
        }
        return _moduleConfig;
    }

    /**
      * This is a non-property public accessor that will return the property value <code>moduleConfig</code>.
      * This is a non-property method because properties are exposed to databinding and that would expose
      * internal data structures to attack.
      * @return the ModuleConfig for this FlowController
      */
    public final ModuleConfig theModuleConfig()
    {
        return getModuleConfig();
    }

    /**
     * Gets the Struts module configuration associated with this controller.
     * @deprecated Use {@link #getModuleConfig()} instead.
     * 
     * @param servletContext the current ServletContext.
     * @return the Struts ModuleConfig for this controller.
     */
    public ModuleConfig getModuleConfig( ServletContext servletContext, HttpServletRequest request )
    {
        return getModuleConfig();
    }

    /**
     * Resolve the given action name to a URI.  This version assumes that the ActionServlet
     * class should be {@link PageFlowActionServlet}.
     * Note: this method invokes the full action-processing cycle on a {@link ScopedRequest}.  Use
     *             {@link FlowController#resolveAction} to resolve the URI for an action in the current page flow.
     * @deprecated Use {@link PageFlowUtils#strutsLookup} instead.  This method will be removed in v1.1.
     */
    public static ActionResult lookup( String actionName, ServletContext context, HttpServletRequest request,
                                       HttpServletResponse response )
        throws Exception
    {
        return PageFlowUtils.strutsLookup( context, request, response, actionName, null );
    }

    /**
     * Resolve the given action name to a URI.
     * Note: this method invokes the full action-processing cycle on a {@link ScopedRequest}.  Use
     *             {@link FlowController#resolveAction} to resolve the URI for an action in the current page flow.
     * @deprecated Use {@link PageFlowUtils#strutsLookup} instead.  This method will be removed in v1.1.
     */
    public static ActionResult lookup( String actionName, ServletContext context, HttpServletRequest request,
                                       HttpServletResponse response, String actionServletClassName )
        throws Exception
    {
        return PageFlowUtils.strutsLookup( context, request, response, actionName, null );
    }

    /**
     * Call an action and return the result URI.
     * 
     * @param actionName the name of the action to run.
     * @param form the form bean instance to pass to the action, or <code>null</code> if none should be passed.
     * @return the result webapp-relative URI, as a String.
     * @throws ActionNotFoundException when the given action does not exist in this FlowController.
     * @throws Exception if the action method throws an Exception.
     */
    public String resolveAction( String actionName, Object form, HttpServletRequest request,
                                 HttpServletResponse response )
        throws Exception
    {
        ActionMapping mapping = ( ActionMapping ) getModuleConfig().findActionConfig( '/' + actionName );

        if ( mapping == null )
        {
            InternalUtils.throwPageFlowException( new ActionNotFoundException( actionName, this, form ), request );
        }

        ActionForward fwd = getActionMethodForward( actionName, form, request, response, mapping );

        if ( fwd instanceof Forward )
        {
            ( ( Forward ) fwd ).initialize( mapping, this, request );
        }

        String path = fwd.getPath();
        if ( fwd.getContextRelative() || FileUtils.isAbsoluteURI( path ) )
        {
            return path;
        }
        else
        {
            return getModulePath() + path;
        }
    }

    /**
     * Call an action and return the result URI.
     * 
     * @deprecated Use {@link FlowController#resolveAction(String, Object, HttpServletRequest, HttpServletResponse)} instead.
     * @param actionName the name of the action to run.
     * @param form the form bean instance to pass to the action, or <code>null</code> if none should be passed.
     * @return the result webapp-relative URI, as a String.
     * @throws ActionNotFoundException when the given action does not exist in this FlowController.
     * @throws Exception if the action method throws an Exception.
     */
    public String resolveAction( String actionName, Object form )
        throws Exception
    {
        return resolveAction( actionName, form, getRequest(), getResponse() );
    }

    /**
     * Get a list of the names of actions handled by methods in this PageFlowController.
     * 
     * @return a String array containing the names of actions handled by methods in this PageFlowController.
     */
    protected String[] getActions()
    {
        ActionConfig[] actionConfigs = getModuleConfig().findActionConfigs();
        ArrayList actionNames = new ArrayList();

        for ( int i = 0; i < actionConfigs.length; i++ )
        {
            ActionConfig ac = actionConfigs[i];
            actionNames.add( ac.getPath().substring( 1 ) ); // every action path has a '/' in front of it
        }

        return ( String[] ) actionNames.toArray( new String[0] );
    }

    /**
     * Tell whether a given String is the name of an action handled by a method in this PageFlowController.
     * 
     * @param name the action-name to query.
     * @return <code>true</code> if <code>name</code> is the name of an action handled by a method in this
     *         PageFlowController.
     */
    public boolean isAction( String name )
    {
        return getModuleConfig().findActionConfig( '/' + name ) != null;
    }

    /**
     * Tell whether this is a {@link PageFlowController}.
     * 
     * @return <code>true</code> if this is a {@link PageFlowController}.
     */
    public boolean isPageFlow()
    {
        return false;
    }

    /**
     * Get the current Struts ActionServlet.
     * 
     * @deprecated This method will be removed with no replacement.  In most cases, {@link FlowController#getServletContext()} is
     *             sufficient; for other cases, the ActionServlet itself is in the ServletContext attribute
     *             {@link Globals#ACTION_SERVLET_KEY}.
     * @return the ActionServlet.
     */
    protected ActionServlet getServlet()
    {
        return InternalUtils.getActionServlet( getServletContext() );
    }

    /**
     * Called on this object for non-lookup (refresh) requests.  This is a framework-invoked method that should not
     * normally be called directly.
     */
    public final synchronized void refresh( HttpServletRequest request, HttpServletResponse response )
    {
        PerRequestState prevState = setPerRequestState( new PerRequestState( request, response, null ) );

        try
        {
            onRefresh();
        }
        finally
        {
            setPerRequestState( prevState );
        }
    }

    /**
     * Callback that is invoked when this controller is involved in a refresh request, as can happen in a portal
     * environment on a request where no action is run in the current page flow, but a previously-displayed page in the
     * page flow is re-rendered.
     */
    protected void onRefresh()
    {
    }

    /**
     * Remove this instance from the user session.
     */
    protected void remove()
    {
        removeFromSession( getRequest() );
    }

    /**
     * Used by derived classes to store information on the most recent action executed.
     */
    void savePreviousActionInfo( ActionForm form, HttpServletRequest request, ActionMapping mapping,
                                 ServletContext servletContext )
    {
    }

    /**
     * Store information about recent pages displayed.  This is a framework-invoked method that should not normally be
     * called directly.
     */
    public void savePreviousPageInfo( ActionForward forward, ActionForm form, ActionMapping mapping,
                                      HttpServletRequest request, ServletContext servletContext,
                                      boolean isSpecialForward )
    {
    }

    /**
     * When this FlowController does not use a {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}
     * annotation with a
     * <code>navigateTo=</code>{@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction Jpf.NavigateTo.previousAction}
     * attribute, the following methods always return <code>null</code> by default.
     * <ul>
     *     <li>getPreviousActionInfo</li>
     *     <li>getPreviousActionURI</li>
     *     <li>getPreviousForm</li>
     * </ul>
     * Override <code>alwaysTrackPreviousAction</code> (which always returns <code>false</code>) to enable these methods
     * in all cases.
     * 
     * @return <code>true</code> if the previous action should always be tracked, regardless of whether
     * <code>return-to="previousAction"</code> is used.
     * @see PageFlowController#getPreviousActionInfo
     * @see PageFlowController#getPreviousActionURI
     * @see PageFlowController#getPreviousFormBean
     */
    protected boolean alwaysTrackPreviousAction()
    {
        return false;
    }

    /**
     * When this FlowController does not use a {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}
     * annotation with either a
     * <code>navigateTo</code>={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#currentPage Jpf.NavigateTo.currentPage}
     * attribute or a
     * <code>navigateTo</code>={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousPage Jpf.NavigateTo.previousPage}
     * attribute, the following methods always return <code>null</code> by default.
     * <ul>
     *     <li>getCurrentPageInfo</li>
     *     <li>getPreviousPageInfo</li>
     *     <li>getCurrentForwardPath</li>
     *     <li>getPreviousForwardPath</li>
     * </ul>
     * Override <code>alwaysTrackPreviousPage</code> (which always returns <code>false</code>) to enable these methods
     * in all cases.
     * 
     * @return <code>true</code> if the previous page should always be tracked, regardless
     *         of whether <code>return-to="currentPage"</code> or <code>return-to="previousPage"</code>
     *         is used.
     * @see PageFlowController#getCurrentPageInfo
     * @see PageFlowController#getPreviousPageInfo
     * @see PageFlowController#getCurrentForwardPath
     * @see PageFlowController#getPreviousForwardPath
     */
    protected boolean alwaysTrackPreviousPage()
    {
        return false;
    }

    /**
     * Increment the count of concurrent requests to this FlowController.  Note that this method
     * is not synchronized -- we use it to decide whether to synchronize on this instance,
     * or to bail out with an error message about too many concurrent requests.
     */
    boolean incrementRequestCount( HttpServletRequest request, HttpServletResponse response,
                                   ServletContext servletContext )
        throws IOException
    {
        //
        // First cache the max-concurrent-request-count value.
        //
        if ( _maxConcurrentRequestCount == -1 )
        {
            _maxConcurrentRequestCount = DEFAULT_MAX_CONCURRENT_REQUEST_COUNT;
            assert servletContext != null;

            String countStr = servletContext.getInitParameter( MAX_CONCURRENT_REQUESTS_PARAM );
            if ( countStr != null )
            {
                try
                {
                    _maxConcurrentRequestCount = Integer.parseInt( countStr );
                }
                catch ( NumberFormatException e )
                {
                    _log.error( "Invalid value for servlet context parameter" + MAX_CONCURRENT_REQUESTS_PARAM
                                + ": " + countStr, e );
                }

                if ( _maxConcurrentRequestCount < 1 )
                {
                    _maxConcurrentRequestCount = DEFAULT_MAX_CONCURRENT_REQUEST_COUNT;
                    _log.error( "Invalid value for servlet context parameter" + MAX_CONCURRENT_REQUESTS_PARAM
                                + ": " + countStr );
                }
            }
        }

        //
        // Now, if the current count of concurrent requests to this instance is greater than the max,
        // send an error on the response.
        //
        if ( _requestCount >= _maxConcurrentRequestCount )
        {
            if ( _log.isInfoEnabled() )
            {
                _log.info( "Too many requests to FlowController " + getDisplayName() + " ("
                           + ( _requestCount + 1 ) + '>' + _maxConcurrentRequestCount + "); returning error code "
                           + EXCEEDED_MAX_CONCURRENT_REQUESTS_ERRORCODE );
            }

            response.sendError( EXCEEDED_MAX_CONCURRENT_REQUESTS_ERRORCODE );
            return false;
        }

        //
        // We're ok -- increment the count and continue.
        //
        ++_requestCount;
        return true;
    }

    void decrementRequestCount( HttpServletRequest request )
    {
        assert _requestCount > 0 : request.getRequestURI();
        --_requestCount;
    }

    /**
     * Invoke the given exception handler method.  This is a framework-invoked method that should not normally be called
     * directly
     * 
     * @param method the action handler method to invoke.
     * @param ex the Throwable that is to be handled.
     * @param message the String message that is to be passed to the handler method.
     * @param wrappedFormBean the wrapped form bean that is associated with the action being processed; may be <code>null</code>.
     * @param exceptionConfig the exception configuration object for the current exception handler.
     * @param actionMapping the action configuration object for the requested action.
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return the ActionForward returned by the exception handler method.
     */
    public synchronized ActionForward invokeExceptionHandler(
            Method method, Throwable ex, String message, ActionForm wrappedFormBean,
            PageFlowExceptionConfig exceptionConfig, ActionMapping actionMapping, HttpServletRequest request,
            HttpServletResponse response )
        throws IOException, ServletException
    {
        PerRequestState prevState = setPerRequestState( new PerRequestState( request, response, actionMapping ) );

        try
        {
            if ( _log.isDebugEnabled() )
            {
                _log.debug( "Invoking exception handler method " + method.getName() + '('
                           + method.getParameterTypes()[0].getName() + ", ...)" );
            }

            try
            {
                ActionForward retVal = null;
                String actionName = InternalUtils.getActionName( actionMapping );

                if ( actionName == null && ex instanceof PageFlowException )
                {
                    actionName = ( ( PageFlowException ) ex ).getActionName();
                }

                try
                {
                    Object formBean = InternalUtils.unwrapFormBean( wrappedFormBean );
                    Object[] args = new Object[]{ ex, actionName, message, formBean };
                    retVal = ( ActionForward ) method.invoke( this, args );
                }
                finally
                {
                    if ( ! exceptionConfig.isReadonly() )
                    {
                        ensureFailover( request );
                    }
                }

                return retVal;
            }
            catch ( InvocationTargetException e )
            {
                Throwable target = e.getTargetException();

                if ( target instanceof Exception )
                {
                    throw ( Exception ) target;
                }
                else
                {
                    throw e;
                }
            }
        }
        catch ( Throwable e )
        {
            _log.error( "Exception while handling exception " + ex.getClass().getName()
                        + ".  The original exception will be thrown.", e );

            ExceptionsHandler eh = Handlers.get( getServletContext() ).getExceptionsHandler();
            FlowControllerHandlerContext context = new FlowControllerHandlerContext( request, response, this );
            Throwable unwrapped = eh.unwrapException( context, e );

            if ( ! eh.eatUnhandledException( context, unwrapped ) )
            {
                if ( ex instanceof ServletException ) throw ( ServletException ) ex;
                if ( ex instanceof IOException ) throw ( IOException ) ex;
                if ( ex instanceof Error ) throw ( Error ) ex;
                throw new UnhandledException( ex );
            }

            return null;
        }
        finally
        {
            setPerRequestState( prevState );
        }
    }

    /**
     * Add a property-related message that will be shown with the Errors and Error tags.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the message.
     * @param messageArgs zero or more arguments to the message.
     */
    protected void addActionError( String propertyName, String messageKey, Object[] messageArgs )
    {
        InternalUtils.addActionError( propertyName, new ActionMessage( messageKey, messageArgs ), getRequest() );
    }

    /**
     * Add a property-related message as an expression that will be evaluated and shown with the Errors and Error tags.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param expression the expression that will be evaluated to generate the error message.
     * @param messageArgs zero or more arguments to the message; may be expressions.
     */
    protected void addActionErrorExpression( String propertyName, String expression, Object[] messageArgs )
    {
        PageFlowUtils.addActionErrorExpression( getRequest(), propertyName, expression, messageArgs );
    }

    /**
     * Add a validation error that will be shown with the Errors and Error tags.
     * @deprecated Use {@link #addActionError} instead.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the error message.
     * @param messageArgs an array of arguments for the error message.
     */
    protected void addValidationError( String propertyName, String messageKey, Object[] messageArgs )
    {
        PageFlowUtils.addValidationError( propertyName, messageKey, messageArgs, getRequest() );
    }

    /**
     * Add a validation error that will be shown with the Errors and Error tags.
     * @deprecated Use {@link #addActionError} instead.
     * 
     * @param propertyName the name of the property with which to associate this error.
     * @param messageKey the message-resources key for the error message.
     */
    protected void addValidationError( String propertyName, String messageKey )
    {
        PageFlowUtils.addValidationError( propertyName, messageKey, getRequest() );
    }

    private static ActionForward handleSimpleAction( PageFlowActionMapping mapping,
                                                     ActionForm wrappedFormBean,
                                                     HttpServletRequest request,
                                                     ServletContext servletContext )
    {

        Map/*< String, String >*/ conditionalForwards = mapping.getConditionalForwardsMap();

        if ( ! conditionalForwards.isEmpty() )
        {
            Object formBean = InternalUtils.unwrapFormBean( wrappedFormBean );

            for ( Iterator/*< Map.Entry< String, String > >*/ i = conditionalForwards.entrySet().iterator(); i.hasNext(); )
            {
                Map.Entry/*< String, String >*/ entry = ( Map.Entry ) i.next();
                String expression = ( String ) entry.getKey();
                String forwardName = ( String ) entry.getValue();

                try
                {
                    if ( InternalExpressionUtils.evaluateCondition( expression, formBean, request, servletContext ) )
                    {
                        if ( _log.isTraceEnabled() )
                        {
                            _log.trace( "Expression '" + expression + "' evaluated to true on simple action "
                                        + mapping.getPath() + "; using forward " + forwardName + '.' );
                        }

                        return new Forward( forwardName );
                    }
                }
                catch( Exception e )  // ELException
                {
                    if( _log.isErrorEnabled() )
                    {
                        _log.error( "Exception occurred evaluating navigation expression '" + expression
                                    + "'.  Cause: " + e.getCause(), e);
                    }
                }
            }
        }


        String defaultForwardName = mapping.getDefaultForward();
        assert defaultForwardName != null : "defaultForwardName is null on Simple Action " + mapping.getPath();

        if ( _log.isTraceEnabled() )
        {
            _log.trace( "No expression evaluated to true on simple action " + mapping.getPath()
                        + "; using forward " + defaultForwardName + '.' );
        }

        return new Forward( defaultForwardName );
    }

    /**
     * Get the system default locale.
     * 
     * @return the system default locale.
     */
    protected static Locale getDefaultLocale()
    {
        return defaultLocale;
    }

    /**
     * Return the default data source for the Struts module associated with this FlowController.
     *
     * @param request the current request.
     */
    protected DataSource getDataSource( HttpServletRequest request )
    {
        return getDataSource( request, Globals.DATA_SOURCE_KEY );
    }

    /**
     * Return the specified data source for the current Struts module.
     *
     * @param request The servlet request we are processing
     * @param key     The key specified in the
     *                <code>&lt;message-resources&gt;</code> element for the
     *                requested bundle
     */
    protected DataSource getDataSource( HttpServletRequest request, String key )
    {
        // Return the requested data source instance
        return ( DataSource ) getServletContext().getAttribute( key + getModuleConfig().getPrefix() );
    }


    /**
     * Return the user's currently selected Locale.
     *
     * @param request the current request.
     * @return the user's currently selected Locale, stored in the session.
     * @deprecated Use {@link #getLocale} or {@link #retrieveUserLocale}.
     */
    protected Locale getLocale( HttpServletRequest request )
    {

        HttpSession session = request.getSession();
        Locale locale = ( Locale ) session.getAttribute( Globals.LOCALE_KEY );
        return locale != null ? locale : DEFAULT_LOCALE;
    }

    /**
     * Get the user's currently selected Locale.
     *
     * @return the user's currently selected Locale, stored in the session.
     */
    protected Locale getLocale()
    {
        return retrieveUserLocale( getRequest(), null );
    }

    public static Locale retrieveUserLocale( HttpServletRequest request, String locale )
    {
        if ( locale == null ) locale = Globals.LOCALE_KEY;
        HttpSession session = request.getSession( false );
        Locale userLocale = null;
        if ( session != null ) userLocale = ( Locale ) session.getAttribute( locale );
        if ( userLocale == null ) userLocale = DEFAULT_LOCALE;
        return userLocale;
    }


    /**
     * Return the message resources for the default module.
     *
     * @deprecated This method can only return the resources for the default
     *             module.  Use {@link #getMessageResources()} to get the
     *             resources for this FlowController.
     */
    protected MessageResources getResources()
    {
        return ( MessageResources ) getServletContext().getAttribute( Globals.MESSAGES_KEY );
    }

    /**
     * Return the default message resources for the current module.
     * @deprecated Use {@link #getMessageResources()} instead.
     *
     * @param request The servlet request we are processing
     */
    protected MessageResources getResources( HttpServletRequest request )
    {
        return getMessageResources();
    }


    /**
     * Return the specified message resources for the current module.
     * @deprecated Use {@link #getMessageResources(String)} instead.
     *
     * @param request the current request.
     * @param key     The bundle key specified in a
     *                {@link org.apache.beehive.netui.pageflow.annotations.Jpf.MessageBundle &#64;Jpf.MessageBundle} annotation.
     */
    protected MessageResources getResources( HttpServletRequest request, String key )
    {
        return getMessageResources( key );
    }

    /**
     * Get the default message resources for this FlowController.
     */
    protected MessageResources getMessageResources()
    {
        return getMessageResources( Globals.MESSAGES_KEY );
    }

    /**
     * Get the specified message resources for this FlowController.
     *
     * @param key The bundle key specified in a
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.MessageBundle &#64;Jpf.MessageBundle} annotation.
     */
    protected MessageResources getMessageResources( String key )
    {
        return ( MessageResources ) getServletContext().getAttribute( key + getModuleConfig().getPrefix() );
    }

    /**
     * <p>Returns <code>true</code> if the current form's cancel button was
     * pressed.  This method will check if the <code>Globals.CANCEL_KEY</code>
     * request attribute has been set, which normally occurs if the cancel
     * button generated by the Struts <strong>CancelTag</strong> was pressed by the user
     * in the current request.  If <code>true</code>, validation performed
     * by an <strong>ActionForm</strong>'s <code>validate()</code> method
     * will have been skipped by the controller servlet.</p>
     * @deprecated This method will be removed without replacement in a future release.  The normal method for
     *     cancelling in a form is to use the <code>action</code> attribute on
     *     {@link org.apache.beehive.netui.tags.html.Button}, rather than avoiding validation on the current action.
     *
     * @param request The servlet request we are processing
     * @see org.apache.struts.taglib.html.CancelTag
     */
    protected boolean isCancelled( HttpServletRequest request )
    {

        return request.getAttribute( Globals.CANCEL_KEY ) != null;

    }

    /**
     * Generate a new transaction token, to be used for enforcing a single request for a particular transaction.
     *
     * @param request the current request.
     * @deprecated Use {@link #generateToken()} instead.
     */
    protected String generateToken( HttpServletRequest request )
    {
        return TOKEN_PROCESSOR.generateToken( request );
    }

    /**
     * Generate a new transaction token, to be used for enforcing a single request for a particular transaction.
     * 
     * @see #isTokenValid()
     * @see #isTokenValid(boolean)
     * @see #resetToken()
     */
    protected String generateToken()
    {
        return TOKEN_PROCESSOR.generateToken( getRequest() );
    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it.  Returns <code>false</code>
     * under any of the following circumstances:
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     * transaction token in the user's session</li>
     * </ul>
     * @deprecated Use {@link #isTokenValid()} instead.
     *
     * @param request The servlet request we are processing
     */
    protected boolean isTokenValid( HttpServletRequest request )
    {

        return TOKEN_PROCESSOR.isTokenValid( request, false );

    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it.  Returns <code>false</code>
     * under any of the following circumstances:
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     * transaction token in the user's session</li>
     * </ul>
     * 
     * @see #generateToken()
     * @see #resetToken()
     */
    protected boolean isTokenValid()
    {

        return TOKEN_PROCESSOR.isTokenValid( getRequest(), false );

    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it.  Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     * transaction token in the user's session</li>
     * </ul>
     * @deprecated Use {@link #isTokenValid(boolean)} instead.
     *
     * @param request The servlet request we are processing
     * @param reset   Should we reset the token after checking it?
     */
    protected boolean isTokenValid( HttpServletRequest request, boolean reset )
    {
        return TOKEN_PROCESSOR.isTokenValid( request, reset );
    }

    /**
     * Return <code>true</code> if there is a transaction token stored in
     * the user's current session, and the value submitted as a request
     * parameter with this action matches it.  Returns <code>false</code>
     * <ul>
     * <li>No session associated with this request</li>
     * <li>No transaction token saved in the session</li>
     * <li>No transaction token included as a request parameter</li>
     * <li>The included transaction token value does not match the
     * transaction token in the user's session</li>
     * </ul>
     *
     * @param reset   Should we reset the token after checking it?
     * @see #generateToken()
     * @see #resetToken()
     */
    protected boolean isTokenValid( boolean reset )
    {

        return TOKEN_PROCESSOR.isTokenValid( getRequest(), reset );
    }


    /**
     * Reset the saved transaction token in the user's session.  This
     * indicates that transactional token checking will not be needed
     * on the next request that is submitted.
     * @deprecated Use {@link #resetToken()} instead.
     *
     * @param request The servlet request we are processing
     */
    protected void resetToken( HttpServletRequest request )
    {
        TOKEN_PROCESSOR.resetToken( request );
    }

    /**
     * Reset the saved transaction token in the user's session.  This
     * indicates that transactional token checking will not be needed
     * on the next request that is submitted.
     * 
     * @see #isTokenValid()
     * @see #isTokenValid(boolean)
     * @see #generateToken()
     */
    protected void resetToken()
    {
        TOKEN_PROCESSOR.resetToken( getRequest() );
    }


    /**
     * Save the specified messages keys into the request for use by the &lt;netui:error&gt; or &lt;netui:errors&gt; tags.
     * @deprecated Use {@link #saveActionErrors} instead.
     *
     * @param errors the ActionMessages to save in the request.
     */
    protected void saveErrors( HttpServletRequest request, ActionMessages errors )
    {
        saveActionErrors( errors );
    }


    /**
     * Save the specified messages keys into the appropriate request
     * attribute for use by the Struts &lt;html:messages&gt; tag (if
     * messages="true" is set), if any messages are required.  Otherwise,
     * ensure that the request attribute is not created.
     * @deprecated This method will be removed without replacement in a future release.
     *
     * @param request  The servlet request we are processing
     * @param messages Messages object
     */
    protected void saveMessages( HttpServletRequest request, ActionMessages messages )
    {

        // Remove any messages attribute if none are required
        if ( messages == null || messages.isEmpty() )
        {
            request.removeAttribute( Globals.MESSAGE_KEY );
            return;
        }

        // Save the messages we need
        request.setAttribute( Globals.MESSAGE_KEY, messages );

    }

    /**
     * Save the specified messages keys into the request for use by the &lt;netui:error&gt; or &lt;netui:errors&gt; tags.
     *
     * @param errors the ActionMessages to save in the request.
     */
    protected void saveActionErrors( ActionMessages errors )
    {
        // Remove any messages attribute if none are required
        if ( errors == null || errors.isEmpty() )
        {
            getRequest().removeAttribute( Globals.ERROR_KEY );
        }
        else
        {
            getRequest().setAttribute( Globals.ERROR_KEY, errors );
        }
    }


    /**
     * Save a new transaction token in the user's current session, creating
     * a new session if necessary.
     *
     * @param request The servlet request we are processing
     */
    protected void saveToken( HttpServletRequest request )
    {
        TOKEN_PROCESSOR.saveToken( request );
    }


    /**
     * Set the user's currently selected Locale.
     *
     * @param request The request we are processing
     * @param locale  The user's selected Locale to be set, or null
     *                to select the server's default Locale
     * @deprecated Use {@link #setLocale(Locale)}.
     */
    protected void setLocale( HttpServletRequest request, Locale locale )
    {

        HttpSession session = request.getSession();
        if ( locale == null )
        {
            locale = getDefaultLocale();
        }
        session.setAttribute( Globals.LOCALE_KEY, locale );

    }

    /**
     * Set the user's currently selected Locale.
     *
     * @param locale  The user's selected Locale to be set, or null to select the server's default Locale
     */
    protected void setLocale( Locale locale )
    {
        if ( locale == null ) locale = getDefaultLocale();
        getSession().setAttribute( Globals.LOCALE_KEY, locale );
    }

    /**
     * Get the flow-scoped form bean member associated with the given ActionMapping.  This is a framework-invoked
     * method that should not normally be called directly.
     */
    public ActionForm getFormBean( ActionMapping mapping )
    {
        if ( mapping instanceof PageFlowActionMapping )
        {
            PageFlowActionMapping pfam = ( PageFlowActionMapping ) mapping;
            String formMember = pfam.getFormMember();
            if ( formMember == null ) return null;

            Field field = null;
            try
            {
                field = getClass().getDeclaredField( formMember );
            }
            catch ( NoSuchFieldException e )
            {
                // try finding a non-private field from the class hierarchy
                field = InternalUtils.lookupField( getClass(), formMember );
                if ( field == null || Modifier.isPrivate( field.getModifiers() ) )
                {
                     _log.error( "Could not find member field " + formMember + " as the form bean.");
                    return null;
                }
            }

            try
            {
                field.setAccessible( true );
                return InternalUtils.wrapFormBean( field.get( this ) );
            }
            catch ( Exception e )
            {
                _log.error( "Could not use member field " + formMember + " as the form bean.", e );
            }
        }

        return null;
    }

    PageFlowRequestProcessor getRequestProcessor()
    {
        ModuleConfig mc = getModuleConfig();
        String key = Globals.REQUEST_PROCESSOR_KEY + mc.getPrefix();
        RequestProcessor rp = ( RequestProcessor ) getServletContext().getAttribute( key );

        //
        // The RequestProcessor may not have been initialized -- if so, just return a new (temporary) one.
        //
        if ( rp == null )
        {
            try
            {
                ControllerConfig cc = mc.getControllerConfig();
                rp = ( RequestProcessor ) RequestUtils.applicationInstance( cc.getProcessorClass() );
                rp.init( InternalUtils.getActionServlet( getServletContext() ), mc );
            }
            catch ( Exception e )
            {
                _log.error( "Could not initialize request processor for module " + mc.getPrefix(), e );
            }
        }

        assert rp == null || rp instanceof PageFlowRequestProcessor : rp.getClass().getName();
        return ( PageFlowRequestProcessor ) rp;
    }

    /**
     * Create a raw action URI, which can be modified before being sent through the registered URL rewriting chain
     * using {@link org.apache.beehive.netui.core.urls.URLRewriterService#rewriteURL}.
     *
     * @param actionName the action name to convert into a MutableURI; may be qualified with a path from the webapp
     *            root, in which case the parent directory from the current request is <i>not</i> used.
     * @return a MutableURI for the given action, suitable for URL rewriting.
     * @throws URISyntaxException    if there is a problem converting the action URI (derived
     *                               from processing the given action name) into a MutableURI.
     * @throws IllegalStateException if this method is invoked outside of action method
     *                               execution (i.e., outside of the call to {@link FlowController#execute},
     *                               and outside of {@link FlowController#onCreate},
     *                               {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    public MutableURI getActionURI( String actionName )
            throws URISyntaxException
    {
        if ( _perRequestState == null )
        {
            throw new IllegalStateException( "getActionURI was called outside of a valid context." );
        }
        ServletContext servletContext = getServletContext();
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        return PageFlowUtils.getActionURI( servletContext, request, response, actionName );
    }

    /**
     * Create a fully-rewritten URI given an action and parameters.
     *
     * @param actionName the action name to convert into a fully-rewritten URI; may be qualified with a path from the
     *            webapp root, in which case the parent directory from the current request is <i>not</i> used.
     * @param parameters the additional parameters to include in the URI query.
     * @param asValidXml flag indicating that the query of the uri should be written
     *                   using the &quot;&amp;amp;&quot; entity, rather than the character, '&amp;'
     * @return a fully-rewritten URI for the given action.
     * @throws URISyntaxException    if there is a problem converting the action url (derived
     *                               from processing the given action name) into a URI.
     * @throws IllegalStateException if this method is invoked outside of action method
     *                               execution (i.e., outside of the call to {@link FlowController#execute},
     *                               and outside of {@link FlowController#onCreate},
     *                               {@link FlowController#beforeAction}, {@link FlowController#afterAction}.
     */
    public String getRewrittenActionURI( String actionName, Map parameters, boolean asValidXml )
            throws URISyntaxException
    {
        if ( _perRequestState == null )
        {
            throw new IllegalStateException( "getRewrittenActionURI was called outside of a valid context." );
        }
        ServletContext servletContext = getServletContext();
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();

        return PageFlowUtils.getRewrittenActionURI( servletContext, request, response,
                                                    actionName, parameters, null, asValidXml );
    }

    FlowControllerHandlerContext getHandlerContext()
    {
        return new FlowControllerHandlerContext( getRequest(), getResponse(), this );
    }
}
