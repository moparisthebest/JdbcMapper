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

import org.apache.beehive.netui.pageflow.FlowController;
import org.apache.beehive.netui.pageflow.PreviousPageInfo;
import org.apache.struts.upload.MultipartRequestWrapper;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;

/**
 * Request wrapper that contains request-scoped values that our runtime uses.  This is faster than sticking everything
 * into attributes on the request.
 */
public final class PageFlowRequestWrapper
        extends HttpServletRequestWrapper
{
    private static final class State
    {
        public Integer forwardedRequestCount;
        public String originalServletPath;
        public FlowController currentFlowController;
        public ViewRenderer viewRenderer;
        public PreviousPageInfo previousPageInfo;
        public boolean returningFromActionIntercept = false;
        public String pageFlowScopedFormName;
        public boolean processPopulateAlreadyCalled = false;
        public boolean forwardedByButton = false;
        public MultipartRequestWrapper multipartRequestWrapper;
        public Throwable exceptionBeingHandled = null;
        public boolean stayInCurrentModule = false;
        public boolean scopedLookup = false;
        public boolean returningFromNesting = false;
    }
    
    private State _state = new State();
    
    public static PageFlowRequestWrapper get( ServletRequest servletRequest )
    {
        servletRequest = InternalUtils.unwrapMultipart( servletRequest );
        assert servletRequest instanceof PageFlowRequestWrapper : servletRequest.getClass().getName();
        return ( PageFlowRequestWrapper ) servletRequest;
    }
    
    /**
     * Unwrap to find the PageFlowRequestWrapper.  This method may return <code>null</code>.
     */
    public static PageFlowRequestWrapper unwrap( ServletRequest servletRequest )
    {
        servletRequest = InternalUtils.unwrapMultipart( servletRequest );
        
        while ( ! ( servletRequest instanceof PageFlowRequestWrapper ) )
        {
            if ( ! ( servletRequest instanceof ServletRequestWrapper ) ) return null;
            servletRequest = ( ( ServletRequestWrapper ) servletRequest ).getRequest();
        }
        
        return ( PageFlowRequestWrapper ) servletRequest;
    }
    
    public void initFrom( PageFlowRequestWrapper wrapper )
    {
        _state = wrapper._state;
    }
    
    public PageFlowRequestWrapper( HttpServletRequest delegate )
    {
        super( delegate );
    }
    
    public boolean isForwardedRequest()
    {
        return _state.forwardedRequestCount != null;
    }
    
    public int getForwardedRequestCount()
    {
        return _state.forwardedRequestCount != null ? _state.forwardedRequestCount.intValue() : 0;
    }
    
    public void setForwardedRequestCount( int count )
    {
        _state.forwardedRequestCount = new Integer( count );
    }

    public String getOriginalServletPath()
    {
        return _state.originalServletPath;
    }

    public void setOriginalServletPath( String originalServletPath )
    {
        _state.originalServletPath = originalServletPath;
    }

    public FlowController getCurrentFlowController()
    {
        return _state.currentFlowController;
    }

    public void setCurrentFlowController( FlowController currentFlowController )
    {
        _state.currentFlowController = currentFlowController;
    }

    public ViewRenderer getViewRenderer()
    {
        return _state.viewRenderer;
    }

    public void setViewRenderer( ViewRenderer viewRenderer )
    {
        _state.viewRenderer = viewRenderer;
    }

    public PreviousPageInfo getPreviousPageInfo( boolean remove )
    {
        PreviousPageInfo retVal = _state.previousPageInfo;
        if ( remove ) _state.previousPageInfo = null;
        return retVal;
    }

    public void setPreviousPageInfo( PreviousPageInfo previousPageInfo )
    {
        _state.previousPageInfo = previousPageInfo;
    }

    public boolean isReturningFromActionIntercept()
    {
        return _state.returningFromActionIntercept;
    }

    public void setReturningFromActionIntercept( boolean returningFromActionIntercept )
    {
        _state.returningFromActionIntercept = returningFromActionIntercept;
    }

    public HttpServletRequest getHttpRequest()
    {
        return ( HttpServletRequest ) super.getRequest();
    }
    
    public String getPageFlowScopedFormName()
    {
        return _state.pageFlowScopedFormName;
    }
    
    public void setPageFlowScopedFormName( String pageFlowScopedFormName )
    {
        _state.pageFlowScopedFormName = pageFlowScopedFormName;
    }
    
    public boolean isProcessPopulateAlreadyCalled()
    {
        return _state.processPopulateAlreadyCalled;
    }
    
    public void setProcessPopulateAlreadyCalled( boolean processPopulateAlreadyCalled )
    {
        _state.processPopulateAlreadyCalled = processPopulateAlreadyCalled;
    }
    
    public boolean isForwardedByButton()
    {
        return _state.forwardedByButton;
    }
    
    public void setForwardedByButton( boolean forwardedByButton )
    {
        _state.forwardedByButton = forwardedByButton;
    }
    
    public MultipartRequestWrapper getMultipartRequestWrapper()
    {
        return _state.multipartRequestWrapper;
    }
    
    public void setMultipartRequestWrapper( MultipartRequestWrapper multipartRequestWrapper )
    {
        _state.multipartRequestWrapper = multipartRequestWrapper;
    }
    
    public boolean isStayInCurrentModule()
    {
        return _state.stayInCurrentModule;
    }
    
    public void setStayInCurrentModule( boolean stayInCurrentModule )
    {
        _state.stayInCurrentModule = stayInCurrentModule;
    }

    public boolean isScopedLookup()
    {
        return _state.scopedLookup;
    }

    public void setScopedLookup( boolean scopedLookup )
    {
        _state.scopedLookup = scopedLookup;
    }

    public Throwable getExceptionBeingHandled()
    {
        return _state.exceptionBeingHandled;
    }
    
    public void setExceptionBeingHandled( Throwable th )
    {
        _state.exceptionBeingHandled = th;
    }
    
    public boolean isReturningFromNesting()
    {
        return _state.returningFromNesting;
    }
    
    public void setReturningFromNesting(boolean returningFromNesting)
    {
        _state.returningFromNesting = returningFromNesting;
    }
    
    public static PageFlowRequestWrapper wrapRequest( HttpServletRequest req )
    {
        if ( req instanceof PageFlowRequestWrapper )
        {
            return (PageFlowRequestWrapper) req;
        }
        
        PageFlowRequestWrapper retVal = new PageFlowRequestWrapper( req );
        
        // If there's *any* PageFlowRequestWrapper up the chain of wrapped requests, we must copy values from that.
        ServletRequest j = retVal.getRequest();
        while ( j instanceof HttpServletRequestWrapper )
        {
            if ( j instanceof PageFlowRequestWrapper )
            {
                retVal.initFrom( ( PageFlowRequestWrapper ) j );
                break;
            }
            
            j = ( ( HttpServletRequestWrapper ) j ).getRequest();
        }
        
        return retVal;
    }
    
    /**
     * This override returns "utf-8" if the character encoding in the request is null.  It works around a Struts
     * issue (http://issues.apache.org/bugzilla/show_bug.cgi?id=29668), where CommonsMultipartRequestHandler uses
     * the character encoding from the request, which causes problems if the encoding wasn't specified in the request
     * (as it usually never is).  This is tracked in Beehive's JIRA as 
     * http://issues.apache.org/jira/browse/BEEHIVE-803 .
     */
    public String getCharacterEncoding()
    {
        String encoding = super.getCharacterEncoding();
        return encoding != null ? encoding : "utf-8";
    }
}
