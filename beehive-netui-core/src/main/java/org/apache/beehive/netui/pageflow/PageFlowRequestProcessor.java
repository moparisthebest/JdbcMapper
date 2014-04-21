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

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.core.urls.TemplatedURLFormatter;
import org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory;
import org.apache.beehive.netui.pageflow.config.PageFlowActionForward;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.pageflow.handler.ActionForwardHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.handler.ForwardRedirectHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.LoginHandler;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.Interceptors;
import org.apache.beehive.netui.pageflow.interceptor.Interceptor;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptorContext;
import org.apache.beehive.netui.pageflow.interceptor.action.InterceptorForward;
import org.apache.beehive.netui.pageflow.interceptor.action.internal.ActionInterceptors;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext;
import org.apache.beehive.netui.pageflow.internal.*;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.internal.ServletUtils;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.PageFlowConfig;
import org.apache.beehive.netui.util.config.bean.PreventCache;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.tiles.TilesUtil;
import org.apache.struts.tiles.TilesUtilImpl;
import org.apache.struts.tiles.TilesUtilStrutsImpl;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.TokenProcessor;

import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * The Page Flow extension of the Struts RequestProcessor, which contains callbacks that are invoked
 * during processing of a request to the Struts action servlet.  This class is registered as the
 * <strong>controller</strong> for all Struts modules derived from page flows.
 */
