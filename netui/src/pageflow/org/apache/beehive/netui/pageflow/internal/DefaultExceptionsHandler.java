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
import org.apache.beehive.netui.util.internal.ServletUtils;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ExceptionConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.el.ELException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;

import org.apache.beehive.netui.pageflow.config.PageFlowExceptionConfig;
import org.apache.beehive.netui.pageflow.config.DelegatingExceptionConfig;
import org.apache.beehive.netui.pageflow.FlowController;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.PageFlowManagedObjectException;
import org.apache.beehive.netui.pageflow.PageFlowEventReporter;
import org.apache.beehive.netui.pageflow.ExpressionMessage;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.FormData;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.handler.ExceptionsHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.handler.ActionForwardHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.cache.ClassLevelCache;
import org.apache.beehive.netui.util.logging.Logger;


public class DefaultExceptionsHandler
    extends DefaultHandler
    implements ExceptionsHandler
{
    private static final Logger _log = Logger.getInstance( DefaultExceptionsHandler.class );
    
    private static final String CACHEID_EXCEPTION_HANDLER_METHODS = "_netui:exceptionHandlers";
    
    private transient PageFlowEventReporter _eventReporter;
    

    public DefaultExceptionsHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );
        _eventReporter = AdapterManager.getServletContainerAdapter( servletContext ).getEventReporter();
    }

    public void reinit( ServletContext servletContext )
    {
        super.reinit( servletContext );
        _eventReporter = AdapterManager.getServletContainerAdapter( servletContext ).getEventReporter();
    }

    public ActionForward handleException( FlowControllerHandlerContext context, Throwable ex,
                                          ActionMapping actionMapping, ActionForm form )
            throws IOException, ServletException
    {
        FlowController flowController = context.getFlowController();
        ServletRequest request = context.getRequest();
        ServletResponse response = context.getResponse();
        
        if ( _log.isInfoEnabled() )
        {
            _log.info( "Handling Throwable " + ex.getClass().getName() );
        }
        
        //
        // If we're already in the process of handling an exception, bail out.
        //
        PageFlowRequestWrapper rw = PageFlowRequestWrapper.get( context.getRequest() );
        Throwable alreadyBeingHandled = rw.getExceptionBeingHandled();
        
        if ( alreadyBeingHandled != null )
        {
            if ( _log.isDebugEnabled() )
            {
                _log.debug( "Already in the process of handling " + alreadyBeingHandled.getClass().getName()
                            + "; bailing out of handling for " + ex.getClass().getName() );
            }
            
            throw new UnhandledException( ex );
        }
        
        rw.setExceptionBeingHandled( ex );
        
        
        // Keep track of the Struts module where we find the exception handler.
        ModuleConfig moduleConfig = flowController.theModuleConfig();
        
        // Callback to the event reporter.
        ActionMapping originalActionMapping = actionMapping;
        _eventReporter.exceptionRaised( context, ex, originalActionMapping, form, flowController );
        long startTime = System.currentTimeMillis();
    
        //
        // Look up the ExceptionConfig that's associated with this Throwable.
        //
        Class exClass = ex.getClass();
        ExceptionConfig exceptionConfig = null;
        if ( actionMapping != null )
        {
            exceptionConfig = actionMapping.findException( exClass );
        }
        else
        {
            // If the mapping was null (i.e., the exception happened before we got the action mapping), look for the
            // exception only in the module config.
            exceptionConfig = getExceptionConfig(exClass, moduleConfig);
        }
        
        //
        // If there was no applicable exception handler in the current ModuleConfig, look in a shared flow's module.
        //
        if ( exceptionConfig == null )
        {
            FlowController fallbackFC =
                    getFallbackFlowController( flowController, exClass, request, response, getServletContext() );
            
            if ( fallbackFC != null )
            {
                flowController = fallbackFC;
                context = new FlowControllerHandlerContext( request, response, flowController );
                moduleConfig = flowController.theModuleConfig();
                exceptionConfig = getExceptionConfig( exClass, moduleConfig );
                
                if ( exceptionConfig != null )
                {
                    // This is the module that will be handling the exception.  Ensure that its message resources are
                    // initialized.
                    assert request instanceof HttpServletRequest : request.getClass().getName();
                    InternalUtils.selectModule( moduleConfig.getPrefix(), ( HttpServletRequest ) request,
                                                getServletContext() );
                    rw.setCurrentFlowController( flowController );
                }
            }
            
            actionMapping = null;   // This action mapping isn't relevant if we found the exception elsewhere.
        }
        
        if ( exceptionConfig != null )
        {
            if ( _log.isDebugEnabled() )
            {
                _log.debug( "Found exception-config for exception " + exClass.getName()
                            + ": handler=" + exceptionConfig.getHandler() + ", path=" + exceptionConfig.getPath() );
            }

            // If this is a delegating exception handler, use its *delegate's* ModuleConfig.
            if (exceptionConfig instanceof DelegatingExceptionConfig) {
                moduleConfig = ((DelegatingExceptionConfig) exceptionConfig).getDelegateModuleConfig(getServletContext());
            }
            
            // First, see if it should be handled by invoking a handler method.
            ActionForward ret = null;
            if ( exceptionConfig instanceof PageFlowExceptionConfig )
            {
                PageFlowExceptionConfig pfExceptionConfig = ( PageFlowExceptionConfig ) exceptionConfig;
                
                if ( pfExceptionConfig.isHandlerMethod() )
                {
                    ret = invokeExceptionHandlerMethod( context, ex, pfExceptionConfig, form, actionMapping );
                }
                else
                {
                    ret = invokeExceptionHandlerClass( context, ex, pfExceptionConfig, actionMapping, form );
                }
            }
            else
            {
                ret = invokeExceptionHandlerClass( context, ex, exceptionConfig, actionMapping, form );
            }

            ActionForwardHandler afh = Handlers.get(getServletContext()).getActionForwardHandler();
            String actionName = InternalUtils.getActionName(actionMapping);
            ret = afh.processForward(context, ret, actionMapping, exceptionConfig, actionName, moduleConfig, form);
            
            // Callback to the event reporter.
            long timeTaken = System.currentTimeMillis() - startTime;
            _eventReporter.exceptionHandled( context, ex, originalActionMapping, form, flowController, ret, timeTaken );
            
            return ret;
        }
        
        if ( _log.isErrorEnabled() )
        {
            InternalStringBuilder msg = new InternalStringBuilder( "Throwable " ).append( exClass.getName() );
            _log.error( msg.append( " unhandled by the current page flow (and any shared flow)" ).toString(), ex );
        }
       
        if ( ! getRegisteredExceptionsHandler().eatUnhandledException( context, ex ) )
        {
            // Throwing this ServletException derivative will prevent any outer try/catch blocks from re-processing
            // the exception.
            throw new UnhandledException( ex );
        }
        
        return null;
    }
    
    public Throwable unwrapException( FlowControllerHandlerContext context, Throwable ex )
    {
        if ( ex instanceof InterceptorException )
        {
            Throwable cause = ex.getCause();
            if ( cause != null ) return unwrapException( context, cause );
        }
        
        //
        // If the exception was thrown in a method we called through reflection, it will be an
        // InvocationTargetException.  Unwrap it.  Do the same for the UndeclaredThrowable exceptions thrown when
        // invoking methods through dynamic proxies.
        //
        if ( ex instanceof InvocationTargetException )
        {
            return unwrapException( context, ( ( InvocationTargetException ) ex ).getTargetException() );
        }
        
        if ( ex instanceof UndeclaredThrowableException )
        {
            return unwrapException( context, ( ( UndeclaredThrowableException ) ex ).getUndeclaredThrowable() );
        }
        
        if ( ex instanceof ServletException )
        {
            ServletException servletException = ( ServletException ) ex;
            Throwable rootCause = servletException.getRootCause();
            if ( rootCause != null ) return unwrapException( context, rootCause );
        }
        
        return ex;
    }
    
    public void exposeException( FlowControllerHandlerContext context, Throwable ex, ActionMapping actionMapping )
    {
        //
        // Put the exception in a place where Struts/NetUI tags will find it.
        //
        context.getRequest().setAttribute( Globals.EXCEPTION_KEY, ex );
    }
    
    protected ExceptionConfig getExceptionConfig( Class exceptionType, ModuleConfig moduleConfig )
    {
        ExceptionConfig config = null;
                
        if ( moduleConfig != null )
        {
            while ( config == null && exceptionType != null )
            {
                config = moduleConfig.findExceptionConfig( exceptionType.getName() );
                
                // Loop again for our superclass (if any)
                exceptionType = exceptionType.getSuperclass();
            }
        }
        
        return config;
    }
    
    protected FlowController getFallbackFlowController( FlowController originalFlowController, Class exClass,
                                                        ServletRequest request, ServletResponse response,
                                                        ServletContext servletContext )
    {
        if ( originalFlowController instanceof PageFlowController )
        {
            Collection/*< SharedFlowController >*/ sharedFlows =
                    ( ( PageFlowController ) originalFlowController ).theSharedFlows().values();
            
            for ( Iterator ii = sharedFlows.iterator(); ii.hasNext(); )  
            {
                SharedFlowController sf = ( SharedFlowController ) ii.next();
                if ( checkForExceptionConfig( sf, exClass, request ) ) return sf;
            }
        }
        
        assert request instanceof HttpServletRequest : request.getClass().getName();
        SharedFlowController globalApp = PageFlowUtils.getGlobalApp( ( HttpServletRequest ) request );
        if ( globalApp != null && checkForExceptionConfig( globalApp, exClass, request ) ) return globalApp;
        return null;
    }
    
    private boolean checkForExceptionConfig( SharedFlowController sf, Class exClass, ServletRequest request )
    {
        ModuleConfig mc = sf.theModuleConfig();
        ExceptionConfig ec = getExceptionConfig( exClass, mc );
        
        if ( ec != null )
        {
            if ( _log.isDebugEnabled() )
            {
                _log.debug( "Found exception-config for " + exClass.getName() + " in SharedFlowController "
                            + sf.getDisplayName() );
            }                    
            
            InternalUtils.setCurrentModule( mc, request );
            return true;
        }
        
        return false;
    }
    
    protected ActionForward invokeExceptionHandlerClass( FlowControllerHandlerContext context, Throwable throwable,
                                                         ExceptionConfig exceptionConfig, ActionMapping actionMapping,
                                                         ActionForm form )
        throws IOException, ServletException
    {
        String handlerClassName = exceptionConfig.getHandler();
        
        try
        {
            //
            // Get the exception-handler class and delegate to it.
            //
            assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
            assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
            HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
            HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
            ExceptionHandler handler = ( ExceptionHandler ) RequestUtils.applicationInstance( handlerClassName );
            Exception ex = throwable instanceof Exception ? ( Exception ) throwable : new Exception( throwable );
            ActionForward result = handler.execute( ex, exceptionConfig, actionMapping, form, request, response );
                    
            if ( _log.isDebugEnabled() )
            {
                _log.debug( "Exception-handler: forward to " + result.getPath() );
            }
                
            return result;
        }
        catch ( ClassNotFoundException e )
        {
            _log.error( "Could not find exception-handler class " + handlerClassName, e );
            ServletUtils.throwServletException(e);
        }
        catch ( InstantiationException e )
        {
            _log.error( "Could not create instance of exception-handler class " + handlerClassName, e );
            ServletUtils.throwServletException(e);
        }
        catch ( IllegalAccessException e )
        {
            _log.error( "Could not create instance of exception-handler class " + handlerClassName, e );
            ServletUtils.throwServletException(e);
        }
        
        assert false;   // should not get here -- either a value is returned or an exception is thrown.
        return null;
    }
    
    protected ActionForward invokeExceptionHandlerMethod( FlowControllerHandlerContext context, Throwable ex,
                                                          PageFlowExceptionConfig exceptionConfig, ActionForm form,
                                                          ActionMapping actionMapping )
        throws IOException, ServletException
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        FlowController flowController = context.getFlowController();
        String methodName = exceptionConfig.getHandler();
        Object unwrappedFormBean = InternalUtils.unwrapFormBean( form );
        Method method = getExceptionHandlerMethod( context, methodName, ex, unwrappedFormBean );

        if ( method != null )
        {
            // First see if there's a hard-coded message set.
            String message = exceptionConfig.getDefaultMessage();
            ActionMessage error = null;
            
            if ( message != null )
            {
                error = new ExpressionMessage( message, new Object[]{ ex.getMessage() } );
                
                try
                {
                    // The message may be an expression.  Evaluate it.
                    message = InternalExpressionUtils.evaluateMessage( message, form, request, getServletContext() );
                }
                catch ( ELException e )
                {
                    _log.error( "error while evaluating expression in exception-handler for " + ex.getClass().getName(), e );
                }
            }


            if ( message == null )
            {
                // No hard-coded message.  Get the message based on the message key.
                String messageKey = exceptionConfig.getKey();
                        
                if ( messageKey != null && messageKey.length() > 0 )
                {
                    message = getMessage( context, messageKey, null, null );
                }
            }
            
            //
            // Expose the exception to the errors tag.
            //
            String msgKey = exceptionConfig.getKey();
            if ( error == null ) error = new ActionMessage( msgKey, ex.getMessage() );
            storeException( request, msgKey, error, exceptionConfig.getScope() ); 
            
            return flowController.invokeExceptionHandler(method, ex, message, form, exceptionConfig, actionMapping,
                                                         request, response);
        }
        else
        {
            //
            // This shouldn't happen except in out-of-date-class situations.  JpfChecker
            // should prevent this at compilation time.
            //
            String err;
            if ( form != null )
            {
                err= Bundle.getString( "PageFlow_MissingExceptionHandlerWithForm",
                                       new Object[]{ methodName, form.getClass().getName() } );
            }
            else
            {
                err = Bundle.getString( "PageFlow_MissingExceptionHandler", methodName );
            }
                    
            InternalUtils.sendError( "PageFlow_Custom_Error", null, request, response, 
                                     new Object[]{ flowController.getDisplayName(), err } );
            return null;
        }        
    }
    
    protected static void storeException( HttpServletRequest request, String key, ActionMessage error, String scope )
    {
        ActionMessages errors = new ActionMessages();
        errors.add( key, error );

        if ( "request".equals( scope ) )
        {
            request.setAttribute( Globals.ERROR_KEY, errors );
        }
        else
        {
            request.getSession().setAttribute( Globals.ERROR_KEY, errors );
        }
    }
    
    protected String getMessage( FlowControllerHandlerContext context, String messageKey, String bundle, Object[] args )
    {
        if ( bundle == null ) bundle = Globals.MESSAGES_KEY;
        
        ServletRequest request = context.getRequest();
        MessageResources resources = InternalUtils.getMessageResources(bundle, request, getServletContext());

        if ( resources == null )
        {
            _log.error( "Could not find message-resources for bundle " + bundle );
            return null;
        }

        Locale userLocale = 
                request instanceof HttpServletRequest
                ? FlowController.retrieveUserLocale( ( HttpServletRequest ) request, null )
                : null;
    
        if ( args == null )
        {
            return resources.getMessage( userLocale, messageKey );
        }
        else
        {
            return resources.getMessage( userLocale, messageKey, args );
        }
    }
    
    public boolean eatUnhandledException( FlowControllerHandlerContext context, Throwable ex )
    {
        _log.error( "Unhandled Page Flow Exception", ex );

        try
        {
            //
            // PageFlowExceptions know what to do in the unhandled case.
            //
            boolean prodMode = AdapterManager.getServletContainerAdapter( getServletContext() ).isInProductionMode();
            
            if ( ! prodMode && ex instanceof PageFlowManagedObjectException  )
            {
                ( ( PageFlowManagedObjectException ) ex ).sendError( context.getRequest(), context.getResponse() );
                return true;
            }
        }
        catch ( IOException ioEx )
        {
            _log.error( ioEx.getMessage(), ioEx );
        }
        
        return false;
    }
    
    /**
     * Get an Exception handler method.
     * 
     * @param methodName the name of the method to get.
     * @param ex the Exception that is to be handled.
     * @return the Method with the given name that handles the given Exception, or <code>null</code>
     *             if none matches.
     */ 
    protected Method getExceptionHandlerMethod( FlowControllerHandlerContext context, String methodName, Throwable ex,
                                                Object formBean )
    {
        FlowController flowController = context.getFlowController();
        String cacheKey = methodName + '/' + ex.getClass().getName();
        ClassLevelCache cache = ClassLevelCache.getCache( flowController.getClass() );
        Method method = ( Method ) cache.get( CACHEID_EXCEPTION_HANDLER_METHODS, cacheKey );
        
        if ( method != null )
        {
            return method;
        }
     
        Class flowControllerClass = flowController.getClass();
        for ( Class exClass = ex.getClass(); exClass != null; exClass = exClass.getSuperclass() )
        {
            Class[] args = new Class[]{ exClass, String.class, String.class, Object.class };
            Method foundMethod = InternalUtils.lookupMethod( flowControllerClass, methodName, args );
            
            //
            // If we didn't find an exception-handler with the right signature, look for the deprecated signature with
            // FormData as the last argument.
            //
            if ( foundMethod == null && ( formBean == null || formBean instanceof FormData ) )
            {
                args = new Class[]{ exClass, String.class, String.class, FormData.class };         
                foundMethod = InternalUtils.lookupMethod( flowControllerClass, methodName, args );
            }
            
            //
            // If we didn't find an exception-handler with the right signature, look for the deprecated signature with
            // ActionForm as the last argument.
            //
            if ( foundMethod == null && ( formBean == null || formBean instanceof ActionForm ) )
            {
                args = new Class[]{ exClass, String.class, String.class, ActionForm.class };         
                foundMethod = InternalUtils.lookupMethod( flowControllerClass, methodName, args );
            }
            
            if ( foundMethod != null )
            {
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "Found exception handler for " + exClass.getName() );
                }
                
                if ( ! Modifier.isPublic( foundMethod.getModifiers() ) ) foundMethod.setAccessible( true );
                cache.put( CACHEID_EXCEPTION_HANDLER_METHODS, cacheKey, foundMethod );
                return foundMethod;
            }
            else
            {
                if ( _log.isErrorEnabled() )
                {
                    InternalStringBuilder msg = new InternalStringBuilder( "Could not find exception handler method " );
                    msg.append( methodName ).append( " for " ).append( exClass.getName() ).append( '.' );
                    _log.error( msg.toString() );
                }
            }
        }
        
        return null;
    }
    
    public ExceptionsHandler getRegisteredExceptionsHandler()
    {
        return ( ExceptionsHandler ) super.getRegisteredHandler();
    }
}
