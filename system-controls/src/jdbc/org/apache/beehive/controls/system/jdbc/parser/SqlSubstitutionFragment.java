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
import org.apache.beehive.controls.system.jdbc.TypeMappingsFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a fragement from the SQL annotation's statement member which begins with '{sql:'.
 * Substitution fragements are unique in that they are fully evaluated BEFORE a PreparedStatement
 * is generated.
 * <p/>
 * Supported 'sql:' escapes are subst and fn. subst is the default mode, and will be used if 'sql: '
 * is specified.
 *
 * The <tt>fn</tt> variant of this construct has a very ridgid syntax at this point.  It must conform to:
 *
 * <pre>
 *   {sql:fn in(x,{y})}
 * </pre>
 *
 * where the '{y}' could also be some literal term.
 */
public final class SqlSubstitutionFragment extends SqlFragmentContainer {

    private boolean _hasParamValue = false;

    /**
     * Constructor for subst or function with no param substitution
     *
     * @param child An child which is contained in this fragment.
     */
    protected SqlSubstitutionFragment(SqlFragment child) {
        super();
        addChild(child);
    }

    /**
     * Constructor for a function which includes a ReflectionFragment
     *
     * @param lf A LiteralFragment which contains the text up to the parameter substitution.
     * @param rf The ReflectionFragment containing the parameter substitution
     * @param lff A LiteralFragment which contains any text which occures after the parameter substitution.
     */
    protected SqlSubstitutionFragment(LiteralFragment lf, ReflectionFragment rf, LiteralFragment lff) {
        super();
        addChild(lf);
        addChild(rf);
        addChild(lff);
    }

    /**
     * Always true for this fragment type
     * @return true
     */
    protected boolean isDynamicFragment() { return true; }

    /**
     * Will be true for this fragment type only if one of its children contains
     * a complex sql fragment.
     * @return true if there are param values which need to be retrieved.
     */
    protected boolean hasParamValue() {
        return _hasParamValue;
    }

    /**
     * Get the parameter values from this fragment and its children.  An SqlSubstitutionFragment
     * only contains parameters if one of its children has a complex value type.
     *
     * @param context A ControlBeanContext instance
     * @param m The annotated method
     * @param args The method parameters
     * @return Array of objects.
     */
    protected Object[] getParameterValues(ControlBeanContext context, Method m, Object[] args) {
        ArrayList<Object> paramValues = new ArrayList<Object>();
        for (SqlFragment frag : _children) {
            if (frag.hasComplexValue(context, m, args)) {
                paramValues.addAll(Arrays.asList(frag.getParameterValues(context, m, args)));
            }
        }
        return paramValues.toArray();
    }

    /**
     * Return the text for a PreparedStatement from this fragment. This type of fragment
     * typically evaluates any reflection parameters at this point.  The exception
     * is if the reflected result is a ComplexSqlFragment, it that case the sql text
     * is retrieved from the fragment in this method.  The parameter values are
     * retrieved in the getParameterValues method of this class.
     *
     * @param context A ControlBeanContext instance
     * @param m The annotated method
     * @param args The method parameters
     * @return A String containing the value of this fragment and its children
     */
    protected String getPreparedStatementText(ControlBeanContext context, Method m, Object[] args) {

        StringBuilder sb = new StringBuilder();
        for (SqlFragment frag : _children) {

            boolean complexFragment = frag.hasComplexValue(context, m, args);
            if (frag.hasParamValue() && !complexFragment) {
                Object[] pValues = frag.getParameterValues(context, m, args);
                for (Object o : pValues) {
                    sb.append(processSqlParams(o));
                }
            } else {
                _hasParamValue |= complexFragment;
                sb.append(frag.getPreparedStatementText(context, m, args));
            }
        }
        return sb.toString();
    }


// ////////////////////////////////////////////// Private Methods //////////////////////////////////////////////


    /**
     * Check for the cases of a null or array type param value. If array type build a string of the array values
     * seperated by commas.
     *
     * @param value
     * @return String containing value.
     */
    private String processSqlParams(Object value) {

        Object[] arr = null;
        if (value != null) {
            arr = TypeMappingsFactory.toObjectArray(value);
        }

        if (value == null || (arr != null && arr.length == 0)) {
            return "";
        } else if (arr != null) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                if (i > 0) {
                    result.append(',');
                    result.append(arr[i].toString());
                } else {
                    result.append(arr[i].toString());
                }
            }
            return result.toString();
        } else {
            return value.toString();
        }
    }
}
