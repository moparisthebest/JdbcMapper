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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is a ThreadLocal class that contains the servlet information.  In addition,
 * it supports a Map that allows other aspects of the framework to create place
 * their own context objects into the thread local context.
 */
public class PageFlowContext
{
    private static ThreadLocal pageflowContext = new PageFlowContextThreadLocal();

    // The context will provide access to the Servlet objects
    // @todo: do we want to abstract these further to provide an abstraction removing the Servlet API?
    private HttpServletRequest _request;
    private HttpServletResponse _response;
    private ServletContext _servletContext;

    // map containin other context objects
    private Map _contexts = new HashMap();

    /**
     * Factory method that will return the <code>PageFlowContext</code> object to the caller.
     * @return A <code>PageFlowContext</code> that is associated with the tread.
     */
    public static PageFlowContext getContext() {
        PageFlowContext ctxt = (PageFlowContext) pageflowContext.get();
        if (ctxt == null) {
            ctxt = new PageFlowContext();
            pageflowContext.set(ctxt);
        }
        return ctxt;
    }

    /**
     * Return the request object.
     * @return The <code>HttpServletRequest</code> object.
     */
    public HttpServletRequest getRequest()
    {
        return _request;
    }

    /**
     * Set the request object.
     * @param request The <code>HttpServletRequst</code>
     */
    public void setRequest(HttpServletRequest request)
    {
        _request = request;
    }

    /**
     * Return the response object to the caller.
     * @return The <code>HttpServletResponse</code> object.
     */
    public HttpServletResponse getResponse()
    {
        return _response;
    }

    /**
     * Set the response object on the context.
     * @param response The <code>HttpServletResponse</code> object.
     */
    public void setResponse(HttpServletResponse response)
    {
        _response = response;
    }

    /**
     * Return the servlet context.
     * @return The <code>ServletContext</code> object for this request.
     */
    public ServletContext getServletContext()
    {
        return _servletContext;
    }

    /**
     * Set the servlet context for this request.
     * @param servletContext The <code>ServletContext</code> object.
     */
    public void setServletContext(ServletContext servletContext)
    {
        _servletContext = servletContext;
    }

    /**
     * This method will lookup a named object and return it.  The object is
     * looked up by name.  If the object doesn't exist, the activator object is
     * called to create a new instance of the object before it is returned.
     * @param name The name of the object to return
     * @param activator An <code>PageFlowContextActivator</code> that will create the new object if
     * it doesn't exist.
     * @return The object stored by the name.
     */
    public Object get(String name, PageFlowContextActivator activator)
    {
        assert (name != null) : "Parameter 'name' must not be null";
        assert (activator != null) : "Parameter 'activator' must not be null";

        Object ret = _contexts.get(name);
        if (ret == null) {
            ret = activator.activate();
            _contexts.put(name,ret);
        }
        return ret;
    }

    /**
     * Place the context into it's initialized state.
     */
    public void init() {
        _request = null;
        _response = null;
        _servletContext = null;
        _contexts.clear();
    }

    /**
     * Thread local...
     */
    private static class PageFlowContextThreadLocal extends ThreadLocal {
        protected Object initializeValue() {
            return new PageFlowContext();
        }
    }
}
