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

import java.lang.reflect.Proxy;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.struts.taglib.html.Constants;

import org.apache.beehive.netui.pageflow.ProcessPopulate;
import org.apache.beehive.netui.pageflow.RequestParameterHandler;
import org.apache.beehive.netui.test.servlet.HttpServletRequestHandler;
import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.test.beans.SimpleTypeActionForm;

/**
 *
 */
public class ProcessPopulateTest
    extends TestCase {

    private ServletRequest _fauxRequest = null;
    private ServletResponse _fauxResponse = null;
    private SimpleTypeActionForm _simpleActionForm = null;

    public static class HelloWorldPrefixHandler
        implements RequestParameterHandler {

        public void process(HttpServletRequest request, String key, String expr, ProcessPopulate.ExpressionUpdateNode node) {
            String[] ary = {"hello world!"};
            node.values = ary;
        }
    }

    public void testParameterHandling()
        throws Exception {
        ProcessPopulate.registerPrefixHandler("foo", new HelloWorldPrefixHandler());

        HttpServletRequestHandler reqHandler = (HttpServletRequestHandler)Proxy.getInvocationHandler(_fauxRequest);
        reqHandler.addParam("wlw-foo:{actionForm.stringProperty}", "some new value");

        // wlw-foo:{request.simple.textProperty} -> request.simple.textProperty="some value"
        String expression = "{actionForm.stringProperty}";
        String hExpr = ProcessPopulate.writeHandlerName("foo", expression);
        ProcessPopulate.populate((HttpServletRequest)_fauxRequest, (HttpServletResponse)_fauxResponse, _simpleActionForm, false);

        assertEquals("hello world!", ((SimpleTypeActionForm)_fauxRequest.getAttribute("simpleBean")).getStringProperty());
    }

    protected void setUp() {
        _simpleActionForm = new SimpleTypeActionForm();

        _fauxRequest = ServletFactory.getServletRequest();
        _fauxRequest.setAttribute("simpleBean", _simpleActionForm);
        _fauxRequest.setAttribute(Constants.BEAN_KEY, _fauxRequest.getAttribute("simpleBean"));

        _fauxResponse = ServletFactory.getServletResponse();
    }

    public ProcessPopulateTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ProcessPopulateTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
