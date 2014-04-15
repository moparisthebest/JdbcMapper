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

import org.apache.beehive.netui.test.script.ExpressionUpdateTest;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;

/**
 *
 */
public class NetUIELExpressionUpdateTest
    extends ExpressionUpdateTest {

    protected ExpressionEvaluator getExpressionEvaluator() {
        return ExpressionEvaluatorFactory.getInstance("netuiel");
    }

    public NetUIELExpressionUpdateTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(new NetUIELExpressionUpdateTest("testNonexistentPropertySet"));
        suite.addTest(new NetUIELExpressionUpdateTest("testPrimitiveUpdate"));
        suite.addTest(new NetUIELExpressionUpdateTest("testPrimitiveWrapperUpdate"));
        suite.addTest(new NetUIELExpressionUpdateTest("testArrayUpdate"));
        suite.addTest(new NetUIELExpressionUpdateTest("testComplexUpdateExpressionEvaluation"));
        suite.addTest(new NetUIELExpressionUpdateTest("testSettingComplexTypes"));
        suite.addTest(new NetUIELExpressionUpdateTest("testWritableContexts"));
        suite.addTest(new NetUIELExpressionUpdateTest("testIndexedUpdate"));
        suite.addTest(new NetUIELExpressionUpdateTest("testDirectListUpdate"));
        suite.addTest(new NetUIELExpressionUpdateTest("testB43011"));
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
