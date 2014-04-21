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

package org.apache.beehive.controls.system.jdbc.parser;

import org.apache.beehive.controls.api.context.ControlBeanContext;

import java.lang.reflect.Method;

/**
 * Represents a portion of the SQL annotation's statement member which is not within substitution delimiters.
 * The parser creates LiteralFragements for portions of the SQL statement which do not require any special processing.
 */
public final class LiteralFragment extends SqlFragment {

    private final String _value;

    /**
     * Create an new LiteralFragment with the specified value.
     * @param value Value of this fragment.
     */
    LiteralFragment(String value) {
        _value = value;
    }

    /**
     * Get the text for a PreparedStatement
     * @param context A ControlBeanContext instance
     * @param m The annotated method.
     * @param args The method's parameters.
     * @return A String containing the literal value for this fragment.
     */
    String getPreparedStatementText(ControlBeanContext context, Method m, Object[] args) {
       return _value;
    }

    /**
     * Required for JUnit testing.
     * @return The String value of this fragment.
     */
    public String toString() {
        return _value;
    }
}
