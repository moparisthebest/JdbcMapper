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

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.core.urls.TemplatedURLFormatter;
import org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.pageflow.internal.PageFlowInitialization;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.DefaultURLRewriter;
import org.apache.beehive.netui.pageflow.internal.PageFlowRequestWrapper;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.util.internal.FileUtils;
import org.apache.beehive.netui.util.internal.ServletUtils;
import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

/**
 * Base class for Servlet Filters that run before requests for JSP pages in a Page Flow enabled web application.
 */
public abstract class PageFlowPageFilter
    implements Filter
{
    private static final Logger LOG = Logger.getInstance( PageFlowPageFilter.class );
    private static final String NO_MODULE_CONFIG = "no config";
    private static final String COMMIT_CHANGES_ATTR_PREFIX = PageFlowPageFilter.class.getName() + ".COMMIT_CHANGES:";

    private ServletContext _servletContext;
    private ServletContainerAdapter _servletContainerAdapter;
    private FlowControllerFactory _flowControllerFactory;
    private Map _knownModulePaths = new InternalConcurrentHashMap();
    private String whitelistPrefix = null;

    protected PageFlowPageFilter()
    {
    }

    PageFlowPageFilter( ServletContext servletContext )
    {
        _servletContext = servletContext;
        _servletContainerAdapter = AdapterManager.getServletContainerAdapter( _servletContext );
        _flowControllerFactory = FlowControllerFactory.get( servletContext );
    }

    public void init( FilterConfig filterConfig )
        throws ServletException
    {
        _servletContext = filterConfig.getServletContext();

        /* todo: NetUI 1.1 -- need to perform initialization in one place; the ServletContextListener */
        if ( ! PageFlowInitialization.isInit( _servletContext ) )
        {
            PageFlowInitialization.performInitializations(_servletContext, null);
        }

        _servletContainerAdapter = AdapterManager.getServletContainerAdapter( _servletContext );
        _flowControllerFactory = FlowControllerFactory.get( _servletContext );

        whitelistPrefix = filterConfig.getInitParameter("whitelist-prefix");
    }

    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException, ServletException
    {
        if ( request instanceof HttpServletRequest && response instanceof HttpServletResponse )
        {
            HttpServletRequest httpRequest = ( HttpServletRequest ) request;
            HttpServletResponse httpResponse = ( HttpServletResponse ) response;

            //
            // Don't do the filter if the request is in error.
            //
            Object errStatusCode = request.getAttribute( "javax.servlet.error.status_code" );
            if ( errStatusCode != null )
            {
                if ( LOG.isDebugEnabled() )
                    LOG.debug( "Request has error status code " + errStatusCode + ".  Skipping filter." );

                continueChainNoWrapper( request, response, chain );
                return;
            }

            String servletPath = InternalUtils.getDecodedServletPath( httpRequest );

            if (whitelistPrefix != null && !servletPath.startsWith(whitelistPrefix)) {
                if (LOG.isDebugEnabled())
                    LOG.debug("Path " + servletPath +
                            " does not start with specified whitelist-prefix " + whitelistPrefix + ".  Skipping filter.");

                continueChainNoWrapper(request, response, chain);
                return;
            }

            String extension = FileUtils.getFileExtension( servletPath );
            Set validFileExtensions = getValidFileExtensions();

            if ( validFileExtensions != null && ! validFileExtensions.contains( extension ) )
            {
                if ( LOG.isDebugEnabled() )
                    LOG.debug( "Path " + servletPath +
                        " does not have an appropriate file extension.  Skipping filter." );

                continueChainNoWrapper( request, response, chain );
                return;
            }

            if ( LOG.isDebugEnabled() )
                LOG.debug( "Filtering request for path " + servletPath );

            //
            // If at an earlier stage in the request we determined that we should prevent caching,
            // actually write the appropriate headers to the response now.
            //
            if ( PageFlowUtils.isPreventCache(request) )
                ServletUtils.preventCache( httpResponse );

            //
            // Initialize the ServletContext in the request.  Often, we need access to the ServletContext when we only
            // have a ServletRequest.
            //
            InternalUtils.setServletContext( httpRequest, _servletContext );

            //
            // Callback to the server adapter.
            //
            PageFlowEventReporter er = _servletContainerAdapter.getEventReporter();
            _servletContainerAdapter.beginRequest( httpRequest, httpResponse );
            RequestContext requestContext = new RequestContext( request, response );
            er.beginPageRequest( requestContext );
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
                URLTemplatesFactory.initServletRequest(request, URLTemplatesFactory.getURLTemplatesFactory(_servletContext));
            }
            if (TemplatedURLFormatter.getTemplatedURLFormatter(request) == null) {
                TemplatedURLFormatter.initServletRequest(request, TemplatedURLFormatter.getTemplatedURLFormatter(_servletContext));
            }

            PageFlowRequestWrapper rw = PageFlowRequestWrapper.unwrap( request );
            boolean isForwardedRequest = rw != null && rw.isForwardedRequest();

            //
            // Below, runPage() will try to synchronize on the current page flow.
            // If we're in a JSP included from another JSP of this page flow, we
            // don't want to commit any session-scoped changes with the
            // storage handler because the locking order will get reversed.
            // See BEEHIVE-1135 in Jira
            //
            boolean commitChanges = true;
            String commitChangesAttrName = null;

            try
            {
                ModuleConfig prevModuleConfig = RequestUtils.getRequestModuleConfig( httpRequest );
                MessageResources prevMessageResources = ( MessageResources ) request.getAttribute( Globals.MESSAGES_KEY );

                //
                // Ensure that the right Struts module is selected, for use by the tags.
                //
                boolean noModuleConfig = false;
                if ( rw == null || ! rw.isStayInCurrentModule() ) {
                    // Dynamically register the Struts module, if appropriate.  If there's no
                    // module config for a given path do not continue to try and register it.
                    // Performance fix until we implement a better caching layer for getting
                    // module configs, etc.
                    //
                    // Note that two threads could potentially get here at the same time, and
                    // both will save the module path.  This is OK -- reads from _knownModulePaths
                    // are consistent, and the worst that will happen is that ensureModuleConfig()
                    // will get called and the module path will get set a few times.
                    String curModulePath = PageFlowUtils.getModulePath( httpRequest );
                    String registered = (String) _knownModulePaths.get(curModulePath);
                    if (registered == null) {
                        ModuleConfig mc = InternalUtils.ensureModuleConfig(curModulePath, _servletContext);
                        if (mc == null) {
                            _knownModulePaths.put(curModulePath, NO_MODULE_CONFIG);
                            noModuleConfig = true;
                        }
                        else {
                            _knownModulePaths.put(curModulePath, curModulePath);
                        }
                    }
                    else if (NO_MODULE_CONFIG.equals(registered)) {
                        noModuleConfig = true;
                    }
                    InternalUtils.selectModule(curModulePath, httpRequest, _servletContext);
                }

                try
                {
                    //
                    // Initialize shared flows for the current request.
                    //
                    Map/*< String, SharedFlowController >*/ sharedFlows =
                            _flowControllerFactory.getSharedFlowsForRequest( requestContext );
                    ImplicitObjectUtil.loadSharedFlow( request, sharedFlows );
                    ImplicitObjectUtil.loadGlobalApp( request, PageFlowUtils.getGlobalApp( httpRequest ) );

                    //
                    // Make sure that the current PageFlowController is set up for this request.
                    //
                    PageFlowController curJpf = null;
                    if ( rw != null && rw.isStayInCurrentModule() )
                    {
                        rw.setStayInCurrentModule( false );
                        curJpf = PageFlowUtils.getCurrentPageFlow( httpRequest, _servletContext );
                    }
                    else if ( !noModuleConfig )
                    {
                        curJpf = _flowControllerFactory.getPageFlowForRequest( requestContext );
                    }

                    //
                    // If there is no pageflow for the current Struts module, than fall back to default
                    // Struts behavior, which is *not* to allow a page request to set the current module.
                    //
                    if ( curJpf == null )
                    {
                        InternalUtils.setCurrentModule( prevModuleConfig, request );
                        request.setAttribute( Globals.MESSAGES_KEY, prevMessageResources );
                        commitChanges = false;
                    }
                    else if (!isForwardedRequest) {
                        //
                        // Check to see if we've already set the commit changes
                        // attribute for this page flow JSP.
                        //
                        String modulePath = curJpf.getModulePath();
                        commitChangesAttrName = COMMIT_CHANGES_ATTR_PREFIX + modulePath;
                        String hasLock = (String) request.getAttribute(commitChangesAttrName);
                        if (hasLock == null || hasLock.length() == 0) {
                            request.setAttribute(commitChangesAttrName, modulePath);
                        }
                        else {
                            //
                            // We're already processing a JSP in this module
                            // that will commit any session-scoped changes in
                            // the request. Don't do it now.
                            //
                            commitChanges = false;
                        }
                    }

                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "Current PageFlowController is: " + curJpf );
                        LOG.debug( "Continuing with filter chain..." );
                    }

                    runPage( curJpf, httpRequest, httpResponse, chain );
                }
                catch ( ClassNotFoundException e )
                {
                    ServletUtils.throwServletException(e);
                }
                catch ( InstantiationException e )
                {
                    ServletUtils.throwServletException(e);
                }
                catch ( IllegalAccessException e )
                {
                    ServletUtils.throwServletException(e);
                }
            }
            finally
            {
                //
                // Callback to the server adapter.
                //
                _servletContainerAdapter.endRequest( httpRequest, httpResponse );
                long timeTaken = System.currentTimeMillis() - startTime;
                er.endPageRequest( requestContext, timeTaken );

                // if we're committing changes, remove the attribute
                if (commitChanges && commitChangesAttrName != null) {
                    request.removeAttribute(commitChangesAttrName);
                }

                //
                // If this is not a forwarded request, then commit any session-scoped changes that were stored in the
                // request.
                //
                if (!isForwardedRequest && commitChanges)
                {
                    Handlers.get( _servletContext ).getStorageHandler().applyChanges( requestContext );
                }
            }
        }
        else
        {
            continueChainNoWrapper( request, response, chain );
        }
    }

    public void destroy()
    {
        _knownModulePaths.clear();
        _servletContext = null;
    }

    /* todo: why is this here?  the valid file extensions are set in web.xml, so why reset them? */
    protected abstract Set getValidFileExtensions();

    private void runPage( PageFlowController curJpf,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          FilterChain chain )
        throws IOException, ServletException
    {
        //
        // Make sure that the pageflow's getRequest() and getResponse() will work while the page
        // is being rendered, since methods on the pageflow may be called (through databinding
        // or tags, or through direct reference).
        //
        if ( curJpf != null )
        {
            //
            // We're going to bail out if there are too many concurrent requests for the same page flow.
            // This prevents an attack that takes advantage of the fact that we synchronize requests
            // to the same pageflow.
            //
            if ( curJpf.incrementRequestCount( request, response, _servletContext ) )
            {
                try
                {
                    //
                    // Any databinding calls, indirect calls to getRequest(), etc. must be protected
                    // against conflicts from running action methods at the same time as rendering
                    // the page here.  Synchronize on the JPF.
                    //
                    synchronized ( curJpf )
                    {
                        // establish the control context for rendering the JSP
                        PageFlowControlContainer pfcc = PageFlowControlContainerFactory.getControlContainer(request,_servletContext);
                        pfcc.beginContextOnPageFlow(curJpf,request,response,_servletContext);


                        FlowController.PerRequestState newState =
                                new FlowController.PerRequestState( request, response, null );
                        FlowController.PerRequestState prevState = curJpf.setPerRequestState( newState );
                        ImplicitObjectUtil.loadImplicitObjects( request, response, _servletContext, curJpf );

                        //
                        // Tell the page flow that we're about to display a page so it can manage settings,
                        // such as previous page information, if needed in advance.
                        //
                        curJpf.beforePage();

                        try
                        {
                            chain.doFilter( request, response );
                        }
                        catch ( ServletException servletEx )
                        {
                            //
                            // If a ServletException escapes out of the page, let the current FlowController handle it.
                            //
                            if ( ! handleException( servletEx, curJpf, request, response ) )
                                throw servletEx;
                        }
                        catch ( IOException ioe )
                        {
                            //
                            // If an IOException escapes out of the page, let the current FlowController handle it.
                            //
                            if ( ! handleException( ioe, curJpf, request, response ) )
                                throw ioe;
                        }
                        catch ( Throwable th )
                        {
                            //
                            // If a Throwable escapes out of the page, let the current FlowController handle it.
                            //
                            if ( ! handleException( th, curJpf, request, response ) )
                            {
                                if ( th instanceof Error )
                                    throw (Error) th;
                                ServletUtils.throwServletException(th);
                            }
                        }
                        finally
                        {
                            curJpf.setPerRequestState( prevState );

                            pfcc.endContextOnPageFlow(curJpf);
                        }
                    }
                }
                finally
                {
                    curJpf.decrementRequestCount( request );
                }
            }
        }
        else
        {
            ImplicitObjectUtil.loadImplicitObjects( request, response, _servletContext, null );
            continueChainNoWrapper( request, response, chain );
        }
    }

    /**
     * Internal method used to handle cases where the filter should continue without processing the
     * request by rendering a page associated with a page flow.
     *
     * @param request the request
     * @param response the response
     * @param chain the filter chain
     * @throws IOException
     * @throws ServletException
     */
    private static void continueChainNoWrapper( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        //
        // Remove our request wrapper -- the page doesn't need to see this.
        //
        if ( request instanceof PageFlowRequestWrapper )
            request = ((PageFlowRequestWrapper)request).getHttpRequest();

        chain.doFilter( request, response );
    }

    /**
     * Internal method that implements exception handling functionality for exceptions thrown while
     * rendering pages (JSPs, etc) associated with a Page Flow.
     *
     * @param th the original throwable
     * @param fc the flow control associated with this request
     * @param request the request
     * @param response the response
     * @return <code>true</code> if the page flow handled the request; <code>false</code> if another exception
     *         occurred while handling this one.
     */
    private boolean handleException( Throwable th,
                                     FlowController fc,
                                     HttpServletRequest request,
                                     HttpServletResponse response )
    {
        try
        {
            ActionMapping mapping = InternalUtils.getCurrentActionMapping( request );
            ActionForm form = InternalUtils.getCurrentActionForm( request );
            ActionForward fwd = fc.handleException( th, mapping, form, request, response );
            fc.getRequestProcessor().doActionForward( request, response, fwd );
            return true;
        }
        catch ( Throwable t )
        {
            LOG.error("Exception occurred while handling exception " + th.getClass().getName()
                + ".  The original exception will be thrown.  Cause: " + t, t);
            return false;
        }
    } 
}
