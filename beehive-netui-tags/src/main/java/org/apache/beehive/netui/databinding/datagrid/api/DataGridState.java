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
package org.apache.beehive.netui.databinding.datagrid.api;

import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;

/**
 * <p>
 * A class used to encapsulate the default state information for a data grid.  All grids are
 * capable of supporting sorting, filtering, and paging.  This class holds the JavaBean model
 * objects that maintain the sort, filter, and page state of a data grid.
 * </p>
 * <p>
 * Instances of DataGridState can live longer than a single request.  As a result, DataGridState
 * objects are serializable, so subclasses should avoid non-transient references to objects
 * like the {@link javax.servlet.http.HttpServletRequest}.
 * </p>
 */
public class DataGridState
    implements java.io.Serializable {

    private FilterModel _filterModel;
    private SortModel _sortModel;
    private PagerModel _pagerModel;

    /**
     * Get the {@link SortModel} for a data grid.
     *
     * @return the {@link SortModel}
     */
    public SortModel getSortModel() {
        return _sortModel;
    }

    /**
     * Set the {@link SortModel} for a data grid.
     *
     * @param sortModel the new {@link SortModel}
     */
    public void setSortModel(SortModel sortModel) {
        _sortModel = sortModel;
    }

    /**
     * Set the {@link FilterModel} for the data grid
     *
     * @return the {@link FilterModel}
     */
    public FilterModel getFilterModel() {
        return _filterModel;
    }

    /**
     * Set the {@link FilterModel} for the data grid
     *
     * @param filterModel the new {@link FilterModel}
     */
    public void setFilterModel(FilterModel filterModel) {
        _filterModel = filterModel;
    }

    /**
     * Get the {@link PagerModel} for the data grid
     *
     * @return the {@link PagerModel}
     */
    public PagerModel getPagerModel() {
        return _pagerModel;
    }

    /**
     * Set the {@link PagerModel} for the data grid
     *
     * @param pagerModel the new {@link PagerModel}
     */
    public void setPagerModel(PagerModel pagerModel) {
        _pagerModel = pagerModel;
    }
}
