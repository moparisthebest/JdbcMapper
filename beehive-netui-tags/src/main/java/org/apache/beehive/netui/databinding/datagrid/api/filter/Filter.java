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

import org.apache.beehive.netui.util.logging.Logger;

/**
* <p>
* The Filter class is a JavaBean that abstractly represents the data needed to calculate a filter
* for a data set.  A filter consists of some {@link String} expression, a {@link FilterOperation}
* and a filter value.  The mechanism for applying a filter to a data set is not provided here.
* </p>
* <p>
 * The filter expression is used to abstractly name some part of a data set to filter.  The filter operation
 * is used to describe the operation to use when performing filtering.  The set of operations for
 * available for use should be related to a subclass of the Filter type; there are no implicitly
 * defined operations.  The filter value is used to describe how to filter a given filter expression.
 * For example, in an application performing filtering using SQL, a filter expression <code>pet</code>
 * with a filter operation mapping to "equals" and a filter value of "dog" could be transformed
 * into a SQL WHERE fragment as:
 * <pre>
 *   WHERE pet = 'dog'
 * </pre>
 * The Filter class simply provides an abstraction for a filter's metadata; the mechanism for performing
 * this transformation from Filter instance to SQL fragment is not provided here.
 * </p>
 * <p>
 * In addition to the fundamental data fora Filter, two additional properties can be defined.  The
 * {@link org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint} property can
 * be used to reference a class of operation related to the hint.  The
 * {@link org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint} property
 * defines a hint for the type of data associated with the Filter's filter expression.  This data can be
 * used to handle quoting and type conversion for a given filter value.  In the example above, a type hint of
 * a {@link org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint#STRING} can be used
 * when constructing the SQL fragment in order to perform the correct quoting of the filter value.
 */
public class Filter
    implements java.io.Serializable {

    private static final Logger LOGGER = Logger.getInstance(Filter.class);

    private String _filterExpression;
    private FilterOperation _filterOperation;
    private FilterOperationHint _filterOperationHint;
    private Object _value;

    private FilterTypeHint _typeHint = FilterTypeHint.getDefault();

    /**
     * Get the type hint for this filter.
     * @return the filter type hint
     */
    public FilterTypeHint getTypeHint() {
        return _typeHint;
    }

    /**
     * Set the type hint for this filter
     * @param typeHint the filter type hint
     */
    public void setTypeHint(FilterTypeHint typeHint) {
        _typeHint = typeHint;
    }

    /**
     * Set the filter expression for this filter
     * @param filterExpression the filter expression
     */
    public void setFilterExpression(String filterExpression) {
        _filterExpression = filterExpression;
    }

    /**
     * Get the filter expression for this filter
     * @return the filter expression
     */
    public String getFilterExpression() {
        return _filterExpression;
    }

    /**
     * Set the filter operation for this filter
     * @param filterOperation the filter operation
     */
    public void setOperation(FilterOperation filterOperation) {
        _filterOperation = filterOperation;
    }

    /**
     * Get the filter operation for this filter
     * @return the filter operation
     */
    public FilterOperation getOperation() {
        return _filterOperation;
    }

    /**
     * Get the operation hint for this filter
     * @return the filter operation hint
     */
    public FilterOperationHint getOperationHint() {
        return _filterOperationHint;
    }

    /**
     * Set the operation hint for this filter
     * @param filterOperationHint the filter operation hint
     */
    public void setOperationHint(FilterOperationHint filterOperationHint) {
        _filterOperationHint = filterOperationHint;
    }

    /**
     * Set the value for this filter.  Note, in the default implementation, the <code>value</code>
     * must implement {@link java.io.Serializable}.
     * @param value the value
     */
    public void setValue(Object value) {

        if(LOGGER.isInfoEnabled() && !(value instanceof java.io.Serializable))
            LOGGER.info("Warning: setting a filter value tiat is not serializable.  The Filter object is serializable and should contain only serializable state");

        _value = value;
    }

    /**
     * Get the value for this filter.
     * @return the value
     */
    public Object getValue() {
        return _value;
    }
}
