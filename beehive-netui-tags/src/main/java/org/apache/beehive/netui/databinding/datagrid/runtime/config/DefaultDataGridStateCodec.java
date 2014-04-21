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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperation;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateCodec;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;
import org.apache.beehive.netui.databinding.datagrid.runtime.sql.SQLSupport;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;

/**
 * <p>
 * Derault implementation of the {@link DataGridStateCodec} abstract base class.  This class provides support
 * for obtaining a {@link DataGridState} object which contains "current" state for a data grid and will
 * be used when rendering a data grid.
 * </p>
 */
class DefaultDataGridStateCodec
    extends DataGridStateCodec {

    /* filter format: netui_filter=<namespace>;<fExpr>~<fOp>~<fVal>,<fExpr>~<fOp>~<fVal> */
    /* sort format:   netui_sort=<namespace>;<expr>,-<expr> */
    /* row format:    netui_row=<namespace>~<row> */
    /* page size format:  netui_pagesize=<namespace>~<pagesize> */

    static final String PARAM_KEY_FILTER = "netui_filter";
    static final String PARAM_KEY_SORT = "netui_sort";
    static final String PARAM_KEY_PAGE_SIZE = "netui_pagesize";
    static final String PARAM_KEY_ROW = "netui_row";

    private static final Logger LOGGER = Logger.getInstance(DefaultDataGridStateCodec.class);
    private static final String DELIM_GRID_NAME = ";";
    private static final String DELIM_SORT_TERM = ",";
    private static final String DELIM_FILTER_TERM = ",";
    private static final String DELIM_FILTER = "~";
    private static final String SORT_DESCENDING = "-";

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_ROW = 0;

    private boolean _decoded = false;

    /**
     * The ServletRequest needs to be processed such that the parameter values of
     * interest are removed from the query param map.  Then, the Map is stateless
     * relative to the state that the current data grid needs to add.
     */
    private HashMap _queryParams = null;
    private DataGridConfig _config = null;
    private DataGridState _state = null;
    private DefaultDataGridURLBuilder _urlBuilder = null;

    /**
     * Package protected constructor; this class should only be constructed via the {@link DefaultDataGridConfig}
     * class.
     * @param config the data grid config object used to manufacture a state object
     */
    DefaultDataGridStateCodec(DataGridConfig config) {
        super();
        _config = config;
        _state = _config.createDataGridState();
        _urlBuilder = new DefaultDataGridURLBuilder(this);
    }

    /**
     * Get the current {@link DataGridState}.
     * @return the data grid state
     */
    public DataGridState getDataGridState() {
        if(!_decoded) {
            decode(getServletRequest().getParameterMap());
        }
        return _state;
    }

    /**
     * Get the {@link DataGridURLBuilder} for this state codec.  The URL builder can be used to build
     * URLs managing the {@link DataGridState} obtainable via {@link #getDataGridState()}.
     * @return the data grid URL builder
     */
    public DataGridURLBuilder getDataGridURLBuilder() {
        return _urlBuilder;
    }

    /**
     * <p>
     * Set the {@link DataGridState} object.  This mechanism provides callers a way to explicitly set the
     * {@link DataGridState}.  This useful when a grid's state needs to be provided from an outside source
     * and attached so it is obtainable from the data grid via the usual mechanism.
     * </p>
     *
     * @param state the new data grid state
     */
    public void setDataGridState(DataGridState state) {
        _state = state;
    }

    /**
     * Returns the existing query parameters map. This is a clone that can be augmented by client code but the
     * existing parameters are not changed.
     *
     * @return
     */
    Map getExistingParams() {
        return _queryParams;
    }

    /**
     * Build the sort parameter map given this list of {@link Sort} instances.  Note, the query parameters returned
     * here are <b>not</b> URL encoded.  The map contains key / value pairs as (String, String[]).
     * @param sorts the sorts
     * @return a map containing the sort query parameters
     */
    Map buildSortParamMap(List sorts) {
        if(sorts == null || sorts.size() == 0)
            return null;

        String encoded = encodeSorts(sorts);
        if(encoded == null)
            return null;
        else {
            HashMap params = new HashMap();
            params.put(PARAM_KEY_SORT, new String[]{encoded});
            return params;
        }
    }

    /**
     * Build the filter parameter map given this list of {@link Filter} instances.  Note, the query parameters
     * returned here are <b>not</b> URL encoded.  The map contains key / value pairs as (String, String[]).
     * @param filters the filters
     * @return a map containing the filter query parameters
     */
    Map buildFilterParamMap(List filters) {
        if(filters == null || filters.size() == 0)
            return null;

        String encoded = encodeFilters(filters);
        if(encoded == null)
            return null;
        else {
            HashMap params = new HashMap();
            params.put(PARAM_KEY_FILTER, new String[]{encoded});
            return params;
        }
    }

    /**
     * Build the URL parameter map given a current row and page size.  Note, the query parameters returned
     * here are <b>not</b> URL encoded.  The map contains key / value pairs as (String, String[]).
     * @param row the current row
     * @param pageSize the current page size
     * @return a map containing the pager query parameters
     */
    Map buildPageParamMap(Integer row, Integer pageSize) {
        HashMap map = new HashMap();
        if(row != null && row.intValue() != DEFAULT_ROW)
            map.put(PARAM_KEY_ROW, new String[]{encodeRow(row.intValue())});

        /* only encode the page size if it is not equal to the default page size for this data grid
        
           for example, if a data grid's default page size is 20 but is set somehow by the application
           to be 50, the default will read 20 but overridden pageSize value should be encoded in the URL
         */
        if(pageSize != null && pageSize.intValue() != _state.getPagerModel().getDefaultPageSize())
            map.put(PARAM_KEY_PAGE_SIZE, new String[]{encodePageSize(pageSize.intValue())});

        return map;
    }

    /**
     * Decode a Map of URL parameters.  This method will convert a complete set of URL parameters into several
     * buckets including the sorts, filters, and paging information for the data grid name associated with this
     * state codec.  In addition, a bucket of 'other' parameters is also collected which are the ones that
     * were in the current request URL and should be maintained on all generated URLs.
     * @param parameters the list of parameters to decode
     */
    private void decode(Map parameters) {
        _decoded = true;

        String namespacePrefix = getGridName() + ";";

        Iterator keys = parameters.keySet().iterator();
        while(keys.hasNext()) {
            String key = (String)keys.next();
            String[] values = (String[])parameters.get(key);

            if(key.equals(PARAM_KEY_SORT)) {
                List sorts = null;
                for(int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if(value.startsWith(namespacePrefix))
                        sorts = decodeSort(value);
                    else
                        addParam(key, value);
                }
                SortModel sortModel = _config.createSortModel(sorts);
                _state.setSortModel(sortModel);
            }
            else if(key.equals(PARAM_KEY_FILTER)) {
                List filters = null;
                for(int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if(value.startsWith(namespacePrefix))
                        filters = decodeFilter(value);
                    else
                        addParam(key, value);
                }
                FilterModel filterModel = _config.createFilterModel(filters);
                _state.setFilterModel(filterModel);
            }
            else if(key.equals(PARAM_KEY_ROW)) {
                int row = DEFAULT_ROW;
                for(int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if(value.startsWith(namespacePrefix))
                        row = decodeRow(value).intValue();
                    else
                        addParam(key, value);
                }
                PagerModel pagerModel = _state.getPagerModel();
                if(pagerModel == null) {
                    pagerModel = _config.createPagerModel();
                    _state.setPagerModel(pagerModel);
                }
                pagerModel.setRow(row);
            }
            else if(key.equals(PARAM_KEY_PAGE_SIZE)) {
                int pageSize = DEFAULT_PAGE_SIZE;
                for(int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if(value.startsWith(namespacePrefix))
                        pageSize = decodeRow(value).intValue();
                    else
                        addParam(key, value);
                }
                PagerModel pagerModel = _state.getPagerModel();
                if(pagerModel == null) {
                    pagerModel = _config.createPagerModel();
                    _state.setPagerModel(pagerModel);
                }
                pagerModel.setPageSize(pageSize);
            }
            else if(key.startsWith(InternalConstants.ACTION_OVERRIDE_PREFIX)) {
                // discard the param
            }
            else
                addParam(key, values);
        }

        /* ensure that there is something created for the grid state model objects */
        if(_state.getSortModel() == null)
            _state.setSortModel(_config.createSortModel(null));
        if(_state.getFilterModel() == null)
            _state.setFilterModel(_config.createFilterModel(null));
        if(_state.getPagerModel() == null)
            _state.setPagerModel(_config.createPagerModel());
    }

    private void addParam(String key, String value) {
        if(_queryParams == null)
            _queryParams = new HashMap();

        ArrayList list = (ArrayList)_queryParams.get(key);
        if(list == null) {
            list = new ArrayList();
            _queryParams.put(key, list);
        }

        list.add(value);
    }

    private void addParam(String key, String[] values) {
        if(_queryParams == null)
            _queryParams = new HashMap();

        ArrayList list = (ArrayList)_queryParams.get(key);
        if(list == null) {
            list = new ArrayList();
            _queryParams.put(key, list);
        }

        for(int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
    }

    private int decodeInt(String value, int defaultValue) {
        int intValue = defaultValue;
        try {
            intValue = Integer.parseInt(value);
        }
        catch(NumberFormatException nfe) {
            LOGGER.error(Bundle.getErrorString("DataGridStateCodec_IllegalIntegerValue", new Object[]{value, nfe}));
        }
        return intValue;
    }

    /*
      Sort handling
     */
    private List decodeSort(String value) {
        ArrayList sorts = new ArrayList();

        String[] nameAndSorts = value.split(DELIM_GRID_NAME);
        if(nameAndSorts.length != 2)
            return null;

        String namespace = nameAndSorts[0];
        String[] sortStrings = nameAndSorts[1].split(DELIM_SORT_TERM);

        // find the list of sorted columns
        // two columns of the bugs grid would be sorted as:
        //
        // netui_sort=bugs~id,-priority
        for(int i = 0; i < sortStrings.length; i++) {
            String sort = sortStrings[i];
            SortDirection sortDirection = SortDirection.NONE;
            if(sort.startsWith("-"))
                sortDirection = SortDirection.DESCENDING;
            else
                sortDirection = SortDirection.ASCENDING;
            String sortExpression = (sortDirection == SortDirection.DESCENDING ? sort.substring(1) : sort);
            Sort gridSort = _config.createSort();
            gridSort.setSortExpression(sortExpression);
            gridSort.setDirection(sortDirection);
            sorts.add(gridSort);
        }

        return sorts;
    }

    String encodeSorts(List sorts) {
        boolean hasSorts = false;
        InternalStringBuilder sb = new InternalStringBuilder(16);
        sb.append(getGridName());
        sb.append(DELIM_GRID_NAME);
        for(int i = 0; i < sorts.size(); i++) {
            Sort sort = (Sort)sorts.get(i);

            if(sort.getDirection() == SortDirection.NONE)
                continue;

            if(hasSorts)
                sb.append(DELIM_SORT_TERM);
            else
                hasSorts = true;

            if(sort.getDirection() == SortDirection.DESCENDING)
                sb.append(SORT_DESCENDING);
            sb.append(sort.getSortExpression());
        }

        if(!hasSorts)
            return null;
        else
            return sb.toString();
    }

    /*
      Filter handling
     */
    private List decodeFilter(String value) {
        String[] nameAndFilters = value.split(DELIM_GRID_NAME);

        assert nameAndFilters.length == 2;

        String namespace = nameAndFilters[0];
        String[] filters = nameAndFilters[1].split(DELIM_FILTER_TERM);

        ArrayList/*<Filter>*/ gridFilters = new ArrayList/*<Filter>*/();
        for(int i = 0; i < filters.length; i++) {
            String[] terms = filters[i].split(DELIM_FILTER);
            Filter filter = null;

            if(terms.length == 2 && terms[1].equals("*"))
                continue;
            else if(terms.length == 3) {
                FilterOperation fOp = SQLSupport.mapFilterAbbreviationToOperation(terms[1]);
                filter = _config.createFilter();
                filter.setFilterExpression(terms[0]);
                filter.setOperation(fOp);
                filter.setValue(terms[2]);
            }
            else {
                LOGGER.error(Bundle.getErrorString("DataGridStateCodec_IllegalFilter", new Object[]{filter}));
                continue;
            }

            assert filter != null;
            gridFilters.add(filter);
        }
        return gridFilters;
    }

    String encodeFilters(List filters) {
        boolean hasFilters = false;
        InternalStringBuilder sb = new InternalStringBuilder();
        sb.append(getGridName());
        sb.append(DELIM_GRID_NAME);
        for(int i = 0; i < filters.size(); i++) {
            Filter filter = (Filter)filters.get(i);

            if(hasFilters)
                sb.append(DELIM_FILTER_TERM);

            sb.append(filter.getFilterExpression());
            sb.append(DELIM_FILTER);
            sb.append(filter.getOperation().getAbbreviation());
            sb.append(DELIM_FILTER);
            sb.append(filter.getValue());

            hasFilters = true;
        }

        return sb.toString();
    }

    /*
       Pager handling
     */
    private Integer decodeRow(final String page) {
        String[] terms = page.split(DELIM_GRID_NAME);

        /* todo: this is really an exception, not an assert */
        assert terms != null && terms.length == 2;
        String intString = terms[1];
        return new Integer(decodeInt(intString, DEFAULT_ROW));
    }

    String encodeRow(final int row) {
        InternalStringBuilder sb = new InternalStringBuilder(16);
        sb.append(getGridName());
        sb.append(DELIM_GRID_NAME);
        sb.append(row);
        return sb.toString();
    }

    private Integer decodePageSize(final String pageSize) {
        String[] terms = pageSize.split(DELIM_GRID_NAME);

        /* todo: this is really an exception, not an assert */
        assert terms != null && terms.length == 2;
        String intString = terms[1];
        return new Integer(decodeInt(intString, DEFAULT_PAGE_SIZE));
    }

    String encodePageSize(final int pageSize) {
        InternalStringBuilder sb = new InternalStringBuilder(16);
        sb.append(getGridName());
        sb.append(DELIM_GRID_NAME);
        sb.append(pageSize);
        return sb.toString();
    }
}