public class PageFlowRequestProcessor
        extends TilesRequestProcessor
        implements Serializable, InternalConstants, PageFlowConstants
{
    private static int requestNumber = 0;

    private static final Logger LOG = Logger.getInstance( PageFlowRequestProcessor.class );
    private static final String ACTION_OVERRIDE_PARAM_PREFIX = "actionOverride:";
    private static final int    ACTION_OVERRIDE_PARAM_PREFIX_LEN = ACTION_OVERRIDE_PARAM_PREFIX.length();
    private static final String SCHEME_UNSECURE = "http";
    private static final String SCHEME_SECURE = "https";
    private static final String REDIRECT_REQUEST_ATTRS_PREFIX = InternalConstants.ATTR_PREFIX + "requestAttrs:";
    private static final String REDIRECT_REQUEST_ATTRS_PARAM = "forceRedirect";
    private static final String FLOW_CONTROLLER_ACTION_CLASSNAME = FlowControllerAction.class.getName();

    private Map/*< String, Class >*/ _formBeanClasses = new HashMap/*< String, Class >*/();
    private Map/*< String, List< ActionMapping > >*/ _overloadedActions = new HashMap/*< String, List< ActionMapping > >*/();
    private Handlers _handlers;
    private FlowControllerFactory _flowControllerFactory;
    private LegacySettings _legacySettings;
    private InternalConcurrentHashMap/*< String, Class >*/ _pageServletClasses = new InternalConcurrentHashMap/*< String, Class >*/();
    private transient ServletContainerAdapter _servletContainerAdapter;
    private transient PageFlowPageFilter _pageServletFilter;

    protected Action processActionCreate( HttpServletRequest request, HttpServletResponse response,
                                          ActionMapping actionMapping )
        throws IOException
    {
        String className = actionMapping.getType();

        if ( className != null && className.equals( FLOW_CONTROLLER_ACTION_CLASSNAME ) )
        {
            FlowController fc = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
            assert fc != null : "no FlowController for request " + request.getRequestURI();
            assert fc.getClass().getName().equals( actionMapping.getParameter() )
                    : "current page flow  type " + fc.getClass().getName() + " does not match type specified in "
                      + FLOW_CONTROLLER_ACTION_CLASSNAME + ": " + actionMapping.getParameter();

            Action action = new FlowControllerAction( fc );
            action.setServlet( servlet );
            return action;
        }

        return super.processActionCreate( request, response, actionMapping );
    }

    /**
     * See if this action mapping is our custom config type, and if so, see if the action should use a member variable
     * in the page flow controller as its form bean (the <code>useFormBean</code> attribute on
     * <code>&#64;Jpf.Action</code>).  If so, return the appropriate Field in the controller class.
     */
    private Field getPageFlowScopedFormMember( ActionMapping mapping, HttpServletRequest request )
    {
        if ( mapping instanceof PageFlowActionMapping )
        {
            PageFlowActionMapping pfam = ( PageFlowActionMapping ) mapping;
            String formMember = pfam.getFormMember();
            if ( formMember == null ) return null;

            Field field = null;
            FlowController fc = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
            try
            {
                field = fc.getClass().getDeclaredField( formMember );
            }
            catch ( NoSuchFieldException e )
            {
                // try finding a non-private field from the class hierarchy
                field = InternalUtils.lookupField( fc.getClass(), formMember );
                if ( field == null || Modifier.isPrivate( field.getModifiers() ) )
                {
                    LOG.error("Could not find page flow member " + formMember + " as the form bean.");
                    return null;
                }
            }

            if ( ! Modifier.isPublic( field.getModifiers() ) ) field.setAccessible( true );
            return field;
        }

        return null;
    }

    protected ActionForm processActionForm( HttpServletRequest request, HttpServletResponse response,
                                            ActionMapping mapping )
    {
        //
        // See if we're using a pageflow-scoped form (a member variable in the current pageflow).
        //
        Field formMemberField = getPageFlowScopedFormMember( mapping, request );

        //
        // First look to see whether the input form was overridden in the request.
        // This happens when a pageflow action forwards to another pageflow,
        // whose begin action expects a form.  In this case, the form is already
        // constructed, and shouldn't be instantiated anew or populated from request
        // parameters.
        //
        ActionForm previousForm = InternalUtils.getForwardedFormBean( request, false );

        if ( previousForm != null )
        {
            //
            // If there was a forwarded form, and if this action specifies a pageflow-scoped form member,
            // set the member with this form.
            //
            if ( formMemberField != null )
            {
                try
                {
                    FlowController fc = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
                    assert fc != null : "no FlowController in request " + request.getRequestURI();
                    formMemberField.set( fc, InternalUtils.unwrapFormBean(  previousForm ) );
                }
                catch ( IllegalAccessException e )
                {
                    LOG.error( "Could not access page flow member " + formMemberField.getName()
                                  + " as the form bean.", e );
                }
            }

            //
            // Return the forwarded form.
            //
            previousForm.setServlet( servlet );
            return previousForm;
        }

        //
        // First see if the previous action put a pageflow-scoped form in the request.  If so, remove it;
        // we don't want a normal request-scoped action to use this pageflow-scoped form.
        //
        String pageFlowScopedFormName = PageFlowRequestWrapper.get( request ).getPageFlowScopedFormName();
        if ( pageFlowScopedFormName != null )
        {
            request.removeAttribute( pageFlowScopedFormName );
            PageFlowRequestWrapper.get( request ).setPageFlowScopedFormName( null );
        }

        //
        // If this action specifies a pageflow-scoped form member variable, use it.
        //
        if ( formMemberField != null )
        {
            try
            {
                FlowController fc = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
                ActionForm form = InternalUtils.wrapFormBean( formMemberField.get( fc ) );

                if ( form == null ) // the pageflow hasn't filled the value yet
                {
                    form = InternalUtils.createActionForm( mapping, moduleConfig, servlet, getServletContext() );
                    form.reset( mapping, request );
                    formMemberField.set( fc, InternalUtils.unwrapFormBean( form ) );
                }

                //
                // Store the form in the right place in the request, so Struts can see it.
                // But, mark a flag so we know to remove this on the next action request -- we don't
                // want this form used by another action unless that action uses the pageflow-scoped
                // form.
                //
                String formAttrName = mapping.getAttribute();
                request.setAttribute( formAttrName, form );
                PageFlowRequestWrapper.get( request ).setPageFlowScopedFormName( formAttrName );
                return form;
            }
            catch ( IllegalAccessException e )
            {
                LOG.error( "Could not access page flow member " + formMemberField.getName() + " as the form bean.", e );
            }
        }

        ActionForm bean = super.processActionForm( request, response, mapping );
        if ( bean == null )
        {
            bean = InternalUtils.createActionForm( mapping, moduleConfig, servlet, getServletContext() );
        }

        return bean;
    }

    protected void processPopulate( HttpServletRequest request, HttpServletResponse response, ActionForm form,
                                    ActionMapping mapping )
        throws ServletException
    {
        //
        // If a previous action forwarded us a form, use that -- don't populate it from request parameters.
        //
        ActionForm previousForm = InternalUtils.getForwardedFormBean( request, true );

        if ( previousForm != null )
        {
            return;
        }

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Populating bean properties from this request" );
        }

        // struts does this
        if ( form != null )
        {
            form.setServlet( servlet );
            form.reset( mapping, request );
        }

        if ( mapping.getMultipartClass() != null )
        {
            request.setAttribute( Globals.MULTIPART_KEY, mapping.getMultipartClass() );
        }

        PageFlowRequestWrapper requestWrapper = PageFlowRequestWrapper.get( request );
        boolean alreadyCalledInRequest = requestWrapper.isProcessPopulateAlreadyCalled();
        if ( ! alreadyCalledInRequest ) requestWrapper.setProcessPopulateAlreadyCalled( true );

        //
        // If this is a forwarded request and the form-bean is null, don't call to ProcessPopulate.
        // We don't want to expose errors due to parameters from the original request, which won't
        // apply to a forwarded action that doesn't take a form.
        //
        if ( !alreadyCalledInRequest || form != null )
        {
            // If this request was forwarded by a button-override of the main form action, then ensure that there are
            // no databinding errors when the override action does not use a form bean.
            if ( form == null && requestWrapper.isForwardedByButton() )
            {
                // This is currently TODO; I removed the use of NullActionForm.
                // See http://issues.apache.org/jira/browse/BEEHIVE-947 .
            }

            ProcessPopulate.populate( request, response, form, alreadyCalledInRequest );
        }
    }

    /**
     * The requested action can be overridden by a request parameter.  In this case, we parse the action from
     * the request parameter and forward to a URI constructed from it.
     *
     * @param request the current HttpServletRequest
     * @param response the current HttpServletResponse
     * @return <code>true</code> if the action was overridden by a request parameter, in which case the request
     *         was forwarded.
     * @throws IOException
     * @throws ServletException
     */
    protected boolean processActionOverride( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        // Only make this check if this is an initial (non-forwarded) request.
        //
        // TODO: performance?
        //
        PageFlowRequestWrapper wrapper = PageFlowRequestWrapper.get( request );
        if ( ! wrapper.isForwardedByButton() && ! wrapper.isForwardedRequest() )
        {
            //
            // First, since we need access to request parameters here, process a multipart request
            // if that's what we have.  This puts the parameters (each in a MIME part) behind an
            // interface that makes them look like normal request parameters.
            //
            HttpServletRequest multipartAwareRequest = processMultipart( request );

            for ( Enumeration e = multipartAwareRequest.getParameterNames(); e.hasMoreElements(); )
            {
                String paramName = ( String ) e.nextElement();

                if ( paramName.startsWith( ACTION_OVERRIDE_PARAM_PREFIX ) )
                {
                    String actionPath = paramName.substring( ACTION_OVERRIDE_PARAM_PREFIX_LEN );
                    ServletContext servletContext = getServletContext();

                    String qualifiedAction = InternalUtils.qualifyAction( servletContext,actionPath );
                    actionPath = InternalUtils.createActionPath(request, qualifiedAction );

                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "A request parameter overrode the action.  Forwarding to: " + actionPath );
                    }

                    wrapper.setForwardedByButton( true );
                    doForward( actionPath, request, response );
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Internal method used to process an action.
     *
     * @param request the current request
     * @param response the current response
     * @throws IOException
     * @throws ServletException
     */
    private void processInternal( HttpServletRequest request, HttpServletResponse response )
            throws IOException, ServletException
    {
        //
        // First wrap the request with an object that contains request-scoped values that our runtime uses.  This
        // is faster than sticking everything into attributes on the request (then basically reading from a HashMap).
        //
        request = PageFlowRequestWrapper.wrapRequest( request );

        String uri = InternalUtils.getDecodedServletPath( request );
        ServletContext servletContext = getServletContext();

        // The ServletContainerAdapter reference may have been lost during serialization/deserialization.
        if (_servletContainerAdapter == null) {
            _servletContainerAdapter = AdapterManager.getServletContainerAdapter( servletContext );
        }

        //
        // Allow the container to do a security check on forwarded requests, if that feature is enabled.
        //
        if ( LegacySettings.get( servletContext ).shouldDoSecureForwards()
             && PageFlowRequestWrapper.get( request ).isForwardedRequest() )
        {
            //
            // In some situations (namely, in scoped requests under portal), the initial
            // security check may not have been done for the request URI.  In this case, a redirect
            // to https may happen during checkSecurity().
            //
            if ( _servletContainerAdapter.doSecurityRedirect( uri, request, response ) )
            {
                if ( LOG.isDebugEnabled() )
                    LOG.debug( "checkSecurity() caused a redirect.  Ending processing for this request "
                               + '(' + uri + ')' );

                return;
            }
        }

        //
        // If we've come in on a forced redirect due to security constraints, look for request attrs
        // that we put into the session.
        //
        String hash = request.getParameter( REDIRECT_REQUEST_ATTRS_PARAM );
        if ( hash != null )
        {
            HttpSession session = request.getSession( false );

            if ( session != null )
            {
                String carryoverAttrName = makeRedirectedRequestAttrsKey( uri, hash );
                Map attrs = ( Map ) session.getAttribute( carryoverAttrName );
                session.removeAttribute( carryoverAttrName );

                if ( attrs != null )
                {
                    for ( Iterator i = attrs.entrySet().iterator(); i.hasNext(); )
                    {
                        Map.Entry entry = ( Map.Entry ) i.next();

                        String attrName = ( String ) entry.getKey();
                        if ( request.getAttribute( attrName ) == null )
                        {
                            request.setAttribute( attrName, entry.getValue() );
                        }
                    }
                }
            }
        }

        //
        // The requested action can be overridden by a request parameter.  In this case, we parse the action from
        // the request parameter and forward to a URI constructed from it.  If this happens, just return.
        //
        if ( processActionOverride( request, response ) ) return;

        //
        // Process any direct request for a page flow by forwarding to its "begin" action.
        //
        if ( processPageFlowRequest( request, response, uri ) ) return;

        //
        // Get the FlowController for this request (page flow or shared flow), and cache it in the request.
        //
        String flowControllerClassName = InternalUtils.getFlowControllerClassName( moduleConfig );

        if ( flowControllerClassName == null &&
             ! ( moduleConfig instanceof AutoRegisterActionServlet.MissingRootModuleControllerConfig ) )
        {
            //
            // If this isn't a blank module initialized in place of a missing root PageFlowController, emit a warning
            // about the missing controllerClass property.
            //
            if(LOG.isWarnEnabled())
                LOG.warn( "Struts module " + moduleConfig.getPrefix() + " is configured to use " + getClass().getName()
                    + " as the request processor, but the <controller> element does not contain a <set-property>"
                    + " for \"controllerClass\".  Page Flow actions in this module may not be handled correctly." );
        }

        //
        // Look up or create the appropriate SharedFlowControllers if they don't exist.
        //
        if ( LOG.isInfoEnabled() )
            LOG.info( "Attempting to instantiate SharedFlowControllers for request " + request.getRequestURI() );

        FlowController currentFlowController = null;

        try
        {
            RequestContext requestContext = new RequestContext( request, response );
            Map/*< String, SharedFlowController >*/ sharedFlows =
                    _flowControllerFactory.getSharedFlowsForRequest( requestContext );

            /* todo: why are the shared flow / global app objects loaded for data binding here? */
            ImplicitObjectUtil.loadSharedFlow( request, sharedFlows );
            ImplicitObjectUtil.loadGlobalApp( request, PageFlowUtils.getGlobalApp( request ) );

            if ( flowControllerClassName != null )
            {
                currentFlowController = getFlowController( requestContext, flowControllerClassName );
                PageFlowRequestWrapper.get( request ).setCurrentFlowController( currentFlowController );
            }
            else
            {
                PageFlowRequestWrapper.get( request ).setCurrentFlowController( null );
            }
        }
        catch ( ClassNotFoundException e )
        {
            LOG.error( "Could not find FlowController class " + flowControllerClassName, e );
            ServletUtils.throwServletException(e);
        }
        catch ( InstantiationException e )
        {
            LOG.error( "Could not instantiate FlowController of type " + flowControllerClassName, e );
            ServletUtils.throwServletException(e);
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( "Could not instantiate FlowController of type " + flowControllerClassName, e );
            ServletUtils.throwServletException(e);
        }

        //
        // Get the page flow for this request.
        //
        PageFlowController jpf = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );

        //
        // Remove any current JavaServer Faces backing bean.  We have "left" any JSF page and are now processing a
        // Page Flow action.
        //
        InternalUtils.removeCurrentFacesBackingBean( request, servletContext );

        //
        // Set up implicit objects used by the expression language in simple actions and in declarative validation.
        //
        ImplicitObjectUtil.loadImplicitObjects( request, response, servletContext, jpf );

        try
        {
            super.process( request, response );
        }
        catch ( UnhandledException unhandledException )
        {
            // If we get here, then we've already tried to find an exception handler.  Just throw.
            rethrowUnhandledException( unhandledException );
        }
        catch ( ServletException servletEx )
        {
            // If a ServletException escapes out of any of the processing methods, let the current FlowController handle it.
            if ( ! handleException( servletEx, currentFlowController, request, response ) )
                throw servletEx;
        }
        catch ( IOException ioe )
        {
            // If an IOException escapes out of any of the processing methods, let the current FlowController handle it.
            if ( ! handleException( ioe, currentFlowController, request, response ) )
                throw ioe;
        }
        catch ( Throwable th )
        {
            // If a Throwable escapes out of any of the processing methods, let the current FlowController handle it.
            if ( ! handleException( th, currentFlowController, request, response ) )
            {
                if ( th instanceof Error )
                    throw ( Error ) th;
                ServletUtils.throwServletException(th);
            }
        }
    }

    private FlowController getFlowController( RequestContext requestContext, String fcClassName )
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class fcClass = _flowControllerFactory.getFlowControllerClass( fcClassName );
        HttpServletRequest request = requestContext.getHttpRequest();
        HttpServletResponse response = requestContext.getHttpResponse();

        if ( PageFlowController.class.isAssignableFrom( fcClass ) )
        {
            PageFlowController current = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );

            if ( current != null && current.getClass().equals( fcClass ) )
            {
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "Using current page flow: " + current );
                }

                //
                // Reinitialize transient data that may have been lost on session failover.
                //
                current.reinitialize( request, response, getServletContext() );
                return current;
            }

            return _flowControllerFactory.createPageFlow( new RequestContext( request, response ), fcClass );
        }
        else
        {
            assert SharedFlowController.class.isAssignableFrom( fcClass ) : fcClass.getName();

            SharedFlowController current = PageFlowUtils.getSharedFlow( fcClass.getName(), request );

            if ( current != null )
            {
                current.reinitialize( request, response, getServletContext() );
                return current;
            }

            return _flowControllerFactory.createSharedFlow( new RequestContext( request, response ), fcClass );
        }
    }

    private boolean handleException( Throwable th, FlowController fc, HttpServletRequest request,
                                     HttpServletResponse response )
    {
        if ( fc != null )
        {
            try
            {
                ActionMapping mapping = InternalUtils.getCurrentActionMapping( request );
                ActionForm form = InternalUtils.getCurrentActionForm( request );
                ActionForward fwd = fc.handleException( th, mapping, form, request, response );
                processForwardConfig( request, response, fwd );
                return true;
            }
            catch ( UnhandledException unhandledException )
            {
                if ( LOG.isInfoEnabled() )
                {
                    LOG.info( "This exception was unhandled by any exception handler.", unhandledException );
                }

                return false;
            }
            catch ( Throwable t )
            {
                LOG.error( "Exception while handling exception " + th.getClass().getName()
                            + ".  The original exception will be thrown.", t );
                return false;
            }
        }

        return false;
    }

    /**
     * Process any direct request for a page flow by forwarding to its "begin" action.
     *
     * @param request the current HttpServletRequest
     * @param response the current HttpServletResponse
     * @param uri the decoded request URI
     * @return <code>true</code> if the request was for a page flow, in which case it was forwarded.
     * @throws IOException
     * @throws ServletException
     */
    protected boolean processPageFlowRequest( HttpServletRequest request, HttpServletResponse response, String uri )
        throws IOException, ServletException
    {
        //
        // Forward requests for *.jpf to the "begin" action within the appropriate Struts module.
        //
        if ( FileUtils.osSensitiveEndsWith( uri, PageFlowConstants.PAGEFLOW_EXTENSION ) )
        {
            //
            // Make sure the current module config matches the request URI.  If not, this could be an
            // EAR where the struts-config.xml wasn't included because of a compilation error.
            //
            String modulePath = PageFlowUtils.getModulePath( request );
            if ( ! moduleConfig.getPrefix().equals( modulePath ) )
            {
                if ( LOG.isErrorEnabled() )
                {
                    InternalStringBuilder msg = new InternalStringBuilder( "No module configuration registered for " );
                    msg.append( uri ).append( " (module path " ).append( modulePath ).append( ")." );
                    LOG.error( msg.toString() );
                }

                if ( modulePath.length() == 0 ) modulePath = "/";
                InternalUtils.sendDevTimeError( "PageFlow_NoModuleConf", null,
                                                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request, response,
                                                getServletContext(), new Object[]{ uri, modulePath } );
                return true;
            }

            // Make sure that the requested pageflow matches the pageflow for the directory.
            ActionMapping beginMapping = getBeginMapping();
            if ( beginMapping != null )
            {
                String desiredType = beginMapping.getParameter();
                desiredType = desiredType.substring( desiredType.lastIndexOf( '.' ) + 1 ) + PAGEFLOW_EXTENSION;
                String requestedType = InternalUtils.getDecodedServletPath( request );
                requestedType = requestedType.substring( requestedType.lastIndexOf( '/' ) + 1 );

                if ( ! requestedType.equals( desiredType ) )
                {
                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "Wrong .jpf requested for this directory: got " + requestedType
                                   + ", expected " + desiredType );
                    }

                    if ( LOG.isErrorEnabled() )
                    {
                        InternalStringBuilder msg = new InternalStringBuilder( "Wrong .jpf requested for this directory: got " );
                        msg.append( requestedType ).append( ", expected " ).append( desiredType ).append( '.' );
                        LOG.error( msg.toString() );
                    }

                    InternalUtils.sendDevTimeError( "PageFlow_WrongPath", null,
                                                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, request, response,
                                                    getServletContext(), new Object[]{ requestedType, desiredType } );

                    return true;
                }
            }

            uri = PageFlowUtils.getBeginActionURI( uri );

            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Got request for " + request.getRequestURI() + ", forwarding to " + uri );
            }

            doForward( uri, request, response );
            return true;
        }

        return false;
    }

    /**
     * A MultipartRequestWrapper that we cache in the outer request once we've handled the multipart request once.
     * It extends the base Struts MultipartRequestWrapper by being aware of ScopedRequests; for ScopedRequests, it
     * filters the parameter names accordingly.
     */
    private static class RehydratedMultipartRequestWrapper extends MultipartRequestWrapper
    {
        public RehydratedMultipartRequestWrapper( HttpServletRequest req )
        {
            super( req );

            MultipartRequestHandler handler = MultipartRequestUtils.getCachedMultipartHandler( req );

            if ( handler != null )
            {
                ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest( req );
                Map textElements = handler.getTextElements();
                parameters = scopedRequest != null ? scopedRequest.filterParameterMap( textElements ) : textElements;
            }
        }
    }

    public void process( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        int localRequestCount = -1;

        if ( LOG.isTraceEnabled() )
        {
            localRequestCount = ++requestNumber;
            LOG.trace( "------------------------------- Start Request #" +
                localRequestCount +
                " -----------------------------------" );
        }

        //
        // First reinitialize the reloadable class handler.  This will bounce a classloader if necessary.
        //
        ServletContext servletContext = getServletContext();
        _handlers.getReloadableClassHandler().reloadClasses( new RequestContext( request, response ) );

        //
        // Get the chain of pre-request interceptors.
        //
        RequestInterceptorContext context = new RequestInterceptorContext( request, response, getServletContext() );
        List/*< Interceptor >*/ interceptors = context.getRequestInterceptors();

        //
        // Execute pre-request interceptors
        //
        try
        {
            Interceptors.doPreIntercept( context, interceptors );

            if ( context.requestWasCancelled() )
            {
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug ( "Interceptor " + context.getOverridingInterceptor() + " cancelled the request." );
                }

                return;
            }
        }
        catch ( InterceptorException e )
        {
            ServletUtils.throwServletException(e);
        }

        //
        // Initialize the ServletContext in the request.  Often, we need access to the ServletContext when we only
        // have a ServletRequest.
        //
        InternalUtils.setServletContext( request, servletContext );

        //
        // Callback to the servlet container adapter.
        //
        PageFlowEventReporter er = _servletContainerAdapter.getEventReporter();
        _servletContainerAdapter.beginRequest( request, response );
        RequestContext requestContext = new RequestContext( request, response );
        er.beginActionRequest( requestContext );
        long startTime = System.currentTimeMillis();

        // Register the default URLRewriter
        URLRewriterService.registerURLRewriter( 0, request, new DefaultURLRewriter() );

        //
        // A ServletContext may have synchronization associated with getting attributes.
        // This could be a bottleneck under load for an app with pages that require lots
        // of URL rewriting. To improve performance add the template factory and formatter
        // to the request.
        //
        if (URLTemplatesFactory.getURLTemplatesFactory(request) == null) {
            URLTemplatesFactory.initServletRequest(request, URLTemplatesFactory.getURLTemplatesFactory(servletContext));
        }
        if (TemplatedURLFormatter.getTemplatedURLFormatter(request) == null) {
            TemplatedURLFormatter.initServletRequest(request, TemplatedURLFormatter.getTemplatedURLFormatter(servletContext));
        }

        PageFlowRequestWrapper rw = PageFlowRequestWrapper.unwrap( request );
        boolean isForwardedRequest = rw != null && rw.isForwardedRequest();

        try
        {
            processInternal( request, response );
        }
        finally
        {
            //
            // If this is not a forwarded request, then commit any session-scoped changes that were stored in the
            // request.
            //
            if ( ! isForwardedRequest )
            {
                Handlers.get( getServletContext() ).getStorageHandler().applyChanges( requestContext );
            }

            //
            // Callback to the server adapter.
            //
            _servletContainerAdapter.endRequest( request, response );
            long timeTaken = System.currentTimeMillis() - startTime;
            er.endActionRequest( requestContext, timeTaken );
        }

        //
        // Execute post-request interceptors
        //
        try
        {
            Interceptors.doPostIntercept( context, interceptors );
        }
        catch ( InterceptorException e )
        {
            ServletUtils.throwServletException(e);
        }

        if ( LOG.isTraceEnabled() )
        {
            LOG.trace( "-------------------------------- End Request #" +
                localRequestCount +
                " ------------------------------------" );
        }
    }

    /**
     * If this is a multipart request, wrap it with a special wrapper.  Otherwise, return the request unchanged.
     *
     * @param request The HttpServletRequest we are processing
     */
    protected HttpServletRequest processMultipart( HttpServletRequest request )
    {
        if ( ! "POST".equalsIgnoreCase( request.getMethod() ) ) return request;

        String contentType = request.getContentType();
        if ( contentType != null && contentType.startsWith( "multipart/form-data" ) )
        {
            PageFlowRequestWrapper pageFlowRequestWrapper = PageFlowRequestWrapper.get( request );

            //
            // We may have already gotten a multipart wrapper during process().  If so, use that.
            //
            MultipartRequestWrapper cachedWrapper = pageFlowRequestWrapper.getMultipartRequestWrapper();

            if ( cachedWrapper != null && cachedWrapper.getRequest() == request ) return cachedWrapper;

            try
            {
                //
                // First, pre-handle the multipart request.  This parses the stream and caches a single
                // MultipartRequestHandler in the outer request, so we can create new wrappers around it at will.
                //
                MultipartRequestUtils.preHandleMultipartRequest( request );
            }
            catch ( ServletException e )
            {
                LOG.error( "Could not parse multipart request.", e.getRootCause() );
                return request;
            }

            MultipartRequestWrapper ret = new RehydratedMultipartRequestWrapper( request );
            pageFlowRequestWrapper.setMultipartRequestWrapper( ret );
            return ret;
        }
        else
        {
            return request;
        }

    }

    protected ActionMapping getBeginMapping()
    {
        return ( ActionMapping ) moduleConfig.findActionConfig( BEGIN_ACTION_PATH );
    }

    private static String makeRedirectedRequestAttrsKey( String webappRelativeURI, String hash )
    {
        return REDIRECT_REQUEST_ATTRS_PREFIX + hash + webappRelativeURI;
    }

    private static void rethrowUnhandledException( UnhandledException ex )
        throws ServletException
    {
        Throwable rootCause = ex.getRootCause();

        //
        // We shouldn't (and don't need to) wrap Errors or RuntimeExceptions.
        //
        if ( rootCause instanceof Error )
        {
            throw ( Error ) rootCause;
        }
        else if ( rootCause instanceof RuntimeException )
        {
            throw ( RuntimeException ) rootCause;
        }

        throw ex;
    }

    public ActionForward processException( HttpServletRequest request, HttpServletResponse response,
                                           Exception ex, ActionForm form, ActionMapping mapping )
        throws IOException, ServletException
    {
        //
        // Note: we should only get here if FlowController.handleException itself throws an exception, or if the user
        // has merged in Struts code that delegates to an action/exception-handler outside of the pageflow.
        //
        // If this is an UnhandledException thrown from FlowController.handleException, don't try to re-handle it here.
        //

        if ( ex instanceof UnhandledException )
        {
            rethrowUnhandledException( ( UnhandledException ) ex );
            assert false;   // rethrowUnhandledException always throws something.
            return null;
        }
        else
        {
            return super.processException( request, response, ex, form, mapping );
        }
    }

    /**
     * Used by {@link PageFlowRequestProcessor#processMapping}.  Its main job is to return
     * {@link ExceptionHandledAction} as the type.
     */
    protected static class ExceptionHandledActionMapping extends ActionMapping
    {
        private ActionForward _fwd;

        public ExceptionHandledActionMapping( String actionPath, ActionForward fwd )
        {
            setPath( actionPath );
            _fwd = fwd;
        }

        public String getType()
        {
            return ExceptionHandledAction.class.getName();
        }

        public ActionForward getActionForward()
        {
            return _fwd;
        }

        public boolean getValidate()
        {
            return false;
        }
    }

    /**
     * Used by {@link PageFlowRequestProcessor#processMapping}.  This action simply returns the ActionForward stored in the
     * ExceptionHandledActionMapping that's passed in.
     */
    public static class ExceptionHandledAction extends Action
    {
        public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                      HttpServletResponse response )
        {
            assert mapping instanceof ExceptionHandledActionMapping : mapping.getClass().getName();

            return ( ( ExceptionHandledActionMapping ) mapping ).getActionForward();
        }
    }

    private boolean isCorrectFormType( Class formBeanClass, ActionMapping mapping )
    {
        assert mapping.getName() != null : "cannot pass an ActionMapping that has no form bean";
        Class cachedFormBeanClass = ( Class ) _formBeanClasses.get( mapping.getName() );
        return isCorrectFormType( formBeanClass, cachedFormBeanClass, mapping );
    }

    private boolean isCorrectFormType( Class formBeanClass, Class actionMappingFormBeanClass, ActionMapping mapping )
    {
        if ( actionMappingFormBeanClass != null )
        {
            return actionMappingFormBeanClass .isAssignableFrom( formBeanClass );
        }
        else
        {
            //
            // The form bean class couldn't be loaded at init time -- just check against the class name.
            //
            FormBeanConfig mappingFormBean = moduleConfig.findFormBeanConfig( mapping.getName() );
            String formClassName = formBeanClass.getName();

            if ( mappingFormBean != null && mappingFormBean.getType().equals( formClassName ) ) return true;

            if ( mapping instanceof PageFlowActionMapping )
            {
                String desiredType = ( ( PageFlowActionMapping ) mapping ).getFormClass();
                if ( formClassName.equals( desiredType ) ) return true;
            }
        }

        return false;
    }

    private ActionMapping checkTransaction( HttpServletRequest request, HttpServletResponse response,
                                            ActionMapping mapping, String actionPath )
        throws IOException
    {
        if ( mapping instanceof PageFlowActionMapping && ( ( PageFlowActionMapping ) mapping ).isPreventDoubleSubmit() )
        {
            if ( ! TokenProcessor.getInstance().isTokenValid( request, true ) )
            {
                FlowController currentFC = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
                String actionName = InternalUtils.getActionName( mapping );
                DoubleSubmitException ex = new DoubleSubmitException( actionName, currentFC );

                if ( currentFC != null )
                {
                    try
                    {
                        ActionForward fwd = currentFC.handleException( ex, mapping, null, request, response );
                        return new ExceptionHandledActionMapping( actionPath, fwd );
                    }
                    catch ( ServletException servletException)
                    {
                        LOG.error( "Exception occurred while handling " + ex.getClass().getName(), servletException );
                    }
                }

                ex.sendResponseErrorCode( response );
                return null;
            }
        }

        return mapping;
    }

    public void init( ActionServlet actionServlet, ModuleConfig mc )
        throws ServletException
    {
        super.init( actionServlet, mc );

        ServletContext servletContext = getServletContext();

        //
        // Cache a reference to the ServletContainerAdapter, the Handlers, and the LegacySettings.
        //
        _servletContainerAdapter = AdapterManager.getServletContainerAdapter( servletContext );
        _legacySettings = LegacySettings.get( servletContext );
        _handlers = Handlers.get( servletContext );
        _flowControllerFactory = FlowControllerFactory.get( servletContext );

        // Initialize delegating action mappings and exception configs.
        InternalUtils.initDelegatingConfigs(mc, servletContext);
        
        // Cache a list of overloaded actions for each overloaded action path (actions are overloaded by form bean type).
        cacheOverloadedActionMappings();

        // Cache the form bean Classes by form bean name.
        cacheFormClasses();

        // Initialize the request interceptors and action interceptors.
        ActionInterceptorContext.init( servletContext );
        RequestInterceptorContext.init( servletContext );

        ensurePageServletFilter();
        assert _pageServletFilter != null;
    }

    private void cacheOverloadedActionMappings()
    {
        ActionConfig[] actionConfigs = moduleConfig.findActionConfigs();

        for ( int i = 0; i < actionConfigs.length; i++ )
        {
            ActionConfig actionConfig = actionConfigs[i];

            if ( actionConfig instanceof PageFlowActionMapping )
            {
                PageFlowActionMapping mapping = ( PageFlowActionMapping ) actionConfig;
                String unqualifiedActionPath = ( ( PageFlowActionMapping ) actionConfig ).getUnqualifiedActionPath();

                if ( unqualifiedActionPath != null )
                {
                    List/*< ActionMapping >*/ overloaded = ( List ) _overloadedActions.get( unqualifiedActionPath );

                    if ( overloaded == null )
                    {
                        overloaded = new ArrayList/*< ActionMapping >*/();
                        _overloadedActions.put( unqualifiedActionPath, overloaded );
                    }

                    overloaded.add( mapping );
                }
            }
        }
    }

    private void cacheFormClasses()
    {
        FormBeanConfig[] formBeans = moduleConfig.findFormBeanConfigs();
        ReloadableClassHandler rch = _handlers.getReloadableClassHandler();

        for ( int i = 0; i < formBeans.length; i++ )
        {
            FormBeanConfig formBeanConfig = formBeans[i];
            String formType = InternalUtils.getFormBeanType( formBeanConfig );

            try
            {
                Class formBeanClass = rch.loadClass( formType );
                _formBeanClasses.put( formBeanConfig.getName(), formBeanClass );
            }
            catch ( ClassNotFoundException e )
            {
                LOG.error( "Could not load class " + formType + " referenced from form bean config "
                            + formBeanConfig.getName() + " in Struts module " + moduleConfig );
            }
        }
    }

    /**
     * Read component instance mapping configuration file.
     * This is where we read files properties.
     */

    protected void initDefinitionsMapping() throws ServletException
    {
        definitionsFactory = null;
        TilesUtilImpl tilesUtil = TilesUtil.getTilesUtil();

        if ( tilesUtil instanceof TilesUtilStrutsImpl )
        {
            // Retrieve and set factory for this modules
            definitionsFactory =
                    ( ( TilesUtilStrutsImpl ) tilesUtil ).getDefinitionsFactory( getServletContext(), moduleConfig );

            if ( definitionsFactory == null && log.isDebugEnabled() )
            {
                log.debug( "Definition Factory not found for module: '"
                           + moduleConfig.getPrefix() );
            }
        }
    }

    public ActionMapping processMapping( HttpServletRequest request, HttpServletResponse response, String path )
        throws IOException
    {
        PageFlowRequestWrapper rw = PageFlowRequestWrapper.get(request);
        FlowController fc = rw.getCurrentFlowController();
        Object forwardedForm = InternalUtils.unwrapFormBean( InternalUtils.getForwardedFormBean( request, false ) );

        //
        // First, see if this is a request for a shared flow action.  The shared flow's name (as declared by the
        // current page flow) will precede the dot.
        //
        if ( fc != null && ! processSharedFlowMapping( request, response, path, fc ) ) return null;

        //
        // Look for a form-specific action path.  This is used when there are two actions with the same
        // name, but different forms (in nesting).
        //
        Class forwardedFormClass = null;

        if ( forwardedForm != null )
        {
            forwardedFormClass = forwardedForm.getClass();
            List/*< ActionMapping >*/ possibleMatches = ( List ) _overloadedActions.get( path );
            ActionMapping bestMatch = null;

            //
            // Troll through the overloaded actions for the given path.  Look for the one whose form bean class is
            // exactly the class of the forwarded form; failing that, look for one that's assignable from the class
            // of the forwarded form.
            //
            for ( int i = 0; possibleMatches != null && i < possibleMatches.size(); ++i )
            {
                ActionMapping possibleMatch = ( ActionMapping ) possibleMatches.get( i );
                assert possibleMatch instanceof PageFlowActionMapping : possibleMatch.getClass();
                Class cachedFormBeanClass = ( Class ) _formBeanClasses.get( possibleMatch.getName() );

                if ( forwardedFormClass.equals( cachedFormBeanClass ) )
                {
                    bestMatch = possibleMatch;
                    break;
                }
                if ( bestMatch == null && isCorrectFormType( forwardedFormClass, possibleMatch ) )
                {
                    bestMatch = possibleMatch;
                }
            }

            if ( bestMatch != null )
            {
                request.setAttribute( Globals.MAPPING_KEY, bestMatch );

                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "Found form-specific action mapping " + bestMatch.getPath() + " for " + path
                                + ", form " + forwardedFormClass.getName() );
                }

                return checkTransaction( request, response, bestMatch, path );
            }
        }

        //
        // Look for a directly-defined mapping for this path.
        //
        ActionMapping mapping = ( ActionMapping ) moduleConfig.findActionConfig( path );

        if ( mapping != null )
        {
            boolean wrongForm = false;

            //
            // We're going to bail out if there is a forwarded form and this mapping requires a different form type.
            //
            if ( forwardedForm != null )
            {
                boolean mappingHasNoFormBean = mapping.getName() == null;
                wrongForm = mappingHasNoFormBean || ! isCorrectFormType( forwardedFormClass, mapping );
            }

            if ( ! wrongForm )
            {
                request.setAttribute( Globals.MAPPING_KEY, mapping );
                return checkTransaction( request, response, mapping, path );
            }
        }

        //
        // Look for a mapping for "unknown" paths
        //
        ActionMapping unknown = getUnknownActionFromConfig( moduleConfig );
        if ( unknown != null )
        {
            request.setAttribute( Globals.MAPPING_KEY, unknown );
            return checkTransaction( request, response, unknown, path );
        }

        // If we haven't already tried this action on a shared flow or on Global.app, see if it's in the Global.app
        // module.  If it is, forward to it.  (Global.app is a deprecated fallback for unhandled actions.)
        String errorServletPath = rw.getOriginalServletPath();
        ModuleConfig globalApp = null;

        if (errorServletPath == null
             && (globalApp = InternalUtils.ensureModuleConfig(GLOBALAPP_MODULE_CONTEXT_PATH, getServletContext())) != null
             && (globalApp.findActionConfig(path) != null || getUnknownActionFromConfig(globalApp) != null))
        {
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying Global.app for unhandled action " + path);
            }

            errorServletPath = InternalUtils.getDecodedServletPath( request );
            rw.setOriginalServletPath( errorServletPath );
            String globalAppURI = GLOBALAPP_MODULE_CONTEXT_PATH + path + ACTION_EXTENSION;
            try
            {
                doForward( globalAppURI, request, response );
            }
            catch ( ServletException e )
            {
                LOG.error( "Could not forward to Global.app path " + globalAppURI );
            }
            return null;
        }
        else
        {
            // If we are currently returning from nesting, then it is possible that the given action may actually be
            // found in a page flow that's farther down the nesting stack.  This can happen when the browser back button
            // is used; see http://issues.apache.org/jira/browse/BEEHIVE-1024 .
            //
            // In this case, we simply forward to the action in the page flow in which it is found.  The runtime will
            // take care of popping page flows until the right instance is made the current page flow.
            if (rw.isReturningFromNesting()) {
                ModuleConfig found = PageFlowStack.get(request, getServletContext()).findActionInStack(path);
                
                if (found != null) {
                    String servletPath = found.getPrefix() + path + ACTION_EXTENSION;
                
                    try {
                        doForward(servletPath, request, response);
                    }
                    catch ( ServletException e ) {
                        LOG.error( "Could not forward to path " + servletPath );
                    }
                    return null;
                }
            }
            
            // If the error action path has a slash in it, then it's not local to the current page flow.  Replace
            // it with the original servlet path.
            if ( errorServletPath != null && path.indexOf( '/' ) > 0 ) path = errorServletPath;
            return processUnresolvedAction( path, request, response, forwardedForm );
        }
    }

    private ActionMapping getUnknownActionFromConfig( ModuleConfig mc )
    {
        ActionConfig configs[] = mc.findActionConfigs();
        for ( int i = 0; i < configs.length; i++ )
        {
            if ( configs[i].getUnknown() )
            {
                return ( ActionMapping ) configs[i];
            }
        }
        return null;
    }

    protected boolean processSharedFlowMapping( HttpServletRequest request, HttpServletResponse response,
                                                String actionPath, FlowController currentFlowController )
            throws IOException
    {
        if ( currentFlowController.isPageFlow() )
        {
            int dot = actionPath.indexOf( '.' );

            if ( dot != -1 )
            {
                Map/*< String, SharedFlowController >*/ sharedFlows = PageFlowUtils.getSharedFlows( request );
                if ( sharedFlows == null ) return true;
                if ( dot == actionPath.length() - 1 ) return true;     // empty action name
                assert actionPath.length() > 0 && actionPath.charAt( 0 ) == '/' : actionPath;
                String sharedFlowName = actionPath.substring( 1, dot );
                SharedFlowController sf = ( SharedFlowController ) sharedFlows.get( sharedFlowName );

                if ( sf != null )
                {
                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "Forwarding to shared flow " + sf.getDisplayName() + " to handle action \""
                                    + actionPath + "\"." );
                    }

                    //
                    // Save the original request URI, so if the action fails on the shared flow, too, then we can
                    // give an error message that includes *this* URI, not the shared flow URI.
                    //
                    PageFlowRequestWrapper.get( request ).setOriginalServletPath( InternalUtils.getDecodedServletPath( request ) );

                    //
                    // Construct a URI that is [shared flow module path] + [base action path] + [action-extension (.do)]
                    //
                    int lastSlash = actionPath.lastIndexOf( '/' );
                    assert lastSlash != -1 : actionPath;
                    InternalStringBuilder uri = new InternalStringBuilder( sf.getModulePath() );
                    uri.append( '/' );
                    uri.append( actionPath.substring( dot + 1 ) );
                    uri.append( ACTION_EXTENSION );

                    try
                    {
                        doForward( uri.toString(), request, response );
                        return false;
                    }
                    catch ( ServletException e )
                    {
                        LOG.error( "Could not forward to shared flow URI " + uri, e );
                    }
                }
            }
        }

        return true;
    }

    protected ActionMapping processUnresolvedAction( String actionPath, HttpServletRequest request,
                                                     HttpServletResponse response, Object returningForm )
        throws IOException
    {
                if ( LOG.isInfoEnabled() )
        {
            InternalStringBuilder msg = new InternalStringBuilder( "Action \"" ).append( actionPath );
            LOG.info( msg.append( "\" was also unhandled by Global.app." ).toString() );
        }

        //
        // If there's a PageFlowController for this request, try and let it handle an
        // action-not-found exception.  Otherwise, let Struts print out its "invalid path"
        // message.
        //
        FlowController fc = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );

        try
        {
            if ( fc != null )
            {
                Exception ex = new ActionNotFoundException( actionPath, fc, returningForm );
                InternalUtils.setCurrentModule( fc.getModuleConfig(), request );
                ActionForward result = fc.handleException( ex, null, null, request, response );
                return new ExceptionHandledActionMapping( actionPath, result );
            }
        }
        catch ( ServletException e )
        {
            // ignore this -- just let Struts do its thing.

            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( e );
            }
        }

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Couldn't handle an ActionNotFoundException -- delegating to Struts" );
        }

        return super.processMapping( request, response, actionPath );
    }

    protected boolean processRoles( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping )
        throws IOException, ServletException
    {
        //
        // If there are no required roles for this action, just return.
        //
        String roles[] = mapping.getRoleNames();
        if ( roles == null || roles.length < 1 )
        {
            return true;
        }

        // Check the current user against the list of required roles
        FlowController fc = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
        FlowControllerHandlerContext context = new FlowControllerHandlerContext( request, response, fc );

        for ( int i = 0; i < roles.length; i++ )
        {
            if ( _handlers.getLoginHandler().isUserInRole( context, roles[i] ) )
            {
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( " User " + request.getRemoteUser() + " has role '" + roles[i] + "', granting access" );
                }

                return true;
            }
        }

        // The current user is not authorized for this action
        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( " User '" + request.getRemoteUser() + "' does not have any required role, denying access" );
        }

        //
        // Here, Struts sends an HTTP error.  We try to let the current page flow handle a relevant exception.
        //
        LoginHandler loginHandler = _handlers.getLoginHandler();
        String actionName = InternalUtils.getActionName( mapping );
        FlowController currentFC = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
        PageFlowException ex;

        if ( loginHandler.getUserPrincipal( context ) == null )
        {
            ex = currentFC.createNotLoggedInException( actionName, request );
        }
        else
        {
            ex = new UnfulfilledRolesException( mapping.getRoleNames(), mapping.getRoles(), actionName, currentFC );
        }

        if ( currentFC != null )
        {
            ActionForward fwd = currentFC.handleException( ex, mapping, null, request, response );
            processForwardConfig( request, response, fwd );
        }
        else
        {
            ( ( ResponseErrorCodeSender ) ex ).sendResponseErrorCode( response );
        }

        return false;
    }

    private static String addScopeParams( String url, HttpServletRequest request )
    {
        //
        // If the current request is scoped, add the right request parameter to the URL.
        //
        String scopeID = request.getParameter( ScopedServletUtils.SCOPE_ID_PARAM );
        if ( scopeID != null )
        {
            return InternalUtils.addParam( url, ScopedServletUtils.SCOPE_ID_PARAM, scopeID );
        }
        else
        {
            return url;
        }
    }

    /**
     * This override of the base method ensures that absolute URIs don't get the context
     * path prepended, and handles forwards to special things like return-to="currentPage".
     */
    protected void processForwardConfig( HttpServletRequest request, HttpServletResponse response, ForwardConfig fwd )
            throws IOException, ServletException
    {
        ServletContext servletContext = getServletContext();
        ForwardRedirectHandler fwdRedirectHandler = _handlers.getForwardRedirectHandler();
        FlowController fc = PageFlowRequestWrapper.get( request ).getCurrentFlowController();
        FlowControllerHandlerContext context = new FlowControllerHandlerContext( request, response, fc );

        // Register this module as the one that's handling the action.
        if ( fc != null )
        {
            InternalUtils.setForwardingModule( request, fc.getModulePath() );
        }

        //
        // The following is similar to what's in super.processForwardConfig(), but it avoids putting
        // a slash in front of absolute URLs (e.g., ones that start with "http:").
        //
        if ( fwd != null )
        {
            if ( LOG.isDebugEnabled() ) LOG.debug( "processForwardConfig(" + fwd + ')' );

            //
            // Try to process a tiles definition. If the forward doesn't contain a
            // a tiles definition, continue on.
            //
            if ( processTilesDefinition( fwd.getPath(), fwd.getContextRelative(), request, response ) )
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug( "  '" + fwd.getPath() + "' - processed as definition" );
                }
                return;
            }

            //
            // If this is a "special" page flow forward, create a Forward to handle it and pass
            // it to the current page flow.  This should only happen when processValidate()
            // calls this method (or if a plain Struts action forwards to this forward) --
            // otherwise, the page flow should be using a Forward already.
            //
            if ( fwd instanceof PageFlowActionForward )
            {
                ActionMapping mapping = ( ActionMapping ) request.getAttribute( Globals.MAPPING_KEY );
                assert mapping != null;
                ActionForm form = InternalUtils.getFormBean( mapping, request );
                Forward pfFwd = new Forward( ( ActionForward ) fwd, servletContext );
                ActionForwardHandler handler = _handlers.getActionForwardHandler();
                fwd = handler.processForward( context, pfFwd, mapping, null, InternalUtils.getActionName( mapping ),
                                              null, form );
            }

            String path = fwd.getPath();
            boolean startsWithSlash = path.length() > 0 && path.charAt( 0 ) == '/';

            //
            // If the URI is absolute (e.g., starts with "http:"), do a redirect to it no matter what.
            //
            if ( FileUtils.isAbsoluteURI( path ) )
            {
                fwdRedirectHandler.redirect( context, addScopeParams( path, request ) );
            }
            else if ( fwd.getRedirect() )
            {
                String redirectURI;

                if ( startsWithSlash && fwd instanceof Forward && ( ( Forward ) fwd ).isExplicitPath() )
                {
                    redirectURI = path;
                }
                else if ( fwd instanceof Forward && ( ( Forward ) fwd ).isExternalRedirect() )
                {
                    assert startsWithSlash : path; // compiler should ensure path starts with '/'
                    redirectURI = path;
                }
                else
                {
                    redirectURI = request.getContextPath() + RequestUtils.forwardURL( request, fwd );
                }

                fwdRedirectHandler.redirect( context, addScopeParams( redirectURI, request ) );
            }
            else
            {
                String fwdURI;

                if ( startsWithSlash && fwd instanceof Forward && ( ( Forward ) fwd ).isExplicitPath() )
                {
                    fwdURI = path;
                }
                else
                {
                    fwdURI = RequestUtils.forwardURL( request, fwd );

                    //
                    // First, see if the current module is a Shared Flow module.  If so, unless this is a forward to
                    // another action in the shared flow, we need to translate the local path so it makes sense (strip
                    // off the shared flow module prefix "/-" and replace it with "/").
                    //
                    ModuleConfig mc = ( ModuleConfig ) request.getAttribute( Globals.MODULE_KEY );

                    if ( InternalUtils.isSharedFlowModule( mc ) && ! fwdURI.endsWith( ACTION_EXTENSION )
                         && fwdURI.startsWith( SHARED_FLOW_MODULE_PREFIX ) )
                    {
                        fwdURI = '/' + fwdURI.substring( SHARED_FLOW_MODULE_PREFIX_LEN );
                    }
                }

                doForward( fwdURI, request, response );
            }
        }
    }

    protected boolean changeScheme( String webappRelativeURI, String scheme, int port,
                                    FlowControllerHandlerContext context )
        throws URISyntaxException, IOException, ServletException
    {
        if ( port == -1 )
        {
            if ( LOG.isWarnEnabled() )
            {
                LOG.warn( "Could not change the scheme to " + scheme + " because the relevant port was not provided "
                           + "by the ServletContainerAdapter." );
                return false;
            }
        }

        //
        // First put all request attributes into the session, so they can be added to the
        // redirected request.
        //
        Map attrs = new HashMap();
        String queryString = null;
        ServletContext servletContext = getServletContext();
        HttpServletRequest request = ( ( RequestContext ) context ).getHttpRequest();

        for ( Enumeration e = request.getAttributeNames(); e.hasMoreElements(); )
        {
            String name = ( String ) e.nextElement();
            attrs.put( name, request.getAttribute( name ) );
        }

        if ( ! attrs.isEmpty() )
        {
            String hash = Integer.toString( request.hashCode() );
            String key = makeRedirectedRequestAttrsKey( webappRelativeURI, hash );
            request.getSession().setAttribute( key, attrs );
            queryString = URLRewriterService.getNamePrefix( servletContext, request, REDIRECT_REQUEST_ATTRS_PARAM )
                          + REDIRECT_REQUEST_ATTRS_PARAM + '=' + hash;
        }


        //
        // Now do the redirect.
        //
        URI redirectURI = new URI( scheme, null, request.getServerName(), port,
                                   request.getContextPath() + webappRelativeURI,
                                   queryString, null );

        ForwardRedirectHandler fwdRedirectHandler = _handlers.getForwardRedirectHandler();
        fwdRedirectHandler.redirect( context, redirectURI.toString() );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Redirected to " + redirectURI );
        }

        return true;
    }

    /**
     * @deprecated Use {@link LegacySettings#shouldDoSecureForwards} instead.
     */
    protected boolean shouldDoSecureForwards()
    {
        return _legacySettings.shouldDoSecureForwards();
    }

    protected void doForward( String uri, HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        boolean securityRedirected = false;
        ServletContext servletContext = getServletContext();
        PageFlowRequestWrapper wrappedRequest = PageFlowRequestWrapper.get( request );

        //
        // As in the TilesRequestProcessor.doForward(), if the response has already been commited,
        // do an include instead.
        //
        if ( ! wrappedRequest.isScopedLookup() && response.isCommitted() )
        {
            doInclude( uri, request, response );
            return;
        }

        FlowController fc = wrappedRequest.getCurrentFlowController();
        FlowControllerHandlerContext context = new FlowControllerHandlerContext( request, response, fc );

        if ( _legacySettings.shouldDoSecureForwards() )
        {
            SecurityProtocol sp = PageFlowUtils.getSecurityProtocol( uri, servletContext, request );

            if ( ! sp.equals( SecurityProtocol.UNSPECIFIED ) )
            {
                try
                {
                    if ( request.isSecure() )
                    {
                        if ( sp.equals( SecurityProtocol.UNSECURE ) )
                        {
                            int listenPort = _servletContainerAdapter.getListenPort( request );
                            securityRedirected = changeScheme( uri, SCHEME_UNSECURE, listenPort, context );
                        }
                    }
                    else
                    {
                        if ( sp.equals( SecurityProtocol.SECURE ) )
                        {
                            int secureListenPort = _servletContainerAdapter.getSecureListenPort( request );
                            securityRedirected = changeScheme( uri, SCHEME_SECURE, secureListenPort, context );
                        }
                    }
                }
                catch ( URISyntaxException e )
                {
                    LOG.error( "Bad forward URI " + uri, e );
                }
            }
        }

        if ( ! securityRedirected )
        {
            if ( ! processPageForward( uri, request, response ) )
            {
                ForwardRedirectHandler fwdRedirectHandler = _handlers.getForwardRedirectHandler();
                fwdRedirectHandler.forward( context, uri );
            }
        }
    }

    /**
     * An opportunity to process a page forward in a different way than performing a server forward.  The default
     * implementation looks for a file on classpath called
     * META-INF/pageflow-page-servlets/<i>path-to-page</i>.properties (e.g.,
     * "/META-INF/pageflow-page-servlets/foo/bar/hello.jsp.properties").  This file contains mappings from
     * <i>platform-name</i> (the value returned by {@link ServletContainerAdapter#getPlatformName}) to the name of a Servlet
     * class that will process the page request.  If the current platform name is not found, the value "default" is
     * tried.  An example file might look like this:
     * <pre>
     *     tomcat=org.apache.jsp.foo.bar.hello_jsp
     *     default=my.servlets.foo.bar.hello
     * </pre>
     * @param pagePath the webapp-relative path to the page, e.g., "/foo/bar/hello.jsp"
     * @param request the current HttpServletRequest
     * @param response the current HttpServletResponse
     * @return <code>true</code> if the method handled the request, in which case it should not be forwarded.
     * @throws IOException
     * @throws ServletException
     */
    private boolean processPageForward( String pagePath, HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException
    {
        Class pageServletClass = ( Class ) _pageServletClasses.get( pagePath );

        if ( pageServletClass == null )
        {
            pageServletClass = Void.class;
            ClassLoader cl = DiscoveryUtils.getClassLoader();
            String path = "META-INF/pageflow-page-servlets" + pagePath + ".properties";
            InputStream in = cl.getResourceAsStream( path );

            if ( in != null )
            {
                String className = null;

                try
                {
                    Properties props = new Properties();
                    props.load( in );
                    className = props.getProperty( _servletContainerAdapter.getPlatformName() );
                    if ( className == null ) className = props.getProperty( "default" );

                    if ( className != null )
                    {
                        pageServletClass = cl.loadClass( className );

                        if ( Servlet.class.isAssignableFrom( pageServletClass ) )
                        {
                            if ( LOG.isInfoEnabled() )
                            {
                                LOG.info( "Loaded page Servlet class " + className + " for path " + pagePath );
                            }
                        }
                        else
                        {
                            pageServletClass = Void.class;
                            LOG.error( "Page Servlet class " + className + " for path " + pagePath
                                        + " does not extend " + Servlet.class.getName() );
                        }
                    }
                }
                catch ( IOException e )
                {
                    LOG.error( "Error while reading " + path, e );
                }
                catch ( ClassNotFoundException e )
                {
                    LOG.error( "Error while loading page Servlet class " + className, e );
                }
            }

            _pageServletClasses.put( pagePath, pageServletClass );
        }

        if ( pageServletClass.equals( Void.class ) )
        {
            return false;
        }

        try
        {
            Servlet pageServlet = ( Servlet ) pageServletClass.newInstance();
            pageServlet.init( new PageServletConfig( pagePath ) );

            ensurePageServletFilter().doFilter( request, response, new PageServletFilterChain( pageServlet ) );
            return true;
        }
        catch ( InstantiationException e )
        {
            LOG.error( "Error while instantiating page Servlet of type " + pageServletClass.getName(), e );
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( "Error while instantiating page Servlet of type " + pageServletClass.getName(), e );
        }

        return false;
    }

    // todo: why does the framework create a subclass of the page flow page filter here?
    private class PageServletFilter extends PageFlowPageFilter
    {
        public PageServletFilter()
        {
            super( getServletContext() );
        }

        /**
         * Accept all file extensions.
         * @return the set of acceptable file extensions
         */
        protected Set getValidFileExtensions()
        {
            return null;    // accept all
        }
    }

    /**
     * Used by {@link PageFlowRequestProcessor#processPageForward} to run a page Servlet.
     */
    private static class PageServletFilterChain implements FilterChain
    {
        private Servlet _pageServlet;

        public PageServletFilterChain( Servlet pageServlet )
        {
            _pageServlet= pageServlet;
        }

        public void doFilter( ServletRequest request, ServletResponse response )
                throws IOException, ServletException
        {
            _pageServlet.service( request, response );
        }
    }

    /**
     * Used by {@link PageFlowRequestProcessor#processPageForward} to initialize a page Servlet.
     */
    private class PageServletConfig implements ServletConfig
    {
        private String _pagePath;

        public PageServletConfig( String pagePath )
        {
            _pagePath = pagePath;
        }

        public String getServletName()
        {
            return _pagePath;
        }

        public ServletContext getServletContext()
        {
            return PageFlowRequestProcessor.this.getServletContext();
        }

        public String getInitParameter( String s )
        {
            return null;
        }

        public Enumeration getInitParameterNames()
        {
            return Collections.enumeration( Collections.EMPTY_LIST );
        }
    }

    /**
     * Set the no-cache headers.  This overrides the base Struts behavior to prevent caching even for the pages.
     */
    protected void processNoCache( HttpServletRequest request, HttpServletResponse response )
    {
        //
        // Set the no-cache headers if:
        //    1) the module is configured for it, or
        //    2) netui-config.xml has an "always" value for <pageflow-config><prevent-cache>, or
        //    3) netui-config.xml has an "inDevMode" value for <pageflow-config><prevent-cache>, and we're not in
        //       production mode.
        //
        boolean noCache = moduleConfig.getControllerConfig().getNocache();

        if ( ! noCache )
        {
            PageFlowConfig pfConfig = ConfigUtil.getConfig().getPageFlowConfig();
            
            if ( pfConfig != null )
            {
                PreventCache preventCache = pfConfig.getPreventCache();
                
                if ( preventCache != null )
                {
                    switch ( preventCache.getValue() )
                    {
                        case PreventCache.INT_ALWAYS:
                            noCache = true;
                            break;
                        case PreventCache.INT_IN_DEV_MODE:
                            noCache = ! _servletContainerAdapter.isInProductionMode();
                            break;
                    }
                }
            }
        }
        
        if ( noCache )
        {
            //
            // The call to PageFlowPageFilter.preventCache() will cause caching to be prevented
            // even when we end up forwarding to a page.  Normally, no-cache headers are lost
            // when a server forward occurs.
            //
            ServletUtils.preventCache( response );
            PageFlowUtils.setPreventCache( request );
        }
    }
    
    private class ActionRunner
        implements ActionInterceptors.ActionExecutor
    {
        RequestInterceptorContext _ctxt;
        private Action _action;
        private ActionForm _formBean;
        private ActionMapping _actionMapping;

        public ActionRunner( RequestInterceptorContext context, Action action, ActionForm formBean,
                             ActionMapping actionMapping )
        {
            _ctxt = context;
            _action = action;
            _formBean = formBean;
            _actionMapping = actionMapping;
        }

        public ActionForward execute()
            throws InterceptorException, ServletException, IOException
        {
            return PageFlowRequestProcessor.super.processActionPerform( _ctxt.getRequest(), _ctxt.getResponse(),
                                                                        _action, _formBean, _actionMapping );
        }
    }

    protected ActionForward processActionPerform( HttpServletRequest request, HttpServletResponse response,
                                                  Action action, ActionForm form, ActionMapping mapping )
            throws IOException, ServletException
    {
        ServletContext servletContext = getServletContext();
        String actionName = InternalUtils.getActionName( mapping );
        ActionInterceptorContext context = null;
        List/*< Interceptor >*/ interceptors = null;
        
        if ( action instanceof FlowControllerAction )
        {
            FlowController fc = ( ( FlowControllerAction ) action ).getFlowController();
            
            if ( fc instanceof PageFlowController )
            {
                PageFlowController pfc = ( PageFlowController ) fc;
                context = new ActionInterceptorContext( request, response, servletContext, pfc, null, actionName );
                interceptors = context.getActionInterceptors();
            }
        }
        
        if ( interceptors != null && interceptors.size() == 0 )
            interceptors = null;
        
        try
        {
            //
            // Run any pre-action interceptors.
            //
            if ( interceptors != null && ! PageFlowRequestWrapper.get( request ).isReturningFromActionIntercept() )
            {
                Interceptors.doPreIntercept( context, interceptors );
                
                if ( context.hasInterceptorForward() )
                {
                    InterceptorForward fwd = context.getInterceptorForward();
                    
                    if ( LOG.isDebugEnabled() )
                    {
                        
                        Interceptor overridingInterceptor = context.getOverridingInterceptor();
                        StringBuffer msg = new StringBuffer();
                        msg.append( "Action interceptor " );
                        msg.append( overridingInterceptor.getClass().getName() );
                        msg.append( " before action " );
                        msg.append( actionName );
                        msg.append( ": forwarding to " );
                        msg.append( fwd != null ? fwd.getPath() : "null [no forward]" );
                        LOG.debug( msg.toString() );
                    }
                    
                    return fwd;
                }
            }
            else
            {
                PageFlowRequestWrapper.get( request ).setReturningFromActionIntercept( false );
            }
            
            //
            // Execute the action.
            //
            RequestInterceptorContext requestContext =
                    context != null ?
                    context :
                    new RequestInterceptorContext( request, response, getServletContext() );
            ActionRunner actionExecutor = new ActionRunner( requestContext, action, form, mapping );
            ActionForward ret = ActionInterceptors.wrapAction( context, interceptors, actionExecutor );
            
            //
            // Run any post-action interceptors.
            //
            if ( interceptors != null )
            {
                context.setOriginalForward( ret );
                Interceptors.doPostIntercept( context, interceptors );
                
                if ( context.hasInterceptorForward() )
                {
                    InterceptorForward fwd = context.getInterceptorForward();
                    
                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "Action interceptor " + context.getOverridingInterceptor().getClass().getName()
                                    + " after action " + actionName + ": forwarding to "
                                    + fwd != null ? fwd.getPath() : "null [no forward]" );
                    }
                    
                    return fwd;
                }
            }
            
            return ret;
        }
        catch ( InterceptorException e )
        {
            ServletUtils.throwServletException(e);
        }

        // should not get here -- either a value is returned or an exception is thrown.
        assert false;
        return null;
    }
    
    void doActionForward( HttpServletRequest request, HttpServletResponse response, ActionForward forward )
        throws IOException, ServletException
    {
        request = PageFlowRequestWrapper.wrapRequest( request );
        processForwardConfig( request, response, forward );
    }

    protected boolean processValidate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      ActionForm form,
                                      ActionMapping mapping)
        throws IOException, ServletException, InvalidCancelException {
        
        //
        // The raw Struts ActionForm doesn't have our logic for enabling declarative validation annotations.
        // If this is what we have, create a wrapper that extends FormData to process validation annotations,
        // but will also invoke the ActionForm's validate().
        //
        if (form != null && ! (form instanceof BaseActionForm)) {
            ActionForm originalForm = form;
            form = new ActionFormValidationWrapper(originalForm);
            form.setServlet(servlet);
            form.setMultipartRequestHandler(originalForm.getMultipartRequestHandler());
        }
        
        return super.processValidate(request, response, form, mapping);
    }
    
    /**
     * Internal method used to ensure that the PageServletFilter is available for use by the request processor.
     * If serialization has occurred, this object is transient in this class and may need to be recreated.
     * @return the page servlet filter
     */
    private PageFlowPageFilter ensurePageServletFilter() {
        if(_pageServletFilter == null)
            _pageServletFilter = new PageServletFilter();

        return _pageServletFilter;
    }

    private static class ActionFormValidationWrapper extends BaseActionForm
    {
        private ActionForm _actionForm;
        
        public ActionFormValidationWrapper(ActionForm actionForm) {
            _actionForm = actionForm;
        }

        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
            return validateBean(_actionForm, mapping.getAttribute(), mapping, request);
        }

        protected ActionErrors getAdditionalActionErrors(ActionMapping mapping, HttpServletRequest request) {
            return _actionForm.validate(mapping, request);
        }
    }
}
