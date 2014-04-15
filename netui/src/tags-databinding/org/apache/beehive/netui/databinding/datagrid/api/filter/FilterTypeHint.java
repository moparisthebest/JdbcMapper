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
package org.apache.beehive.netui.databinding.datagrid.api.filter;

import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The FilterTypeHint is used by a {@link Filter} object in order to provide metadata about the type of
 * the data in a data set represented by a filter expression.  When used on a {@link Filter}, infrastructure
 * used to build or perform querying using {@link Filter} instances can use type hints in order to build
 * or perform filtering correctly.  For example, when using {@link Filter} instances with SQL, a type
 * hint can be used to know when to quote a value inside of a <i>WHERE</i> clause.
 * </p>
 */
public class FilterTypeHint
    implements java.io.Serializable {

    /**
     * String value for a date type hint.
     */
    private static final String STR_DATE = "DATE";

    /**
     * String value for a string type hint.
     */
    private static final String STR_STRING = "STRING";

    /**
     * String value for a numeric type hint.
     */
    private static final String STR_NUMERIC = "NUMERIC";

    /**
     * Filter type hint representing a date type.
     */
    public static final FilterTypeHint DATE = new FilterTypeHint(STR_DATE);

    /**
     * Filter type hint representing a string type.
     */
    public static final FilterTypeHint STRING = new FilterTypeHint(STR_STRING);

    /**
     * Filter type hint representing a numeric type.
     */
    public static final FilterTypeHint NUMERIC = new FilterTypeHint(STR_NUMERIC);

    private String _hint = null;

    /**
     * Private constructor.
     *
     * @param hint
     */
    private FilterTypeHint(String hint) {
        _hint = hint;
    }

    /**
     * Get the type hint string.
     * @return the type hint string
     */
    public String getHint() {
        return _hint;
    }

    /**
     * Get the default filter type hint.  This is {@link #STRING}.
     * @return the default type hint
     */
    public static FilterTypeHint getDefault() {
        return STRING;
    }

    /**
     * Given a String, lookup a FilterTypeHint for the String.  Valid
     * @param hint the String to use when looking up a filter type hint
     * @return the type hint
     * @throws IllegalArgumentException if the given <code>hint</code> doesn't match a know type hint
     */
    public static FilterTypeHint getTypeHint(String hint) {
        /* todo: this is static; consider providing on a concrete / overridable object */
        if(STRING.getHint().equals(hint))
            return STRING;
        else if(NUMERIC.getHint().equals(hint))
            return NUMERIC;
        else if(DATE.getHint().equals(hint))
            return DATE;
        else throw new IllegalArgumentException(Bundle.getErrorString("FilterTypeHint_UnknownHintString",
                                                                      new Object[] {hint}));
    }
}
