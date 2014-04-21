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

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.lang.reflect.Modifier;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import org.apache.beehive.netui.core.factory.Factory;
import org.apache.beehive.netui.core.factory.FactoryUtils;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.PageFlowFactoryConfig;
import org.apache.beehive.netui.util.config.bean.PageFlowFactoriesConfig;
import org.apache.beehive.netui.util.config.bean.PageFlowConfig;
import org.apache.beehive.netui.util.config.bean.SharedFlowRefConfig;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.pageflow.config.PageFlowControllerConfig;
import org.apache.beehive.netui.pageflow.handler.ReloadableClassHandler;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ControllerConfig;

/**
 * <p>
 * Factory for creating {@link FlowController}s - user {@link PageFlowController}s and {@link SharedFlowController}s.
 * </p>
 */
public class FlowControllerFactory
    extends Factory
{
    private static final Logger LOG = Logger.getInstance( FlowControllerFactory.class );

    private static final String CONTEXT_ATTR = InternalConstants.ATTR_PREFIX + "fcFactory";
    private static final String NO_GLOBAL_APP_KEY = InternalConstants.ATTR_PREFIX + "noglobalapp";

    private transient ReloadableClassHandler _rch;

    protected FlowControllerFactory()
    {
    }

    /**
     * Initialize an instance of this class in the ServletContext.  This is a framework-invoked
     * method and should not normally be called directly.
     */
    public static void init( ServletContext servletContext )
    {
        PageFlowFactoriesConfig factoriesBean = ConfigUtil.getConfig().getPageFlowFactories();
        FlowControllerFactory factory = null;

        if ( factoriesBean != null )
        {
            PageFlowFactoryConfig fcFactoryBean = factoriesBean.getPageFlowFactory();
            factory = ( FlowControllerFactory ) FactoryUtils.getFactory( servletContext, fcFactoryBean, FlowControllerFactory.class );
        }

        if ( factory == null )
            factory = new FlowControllerFactory();

        factory.reinit( servletContext );

        servletContext.setAttribute( CONTEXT_ATTR, factory );
    }

    /**
     * Called to reinitialize this instance, most importantly after it has been serialized/deserialized.
     *
     * @param servletContext the current ServletContext.
     */
    protected void reinit( ServletContext servletContext )
    {
        super.reinit( servletContext );
        if ( _rch == null )
            _rch = Handlers.get( servletContext ).getReloadableClassHandler();
    }

    /**
     * Get a {@link FlowControllerFactory}.  The instance returned may or may not have been cached.
     *
     * @param servletContext the current {@link ServletContext}.
     * @return a {@link FlowControllerFactory} for the given {@link ServletContext}.
     */
    public static FlowControllerFactory get( ServletContext servletContext )
    {
        FlowControllerFactory factory = ( FlowControllerFactory ) servletContext.getAttribute( CONTEXT_ATTR );
        assert factory != null
                : FlowControllerFactory.class.getName() + " was not found in ServletContext attribute " + CONTEXT_ATTR;
        factory.reinit( servletContext );
        return factory;
    }

    /**
     * Get the page flow instance that should be associated with the given request.  If it doesn't exist, create it.
     * If one is created, the page flow stack (for nesting) will be cleared or pushed, and the new instance will be
     * stored as the current page flow.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @return the {@link PageFlowController} for the request, or <code>null</code> if none was found.
     */
    public PageFlowController getPageFlowForRequest( RequestContext context )
            throws InstantiationException, IllegalAccessException
    {
        String servletPath = InternalUtils.getDecodedServletPath( context.getHttpRequest() );
        return getPageFlowForPath( context, servletPath );
    }

    /**
     * Get the page flow instance that should be associated with the given path.  If it doesn't exist, create it.
     * If one is created, the page flow stack (for nesting) will be cleared or pushed, and the new instance will be
     * stored as the current page flow.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @param path a <strong>webapp-relative</strong> path.  The path should not contain the webapp context path.
     * @return the {@link PageFlowController} for the given path, or <code>null</code> if none was found.
     */
    public PageFlowController getPageFlowForPath( RequestContext context, String path )
        throws InstantiationException, IllegalAccessException
    {
        HttpServletRequest request = context.getHttpRequest();
        HttpServletResponse response = context.getHttpResponse();
        PageFlowController cur = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );
        String parentDir = PageFlowUtils.getModulePathForRelativeURI( path );

        //
        // Reinitialize transient data that may have been lost on session failover.
        //
        if ( cur != null )
            cur.reinitialize( request, response, getServletContext() );

        //
        // If there's no current PageFlow, or if the current PageFlowController has a module path that
        // is incompatible with the current request URI, then create the appropriate PageFlowController.
        //
        if ( cur == null || ! PageFlowUtils.getModulePathForRelativeURI( cur.getURI() ).equals( parentDir ) )
        {
            String className = null;
            try
            {
                className = InternalUtils.getFlowControllerClassName( parentDir, request, getServletContext() );
                return className != null ? createPageFlow( context, className ) : null;
            }
            catch ( ClassNotFoundException e )
            {
                if ( LOG.isInfoEnabled() )
                    LOG.info("No page flow exists for path " + path +
                        ".  Unable to load class \"" + className + "\".  Cause: " + e, e);
                return null;
            }
        }

        return cur;
    }

    /**
     * Create a {@link PageFlowController} of the given type.  The {@link PageFlowController} stack used
     * for nesting will be cleared or pushed, and the new instance will be stored as the current
     * {@link PageFlowController}.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @param pageFlowClassName the type name of the desired page flow.
     * @return the newly-created PageFlowController, or <code>null</code> if none was found.
     */
    public PageFlowController createPageFlow( RequestContext context, String pageFlowClassName )
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class pageFlowClass = getFlowControllerClass( pageFlowClassName );
        return createPageFlow( context, pageFlowClass );
    }

    /**
     * Create a {@link PageFlowController} of the given type.  The PageFlowController stack (for
     * nesting) will be cleared or pushed, and the new instance will be stored as the current
     * PageFlowController.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @param pageFlowClass the type of the desired {@link PageFlowController}.
     * @return the newly-created {@link PageFlowController}, or <code>null</code> if none was found.
     */
    public PageFlowController createPageFlow( RequestContext context, Class pageFlowClass )
            throws InstantiationException, IllegalAccessException
    {
        assert PageFlowController.class.isAssignableFrom( pageFlowClass ) : pageFlowClass.getName();

        // If this is an abstract controller class, don't use it.
        if (Modifier.isAbstract(pageFlowClass.getModifiers())) {
            if(LOG.isInfoEnabled())
                LOG.info("Unable to create Page Flow for class \"" + pageFlowClass.getName() +
                    "\" because the class is abstract");
            return null;
        }

        //
        // First check if this is a request for a "long lived" page flow.  If so, try
        // PageFlowUtils.getCurrentPageFlow again, with the longLived flag.
        //
        HttpServletRequest request = context.getHttpRequest();
        HttpServletResponse response = context.getHttpResponse();
        ServletContext servletContext = getServletContext();
        PageFlowController retVal = null;
        // Make sure our request wrapper is in place.
        request = PageFlowRequestWrapper.wrapRequest(request);
        String modulePath = InternalUtils.inferModulePathFromClassName( pageFlowClass.getName() );
        ModuleConfig mc = InternalUtils.ensureModuleConfig( modulePath, servletContext );

        if ( mc == null )
        {
            LOG.error( "Struts module " + modulePath + " not found for " + pageFlowClass.getName()
                        + "; cannot create page flow.");
            return null;
        }

        if ( InternalUtils.isLongLived( mc ) )
        {
            retVal = PageFlowUtils.getLongLivedPageFlow( modulePath, request, servletContext );

            if ( LOG.isDebugEnabled() )
            {
                if ( retVal != null )
                {
                    LOG.debug( "Using long lived PageFlowController of type " + pageFlowClass.getName() );
                }
            }
        }

        //
        // First, see if this is a nested page flow that's already on the stack.  Unless "renesting" is explicitly
        // enabled, we don't want to allow another instance of this page flow to be nested.  This is a common
        // browser back-button problem:
        //    1) request nested page flow A
        //    2) request nested page flow B
        //    3) press back button, and execute an action on A.
        //
        // This logic does not deal with immediate self-nesting (A->A), which is taken care of in
        // PageFlowController.forwardTo().  Nested page flows can only self-nest by forwarding to the .jpf URI, not
        // indirectly by executing actions on themselves (think about it -- that would be a disaster).
        //
        boolean createdNew = false;
        boolean isNestable = InternalUtils.isNestable( mc );
        PageFlowStack pfStack = PageFlowStack.get( request, getServletContext(), false );

        if ( isNestable && pfStack != null )
        {
            PageFlowConfig options = ConfigUtil.getConfig().getPageFlowConfig();

            if ( options == null || ! options.isEnableSelfNesting() )
            {
                PageFlowController existing = pfStack.popUntil(request, pageFlowClass, true);

                if (existing != null) {
                    existing.persistInSession(request, response);
                    return existing;
                } else {
                    //
                    // If the feature is enabled, crawl back through the nesting history.
                    // This is implemented below.
                    //
                }
            }
        }

        //
        // OK, if it's not an existing long lived page flow, and if this wasn't a nested page flow already on the
        // stack, then create a new instance.
        //
        if ( retVal == null )
        {
            if ( LOG.isDebugEnabled() )
                LOG.debug( "Creating PageFlowController of type " + pageFlowClass.getName() );

            retVal = ( PageFlowController ) getFlowControllerInstance( pageFlowClass );
            createdNew = true;
        }

        //
        // Store the previous PageFlowController on the nesting stack (if this one is nestable),
        // or destroy the nesting stack.
        //
        if ( isNestable )
        {
            //
            // Call create() on the newly-created page flow.  Note, the PageFlowController is responsible for
            // driving the appropriate Control lifecycle.
            //
            if ( createdNew )
                retVal.create( request, response, servletContext );

            PageFlowController current = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );

            if ( current != null )
            {
                if ( LOG.isDebugEnabled() )
                    LOG.debug( "Pushing PageFlowController " + current + " onto the nesting stack" );

                if ( pfStack == null )
                    pfStack = PageFlowStack.get( request, getServletContext(), true );
                pfStack.push( current, request );
            }

            retVal.reinitialize( request, response, servletContext );
            retVal.persistInSession( request, response );
        }
        //
        // New page flow is not nestable; behave accordingly
        //
        else
        {
            //
            // A Page Flow nesting stack exists and needs to be destroyed.
            //
            if ( pfStack != null )
            {
                if ( LOG.isDebugEnabled() )
                    LOG.debug( "Destroying the PageFlowController stack." );

                //
                // Start popping page flows until:
                // 1) there are none left on the stack
                // 2) we find one of the type we're returning.
                // If (2), we'll use that one (this means that executing an action on a nesting page flow
                // while in a nested one will not destroy the nesting page flow only to create a new instance of it).
                //
                PageFlowController onStackAlready = pfStack.popUntil( request, retVal.getClass(), false );

                if ( onStackAlready != null )
                {
                    if ( LOG.isDebugEnabled() )
                        LOG.debug( "Found a page flow of type " + retVal.getClass() + " in the stack; "
                                    + "using that instance and stopping destruction of the nesting stack." );

                    retVal = onStackAlready;
                    retVal.persistInSession( request, response );
                }
                else
                {
                    //
                    // We're actually using the newly-created page flow, so call create() on it.
                    // Note that we make the call to persistInSession *before* create, so the previous flow's
                    // onDestroy() gets called before the new one's onCreate().
                    //
                    retVal.reinitialize( request, response, servletContext );
                    retVal.persistInSession( request, response );
                    retVal.create( request, response, servletContext );
                }
            }
            //
            // There is no nesting stack, so just persist the new page flow
            //
            else
            {
                //
                // We're actually using the newly-created page flow, so take several steps that:
                // 1. reinitialize the new page flow
                // 2. persiste the new page flow in the session.  Note, this causes the "destroy" lifecycle
                //    method to be invoked on the previous page flow which is still in the session.
                // 3. if the page flow is newly created, invoke its "create" lifecycle method.
                //
                // Note, steps 2 and 3 must happen in this order in order to remove the previous page flow from
                // the session before adding the new one.
                //
                // For example, this code will execute when forwarding from an existing Page Flow to a new Page Flow
                //
                retVal.reinitialize( request, response, servletContext );
                retVal.persistInSession( request, response );
                if ( createdNew ) 
                    retVal.create( request, response, servletContext );
            }
        }

        return retVal;
    }

    /**
     * Create a {@link SharedFlowController} of the given type.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @param sharedFlowClassName the type name of the desired SharedFlowController.
     * @return the newly-created SharedFlowController, or <code>null</code> if none was found.
     */
    public SharedFlowController createSharedFlow( RequestContext context, String sharedFlowClassName )
            throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class sharedFlowClass = getFlowControllerClass( sharedFlowClassName );
        return createSharedFlow( context, sharedFlowClass );
    }

    /**
     * Create a {@link SharedFlowController} of the given type.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @param sharedFlowClass the type of the desired SharedFlowController.
     * @return the newly-created SharedFlowController, or <code>null</code> if none was found.
     */
    public SharedFlowController createSharedFlow( RequestContext context, Class sharedFlowClass )
            throws InstantiationException, IllegalAccessException
    {
        assert SharedFlowController.class.isAssignableFrom( sharedFlowClass ) : sharedFlowClass.getName();

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Creating SharedFlowController of type " + sharedFlowClass.getName() );
        }

        SharedFlowController retVal = ( SharedFlowController ) getFlowControllerInstance( sharedFlowClass );
        HttpServletRequest request = context.getHttpRequest();
        HttpServletResponse response = context.getHttpResponse();
        retVal.create( request, response, getServletContext() );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Storing new shared flow " + retVal + " in the session" );
        }

        retVal.persistInSession( request, response );
        return retVal;
    }

    /**
     * Get the map of shared flows for the given request.  The map is derived from the shared flows
     * that are declared (through the <code>sharedFlowRefs</code> attribute of
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}) in the
     * page flow for the request.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @return a Map of shared-flow-name (String) to {@link SharedFlowController}.
     * @throws ClassNotFoundException if a declared shared flow class could not be found.
     * @throws InstantiationException if a declared shared flow class could not be instantiated.
     * @throws IllegalAccessException if a declared shared flow class was not accessible.
     */
    public Map/*< String, SharedFlowController >*/ getSharedFlowsForRequest( RequestContext context )
            throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String path = InternalUtils.getDecodedServletPath( context.getHttpRequest() );
        return getSharedFlowsForPath( context, path );
    }

    /**
     * Get the map of shared flows for the given path.  The map is derived from the shared flows
     * that are declared (through the <code>sharedFlowRefs</code> attribute of
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Controller &#64;Jpf.Controller}) in the page flow
     * for the path.
     *
     * @param context a {@link RequestContext} object which contains the current request and response.
     * @param path a <strong>webapp-relative</strong> path.  The path should not contain the webapp context path.
     * @return a Map of shared-flow-name (String) to {@link SharedFlowController}.
     * @throws ClassNotFoundException if a declared shared flow class could not be found.
     * @throws InstantiationException if a declared shared flow class could not be instantiated.
     * @throws IllegalAccessException if a declared shared flow class was not accessible.
     */
    public Map/*< String, SharedFlowController >*/ getSharedFlowsForPath( RequestContext context, String path )
        throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String parentDir = PageFlowUtils.getModulePathForRelativeURI( path );
        HttpServletRequest request = context.getHttpRequest();
        HttpServletResponse response = context.getHttpResponse();
        ModuleConfig mc = InternalUtils.ensureModuleConfig( parentDir, getServletContext() );
        LinkedHashMap/*< String, SharedFlowController >*/ sharedFlows = getDefaultSharedFlows( context );

        if ( mc != null )
        {
            ControllerConfig cc = mc.getControllerConfig();

            if ( cc instanceof PageFlowControllerConfig )
            {
                Map/*< String, String >*/ sharedFlowTypes = ( ( PageFlowControllerConfig ) cc ).getSharedFlowTypes();

                if ( sharedFlowTypes != null && sharedFlowTypes.size() > 0 )
                {
                    if ( sharedFlows == null ) sharedFlows = new LinkedHashMap/*< String, SharedFlowController >*/();

                    for ( Iterator/*<Map.Entry>*/ i = sharedFlowTypes.entrySet().iterator(); i.hasNext(); )
                    {
                        Map.Entry entry = ( Map.Entry ) i.next();
                        String name = ( String ) entry.getKey();
                        String type = ( String ) entry.getValue();
                        addSharedFlow( context, name, type, sharedFlows );
                    }

                    return sharedFlows;
                }
            }
        }

        //
        // Legacy behavior: if there's no shared flow for the request, initialize a GlobalApp instance.
        //
        SharedFlowController ga = PageFlowUtils.getGlobalApp( request );

        if ( ga != null )
        {
            ga.reinitialize( request, response, getServletContext() );
        }
        else
        {
            getGlobalApp( request, response, getServletContext() );
        }

        return sharedFlows;
    }

    /**
     * Get a FlowController class.  By default, this loads the class using the thread context class loader.
     * 
     * @param className the name of the {@link FlowController} class to load.
     * @return the loaded {@link FlowController} class.
     * @throws ClassNotFoundException if the requested class could not be found.
     */
    public Class getFlowControllerClass( String className )
        throws ClassNotFoundException
    {
        return _rch.loadClass( className );
    }

    /**
     * Get a FlowController instance, given a FlowController class.
     * 
     * @param flowControllerClass the Class, which must be assignable to {@link FlowController}.
     * @return a new FlowController instance.
     */
    public FlowController getFlowControllerInstance( Class flowControllerClass )
        throws InstantiationException, IllegalAccessException
    {
        assert FlowController.class.isAssignableFrom( flowControllerClass )
                : "Class " + flowControllerClass.getName() + " does not extend " + FlowController.class.getName();
        return ( FlowController ) flowControllerClass.newInstance();
    }

    /* ------------------------------------------------------------------------------

        Deprecated APIs

       ------------------------------------------------------------------------------ */

    /**
     * Get the page flow instance that should be associated with the given request.  If it doesn't exist, create it. 
     * If one is created, the page flow stack (for nesting) will be cleared or pushed, and the new instance will be
     * stored as the current page flow.
     * @deprecated Use {@link #getPageFlowForRequest(RequestContext)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param servletContext the current ServletContext.
     * @return the {@link PageFlowController} for the request, or <code>null</code> if none was found.
     */
    public static PageFlowController getPageFlowForRequest( HttpServletRequest request,
                                                            HttpServletResponse response,
                                                            ServletContext servletContext )
    {
        return getPageFlowForRelativeURI( request, response, InternalUtils.getDecodedServletPath( request ), servletContext );
    }

    /**
     * Get the page flow instance that should be associated with the given URI.  If it doesn't exist, create it. 
     * If one is created, the page flow stack (for nesting) will be cleared or pushed, and the new instance will be
     * stored as the current page flow.
     * @deprecated Use {@link #getPageFlowForPath(RequestContext, String)} instead.  The URI must be stripped of the
     *     webapp context path before being passed.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param uri a server-relative URI.  The URI should contain the webapp context path.
     * @param servletContext the current ServletContext.
     * @return the {@link PageFlowController} for the given URI, or <code>null</code> if none was found.
     */
    public static PageFlowController getPageFlowForURI( HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        String uri,
                                                        ServletContext servletContext )
    {
        return getPageFlowForRelativeURI( request, response, PageFlowUtils.getRelativeURI( request, uri, null ),
                                          servletContext );
    }

    /**
     * Get the page flow instance that should be associated with the given path.  If it doesn't exist, create it. 
     * If one is created, the page flow stack (for nesting) will be cleared or pushed, and the new instance will be
     * stored as the current page flow.
     * @deprecated Use {@link #getPageFlowForPath(RequestContext, String)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param path a <strong>webapp-relative</strong> path.  The path should not contain the webapp context path.
     * @param servletContext the current ServletContext.
     * @return the {@link PageFlowController} for the given path, or <code>null</code> if none was found.
     */
    public static PageFlowController getPageFlowForRelativeURI( HttpServletRequest request,
                                                                HttpServletResponse response,
                                                                String path,
                                                                ServletContext servletContext )
    {
        try
        {
            return get( servletContext ).getPageFlowForPath( new RequestContext( request, response ), path );
        }
        catch ( InstantiationException e )
        {
            LOG.error( "Could not instantiate PageFlowController for request " + request.getRequestURI(), e );
            return null;
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( "Could not instantiate PageFlowController for request " + request.getRequestURI(), e );
            return null;
        }
    }

    /**
     * Create a page flow of the given type.  The page flow stack (for nesting) will be cleared or pushed, and the new
     * instance will be stored as the current page flow.
     * @deprecated Use {@link #createPageFlow(RequestContext, String)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param pageFlowClassName the type name of the desired page flow.
     * @param servletContext the current ServletContext.
     * @return the newly-created {@link PageFlowController}, or <code>null</code> if none was found.
     */
    public static PageFlowController getPageFlow( String pageFlowClassName,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  ServletContext servletContext )
    {
        try
        {
            return get( servletContext ).createPageFlow( new RequestContext( request, response ), pageFlowClassName );
        }
        catch ( ClassNotFoundException e)
        {
            LOG.error( "Requested page flow class " + pageFlowClassName + " not found." );
            return null;
        }
        catch ( InstantiationException e )
        {
            LOG.error( "Could not instantiate PageFlowController of type " + pageFlowClassName, e );
            return null;
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( "Could not instantiate PageFlowController of type " + pageFlowClassName, e );
            return null;
        }
    }

    /**
     * Get or create the current user session's GlobalApp instance (from the Global.app file).
     * @deprecated Global.app is deprecated; use shared flows and {@link #getSharedFlowsForRequest(RequestContext)}.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @return the current session's {@link GlobalApp} instance, or a new one (based on Global.app) if none is found.
     *     If Global.app does not exist in the current webapp, <code>null</code> is returned.
     */
    public static GlobalApp getGlobalApp( HttpServletRequest request,
                                          HttpServletResponse response,
                                          ServletContext servletContext )
    {
        Boolean noGlobalApp = (Boolean)servletContext.getAttribute(NO_GLOBAL_APP_KEY);
        if(noGlobalApp != null && noGlobalApp.booleanValue())
            return null;

        GlobalApp current = PageFlowUtils.getGlobalApp( request );
        if ( current != null )
            return current;

        try
        {
            try
            {
                FlowControllerFactory factory = get( servletContext );
                SharedFlowController sf =
                    factory.createSharedFlow( new RequestContext( request, response ), PageFlowConstants.GLOBALAPP_CLASSNAME );

                if ( ! ( sf instanceof GlobalApp ) )
                {
                    LOG.error( "Class " + PageFlowConstants.GLOBALAPP_CLASSNAME + " is not an instance of "
                                + GlobalApp.class.getName() );
                    return null;
                }

                return ( GlobalApp ) sf;
            }
            catch ( InstantiationException e )
            {
                LOG.error( "Could not instantiate Global.app.", e );
                return null;
            }
            catch ( IllegalAccessException e )
            {
                LOG.error( "Could not instantiate Global.app", e );
                return null;
            }
        }
        /*
        This is an expected failure.  When no "global/Global.class" is found in classloader, the
        ReloadableClassHandler will throw a CNF exception.  When that happens, set a flag that
        short-circuits this check on subsequent requests so as to prevent throwing CNF(s) for
        every request.

        Note, iterative development of "global/Global.class" files doesn't work in "bouncy" mode
        and shouldn't -- this is a deprecated feature and just needs to continue to function.
        */
        catch ( ClassNotFoundException e )
        {
            LOG.info("No Global.app was found in " + request.getContextPath());
            servletContext.setAttribute(NO_GLOBAL_APP_KEY, Boolean.TRUE);
            return null;
        }
    }

    /**
     * Create a page flow of the given type.  The page flow stack (for nesting) will be cleared or pushed, and the new
     * instance will be stored as the current page flow.
     * @deprecated Use {@link #createPageFlow(RequestContext, Class)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @param response the current HttpServletResponse.
     * @param pageFlowClass the type of the desired page flow.
     * @param servletContext the current ServletContext.
     * @return the newly-created {@link PageFlowController}, or <code>null</code> if none was found.
     */
    public static PageFlowController getPageFlow( Class pageFlowClass,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  ServletContext servletContext )
    {
        try
        {
            FlowControllerFactory factory = get( servletContext );
            return factory.createPageFlow( new RequestContext( request, response ), pageFlowClass );
        }
        catch ( InstantiationException e )
        {
            LOG.error( "Could not instantiate PageFlowController of type " + pageFlowClass.getName(), e );
            return null;
        }
        catch ( IllegalAccessException e )
        {
            LOG.error( "Could not instantiate PageFlowController of type " + pageFlowClass.getName(), e );
            return null;
        }
    }

    /* ------------------------------------------------------------------------------

        Internal APIs

       ------------------------------------------------------------------------------ */

    LinkedHashMap/*< String, SharedFlowController >*/ getDefaultSharedFlows( RequestContext context )
            throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        SharedFlowRefConfig[] defaultRefs = ConfigUtil.getConfig().getSharedFlowRefs();

        if ( defaultRefs != null )
        {
            if ( defaultRefs.length > 0 )
            {
                LinkedHashMap/*< String, SharedFlowController >*/ sharedFlows = new LinkedHashMap();

                for ( int i = 0; i < defaultRefs.length; i++ )
                {
                    SharedFlowRefConfig ref = defaultRefs[i];
                    if ( LOG.isInfoEnabled() )
                    {
                        LOG.info( "Shared flow of type " + ref.getType() + " is a default shared flow reference "
                                    + "with name " + ref.getName() );
                    }
                    addSharedFlow( context, ref.getName(), ref.getType(), sharedFlows );
                }

                return sharedFlows;
            }
        }

        return null;
    }

    private void addSharedFlow( RequestContext context, String name, String type, LinkedHashMap sharedFlows )
            throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        HttpServletRequest request = context.getHttpRequest();
        SharedFlowController sf = PageFlowUtils.getSharedFlow( type, request );

        //
        // Reinitialize transient data that may have been lost on session failover.
        //
        if ( sf != null )
        {
            sf.reinitialize( request, context.getHttpResponse(), getServletContext() );
        }
        else
        {
            sf = createSharedFlow( context, type );
        }

        if ( ! ( sf instanceof GlobalApp ) )
            sharedFlows.put( name, sf );
    }
}
