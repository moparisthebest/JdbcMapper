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
package org.apache.beehive.netui.test.script;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.VariableResolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.struts.action.ActionForm;
import org.apache.struts.taglib.html.Constants;

import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.test.beans.SimpleTypeActionForm;
import org.apache.beehive.netui.test.beans.ComplexTypeActionForm;
import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.test.util.config.TestConfigUtil;

public abstract class AbstractExpressionTest
    extends TestCase {

    protected static final int COMPLEX_FORM = 1;
    protected static final int SIMPLE_FORM = 2;

    private PageContext _fauxPageContext = null;
    private ServletRequest _fauxRequest = null;
    private ServletResponse _fauxResponse = null;
    private SimpleTypeActionForm _simpleActionForm = null;
    private ComplexTypeActionForm _complexActionForm = null;

    public AbstractExpressionTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(AbstractExpressionTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected PageContext getPageContext() {
        return _fauxPageContext;
    }

    public ServletRequest getRequest() {
        return _fauxRequest;
    }

    public ServletResponse getResponse() {
        return _fauxResponse;
    }

    protected abstract ExpressionEvaluator getExpressionEvaluator();

    protected final Object evaluateExpression(String expression, PageContext pc)
        throws Exception {
        return getExpressionEvaluator().evaluateStrict(expression, ImplicitObjectUtil.getReadVariableResolver(pc));
    }

    protected final void evaluateUpdateExpression(String expression, Object value, ServletRequest request, ServletResponse response, ActionForm form)
        throws Exception {
        evaluateUpdateExpression(expression, value, request, response, form, true);
    }

    protected final void evaluateUpdateExpression(String expression, Object value, ServletRequest request, ServletResponse response, ActionForm form, boolean requestParameter)
        throws Exception {
        Object theForm = form;
        if(theForm == null)
            theForm = request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY);

        VariableResolver vr = ImplicitObjectUtil.getUpdateVariableResolver(theForm, request, response, requestParameter);

        getExpressionEvaluator().update(expression, value, vr, requestParameter);
    }

    protected void setUp() {
        _fauxPageContext = ServletFactory.getPageContext();
        _fauxRequest = getPageContext().getRequest();
        _fauxResponse = getPageContext().getResponse();

        _simpleActionForm = new SimpleTypeActionForm();
        _complexActionForm = new ComplexTypeActionForm();

        getPageContext().getRequest().setAttribute("simpleBean", _simpleActionForm);
        getPageContext().getRequest().setAttribute("complexBean", _complexActionForm);

        useForm(SIMPLE_FORM);

        TestConfigUtil.testInit();
    }

    protected void tearDown() {
        _fauxPageContext = null;
        _fauxRequest = null;
        _fauxResponse = null;
    }

    protected void useForm(int formId) {
        Object form = null;
        HttpServletRequest request = (HttpServletRequest)getPageContext().getRequest();
        if(formId == SIMPLE_FORM)
            form = request.getAttribute("simpleBean");
        else if(formId == COMPLEX_FORM)
            form = request.getAttribute("complexBean");

        useForm(form);
    }

    protected void useForm(Object actionForm) {
        getPageContext().getRequest().setAttribute(Constants.BEAN_KEY, actionForm);
        getPageContext().setAttribute("actionForm", actionForm);
    }

    protected ActionForm getActionForm() {
        HttpServletRequest request = (HttpServletRequest)getPageContext().getRequest();
        return (ActionForm)request.getAttribute(Constants.BEAN_KEY);
    }
}
