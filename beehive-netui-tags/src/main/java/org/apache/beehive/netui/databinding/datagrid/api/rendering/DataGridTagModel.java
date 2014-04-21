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
package org.apache.beehive.netui.databinding.datagrid.api.rendering;

import java.util.HashMap;
import javax.servlet.jsp.JspContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.sort.SortDirection;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridResourceProvider;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridURLBuilder;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.PagedDataSet;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The DataGridTagModel is a JavaBean that is exposed by the NetUI data grid tag into
 * the {@link javax.servlet.jsp.PageContext} in a JSP.  This bean exposes state and services
 * which can be data bound by an expression language in a JSP.
 * </p>
 */
public class DataGridTagModel {

    /**
     * The render state used when the data grid performs its first pass through its tag body.
     */
    public static final int RENDER_STATE_START = 10;

    /**
     * The render state used when the data grid is rendering the caption tag.
     */
    public static final int RENDER_STATE_CAPTION = 20;

    /**
     * The render state used when the data grid is rendering the header tag.
     */
    public static final int RENDER_STATE_HEADER = 30;

    /**
     * The render state used when the data grid is rendering the data rows.
     */
    public static final int RENDER_STATE_GRID = 40;

    /**
     * The render state used when the data grid is rendering the footer.
     */
    public static final int RENDER_STATE_FOOTER = 50;

    /**
     * The render state used when the data grid has completed rendering.
     */
    public static final int RENDER_STATE_END = 60;

    private static final int RENDER_STATE_UNINIT = -1;

    private int _renderState = RENDER_STATE_UNINIT;
    private boolean _disableDefaultPagerRendering = false;
    private boolean _renderRowGroups = false;
    private String _name = null;

    private JspContext _jspContext = null;
    private HttpServletRequest _request = null;
    private StyleModel _styleModel = null;
    private PagerRenderer _pagerRenderer = null;
    private PagedDataSet _dataSet = null;
    private TableRenderer _tableRenderer = null;
    private DataGridResourceProvider _resourceProvider = null;
    private DataGridState _dataGridState = null;
    private DataGridURLBuilder _urlBuilder = null;
    private DataGridConfig _dataGridConfig = null;

    /**
     * Constructor used to build a DataGridTagModel.
     *
     * @param name the name of the data grid
     * @param dataGridConfig the {@link DataGridConfig} object for a data grid
     * @param jspContext the {@link JspContext} for the JSP in which the data grid's rendering started
     */
    public DataGridTagModel(String name, DataGridConfig dataGridConfig, JspContext jspContext) {
        super();

        _jspContext = jspContext;
        _request = JspUtil.getRequest(_jspContext);

        _name = name;
        _dataGridConfig = dataGridConfig;

        DataGridStateFactory stateFactory = DataGridStateFactory.getInstance(_jspContext);
        _dataGridState = stateFactory.getDataGridState(_name, _dataGridConfig);
        _urlBuilder = stateFactory.getDataGridURLBuilder(_name, _dataGridConfig);
    }

    /**
     * Accessor for the name of the data grid.
     *
     * @return the name of the data grid
     */
    public String getName() {
        return _name;
    }

    /**
     * Accessor for the {@link JspContext} for the JSP in which the grid started to render.
     *
     * @return the {@link JspContext}
     */
    public JspContext getJspContext() {
        return _jspContext;
    }

    /**
     * <p>
     * Accessor for the current render state.  This should be used by clients that need to
     * affect their behavior based on the data grid's current render state.  This value will
     * be one of:
     * <ul>
     * <li>{@link #RENDER_STATE_START}</li>
     * <li>{@link #RENDER_STATE_CAPTION}</li>
     * <li>{@link #RENDER_STATE_HEADER}</li>
     * <li>{@link #RENDER_STATE_GRID}</li>
     * <li>{@link #RENDER_STATE_FOOTER}</li>
     * <li>{@link #RENDER_STATE_END}</li>
     * </ul>
     * </p>
     *
     * @return the current render state
     */
    public int getRenderState() {
        return _renderState;
    }

