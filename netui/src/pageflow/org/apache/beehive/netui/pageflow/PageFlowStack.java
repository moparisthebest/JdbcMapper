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

import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.interceptor.action.InterceptorForward;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptorContext;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.StorageHandler;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import java.util.Stack;
import java.io.Serializable;

/**
 * <p>
 * Stack for keeping track of a series of nested page flows.  When a nested page flow is entered,
 * the previous page flow is pushed onto this stack, which is kept in the user session.
 * </p>
 * <p>
 * This Stack implements the {@link HttpSessionBindingListener} which will receive a callback when
 * this is removed from the {@link HttpSession}.  At this time, any PageFlowController instances stored
 * on the stack will be destroyed using the {@link PageFlowManagedObject#destroy(javax.servlet.http.HttpSession)}
 * lifecycle method.
 * </p>
 */ 
public class PageFlowStack
        implements HttpSessionBindingListener, Serializable
{
    private static final Logger _log = Logger.getInstance( PageFlowStack.class );
    private static final String JPF_STACK_ATTR = InternalConstants.ATTR_PREFIX  + "nestingStack";
    
    private Stack _stack = new Stack();
    private transient ServletContext _servletContext;
    
    /**
     * Wrapper that contains a pushed page flow and information related to it.
     */ 
    public static class PushedPageFlow implements Serializable
    {
        private PageFlowController _pageFlow;
        private ActionInterceptor _interceptor;
        private InterceptorForward _interceptedForward;
        private String _interceptedActionName;

        public PushedPageFlow( PageFlowController pageFlow )
        {
            _pageFlow = pageFlow;
        }
        
        public PushedPageFlow( PageFlowController pageFlow, ActionInterceptor interceptor,
                               InterceptorForward interceptedFwd, String interceptedActionName )
        {
            this( pageFlow );
            _interceptor = interceptor;
            _interceptedForward = interceptedFwd;
            _interceptedActionName = interceptedActionName;
        }

        public PageFlowController getPageFlow()
        {
            return _pageFlow;
        }

        public ActionInterceptor getInterceptor()
        {
            return _interceptor;
        }

        public InterceptorForward getInterceptedForward()
        {
            return _interceptedForward;
        }

        public String getInterceptedActionName()
        {
            return _interceptedActionName;
        }
        
        public String toString() {
            return _pageFlow.getURI();
        }
    }
    
    /**
     * Get the stack of nested page flows for the current user session.  Create and store an empty
     * stack if none exists.
     * 
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return the stack of nested page flows {@link PushedPageFlow}s) for the current user session.
     */
    public static PageFlowStack get( HttpServletRequest request, ServletContext servletContext )
    {
        return get( request, servletContext, true );
    }
    
   /**
     * Get the stack of nested page flows for the current user session.  Create and store an empty
     * stack if none exists.
     * @deprecated Use {@link #get(HttpServletRequest, ServletContext)} instead.
     * 
     * @param request the current HttpServletRequest.
     * @return the stack of nested page flows {@link PushedPageFlow}s) for the current user session.
     */
    public static PageFlowStack get( HttpServletRequest request )
    {
        return get( request, InternalUtils.getServletContext( request ) );
    }
    
    /**
     * Get the stack of nested page flows for the current user session.  Create and store an empty
     * stack if none exists.
     * 
     * @param request the current HttpServletRequest.
     * @param servletContext the current ServletContext.
     * @return a {@link PageFlowStack} of nested page flows ({@link PageFlowController}s) for the current user session.
     */    
    public static PageFlowStack get( HttpServletRequest request, ServletContext servletContext, boolean createIfNotExist )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName( JPF_STACK_ATTR, unwrappedRequest );
        PageFlowStack jpfStack = ( PageFlowStack ) sh.getAttribute( rc, attrName );
        
        if ( jpfStack == null && createIfNotExist )
        {
            jpfStack = new PageFlowStack( servletContext );
            jpfStack.save( request );
        }
        else if ( jpfStack != null )
        {
            jpfStack.setServletContext( servletContext );
        }
        
        return jpfStack;
    }

    /**
     * Get the stack of nested page flows for the current user session.  Create and store an empty
     * stack if none exists.
     * @deprecated Use {@link #get(HttpServletRequest, ServletContext, boolean)} instead.
     * 
     * @param request the current HttpServletRequest
     * @return a {@link PageFlowStack} of nested page flows ({@link PageFlowController}s) for the current user session.
     */    
    public static PageFlowStack get( HttpServletRequest request, boolean createIfNotExist )
    {
        return get( request, InternalUtils.getServletContext( request ), createIfNotExist );
    }
    
    /**
     * Destroy the stack of {@link PageFlowController}s that have invoked nested page flows.
     * 
     * @param request the current HttpServletRequest.
     */ 
    public void destroy( HttpServletRequest request )
    {
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName( JPF_STACK_ATTR, unwrappedRequest );
        
        sh.removeAttribute( rc, attrName );
    }
    
    /**
     * Pop page flows from the nesting stack until one of the given type is found.
     * 
     * @return the last popped page flow if one of the given type was found, or <code>null</code>
     *         if none was found.
     */ 
    PageFlowController popUntil( HttpServletRequest request, Class stopAt, boolean onlyIfPresent )
    {
        if (onlyIfPresent && lastIndexOf(stopAt) == -1) {
            return null;
        }
        
        while ( ! isEmpty()  )
        {
            PageFlowController popped = pop( request ).getPageFlow();
            
            if ( popped.getClass().equals( stopAt ) )
            {
                //
                // If we've popped everything from the stack, remove the stack attribute from the session.
                //
                if ( isEmpty() ) destroy( request );
                return popped;
            }
            else
            {
                //
                // We're discarding the popped page flow.  Invoke its destroy() callback, unless it's longLived.
                //
                if ( ! popped.isLongLived() ) popped.destroy( request.getSession( false ) );
            }
        }

        destroy( request );   // we're empty -- remove the attribute from the session.
        return null;
    }
    
    private int lastIndexOf( Class target )
    {
        for ( int i = _stack.size() - 1; i >= 0; --i )
        {
            if ( ( ( PushedPageFlow ) _stack.elementAt( i ) ).getPageFlow().getClass().equals( target ) )
            {
                return i;
            }
        }
        
        return -1;
    }
    
    void ensureFailover( HttpServletRequest request, ServletContext servletContext )
    {
        StorageHandler sh = Handlers.get( servletContext ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName( JPF_STACK_ATTR, unwrappedRequest );
        
        sh.ensureFailover( rc, attrName, this );
    }
    
    void save( HttpServletRequest request )
    {
        StorageHandler sh = Handlers.get( getServletContext() ).getStorageHandler();
        HttpServletRequest unwrappedRequest = PageFlowUtils.unwrapMultipart( request );
        RequestContext rc = new RequestContext( unwrappedRequest, null );
        String attrName = ScopedServletUtils.getScopedSessionAttrName( JPF_STACK_ATTR, unwrappedRequest );
        
        sh.setAttribute( rc, attrName, this );
    }
    
    private PageFlowStack( ServletContext servletContext )
    {
        _servletContext = servletContext;
    }
    
    /**
     * Push a page flow onto the stack of nested page flows in the session.
     * 
     * @param pageFlow the page flow to push.
     * @param request the current HttpServletRequest.
     */ 
    public void push( PageFlowController pageFlow, HttpServletRequest request )
    {
        ActionInterceptorContext interceptorContext = ActionInterceptorContext.getActiveContext( request, true );
        if ( interceptorContext != null )
        {
            ActionInterceptor interceptor = interceptorContext.getOverridingActionInterceptor();
            InterceptorForward originalForward = interceptorContext.getOriginalForward();
            String actionName = interceptorContext.getActionName();
            _stack.push( new PushedPageFlow( pageFlow, interceptor, originalForward, actionName ) );
        }
        else
        {
            _stack.push( new PushedPageFlow( pageFlow ) );
        }
        
        // Tell the page flow that it is on the nesting stack.
        pageFlow.setIsOnNestingStack( true );
        
        // To ensure that this attribute is replicated for session failover.
        ensureFailover( request, getServletContext() );
    }
    
    /**
     * Pop the most recently-pushed page flow from the stack of nested page flows in the session. 
     * 
     * @param request the current HttpServletRequest.
     * @return a {@link PushedPageFlow} that represents the popped page flow.
     */ 
    public PushedPageFlow pop( HttpServletRequest request )
    {
        PushedPageFlow ppf = ( PushedPageFlow ) _stack.pop();
        PageFlowController pfc = ppf.getPageFlow();
        pfc.setIsOnNestingStack( false );
        
        if ( request != null )  // may be null if we're called from valueUnbound()
        {
            ServletContext servletContext = getServletContext();
            
            // Reinitialize the page flow, in case it's lost its transient state.
            pfc.reinitialize( request, null, servletContext );
            ensureFailover( request, servletContext );   // to ensure that this attribute is replicated for session failover
        }
        
        return ppf;
    }
    
    /**
     * Get the most recently-pushed page flow from the stack of nested page flows in the session.
     * 
     * @return a {@link PushedPageFlow} that represents the page flow at the top of the stack.
     */ 
    public PushedPageFlow peek()
    {
        return ( PushedPageFlow ) _stack.peek();
    }
    
    /**
     * Tell whether the stack of nested page flows is empty.
     * 
     * @return <code>true</code> if there are no nested page flows on the stack.
     */ 
    public boolean isEmpty()
    {
        return _stack.isEmpty();
    }
    
    /**
     * Get the size of the stack of nested page flows.
     * 
     * @return the number of page flows that are currently (hidden away) on the stack.
     */ 
    public int size()
    {
        return _stack.size();
    }
    
    /**
     * Callback for {@link HttpSessionBindingListener} -- should not be invoked directly.
     */
    public void valueBound( HttpSessionBindingEvent event )
    {
    }

    /**
     * Callback for {@link HttpSessionBindingListener} -- should not be invoked directly.
     */
    public void valueUnbound( HttpSessionBindingEvent event )
    {
        if ( _log.isDebugEnabled() )
        {
            _log.debug( "The page flow stack is being unbound from the session." );
        }
        
        while ( ! isEmpty() )
        {
            PageFlowController jpf = pop( null ).getPageFlow();
            
            // Note that this page flow may have been serialized/deserialized, which will cause its transient info
            // to be lost.  Rehydrate it.
            HttpSession session = event.getSession();
            if ( session != null ) jpf.reinitialize( null, null, session.getServletContext() );
            
            if ( ! jpf.isLongLived() ) jpf.destroy( event.getSession() );
        }
    }
    
    /**
     * Get a stack of PageFlowControllers, not of PushedPageFlows.
     */ 
    Stack getLegacyStack()
    {
        Stack ret = new Stack();
        
        for ( int i = 0; i < _stack.size(); ++i )
        {
            ret.push( ( ( PushedPageFlow ) _stack.get( i ) ).getPageFlow() );
        }
        
        return ret;
    }

    private ServletContext getServletContext()
    {
        return _servletContext;
    }

    private void setServletContext( ServletContext servletContext )
    {
        _servletContext = servletContext;
    }

    /**
     * Internal (to our framework) method for seeing whether a given action exists in a page flow that is somewhere in
     * the stack.  If so, the page flow's ModuleConfig is returned.
     */
    ModuleConfig findActionInStack(String actionPath)
    {
        for ( int i = _stack.size() - 1; i >= 0; --i )
        {
            ModuleConfig moduleConfig = ((PushedPageFlow) _stack.elementAt(i)).getPageFlow().getModuleConfig();
            
            if (moduleConfig.findActionConfig(actionPath) != null) {
                return moduleConfig;
            }
        }
    
        return null;
    }
    
    public String toString()
    {
        if (_stack.isEmpty()) {
            return "[empty]";
        }
        
        InternalStringBuilder sb = new InternalStringBuilder(_stack.get(0).toString());
        for (int i = 1; i < _stack.size(); ++i) {
            sb.append(" -> ").append(_stack.get(i).toString());
        }
        return sb.toString();
    }
}
