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

/**
 * <p>
 * This class provides a hint for the operation performed by a
 * {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} instance.  The
 * filter operations supported for a specific query language are not encoded in the
 * FilterOperation class and are provided on an per-instance basis.  The hint provides
 * a way of representing a generic type of operation.  The operations here are common
 * across query languages and are not meant to be an exhaustive list.
 * </p>
 * <p>
 * Setting a FilterOperationHint on a Filter object, allows query infrastructure to
 * generically interact with Filter instances regardless of the type of query mechanism.
 * </p>
 */
public class FilterOperationHint
    implements java.io.Serializable {

    /**
     * Int value representing no operation.
     */
    public static final int INT_NONE = 0;

    /**
     * Int value representing equal.
     */
    public static final int INT_EQUAL = 1;

    /**
     * Int value representing not equal.
     */
    public static final int INT_NOT_EQUAL = 2;

    /**
     * Int value representing greater than.
     */
    public static final int INT_GREATER_THAN = 3;

    /**
     * Int value representing less than.
     */
    public static final int INT_LESS_THAN = 4;

    /**
     * Int value representing greater than or equal.
     */
    public static final int INT_GREATER_THAN_OR_EQUAL = 5;

    /**
     * Int value representing less than or equal.
     */
    public static final int INT_LESS_THAN_OR_EQUAL = 6;

    /**
     * Int value representing is one of.
     */
    public static final int INT_IS_ONE_OF = 7;

    /**
     * Int value representing starts with.
     */
    public static final int INT_STARTS_WITH = 8;

    /**
     * Int value representing contains.
     */
    public static final int INT_CONTAINS = 9;

    /**
     * Int value representing is empty.
     */
    public static final int INT_IS_EMPTY = 10;

    /**
     * Int value representing is not empty.
     */
    public static final int INT_IS_NOT_EMPTY = 11;

    /**
     * Operation representing no filter.
     */
    public static final FilterOperationHint NONE = new FilterOperationHint(INT_NONE);

    /**
     * Operation representing equal.
     */
    public static final FilterOperationHint EQUAL = new FilterOperationHint(INT_EQUAL);

    /**
     * Operation representing not equal.
     */
    public static final FilterOperationHint NOT_EQUAL = new FilterOperationHint(INT_NOT_EQUAL);

    /**
     * Operation representing greater than.
     */
    public static final FilterOperationHint GREATER_THAN = new FilterOperationHint(INT_GREATER_THAN);

    /**
     * Operation representing less than.
     */
    public static final FilterOperationHint LESS_THAN = new FilterOperationHint(INT_LESS_THAN);

    /**
     * Operation representing greater than or equal.
     */
    public static final FilterOperationHint GREATER_THAN_OR_EQUAL = new FilterOperationHint(INT_GREATER_THAN_OR_EQUAL);

    /**
     * Operation representing less than or equal.
     */
    public static final FilterOperationHint LESS_THAN_OR_EQUAL = new FilterOperationHint(INT_LESS_THAN_OR_EQUAL);

    /**
     * Operation representing is one of.  The implementations of an 'is one of' operation is left
     * to the interpreter of a Filter instance.
     */
    public static final FilterOperationHint IS_ONE_OF = new FilterOperationHint(INT_IS_ONE_OF);

    /**
     * Operation representing starts with.
     */
    public static final FilterOperationHint STARTS_WITH = new FilterOperationHint(INT_STARTS_WITH);

    /**
     * Operation representing 'contains'.  The implementation of a contains operation is left
     * to the interpreter of a Filter instance.
     */
    public static final FilterOperationHint CONTAINS = new FilterOperationHint(INT_CONTAINS);

    /**
     * Operation representing 'is empty'.
     */
    public static final FilterOperationHint IS_EMPTY = new FilterOperationHint(INT_IS_EMPTY);

    /**
     * Operation representing 'is not empty'.
     */
    public static final FilterOperationHint IS_NOT_EMPTY = new FilterOperationHint(INT_IS_NOT_EMPTY);

    /**
     * Integer representation of the filter operation.
     */
    private int _val;

    /**
     * Private constructor.
     * @param val the filter value
     */
    private FilterOperationHint(int val) {
        _val = val;
    }

    /**
     * Convert this filter operation hint to a readable String.  Note, this does not return the
     * operator -- only text for the operation hint itself.
     * @return the readable operation name
     */
    public final String toString() {
        switch(_val) {
            case INT_NONE:
                return "NONE";
            case INT_EQUAL:
                return "EQUAL";
            case INT_NOT_EQUAL:
                return "NOT_EQUAL";
            case INT_GREATER_THAN:
                return "GREATER_THAN";
            case INT_LESS_THAN:
                return "LESS_THAN";
            case INT_GREATER_THAN_OR_EQUAL:
                return "GREATER_THAN_OR_EQUAL";
            case INT_LESS_THAN_OR_EQUAL:
                return "LESS_THAN_OR_EQUAL";
            case INT_IS_ONE_OF:
                return "IS_ONE_OF";
            case INT_STARTS_WITH:
                return "STARTS_WITH";
            case INT_CONTAINS:
                return "CONTAINS";
            case INT_IS_EMPTY:
                return "IS_EMPTY";
            case INT_IS_NOT_EMPTY:
                return "IS_NOT_EMPTY";
        }

        String message = "Encountered an unknown filter operation with value \"" + _val + "\"";
        assert false : message;

        throw new IllegalStateException(message);
    }

    /**
     * Equals method.
     * @param value value to check
     * @return <code>true</code> if this hint matches the <code>value</code>.  <code>false</code> otherwise.
     */
    public boolean equals(Object value) {
        if(value == this)
            return true;

        if(value == null || !(value instanceof FilterOperationHint))
            return false;

        return ((FilterOperationHint)value)._val == _val;
    }

    /**
     * Hash code.
     * @return the hash code
     */
    public int hashCode() {
        return _val;
    }

    /**
     * The hint's int value.
     *
     * @return the hint's value
     */
    public int getValue() {
        return _val;
    }
}