    /**
     * <p>
     * Method that alters the data grid's current render to the new <code>renderState</code>.  If
     * the provided render state value is unknown, an {@link IllegalStateException} is thrown.  The
     * data grid cycles through its state in this order:
     * <ul>
     * <li>{@link #RENDER_STATE_START}</li>
     * <li>{@link #RENDER_STATE_CAPTION}</li>
     * <li>{@link #RENDER_STATE_HEADER}</li>
     * <li>{@link #RENDER_STATE_GRID}</li>
     * <li>{@link #RENDER_STATE_FOOTER}</li>
     * <li>{@link #RENDER_STATE_END}</li>
     * </ul>
     * </p>
     *
     * @param renderState the DataGridTagModel's new render state
     * @throws IllegalStateException if an invalid state is provided
     */
    public void changeRenderState(int renderState) {
        switch(_renderState) {
            case RENDER_STATE_UNINIT:
                _renderState = RENDER_STATE_START;
                break;
            case RENDER_STATE_START:
                _renderState = RENDER_STATE_CAPTION;
                break;
            case RENDER_STATE_CAPTION:
                _renderState = RENDER_STATE_HEADER;
                break;
            case RENDER_STATE_HEADER:
                _renderState = RENDER_STATE_GRID;
                break;
            case RENDER_STATE_GRID:
                _renderState = RENDER_STATE_FOOTER;
                break;
            case RENDER_STATE_FOOTER:
                _renderState = RENDER_STATE_END;
                break;
            default:
                throw new IllegalStateException(Bundle.getErrorString("DataGridTagModel_InvalidStateTransition"));
        }
    }

    /**
     * Accessor for the {@link PagedDataSet} that is used to render a data set in the grid.
     *
     * @return a {@link PagedDataSet} for the current data set
     */
    public PagedDataSet getDataSet() {
        return _dataSet;
    }

    /**
     * Setter for the {@link PagedDataSet} object.  In order to canonicalize the type used by
     * the data grid to manipulate the data set, the {@link PagedDataSet} is used to
     * navigate the data set.
     *
     * @param dataSet the data set
     */
    public void setDataSet(PagedDataSet dataSet) {
        /* todo: would be nice to address this side-effect outside of the setter */
        _dataSet = dataSet;
        _dataGridState.getPagerModel().setDataSetSize(_dataSet.getSize());
    }

    /**
     * Accessor for the {@link PagerRenderer}.  This is the {@link PagerRenderer} instance that
     * will be used to render the UI used to display the pager.
     *
     * @return the {@link PagerRenderer} for the data grid
     */
    public PagerRenderer getPagerRenderer() {
        if(_pagerRenderer == null)
            setPagerRenderer(_dataGridConfig.getDefaultPagerRenderer());

        return _pagerRenderer;
    }

    /**
     * Set the {@link PagerRenderer} used to render the paging UI for the data grid.
     *
     * @param pagerRenderer the {@link PagerRenderer} to use 
     */
    public void setPagerRenderer(PagerRenderer pagerRenderer) {
        /* todo: would be nice to address this side-effect outside of the setter */
        _pagerRenderer = pagerRenderer;
        _pagerRenderer.setDataGridTagModel(this);
    }

    /**
     * Get the {@link DataGridResourceProvider} used to provide string messages, paths, etc during
     * data grid rendering.
     *
     * @return the {@link DataGridResourceProvider}
     */
    public DataGridResourceProvider getResourceProvider() {
        return _resourceProvider;
    }

    /**
     * Set the {@link DataGridResourceProvider} used to render the data grid.
     * @param resourceProvider the new resource provider
     */
    public void setResourceProvider(DataGridResourceProvider resourceProvider) {
        _resourceProvider = resourceProvider;
    }

    /**
     * Check to see if the data grid will render its pager UI by default.  The location for the default UI
     * is controlled by the JSP tag doing the rendering.
     * @return <code>true</code> if default rendering is enabled; <code>false</code> otherwise.
     */
    public boolean isDisableDefaultPagerRendering() {
        return _disableDefaultPagerRendering;
    }

    /**
     * Set a boolean to enable or disable rendering the pager UI by default.  If <code>true</code>, the
     * data grid rendering tags will produce the pager markup in some default location.  If <code>false</code>
     * the default pager rendering will be disabled.  The default location is determined by the tags
     * doing the rendering.
     * @param disableDefaultPagerRendering boolean for enabling or disabling rendering the pager in the default location
     */
    public void setDisableDefaultPagerRendering(boolean disableDefaultPagerRendering) {
        _disableDefaultPagerRendering = disableDefaultPagerRendering;
    }

    /**
     * <p>
     * Get the flag for whether to render the data grid using HTML row groups.  Row groups include the HTML
     * <code>thead</code>, <code>tbody</code>, and <code>tfoot</code> tags.  If row group rendering is enabled,
     * the HTML produced by the data grid will be contained inside of these tags and rendered in the correct
     * order in the produced HTML.  More detail on row groups can be found
     * <a href="http://www.w3.org/TR/REC-html40/struct/tables.html#h-11.2.3">here</a>.
     * </p>
     * @return <code>true</code> if row groups will be rendered; <code>false</code> otherwise
     */
    public boolean isRenderRowGroups() {
        return _renderRowGroups;
    }

    /**
     * Set whether to render the data grid using HTML row groups.  For more detail, see {@link #isRenderRowGroups()}.
     *
     * @param renderRowGroups <code>true</code> if rendering row groups; <code>false</code> otherwise
     */
    public void setRenderRowGroups(boolean renderRowGroups) {
        _renderRowGroups = renderRowGroups;
    }

