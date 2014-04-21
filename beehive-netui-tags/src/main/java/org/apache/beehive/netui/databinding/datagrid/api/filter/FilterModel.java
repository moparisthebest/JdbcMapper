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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * The FilterModel class supports grouping a data grid's {@link Filter} objects into a single JavaBean that
 * implements functionality for interacting with them together.
 * </p>
 * <p>
 * The list of {@link Filter} objects in a FilterModel are ordered.  This class provides methods that
 * can be used to find all the filters for a data grid and to determine if a given filter expression is sorted.
 * </p>
 */
public class FilterModel
    implements java.io.Serializable {

    private List/*<Filter>*/ _filters = null;

    /**
     * Construct an a filter model given a {@link List} of {@link Filter} instances.
     *
     * @param filters the filters for a data grid.
     */
    public FilterModel(List/*<Filter>*/ filters) {
        _filters = filters;
    }

    /**
     * Get the {@link List} of filters for a data grid.
     * @return a data grid's fitlers
     */
    public List/*<Filter>*/ getFilters() {
        if(_filters == null)
            return null;
        else
            return _filters;
    }

    /**
     * Get a {@link List} of {@link Filter} objects.  The list returned will contain
     * all of the {@link Filter} objects whose {@link Filter#getFilterExpression()} matches the given
     * <code>filterExpression</code>.
     * @param filterExpression the expression whose matching filters to find
     * @return <code>null</code> if no matching {@link Filter} objects are found; a {@link List} of {@link Filter}
     * objects otherwise.
     */
    public List/*<Filter>*/ getFilters(String filterExpression) {
        if(_filters == null || filterExpression == null)
            return null;
        else return lookupFilters(filterExpression);
    }

    /**
     * Utility method that checks to see if the given <code>filterExpression</code> matches any of the current
     * {@link Filter} instances.
     * @param filterExpression the filter expression to check
     * @return <code>true</code> if at least one filter matches the <code>filterExpression</code>; <code>false</code>
     *         otherwise.
     */
    public boolean isFiltered(String filterExpression) {
        if(_filters == null || filterExpression == null)
            return false;

        ArrayList list = lookupFilters(filterExpression);
        if(list != null && list.size() > 0)
            return true;
        else return false;
    }

    /**
     * Internal method used to lookup {@link Filter} instances by <code>filterExpression</code>.
     * @param filterExpression the filter expression whose filters to find
     * @return <code>null</code> if no matching {@link Filter} objects are found; a {@link List} of {@link Filter}
     * objects otherwise.
     */
    private ArrayList/*<Filter>*/ lookupFilters(String filterExpression) {
        assert filterExpression != null;
        assert !filterExpression.equals("");

        /* todo: perf.  caching or abstraction to make this faster */
        ArrayList/*<Filter>*/ filters = new ArrayList/*<Filter>*/();
        for(int i = 0; i < _filters.size(); i++) {
            assert _filters.get(i) instanceof Filter;
            Filter filter = (Filter)_filters.get(i);
            if(filter.getFilterExpression().equals(filterExpression))
                filters.add(filter);
        }

        return filters.size() > 0 ? filters : null;
    }
}
