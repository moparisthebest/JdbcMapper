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

import java.util.Stack;
import java.io.InputStream;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceProvider;
import java.net.URL;
import java.net.MalformedURLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.controls.runtime.bean.ControlContainerContext;
import org.apache.beehive.controls.runtime.bean.WebContextFactoryProvider;
import org.apache.beehive.controls.spi.context.ControlBeanContextFactory;

/**
 * The ServletBeanContext provides a ControlBeanContext implementation that offers services
 * and a resource management context that is appropriate to web tier usage of controls.
 */
public class ServletBeanContext
    extends ControlContainerContext
{
    public ServletBeanContext() {
        //
        // This sets the BeanContextServicesFactory instance on the ControlBeanContext and allows this
        // CCC object to be created with a BeanContextServicesDelegate of the type returned by this factory
        //
        super(WebContextFactoryProvider.WEB_CONTEXT_BCS_FACTORY);
    }

    /**
     * Called by BeanContextSupport superclass during construction and deserialization to
     * initialize subclass transient state
     */
    public void initialize()
    {
        super.initialize();

        //
        // Register the ServletService classes associated with the ServletServiceProvider
        //
        ServletServiceProvider ssp = ServletServiceProvider.getProvider();
        addService(ServletContext.class, ssp);
        addService(ServletRequest.class, ssp);
        addService(ServletResponse.class, ssp);
        addService(HttpServletRequest.class, ssp);
        addService(HttpServletResponse.class, ssp);

        //
        // Register an *internal* service that is used to create ControlBeanContext objects for
        // children of this control container
        //
        _bcsp = WebContextFactoryProvider.getProvider();
        addService(ControlBeanContextFactory.class, _bcsp);
    }

    /**
     * Begins a new execution context, associated with a specific ServletRequest
     */
    public void beginContext(ServletContext context, ServletRequest req, ServletResponse resp)
    {
        pushRequestContext(context, req, resp);
        super.beginContext();
    }

    /**
     * Ends the current execution context, and resetes the current active ServletRequest.
     */
    public void endContext()
    {
        super.endContext();
        popRequestContext();
    }

    private Stack<RequestContext> getRequestStack()
    {
        if (_reqStack == null)
            _reqStack = new Stack<RequestContext>();

        return _reqStack;
    }

    /**
     * Pushes the current request context onto the stack
     */
    private synchronized void pushRequestContext(ServletContext context,
                                                 ServletRequest req,
                                                 ServletResponse resp)
    {
        getRequestStack().push(new RequestContext(context, req, resp));
    }

    /**
     * Pops the current request context from the stack
     */
    private synchronized void popRequestContext()
    {
        getRequestStack().pop();
    }

    /**
     * Returns the current request context, or null is none is available
     */
    private synchronized RequestContext peekRequestContext()
    {
        Stack<RequestContext> reqStack = getRequestStack();
        if (reqStack.empty())
            return null;

        return reqStack.peek();
    }

    /**
     * Returns the ServletContext associated with this context (or null if not currently
     * processing a request)
     */
    public ServletContext getServletContext()
    {
        RequestContext reqContext = peekRequestContext();
        if (reqContext == null)
            return null;

        return  reqContext._context;
    }

    /**
     * Returns the ServletRequest associated with this context (or null if not currently
     * processing a request)
     */
    public ServletRequest getServletRequest()
    {
        RequestContext reqContext = peekRequestContext();
        if (reqContext == null)
            return null;

        return  reqContext._request;
    }

    /**
     * Returns the ServletResponse associated with this context (or null if not currently
     * processing a request)
     */
    public ServletResponse getServletResponse()
    {
        RequestContext reqContext = peekRequestContext();
        if (reqContext == null)
            return null;

        return  reqContext._response;
    }

    /**
     * Enables/disable the use of request/response wrappers for this context.  By default,
     * wrappers are enabled if this API is not invoked.
     */
    public void setWrappers(boolean useWrappers)
    {
        _useWrappers = useWrappers;
    }

    /**
     * Override BeanContext.getResourceAsStream() so it delegates to the current ServletContext.
     *
     * @param name the resource name
     * @param bcc the specified child
     * @return an <code>InputStream</code> for reading the resource, or
     *         <code>null</code> if the resource could not be found.
     * @throws IllegalArgumentException <code>IllegalArgumentException</code> if the resource is not valid
     */
    public InputStream getResourceAsStream(String name, BeanContextChild bcc)
        throws IllegalArgumentException
    {
        ServletContext sc = getServletContext();
        if ( sc != null )
            return sc.getResourceAsStream( name );

        return null;
    }

    /**
     * Override BeanContext.getResource() so it delegates to the current ServletContext.
     *
     * @param name the resource name
     * @param bcc the specified child
     * @return a <code>URL</code> for the named
     * resource for the specified child
     * @throws IllegalArgumentException <code>IllegalArgumentException</code> if the resource is not valid
     */
    public URL getResource(String name, BeanContextChild bcc)
        throws IllegalArgumentException
    {
        ServletContext sc = getServletContext();
        if ( sc != null )
        {
            try
            {
                return sc.getResource( name );
            }
            catch ( MalformedURLException mue )
            {
                throw new IllegalArgumentException( mue.getMessage() );
            }
        }

        return null;
    }

    /**
     * Override ControlBeanContext.getService().  A control bean creates its bean context using the
     * ControlBeanContextFactory service provided by this context.  A control bean will attempt to create
     * its context before adding its self to this context as a child. This creates a chicken/egg problem since
     * only a child of a context may request a service from it.
     *
     * This method provides a way to crack the chicken/egg problem by first trying to get the service using the
     * control bean context's getService() method, and if that call returns null and the requested service is
     * the ControlBeanContextFactory then returning an instance of the service provider.
     *
     * @param serviceClass
     * @param selector
     */
    public <T> T getService(Class<T> serviceClass, Object selector)
    {
        T service = super.getService(serviceClass, selector);
        if (service == null && serviceClass.equals(ControlBeanContextFactory.class)) {
            return (T)_bcsp.getService(this, this, serviceClass, selector);
        }
        return service;
    }
    protected boolean useWrappers() {
        return _useWrappers;
    }

    private static class RequestContext
    {
        RequestContext(ServletContext context, ServletRequest req, ServletResponse resp)
        {
            _context = context;
            _request = req;
            _response = resp;
        }

        ServletContext _context;
        ServletResponse _response;
        ServletRequest _request;
    }

    private boolean _useWrappers = true;
    private transient Stack<RequestContext> _reqStack;
    private transient BeanContextServiceProvider _bcsp;
}