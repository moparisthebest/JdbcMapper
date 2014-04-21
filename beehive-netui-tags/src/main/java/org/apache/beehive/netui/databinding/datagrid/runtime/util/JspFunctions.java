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
package org.apache.beehive.netui.databinding.datagrid.runtime.util;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.jsp.JspContext;

import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfigFactory;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;

/**
 * <p>
 * This class contains utility methods that are callable as JSP functions by page authors.  These are used
 * to provide utility methods that can be accessed via JSP EL for invoking methods on various data grid
 * state objects.
 * </p>
 * @netui:jspfunctions
 */
public final class JspFunctions {

    private JspFunctions() {}

    /**
     * Given a sort expression, check to see if the sort expression is sorted ascending in a data grid's
     * {@link SortModel}.
     * @param sortModel a grid's sort model
     * @param sortExpression the sort expression
     * @return return <code>true</code> if a {@link Sort} is found whose sort expression
     * matches the sort expression given here and whose direction is {@link SortDirection#ASCENDING}.
     * @netui:jspfunction name="isSortedAscending"
     * signature="boolean isSortedAscending(org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel,java.lang.String)"
     */
    public static boolean isSortedAscending(SortModel sortModel, String sortExpression) {
        if(sortModel == null || sortExpression == null)
            return false;

        Sort sort = sortModel.lookupSort(sortExpression);
        if(sort != null && sort.getDirection() == SortDirection.ASCENDING)
            return true;
        else return false;
    }

    /**
     * Given a sort expression, check to see if the sort expression is sorted descending in a data grid's
     * {@link SortModel}.
     * @param sortModel a grid's sort model
     * @param sortExpression the sort expression
     * @return return <code>true</code> if a {@link Sort} is found whose sort expression
     * matches the sort expression given here and whose direction is {@link SortDirection#DESCENDING}.
     * @netui:jspfunction name="isSortedDescending"
     * signature="boolean isSortedDescending(org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel,java.lang.String)"
     */
    public static boolean isSortedDescending(SortModel sortModel, String sortExpression) {
        if(sortModel == null || sortExpression == null)
            return false;

        Sort sort = sortModel.lookupSort(sortExpression);
        if(sort != null && sort.getDirection() == SortDirection.DESCENDING)
            return true;
        else return false;
    }

    /**
     * <p>
     * Utility method used to build a query parameter map which includes the list of parameters needed to change the
     * direction of a sort related to the given sort expression.  This method uses a {@link DataGridURLBuilder}
     * instance to call its {@link DataGridURLBuilder#buildSortQueryParamsMap(String)} method from JSP EL.
     * </p>
     * @param urlBuilder the data grid URL builder associated with a
     * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridState} object that is used to
     * build lists of query parameters
     * @param sortExpression the sort expression whose direction to change
     * @return a {@link Map} of key / value pairs for query parameters
     * @netui:jspfunction name="buildQueryParamsMapForSortExpression"
     * signature="java.util.Map buildQueryParamsMapForSortExpression(org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder,java.lang.String)"
     */
    public static Map buildQueryParamsMapForSortExpression(DataGridURLBuilder urlBuilder, String sortExpression) {
        if(urlBuilder == null || sortExpression == null)
            return null;

        return urlBuilder.buildSortQueryParamsMap(sortExpression);
    }
}
