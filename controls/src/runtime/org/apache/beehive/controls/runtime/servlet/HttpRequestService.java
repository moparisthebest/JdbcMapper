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
package org.apache.beehive.controls.runtime.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.ServletRequest;

/**
 * This class is the contextual service implementation for javax.servlet.http.HttpServletRequest.
 * It acts as an intermediary between the client and the HttpServletRequest instance held by the
 * associated ServletBeanContext.  It validates that attempt to access the HttpServletRequest
 * only occur during the servlet request processing lifecycle, then delegates to the underlying
 * HttpServletRequest instance for actual services.
 */
/* package */ class HttpRequestService
    extends ServletRequestService
    implements HttpServletRequest
{

    // todo: how will this class version with subsequent Servlet specification versions?

    /**
     * This static helper class derives from javax.servlet.HttpServletRequestWrapper and can
     * be used to wrap a HttpServletRequestService instance in cases where the client expects
     * to be able to use the standard wrapper interfaces to unwrap requests. 
     */ 
    private static class Wrapper extends HttpServletRequestWrapper
    {
        Wrapper(HttpRequestService requestService)
        {
            super(requestService);
            _requestService = requestService;
        }

        /**
         * Overrides the default HttpServletRequestWrapper.getRequest implementation.  Rather
         * than just returning the request passed into the constructor (i.e. the request
         * service), it will go unwrap step further and return the current (active) http
         * request.
         */ 
        public HttpServletRequest getRequest()
        {
            return _requestService.getHttpServletRequest();
        }

        HttpRequestService _requestService;
    }
    
    /* package */ HttpRequestService(ServletBeanContext beanContext)
    {
        super(beanContext);
    }

    final protected HttpServletRequest getHttpServletRequest()
    {
        ServletRequest servletRequest = getServletBeanContext().getServletRequest();
        if (! (servletRequest instanceof HttpServletRequest))
            throw new IllegalStateException("Current request is not an HttpServletRequest");
        return (HttpServletRequest)servletRequest;
    }

    /**
     * This method returns a HttpServletRequestWrapper instance that wraps the request service.
     * This is useful in instances where there is code that uses the wrapper interfaces to
     * unwrap requests to get to the 'root' request.
     */ 
    /* package */ HttpServletRequestWrapper getHttpRequestWrapper()
    {
        return new Wrapper(this);
    }

    public java.lang.String getAuthType()
    {
        return getHttpServletRequest().getAuthType();
    }

    public javax.servlet.http.Cookie[] getCookies()
    {
        return getHttpServletRequest().getCookies();
    }

    public long getDateHeader(java.lang.String name)
    {
        return getHttpServletRequest().getDateHeader(name);
    }

    public java.lang.String getHeader(java.lang.String name)
    {
        return getHttpServletRequest().getHeader(name);
    }

    public java.util.Enumeration getHeaders(java.lang.String name)
    {
        return getHttpServletRequest().getHeaders(name);
    }

    public java.util.Enumeration getHeaderNames()
    {
        return getHttpServletRequest().getHeaderNames();
    }

    public int getIntHeader(java.lang.String name)
    {
        return getHttpServletRequest().getIntHeader(name);
    }

    public java.lang.String getMethod()
    {
        return getHttpServletRequest().getMethod();
    }

    public java.lang.String getPathInfo()
    {
        return getHttpServletRequest().getPathInfo();
    }

    public java.lang.String getPathTranslated()
    {
        return getHttpServletRequest().getPathTranslated();
    }

    public java.lang.String getContextPath()
    {
        return getHttpServletRequest().getContextPath();
    }

    public java.lang.String getQueryString()
    {
        return getHttpServletRequest().getQueryString();
    }

    public java.lang.String getRemoteUser()
    {
        return getHttpServletRequest().getRemoteUser();
    }

    public boolean isUserInRole(java.lang.String role)
    {
        return getHttpServletRequest().isUserInRole(role);
    }

    public java.security.Principal getUserPrincipal()
    {
        return getHttpServletRequest().getUserPrincipal();
    }

    public java.lang.String getRequestedSessionId()
    {
        return getHttpServletRequest().getRequestedSessionId();
    }

    public java.lang.String getRequestURI()
    {
        return getHttpServletRequest().getRequestURI();
    }

    public java.lang.StringBuffer getRequestURL()
    {
        return getHttpServletRequest().getRequestURL();
    }

    public java.lang.String getServletPath()
    {
        return getHttpServletRequest().getServletPath();
    }

    public javax.servlet.http.HttpSession getSession(boolean create)
    {
        return getHttpServletRequest().getSession(create);
    }

    public javax.servlet.http.HttpSession getSession()
    {
        return getHttpServletRequest().getSession();
    }

    public boolean isRequestedSessionIdValid()
    {
        return getHttpServletRequest().isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return getHttpServletRequest().isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return getHttpServletRequest().isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return getHttpServletRequest().isRequestedSessionIdFromUrl();
    }
}
