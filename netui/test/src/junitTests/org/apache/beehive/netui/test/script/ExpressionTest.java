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

import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;
import org.apache.beehive.netui.test.beans.FooJavaBean;
import org.apache.beehive.netui.test.beans.SimpleTypeActionForm;

import org.apache.beehive.netui.util.logging.Logger;

/**
 * Unit tests for NetUI EL expression parsing.
 */
public abstract class ExpressionTest
    extends AbstractExpressionTest {

    private static final Logger _logger = Logger.getInstance(ExpressionTest.class);

    // expression, shouldParse, isExpression, containsExpression, 
    Object[][] isExprData =
        {
            {"{pageScope.foo.bar.baz.property}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[42].property.nextProperty}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"\\{pageScope.foo.bar.baz.property}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz.property\\}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{pageScope.foo.bar.baz['\\\"%$#@!']}", Boolean.FALSE, Boolean.TRUE, Boolean.TRUE},

            {"{pageScope.foo.bar.baz[\"\\\"\\\"\\\"\\\"\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"'\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, // Radar bug: 16763
            {"{pageScope.foo.bar.baz['\"']}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, // Radar bug: 16763
            {"{pageScope.foo.bar.baz[\"%$#@!\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},

            {"{", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "{"},
            {"{{", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "{{"},
            {"\\{", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, "\\{"},
            {"\\}", Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "\\}"},
            {"}", Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "}"},
            {"}}", Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "}}"},
            {"", Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, ""},
            {"{pageScope.bean._Property}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},

            // Radar 30458
            {"{ pageScope.bean}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{pageScope. bean}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{pageScope.bean }", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{pageScope.bean@}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{pageflub.bean@}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{@}", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {"{ }", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},

            // contain expressions, !expressions
            {" {pageScope.foo.bar.baz.property} ", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {" {pageScope.foo.bar.baz.property\\} ", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE},
            {" {actionForm} {pageScope.foo.bar.baz.property\\} {container} ", Boolean.FALSE, null, null},
            {" {actionForm} \\{pageScope.foo.bar.baz.property\\} {container} ", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {" {container}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"{container} ", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"{container}{actionForm} ", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},

            // nested exceptions
            {"{pageScope.foo.bar.baz[{foo.bar.baz.nest}]}", Boolean.FALSE, null, null},
            {"{pageScope.foo.bar.baz[\"{}\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"{\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"}\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"'\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, // FAIL 1/4/2003 -- fixed in AttributeParser
            {"{pageScope.foo.bar.baz[\"\\\"\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, // FAIL 1/4/2003 -- fixed in AttributeParser
            {"{pageScope.foo.bar.baz[\"{foo.bar.baz}\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"\\\\{foo.bar.{baz.nest}}\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.foo.bar.baz[\"\\\\{foo.bar.baz.nest}\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},

            {"this = {pageScope.foo}'", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = {pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = /{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = \\{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = / {pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = \\ {pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = @{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = *{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = ({pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = [{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = [{pageScope.foo}}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = [{pageScope.foo}\"", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"this = [{pageScope.foo}\'", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},
            {"/{pageFlow.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},

            {"{pageScope.foo.bar.baz['\"']}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE}, // FAIL 1/4/2003 -- fixed 05/13-2003
            {"this = '{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, // FAIL 4/21/2003
            {"this = \"{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, // FAIL 4/21/2003
            {"this = \'{pageScope.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, // FAIL 4/21/2003

            {"{pageScope.files[\"/foo\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            {"{pageScope.files[\"{foo\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},

            // ============================
            // Broken test cases
            // ============================

            // @bug: xscript parser -- shouldn't accept {}
            //{"{}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},
            // @bug: netuiel / xscript?  accepted by the latter but not the former -- the string litreal doesn't handle \\{ but \\\\{ works
            //{"{pageScope.foo.bar.baz[\"\\{foo.bar.baz.nest}\"]}", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE},

            // @bug: netuiel parser -- \\{ is broken in el
            {"\\\\{pageFlow.foo}", Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}, // FAIL 4/21/2003 -- fixed 05/13-2003
        };

    public void testIsContainsExpression()
        throws Exception {
        Object[][] data = isExprData;

        ExpressionEvaluator ee = getExpressionEvaluator();
        for(int i = 0; i < data.length; i++) {
            String expr = (String)data[i][0];
            boolean shouldParse = ((Boolean)data[i][1]).booleanValue();
            boolean shouldBeExpr = false;
            boolean shouldContainExpr = false;

            if(shouldParse) {
                shouldBeExpr = ((Boolean)data[i][2]).booleanValue();
                shouldContainExpr = ((Boolean)data[i][3]).booleanValue();
            }

            if(_logger.isDebugEnabled()) _logger.debug("Expression \"" + expr + "\" should parse \"" + shouldParse + "\"");

            try {
                boolean isExpr = ee.isExpression(expr);

                // should this have succeeded
                assertTrue(true == shouldParse);

                // should expr be an expression
                assertTrue(isExpr == shouldBeExpr);

                boolean containsExpr = ee.containsExpression(expr);

                // should expr contain an expression
                assertTrue(containsExpr == shouldContainExpr);
            } catch(Throwable t) {
                if(_logger.isDebugEnabled()) _logger.debug((shouldParse ? "Failure" : "Expected failure") + " on test [" + i + "] with expression \"" + expr + "\"", t);

                // success!
                if(!shouldParse) {
                    if(_logger.isDebugEnabled()) _logger.debug("Caught expected failure for expression \"" + expr + "\"");
                    continue;
                } else
                    throw new ExpressionTestException("An error occurred parsing test [" + i + "] with expression \"" + expr + "\"", t);
            }
        }
    }

    /**
     * Simple sanity check of the FauxPageContext that is used in the rest of these tests
     */
    public void testPageContext() {
        FooJavaBean bean = new FooJavaBean("JunitTestJavaBean");
        getPageContext().setAttribute("foo", bean);
        assertEquals(bean.getTextProperty(), ((FooJavaBean)getPageContext().getAttribute("foo")).getTextProperty());
    }

    /**
     * Test a simple String read
     */
    public void testStringRead()
        throws Exception {
        FooJavaBean bean = new FooJavaBean("JunitTestJavaBean");
        getPageContext().setAttribute("foo", bean);

        Object result = evaluateExpression("{pageScope.foo.textProperty}", getPageContext());
        assertEquals(result, bean.getTextProperty());
    }

    public void testUnderbarStringRead()
        throws Exception {
        Object result = null;

        result = evaluateExpression("{requestScope.simpleBean._StringProperty}", getPageContext());
        assertTrue(result.equals(((SimpleTypeActionForm)getPageContext().getRequest().getAttribute("simpleBean")).get_StringProperty()));
        result = null;

        result = evaluateExpression("{requestScope.simpleBean._stringProperty}", getPageContext());
        assertTrue(result.equals(((SimpleTypeActionForm)getPageContext().getRequest().getAttribute("simpleBean")).get_stringProperty()));
    }

    public void testNullProperty()
        throws Exception {
        Object result = evaluateExpression("{requestScope.simpleBean.nullProperty}", getPageContext());
        assertNull(result);
    }

    public void testCharProperty()
        throws Exception {
        Object result = evaluateExpression("{requestScope.simpleBean.charProperty}", getPageContext());
        if(!(result instanceof String && ((String)result).equals("a")))
            throw new ExpressionTestException("The Character value returned from XScript was not valid.", null);
    }

    public void testEmptyReadExpr()
        throws Exception {
        Object result = evaluateExpression("{}", getPageContext());
        assertNull(result);
    }

    /**
     * This is bad; this test passes w/o exception
     */
    public void testNonexistentPropertyGet()
        throws Exception {
        boolean exception = false;
        try {
            evaluateExpression("{requestScope.simpleBean.neverMoreProperty}", getPageContext());
        } catch(Exception e) {
            exception = true;
        }
        assertTrue(exception);
    }

    // Bug 15430 -- make sure that the bean property naming convention is enforced
    public void test15430()
        throws Exception {
        Object result = null;
        boolean pass = false;

        try {
            result = evaluateExpression("{requestScope.simpleBean.StringProperty}", getPageContext());
        } catch(Exception e) {
            pass = true;
        }
        assertTrue(pass);

        pass = true;
        try {
            result = evaluateExpression("{requestScope.simpleBean.stringProperty}", getPageContext());
        } catch(Exception e) {
            pass = false;
        }
        assertTrue(pass);
    }

    public void testStrangeCharacters()
        throws Exception {
        Object[][] data =
            {
                {"{requestScope.complexBean.map[\" \"]}", "space"},
                {"{requestScope.complexBean.map[\"*\"]}", "asterisk"},
                {"{requestScope.complexBean.map[\"%\"]}", "percent"},
                {"{requestScope.complexBean.map[\"'\"]}", "single quote"},
                {"{requestScope.complexBean.map[\"''\"]}", "double, single quote"},
                {"{requestScope.complexBean.map[' ']}", "space"},
                {"{requestScope.complexBean.map['*']}", "asterisk"},
                {"{requestScope.complexBean.map['%']}", "percent"},

                // @bug: netuiel -- these aren't handled
                {"{requestScope.complexBean.map['\\'']}", "single quote"}, // how do you do this in XScript?
                {"{requestScope.complexBean.map['\\'\\'']}", "double, single quote"},
                {"{requestScope.complexBean.map['\\\"\\\"']}", "double, double quote"},
                {"{requestScope.complexBean.map['\\\"']}", "double quote"}, // Radar bug: 16763 -- XScript doesn't handle this case - 02/03/2003 fixed
                {"{requestScope.complexBean.map[\"\\\"\\\"\"]}", "double, double quote"},
                {"{requestScope.complexBean.map[\"\\\"\"]}", "double quote"}, // Radar bug: 16763 -- XScript doesn't handle this case - 02/03/2003 fixed

                {"{requestScope.complexBean.map[\"\"]}", "empty string"},
                {"{requestScope.complexBean.map['']}", "empty string"},
            };

        int i = 0;
        String expr = null;
        try {
            for(i = 0; i < data.length; i++) {
                expr = (String)data[i][0];
                String value = (String)data[i][1];

                Object result = null;
                result = evaluateExpression(expr, getPageContext());

                assertTrue(value.equals(result));
            }
        } catch(Throwable t) {
            t.printStackTrace();
            throw new ExpressionTestException("An error occurred parsing test [" + i + "] with expression \"" + expr + "\"", t);
        }
    }

    public void testQualify()
        throws Exception {
        String[][] data =
            {
                {"stuff", "actionForm", "{actionForm.stuff}"}
            };

        ExpressionEvaluator ee = ExpressionEvaluatorFactory.getInstance();
        for(int i = 0; i < data.length; i++) {
            String result = ee.qualify(data[i][1], data[i][0]);
            assertEquals(data[i][2], result);
        }
    }

    public ExpressionTest(String name) {
        super(name);
    }
}
