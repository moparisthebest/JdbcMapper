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
package org.apache.beehive.netui.databinding.datagrid.runtime.config;


import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortStrategy;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * This class exposes parameter maps that contain URL key / value pairs which can be data bound to anchors
 * and other markup during page rendering. Methods exposed here are useful for building URLs that can be clicked on
 * in the <i>future</i>.  A case of this would be a pager URL that will move a UI to the "next" page of data.  The
 * URL parameters would be computed using this class and rendered to the UI so that it is clickable for the
 * next HTTP submit.
 * <p/>
 * <p>
 * An instance of this class is associated with a single data grid by name.  Instances should be obtained
 * via the {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory#getDataGridURLBuilder(String)}
 * method.
 * </p>
 */
class DefaultDataGridURLBuilder
    extends DataGridURLBuilder {

    private transient DefaultDataGridStateCodec _codec;

    /**
     * <p>
     * Package protected constructor.  This class is intended only to be created via the default
     * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig} object.
     * </p>
     * @param codec the state codec providing state information which will be converted into URLs here.
     */
    DefaultDataGridURLBuilder(DefaultDataGridStateCodec codec) {
        _codec = codec;
    }

    /**
     * <p>
     * Get the URL query parameter key used to store the current row for a data grid's pager.  The value
     * of this key is:
     * <br/>
     * <pre>
     *     netui_row
     * </pre>
     * </p>
     * @return the URL parameter key for the current row
     */
    public String getPagerRowQueryParamKey() {
        return DefaultDataGridStateCodec.PARAM_KEY_ROW;
    }

    /**
     * <p>
     * Get the query parameters for the currene state of the data grid.  The {@link Map} returned by this method
     * will maintain the <i>current</i> state of the data grid and all of the associated URL parameters.  The
     * {@link Map} contains key / value pairs of type String / String[].
     * </p>
     * @return the current URL and data grid state
     */
    public Map getQueryParams() {
        Map params = _codec.getExistingParams();
        Map newParams = new HashMap();

        addSortParams(newParams);
        addFilterParams(newParams);
        addPagerParams(newParams);

        params = mergeMaps(params, newParams);
        params = transformMap(params);

        return params;
    }

    /**
     * <p>
     * Get a URL query parameter map that will change the data grid's page value to display the
     * <i>first</i> page in a data set.  This map also contains all of the other existing URL
     * parameters.  The {@link Map} contains key / value pairs of type String / String[].
     * </p>
     * @return the URL and data grid state needed to change to the <i>first</i> page for a data grid
     */
    public Map getQueryParamsForFirstPage() {
        Map params = _codec.getExistingParams();
        Map newParams = new HashMap();

        PagerModel pagerModel = getDataGridState().getPagerModel();
        assert pagerModel != null;

        addSortParams(newParams);
        addFilterParams(newParams);
        newParams.putAll(_codec.buildPageParamMap(new Integer(pagerModel.getRowForFirstPage()),
                                                  new Integer(pagerModel.getPageSize())));

        params = mergeMaps(params, newParams);
        params = transformMap(params);

        return params;
    }

    /**
     * <p>
     * Get a URL query parameter map that will change the data grid's page value to display the
     * <i>previous</i> page in a data set relative to the current page.  This map also contains all of
     * the other existing URL parameters.  The {@link Map} contains key / value pairs of type
     * String / String[].
     * </p>
     * @return the URL and data grid state needed to change to the <i>previous</i> page for a data grid
     */
    public Map getQueryParamsForPreviousPage() {
        Map params = _codec.getExistingParams();
        Map newParams = new HashMap();

        PagerModel pagerModel = getDataGridState().getPagerModel();
        assert pagerModel != null;

        addSortParams(newParams);
        addFilterParams(newParams);
        newParams.putAll(_codec.buildPageParamMap(new Integer(pagerModel.getRowForPreviousPage()),
                                                  new Integer(pagerModel.getPageSize())));

        params = mergeMaps(params, newParams);
        params = transformMap(params);

        return params;
    }

    /**
     * <p>
     * Get a URL query parameter map that will change the data grid's page value to display the
     * <i>next</i> page in a data set relative to the current page.  This map also contains all of
     * the other existing URL parameters.  The {@link Map} contains key / value pairs of type
     * String / String[].
     * </p>
     * @return the URL and data grid state needed to change to the <i>next</i> page for a data grid
     */
    public Map getQueryParamsForNextPage() {
        Map params = _codec.getExistingParams();
        Map newParams = new HashMap();

        PagerModel pagerModel = getDataGridState().getPagerModel();
        assert pagerModel != null;

        addSortParams(newParams);
        addFilterParams(newParams);
        newParams.putAll(_codec.buildPageParamMap(new Integer(pagerModel.getRowForNextPage()),
                                                  new Integer(pagerModel.getPageSize())));

        params = mergeMaps(params, newParams);
        params = transformMap(params);

        return params;
    }

    /**
     * <p>
     * Get a URL query parameter map that will change the data grid's page value to display the
     * <i>last</i> page in a data set.  This map also contains all of the other existing URL parameters.
     * The {@link Map} contains key / value pairs of type String / String[].
     * </p>
     * @return the URL and data grid state needed to change to the <i>last</i> page for a data grid
     */
    public Map getQueryParamsForLastPage() {
        Map params = _codec.getExistingParams();
        Map newParams = new HashMap();

        PagerModel pagerModel = getDataGridState().getPagerModel();
        assert pagerModel != null;

        addSortParams(newParams);
        addFilterParams(newParams);
        newParams.putAll(_codec.buildPageParamMap(new Integer(pagerModel.getRowForLastPage()),
                                                  new Integer(pagerModel.getPageSize())));

        params = mergeMaps(params, newParams);
        params = transformMap(params);

        return params;
    }

    /**
     * <p>
     * Get a String[] of the URL query parameter values that could when submitted to a server to make a request
     * can explicitly change the current page for a data grid to a specific page.  The returned pager parameter
     * values are structured as:
     * <pre>
     *     <datagrid-name>~<row-number>
     * </pre>
     * These values can be attached to a URL in order to submit a query string which will set the data grid's
     * current page.
     * </p>
     * @return
     */
    public String[] getPagerParamValues() {
        PagerModel pagerModel = getDataGridState().getPagerModel();

        String[] params = new String[pagerModel.getPageCount()];
        for(int i = 0; i < params.length; i++) {
            params[i] = _codec.encodePageSize(pagerModel.encodeRowForPage(i));
        }

        return params;
    }

    /**
     * <p>
     * Get a URL query parameter map that contains state which will change the direction of a
     * {@link Sort} whose sort expression matches the given sort expression value.  The {@link SortStrategy}
     * associated with the data grid's {@link SortModel} will be used to choose the next
     * {@link org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection} given the sort's current
     * sort direction.  This map also contains all of the other existing URL parameters.  The {@link Map}
     * contains key / value pairs of type String / String[].
     * </p>
     * @param sortExpression the sort expression whose direction to change
     * @return the URL and data grid state needed to change the direction of a {@link Sort} with the given
     *         sort expression
     */
    public Map buildSortQueryParamsMap(String sortExpression) {

        SortModel sortModel = getDataGridState().getSortModel();
        SortStrategy sortStrategy = sortModel.getSortStrategy();

        List currSorts = sortModel.getSorts();
        ArrayList newSorts = new ArrayList();
        if(currSorts == null || currSorts.size() == 0) {
            Sort sort = new Sort();
            sort.setSortExpression(sortExpression);
            sort.setDirection(sortStrategy.getDefaultDirection());
            newSorts.add(sort);
        }
        else {
            boolean foundSort = false;
            for(int i = 0; i < currSorts.size(); i++) {
                Sort sort = (Sort)currSorts.get(i);
                if(!sort.getSortExpression().equals(sortExpression)) {
                    newSorts.add(sort);
                }
                else {
                    Sort newSort = new Sort();
                    newSort.setSortExpression(sortExpression);
                    newSort.setDirection(sortStrategy.nextDirection(sort.getDirection()));
                    newSorts.add(newSort);
                    foundSort = true;
                }
            }
            if(!foundSort) {
                Sort newSort = new Sort();
                newSort.setSortExpression(sortExpression);
                newSort.setDirection(sortStrategy.getDefaultDirection());
                newSorts.add(newSort);
            }
        }

        Map params = _codec.getExistingParams();
        Map newParams = new HashMap();

        Map tmp = _codec.buildSortParamMap(newSorts);
        if(tmp != null)
            newParams.putAll(tmp);

        addFilterParams(newParams);
        addPagerParams(newParams);

        params = mergeMaps(params, newParams);
        params = transformMap(params);

        return params;
    }

    private final DataGridState getDataGridState() {
        return _codec.getDataGridState();
    }

    private void addSortParams(Map map) {
        Map tmp = _codec.buildSortParamMap(getDataGridState().getSortModel().getSorts());
        if(tmp != null)
            map.putAll(tmp);
    }

    private void addFilterParams(Map map) {
        Map tmp = _codec.buildFilterParamMap(getDataGridState().getFilterModel().getFilters());
        if(tmp != null)
            map.putAll(tmp);
    }

    private void addPagerParams(Map map) {
        Map tmp = _codec.buildPageParamMap(new Integer(getDataGridState().getPagerModel().getRow()),
                new Integer(getDataGridState().getPagerModel().getPageSize()));
        if(tmp != null)
            map.putAll(tmp);
    }

    private Map mergeMaps(Map curr, Map merge) {
        Map newMap = new HashMap();
        if(curr != null)
            newMap.putAll(curr);

        Iterator iterator = merge.keySet().iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            assert merge.get(key) instanceof String[];
            String[] values = (String[])merge.get(key);
            if(newMap.containsKey(key)) {
                Object currValues = newMap.get(key);
                if(currValues instanceof List) {
                    List currList = (List)currValues;
                    for(int i = 0; i < values.length; i++) {
                        currList.add(values[i]);
                    }
                }
                else
                    throw new IllegalStateException(Bundle.getErrorString("DataGridURLBuilder_UnableToMergeValues",
                            new Object[]{currValues.getClass().getName()}));
            }
            else
                newMap.put(key, values);
        }
        return newMap;
    }

    private Map transformMap(Map map) {
        HashMap newMap = new HashMap();
        Iterator iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            Object values = map.get(key);
            if(values instanceof String[])
                newMap.put(key, values);
            else if(values instanceof List)
                newMap.put(key, ((List)values).toArray(new String[0]));
            else if(values == null)
                newMap.put(key, null);
            else
                assert false : "Found invalid type in map: " + values.getClass().getName();
        }

        return newMap;
    }
}
