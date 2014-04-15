/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
*/
package org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.util;

import java.util.List;
import java.util.LinkedList;

/**
 * Simple, <b>demonstration-only</b> implementation of a filtering algorithm to show the use
 * of the sort and filter functionality of the data grid.  This allows filter predicates to be
 * chained together and evaluated on each item in a data set.
 */
public class FilterByProperty {

    private LinkedList<FilterPredicate> _filters = null;

    /**
     * Add a {@link FilterPredicate} that implements a condition that will be tested
     * against each item in a data set.
     *
     * @param filterPredicate
     */
    public void addPredicate(FilterPredicate filterPredicate) {
        if(_filters == null)
            _filters = new LinkedList<FilterPredicate>();
        _filters.add(filterPredicate);
    }

    /**
     * Filter a data set using a set of filter predicates.  Each filter
     * predicate will be evaluated against each item in the data set.  Items that
     * are accepted by each predicate will be present in the returned {@link List}.
     * Items returned will be in the order they were processed.
     *
     * @param dataSet the data set to filter
     * @return a {@link List} of items that matched the filter criteria
     */
    public <T> List<T> filter(List<T> dataSet) {
        if(dataSet == null || dataSet.size() == 0)
            return null;

        List<T> filtered = new LinkedList<T>();
        for(T item : dataSet) {
            boolean pass = true;
            for(FilterPredicate filterPredicate : _filters) {
                if(!filterPredicate.check(item))
                    pass = false;
            }
            if(pass)
                filtered.add(item);
        }
        return filtered;
    }
}
