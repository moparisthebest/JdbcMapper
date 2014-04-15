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

import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortStrategy;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.PagerRenderer;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridResourceProvider;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateCodec;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.pager.PreviousNextPagerRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.style.DefaultStyleModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.style.EmptyStyleModel;

/**
 * <p>
 * Default implementation of the {@link DataGridConfig} base class.  This class is used to provide concrete
 * implementations of state containers and service providers for the data grid.
 * </p>
 */
public class DefaultDataGridConfig
    extends DataGridConfig {

    private static final String STYLE_POLICY_NAME_EMPTY = "empty";
    private static final String STYLE_POLICY_NAME_DEFAULT = "default";
    private static final String STYLE_PREFIX_DEFAULT = "datagrid";

    private static final StyleModel DEFAULT_STYLE_POLICY = new DefaultStyleModel(STYLE_PREFIX_DEFAULT);
    private static final StyleModel EMPTY_STYLE_POLICY = new EmptyStyleModel();
    private static final SortStrategy SORT_STRATEGY = new DefaultSortStrategy();

    /**
     * Create a {@link DataGridState} object.  The default implementation returned is
     * {@link DataGridState}.
     * @return a data grid state implementation
     */
    public DataGridState createDataGridState() {
        return new DataGridState();
    }

    /**
     * Create a {@link Sort} object.  The default implementation returned is {@link Sort}.
     * @return a sort
     */
    public Sort createSort() {
        return new Sort();
    }

    /**
     * Create a {@link Filter} object.  The default implementation returned is {@link Filter}.
     * @return a filter
     */
    public Filter createFilter() {
        return new Filter();
    }

    /**
     * Create a {@link SortModel} object.  The default implementation returned is {@link SortModel} with a
     * {@link SortStrategy} of {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultSortStrategy}.
     * @param sorts the list of sorts for a data grid
     * @return a sort model
     */
    public SortModel createSortModel(List/*<Sort>*/ sorts) {
        SortModel sortModel = new SortModel(sorts);
        sortModel.setSortStrategy(SORT_STRATEGY);
        return sortModel;
    }

    /**
     * Create a {@link FilterModel} object.  The default implementation returned is {@link FilterModel}.
     * @param filters the list of filters for a data grid
     * @return a filter model
     */
    public FilterModel createFilterModel(List/*<Filter>*/ filters) {
        return new FilterModel(filters);
    }

    /**
     * Create a {@link PagerModel} object.  The default implementation returned is {@link PagerModel}.
     * @return a pager model
     */
    public PagerModel createPagerModel() {
        return new PagerModel();
    }

    /**
     * Create a {@link DataGridStateCodec} for a grid with the given name for the given {@link ServletRequest}.
     * @param request the current request
     * @param gridName a data grid's name
     * @return the state encoder / decoder for a data grid's request state
     */
    public DataGridStateCodec createStateCodec(ServletRequest request, String gridName) {
        DefaultDataGridStateCodec codec = new DefaultDataGridStateCodec(this);
        codec.setServletRequest(request);
        codec.setGridName(gridName);
        return codec;
    }

    /**
     * Get the default {@link PagerRenderer}.  The default pager renderer will display a pager with previous / next
     * page links via the implementation class {@link PreviousNextPagerRenderer}.
     * @return the pager renderer
     */
    public PagerRenderer getDefaultPagerRenderer() {
        return new PreviousNextPagerRenderer();
    }

    /**
     * Get a {@link DataGridResourceProvider}.  The default implementation class is
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridResourceProvider}
     * and provides a basic implementation that reads messages from the default .properties file.
     * @return the resource provider
     */
    public DataGridResourceProvider getDefaultResourceProvider() {
        return new DefaultDataGridResourceProvider();
    }

    /**
     * Get a {@link DataGridResourceProvider} for the given resource bundle path.  The default implementation
     * will set this resource bundle but does not enable message chaining.
     * @param resourceBundle a resource bundle specifically requested by a data grid
     * @return the resource provider
     */
    public DataGridResourceProvider getResourceProvider(String resourceBundle) {
        DataGridResourceProvider resourceProvider = new DefaultDataGridResourceProvider();
        resourceProvider.setResourceBundlePath(resourceBundle);
        return resourceProvider;
    }

    /**
     * Get a {@link StyleModel} given a model name and a style class prefix.  This class exposes two available
     * style names:
     * <table>
     * <tr><td>Name</td><td>Description</td><td>Implementation Class</td></tr>
     * <tr><td><code>empty</code></td>
     *     <td>Renders CSS style classes that are non-prefixed and generally empty.</td>
     *     <td>{@link EmptyStyleModel}</td>
     * </tr>
     * <tr><td><code>default</code></td>
     *     <td>Renders CSS style classes with names using a default prefix of <code>datagrid</code></td>
     *     <td>{@link DefaultStyleModel}</td>
     * </tr>
     * </table>
     * When using the <code>empty</code> style model, styles rendered on the &lt;table&gt; element will
     * be empty; the same style rendered wtih the <code>default</code> style model will render as
     * <code>class="datagrid"</code>.  If the style prefix "foo" is provided for the <code>default</code> style policy
     * the style name will be rendered as <code>class="foo"</code>.x
     * @param name the name of a {@link StyleModel} implementation to use
     * @param classPrefix the prefix for a style name
     * @return the style model
     */
    public StyleModel getStyleModel(String name, String classPrefix) {
        if(name == null || name.equals(STYLE_POLICY_NAME_DEFAULT)) {
            if(classPrefix != null)
                return new DefaultStyleModel(classPrefix);
            else return DEFAULT_STYLE_POLICY;
        }
        else if(name != null && name.equals(STYLE_POLICY_NAME_EMPTY))
            return EMPTY_STYLE_POLICY;
        else return DEFAULT_STYLE_POLICY;
    }
}
