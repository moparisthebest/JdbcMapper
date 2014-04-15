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
package org.apache.beehive.netui.pageflow.internal;

import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.el.ELException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import org.apache.beehive.netui.util.logging.Logger;
import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.el.EnumeratedMap;

/**
 * Internal class used to evaluate simple action expressions.
 * 
 * todo: need to merge this down into the expression language registration infrastructure
 * todo: need to provdie an ImplicitObjectFactory that will create Maps for requestScope, sessionScope, etc.
 */
public class InternalExpressionUtils
{
    private static final Logger _logger = Logger.getInstance(InternalExpressionUtils.class);

    public static final boolean evaluateCondition(String expression,
                                                  Object actionForm,
                                                  HttpServletRequest request, 
                                                  ServletContext servletContext)
        throws ELException
    {
        return ((Boolean)evaluate(expression, Boolean.class, actionForm, request, servletContext)).booleanValue();
    }

    public static final String evaluateMessage(String expression, Object actionForm, HttpServletRequest request, 
                                               ServletContext servletContext)
        throws ELException
    {
        return (String)evaluate(expression, String.class, actionForm, request, servletContext);
    }

    /* do not construct */
    private InternalExpressionUtils() {}

    private static final Object evaluate(String expression, Class expectedType, Object actionForm, HttpServletRequest request, ServletContext servletContext)
        throws ELException
    {
        // todo: can this be static / final?
        ExpressionEvaluator ee = getExpressionEvaluator();
        return ee.evaluate(expression, expectedType, getVariableResolver(actionForm, request, servletContext), null);
    }

    private static final ExpressionEvaluator getExpressionEvaluator()
    {
        return new ExpressionEvaluatorImpl();
    }

    private static final VariableResolver getVariableResolver(Object actionForm, HttpServletRequest request, ServletContext servletContext)
    {
        return new SimpleActionVariableResolver(actionForm, request, servletContext);
    }

    private static class SimpleActionVariableResolver
        implements VariableResolver
    {
        private Object _actionForm = null;
        private HttpServletRequest _request = null;
        private ServletContext _servletContext = null;

        private SimpleActionVariableResolver(Object actionForm, HttpServletRequest request, ServletContext servletContext)
        {
            _actionForm = actionForm;
            _request = request;
            _servletContext = servletContext;
        }

        public Object resolveVariable(String name)
        {
            // requestScope, sessionScope, applicationScope, param, paramValues, header, headerValues, cookie, initParam, <default>
            if(name.equals("actionForm"))
                return _actionForm;
            else if(name.equals("requestScope"))
                return buildRequestScopeMap(_request);
            else if(name.equals("sessionScope"))
                return buildSessionScopeMap(_request);
            else if(name.equals("applicationScope"))
                return buildServletContextMap(_servletContext);
            else if(name.equals("param"))
                return buildParamMap(_request);
            else if(name.equals("paramValues"))
                return buildParamsMap(_request);
            else if(name.equals("header"))
                return buildHeaderMap(_request);
            else if(name.equals("headerValues"))
                return buildHeadersMap(_request);
            else if(name.equals("cookie"))
                return buildCookieMap(_request);
            else if(name.equals("initParam"))
                return buildInitParamMap(_servletContext);
            // chain up the request > session (if exists) > application
            // note, this should handle pageFlow, globalApp, sharedFlow, and bundle if they're in the request
            // attribute map already
            else if(_request.getAttribute(name) != null)
                return _request.getAttribute(name);
            else if(_request.getSession(false) != null && _request.getSession(false).getAttribute(name) != null)
                return _request.getSession(false).getAttribute(name);
            else return _servletContext.getAttribute(name);
        }

        private static final Map buildCookieMap(HttpServletRequest httpServletRequest)
        {
            HttpServletRequest servletRequest = httpServletRequest;
            Map/*<String, Cookie>*/ cookieMap = new HashMap/*<String, Cookie>*/();
            Cookie[] cookies = servletRequest.getCookies();
            for(int i = 0; i < cookies.length; i++)
            {
                if(!cookieMap.containsKey(cookies[i].getName()))
                    cookieMap.put(cookies[i].getName(), cookies[i]);
            }
            return cookieMap;
        }

        private static final Map buildHeadersMap(HttpServletRequest httpServletRequest)
        {
            final HttpServletRequest _servletRequest = httpServletRequest;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletRequest.getHeaderNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletRequest.getHeaders((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildHeaderMap(HttpServletRequest httpServletRequest)
        {
            final HttpServletRequest _servletRequest = httpServletRequest;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletRequest.getHeaderNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletRequest.getHeader((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildInitParamMap(ServletContext servletContext)
        {
            final ServletContext _servletContext = servletContext;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletContext.getInitParameterNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletContext.getInitParameter((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildParamsMap(HttpServletRequest servletRequest)
        {
            final HttpServletRequest _servletRequest = servletRequest;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletRequest.getParameterNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletRequest.getParameterValues((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildParamMap(HttpServletRequest servletRequest)
        {
            final HttpServletRequest _servletRequest = servletRequest;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletRequest.getParameterNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletRequest.getParameter((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildRequestScopeMap(HttpServletRequest servletRequest)
        {
            final HttpServletRequest _servletRequest = servletRequest;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletRequest.getAttributeNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletRequest.getAttribute((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildSessionScopeMap(HttpServletRequest servletRequest)
        {
            if(servletRequest.getSession(false) == null)
                return null;

            final HttpSession _session = servletRequest.getSession(false);
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _session.getAttributeNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _session.getAttribute((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }

        private static final Map buildServletContextMap(ServletContext servletContext)
        {
            final ServletContext _servletContext = servletContext;
            return new EnumeratedMap()
            {
                public Enumeration enumerateKeys()
                {
                    return _servletContext.getAttributeNames();
                }

                public Object getValue(Object key)
                {
                    return (key instanceof String ? _servletContext.getAttribute((String)key) : null);
                }

                public boolean isMutable() {return false;}
            };
        }
    }
}
