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

import org.apache.beehive.netui.script.Expression;

/**
 * Unit tests for XScript expression parsing.
 */
public abstract class ParsedExpressionTest
    extends AbstractExpressionTest {

    private Object[][] _data =
        {
            {"{pageScope.foo}", "pageScope"},
            {"{pageFlow}", "pageFlow"},
            {"{actionForm[42]}", "actionForm"},
            {"{actionForm[\"42\"]}", "actionForm"},
            {"{actionForm[\"42\"]", null},
            {"nonExpressionText", null},
        };

    public void testParsedExpression() {
        for(int i = 0; i < _data.length; i++) {
            String expr = (String)_data[i][0];
            String context = (String)_data[i][1];

            try {
                Expression act = getExpressionEvaluator().parseExpression(expr);

                assertEquals("ParsedExpression context failed on test case [" + i + "] with expression \"" + expr + "\" context is \"" + context + "\"",
                    context, act.getContext());
            } catch(Throwable t) {
                if(context == null)
                    continue;
                else
                    throw new RuntimeException("ParsedExpression context test failed in test case [" + i + "].  Cause: " + t);
            }
        }
    }

    public ParsedExpressionTest(String name) {
        super(name);
    }

}
