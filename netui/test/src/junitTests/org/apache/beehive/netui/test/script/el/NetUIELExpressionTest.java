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
package org.apache.beehive.netui.test.script.el;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.script.ExpressionTest;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;

/**
 *
 */
public class NetUIELExpressionTest
    extends ExpressionTest {

    protected ExpressionEvaluator getExpressionEvaluator() {
        return ExpressionEvaluatorFactory.getInstance("netuiel");
    }

    public NetUIELExpressionTest(String name) {
        super(name);
    }

    public void testNetUIELContainsExpression() {
        try {
            ExpressionEvaluator ee = getExpressionEvaluator();
            System.out.println("contains expression: " + ee.containsExpression("{pageFlow.foo"));
        } catch(Exception e) {
            System.out.println("Caught exception doing containsExpression: " + e);
            e.printStackTrace();
            throw new RuntimeException("The containsExpression call threw an exception when it shouldn't have", e);
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(new NetUIELExpressionTest("testStrangeCharacters"));
        suite.addTest(new NetUIELExpressionTest("testUnderbarStringRead"));

        suite.addTest(new NetUIELExpressionTest("testStringRead"));
        suite.addTest(new NetUIELExpressionTest("testPageContext"));
        suite.addTest(new NetUIELExpressionTest("testIsContainsExpression"));
        suite.addTest(new NetUIELExpressionTest("testNullProperty"));
        suite.addTest(new NetUIELExpressionTest("testNonexistentPropertyGet"));
        suite.addTest(new NetUIELExpressionTest("test15430"));
        suite.addTest(new NetUIELExpressionTest("testQualify"));
        suite.addTest(new NetUIELExpressionTest("testNetUIELContainsExpression"));
        
        // @bug: broken -- {} isn't valid
        //suite.addTest(new NetUIELExpressionTest("testEmptyReadExpr"));

        // @bug: broken -- does the right thing with String and Character
        //suite.addTest(new NetUIELExpressionTest("testCharProperty"));        

        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
