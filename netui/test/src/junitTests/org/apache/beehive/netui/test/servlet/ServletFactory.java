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
package org.apache.beehive.netui.test.servlet;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 *
 */
public class ServletFactory {

    public static HttpSession getSession() {
        return (HttpSession)create(HttpSession.class);
    }

    public static HttpServletRequest getServletRequest() {
        ServletHandler handler = new HttpServletRequestHandler();
        return (HttpServletRequest)Proxy.newProxyInstance(handler.getClass().getClassLoader(),
            new Class[]{HttpServletRequest.class},
            handler);
    }

    public static HttpServletRequest getServletRequest(String query) {
        HttpServletRequestHandler handler = new HttpServletRequestHandler();
        handler.setQueryString(query);

        return (HttpServletRequest)Proxy.newProxyInstance(handler.getClass().getClassLoader(),
            new Class[]{HttpServletRequest.class},
            handler);
    }

    public static HttpServletResponse getServletResponse() {
        InvocationHandler handler = new HttpServletResponseHandler();
        return (HttpServletResponse)Proxy.newProxyInstance(handler.getClass().getClassLoader(),
            new Class[]{HttpServletResponse.class},
            handler);
    }

    public static ServletContext getServletContext() {
        return (ServletContext)create(ServletContext.class);
    }

    public static PageContext getPageContext(HttpServletRequest request, HttpServletResponse response,
                                             HttpSession session, ServletContext application) {
        return new FauxPageContext(request, response, session, application);
    }

    public static PageContext getPageContext() {
        HttpServletRequest request = ServletFactory.getServletRequest();
        HttpServletResponse response = ServletFactory.getServletResponse();
        HttpSession session = ServletFactory.getSession();
        ServletContext application = ServletFactory.getServletContext();

        HttpServletRequestHandler h = (HttpServletRequestHandler)Proxy.getInvocationHandler(request);
        h.setSession(session);

        PageContext pageContext = getPageContext(request, response, session, application);

        return pageContext;
    }

    private static Object create(Class type) {
        ServletHandler handler = new ServletHandler();
        return Proxy.newProxyInstance(type.getClassLoader(),
            new Class[]{type},
            handler);
    }
}
