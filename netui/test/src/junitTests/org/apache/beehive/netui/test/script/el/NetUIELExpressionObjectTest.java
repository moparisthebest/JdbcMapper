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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.script.Expression;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.el.ExpressionEvaluatorImpl;

public class NetUIELExpressionObjectTest
    extends TestCase {

    public void testSimple() {
        ExpressionEvaluator ee = createExpressionEvaluator();

        Expression expr = ee.parseExpression("{actionForm.customers[42].orders[12].lineItems[3].name}");

        List tokens = expr.getTokens();
        for(int i = 0; i < tokens.size(); i++) {
            System.out.println("tokens[" + i + "]: " + tokens.get(i));
        }

        expr = ee.parseExpression("{container.item.foo.bar.blee}");
        System.out.println("expression from index 2: " + expr.getExpression(2));
    }

    private static final ExpressionEvaluator createExpressionEvaluator() {
        return new ExpressionEvaluatorImpl();
    }

    public NetUIELExpressionObjectTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(NetUIELExpressionObjectTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

}


