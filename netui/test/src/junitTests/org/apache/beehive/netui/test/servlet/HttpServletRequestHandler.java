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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 */
public class HttpServletRequestHandler
    extends ServletHandler {

    /*
     * this is a java.util.Hashtable because the HttpServletRequest returns an Enumeration, 
     * and it's the only Map implementation that returns an Enumeration.  Fun, huh?
     */
    private Hashtable _params = null;

    private String _queryString = null;
    private HttpSession _session = null;

    public HttpServletRequestHandler() {
        super();
        _params = new Hashtable();
    }

    public void setSession(HttpSession session) {
        _session = session;
    }

    public void addParam(String key, Object value) {
        List list = (List)_params.get(key);
        if(list == null) {
            list = new ArrayList();
            list.add(value);
            _params.put(key, list);
        } else
            list.add(value);
    }

    public void setQueryString(String queryString) {
        _queryString = queryString;
        _params = null;

        if(queryString == null)
            return;

        if(queryString.startsWith("?"))
            queryString = queryString.substring(1);

        _params = new Hashtable();

        String[] queryParams = queryString.split("&");
        for(int i = 0; i < queryParams.length; i++) {
            String[] keyValue = queryParams[i].split("=");

            String key = keyValue[0];
            String value = null;

            if(keyValue.length == 1 || keyValue[1] == null)
                value = "";
            else
                value = keyValue[1];

            if(_params.containsKey(keyValue[0])) {
                ((List)_params.get(key)).add(value);
            } else {
                List list = new ArrayList();
                list.add(value);

                _params.put(key, list);
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        if(method.getName().equals("getParameter")) {
            String key = (String)args[0];

            Object o = _params.get(key);

            if(o == null)
                return null;
            else if(o instanceof List) {
                return ((List)o).get(0);
            } else {
                assert (o instanceof String[]);

                return ((String[])o)[0];
            }
        } else if(method.getName().equals("getParameterValues")) {
            String key = (String)args[0];

            List list = (List)_params.get(key);
            return listToStrAry(list);
        } else if(method.getName().equals("getParameterNames")) {
            return _params.keys();
        } else if(method.getName().equals("getQueryString"))
            return _queryString;
        else if(method.getName().equals("getParameter"))
            return _params.get(args[0]);
        else if(method.getName().equals("getSession"))
            return _session;
        else if(method.getName().equals("getContentType"))
            return null;
        else if(method.getName().equals("getCharacterEncoding"))
            return "utf-8";
        else if(method.getName().equals("getMethod"))
            return "POST";
        else if(method.getName().equals("getParameterMap")) {
            HashMap map = new HashMap();
            Enumeration e = _params.keys();
            while(e.hasMoreElements()) {
                String key = (String)e.nextElement();
                String[] ary = listToStrAry((List)_params.get(key));

                map.put(key, ary);
            }
            return map;
        } else if(method.getName().equals("equals")) {
            Object eq = args[0];
            if(eq == null)
                return Boolean.FALSE;
            else if(!(eq instanceof HttpServletRequest))
                return Boolean.FALSE;
            // don't laugh; this works in this context
            else if(Proxy.isProxyClass(eq.getClass()) && (System.identityHashCode(this) == System.identityHashCode(Proxy.getInvocationHandler(eq))))
                return Boolean.TRUE;
            else
                return Boolean.FALSE;
        } else if(method.getName().equals("getContextPath"))
            return "/netuiFauxTestWeb";
        else if(method.getName().equals("getServletPath"))
            return "/netuiFauxTestWeb/fauxServlet";
        else if(method.getName().equals("getRequestURI"))
            return "/netuiFauxTestWeb/fauxServlet.jsp";
        else if(method.getName().equals("getLocale"))
            /* default to the US Locale just for simplicity in testing */
            return Locale.US;
        else
            return super.invoke(proxy, method, args);
    }

    private String[] listToStrAry(List list) {
        if(list == null)
            return null;

        String[] ary = new String[list.size()];
        for(int i = 0; i < ary.length; i++) {
            ary[i] = (String)list.get(i);
        }

        return ary;
    }
}
