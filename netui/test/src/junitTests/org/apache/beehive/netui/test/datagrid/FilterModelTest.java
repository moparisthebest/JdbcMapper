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
package org.apache.beehive.netui.test.datagrid;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;

/**
 *
 */
public class FilterModelTest
    extends TestCase {

    private HttpServletRequest _request = null;
    private String _namespace = "bugs";

    public void testSortModelSimple() {
        String queryString = "netui_filter=" + _namespace + ";CUSTOMERNAME~eq~homer&netui_filter=products;ID~ne~1234";
        DataGridTestUtil.initQueryString(_request, queryString);
        DataGridState state = DataGridTestUtil.createDataGridState(_request, _namespace);

        FilterModel fm = state.getFilterModel();
        assertNotNull(fm);
        assertNotNull(fm.getFilters("CUSTOMERNAME"));
        assertTrue(fm.isFiltered("CUSTOMERNAME"));
        assertEquals(FilterOperationHint.EQUAL, ((Filter)fm.getFilters("CUSTOMERNAME").get(0)).getOperation().getOperationHint());

        state = DataGridTestUtil.createDataGridState(_request, "products");
        fm = state.getFilterModel();
        assertNotNull(fm);
        assertNotNull(fm.getFilters("ID"));
        assertTrue(fm.isFiltered("ID"));
        assertEquals(FilterOperationHint.NOT_EQUAL, ((Filter)fm.getFilters("ID").get(0)).getOperation().getOperationHint());

        assertFalse(fm.isFiltered("QWERTY"));
    }

    protected void setUp() {
        _request = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _request = null;
    }

    public FilterModelTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(FilterModelTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
