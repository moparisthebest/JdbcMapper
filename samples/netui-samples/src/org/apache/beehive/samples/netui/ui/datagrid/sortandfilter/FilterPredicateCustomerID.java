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
package org.apache.beehive.samples.netui.ui.datagrid.sortandfilter;

import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.util.FilterPredicate;

/**
 * Filter criteria that can be used to test whether a given customer id <i>equals</i>
 * the provided value.  This is an example filter predicate; a real predicate might support
 * additional filter operations such as "starts with", "ends with", "equals" and so on.
 */
public class FilterPredicateCustomerID
    extends FilterPredicate {

    public FilterPredicateCustomerID(FilterOperationHint filterOperation, Object value) {
        super(filterOperation, value);
    }

    public boolean check(Object object) {
        assert object instanceof CustomerBean;
        assert getValue() instanceof Integer;

        CustomerBean customerBean = (CustomerBean)object;
        if(getFilterOperationHint() == FilterOperationHint.EQUAL)
            return customerBean.getCustomerId() == (Integer)getValue();
        else throw new IllegalStateException("Filter predicate does not support the operation " +
            getFilterOperationHint().toString());
    }
}