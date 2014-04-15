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
package org.apache.beehive.netui.test.script.simpleaction;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.beehive.netui.pageflow.internal.InternalExpressionUtils;
import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.test.beans.SimpleTypeActionForm;
import org.apache.beehive.netui.test.beans.FooJavaBean;

public class InternalExpressionUtilsTest
    extends TestCase {

    private HttpServletRequest _request = null;
    private ServletContext _servletContext = null;
    private SimpleTypeActionForm _actionForm = null;
    private FooJavaBean _beanActionForm = null;

    public InternalExpressionUtilsTest(String name) {
        super(name);
    }

    public void setUp() {
        PageContext _pageContext = ServletFactory.getPageContext();
        _request = (HttpServletRequest)_pageContext.getRequest();
        _servletContext = _pageContext.getServletContext();
        _actionForm = new SimpleTypeActionForm();
        _beanActionForm = new FooJavaBean();
    }

    public void testConditionEvaluation()
        throws ELException {
        boolean result = evaluateCondition("${actionForm.boolProperty}");
        assertFalse(result);

        _actionForm.setBoolProperty(true);
        result = evaluateCondition("${actionForm.boolProperty}");
        assertTrue(result);

        result = evaluateCondition("${requestScope.foo}");
        assertFalse(result);
        result = evaluateCondition("${sessionScope.foo}");
        assertFalse(result);
        result = evaluateCondition("${applicationScope.foo}");
        assertFalse(result);
        result = evaluateCondition("${param.foo}");
        assertFalse(result);
        result = evaluateCondition("${paramValues.foo}");
        assertFalse(result);
/* the faux servlet proxy layer doesn't support these calls right now
        result = evaluateCondition("${header.foo}");
        assertFalse(result);
        result = evaluateCondition("${headerValues.foo}");
        assertFalse(result);
        result = evaluateCondition("${cookie.foo}");
        assertFalse(result);
        result = evaluateCondition("${initParam.foo}");
        assertFalse(result);
*/
        _beanActionForm.setBooleanProperty(true);
        result = InternalExpressionUtils.evaluateCondition("${actionForm.booleanProperty}", _beanActionForm, _request, _servletContext);
        assertTrue(result);
    }

    public void testMessageEvaluation()
        throws ELException {
        String result = evaluateMessage("${actionForm.stringProperty}");
        assertEquals(result, _actionForm.getStringProperty());
    }

    private boolean evaluateCondition(String expression)
        throws ELException {
        return InternalExpressionUtils.evaluateCondition(expression, _actionForm, _request, _servletContext);
    }

    private String evaluateMessage(String expression)
        throws ELException {
        return InternalExpressionUtils.evaluateMessage(expression, _actionForm, _request, _servletContext);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(InternalExpressionUtilsTest.class);
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
