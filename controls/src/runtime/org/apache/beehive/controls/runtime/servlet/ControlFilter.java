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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * The ControlFilter class provides an implementation of an HTTP servlet filter that supports
 * running controls in the web tier.  It works, in conjunction with the {@link ServletBeanContext}
 * class to provide runtime containment for controls.  It ensures that a valid BeanContext has 
 * been set up prior to forwarding the request to the actual target servlet, and does 
 * post-processing to ensure that resources have been properly released.
 *
 * This filter needs to be configured in web.xml for URL mappings that will be hosting servlets.
 */
public class ControlFilter implements Filter
{
    public static String BEAN_CONTEXT_ATTRIBUTE = ServletBeanContext.class.getName();

    /**
     * The contextClass init parameter is a class name that defines the BeanContext class to use 
     * for containing Controls in the servlet container.  This class <b>must be</b> a subclass of 
     * the org.apache.beehive.runtime.servlet.ServletBeanContext class.
     */
    public static String INIT_PARAM_CONTEXT_CLASS = "contextClass";

    public void init(FilterConfig filterConfig) throws ServletException 
    {
        _filterConfig = filterConfig;
        String contextClassName = filterConfig.getInitParameter(INIT_PARAM_CONTEXT_CLASS);
        if(contextClassName != null)
        {
            try
            {
                _contextClass = Class.forName(contextClassName);
            }
            catch(Exception e)
            {
                throw new ServletException("Cannot load container context class '"+contextClassName+"'");
            }
            if(!ServletBeanContext.class.isAssignableFrom(_contextClass))
            {
                throw new ServletException("'"+contextClassName+"' is not a ServletBeanContext sub-class");
            }
            
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws java.io.IOException, ServletException
    {
        if (request instanceof HttpServletRequest)
        {
            ServletBeanContext beanContext;
            try
            {
                beanContext = (ServletBeanContext)_contextClass.newInstance();
            }
            catch(Exception e)
            {
                throw new ServletException("Cannot construct BeanContext class instance: " +
                                           _contextClass.getName());
            }

            //
            // Start a new execution context
            //
            beanContext.beginContext(_filterConfig.getServletContext(), request, response);
            try
            {
                //
                // Pass the requst on to the next filter or target servlet
                //
                chain.doFilter(request, response);
            }
            finally
            {
                //
                // End the execution context
                //
                beanContext.endContext();
            }
        }
        else
        {
            //
            // If the filter is (mis)configured on something other than an http servlet, just
            // pass it on
            //
            chain.doFilter(request, response);
        }
    }

    public void destroy() {}

    /**
     * The FilterConfig associated with this filter instance.
     */
    FilterConfig _filterConfig;

    /**
     * The BeanContext class to use as the container for controls running in the ServletContainer
     */
    private Class _contextClass = ServletBeanContext.class;
}
