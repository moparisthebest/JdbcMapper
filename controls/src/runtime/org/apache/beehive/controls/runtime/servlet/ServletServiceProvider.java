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

import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextServiceProvider;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The ServletContextProvider helper class acts at the BeanContextServiceProvider provides
 * instances of web tier services associated with a ServletBeanContext
 */
class ServletServiceProvider implements BeanContextServiceProvider
{
    static final private ServletServiceProvider _provider = new ServletServiceProvider();

    static ServletServiceProvider getProvider() {
        return _provider;
    }

    /* package */ ServletServiceProvider() {}

    public Object getService(BeanContextServices bcs, Object requestor, Class serviceClass, Object serviceSelector)
    {
        //
        // These services are only available to controls running within the scope of a
        // ServletBeanContext
        //
        if (! (bcs instanceof ServletBeanContext))
            return null;

        ServletBeanContext sbc = (ServletBeanContext)bcs;
        if (serviceClass.equals(ServletContext.class))
            return new ServletContextService(sbc);

        if (serviceClass.equals(HttpServletRequest.class))
        {
            HttpRequestService requestService = new HttpRequestService(sbc);
            if (sbc.useWrappers())
                return requestService.getHttpRequestWrapper();
            return requestService;
        }

        if (serviceClass.equals(HttpServletResponse.class))
        {
            HttpResponseService responseService = new HttpResponseService(sbc);
            if (sbc.useWrappers())
                return responseService.getHttpResponseWrapper();
            return responseService;
        }

        if (serviceClass.equals(ServletRequest.class))
        {
            ServletRequestService requestService = new ServletRequestService(sbc);
            if (sbc.useWrappers())
                return requestService.getRequestWrapper();
            return requestService;
        }

        if (serviceClass.equals(ServletResponse.class))
        {
            ServletResponseService responseService = new ServletResponseService(sbc);
            if (sbc.useWrappers())
                return responseService.getResponseWrapper();
            return responseService;
        }

        return null;
    }

    public void releaseService(BeanContextServices bcs, Object requestor, Object service)
    {
        // noop, because the provider isn't tracking service instances.
    }

    public Iterator getCurrentServiceSelectors(BeanContextServices bcs, Class serviceClass)
    {
        return null;    // no selectors
    }
}
