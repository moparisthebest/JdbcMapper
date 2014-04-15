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

import java.util.Map;
import javax.servlet.jsp.JspContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.test.servlet.ServletFactory;

/**
 *
 */
public class PagerModelTest
    extends TestCase {

    private JspContext _jspContext = null;

    public void testEmptyPagerModel() {
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext);
        PagerModel pm = DataGridTestUtil.getPagerModel(dgm);

        assertEquals("Invalid page", 0, pm.getPage());
        assertEquals("Invalid page size", pm.getDefaultPageSize(), pm.getPageSize());
        assertEquals("Invalid row", PagerModel.DEFAULT_ROW, pm.getRow());
    }

    public void testPagerModel1() {
        /*
          row: 4
          pagesize: 2
          current page: 2
         */
        String name = "bugs";
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;4&netui_pagesize=bugs;2");
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext, name);
        PagerModel pm = DataGridTestUtil.getPagerModel(dgm);
        pm.setDefaultPageSize(2);

        assertEquals("Invalid page", 2, pm.getPage());
        assertEquals("Invalid page size", 2, pm.getPageSize());
        assertEquals("Invalid row", 4, pm.getRow());
        assertEquals("Invalid previous page row", 2, pm.getRowForPreviousPage());
        assertEquals("Invalid last page row", 10, pm.getRowForLastPage());

        DataGridURLBuilder urlBuilder = DataGridTestUtil.createDataGridURLBuilder(DataGridTestUtil.getRequest(_jspContext), name);
        Map queryParams = urlBuilder.getQueryParamsForFirstPage();
        assertEquals(0, queryParams.size());

        queryParams = urlBuilder.getQueryParamsForPreviousPage();
        assertEquals(1, queryParams.size());
        assertEquals("bugs;2", ((String[])queryParams.get("netui_row"))[0]);

        queryParams = urlBuilder.getQueryParamsForNextPage();
        assertEquals(1, queryParams.size());
        assertEquals("bugs;6", ((String[])queryParams.get("netui_row"))[0]);

        queryParams = urlBuilder.getQueryParamsForLastPage();
        assertEquals(1, queryParams.size());
        assertEquals("bugs;10", ((String[])queryParams.get("netui_row"))[0]);
    }

    public void testPagerModel2() {
        /*
          row: 7
          pagesize: 3
          current page: 2
         */
        String name = "bugs";
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;6&netui_pagesize=bugs;3");
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext, name);
        PagerModel pm = DataGridTestUtil.getPagerModel(dgm);

        assertEquals("Invalid page", 2, pm.getPage());
        assertEquals("Invalid page size", 3, pm.getPageSize());
        assertEquals("Invalid row", 6, pm.getRow());
    }

    public void testPagerModelPageSizeOverride() {
        /*
          row: 7
          pagesize: 3
          current page: 2
         */
        String name = "bugs";
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;6&netui_pagesize=bugs;3");
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext, name);
        PagerModel pm = DataGridTestUtil.getPagerModel(dgm);

        assertEquals(2, pm.getPage());
        assertEquals(3, pm.getPageSize());
        assertEquals(6, pm.getRow());

        pm.setPageSize(12);
        pm.setRow(0); /* this explicitly sets the current row to the beginning */
        assertEquals(12, pm.getPageSize());
        assertEquals(10, pm.getDefaultPageSize());
        assertEquals(0, pm.getRowForLastPage());
        assertEquals(11, pm.getLastRowForPage());
        assertEquals(0, pm.getRowForFirstPage());
        /* todo: since there is no next page, should this be NO_SUCH_PAGE? */
        assertEquals(12, pm.getRowForNextPage());
    }

    public void testPagerModel3() {
        /*
          row: 9
          pagesize: 10, manually set to 5
          current page: 0
          last page: 1
         */
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=" + DataGridTestUtil.DEFAULT_DATA_GRID_NAME + ";0");
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext);
        PagerModel pm = DataGridTestUtil.getPagerModel(dgm);
        pm.setPageSize(5);
        /* 12: 0-4, 5-9, 10-15 _lastRow=10*/

        assertEquals("Invalid page", 0, pm.getPage());
        assertEquals("Invalid page size", 5, pm.getPageSize());
        assertEquals("Invalid row", 0, pm.getRow());
        assertEquals("Invalid last page", 10, pm.getRowForLastPage());
        assertEquals("Invalid previous page", -1, pm.getRowForPreviousPage());
        assertEquals("Invalid next page", 5, pm.getRowForNextPage());

        String[] pagerParamValules = dgm.getUrlBuilder().getPagerParamValues();
        assertNotNull("Expected non-null pagerParamValues", pagerParamValules);
        assertEquals("Invalud number of pager params", 3, pagerParamValules.length);
        assertEquals("Expected empty pagerParamValue[0]", DataGridTestUtil.DEFAULT_DATA_GRID_NAME + ";0", pagerParamValules[0]);
        assertNotNull("Expected non-null next page query params", dgm.getUrlBuilder().getQueryParamsForNextPage());
    }

    public void testPagerModel4() {
        /*
          row: 7
          pagesize: 3
          current page: 2
          data set size: 12
         */
        String name = "bugs";
        DataGridTestUtil.initQueryString(_jspContext, "netui_pagesize=bugs;2");
        DataGridTagModel dgm = DataGridTestUtil.getDataGridTagModel(_jspContext, name);
        PagerModel pm = DataGridTestUtil.getPagerModel(dgm);

        assertEquals("Invalid page", 0, pm.getPage());
        assertEquals("Invalid page size", 2, pm.getPageSize());
        assertEquals("Invalid row", 0, pm.getRow());
        assertEquals("Invalid last page", 10, pm.getRowForLastPage());
        assertEquals("Invalid previous page", -1, pm.getRowForPreviousPage());
        assertEquals("Invalid next page", 2, pm.getRowForNextPage());
    }

    public void testPagerModel_emptyURL() {
        String namespace = "bugs";

        DataGridState dgs = DataGridTestUtil.createDataGridState(_jspContext, namespace);
        PagerModel pm = dgs.getPagerModel();

        assertNotNull("Expected non-null PagerModel", pm);
        assertEquals("Invalid current row", PagerModel.DEFAULT_ROW, pm.getRow());
        assertEquals("Invalid page", 0, pm.getPage());
        assertEquals("Invalid page size", pm.getDefaultPageSize(), pm.getPageSize());
        assertEquals("Invalid first page", 0, pm.getRowForFirstPage());
        assertEquals("Invalid next page", 10, pm.getRowForNextPage());

        boolean threw = false;
        try {
            pm.getRowForLastPage();
        } catch(Exception e) {
            threw = true;
        }
        assertTrue("Expected exception when calling getRowForLastPage", threw);

        pm.setDataSetSize(20);

        assertEquals("Invalid last page", 10, pm.getRowForLastPage());
        assertEquals("Previous page", -1, pm.getRowForPreviousPage());
    }

    public void testPagerModel5() {
        String namespace = "bugs";

        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;20");
        DataGridState state = DataGridTestUtil.createDataGridState(_jspContext, namespace);
        PagerModel pm = state.getPagerModel();

        assertEquals("Invalid current row", 20, pm.getRow());
        assertEquals("Invalid current page", 2, pm.getPage());
        assertEquals("Invalid first page", 0, pm.getRowForFirstPage());
        assertEquals("Invalid next page", 30, pm.getRowForNextPage());
        assertEquals("Invalid previous page", 10, pm.getRowForPreviousPage());
    }

    public void testPagerModel6() {
        String namespace = "bugs";

        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;18&netui_pagesize=bugs;6");
        DataGridState state = DataGridTestUtil.createDataGridState(_jspContext, namespace);
        PagerModel pm = state.getPagerModel();

        assertEquals("Invalid current row", 18, pm.getRow());
        assertEquals("Invalid current page", 3, pm.getPage());
    }

    public void testPagerModel7() {
        String namespace = "bugs";

        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;18&netui_pagesize=bugs;6&netui_row=issues;30");
        DataGridState state = DataGridTestUtil.createDataGridState(_jspContext, namespace);
        PagerModel pm = state.getPagerModel();

        assertEquals("Invalid current row", 18, pm.getRow());
        assertEquals("Invalid current page", 3, pm.getPage());

        state = DataGridTestUtil.createDataGridState(_jspContext, "issues");
        pm = state.getPagerModel();
        assertEquals("Invalid current row", 30, pm.getRow());
        assertEquals("Invalid current page size", 10, pm.getPageSize());
        assertEquals("Invalid current page", 3, pm.getPage());

    }

    public void testPagerModelAPI1() {
        String namespace = "bugs";

        /*
        0-5, 6-11, 12-17, 18-23, 24-29, 30-35
         */
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;21&netui_pagesize=bugs;6");
        DataGridState state = DataGridTestUtil.createDataGridState(_jspContext, namespace);
        PagerModel pm = state.getPagerModel();
        pm.setDataSetSize(32);

        assertEquals("Invalid current row", 18, pm.getRow());
        assertEquals("Invalid current page", 3, pm.getPage());

        assertEquals("Invalid first row", 0, pm.getRowForFirstPage());
        assertEquals("Invalid previous row", 12, pm.getRowForPreviousPage());
        assertEquals("Invalid next row", 24, pm.getRowForNextPage());
        assertEquals("Invalid last row", 30, pm.getRowForLastPage());

        assertEquals("Invalid first page", 0, pm.getFirstPage());
        assertEquals("Invalid next page", 4, pm.getNextPage());
        assertEquals("Invalid previous page", 2, pm.getPreviousPage());
        assertEquals("Invalid last page", 5, pm.getLastPage());

        pm.setRow(15);
        assertEquals("Invalid current row", 12, pm.getRow());
    }

    public void testPagerModelAPI2() {
        String namespace = "bugs";

        /*
        0-5, 6-11, 12-17, 18-23, 24-29, 30-35
         */
        DataGridTestUtil.initQueryString(_jspContext, "netui_row=bugs;21&netui_pagesize=bugs;6");
        DataGridState state = DataGridTestUtil.createDataGridState(_jspContext, namespace);
        PagerModel pm = state.getPagerModel();
        pm.setDataSetSize(32);

        pm.setRow(10000);
        assertEquals("Invalid row value", 30, pm.getRow());
    }

    protected void setUp() {
        _jspContext = ServletFactory.getPageContext();
    }

    protected void tearDown() {
        _jspContext = null;
    }

    public PagerModelTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(PagerModelTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
