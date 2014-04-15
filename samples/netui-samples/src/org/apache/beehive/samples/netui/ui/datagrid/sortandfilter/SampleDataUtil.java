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

import java.util.List;
import java.util.LinkedList;

/**
 * Class to provide sample data.  All data grids for all sessions will receive this same list.
 */
public final class SampleDataUtil {

    private SampleDataUtil() {}

    public static List<CustomerBean> createSampleData() {
        LinkedList<CustomerBean> _customers = new LinkedList<CustomerBean>();
        
        _customers.add(new CustomerBean(0, "Parts Unlimited", "Art Smith", "Senior Parts Dealer",
                                        "123 Fake St.", "San Francisco", "CA", "94104", "USA", null, null));
        _customers.add(new CustomerBean(1, "Advanced Parts Supplier", "Joe Franklin", "Widget Expert",
                                        "123 Fake St.", "Boston", "MA", "00123", "USA", null, null));
        _customers.add(new CustomerBean(2, "Fancy Parts", "Sara Sprocket", "Distributor / Owner",
                                        "123 Fake St.", "Seattle", "WA", "98103", "USA", null, null));
        _customers.add(new CustomerBean(3, "WidgetsAreUs", "Mike Widget", "Widgeter",
                                        "123 Fake St.", "Plano", "TX", "75023", "USA", null, null));
        _customers.add(new CustomerBean(4, "Real Bike Parts", "Michael Foley", "Bolt Master",
                                        "123 Fake St.", "McLean", "VA", "22102", "USA", null, null));
        _customers.add(new CustomerBean(5, "The Frame Shop", "Joe Frame", "Frame Artisan",
                                        "123 Fake St.", "Denver", "CO", "80237", "USA", null, null));
        _customers.add(new CustomerBean(6, "Wheels in the Round", "Stacy Wheel", "Wheel Builder",
                                        "123 Fake St.", "Portland", "OR", "97214", "USA", null, null));
        _customers.add(new CustomerBean(7, "Group Cycles", "Mike Group", "Bike Retailer",
                                        "123 Fake St.", "Atlanta", "GA", "30000", "USA", null, null));
        _customers.add(new CustomerBean(8, "Wheelsmith", "Tim Tuneup", "Craftsman",
                                        "123 Fake St.", "Lawrence", "KS", "66047", "USA", null, null));
        _customers.add(new CustomerBean(9, "Peloton Sports", "Jack Hamilton", "Bicycle Dealer",
                                        "123 Fake St.", "Chicago", "IL", "60640", "USA", null, null));
        return _customers;
    }

}
