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

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.*;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.PagedDataSet;
import org.apache.beehive.netui.test.servlet.HttpServletRequestHandler;

/**
 *
 */
class DataGridTestUtil {

    public static final String DEFAULT_DATA_GRID_NAME = "testgrid";

    /* do not construct */
    private DataGridTestUtil() {
    }

    static final void initQueryString(ServletRequest request, String queryString) {
        assert request != null : "Received null request!";
        assert Proxy.getInvocationHandler(request) instanceof HttpServletRequestHandler;

        HttpServletRequestHandler handler = (HttpServletRequestHandler)Proxy.getInvocationHandler(request);
        handler.setQueryString(queryString);
    }

    static final ServletRequest getRequest(JspContext jspContext) {
        return ((PageContext)jspContext).getRequest();
    }

    static final void initQueryString(JspContext jspContext, String queryString) {
        initQueryString(getRequest(jspContext), queryString);
    }

    static final DataGridTagModel getDataGridTagModel(JspContext jspContext) {
        return buildDataGridTagModel(jspContext, DEFAULT_DATA_GRID_NAME, getPagedDataSet());
    }

    static final DataGridTagModel getDataGridTagModel(JspContext jspContext, String name) {
        return buildDataGridTagModel(jspContext, name, getPagedDataSet());
    }

    static final DataGridTagModel getEmptyDataGridTagModel(JspContext jspContext) {
        return buildDataGridTagModel(jspContext, DEFAULT_DATA_GRID_NAME, getEmptyPagedDataSet());
    }

   static final PagedDataSet getEmptyPagedDataSet() {
        return new PagedDataSet("${actionForm.emptyDataSource}", Collections.EMPTY_LIST.iterator());
    }

    static final PagedDataSet getPagedDataSet() {
        ArrayList/*<String>*/ list = new ArrayList/*<String>*/();
        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        list.add("pqr");
        list.add("stu");
        list.add("vwx");
        list.add("yz");
        list.add("123");
        list.add("456");
        list.add("789");
        return new PagedDataSet("${actionForm.dataSource}", list.iterator());
    }

    private static final DataGridTagModel buildDataGridTagModel(JspContext jspContext, String name, PagedDataSet dataSet) {
        DataGridConfig config = getDataGridConfig();
        DataGridResourceProvider provider = getDataGridConfig().getDefaultResourceProvider();
        provider.setLocale(Locale.US);
        DataGridTagModel dgm = new DataGridTagModel(name, config, jspContext);
        dgm.setDataSet(dataSet);
        dgm.setResourceProvider(provider);
        dgm.getState().getPagerModel().setPageAction("fauxPageAction.do");
        return dgm;
    }

    static final PagerModel getPagerModel(DataGridTagModel dgm) {
        return dgm.getState().getPagerModel();
    }

    static final DataGridConfig getDataGridConfig() {
        return DataGridConfigFactory.getInstance();
    }

    static final DataGridState createDataGridState(ServletRequest request, String gridName) {
        DataGridStateFactory factory = DataGridStateFactory.getInstance(request);
        return factory.getDataGridState(gridName, getDataGridConfig());
    }

    static final DataGridState createDataGridState(JspContext jspContext, String gridName) {
        DataGridStateFactory factory = DataGridStateFactory.getInstance(getRequest(jspContext));
        return factory.getDataGridState(gridName, getDataGridConfig());
    }

    static final DataGridURLBuilder createDataGridURLBuilder(ServletRequest request, String gridName) {
        DataGridStateFactory factory = DataGridStateFactory.getInstance(request);
        return factory.getDataGridURLBuilder(gridName, getDataGridConfig());
    }

    static final SortModel createSortModel(ServletRequest request, String gridNamespace) {
        DataGridState state = DataGridTestUtil.createDataGridState(request, gridNamespace);
        SortModel sortModel = state.getSortModel();
        return sortModel;
    }

}
