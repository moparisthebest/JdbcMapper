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
package org.apache.beehive.netui.databinding.datagrid.api.sort;

/**
 * <p>
 * The SortDirection class is an abstract representation of the direction of a sort.  This class
 * is able to represent a sort that is either {@link #ASCENDING}, {@link #DESCENDING},
 * or {@link #NONE}.
 * </p>
 * <p>
 * The SortDirection class is used to specify the direction of sorting on a {@link Sort} JavaBean instance.
 * </p>  
 */
public class SortDirection
    implements java.io.Serializable {

    /**
     * Int value representing an ascending sort.
     */
    public static final int INT_ASCENDING = 0;

    /**
     * Int value representing a descending sort.
     */
    public static final int INT_DESCENDING = 1;

    /**
     * Int value representing no sort.
     */
    public static final int INT_NONE = 2;

    /**
     * Direction representing ascending.
     */
    public static final SortDirection ASCENDING = new SortDirection(INT_ASCENDING);

    /**
     * Direction representing descending.
     */
    public static final SortDirection DESCENDING = new SortDirection(INT_DESCENDING);

    /**
     * Direction representing no sort direction
     * */
    public static final SortDirection NONE = new SortDirection(INT_NONE);

    private int _val;

    private SortDirection(int val) {
        _val = val;
    }

    /**
     * Convert this sort direction to a readable String.  Note, this does not return the query language
     * operator -- only text for the direction itself.
     * @return the readable direction name
     */
    public String toString() {
        switch(_val) {
            case INT_ASCENDING:
                return "ASCENDING";
            case INT_DESCENDING:
                return "DESCENDING";
            case INT_NONE:
                return "NONE";
        }

        String message = "Encountered an unknown sort direction with value \"" + _val + "\"";
        assert false : message;
        throw new IllegalStateException(message);
    }

    /**
     * Equals method.
     * @param value value to check
     * @return <code>true</code> if this direction matches the <code>value</code>; <code>false</code> otherwise.
     */
    public boolean equals(Object value) {
        if(value == this)
            return true;
        if(value == null || !(value instanceof SortDirection))
            return false;

        return ((SortDirection)value)._val == _val;
    }

    /**
     * Hash code.
     * @return the hash code
     */
    public int hashCode() {
        return _val;
    }

    /**
     * The direction's int value.
     *
     * @return the direction's value
     */
    public int getValue() {
        return _val;
    }
}
