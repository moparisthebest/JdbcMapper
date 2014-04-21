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
 * The Sort class is a JavaBean that abstractly represents the data needed to calculate a sort
 * on some data set.  A sort consists of some {@link String} expression and a {@link SortDirection}.
 * The mechanism for performing the sort is not provided here.
 * </p>
 * <p>
 * A Sort object can be used by some sorting infrastructure to either parameterise a SQL or XQuery
 * query or to simply sort in-memory Java objects.   For example, when converting a Sort into
 * a SQL fragment, a Sort with sortExpression "foo" and sortDirection {@link SortDirection#DESCENDING} could
 * be converted into:
 * <pre>
 *     ORDER BY FOO DESC
 * </pre>
 * </p>
*/
public class Sort
    implements java.io.Serializable {

    private String _sortExpression;
    private SortDirection _sortDirection;

    /**
     * Empty constructor.
     */
    public Sort() {
    }

    /**
     * Constructs a Sort with the given <code>sortExpression</code> and <code>sortDirection</code>.
     *
     * @param sortExpression the Sort's sort expression
     * @param sortDirection the Sort's sort direction
     */
    public Sort(String sortExpression, SortDirection sortDirection) {
        this();
        _sortExpression = sortExpression;
        _sortDirection = sortDirection;
    }

    /**
     * Get the sort expression
     * @return the sort expression
     */
    public String getSortExpression() {
        return _sortExpression;
    }

    /**
     * Set the sort expression
     * @param expression the sort expression
     */
    public void setSortExpression(String expression) {
        _sortExpression = expression;
    }

    /**
     * Get the {@link SortDirection}
     * @return the sort direction
     */
    public SortDirection getDirection() {
        return _sortDirection;
    }

    /**
     * Set the {@link SortDirection}
     * @param sortDirection the sort direction
     */
    public void setDirection(SortDirection sortDirection) {
        _sortDirection = sortDirection;
    }
}
