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
package org.apache.beehive.netui.test.pageflow.scoping;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;

public class ScopedRequestTest extends TestCase {

    private HttpServletRequest _fauxRequest = null;
    private ScopedRequest _fauxScopedRequest = null;
    protected String _servletPath = "/somePageFlow/begin.do";
    protected String _scopeId = "_scopeTest";
    protected String _paramNameA = "scopedParamA";
    protected String _paramValueA = "foo";
    protected String _paramNameB = "scopedParamB";
    protected String _paramValueB = "bar";
    protected String _outerParamName = "outerParam";
    protected String _outerParamValue = "foobar";
    protected String _query = _outerParamName + "=" + _outerParamValue + "&"
                              + _scopeId + _paramNameA + "=" + _paramValueA
                              + "&" + _scopeId + _paramNameB + "=" + _paramValueB;
    protected String _attrNameA = "scopedAttrA";
    protected String _attrValueA = "scopedAttrValueA";
    protected String _outerAttrName = "outerAttr";
    protected String _outerAttrValue = "outerAttrValue";

    public ScopedRequestTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ScopedRequestTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public HttpServletRequest getRequest() {
        return _fauxRequest;
    }

    public ScopedRequest getScopedRequest() {
        return _fauxScopedRequest;
    }

    protected void setUp() {
        _fauxRequest = ServletFactory.getServletRequest(_query);
        _fauxRequest.setAttribute(_outerAttrName, _outerAttrValue);

        String requestUri = _fauxRequest.getContextPath() + _servletPath;
        boolean seeOuterRequestAttributes = false;
        _fauxScopedRequest = ScopedServletUtils.getScopedRequest(_fauxRequest, requestUri, null, _scopeId, seeOuterRequestAttributes);
        _fauxScopedRequest.setAttribute(_attrNameA, _attrValueA);
    }

    protected void tearDown() {
    }

    public void testScopedRequest() {
        ScopedRequest scopedRequest = getScopedRequest();
        assertEquals(getRequest(), scopedRequest.getOuterRequest());

        assertEquals(getRequest().getContextPath(), scopedRequest.getContextPath());
        String requestUri = getRequest().getContextPath() + _servletPath;
        assertEquals(requestUri, scopedRequest.getRequestURI());
        assertEquals(_servletPath, scopedRequest.getServletPath());
        assertEquals(_query, scopedRequest.getQueryString());

        String newServletPath = "/someOtherPageFlow/begin.do";
        requestUri = getRequest().getContextPath() + newServletPath;
        scopedRequest.setRequestURI(requestUri);
        assertEquals(requestUri, scopedRequest.getRequestURI());
        assertEquals(newServletPath, scopedRequest.getServletPath());
        assertEquals(_query, scopedRequest.getQueryString());
    }

    public void testScopedRequestScopeKey() {
        ScopedRequest scopedRequest = getScopedRequest();
        assertEquals(_scopeId, scopedRequest.getScopeKey());
        String name = "MyName";
        assertEquals(_scopeId + name, scopedRequest.getScopedName(name));

        String newScopeId = "newScopeId";
        scopedRequest.renameScope(newScopeId);
        assertEquals(newScopeId, scopedRequest.getScopeKey());
        assertEquals(newScopeId + name, scopedRequest.getScopedName(name));
    }

    public void testScopedRequestParameters() {
        HttpServletRequest request = getRequest();
        assertEquals(_outerParamValue, request.getParameter(_outerParamName));
        assertNull(request.getParameter(_paramNameA));
        assertNull(request.getParameter(_paramNameB));

        ScopedRequest scopedRequest = getScopedRequest();
        assertNull(scopedRequest.getParameter(_outerParamName));
        assertEquals(_paramValueA, scopedRequest.getParameter(_paramNameA));
        assertEquals(_paramValueB, scopedRequest.getParameter(_paramNameB));

        // the setUp() should only give us two scoped params
        Enumeration names = scopedRequest.getParameterNames();
        assertNotNull("The scoped request parameter names was returned as null", names);
        int count = 0;
        if (names != null) {
            while (names.hasMoreElements()) {
                count++;
                String name = (String) names.nextElement();
                String[] values = scopedRequest.getParameterValues(name);
                assertNotNull("The scoped request parameter values returned null", values);
                if (values != null && values.length > 0) {
                    if (name.equals(_paramNameA)) {
                        assertEquals(_paramValueA, values[0]);
                    }
                    else if (name.equals(_paramNameB)) {
                        assertEquals(_paramValueB, values[0]);
                    }
                    else {
                        fail("Incorrect parameter value returned");
                    }
                }
                else {
                     fail("the test should return parameter values");
                }
            }
        }
        assertEquals(2, count);
    }

    public void testScopedRequestAttributes() {
        HttpServletRequest request = getRequest();
        assertEquals(_outerAttrValue, request.getAttribute(_outerAttrName));
        assertNull(request.getAttribute(_attrNameA));

        ScopedRequest scopedRequest = getScopedRequest();
        assertNull(scopedRequest.getAttribute(_outerAttrName));
        assertEquals(_attrValueA, scopedRequest.getAttribute(_attrNameA));

        // the setUp() should only give us one scoped attribute
        java.util.Enumeration names = scopedRequest.getAttributeNames();
        assertNotNull("The scoped request attribute names was returned as null", names);
        int count = 0;
        if (names != null) {
            while (names.hasMoreElements()) {
                count++;
                String name = (String) names.nextElement();
                Object value = scopedRequest.getAttribute(name);
                assertNotNull("The scoped request attribute value returned null", value);
                if (name.equals(_attrNameA)) {
                    assertEquals(_attrValueA, value);
                }
                else {
                    fail("Incorrect attribute value returned");
                }
            }
        }
        assertEquals(1, count);

        String attrName = "myReqAttr";
        Object attrValue = new StringBuffer("Attribute Value Object");
        request.setAttribute(attrName, attrValue);
        String scopedAttrName = "myScopedReqAttr";
        Object scopedAttrValue = new StringBuffer("Scoped Attr Value Object");
        scopedRequest.setAttribute(scopedAttrName, scopedAttrValue);
        assertEquals(attrValue, request.getAttribute(attrName));
        assertNull(request.getAttribute(scopedAttrName));
        assertNull(scopedRequest.getAttribute(attrName));
        assertEquals(scopedAttrValue, scopedRequest.getAttribute(scopedAttrName));

        scopedRequest.removeAttribute(attrName);
        assertEquals(attrValue, request.getAttribute(attrName));
        scopedRequest.removeAttribute(scopedAttrName);
        assertNull(scopedRequest.getAttribute(scopedAttrName));

        scopedRequest.registerOuterAttribute(attrName);
        assertEquals(attrValue, scopedRequest.getAttribute(attrName));
    }

    public void testSeeOuterRequestAttributes() {
        HttpServletRequest request = ServletFactory.getServletRequest(_query);
        request.setAttribute(_outerAttrName, _outerAttrValue);
        String requestUri = request.getContextPath() + _servletPath;
        ScopedRequest scopedRequest = ScopedServletUtils.getScopedRequest(request, requestUri, null, _scopeId, true);
        scopedRequest.setAttribute(_attrNameA, _attrValueA);
        assertEquals(_outerAttrValue, request.getAttribute(_outerAttrName));
        assertNull(request.getAttribute(_attrNameA));
        assertEquals(_outerAttrValue, scopedRequest.getAttribute(_outerAttrName));
        assertEquals(_attrValueA, scopedRequest.getAttribute(_attrNameA));
    }
}
