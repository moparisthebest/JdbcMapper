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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.ServletResponse;

/**
 * This class is the contextual service implementation for javax.servlet.http.HttpServletResponse.
 * It acts as an intermediary between the client and the HttpServletResponse instance held by the
 * associated ServletBeanContext.  It validates that attempt to access the HttpServletResponse
 * only occur during the servlet request processing lifecycle, then delegates to the underlying
 * HttpServletResponse instance for actual services.
 */
/* package */ class HttpResponseService
    extends ServletResponseService 
    implements HttpServletResponse
{

    // todo: how will this class version with subsequent Servlet specification versions?
    
    /**
     * This static helper class derives from javax.servlet.HttpServletResponseWrapper and can
     * be used to wrap a HttpServletResponseService instance in cases where the client expects
     * to be able to use the standard wrapper interfaces to unwrap responses. 
     */ 
    private static class Wrapper extends HttpServletResponseWrapper
    {
        Wrapper(HttpResponseService responseService)
        {
            super(responseService);
            _responseService = responseService;
        }

        /**
         * Overrides the default HttpServletRequestWrapper.getResponse implementation.  Rather
         * than just returning the request passed into the constructor (i.e. the request
         * service), it will go unwrap step further and return the current (active) http
         * response.
         */ 
        public HttpServletResponse getResponse()
        {
            return _responseService.getHttpServletResponse();
        }

        HttpResponseService _responseService;
    }
    
    
    /* package */ HttpResponseService(ServletBeanContext beanContext)
    {
        super(beanContext);
    }

    final protected HttpServletResponse getHttpServletResponse()
    {
        ServletResponse servletRequest = getServletBeanContext().getServletResponse();
        if (! (servletRequest instanceof HttpServletResponse))
            throw new IllegalStateException("Current request is not an HttpServletResponse");
        return (HttpServletResponse)servletRequest;
    }

    /**
     * This method returns a ServletRequestWrapper instance that wraps the request service.
     * This is useful in instances where there is code that uses the wrapper interfaces to
     * unwrap requests to get to the 'root' request.
     */ 
    /* package */ HttpServletResponse getHttpResponseWrapper()
    {
        return new Wrapper(this);
    }

    public void addCookie(javax.servlet.http.Cookie cookie)
    {
        getHttpServletResponse().addCookie(cookie);
    }

    public boolean containsHeader(java.lang.String name)
    {
        return getHttpServletResponse().containsHeader(name);
    }

    public java.lang.String encodeURL(java.lang.String url)
    {
        return getHttpServletResponse().encodeURL(url);
    }

    public java.lang.String encodeRedirectURL(java.lang.String url)
    {
        return getHttpServletResponse().encodeRedirectURL(url);
    }

    public java.lang.String encodeUrl(java.lang.String url)
    {
        return getHttpServletResponse().encodeUrl(url);
    }

    public java.lang.String encodeRedirectUrl(java.lang.String url)
    {
        return getHttpServletResponse().encodeRedirectUrl(url);
    }

    public void sendError(int sc, java.lang.String msg) throws java.io.IOException    
    {
        getHttpServletResponse().sendError(sc, msg);
    }

    public void sendError(int sc) throws java.io.IOException
    {
        getHttpServletResponse().sendError(sc);
    }

    public void sendRedirect(java.lang.String location) throws java.io.IOException
    {
        getHttpServletResponse().sendRedirect(location);
    }

    public void setDateHeader(java.lang.String name, long date)
    {
        getHttpServletResponse().setDateHeader(name, date);
    }

    public void addDateHeader(java.lang.String name, long date)
    {
        getHttpServletResponse().addDateHeader(name, date);
    }

    public void setHeader(java.lang.String name, java.lang.String value)
    {
        getHttpServletResponse().setHeader(name, value);
    }

    public void addHeader(java.lang.String name, java.lang.String value)
    {
        getHttpServletResponse().addHeader(name, value);
    }

    public void setIntHeader(java.lang.String name, int value)
    {
        getHttpServletResponse().setIntHeader(name, value);
    }

    public void addIntHeader(java.lang.String name, int value)
    {
        getHttpServletResponse().addIntHeader(name, value);
    }

    public void setStatus(int sc)
    {
        getHttpServletResponse().setStatus(sc);
    }

    public void setStatus(int sc, java.lang.String sm)
    {
        getHttpServletResponse().setStatus(sc, sm);
    }
}