    /**
     * Get the instance of {@link TableRenderer} that is used to render HTML table markup for a data grid.
     * @return the {@link TableRenderer}
     */
    public TableRenderer getTableRenderer() {
        return _tableRenderer;
    }

    /**
     * Set the {@link TableRenderer} used to render HTML table markup for a data grid.
     * @param tableRenderer the {@link TableRenderer} to use for rendering
     */
    public void setTableRenderer(TableRenderer tableRenderer) {
        _tableRenderer = tableRenderer;
    }

    /**
     * Get the {@link StyleModel} used to create style classes during data grid rendering.
     * @return the {@link StyleModel}
     */
    public StyleModel getStyleModel() {
        return _styleModel;
    }

    /**
     * Set the {@link StyleModel} used to create style classes during data grid rendering.
     * @param styleModel the {@link StyleModel}
     */
    public void setStyleModel(StyleModel styleModel) {
        _styleModel = styleModel;
    }

    /**
     * Get a message given a resource string name <code>key</code>.
     * @param key the message key
     * @return the value of the message
     */
    public String getMessage(String key) {
        assert _resourceProvider != null : "Received a null resource provider";
        return _resourceProvider.getMessage(key);
    }

    /**
     * Format a message given a resource string name <code>key</code> and a set of
     * formatting arguments <code>args</code>.
     * @param key the message key
     * @param args the arguments used when formatting the message
     * @return the formatted message
     */
    public String formatMessage(String key, Object[] args) {
        assert _resourceProvider != null : "Received a null resource provider";
        return _resourceProvider.formatMessage(key, args);
    }

    /**
     * <p>
     * This method provides support for overriding the messages available in the {@link DataGridResourceProvider} on a
     * per-message basis.  The key and value parameters here will override (or add) a message available via
     * the {@link DataGridResourceProvider} without requiring an entire Java properties file or custom
     * {@link DataGridResourceProvider} implementation.
     * </p>
     * @param key the key of the message to override
     * @param value the new value for the message key
     */
    public void addResourceOverride(String key, String value) {
        OverridableDataGridResourceProvider overrideResourceProvider = null;
        if(!(_resourceProvider instanceof OverridableDataGridResourceProvider)) {
            overrideResourceProvider = new OverridableDataGridResourceProvider(_resourceProvider);
            _resourceProvider = overrideResourceProvider;
        }
        else {
            assert _resourceProvider instanceof OverridableDataGridResourceProvider;
            overrideResourceProvider = (OverridableDataGridResourceProvider)_resourceProvider;
        }

        overrideResourceProvider.addResourceOverride(key, value);
    }

    /**
     * <p>
     * Get the resourrce path used when creating HTML image links during data grid rendering.  The value of the
     * default resource path is the {@link javax.servlet.http.HttpServletRequest#getContextPath()} combined
     * with the value of the data grid message stringn obtained with the key {@link IDataGridMessageKeys#DATAGRID_RESOURCE_PATH}.
     * </p>
     * @return the string resource path
     */
    public String getResourcePath() {
        /* todo: fix the message here to format with the context path */
        return _request.getContextPath() + "/" + getMessage(IDataGridMessageKeys.DATAGRID_RESOURCE_PATH);
    }

    /**
     * <p>
     * Get the image paths used for the given {@link SortDirection}.  The image paths are discovered
     * by using the following mapping.
     * <br/>
     * <table>
     * <tr><td>Sort direction</td><td>Message key</td></tr>
     * <tr><td><code>{@link SortDirection#ASCENDING}</code></td><td><code>{@link IDataGridMessageKeys#SORT_ASC_IMAGE_PATH}</code></td></tr>
     * <tr><td><code>{@link SortDirection#DESCENDING}</code></td><td><code>{@link IDataGridMessageKeys#SORT_DESC_IMAGE_PATH}</code></td></tr>
     * <tr><td><code>{@link SortDirection#NONE}</code></td><td><code>{@link IDataGridMessageKeys#SORT_NONE_IMAGE_PATH}</code></td></tr>
     * </table>
     * The value for the message is obtained by looking up a value in the {@link DataGridResourceProvider} obtained
     * via {@link #getResourceProvider()} using the message key in the table above.
     * </p>
     * @param sortDirection the {@link SortDirection} used to lookup an image path
     * @return the string image used to represent a sort direction graphically
     */
    public String getSortImagePath(SortDirection sortDirection) {
        /* todo: move to the DataGridConfig object */
        if(sortDirection == SortDirection.ASCENDING)
            return getMessage(IDataGridMessageKeys.SORT_ASC_IMAGE_PATH);
        else if(sortDirection == SortDirection.DESCENDING)
            return getMessage(IDataGridMessageKeys.SORT_DESC_IMAGE_PATH);
        else {
            assert sortDirection == SortDirection.NONE : "Encountered an invalid sort direction.";
            return getDefaultSortImagePath();
        }
    }

