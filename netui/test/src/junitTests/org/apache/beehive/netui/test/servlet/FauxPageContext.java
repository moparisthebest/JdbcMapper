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

import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.taglibs.standard.lang.jstl.JSTLVariableResolver;

/**
 *
 */
public class FauxPageContext
    extends PageContext {

    private Hashtable _attributes = null;
    private HttpServletRequest _request = null;
    private HttpServletResponse _response = null;
    private HttpSession _session = null;
    private ServletContext _servletContext = null;

    public FauxPageContext(HttpServletRequest request,
                           HttpServletResponse response,
                           HttpSession session,
                           ServletContext servletContext) {
        super();

        _request = request;
        _response = response;
        _session = session;
        _servletContext = servletContext;
        _attributes = new Hashtable();
    }

    public Object findAttribute(String name) {
        Object ret = null;
        ret = getAttribute(name);

        if(ret == null) {
            ret = getAttribute(name, REQUEST_SCOPE);
            if(ret == null)
                ret = getAttribute(name, SESSION_SCOPE);
            if(ret == null)
                ret = getAttribute(name, APPLICATION_SCOPE);
        }

        return ret;
    }

    public Object getAttribute(String name) {
        return _attributes.get(name);
    }

    public Enumeration getAttributeNamesInScope(int scope) {
        switch(scope) {
            case PAGE_SCOPE:
                return _attributes.keys();
            case REQUEST_SCOPE:
                return _request.getAttributeNames();
            case SESSION_SCOPE:
                return _session.getAttributeNames();
            case APPLICATION_SCOPE:
                return _servletContext.getAttributeNames();
            default:
                throw new IllegalStateException("The scope specified by the index \"" + scope + "\" is not valid.");
        }

    }

    public Object getAttribute(String name, int scope) {
        switch(scope) {
            case PAGE_SCOPE:
                return _attributes.get(name);
            case REQUEST_SCOPE:
                return _request.getAttribute(name);
            case SESSION_SCOPE:
                return _session.getAttribute(name);
            case APPLICATION_SCOPE:
                return _servletContext.getAttribute(name);
            default:
                throw new IllegalStateException("The scope specified by the index \"" + scope + "\" is not valid.");
        }
    }

    public void removeAttribute(String name) {
        _attributes.remove(name);
    }

    public void removeAttribute(String name, int scope) {
        switch(scope) {
            case PAGE_SCOPE:
                _attributes.remove(name);
                return;
            case REQUEST_SCOPE:
                _request.removeAttribute(name);
                return;
            case SESSION_SCOPE:
                _session.removeAttribute(name);
                return;
            case APPLICATION_SCOPE:
                _servletContext.removeAttribute(name);
                return;
            default:
                throw new IllegalStateException("The scope specified by the index \"" + scope + "\" is not valid.");
        }
    }

    public void setAttribute(String name, Object attribute) {
        _attributes.put(name, attribute);
    }

    public void setAttribute(String name, Object o, int scope) {
        switch(scope) {
            case PAGE_SCOPE:
                _attributes.put(name, o);
                return;
            case REQUEST_SCOPE:
                _request.setAttribute(name, o);
                return;
            case SESSION_SCOPE:
                _session.setAttribute(name, o);
                return;
            case APPLICATION_SCOPE:
                _servletContext.setAttribute(name, o);
                return;
            default:
                throw new IllegalStateException("The scope specified by the index \"" + scope + "\" is not valid.");
        }
    }

    public ServletRequest getRequest() {
        return _request;
    }

    public ServletContext getServletContext() {
        return _servletContext;
    }

    public HttpSession getSession() {
        return _session;
    }

    /* ============================================ 
     *
     * PageContext methods
     *
     * ============================================ 
     */
    public void forward(String path) {
    }

    public int getAttributesScope(String name) {
        return -1;
    }

    public Exception getException() {
        return null;
    }

    public JspWriter getOut() {
        return null;
    }

    public Object getPage() {
        return null;
    }

    public ServletResponse getResponse() {
        return _response;
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void handlePageException(Exception e) {
    }

    public void handlePageException(Throwable e) {
    }

    public void include(String path) {
    }

    public void initialize(Servlet servlet, ServletRequest req, ServletResponse resp,
                           String errorPageUrl, boolean needsSession, int bufferSize,
                           boolean autoFlush) {

    }

    public JspWriter popBody() {
        return null;
    }

    public BodyContent pushBody() {
        return null;
    }

    public void release() {
    }

    // Servlet 2.4 method implementation
    public void include(String string, boolean bool) {
    }

    // Servlet 2.4 method implementation
    public VariableResolver getVariableResolver() {
        return new FauxVariableResolver(this);
    }

    // Servlet 2.4 method implementation
    public javax.servlet.jsp.el.ExpressionEvaluator getExpressionEvaluator() {
        throw new UnsupportedOperationException("nyi");
    }

    public static class FauxVariableResolver
        implements VariableResolver {

        private PageContext _pageContext = null;
        private JSTLVariableResolver _vr = null;

        private FauxVariableResolver(PageContext pageContext) {
            _pageContext = pageContext;
            _vr = new JSTLVariableResolver();
        }

        public Object resolveVariable(String name)
            throws ELException {
            try {
                return _vr.resolveVariable(name, _pageContext);
            } catch(org.apache.taglibs.standard.lang.jstl.ELException e) {
                throw new ELException(e);
            }
        }
    }
}
