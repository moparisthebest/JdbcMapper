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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.pageflow.config.PageFlowControllerConfig;
import org.apache.beehive.netui.pageflow.config.PageFlowActionFormBean;
import org.apache.beehive.netui.pageflow.config.DelegatingActionMapping;
import org.apache.beehive.netui.pageflow.config.DelegatingExceptionConfig;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.ServletUtils;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.MultipartHandler;
import org.apache.beehive.netui.util.config.bean.PageFlowConfig;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.action.*;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.upload.MultipartRequestWrapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Locale;

public class InternalUtils
        implements PageFlowConstants, InternalConstants
{
    private static final Logger _log = Logger.getInstance( InternalUtils.class );

    private static final String BEA_XMLOBJECT_CLASSNAME = "com.bea.xml.XmlObject";
    private static final String APACHE_XMLOBJECT_CLASSNAME = "org.apache.xmlbeans.XmlObject";
    private static final Class BEA_XMLOBJECT_CLASS = loadClassNonFatal( BEA_XMLOBJECT_CLASSNAME );
    private static final Class APACHE_XMLOBJECT_CLASS = loadClassNonFatal( APACHE_XMLOBJECT_CLASSNAME );
    private static final String LONGLIVED_PAGEFLOWS_ATTR_PREFIX = ATTR_PREFIX + "longLivedPageFlow:";
    private static final String ACTIONOUTPUT_MAP_ATTR = ATTR_PREFIX + "actionOutputs";
    private static final String BINDING_UPDATE_ERRORS_ATTR = ATTR_PREFIX + "bindingUpdateErrors";
    private static final String SHARED_FLOW_CLASSNAME_ATTR = ATTR_PREFIX + "sharedFlowClass";
    private static final String SERVLET_CONTEXT_ATTR = ATTR_PREFIX + "servletContext";
    private static final String AVOID_DIRECT_RESPONSE_OUTPUT_ATTR = ATTR_PREFIX + "_avoidDirectResponseOutput";
    private static final String FORWARDED_FORMBEAN_ATTR = ATTR_PREFIX + "forwardedForm";
    private static final String FORWARDING_MODULE_ATTR = ATTR_PREFIX + "fwdModule";
    private static final String IGNORE_INCLUDE_SERVLET_PATH_ATTR = ATTR_PREFIX + "ignoreIncludeServletPath";


    /**
     * If not in production mode, write an error to the response; otherwise, set a response error code.
     */
    public static void sendDevTimeError( String messageKey, Throwable cause, int productionTimeErrorCode,
                                         ServletRequest request, ServletResponse response,
                                         ServletContext servletContext, Object[] messageArgs )
            throws IOException
    {
        sendDevTimeError( messageKey, messageArgs, cause, productionTimeErrorCode, request, response, servletContext );
    }

    /**
     * If not in production mode, write an error to the response; otherwise, set a response error code.
     * @deprecated Use {@link #sendDevTimeError(String, Throwable, int, ServletRequest, ServletResponse, ServletContext, Object[])}
     */
    public static void sendDevTimeError( String messageKey, Object[] messageArgs, Throwable cause,
                                         int productionTimeErrorCode, ServletRequest request,
                                         ServletResponse response, ServletContext servletContext )
            throws IOException
    {
        boolean prodMode = AdapterManager.getServletContainerAdapter( servletContext ).isInProductionMode();
        boolean avoidDirectResponseOutput = avoidDirectResponseOutput( request );

        if ( prodMode && ! avoidDirectResponseOutput && response instanceof HttpServletResponse )
        {
            if ( _log.isErrorEnabled() )
            {
                _log.error( "Error (message key " + messageKey + ") occurred.  Response error was set to "
                            + productionTimeErrorCode, cause );
            }

            ( ( HttpServletResponse ) response ).sendError( productionTimeErrorCode );
        }
        else
        {
            sendError( messageKey, messageArgs, request, response, cause, prodMode || avoidDirectResponseOutput );
        }
    }

    /**
     * Write an error to the response.
     */
    public static void sendError( String messageKey, Throwable cause, ServletRequest request,
                                  HttpServletResponse response, Object[] messageArgs )
            throws IOException
    {
        // TODO: the following null check will be unnecessary once the deprecated
        // FlowController.sendError(String, HttpServletResponse) is removed.
        boolean avoidDirectResponseOutput = request != null ? avoidDirectResponseOutput( request ) : false;
        sendError( messageKey, messageArgs, request, response, cause, avoidDirectResponseOutput );
    }

    /**
     * Write an error to the response.
     */
    public static void sendError( String messageKey, Object[] messageArgs, ServletRequest request,
                                  ServletResponse response, Throwable cause, boolean avoidDirectResponseOutput )
            throws IOException
    {
        assert messageArgs.length == 0 || ! ( messageArgs[0] instanceof Object[] )
                : "Object[] passed to sendError; this is probably a mistaken use of varargs";

        // request may be null because of deprecated FlowController.sendError().
        if ( request != null && avoidDirectResponseOutput )
        {
            String baseMessage = Bundle.getString( messageKey + "_Message", messageArgs );
            throw new ResponseOutputException( baseMessage, cause );
        }

        // Filter the message args to prevent cross-site scripting (XSS) attacks (e.g., if one of the args is something
        // that ultimately came from the user request, and it contains something like <script>[some sscript]</script>.
        for (int i = 0; i < messageArgs.length; i++) {
            Object messageArg = messageArgs[i];
            messageArgs[i] = messageArg != null ? filterValue(messageArg.toString()) : null;
        }
        
        String html = Bundle.getString( messageKey + "_Page", messageArgs );
        response.setContentType( "text/html;charset=UTF-8" );
        response.getWriter().println(html);
        ServletUtils.preventCache( response );
    }

    /**
     * Filter output to prevent cross-site scripting (XSS) attacks.
     */
    private static String filterValue(String value) 
            throws IOException {
        InternalStringBuilder result = new InternalStringBuilder(value.length());
        
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            switch (c) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                default:
                    result.append(c);
            }
        }
        
        return result.toString();
    }

    /**
     * We unwrap two special form types: XmlBeanActionForm and AnyBeanActionForm.
     */
    // TODO: make this pluggable
    public static Object unwrapFormBean( ActionForm form )
    {
        if ( form == null ) return null;

        if ( form instanceof AnyBeanActionForm )
        {
            return ( ( AnyBeanActionForm ) form ).getBean();
        }

        return form;
    }

    public static ActionForm wrapFormBean( Object formBean )
    {
        if ( formBean == null ) return null;

        if ( formBean instanceof ActionForm )
        {
            return ( ActionForm ) formBean;
        }
        else
        {
            Class formClass = formBean.getClass();

            if ( BEA_XMLOBJECT_CLASS != null && BEA_XMLOBJECT_CLASS.isAssignableFrom( formClass ) )
            {
                return new XmlBeanActionForm( formBean );
            }
            else if ( APACHE_XMLOBJECT_CLASS != null && APACHE_XMLOBJECT_CLASS.isAssignableFrom( formClass ) )
            {
                return new XmlBeanActionForm( formBean );
            }

            return new AnyBeanActionForm( formBean );
        }
    }

    private static Class loadClassNonFatal( String className )
    {
        try
        {
            return Class.forName( className );
        }
        catch ( ClassNotFoundException e )
        {
            // Not fatal -- we don't require this to be there.  Only if the user wants to use it.

            if ( _log.isDebugEnabled() )
            {
                _log.debug( "Could not load class " + className );
            }
        }

        return null;
    }

    /**
     * Get a Method in a Class.
     *
     * @param parentClass the Class in which to find the Method.
     * @param methodName the name of the Method.
     * @param signature the argument types for the Method.
     * @return the Method with the given name and signature, or <code>null</code> if the method does not exist.
     */
    public static Method lookupMethod( Class parentClass, String methodName, Class[] signature )
    {
        try
        {
            return parentClass.getDeclaredMethod( methodName, signature );
        }
        catch ( NoSuchMethodException e )
        {
            Class superClass = parentClass.getSuperclass();
            return superClass != null ? lookupMethod( superClass, methodName, signature ) : null;
        }
    }

    /**
     * Get a Field in a Class.
     *
     * @param parentClass the Class in which to find the Field.
     * @param fieldName the name of the Field.
     * @return the Field with the given name, or <code>null</code> if the field does not exist.
     */
    public static Field lookupField( Class parentClass, String fieldName )
    {
        try {
            return parentClass.getDeclaredField( fieldName );
        }
        catch ( NoSuchFieldException e ) {
            Class superClass = parentClass.getSuperclass();
            return superClass != null ? lookupField( superClass, fieldName ) : null;
        }
    }

    public static String getFlowControllerClassName( String modulePath, ServletRequest request, ServletContext context )
    {
        //
        // We're going to look in the struts config to get the PageFlowController class.
        //
        ModuleConfig mc = ensureModuleConfig( modulePath, context );
        return mc != null ? getFlowControllerClassName( mc ) : null;
    }

    public static String getFlowControllerClassName( ModuleConfig mc )
    {
        ControllerConfig cc = mc.getControllerConfig();
        return cc instanceof PageFlowControllerConfig ? ( ( PageFlowControllerConfig ) cc ).getControllerClass() : null;
    }

    /**
     * Tell whether the given module is a long-lived page flow.
     */
    public static boolean isLongLived( ModuleConfig moduleConfig )
    {
        ControllerConfig cc = moduleConfig.getControllerConfig();

        if ( cc instanceof PageFlowControllerConfig )
        {
            return ( ( PageFlowControllerConfig ) cc ).isLongLivedPageFlow();
        }
        else
        {
            return false;
        }
    }

    /**
     * Tell whether the given module is a nested page flow.
     */
    public static boolean isNestable( ModuleConfig moduleConfig )
    {
        ControllerConfig cc = moduleConfig.getControllerConfig();
        return cc instanceof PageFlowControllerConfig && ( ( PageFlowControllerConfig ) cc ).isNestedPageFlow();
    }

    public static String getLongLivedFlowAttr( String modulePath )
    {
        return LONGLIVED_PAGEFLOWS_ATTR_PREFIX + modulePath;
    }

    public static void setCurrentPageFlow( PageFlowController jpf, HttpServletRequest request,
                                           ServletContext servletContext )
    {
        setCurrentActionResolver( jpf, request, servletContext );
    }

    public static void removeCurrentPageFlow( HttpServletRequest request, ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String currentJpfAttrName = ScopedServletUtils.getScopedSessionAttrName( CURRENT_JPF_ATTR, unwrappedRequest );
        String currentLongLivedAttrName =
                ScopedServletUtils.getScopedSessionAttrName( CURRENT_LONGLIVED_ATTR, unwrappedRequest );

        sh.removeAttribute( rc, currentJpfAttrName );
        sh.removeAttribute( rc, currentLongLivedAttrName );
    }

    public static void removeCurrentFacesBackingBean( HttpServletRequest request, ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName( FACES_BACKING_ATTR, unwrappedRequest );

        sh.removeAttribute( rc, attrName );
    }

    public static String getDecodedURI( HttpServletRequest request )
    {
        return request.getContextPath() + getDecodedServletPath( request );
    }

    public static String getDecodedServletPath( HttpServletRequest request )
    {
        if ( ignoreIncludeServletPath( request ) ) return request.getServletPath();

        String servletIncludePath = ( String ) request.getAttribute( RequestProcessor.INCLUDE_SERVLET_PATH );
        return servletIncludePath != null ? servletIncludePath : request.getServletPath();
    }

    public static void addActionOutputs( Map toAdd, ServletRequest request, boolean overwrite )
    {
        if ( toAdd != null )
        {
            Map map = getActionOutputMap( request, true );

            for ( Iterator i = toAdd.entrySet().iterator(); i.hasNext(); )
            {
                Map.Entry entry = ( Map.Entry ) i.next();
                String name = ( String ) entry.getKey();
                boolean alreadyExists = map.containsKey( name );

                if ( overwrite || ! alreadyExists )
                {
                    if ( alreadyExists )
                    {
                        if ( _log.isWarnEnabled() )
                        {
                            _log.warn( "Overwriting action output \"" + name + "\"." );
                        }
                    }

                    map.put( name, entry.getValue() );
                }
            }
        }
    }

    public static void addActionError( String propertyName, ActionMessage error, ServletRequest request )
    {
        ActionMessages errors = ( ActionMessages ) request.getAttribute( Globals.ERROR_KEY );
        if ( errors == null ) request.setAttribute( Globals.ERROR_KEY, errors = new ActionMessages() );
        errors.add( propertyName, error );
    }

    public static Object newReloadableInstance( String className, ServletContext servletContext )
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        return getReloadableClass( className, servletContext ).newInstance();
    }

    public static Class getReloadableClass( String className, ServletContext servletContext )
        throws ClassNotFoundException
    {
        ReloadableClassHandler handler = Handlers.get( servletContext ).getReloadableClassHandler();
        return handler.loadClass( className );
    }

    public static Map getActionOutputMap( ServletRequest request, boolean createIfNotExist )
    {
        Map map = ( Map ) request.getAttribute( ACTIONOUTPUT_MAP_ATTR );

        if ( map == null && createIfNotExist )
        {
            map = new HashMap();
            request.setAttribute( ACTIONOUTPUT_MAP_ATTR, map );
        }

        return map;
    }

    public static Map getPageInputMap( ServletRequest request, ServletContext servletContext )
    {
        Map actionOutputsFromPageFlow = getActionOutputMap( request, false );
        if ( actionOutputsFromPageFlow != null ) return actionOutputsFromPageFlow;
        FacesBackingBean fbb = getFacesBackingBean( request, servletContext );
        return fbb != null ? fbb.getPageInputMap() : null;
    }

    public static Map getPageInputMap( ServletRequest request )
    {
        Map actionOutputsFromPageFlow = getActionOutputMap( request, false );
        if ( actionOutputsFromPageFlow != null ) return actionOutputsFromPageFlow;
        FacesBackingBean fbb = getFacesBackingBean( request, getServletContext( request ) );
        return fbb != null ? fbb.getPageInputMap() : null;
    }

    /**
     * Get the Struts ModuleConfig for the given module path.
     */
    public static ModuleConfig getModuleConfig( String modulePath, ServletContext context )
    {
        return ( ModuleConfig ) context.getAttribute( Globals.MODULE_KEY + modulePath );
    }

    /**
     * Get the Struts ModuleConfig for the given module path.  If there is none registered,
     * and if it is possible to register one automatically, do so.
     */
    public static ModuleConfig ensureModuleConfig( String modulePath, ServletContext context )
    {
        try
        {
            ModuleConfig ret = getModuleConfig( modulePath, context );

            if ( ret != null )
            {
                return ret;
            }
            else
            {
                ActionServlet as = getActionServlet( context );

                if ( as instanceof AutoRegisterActionServlet )
                {
                    return ( ( AutoRegisterActionServlet ) as ).ensureModuleRegistered( modulePath );
                }
            }
        }
        catch ( IOException e )
        {
            _log.error( "Error while registering Struts module " + modulePath, e );
        }
        catch ( ServletException e )
        {
            _log.error( "Error while registering Struts module " + modulePath, e );
        }

        return null;
    }

    /**
     * Initialize delegating action configs and exception configs for a Struts module that should delegate
     * to one generated from a superclass.
     */
    public static void initDelegatingConfigs(ModuleConfig moduleConfig, ServletContext servletContext)
    {
        ActionConfig[] actionConfigs = moduleConfig.findActionConfigs();
        
        // Initialize action configs.
        for (int i = 0; i < actionConfigs.length; i++) {
            ActionConfig actionConfig = actionConfigs[i];
            if (actionConfig instanceof DelegatingActionMapping) {
                ((DelegatingActionMapping) actionConfig).init(servletContext);
            } else {
                // Initialize action-level exception configs.
                ExceptionConfig[] exceptionConfigs = actionConfig.findExceptionConfigs();
                for (int j = 0; j < exceptionConfigs.length; j++) {
                    ExceptionConfig exceptionConfig = exceptionConfigs[j];
                    if (exceptionConfig instanceof DelegatingExceptionConfig) {
                        ((DelegatingExceptionConfig) exceptionConfig).init(servletContext);
                    }
                }
            }
        }
        
        // Initialize module-level exception configs.
        ExceptionConfig[] exceptionConfigs = moduleConfig.findExceptionConfigs();
        for (int i = 0; i < exceptionConfigs.length; i++) {
            ExceptionConfig exceptionConfig = exceptionConfigs[i];
            if (exceptionConfig instanceof DelegatingExceptionConfig) {
                ((DelegatingExceptionConfig) exceptionConfig).init(servletContext);
            }
        }
    }
    
    /**
     * Get the current ActionServlet.
     *
     * @param context the current ServletContext
     * @return the ActionServlet that is stored as an attribute in the ServletContext
     */
    public static ActionServlet getActionServlet( ServletContext context )
    {
        if ( context == null ) return null;
        return ( ActionServlet ) context.getAttribute( Globals.ACTION_SERVLET_KEY );
    }

    /**
     * Add a BindingUpdateError to the request.
     *
     * @param request the current ServletRequest.
     * @param expression the expression associated with this error.
     * @param message the error message.
     * @param cause the Throwable that caused the error.
     */
    public static void addBindingUpdateError( ServletRequest request, String expression, String message, Throwable cause )
    {
        Map errors = ( Map ) request.getAttribute( BINDING_UPDATE_ERRORS_ATTR );

        if ( errors == null )
        {
            errors = new LinkedHashMap();
            request.setAttribute( BINDING_UPDATE_ERRORS_ATTR, errors );
        }

        errors.put( expression, new BindingUpdateError( expression, message, cause ) );
    }

    /**
     * Get a map of BindingUpdateErrors stored in the request.
     *
     * @return a Map of expression (String) -> BindingUpdateError.
     */
    public static Map getBindingUpdateErrors( ServletRequest request )
    {
        return ( Map ) request.getAttribute( BINDING_UPDATE_ERRORS_ATTR );
    }

    public static void setCurrentModule( ModuleConfig mc, ServletRequest request )
    {
        request.setAttribute( Globals.MODULE_KEY, mc );
    }

    public static ActionForm createActionForm( ActionMapping mapping, ModuleConfig moduleConfig,
                                               ActionServlet actionServlet, ServletContext servletContext )
    {
        String formName = mapping.getName();
        if ( formName == null ) return null;
        FormBeanConfig config = moduleConfig.findFormBeanConfig( formName );
        if ( config == null ) return null;

        try
        {
            ActionForm bean;

            if ( config.getDynamic() )
            {
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "Creating new DynaActionForm instance of type " + config.getType() );
                }

                DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass( config );
                bean = ( ActionForm ) dynaClass.newInstance();
                ( ( DynaActionForm ) bean ).initialize( mapping );
            }
            else
            {
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "Creating new ActionForm instance of type " + config.getType() );
                }

                bean = ( ActionForm ) newReloadableInstance( config.getType(), servletContext );
            }

            bean.setServlet( actionServlet );
            return bean;
        }
        catch ( Exception e )
        {
            if ( _log.isErrorEnabled() )
            {
                _log.error( "Error creating action form of type " + config.getType(), e );
            }

            return null;
        }
    }

    /**
     * Set the given form in either the request or session, as appropriate, so Struts/NetUI
     * tags will have access to it.
     */
    public static void setFormInScope( String formName, ActionForm form, ActionMapping mapping,
                                       HttpServletRequest request, boolean overwrite )
    {
        if ( formName != null && form != null )
        {
            if ( isSessionScope( mapping ) )
            {
                HttpSession session = request.getSession();

                if ( overwrite || session.getAttribute( formName ) == null )
                {
                    session.setAttribute( formName, form );
                }
            }
            else
            {
                if ( overwrite || request.getAttribute( formName ) == null )
                {
                    request.setAttribute( formName, form );
                }
            }
        }
    }

    public static boolean isSessionScope( ActionMapping mapping )
    {
        return ( mapping.getScope() == null || mapping.getScope().equals( "session" ) );
    }

    public static ActionForm getFormBean( ActionMapping mapping, ServletRequest request )
    {
        String formBeanName = mapping.getAttribute();

        if ( formBeanName != null )
        {
            if ( isSessionScope( mapping ) )
            {
                HttpSession session = getHttpSession( request, false );
                return session != null ? ( ActionForm ) session.getAttribute( formBeanName ) : null;
            }
            else
            {
                return ( ActionForm ) request.getAttribute( formBeanName );
            }
        }

        return null;
    }

    /**
     * Set the current {@link ActionResolver} (or {@link PageFlowController}) in the user session.
     *
     * @param resolver the {@link ActionResolver} to set as the current one in the user session.
     * @deprecated Will be removed in the next version.
     */
    public static void setCurrentActionResolver( ActionResolver resolver, HttpServletRequest request,
                                                 ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String currentJpfAttrName =
            ScopedServletUtils.getScopedSessionAttrName( CURRENT_JPF_ATTR, unwrappedRequest );
        String currentLongLivedJpfAttrName =
                ScopedServletUtils.getScopedSessionAttrName( CURRENT_LONGLIVED_ATTR, unwrappedRequest );

        //
        // This case occurs when the previous page flow is no longer active and there is no new page flow
        //
        if ( resolver == null )
        {
            sh.removeAttribute( rc, currentJpfAttrName );
            sh.removeAttribute( rc, currentLongLivedJpfAttrName );
            return;
        }

        //
        // If this is a long-lived page flow, also store the instance in an attribute that never goes away.
        //
        if ( resolver.isPageFlow() && isLongLived( ( ( PageFlowController ) resolver ).theModuleConfig() ) )
        {
            String longLivedAttrName = getLongLivedFlowAttr( resolver.getModulePath() );
            longLivedAttrName = ScopedServletUtils.getScopedSessionAttrName( longLivedAttrName, unwrappedRequest );

            // Only set this attribute if it's not already there.  We want to avoid our onDestroy() callback that's
            // invoked when the page flow's session attribute is unbound.
            if ( sh.getAttribute( rc, longLivedAttrName ) != resolver )
            {
                sh.setAttribute( rc, longLivedAttrName, resolver );
            }

            sh.setAttribute( rc, currentLongLivedJpfAttrName, resolver.getModulePath() );
            sh.removeAttribute( rc, currentJpfAttrName );
        }
        //
        // Default case for removing a previous page flow in the presence of a new page flow.
        //
        else
        {
            sh.setAttribute( rc, currentJpfAttrName, resolver );
            sh.removeAttribute( rc, currentLongLivedJpfAttrName );
        }
    }

    public static boolean isSharedFlowModule( ModuleConfig mc )
    {
        ControllerConfig cc = mc.getControllerConfig();
        return cc instanceof PageFlowControllerConfig && ( ( PageFlowControllerConfig ) cc ).isSharedFlow();
    }


    public static FacesBackingBean getFacesBackingBean( ServletRequest request, ServletContext servletContext )
    {
        if ( request instanceof HttpServletRequest )
        {
            StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
            HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( ( HttpServletRequest ) request );
            RequestContext rc = new RequestContext( unwrappedRequest, null );
            String attrName = ScopedServletUtils.getScopedSessionAttrName( FACES_BACKING_ATTR, unwrappedRequest );
            return ( FacesBackingBean ) sh.getAttribute( rc, attrName );
        }

        return null;
    }

    public static String inferModulePathFromClassName( String className )
    {
        int lastDot = className.lastIndexOf( '.' );

        if ( lastDot != -1 )
        {
            className = className.substring( 0, lastDot );
            return '/' + className.replace( '.', '/' );
        }
        else
        {
            return "";
        }
    }

    public static boolean isMultipartHandlingEnabled( ServletRequest request )
    {
        ModuleConfig moduleConfig = ( ModuleConfig ) request.getAttribute( Globals.MODULE_KEY );
        return moduleConfig.getControllerConfig().getMultipartClass() != null;
    }

    public static MultipartHandler getMultipartHandlerType()
    {
        PageFlowConfig pfConfig = ConfigUtil.getConfig().getPageFlowConfig();
        return pfConfig != null ? pfConfig.getMultipartHandler() : null;
    }

    public static void setServletContext( ServletRequest request, ServletContext servletContext )
    {
        ScopedServletUtils.getOuterServletRequest( request ).setAttribute( SERVLET_CONTEXT_ATTR, servletContext );
    }

    public static ServletContext getServletContext( ServletRequest req )
    {
        HttpSession session = getHttpSession( req, false );
        return session != null
            ? session.getServletContext()
            : ( ServletContext ) ScopedServletUtils.getOuterServletRequest( req ).getAttribute( SERVLET_CONTEXT_ATTR );
    }

    public static HttpSession getHttpSession( ServletRequest request, boolean create )
    {
        if ( ! ( request instanceof HttpServletRequest ) ) return null;
        return ( ( HttpServletRequest ) request ).getSession( create );
    }

    public static String createActionPath( ServletRequest request, String qualifiedAction )
    {
        ModuleConfig appConfig = ( ModuleConfig ) request.getAttribute( Globals.MODULE_KEY );

        if ( appConfig != null )
        {
            InternalStringBuilder value = new InternalStringBuilder( qualifiedAction.length() + 16 );
            value.append( appConfig.getPrefix() );
            value.append( qualifiedAction );
            return value.toString();
        }

        return qualifiedAction;
    }

    public static String qualifyAction( ServletContext servletContext, String action )
    {
        assert action != null;
        InternalStringBuilder sb = null;

        String queryString = null;
        int question = action.indexOf( '?' );
        if ( question >= 0 ) queryString = action.substring( question );

        String actionMapping = getActionMappingName( action );
        sb = new InternalStringBuilder( action.length() + ACTION_EXTENSION_LEN + 1 );
        sb.append( actionMapping );
        sb.append( ACTION_EXTENSION );
        if ( queryString != null ) sb.append( queryString );

        return sb.toString();
    }

    /**
     * Return the form action converted into an action mapping path.  The
     * value of the <code>action</code> property is manipulated as follows in
     * computing the name of the requested mapping:
     * <ul>
     * <li>Any filename extension is removed (on the theory that extension
     * mapping is being used to select the controller servlet).</li>
     * <li>If the resulting value does not start with a slash, then a
     * slash is prepended.</li>
     * </ul>
     *
     * @param action the action name to be converted.
     * @return an action path, suitable for lookup in the Struts configuration file.
     */
    public static String getActionMappingName( String action )
    {
        return getCleanActionName( action, true );
    }

    public static String getCleanActionName( String action, boolean prependSlash )
    {
        int question = action.indexOf( '?' );
        if ( question >= 0 )
        {
            action = action.substring( 0, question );
        }

        if ( action.endsWith( ACTION_EXTENSION ) )
        {
            action = action.substring( 0, action.length() - ACTION_EXTENSION_LEN );
        }

        if ( action.charAt( 0 ) == '/' )
        {
            if ( ! prependSlash ) action = action.substring( 1 );
        }
        else
        {
            if ( prependSlash ) action = '/' + action;
        }

        return action;
    }


    /**
     * Add a parameter to the given URL. Assumes there is no trailing
     * anchor/fragment indicated with a '#'.
     *
     * @param url       the URL to which to append.
     * @param paramName the name of the parameter to add.
     * @param paramVal  the value of the parameter to add.
     * @return the URL, with the given parameter added.
     */
    public static String addParam( String url, String paramName, String paramVal )
    {
        return url + ( url.indexOf( '?' ) != -1 ? '&' : '?' ) + paramName + '=' + paramVal;
    }

    public static String getActionName( ActionMapping mapping )
    {
        if ( mapping == null ) return null;

        String actionName = mapping.getPath();
        if ( actionName.charAt( 0 ) == '/' ) actionName = actionName.substring( 1 );

        //
        // Look to see if we need are in a disambiguated action, i.e., one whose name is qualified
        // by the form.  If so, we need to restore the unqualified action name.
        //
        if ( mapping instanceof PageFlowActionMapping )
        {
            String unqualifiedAction = ( ( PageFlowActionMapping ) mapping ).getUnqualifiedActionName();
            if ( unqualifiedAction != null ) actionName = unqualifiedAction;
        }

        return actionName;
    }

    public static ActionMapping getCurrentActionMapping( ServletRequest request )
    {
        return ( ActionMapping ) request.getAttribute( Globals.MAPPING_KEY );
    }

    public static ActionForm getCurrentActionForm( ServletRequest request )
    {
        ActionMapping mapping = getCurrentActionMapping( request );
        String attribute = mapping != null ? mapping.getAttribute() : null;
        if ( attribute == null ) return null;

        if ( "request".equals( mapping.getScope() ) )
        {
            return ( ActionForm ) request.getAttribute( attribute );
        }
        else
        {
            HttpSession session = getHttpSession( request, false );
            return session != null ? ( ActionForm ) session.getAttribute( attribute ) : null;
        }
    }

    public static boolean sessionExpired( ServletRequest servletRequest )
    {
        if ( servletRequest instanceof HttpServletRequest )
        {
            HttpServletRequest request = ( HttpServletRequest ) servletRequest;
            String requestedSessionID = request.getRequestedSessionId();

            if ( requestedSessionID != null )
            {
                HttpSession session = request.getSession( false );
                return session == null || ! requestedSessionID.equals( session.getId() );
            }
        }

        return false;
    }

    public static void throwPageFlowException( PageFlowException ex )
    {
        throwPageFlowException( ex, null );
    }

    public static void throwPageFlowException( PageFlowException effect, ServletRequest request )
            throws PageFlowException
    {
        if ( request != null && effect.causeMayBeSessionExpiration() && sessionExpired( request ) )
        {
            PageFlowConfig pfc = ConfigUtil.getConfig().getPageFlowConfig();
            if ( pfc == null || pfc.isThrowSessionExpiredException() )
            {
                throw new SessionExpiredException( effect );
            }
        }
        
        throw effect;
    }
    
    /**
     * Get the Struts ActionConfig for the given action config path and module path.
     */
    public static ActionConfig findActionConfig( String actionConfigPath, String modulePath, ServletContext context )
    {
        ModuleConfig moduleConfig = getModuleConfig( modulePath, context );
        assert moduleConfig != null;
        return moduleConfig.findActionConfig( actionConfigPath );
    }

    /**
     * Get the Struts ActionMapping path from the ActionMapping that is in the request under the key
     * Globals.MAPPING_KEY.
     *
     * @return the path for the ActionMapping, as found with ActionMapping.getPath()
     */
    public static String getActionMappingPath( ServletRequest request )
    {
        ActionMapping actionMapping = ( ActionMapping ) request.getAttribute( Globals.MAPPING_KEY );
        return actionMapping != null ? actionMapping.getPath() : null;
    }

    /**
     * Gets the Struts module path from the input request.  If a ModuleConfig
     * object has been populated into the request it is used to get the module prefix,
     * otherwise getModulePath is called, which derives the module path from
     * the request URI.
     */
    public static String getModulePathFromReqAttr( HttpServletRequest request )
    {
        //
        // If a config was in the request, use its associated prefix; otherwise, fall back to the URI.
        //
        ModuleConfig config = ( ModuleConfig ) request.getAttribute( Globals.MODULE_KEY );
        return config != null ? config.getPrefix() : PageFlowUtils.getModulePath( request );
    }
    
    /**
     * Set the forwarded form.  This overrides the auto-generated form created by processActionForm
     * and populated by processPopulate (in PageFlowRequestProcessor).
     */ 
    public static void setForwardedFormBean( ServletRequest request, ActionForm form )
    {
        if ( form == null )
        {
            request.removeAttribute( FORWARDED_FORMBEAN_ATTR );
        }
        else
        {
            request.setAttribute( FORWARDED_FORMBEAN_ATTR, form );
        }
    }
    
    public static ActionForm getForwardedFormBean( ServletRequest request, boolean removeFromRequest )
    {
        ActionForm form = ( ActionForm ) request.getAttribute( FORWARDED_FORMBEAN_ATTR );
        if ( removeFromRequest ) request.removeAttribute( FORWARDED_FORMBEAN_ATTR );
        return form;
    }
    
    /**
     * Tell whether a special request attribute was set, indicating that we should avoid writing to the response (or
     * setting response error codes).
     */ 
    public static boolean avoidDirectResponseOutput( ServletRequest request )
    {
        Boolean avoid = ( Boolean ) request.getAttribute( AVOID_DIRECT_RESPONSE_OUTPUT_ATTR );
        return avoid != null && avoid.booleanValue();
    }

    /**
     * Set a special request attribute to indicate that we should avoid writing to the response (or
     * setting response error codes).
     */ 
    public static void setAvoidDirectResponseOutput( ServletRequest request )
    {
        request.setAttribute( AVOID_DIRECT_RESPONSE_OUTPUT_ATTR, Boolean.TRUE );
    }
    
    /**
     * Set the module prefix for the ModuleConfig that is performing a forward in this request.
     */ 
    public static void setForwardingModule( ServletRequest request, String modulePrefix )
    {
        request.setAttribute( FORWARDING_MODULE_ATTR, modulePrefix );
    }
    
    /**
     * Set the module prefix for the ModuleConfig that is performing a forward in this request.
     */ 
    public static String getForwardingModule( ServletRequest request )
    {
        return ( String ) request.getAttribute( FORWARDING_MODULE_ATTR );
    }
    
    public static String getFormBeanType( FormBeanConfig formBeanConfig )
    {
        String formBeanType = null;
        
        // First, try to read the form bean type from our custom property, which supports the any-bean feature.
        if ( formBeanConfig instanceof PageFlowActionFormBean )
        {
            formBeanType = ( ( PageFlowActionFormBean ) formBeanConfig ).getActualType();
        }
        
        // If we didn't find it there, this is a normal Struts ActionForm.  Just get it from the type attr.
        if ( formBeanType == null ) formBeanType = formBeanConfig.getType();
        
        return formBeanType;
    }
    
    /**
     * Tell {@link #getDecodedServletPath} (and all that call it) to ignore the attribute that specifies the Servlet
     * Include path, which is set when a Servlet include is done through RequestDispatcher.  Normally,
     * getDecodedServletPath tries the Servlet Include path before falling back to getServletPath() on the request.
     * Note that this is basically a stack of instructions to ignore the include path, and this method expects each
     * call with <code>ignore</code>==<code>true</code> to be balanced by a call with
     * <code>ignore</code>==<code>false</code>.
     */
    public static void setIgnoreIncludeServletPath( ServletRequest request, boolean ignore )
    {
        Integer depth = ( Integer ) request.getAttribute( IGNORE_INCLUDE_SERVLET_PATH_ATTR );
        
        if ( ignore )
        {
            if ( depth == null ) depth = new Integer( 0 );
            request.setAttribute( IGNORE_INCLUDE_SERVLET_PATH_ATTR, new Integer( depth.intValue() + 1 ) );
        }
        else
        {
            assert depth != null : "call to setIgnoreIncludeServletPath() was imbalanced";
            depth = new Integer( depth.intValue() - 1 );
            
            if ( depth.intValue() == 0 )
            {
                request.removeAttribute( IGNORE_INCLUDE_SERVLET_PATH_ATTR );
            }
            else
            {
                request.setAttribute( IGNORE_INCLUDE_SERVLET_PATH_ATTR, depth );
            }
        }
    }
    
    public static boolean ignoreIncludeServletPath( ServletRequest request )
    {
        return request.getAttribute( IGNORE_INCLUDE_SERVLET_PATH_ATTR ) != null;
    }

    /**
     * If the given request is a MultipartRequestWrapper (Struts class that doesn't extend
     * HttpServletRequestWrapper), return the wrapped request; otherwise, return the given request.
     */
    public static ServletRequest unwrapMultipart( ServletRequest request )
    {
        if ( request instanceof MultipartRequestWrapper )
        {
            request = ( ( MultipartRequestWrapper ) request ).getRequest();
        }

        return request;
    }
    
    /**
     * Set the given Struts module in the request, and expose its set of MessageResources as request attributes.
     * 
     * @param prefix the prefix of the desired module.
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return the selected ModuleConfig, or <code>null</code> if there is none for the given module prefix.
     */ 
    public static ModuleConfig selectModule( String prefix, HttpServletRequest request, ServletContext servletContext )
    {
        ModuleConfig moduleConfig = getModuleConfig( prefix, servletContext );

        if ( moduleConfig == null )
        {
            request.removeAttribute( Globals.MODULE_KEY );
            return null;
        }
        
        // If this module came from an abstract page flow controller class, don't select it.
        ControllerConfig cc = moduleConfig.getControllerConfig();
        if (cc instanceof PageFlowControllerConfig && ((PageFlowControllerConfig) cc).isAbstract()) {
            return moduleConfig;
        }
        
        // Just return it if it's already registered.
        if ( request.getAttribute( Globals.MODULE_KEY ) == moduleConfig ) return moduleConfig;
        request.setAttribute( Globals.MODULE_KEY, moduleConfig );

        MessageResourcesConfig[] mrConfig = moduleConfig.findMessageResourcesConfigs();
        Object formBean = unwrapFormBean( getCurrentActionForm( request ) );
        
        for ( int i = 0; i < mrConfig.length; i++ )
        {
            String key = mrConfig[i].getKey();
            MessageResources resources = ( MessageResources ) servletContext.getAttribute( key + prefix );
            
            if ( resources != null )
            {
                if ( ! ( resources instanceof ExpressionAwareMessageResources ) )
                {
                    resources = new ExpressionAwareMessageResources( resources, formBean, request, servletContext );
                }
                
                request.setAttribute( key, resources );
            }
            else
            {
                request.removeAttribute( key );
            }
        }
        
        return moduleConfig;
    }
    
    public static MessageResources getMessageResources( String bundleName, ServletRequest request,
                                                        ServletContext servletContext )
    {
        MessageResources resources = (MessageResources) request.getAttribute(bundleName);
        
        if ( resources == null )
        {
            String qualified = getQualifiedBundleName(bundleName, request);
            resources = (MessageResources) servletContext.getAttribute(qualified);
        }
        
        // If we can't find resources with this name, try them at the root (unqualified).
        if ( resources == null ) resources = (MessageResources) servletContext.getAttribute(bundleName);
        
        return resources;
    }
    
    /**
     * Qualify the given bundle name with the current module path to return a full bundle name.
     *
     * @return the qualified Bundle name
     */
    public static String getQualifiedBundleName( String bundleName, ServletRequest request )
    {
        if ( bundleName != null )
        {
            if ( bundleName.indexOf( '/' ) == -1 )
            {
                ModuleConfig mc = ( ModuleConfig ) request.getAttribute( Globals.MODULE_KEY );

                // Note that we don't append the module path for the root module.
                if ( mc != null && mc.getPrefix() != null && mc.getPrefix().length() > 1 )
                {
                    bundleName += mc.getPrefix();
                }
            }
            else if ( bundleName.endsWith( "/" ) )
            {
                // Special handling for bundles referring to the root module -- they should not have
                // the module path ("/") at the end.
                bundleName = bundleName.substring( 0, bundleName.length() - 1 );
            }
        }

        return bundleName;
    }

    public static Locale lookupLocale(JspContext jspContext) {
        assert jspContext instanceof PageContext : "Found JspContext of type \"" + (jspContext != null ? jspContext.getClass().getName() : "null") + "\"";
        return lookupLocale(((PageContext)jspContext).getRequest());
    }

    public static Locale lookupLocale(ServletRequest request) {
        assert request instanceof HttpServletRequest : "Found servlet request of type \"" + (request != null ? request.getClass().getName() : "null") + "\"";

        Locale locale = null;
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpSession session = httpServletRequest.getSession(false);
        if(session != null)
            locale = (Locale)session.getAttribute(Globals.LOCALE_KEY);

        if(locale == null)
            locale = request.getLocale();

        return locale;
    }

}