    /**
     * Get the default image path used when constructing links to sort images.  This value
     * is taken from the resource String available via the {@link DataGridResourceProvider}
     * obtained using {@link #getResourceProvider()} using the {@link IDataGridMessageKeys#SORT_NONE_IMAGE_PATH} key.
     * @return the String path
     */
    public String getDefaultSortImagePath() {
        return getMessage(IDataGridMessageKeys.SORT_NONE_IMAGE_PATH);
    }

    /**
     * Method used to render the data grid's pager UI into the given {@link AbstractRenderAppender}.
     *
     * @param appender the {@link AbstractRenderAppender} into which the pager will be rendered
     */
    public void renderPager(AbstractRenderAppender appender) {
        if(getPagerRenderer() != null)
            appender.append(getPagerRenderer().render());
    }

    /**
     * Accessor for obtaining the {@link DataGridState} object.  This is a JavaBean
     * property that can be accessed via an expression language in order to obtain
     * access to the state information for the data grid stored in the returned object.
     *
     * @return the data grid's {@link DataGridState}
     */
    public DataGridState getState() {
        return _dataGridState;
    }

    /**
     * Accessor for obtaining the {@link DataGridURLBuilder} object.  This is a JavaBean
     * that can be accessed via an expression language in order to obtain access to the
     * URL information for the data grid stored in the returned object.
     *
     * @return the data grid's {@link DataGridURLBuilder}
     */
    public DataGridURLBuilder getUrlBuilder() {
        return _urlBuilder;
    }

    /**
     * Accessor for obtaining the current index in the data set.  This value is a zero
     * based count current item being rendered.  For the array {"foo", "bar", "baz"}, the
     * indices for each item would be 0, 1, and 2.  This value does correspond to the
     * index into an Object array or a list, but in an arbitrary Collection, the index
     * is simply the number of items that appeared in the Collection before the current one.
     *
     * @return the current index
     */
    public int getCurrentIndex() {
        assert _dataSet != null;
        return _dataSet.getCurrentIndex();
    }

    /**
     * Accessor for obtaining the current item in the data set.
     *
     * @return the current item in the data set
     */
    public Object getCurrentItem() {
        assert _dataSet != null;
        return _dataSet.getCurrentItem();
    }

    /**
     * Accessor for obtaining the data source expression that was used to data bind to the data set.
     *
     * @return the String for the data source
     */
    public String getDataSource() {
        assert _dataSet != null;
        return _dataSet.getDataSource();
    }

    /**
     * Accessor for getting the next item in the data set.
     *
     * @return the next item in the data set.  Note, depending on the data set, this item could
     * be <code>null</code>.
     */
    public Object nextDataItem() {
        assert _dataSet != null;
        return _dataSet.next();
    }

    /**
     * Accessor for determining if there is another item in the data set.
     *
     * @return <code>true</code> if there is a next item; <code>false</code> otherwise.
     */
    public boolean hasNextDataItem() {
        assert _dataSet != null;
        return _dataSet.hasNext();
    }

    /* -------------------------------------------------------------

       Implementation specifics

       ------------------------------------------------------------- */

    /**
     * <p>
     * An internal class that represents a {@link DataGridResourceProvider} that supports
     * property-by-property overrides.  A JSP is able to specify message keys whose values
     * to override; in situations where this functionality is used, this class provides
     * the {@link DataGridResourceProvider} abstraction that implements this override
     * functionality.
     * </p>
     */
    private final class OverridableDataGridResourceProvider
            extends DataGridResourceProvider {

        private DataGridResourceProvider _delegate = null;
        private HashMap/*<String, String>*/ _resourceOverrides = null;

        private OverridableDataGridResourceProvider(DataGridResourceProvider resourceProvider) {
            _delegate = resourceProvider;
        }

        private void addResourceOverride(String key, String value) {
            if(_resourceOverrides == null)
                _resourceOverrides = new HashMap/*<String, String>*/();

            /* todo: could consider asserting that this key is known by the data grid framework */
            _resourceOverrides.put(key, value);
        }

        public String getMessage(String key) {
            String msg = internalGetMessage(key);
            if(msg != null)
                return msg;
            else
                return _delegate.getMessage(key);
        }

        public String formatMessage(String key, Object[] args) {
            String pattern = internalGetMessage(key);
            if(pattern != null)
                return internalFormatMessage(pattern, args);
            else
                return _delegate.formatMessage(key, args);
        }

        private final String internalGetMessage(String key) {
            if(_resourceOverrides != null && _resourceOverrides.containsKey(key))
                return (String)_resourceOverrides.get(key);
            else
                return null;
        }
    }
}
