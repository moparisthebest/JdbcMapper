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

import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperation;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;

/**
 * A predicate that is evaluated when performing filtering.  A subclass will implement the {@link #check(Object)}
 * method to evaluate a certain criteria against an object.
 */
public abstract class FilterPredicate {

    private FilterOperationHint _filterOperationHint;
    private Object _value;

    public FilterPredicate(FilterOperationHint filterOperation, Object value) {
        _value = value;
        _filterOperationHint = filterOperation;
    }

    public abstract boolean check(Object object);

    protected FilterOperationHint getFilterOperationHint() {
        return _filterOperationHint;
    }

    protected Object getValue() {
        return _value;
    }
}
