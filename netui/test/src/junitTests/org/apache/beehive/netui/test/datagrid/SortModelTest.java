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

import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;

/**
 *
 */
public class SortModelTest
    extends TestCase {

    private static final String GRID_NAME = "bugs";

    private ServletRequest _servletRequest;

    public void testSortModelSimple() {
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List/*<Sort>*/ sorts = sortModel.getSorts();
        assertNull(sorts);
    }

    public void testSortModelSimple2() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List/*<Sort>*/ sorts = sortModel.getSorts();
        assertNull(sorts);

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(1, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);
    }

    public void testSortModelSimple3() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;id");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List/*<Sort>*/ sorts = sortModel.getSorts();
        assertNotNull(sorts);
        assertEquals(1, sorts.size());
        assertEquals(SortDirection.ASCENDING, ((Sort)sorts.get(0)).getDirection());
        assertEquals("id", ((Sort)sorts.get(0)).getSortExpression());

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;id", values[0]);
    }

    public void testSortModelSimple4() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;id,-name");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List/*<Sort>*/ sorts = sortModel.getSorts();
        assertNotNull(sorts);
        assertEquals(2, sorts.size());
        assertEquals(SortDirection.ASCENDING, ((Sort)sorts.get(0)).getDirection());
        assertEquals("id", ((Sort)sorts.get(0)).getSortExpression());
        assertEquals(SortDirection.DESCENDING, ((Sort)sorts.get(1)).getDirection());
        assertEquals("name", ((Sort)sorts.get(1)).getSortExpression());

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;id,-name", values[0]);
    }

    public void testSortModelSimple5() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;id,-name");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List/*<Sort>*/ sorts = sortModel.getSorts();
        assertNotNull(sorts);
        assertEquals(2, sorts.size());
        assertEquals(SortDirection.ASCENDING, ((Sort)sorts.get(0)).getDirection());
        assertEquals("id", ((Sort)sorts.get(0)).getSortExpression());
        assertEquals(SortDirection.DESCENDING, ((Sort)sorts.get(1)).getDirection());
        assertEquals("name", ((Sort)sorts.get(1)).getSortExpression());

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;id,-name", values[0]);

        // now, for the "id" expression, change the direction
        queryParams = urlBuilder.buildSortQueryParamsMap("id");
        assertEquals(2, queryParams.size());
        values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;-id,-name", values[0]);

    }

    public void testManualSortModel() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;id,-name");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        /* set "id" direction to NONE */
        List sorts = sortModel.getSorts();
        assertNotNull(sorts);
        Sort idSort = lookupSort(sorts, "id");
        assertNotNull(idSort);
        idSort.setDirection(SortDirection.NONE);

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;-name", values[0]);

        /* set "id" direction to ASCENDING */
        idSort.setDirection(SortDirection.ASCENDING);
        queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;id,-name", values[0]);

        /* set "name" direction to NONE */
        Sort nameSort = lookupSort(sorts, "name");
        assertNotNull(nameSort);
        nameSort.setDirection(SortDirection.NONE);
        queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;id", values[0]);
        /* reset "name" direction to DESCENDING */
        nameSort.setDirection(SortDirection.DESCENDING);

        /* add a sort for a new column */
        Sort newSort = new Sort();
        newSort.setSortExpression("newcolumn");
        newSort.setDirection(SortDirection.DESCENDING);
        sorts.add(newSort);

        queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;id,-name,-newcolumn", values[0]);
    }

    public void testSortModelSimple6() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;-id,-name");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List sorts = sortModel.getSorts();
        assertNotNull(sorts);
        Sort sort = lookupSort(sorts, "id");
        assertNotNull(sort);
        sort.setDirection(SortDirection.NONE);

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(1, values.length);
        assertEquals("bugs;-name", values[0]);
    }

    public void testSortModelSimple7() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;-id");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List sorts = sortModel.getSorts();
        assertNotNull(sorts);
        Sort sort = lookupSort(sorts, "id");
        assertNotNull(sort);
        sort.setDirection(SortDirection.NONE);

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(1, queryParams.size());
        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);
    }

    /* this is a test of sorts for two grids on the same URL */
    public void testSortModelSimple8() {
        DataGridTestUtil.initQueryString(_servletRequest, "foo=bar&netui_sort=bugs;id,-name&netui_sort=developers;bugcount,-milestone");
        SortModel sortModel = DataGridTestUtil.createSortModel(_servletRequest, GRID_NAME);

        List/*<Sort>*/ sorts = sortModel.getSorts();
        assertNotNull(sorts);
        assertEquals(2, sorts.size());
        assertEquals(SortDirection.ASCENDING, ((Sort)sorts.get(0)).getDirection());
        assertEquals("id", ((Sort)sorts.get(0)).getSortExpression());
        assertEquals(SortDirection.DESCENDING, ((Sort)sorts.get(1)).getDirection());
        assertEquals("name", ((Sort)sorts.get(1)).getSortExpression());

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(_servletRequest, GRID_NAME);
        Map queryParams = urlBuilder.getQueryParams();
        assertEquals(2, queryParams.size());

        String[] values = (String[])queryParams.get("foo");
        assertEquals(1, values.length);
        assertEquals("bar", values[0]);

        values = (String[])queryParams.get("netui_sort");
        assertEquals(2, values.length);
        assertEquals("developers;bugcount,-milestone", values[0]);
        assertEquals("bugs;id,-name", values[1]);
    }

    private static final Sort lookupSort(List sorts, String sortExpression) {
        if(sorts == null)
            return null;

        for(int i = 0; i < sorts.size(); i++) {
            Sort sort = (Sort)sorts.get(i);
            if(sort.getSortExpression().equals(sortExpression))
                return sort;
        }
        return null;
    }

    protected void setUp() {
        _servletRequest = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _servletRequest = null;
    }

    public SortModelTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SortModelTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
