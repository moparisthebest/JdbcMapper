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

import javax.servlet.ServletContext;

import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.api.context.ControlThreadContext;

/**
 * This class is the contextual service implementation for javax.servlet.ServletContext.   It
 * acts as an intermediary between the client and the ServletContext instance held by the
 * associated ServletBeanContext.   It validates that attempt to access the ServletContext
 * only occur during the servlet request processing lifecycle, then delegates to the underlying
 * ServletContext instance for actual services.
 */
/* package */ class ServletContextService
    implements ServletContext
{
    
    /* package */ ServletContextService(ServletBeanContext beanContext)
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

    final protected ServletContext getServletContext()
    {
        ServletContext servletContext = getServletBeanContext().getServletContext();
        if (servletContext == null)
            throw new IllegalStateException("No access to ServletContext outside request processing");
        return servletContext;
    }    

    public java.lang.Object getAttribute(java.lang.String name) 
    {
        return getServletContext().getAttribute(name);
    }

    public java.util.Enumeration getAttributeNames()
    {
        return getServletContext().getAttributeNames();
    }

    public ServletContext getContext(java.lang.String uripath)
    {
        return getServletContext().getContext(uripath); 
    }

    public java.lang.String getInitParameter(java.lang.String name)
    {
        return getServletContext().getInitParameter(name);
    }

    public java.util.Enumeration getInitParameterNames()
    {
        return getServletContext().getInitParameterNames();
    }

    public int getMajorVersion()
    {
        return getServletContext().getMajorVersion();
    }

    public java.lang.String getMimeType(java.lang.String file)
    {
        return getServletContext().getMimeType(file);
    }

    public int getMinorVersion()
    {
        return getServletContext().getMinorVersion();
    }

    public javax.servlet.RequestDispatcher getNamedDispatcher(java.lang.String name)
    {
        return getServletContext().getNamedDispatcher(name);
    }

    public java.lang.String getRealPath(java.lang.String path)
    {
        return getServletContext().getRealPath(path);
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(java.lang.String path)
    {
        return getServletContext().getRequestDispatcher(path);
    }

    public java.net.URL getResource(java.lang.String path) throws java.net.MalformedURLException
    {
        return getServletContext().getResource(path);
    }

    public java.io.InputStream getResourceAsStream(java.lang.String path)
    {
        return getServletContext().getResourceAsStream(path);
    }

    public java.util.Set getResourcePaths(java.lang.String path)
    {
        return getServletContext().getResourcePaths(path);
    }

    public java.lang.String getServerInfo()
    {
        return getServletContext().getServerInfo();
    }

    public javax.servlet.Servlet getServlet(java.lang.String name) throws javax.servlet.ServletException
    {
        return getServletContext().getServlet(name);
    }

    public java.lang.String getServletContextName()
    {
        return getServletContext().getServletContextName();
    }

    public java.util.Enumeration getServletNames()
    {
        return getServletContext().getServletNames();
    }

    public java.util.Enumeration getServlets()
    {
        return getServletContext().getServlets();
    }

    public void log(java.lang.String msg)
    {
        getServletContext().log(msg);
    }

    public void log(java.lang.Exception exception, java.lang.String msg)
    {
        getServletContext().log(exception, msg);
    }

    public void log(java.lang.String message, java.lang.Throwable throwable)
    {
        getServletContext().log(message, throwable);
    }

    public void removeAttribute(java.lang.String name)
    {
        getServletContext().removeAttribute(name);
    }

    public void setAttribute(java.lang.String name, java.lang.Object object)
    {
        getServletContext().setAttribute(name, object);
    }

    transient private ServletBeanContext _beanContext;  // never access directly
}
