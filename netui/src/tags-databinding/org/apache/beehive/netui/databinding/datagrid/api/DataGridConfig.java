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

import java.util.List;
import javax.servlet.ServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.PagerRenderer;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;

/**
 * <p>
 * Abstract class used by the data grid to create model objects used for state
 * management and rendering.  This abstraction exists in order to allow the data grid's
 * implementation to be configurable.  Various data grid implementation classes are exposed
 * here including sorting, filtering, and paging.  In addition, classes such as the
 * {@link PagerRenderer} and {@link StyleModel} are created by subclasses in order to provide
 * a data grid specific implementation of pager UI or style support.
 * </p>
 * <p>
 * By default, DataGridConfig implementations are not thread safe, but custom implementations are
 * free to store instance state as long as the DataGridConfig object lifetimes are managed
 * in user code.
 * </p>
 * <p>
 * A specific DataGridConfig object can be created and passed to a data grid via the
 * data grid's
 * {@link org.apache.beehive.netui.tags.databinding.datagrid.DataGrid#setDataGridConfig(DataGridConfig)}
 * method.
 * </p>
 */
public abstract class DataGridConfig
    implements java.io.Serializable {

    /**
     * <p>
     * Create a {@link DataGridState} instance that will be used to store the state for a data grid.  This
     * method will be called only when a DataGridState object needs to be manufactured by a data grid tag.
     * </p>
     *
     * @return the {@link DataGridState} object for a data grid.
     */
    public abstract DataGridState createDataGridState();

    /**
     * <p>
     * Create a concrete {@link Sort} implementation for a data grid.  When the sort state of a data grid
     * is created, this {@link Sort} instance will contain a single sort.
     * </p>
     * @return a {@link Sort} instance
     */
    public abstract Sort createSort();

    /**
     * <p>
     * Create a {@link SortModel} instance used to store the {@link Sort} state for a data grid.
     * </p>
     * @param sorts the current {@link List} of sorts for a data grid
     * @return the {@link SortModel}
     */
    public abstract SortModel createSortModel(List/*<Sort>*/ sorts);

    /**
     * <p>
     * Create a concrete {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} implementation for a data grid.  When the filter state of a data grid
     * is created, this {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} instance will contain a single filter.
     * </p>
     * @return a {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} instance
     */
    public abstract Filter createFilter();

    /**
     * <p>
     * Create a {@link FilterModel} instance used to store the {@link Filter} state for a data grid.
     * </p>
     * @param filters the current {@link List} of filters for a data grid
     * @return a {@link FilterModel}
     */
    public abstract FilterModel createFilterModel(List/*<Filter>*/ filters);

    /**
     * <p>
     * Create a {@link PagerModel} instance used to store the current paging state for a data grid.
     * </p>
     * @return a {@link PagerModel}
     */
    public abstract PagerModel createPagerModel();

    /**
     * <p>
     * Create a {@link DataGridStateCodec} instance used to obtain services for handling data grid state.
     * </p>
     * @param request the current {@link ServletRequest}
     * @param gridName the name of a data grid whose {@link DataGridStateCodec} to obtain
     * @return a {@link DataGridStateCodec}
     */
    public abstract DataGridStateCodec createStateCodec(ServletRequest request, String gridName);

    /**
     * <p>
     * Create the default {@link DataGridResourceProvider}.  A resource provider is an implementation of
     * the {@link DataGridResourceProvider} which is used during data grid rendering to obtain strings
     * for messages, paths, etc.  The default resource provider simply exposes the default data grid
     * messages stored in the properties file located at:
     * <pre>
     *   org.apache.beehive.netui.databinding.datagrid.runtime.util.data-grid-default.properties
     * </pre>
     * </p>
     * @return a {@link DataGridResourceProvider}
     */
    public abstract DataGridResourceProvider getDefaultResourceProvider();

    /**
     * <p>
     * Create a DataGridConfig-specific implementation of a {@link DataGridResourceProvider}.  The DataGridConfig
     * instance should use the provided resource bundle path to create a {@link DataGridResourceProvider} which
     * will expose those messages into the data grid for rendering. 
     * </p>
     * @param resourceBundle the resource bundle to use for grid messages
     * @return a {@link DataGridResourceProvider}
     */
    public abstract DataGridResourceProvider getResourceProvider(String resourceBundle);

    /**
     * <p>
     * Create a {@link StyleModel} used by a data grid to render styles onto various HTML markup elements
     * generated by a data grid.
     * </p>
     * @param name the name of a style type to support.  This name can vary by {@link DataGridConfig} implementation
     *             subclasses may use this value to configure the type of {@link StyleModel} returned to the caller.
     * @param classPrefix an optional prefix to use when generating style class names
     * @return the {@link StyleModel}
     */
    public abstract StyleModel getStyleModel(String name, String classPrefix);

    /**
     * <p>
     * Create a concrete {@link PagerRenderer} implementation that will be used to render the pager
     * in the absence of an alternate pager renderer.
     * </p>
     * @return the default {@link PagerRenderer} used to render a paging user interface
     */
    public abstract PagerRenderer getDefaultPagerRenderer();
}
