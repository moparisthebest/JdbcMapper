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
import java.util.ArrayList;

/**
 * Represents an SQL escape sequence found in the SQL annotation's statement member. A JdbcFragment may
 * contain child SqlFragments, typically these fragments consist of LiteralFragments and ReflectionFragments.
 * Parameter substitutions may occur within the SQL escape delimiters {}.
 *
 * Syntactically an SQL escape sequence must match one of the following forms, where <i>_space_</i> is a whitespace character:
 *
 * <UL><LI>{call_space_.....}</LI>
 * <LI>{?=_space_.....}</LI>
 * <LI>{d_space_.....}</LI>
 * <LI>{t_space_.....}</LI>
 * <LI>{ts_space_.....}</LI>
 * <LI>{fn_space_.....}</LI>
 * <LI>{escape_space_.....}</LI>
 * <LI>{oj_space_.....}</LI>
 */
public final class JdbcFragment extends SqlFragmentContainer {

    /**
     * Create a new JdbcFragment
     */
    JdbcFragment() {
        super();
    }

    /**
     * Get the prepared statement parameter value(s) contained within this fragment.
     *
     * @param context A ControlBeanContext instance.
     * @param method The annotated method.
     * @param args The method's arguments.
     *
     * @return null if this fragment doesn't contain a parameter value.
     */
    Object[] getParameterValues(ControlBeanContext context, Method method, Object[] args) {

        ArrayList<Object> values = new ArrayList<Object>();
        for (SqlFragment sf : _children) {
            if (sf.hasParamValue()) {
                Object[] moreValues = sf.getParameterValues(context, method, args);
                for (Object o : moreValues) {
                    values.add(o);
                }
            }
        }
        return values.toArray();
    }
}
