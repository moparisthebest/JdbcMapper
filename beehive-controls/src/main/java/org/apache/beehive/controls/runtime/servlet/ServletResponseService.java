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

import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;

import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;

/**
 * This class is the contextual service implementation for javax.servlet.ServletResponse.   It
 * acts as an intermediary between the client and the ServletResponse instance held by the
 * associated ServletBeanContext.   It validates that attempt to access the ServletResponse
 * only occur during the servlet request processing lifecycle, then delegates to the underlying
 * ServletResponse instance for actual services.
 */
/* package */ class ServletResponseService implements ServletResponse
{
    /**
     * This static helper class derives from javax.servlet.ServletRequestWrapper and can
     * be used to wrap a ServletRequestService instance in cases where the client expects
     * to be able to use the standard wrapper interfaces to unwrap requests. 
     */ 
    private static class Wrapper extends ServletResponseWrapper
    {
        Wrapper(ServletResponseService responseService)
        {
            super(responseService);
            _responseService = responseService;
        }

        /**
         * Overrides the default ServletRequestWrapper.getRequest implementation.  Rather
         * than just returning the request passed into the constructor (i.e. the request
         * service), it will go unwrap step further and return the current (active) http
         * request.
         */ 
        public ServletResponse getResponse()
        {
            return _responseService.getServletResponse();
        }

        ServletResponseService _responseService;
    }
    
    /* package */ ServletResponseService(ServletBeanContext beanContext)
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

    final protected ServletResponse getServletResponse()
    {
        ServletResponse servletResponse = getServletBeanContext().getServletResponse();
        if (servletResponse == null)
            throw new IllegalStateException("No access to ServletResponse outside request processing");
        return servletResponse;
    }

    /**
     * This method returns a ServletResponseWrapper instance that wraps the response service.
     * This is useful in instances where there is code that uses the wrapper interfaces to
     * unwrap responses to get to the 'root' response.
     */ 
    /* package */ ServletResponseWrapper getResponseWrapper()
    {
        return new Wrapper(this);
    }

    public java.lang.String getCharacterEncoding()
    {
        return getServletResponse().getCharacterEncoding();
    }

    public java.lang.String getContentType()
    {
        return getServletResponse().getContentType();
    }

    public javax.servlet.ServletOutputStream getOutputStream() throws java.io.IOException
    {
        return getServletResponse().getOutputStream();
    }

    public java.io.PrintWriter getWriter() throws java.io.IOException
    {
        return getServletResponse().getWriter();
    }

    public void setCharacterEncoding(java.lang.String charset)
    {
        getServletResponse().setCharacterEncoding(charset);
    }

    public void setContentLength(int len)
    {
        getServletResponse().setContentLength(len);
    }

    public void setContentType(java.lang.String type)
    {
        getServletResponse().setContentType(type);
    }

    public void setBufferSize(int size)
    {
        getServletResponse().setBufferSize(size);
    }

    public int getBufferSize()
    {
        return getServletResponse().getBufferSize();
    }

    public void flushBuffer() throws java.io.IOException
    {
        getServletResponse().flushBuffer();
    }

    public void resetBuffer()
    {
        getServletResponse().resetBuffer();
    }

    public boolean isCommitted()
    {
        return getServletResponse().isCommitted();
    }

    public void reset()
    {
        getServletResponse().reset();
    }

    public void setLocale(java.util.Locale loc)
    {
        getServletResponse().setLocale(loc);
    }

    public java.util.Locale getLocale() 
    {
        return getServletResponse().getLocale();
    }

    transient private ServletBeanContext _beanContext;  // never access directly
}
