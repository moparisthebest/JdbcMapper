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
package org.apache.beehive.netui.pageflow.interceptor.request;

import org.apache.beehive.netui.pageflow.interceptor.InterceptorContext;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.RequestInterceptorsConfig;
import org.apache.beehive.netui.util.config.bean.InterceptorConfig;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Context passed to methods on {@link RequestInterceptor}.
 */
public class RequestInterceptorContext
        extends InterceptorContext
{
    private static final String INTERCEPTORS_LIST_ATTR = InternalConstants.ATTR_PREFIX + "requestInterceptors";

    private transient ServletContext _servletContext;
    private transient HttpServletRequest _request;
    private transient HttpServletResponse _response;

    public RequestInterceptorContext( HttpServletRequest request, HttpServletResponse response,
                                      ServletContext servletContext )
    {
        _request = request;
        _response = response;
        _servletContext = servletContext;
    }

    public void cancelRequest( RequestInterceptor interceptor )
    {
        setResultOverride( null, interceptor );
    }

    public boolean requestWasCancelled()
    {
        return hasResultOverride() && getResultOverride() == null;
    }

    public HttpServletRequest getRequest()
    {
        return _request;
    }

    public HttpServletResponse getResponse()
    {
        return _response;
    }

    public ServletContext getServletContext()
    {
        return _servletContext;
    }

    public static void init( ServletContext servletContext )
    {
        RequestInterceptorsConfig requestInterceptors = ConfigUtil.getConfig().getRequestInterceptors();

        if ( requestInterceptors != null )
        {
            InterceptorConfig[] globalRequestInterceptors = requestInterceptors.getGlobalRequestInterceptors();

            if ( globalRequestInterceptors != null )
            {
                ArrayList/*< Interceptor >*/ interceptorsList = new ArrayList/*< Interceptor >*/();
                addInterceptors( globalRequestInterceptors, interceptorsList, RequestInterceptor.class );
                servletContext.setAttribute( INTERCEPTORS_LIST_ATTR, interceptorsList );
            }
        }
    }
    
    public List/*< Interceptor >*/ getRequestInterceptors()
    {
        return ( List/*< Interceptor >*/ ) getServletContext().getAttribute( INTERCEPTORS_LIST_ATTR );
    }
    
    public static void addInterceptor( ServletContext servletContext, RequestInterceptor interceptor )
    {
        List/*< Interceptor >*/ interceptorsList =
                ( List/*< Interceptor >*/ ) servletContext.getAttribute( INTERCEPTORS_LIST_ATTR );
        if ( interceptorsList == null )
            interceptorsList = new ArrayList/*< Interceptor >*/();
        
        interceptorsList.add( interceptor );
        servletContext.setAttribute( INTERCEPTORS_LIST_ATTR, interceptorsList );
    }
}
