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
package org.apache.beehive.netui.script;

import java.util.List;

/**
 * This class implements support for an Expression object.
 * It provides access to metadata about the parsed expression
 * including the data binding context and tokens that constitute
 * the expression.
 */
public abstract class Expression {

    /**
     * Get the expression's data binding context.
     * @return the implicit object that this expression references
     */
    public abstract String getContext();

    /**
     * Get the expression's token list.  For an expression that looks like
     * "actionForm.customer.name", this will include the tokens "actionForm",
     * "customer", and "name".
     * @return the list of tokens contained in the expression
     */
    public abstract List getTokens();

    /**
     * Return an expression that is created starting with the
     * token at the given index.
     *
     * @param start the token index at which to build the sub-expression
     * @return a sub-expression starting with the token referenced by <code>start</code>
     * @throws java.lang.IllegalStateException if the provided start token is out of bounds
     *                given the number of tokens in the expression.
     */
    public abstract String getExpression(int start);
}
