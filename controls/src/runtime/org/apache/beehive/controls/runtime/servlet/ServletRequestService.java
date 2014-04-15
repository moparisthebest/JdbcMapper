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

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;

import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;

/**
 * This class is the contextual service implementation for javax.servlet.ServletRequest.   It
 * acts as an intermediary between the client and the ServletRequest instance held by the
 * associated ServletBeanContext.   It validates that attempt to access the ServletRequest
 * only occur during the servlet request processing lifecycle, then delegates to the underlying
 * ServletRequest instance for actual services.
 */
/* package */ class ServletRequestService implements ServletRequest
{
    /**
     * This static helper class derives from javax.servlet.ServletRequestWrapper and can
     * be used to wrap a ServletRequestService instance in cases where the client expects
     * to be able to use the standard wrapper interfaces to unwrap requests. 
     */ 
    private static class Wrapper extends ServletRequestWrapper
    {
        Wrapper(ServletRequestService requestService)
        {
            super(requestService);
            _requestService = requestService;
        }

        /**
         * Overrides the default ServletRequestWrapper.getRequest implementation.  Rather
         * than just returning the request passed into the constructor (i.e. the request
         * service), it will go unwrap step further and return the current (active) http
         * request.
         */ 
        public ServletRequest getRequest()
        {
            return _requestService.getServletRequest();
        }

        ServletRequestService _requestService;
    }
    
    /* package */ ServletRequestService(ServletBeanContext beanContext)
    {
        _beanContext = beanContext;
    }

    /**
     * This method retrieves the current ServletBeanContext for the service.  It is capable
     * of reassociating with the current active context, if the service instance has been
     * serialized/deserialized.
     */
    final protected ServletBeanContext getServletBeanContext()
    {
        if (_beanContext == null)
        {
            ControlContainerContext ccc = ControlThreadContext.getContext();
            if (! (ccc instanceof ServletBeanContext))
                throw new IllegalStateException("No ServletBeanContext available");

            _beanContext = (ServletBeanContext)ccc;
        }
        return _beanContext;
    }

    /**
     * This method returns a ServletRequestWrapper instance that wraps the request service.
     * This is useful in instances where there is code that uses the wrapper interfaces to
     * unwrap requests to get to the 'root' request.
     */ 
    /* package */ ServletRequestWrapper getRequestWrapper()
    {
        return new Wrapper(this);
    }

    final protected ServletRequest getServletRequest()
    {
        ServletRequest servletRequest = getServletBeanContext().getServletRequest();
        if (servletRequest == null)
            throw new IllegalStateException("No access to ServletRequest outside request processing");
        return servletRequest;
    }

    public java.lang.Object getAttribute(java.lang.String name)
    {
        return getServletRequest().getAttribute(name);
    }

    public java.util.Enumeration getAttributeNames()
    {
        return getServletRequest().getAttributeNames();
    }

    public java.lang.String getCharacterEncoding()
    {
        return getServletRequest().getCharacterEncoding();
    }

    public void setCharacterEncoding(java.lang.String env) 
                throws java.io.UnsupportedEncodingException
    {
        getServletRequest().setCharacterEncoding(env);
    }

    public int getContentLength()
    {
        return getServletRequest().getContentLength();
    }

    public java.lang.String getContentType()
    {
        return getServletRequest().getContentType();
    }

    public javax.servlet.ServletInputStream getInputStream() throws java.io.IOException 
    {
        return getServletRequest().getInputStream();
    }

    public java.lang.String getParameter(java.lang.String name)
    {
        return getServletRequest().getParameter(name);
    }

    public java.util.Enumeration getParameterNames()
    {
        return getServletRequest().getParameterNames();
    }

    public java.lang.String[] getParameterValues(java.lang.String name)
    {
        return getServletRequest().getParameterValues(name);
    }

    public java.util.Map getParameterMap()
    {
        return getServletRequest().getParameterMap();
    }

    public java.lang.String getProtocol()
    {
        return getServletRequest().getProtocol();
    }

    public java.lang.String getScheme()
    {
        return getServletRequest().getScheme();
    }

    public java.lang.String getServerName()
    {
        return getServletRequest().getServerName();
    }

    public int getServerPort()
    {
        return getServletRequest().getServerPort();
    }

    public int getLocalPort()
    {
        return getServletRequest().getLocalPort();
    }

    public java.lang.String getLocalAddr()
    {
        return getServletRequest().getLocalAddr();
    }

    public java.lang.String getLocalName()
    {
        return getServletRequest().getLocalName();
    }


    public java.io.BufferedReader getReader() throws java.io.IOException
    {
        return getServletRequest().getReader();
    }

    public java.lang.String getRemoteAddr()
    {
        return getServletRequest().getRemoteAddr();
    }

    public java.lang.String getRemoteHost()
    {
        return getServletRequest().getRemoteHost();
    }

    public int getRemotePort()
    {
        return getServletRequest().getRemotePort();
    }

    public void setAttribute(java.lang.String name, java.lang.Object o)
    {
        getServletRequest().setAttribute(name, o);
    }

    public void removeAttribute(java.lang.String name)
    {
        getServletRequest().removeAttribute(name);
    }

    public java.util.Locale getLocale()
    {
        return getServletRequest().getLocale();
    }

    public java.util.Enumeration getLocales()
    {
        return getServletRequest().getLocales();
    }

    public boolean isSecure()
    {
        return getServletRequest().isSecure();
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(java.lang.String path)
    {
        return getServletRequest().getRequestDispatcher(path);
    }

    public java.lang.String getRealPath(java.lang.String path)
    {
        return getServletRequest().getRealPath(path);
    }

    transient private ServletBeanContext _beanContext;  // never access directly
}
