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

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ExceptionConfig;

import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.action.AfterNestedInterceptContext;
import org.apache.beehive.netui.pageflow.interceptor.action.InterceptorForward;
import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.config.PageFlowExceptionConfig;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.handler.ActionForwardHandler;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

public class DefaultActionForwardHandler 
        extends DefaultHandler
        implements ActionForwardHandler
{
    private static final Logger _log = Logger.getInstance( DefaultActionForwardHandler.class );

    public DefaultActionForwardHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );
    }

    public ActionForward processForward( FlowControllerHandlerContext context, ActionForward fwd, ActionMapping mapping,
                                         ExceptionConfig exceptionConfig, String actionName,
                                         ModuleConfig altModuleConfig, ActionForm form )
    {
        boolean isSpecialForward = false;
        boolean isReturnToCurrentPage = false;
        boolean isNonInheritedGlobalForward = false;
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        FlowController flowController = context.getFlowController();
        
        //
        // There is a special forward ("auto"), which signals us to render using a registered ViewRenderer.
        // This is used as part of popup window support.
        //
        if ( fwd != null && PageFlowConstants.AUTO_VIEW_RENDER_FORWARD_NAME.equals( fwd.getName() ) )
        {
            return getRegisteredActionForwardHandler().doAutoViewRender( context, mapping, form );
        }
        
        if ( fwd != null && fwd instanceof Forward )
        {
            Forward pageFlowFwd = ( Forward ) fwd;

            pageFlowFwd.initialize( mapping, flowController, request );
            pageFlowFwd.setAlternateModuleConfig( altModuleConfig );

            if ( ! pageFlowFwd.doesResolve() )
            {
                PageFlowException ex =
                        new UnresolvableForwardException( pageFlowFwd.getName(), actionName, flowController );
                InternalUtils.throwPageFlowException( ex, request );
            }

            //
            // If it's a return-to-page, do what's necessary to return to the previous page,
            // with its state intact.
            //
            if ( pageFlowFwd.isReturnToPage() )
            {
                isSpecialForward = true;
                
                //
                // We need access to _previousPageInfo from the *current PageFlow*.  That is
                // most likely this FlowController, but if it's Global.app, then we don't want
                // to use that.
                //
                PageFlowController curJpf = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );
                
                if ( curJpf == null )
                {
                    PageFlowException ex = new NoCurrentPageFlowException( actionName, pageFlowFwd );
                    InternalUtils.throwPageFlowException( ex, request );
                    assert false;   // throwPageFlowException() must throw.
                }
                
                PreviousPageInfo prevPageInfo;
                
                switch ( pageFlowFwd.getReturnToType() )
                {
                    case Forward.RETURN_TO_CURRENT_PAGE:
                        prevPageInfo = curJpf.theCurrentPageInfo();
                        isReturnToCurrentPage = true;
                        break;
                        
                    case Forward.RETURN_TO_PREVIOUS_PAGE:
                        prevPageInfo = curJpf.thePreviousPageInfo();
                        break;
                    
                    case Forward.RETURN_TO_PAGE:
                        prevPageInfo = flowController.getPreviousPageInfoLegacy( curJpf, request );
                        break;
                    
                    default:
                        assert false : pageFlowFwd.getReturnToType();
                        prevPageInfo = curJpf.theCurrentPageInfo();
                }
                
                fwd =
                  getRegisteredActionForwardHandler().doReturnToPage( context, prevPageInfo, curJpf, form, actionName, pageFlowFwd );
                
                if ( prevPageInfo != null )
                {
                    mapping = prevPageInfo.getMapping();
                    if ( form == null ) form = prevPageInfo.getForm();
                }
                
                if ( _log.isDebugEnabled() )
                {
                    _log.debug( "return-to-page: " + ( fwd != null ? fwd.getPath() : "[null]" ) );
                }
            }
            else if ( pageFlowFwd.isReturnToAction() )
            {
                isSpecialForward = true;
                fwd = getRegisteredActionForwardHandler().doReturnToAction( context, actionName, pageFlowFwd );
            }
            else if ( pageFlowFwd.isNestedReturn() )
            {
                //
                // Pop the current PageFlowController (done nesting).
                //
                isSpecialForward = true;
                fwd = getRegisteredActionForwardHandler().doNestingReturn( context, pageFlowFwd, mapping, form );
            }

            //
            // Set ActionForms specified in the Forward.  Note that this overwrites any forms restored
            // during return-to="page".
            //
            PageFlowUtils.setOutputForms( mapping, pageFlowFwd, request );
            InternalUtils.addActionOutputs( pageFlowFwd.getActionOutputs() , request, true );

            // Check if this is a non-inherited global forward to help determine
            // if we need to ensure we use a path local to the current page flow.
            if ( mapping != null && mapping.findForwardConfig( pageFlowFwd.getName() ) == null
                 && mapping.getModuleConfig().findForwardConfig( pageFlowFwd.getName() ) != null
                 && ! pageFlowFwd.hasRelativeToPath() )
            {
                isNonInheritedGlobalForward = true;
            }
        }

        if ( fwd != null )
        {
            // It's a normal path.  Let the Forward object do some magic if we're specifying that it's relative
            // to a particular directory path (as is the case when inheriting local paths from base classes).
            // Note that if *either* the exception-config or the action-config specifies a value for 
            // getLocalPathsRelativeTo(), we use it. Also note that we do not do this for forward to other actions
            // or if this is a non-inherited global forward; in those cases we always want paths to be local
            // to the current page flow.
            String localPathsRelativeTo = null;
            if (exceptionConfig != null && exceptionConfig instanceof PageFlowExceptionConfig) {
                localPathsRelativeTo = ((PageFlowExceptionConfig) exceptionConfig).getLocalPathsRelativeTo();
            }
            if (localPathsRelativeTo == null && mapping instanceof PageFlowActionMapping) {
                localPathsRelativeTo = ((PageFlowActionMapping) mapping).getLocalPathsRelativeTo();
            }
            if (localPathsRelativeTo != null) {
                String path = fwd.getPath();
                if (!path.endsWith(PageFlowConstants.ACTION_EXTENSION) && !isNonInheritedGlobalForward) {
                    Forward pageFlowFwd = fwd instanceof Forward ? (Forward) fwd : new WrapperForward(fwd);
                    pageFlowFwd.initializeRelativePath(request, localPathsRelativeTo);
                    fwd = pageFlowFwd;
                }
            }
            else if ( fwd instanceof Forward )
            {
                ( ( Forward ) fwd ).initializeRelativePath(request, null);
            }
            
            if ( _log.isDebugEnabled() )
            {
                if ( fwd.getRedirect() )
                {
                    _log.debug( "Redirecting to " + fwd.getPath() );
                }
                else
                {
                    _log.debug( "Forwarding to " + fwd.getPath() );
                }
            }
        }
        else
        {
            _log.debug( "null ActionForward -- not doing any forward or redirect." );
        }
        
        //
        // Save info on this forward for return-to="currentPage" or return-to="previousPage".
        // However, don't save the current forward as previous if this is a
        // return-to="currentPage" -- we don't want this to turn into the page
        // that's seen for *both* return-to="currentPage" and return-to="previousPage".
        // Just set the request attribute indicating that prev page info state was saved.
        //
        if (!isReturnToCurrentPage) {
            flowController.savePreviousPageInfo(fwd, form, mapping, request, getServletContext(), isSpecialForward);
        }
        else {
            request.setAttribute(InternalConstants.SAVED_PREVIOUS_PAGE_INFO_ATTR, Boolean.TRUE);
        }

        return fwd;
    }
    
    private class WrapperForward extends Forward
    {
        public WrapperForward(ActionForward base)
        {
            super(base, getServletContext());
        }
    }
    
    public ActionForward doAutoViewRender( FlowControllerHandlerContext context, ActionMapping mapping, ActionForm form )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        ViewRenderer vr = PageFlowRequestWrapper.get( request ).getViewRenderer();
        
        if ( vr != null )
        {
            _log.debug( "ActionForward -- delegating to ViewRenderer " + vr + " to handle response." );
            
            try
            {
                request.setAttribute(PageFlowConstants.VIEW_RENDERER_ATTRIBUTE_NAME, vr);

                String ext = getServletContext().getInitParameter(PageFlowConstants.VIEW_RENDERING_EXTENSION_PARAM);
                if (ext == null || ext.length() == 0) {
                    ext = PageFlowConstants.DEFAULT_VIEW_RENDERING_EXTENSION;
                }

                InternalStringBuilder path = new InternalStringBuilder(64);
                path.append("/");
                path.append(PageFlowConstants.VIEW_RENDERER_URI_COMMAND);
                path.append(".").append(ext);

                ActionForward fwd = new ActionForward( path.toString(), false );
                fwd.setContextRelative( true );
                return fwd;
            }
            catch ( Throwable th )
            {
                try
                {
                    return context.getFlowController().handleException( th, mapping, form, request, response );
                }
                catch ( Exception e )
                {
                    _log.error( "Exception thrown while handling exception in ViewRenderer " + vr + ": "
                                + e.getMessage(), th );
                }
            }
            
        }
        else
        {
            _log.error( "Auto-render forward " + PageFlowConstants.AUTO_VIEW_RENDER_FORWARD_NAME
                        + " used, but no ViewRenderer " + "was registered -- not doing any forward or redirect." );
        }
        
        return null;
    }
    
    /**
     * Get an ActionForward to the original page that was visible before the previous action.
     */
    public ActionForward doReturnToPage( FlowControllerHandlerContext context, PreviousPageInfo prevPageInfo,
                                         PageFlowController currentPageFlow, ActionForm currentForm,
                                         String actionName, Forward pageFlowFwd )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        
        if ( prevPageInfo == null )
        {
            if ( _log.isInfoEnabled() )
            {
                _log.info( "Attempted return-to-page, but previous page info was missing." );
            }
        
            PageFlowException ex = new NoPreviousPageException( actionName, pageFlowFwd, currentPageFlow );
            InternalUtils.throwPageFlowException( ex, request );
        }
        
        //
        // Figure out what URI to return to, and set the original form in the request or session.
        //        
        ActionForward retFwd = prevPageInfo.getForward();
        ActionMapping prevMapping = prevPageInfo.getMapping();
        
        //
        // Restore any forms that are specified by this Forward (overwrite the original forms).
        //
        if ( retFwd instanceof Forward )
        {
            PageFlowUtils.setOutputForms( prevMapping, ( Forward ) retFwd, request, false );
            InternalUtils.addActionOutputs( ( ( Forward ) retFwd ).getActionOutputs(), request, false );
        }
        
        //
        // If the user hit the previous page directly (without going through an action), prevMapping will be null.
        //
        if ( prevMapping != null )
        {
            //
            // If the currently-posted form is of the right type, initialize the page with that (but we don't overwrite
            // the form that was set above).
            //
            if ( currentForm != null ) PageFlowUtils.setOutputForm( prevMapping, currentForm, request, false );
        
            //
            // Initialize the page with the original form it got forwarded (but we don't overwrite the form that was
            // set above).
            //
            InternalUtils.setFormInScope( prevMapping.getName(), prevPageInfo.getForm(), prevMapping, request, false );
        }
            
        //
        // If we're forwarding to a page in a different pageflow, we need to make sure the returned ActionForward has
        // the right module path, and that it has contextRelative=true.
        //
        FlowController flowController = context.getFlowController();
        
        if ( ! retFwd.getContextRelative() && flowController != currentPageFlow )
        {

            retFwd = new ActionForward( retFwd.getName(),
                                        currentPageFlow.getModulePath() + retFwd.getPath(),
                                        retFwd.getRedirect(),
                                        true );

        }
        
        if ( _log.isDebugEnabled() )
        {
            _log.debug( "Return-to-page in PageFlowController " + flowController.getClass().getName()
                       + ": original URI " + retFwd.getPath() );
        }
        
        if ( retFwd != null )
        {
            //
            // If the new (return-to) Forward specifies a redirect value explicitly, use that; otherwise
            // use the redirect value from the original Forward.
            //
            if ( pageFlowFwd.hasExplicitRedirectValue() ) retFwd.setRedirect( pageFlowFwd.getRedirect() );
            
            //
            // If there's a query string, override the previous query string.
            //
            String fwdPath = retFwd.getPath();
            String newQueryString = pageFlowFwd.getQueryString();
            int existingQueryPos = fwdPath.indexOf( '?' );
            
            //
            // If the new Forward (the one with Jpf.NavigateTo.currentPage/previousPage) has a query string, use that.
            // Otherwise, if the old Forward has no query string, restore the one from the PreviousPageInfo if
            // appropriate.
            //
            if ( newQueryString != null )
            {
                // Chop off the old query string if necessary.
                if ( existingQueryPos != -1 ) fwdPath = fwdPath.substring( 0, existingQueryPos );
                retFwd.setPath( fwdPath + newQueryString );
            }
            else if ( existingQueryPos == -1 )
            {
                retFwd.setPath( fwdPath + getQueryString( pageFlowFwd, prevPageInfo ) );
            }
        }
        
        PageFlowRequestWrapper.get( request ).setPreviousPageInfo( prevPageInfo );
        return retFwd;
    }
    
    public ActionForward doReturnToAction( FlowControllerHandlerContext context, String actionName, Forward pageFlowFwd )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        
        //
        // We need access to _previousPageInfo from the *current PageFlow*.  That is
        // most likely this FlowController, but if it's Global.app, then we don't want
        // to use that.
        //
        PageFlowController curJpf = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );
        
        if ( curJpf == null )
        {
            PageFlowException ex = new NoCurrentPageFlowException( actionName, pageFlowFwd );
            InternalUtils.throwPageFlowException( ex, request );
            assert false;   // throwPageFlowException() must throw.
        }
                        
        PreviousActionInfo prevActionInfo = curJpf.thePreviousActionInfo();
        
        if ( prevActionInfo != null )
        {
            String actionURI = prevActionInfo.getActionURI();
            
            if ( _log.isDebugEnabled() ) _log.debug( "return-to-action: " + actionURI );

            //
            // If there's no form specified in this return-to-action forward, then use the original form that was saved
            // in the action.  Only do this if we're not doing a redirect, which precludes request attributes.
            //
            if ( ! pageFlowFwd.isRedirect() && prevActionInfo.getForm() != null
                 && pageFlowFwd.getFirstOutputForm( request ) == null )
            {
                pageFlowFwd.addOutputForm( prevActionInfo.getForm() );
            }
            
            String query = getQueryString( pageFlowFwd, prevActionInfo );
            ActionForward fwd = new ActionForward( actionURI + query, pageFlowFwd.getRedirect() );
            fwd.setContextRelative( true );
            return fwd;
        }
        else
        {
            if ( _log.isInfoEnabled() )
            {
                _log.info( "Attempted return-to-action, but previous action info was missing." );
            }
            
            PageFlowException ex = new NoPreviousActionException( actionName, pageFlowFwd, curJpf );
            InternalUtils.throwPageFlowException( ex, request );
            assert false;   // previous method always throws
            return null;
        }
    }
    
    private static String getQueryString( Forward pageFlowFwd, PreviousInfo previousInfo )
    {
        String query = pageFlowFwd.getQueryString();
        if ( query == null ) query = "";
            
        //
        // If the restoreQueryString attribute was set, use the query string from the original action URI.
        //
        boolean restoreQueryString = pageFlowFwd.doesRestoreQueryString();
        if ( restoreQueryString )
        {
            String prevQuery = previousInfo.getQueryString();
            if ( prevQuery != null ) query += ( query.length() > 0 ? "&" : "?" ) + prevQuery;
        }
        
        return query;
    }
    
    public ActionForward doNestingReturn( FlowControllerHandlerContext context, Forward pageFlowFwd, 
                                          ActionMapping mapping, ActionForm form )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        
        PageFlowStack pfStack = PageFlowStack.get( request, getServletContext() );
        String returnAction = pageFlowFwd.getPath();
                
        if ( pfStack.isEmpty() )
        {
            PageFlowController curJpf = PageFlowUtils.getCurrentPageFlow( request, getServletContext() );
                    
            if ( _log.isInfoEnabled() )
            {
                _log.info( "Tried to pop from empty PageFlow stack.  Current = "
                           + curJpf.getClass().getName() );
            }
                    
            if ( _log.isWarnEnabled() )
            {
                InternalStringBuilder msg = new InternalStringBuilder( "Tried to pop from empty PageFlow stack." );
                msg.append( "  Current page flow is " );
                msg.append( curJpf != null ? curJpf.getClass().getName() : null );
                _log.warn( msg.append( '.' ).toString() );
            }
                    
            PageFlowException ex = new EmptyNestingStackException( returnAction, curJpf );
            InternalUtils.throwPageFlowException( ex, request );
        }
                
        // Only nested PageFlowControllers can have return actions.
        assert context.getFlowController() instanceof PageFlowController
                : context.getFlowController().getClass().getName() + " is not a " + PageFlowController.class.getName();
        ActionForward exceptionFwd = 
                ( ( PageFlowController ) context.getFlowController() ).exitNesting( request, response, mapping, form );
        if ( exceptionFwd != null ) return exceptionFwd;
                
        PageFlowStack.PushedPageFlow pushedPageFlowWrapper = pfStack.pop( request );
        PageFlowController poppedPageFlow = pushedPageFlowWrapper.getPageFlow();

        if ( _log.isDebugEnabled() )
        {
            _log.debug( "Popped PageFlowController " + poppedPageFlow + " from the nesting stack" );
        }

        InternalUtils.setCurrentPageFlow( poppedPageFlow, request, getServletContext() );

                
        //
        // If an ActionInterceptor forwarded to the nested page flow, give it a chance to change the URI as the nested
        // flow is returning.  If it doesn't, we'll go to the originally-intended Forward.
        //
        ActionInterceptor interceptor = pushedPageFlowWrapper.getInterceptor();
                
        if ( interceptor != null )
        {
            return getRegisteredActionForwardHandler().handleInterceptorReturn( context, poppedPageFlow,
                                                                                pushedPageFlowWrapper, returnAction,
                                                                                mapping, form, interceptor );
        }

        //
        // Raise the returned action on the popped pageflow.
        //                    
        assert returnAction.charAt( 0 ) != '/' : returnAction;

        if ( _log.isDebugEnabled() )
        {
            _log.debug( "Action on popped PageFlowController is " + returnAction );
        }

        InternalStringBuilder returnActionPath = new InternalStringBuilder( poppedPageFlow.getModulePath() );
        returnActionPath.append( '/' ).append( returnAction ).append( PageFlowConstants.ACTION_EXTENSION );

        //
        // Store the returned form in the request.
        //
        ActionForm retForm = pageFlowFwd.getFirstOutputForm( request );
        if ( retForm != null )
        {
            InternalUtils.setForwardedFormBean( request, retForm );
            ImplicitObjectUtil.loadOutputFormBean( request, InternalUtils.unwrapFormBean( retForm ) );
        }
                
        // Keep track of the fact that we are returning from nesting in this request.
        PageFlowRequestWrapper.get(request).setReturningFromNesting(true);
        
        //
        // Forward to the return-action on the nesting page flow.
        //
        ActionForward fwd = new ActionForward( returnActionPath.toString(), false );
        fwd.setContextRelative( true );
        return fwd;
    }
    
    public ActionForward handleInterceptorReturn( FlowControllerHandlerContext context,
                                                  PageFlowController poppedPageFlow,
                                                  PageFlowStack.PushedPageFlow pushedPageFlowWrapper,
                                                  String returnAction, ActionMapping actionMapping,
                                                  ActionForm form, ActionInterceptor interceptor )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        
        PageFlowRequestWrapper.get( request ).setReturningFromActionIntercept( true );
        
        try
        {
            AfterNestedInterceptContext interceptorContext =
                    new AfterNestedInterceptContext( request, response, getServletContext(), poppedPageFlow,
                                                     pushedPageFlowWrapper.getInterceptedForward(),
                                                     pushedPageFlowWrapper.getInterceptedActionName(),
                                                     returnAction );
            
            interceptor.afterNestedIntercept( interceptorContext );
            
            if ( interceptorContext.hasInterceptorForward() )
            {
                InterceptorForward fwd = interceptorContext.getInterceptorForward();
                
                if ( _log.isDebugEnabled() )
                {
                    InternalStringBuilder message = new InternalStringBuilder();
                    message.append( "Interceptor " );
                    message.append( interceptor.getClass().getName() );
                    message.append( " after nested page flow: " );
                    
                    if ( fwd != null )
                    {
                        message.append( "forwarding to " );
                        message.append( fwd.getPath() );
                    }
                    else
                    {
                        message.append( "returned InterceptorForward is null." );
                    }
                    
                    _log.debug( message.toString() );
                }
                
                if ( fwd != null ) fwd.rehydrateRequest( request );
                return fwd;
            }
        }
        catch ( Throwable e )
        {
            //
            // Yes, we *do* mean to catch Throwable here.  It will get re-thrown if the page flow does not handle it.
            //
            _log.error( "Exception in " + interceptor.getClass().getName() + ".afterNestedIntercept", e );
            
            try
            {
                return poppedPageFlow.handleException( e, actionMapping, form, request, response );
            }
            catch ( Exception anotherException )
            {
                _log.error( "Exception thrown while handling exception.", anotherException );
            }
        }
        
        //
        // The interceptor declined to forward us anywhere -- just go to the originally-intended Forward.
        //
        InterceptorForward fwd = pushedPageFlowWrapper.getInterceptedForward();
        fwd.rehydrateRequest( request );
        return fwd;
    }
    
    public ActionForwardHandler getRegisteredActionForwardHandler()
    {
        return ( ActionForwardHandler ) super.getRegisteredHandler();
    }
}
