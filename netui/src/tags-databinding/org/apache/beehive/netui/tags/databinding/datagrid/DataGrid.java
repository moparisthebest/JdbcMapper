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
package org.apache.beehive.netui.tags.databinding.datagrid;

import java.util.Iterator;
import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfigFactory;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridResourceProvider;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.PagedDataSet;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.script.common.IDataAccessProvider;
import org.apache.beehive.netui.script.common.DataAccessProviderStack;
import org.apache.beehive.netui.tags.ExpressionHandling;
import org.apache.beehive.netui.tags.IBehaviorConsumer;
import org.apache.beehive.netui.tags.IHtmlI18n;
import org.apache.beehive.netui.tags.IHtmlEvents;
import org.apache.beehive.netui.tags.IHtmlCore;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.TableTag;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.iterator.IteratorFactory;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * This tag is the containing tag for all tags and markup used to render a data grid.  In its simplest form, a data
 * grid is an HTML table containing an HTML table row for every item in a data set.  The data grid also provides
 * functionality for rendering the following major regions:
 * <ul>
 * <li>a header -- a header contains the top-most rows in a data grid's HTML table and is rendered using
 *                 the {@link Header} tag</li>
 * <li>data rows -- markup rendered in the data grid for each record in the data set must be contained
 *                  in a {@link Rows} tag</li>
 * <li>a footer -- a footer contains the bottom-most rows in a data grid's HTML table and is rendered using the
 *                 {@link Footer} tag</li>
 * <li>caption -- an HTML table caption appears at the top of the table and is rendered using the
 *                {@link Caption} tag.</li>
 * </ul>
 * In addition, a data grid can also configure and render a pager using the {@link ConfigurePager} and
 * {@link RenderPager} tags respectively.
 * </p>
 * <p>
 * Inside of the {@link Header} and {@link Rows} rendering regions, the data grid renders HTML table cells.  The
 * data grid tag set provides a set of tags that can be used render these cells with varying content including:
 * <ul>
 * <li>HTML &lt;th&gt; cells -- these are generally used inside the {@link Header}</li>
 * <li>anchors -- these can be rendered using the {@link AnchorCell} tag</li>
 * <li>images -- these can be rendered using the {@link ImageCell} tag</li>
 * <li>image anchors-- these can be rendered using the {@link ImageAnchorCell} tag</li>
 * <li>HTML spans -- these can be rendered using the {@link SpanCell} tag</li>
 * </ul>
 * The {@link TemplateCell} tag can be used as a container for arbitrary content that may be included in a cell's
 * contents.  The {@link Footer} tag's content can also use these tags.
 * </p>
 * <p>
 * When the data grid renders its data set, the <code>container</code> JSP EL implicit object is exposed in the
 * JSP's {@link JspContext} and can be referenced using the <code>${contaimer}</code> JSP EL expression.  The
 * <i>current</i> item of data from the data set can be referenced using the <code>${container.item}</code>
 * expression.  If the item had a <code>name</code> property, it could be referenced as
 * <code>${container.item.name}</code>.  By default, the data grid renders a paged data set which will only
 * display a portion of the complete data set.  The default page size is {@link PagerModel#DEFAULT_PAGE_SIZE}
 * and can be changed by setting the {@link ConfigurePager#setPageSize(int)} attribute.
 * </p>
 * <p>
 * In addition to rendering a data grid, this tag set cooperates with a set of state management services exposed
 * via the {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory}.  These services
 * help to manage state related to paging, sorting, and filtering.  For example, the first row displayed
 * in the grid's current page and the sorts for a particular column of data are can be read / written using these
 * state objects.  The data grid will use various state information from these classes at reunder time.  For example,
 * when rendering a paged data set, the data grid will use the
 * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory} to obtain a {@link PagerModel}
 * which can be used to determine the current
 * {@link org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel#getRow()}.  The grid will then
 * use this row value to advance the grid to the appropriate page to display.
 * </p>
 * <p>
 * By default, the data grid uses a configuration JavaBean which provides instances of state containers and services
 * that are used to maintain state and render grid markup.  This config object is a subclass of
 * {@link DataGridConfig} and is obtained via the {@link DataGridConfigFactory}.  The default implementation is
 * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig}.  Page authors
 * may provide their own implementations of this object and set an instance via
 * {@link #setDataGridConfig(org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig)}.  This can be
 * used to change default behaviors, change the appearance of the pager, and change the messages displayed
 * during rendering among other things.
 * </p>
 * <p>
 * A simple, sortable, and pageable data grid that uses a first / previous // next / last pager might be
 * written as:
 * <pre>
 *     &lt;netui-data:dataGrid dataSource="pageScope.zooAnimals">
 *         &lt;netui-data:configurePager disableDefaultPager="true" pageAction="page" pagerFormat="firstPreviousNextLast"/>
 *         &lt;netui-data:caption>
 *             &lt;netui-data:renderPager/>
 *         &lt;netui-data:caption>
 *         &lt;netui-data:header>
 *             &lt;netui-data:heaederCell value="Animal" sortExpression="animal"/>
 *             &lt;netui-data:heaederCell value="Quantity" sortExpression="quantity"/>
 *             &lt;netui-data:heaederCell value="Details"/>
 *         &lt;/netui-data:header>
 *         &lt;netui-data:rows>
 *             &lt;netui-data:spanCell value="${container.item.animalName}"/>
 *             &lt;netui-data:spanCell value="${container.item.quantity}"/>
 *             &lt;netui-data:anchorCell action="details" value="Details">
 *                 &lt;netui:parameter name="animalId" value="${container.item.animalId}"/>
 *             &lt;/netui-data:anchorCell>
 *         &lt;/netui-data:rows>
 *     &lt;/netui-data:dataGrid>
 * </pre>
 * This data grid would render an HTML table with a &lt;caption&gt; that contains a first / previous // next / last
 * formated pager.  The data grid would display a page with ten data rows and three columns.  The header
 * contains the column titles with clickable sorting links for sorting by the animal name and quantity.  The
 * body of the data grid contains three cells per row containing two HTML &lt;span&gt; tags and an HTML anchor
 * which will navigate to a Page Flow action caclled <code>details</code> when clicked.
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * This tag is the containing tag for all tags and markup used to render a data grid.  In its simplest form, a data
 * grid is an HTML table containing an HTML table row for every item in a data set.  The data grid also provides
 * functionality for rendering the following major regions:
 * <ul>
 * <li>a header -- a header contains the top-most rows in a data grid's HTML table and is rendered using
 *                 the {@link Header} tag</li>
 * <li>data rows -- markup rendered in the data grid for each record in the data set must be contained
 *                  in a {@link Rows} tag</li>
 * <li>a footer -- a footer contains the bottom-most rows in a data grid's HTML table and is rendered using the
 *                 {@link Footer} tag</li>
 * <li>caption -- an HTML table caption appears at the top of the table and is rendered using the
 *                {@link Caption} tag.</li>
 * </ul>
 * In addition, a data grid can also configure and render a pager using the {@link ConfigurePager} and
 * {@link RenderPager} tags respectively.
 * </p>
 * <p>
 * Inside of the {@link Header} and {@link Rows} rendering regions, the data grid renders HTML table cells.  The
 * data grid tag set provides a set of tags that can be used render these cells with varying content including:
 * <ul>
 * <li>HTML &lt;th&gt; cells -- these are generally used inside the {@link Header}</li>
 * <li>anchors -- these can be rendered using the {@link AnchorCell} tag</li>
 * <li>images -- these can be rendered using the {@link ImageCell} tag</li>
 * <li>image anchors-- these can be rendered using the {@link ImageAnchorCell} tag</li>
 * <li>HTML spans -- these can be rendered using the {@link SpanCell} tag</li>
 * </ul>
 * The {@link TemplateCell} tag can be used as a container for arbitrary content that may be included in a cell's
 * contents.  The {@link Footer} tag's content can also use these tags.
 * </p>
 * <p>
 * When the data grid renders its data set, the <code>container</code> JSP EL implicit object is exposed in the
 * JSP's {@link JspContext} and can be referenced using the <code>${contaimer}</code> JSP EL expression.  The
 * <i>current</i> item of data from the data set can be referenced using the <code>${container.item}</code>
 * expression.  If the item had a <code>name</code> property, it could be referenced as
 * <code>${container.item.name}</code>.  By default, the data grid renders a paged data set which will only
 * display a portion of the complete data set.  The default page size is {@link PagerModel#DEFAULT_PAGE_SIZE}
 * and can be changed by setting the {@link ConfigurePager#setPageSize(int)} attribute.
 * </p>
 * <p>
 * In addition to rendering a data grid, this tag set cooperates with a set of state management services exposed
 * via the {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory}.  These services
 * help to manage state related to paging, sorting, and filtering.  For example, the first row displayed
 * in the grid's current page and the sorts for a particular column of data are can be read / written using these
 * state objects.  The data grid will use various state information from these classes at reunder time.  For example,
 * when rendering a paged data set, the data grid will use the
 * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory} to obtain a {@link PagerModel}
 * which can be used to determine the current
 * {@link org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel#getRow()}.  The grid will then
 * use this row value to advance the grid to the appropriate page to display.
 * </p>
 * <p>
 * By default, the data grid uses a configuration JavaBean which provides instances of state containers and services
 * that are used to maintain state and render grid markup.  This config object is a subclass of
 * {@link DataGridConfig} and is obtained via the {@link DataGridConfigFactory}.  The default implementation is
 * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig}.  Page authors
 * may provide their own implementations of this object and set an instance via
 * {@link #setDataGridConfig(org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig)}.  This can be
 * used to change default behaviors, change the appearance of the pager, and change the messages displayed
 * during rendering among other things.
 * </p>
 * <p>
 * A simple, sortable, and pageable data grid that uses a first / previous // next / last pager might be
 * written as:
 * <pre>
 *     &lt;netui-data:dataGrid dataSource="pageScope.zooAnimals">
 *         &lt;netui-data:configurePager disableDefaultPager="true" pageAction="page" pagerFormat="firstPreviousNextLast"/>
 *         &lt;netui-data:caption>
 *             &lt;netui-data:renderPager/>
 *         &lt;netui-data:caption>
 *         &lt;netui-data:header>
 *             &lt;netui-data:heaederCell value="Animal" sortExpression="animal"/>
 *             &lt;netui-data:heaederCell value="Quantity" sortExpression="quantity"/>
 *             &lt;netui-data:heaederCell value="Details"/>
 *         &lt;/netui-data:header>
 *         &lt;netui-data:rows>
 *             &lt;netui-data:spanCell value="${container.item.animalName}"/>
 *             &lt;netui-data:spanCell value="${container.item.quantity}"/>
 *             &lt;netui-data:anchorCell action="details" value="Details">
 *                 &lt;netui:parameter name="animalId" value="${container.item.animalId}"/>
 *             &lt;/netui-data:anchorCell>
 *         &lt;/netui-data:rows>
 *     &lt;/netui-data:dataGrid>
 * </pre>
 * This data grid would render an HTML table with a &lt;caption&gt; that contains a first / previous // next / last
 * formated pager.  The data grid would display a page with ten data rows and three columns.  The header
 * contains the column titles with clickable sorting links for sorting by the animal name and quantity.  The
 * body of the data grid contains three cells per row containing two HTML &lt;span&gt; tags and an HTML anchor
 * which will navigate to a Page Flow action caclled <code>details</code> when clicked.
 * </p>
 * @netui:tag name="dataGrid" body-content="scriptless"
 *            description="Containing tag for tags in the data grid tag set.
 *                         Renders a pageable, sortable, and filterable HTML table containing a data set"
 */
public class DataGrid
    extends AbstractDataGridHtmlTag
    implements IDataAccessProvider, IBehaviorConsumer, IHtmlCore, IHtmlEvents, IHtmlI18n {

    private static final String FACET_RESOURCE = "resource";

    private boolean _renderRowGroups = false;
    private String _name = null;
    private String _styleClassPrefix = null;
    private String _stylePolicyName = null;
    private String _dataSource = null;
    private String _resourceBundlePath = null;
    private DataGridConfig _dataGridConfig = null;
    private DataGridTagModel _dataGridTagModel = null;
    private TableTag.State _tableState = new TableTag.State();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "DataGrid";
    }

    /**
     * <p>
     * Set the {@link DataGridConfig} instance that this tag will use to create state containers and other
     * data grid objects used during rendering.  Custom implementations of this class can be provided
     * that will override the defaults set in the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig}.
     * </p>
     * @jsptagref.attributedescription
     * <p>
     * Set the {@link DataGridConfig} instance that this tag will use to create state containers and other
     * data grid objects used during rendering.  Custom implementations of this class can be provided
     * that will override the defaults set in the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig}.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>string_dataGridConfig</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The DataGridConfig instance used by the data grid to create state containers and objects for rendering"
     */
    public void setDataGridConfig(DataGridConfig dataGridConfig) {
        _dataGridConfig = dataGridConfig;
    }

    /**
     * Set the name of this data grid.  The name should be a simple String that is used to uniquely identify a data
     * grid inside of a JSP.  This value is also used to namespace state information in the URL that is scoped
     * to a data grid.  Within a given scope in a page, the page author is responsible for ensuring that this
     * name is unique.
     * @jsptagref.attributedescription
     * Set the name of this data grid.  The name should be a simple String that is used to uniquely identify a data
     * grid inside of a JSP.  This value is also used to namespace state information in the URL that is scoped
     * to a data grid.  Within a given scope in a page, the page author is responsible for ensuring that this
     * name is unique.
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true"
     *                  description="The name for a data grid"
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * <p>
     * Set the data source that references a data set to be rendered by the data grid.  The data source should be
     * a NetUI EL expression and generally looks like a JSP EL expression without the '${' and '}' characters.
     * For example, to reference an array of Employee objects exposed via a NetUI page input, the expression
     * might look like:
     * <pre>
     *     &lt;netui-data:dataGrid dataSource="pageInput.employeeArray" name="employeeGrid"&gt;
     * </pre>
     * This expression will be evaluated the data grid in order to obtain a reference to the data set.
     * </p>
     *
     * @jsptagref.attributedescription
     * <p>
     * Set the data source that references a data set to be rendered by the data grid.  The data source should be
     * a NetUI EL expression and generally looks like a JSP EL expression without the '${' and '}' characters.
     * For example, to reference an array of Employee objects exposed via a NetUI page input, the expression
     * might look like:
     * <pre>
     *     &lt;netui-data:dataGrid dataSource="pageInput.employeeArray" name="employeeGrid"&gt;
     * </pre>
     * This expression will be evaluated the data grid in order to obtain a reference to the data set.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>string_dataSource</i>
     * @netui:attribute required="true"
     *                  description="The <code>dataSource</code> attribute determines both
     *                               the source of populating data for the tag and the object to which the tag submits data."
     */
    public void setDataSource(String dataSource) {
        _dataSource = dataSource;
    }

    /**
     * <p>
     * Set the style class prefix used to namespace style class names rendered as attributes on HTML tags
     * generated by the data grid.  For example, when using the default style policy without setting this
     * attribute, the style rendered for the generated HTML table tag will be:
     * <pre>
     *   &lt;table class="datagrid"&gt;
     * </pre>
     * With the style class prefix of <code>foo</code>, the rendered HTML style class will be:
     * <pre>
     *   &lt;table class="foo"&gt;
     * </pre>
     * </p>
     * @jsptagref.attributedescription
     * <p>
     * Set the style class prefix used to namespace style class names rendered as attributes on HTML tags
     * generated by the data grid.  For example, when using the default style policy without setting this
     * attribute, the style rendered for the generated HTML table tag will be:
     * <pre>
     *   &lt;table class="datagrid"&gt;
     * </pre>
     * With the style class prefix of <code>foo</code>, the rendered HTML style class will be:
     * <pre>
     *   &lt;table class="foo"&gt;
     * </pre>
     * </p>
     * @jsptagref.attributesyntaxvalue <i>string_styleClassPrefix</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The style class prefix used when setting CSS style classes on HTML elements generated by the data grid."
     */
    public void setStyleClassPrefix(String styleClassPrefix) {
        _styleClassPrefix = styleClassPrefix;
    }

    /**
     * <p>
     * Set the resource bundle path used when getting messages from a {@link DataGridResourceProvider} during
     * data grid rendering.  The resource bundle provided here will entirely override messages obtained from
     * the {@link DataGridResourceProvider} and must include all message keys that are used for rendering.
     * In order to replace individual messages, use the behavior available from the
     * {@link #setBehavior(String, Object, String)} method.
     * </p>
     * @jsptagref.attributedescription
     * <p>
     * Set the resource bundle path used when getting messages from a {@link DataGridResourceProvider} during
     * data grid rendering.  The resource bundle provided here will entirely override messages obtained from
     * the {@link DataGridResourceProvider} and must include all message keys that are used for rendering.
     * In order to replace individual messages, use the behavior available from the
     * {@link #setBehavior(String, Object, String)} method.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>string_resourceBundlePath</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="A resource bundle path that can be used to replace the default strings rendered by a data grid"
     */
    public void setResourceBundlePath(String resourceBundlePath) {
        _resourceBundlePath = resourceBundlePath;
    }

    /**
     * <p>
     * Set the name of a CSS policy to use when rendering HTML elements in a data grid.  The data grid supports the
     * default style policy names defined here
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig#getStyleModel(String, String)}.
     * </p>
     * @jsptagref.attributedescription
     * <p>
     * Set the name of a CSS policy to use when rendering HTML elements in a data grid.  The data grid supports the
     * default style policy names defined here
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.config.DefaultDataGridConfig#getStyleModel(String, String)}.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>string_stylePolicy</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Set the name of a CSS policy used when rendering a data grid."
     */
    public void setStyleClassPolicy(String stylePolicy) {
        _stylePolicyName = stylePolicy;
    }

    /**
     * <p>
     * Sets a boolean that enables / disables rendering of HTML table row groups in the data grid.  When
     * row group rendering is enabled, the data grid tags will produce the thead, tbody, and tfoot HTML tags
     * via the {@link Header}, {@link Rows}, and {@link Footer} tags respectively.  In addition, as per the
     * <a href="http://www.w3.org/TR/REC-html40/struct/tables.html#h-11.2.3">HTML specification</a>, the data
     * grid will reorder the output of the row groups to in order to produce valid HTML.  When row group rendering
     * is enabled and a page is using JavaScript, the data grid <b>must</b> be nested inside of a NetUI
     * {@link org.apache.beehive.netui.tags.javascript.ScriptContainer} in order for JavaScript rendering
     * to be ordered correctly.  Legacy JavaScript script mode is not supported by the data grid.
     * </p>
     * @jsptagref.attributedescription
     * <p>
     * Sets a boolean that enables / disables rendering of HTML table row groups in the data grid.  When
     * row group rendering is enabled, the data grid tags will produce the thead, tbody, and tfoot HTML tags
     * via the {@link Header}, {@link Rows}, and {@link Footer} tags respectively.  In addition, as per the
     * <a href="http://www.w3.org/TR/REC-html40/struct/tables.html#h-11.2.3">HTML specification</a>, the data
     * grid will reorder the output of the row groups to in order to produce valid HTML.  When row group rendering
     * is enabled and a page is using JavaScript, the data grid <b>must</b> be nested inside of a NetUI
     * {@link org.apache.beehive.netui.tags.javascript.ScriptContainer} in order for JavaScript rendering
     * to be ordered correctly.  Legacy JavaScript script mode is not supported by the data grid.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>boolean_renderRowGroups</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Set a boolean flag for enabling / disabling row group rendering"
     */
    public void setRenderRowGroups(boolean renderRowGroups) {
        _renderRowGroups = renderRowGroups;
    }

    /**
     * Sets the onClick JavaScript event for the HTML table tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event for the HTML table tag."
     */
    public void setOnClick(String onClick) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event for the HTML tag."
     */
    public void setOnDblClick(String onDblClick) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event for the HTML tag."
     */
    public void setOnKeyDown(String onKeyDown) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event for the HTML tag."
     */
    public void setOnKeyUp(String onKeyUp) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event for the HTML tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event for the HTML tag."
     */
    public void setOnKeyPress(String onKeyPress) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event for the HTML tag."
     */
    public void setOnMouseDown(String onMouseDown) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event for the HTML tag."
     */
    public void setOnMouseUp(String onMouseUp) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event for the HTML tag."
     */
    public void setOnMouseMove(String onMouseMove) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event for the HTML tag."
     */
    public void setOnMouseOut(String onMouseOut) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML table tag..
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event for the HTML tag."
     */
    public void setOnMouseOver(String onMouseOver) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style for the HTML table tag.
     *
     * @param style the html style.
     * @jsptagref.attributedescription The style for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style for the HTML table tag"
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _tableState.style = style;
    }

    /**
     * Sets the style class for the HTML table tag.
     *
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the HTML table tag."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _tableState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML table tag.
     *
     * @param title the title
     * @jsptagref.attributedescription The title for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title for the HTML table tag."
     */
    public void setTitle(String title) {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the lang attribute for the HTML table tag.
     * @param lang the lang
     * @jsptagref.attributedescription The lang for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The lang for the HTML table tag."
     */
    public void setLang(String lang)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the dir attribute for the HTML table tag.
     * @param dir the dir
     * @jsptagref.attributedescription The dir for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The dir for the HTML table tag."
     */
    public void setDir(String dir)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Sets the summary attribute for the HTML table tag.
     * @param summary the summary
     * @jsptagref.attributedescription The summary for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_summary</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The summary for the HTML table tag."
     */
    public void setSummary(String summary)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.SUMMARY, summary);
    }

    /**
     * Sets the width attribute for the HTML table tag.
     * @param width the width
     * @jsptagref.attributedescription The width for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_width</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The width attribute for the HTML table tag."
     */
    public void setWidth(String width)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.WIDTH, width);
    }

    /**
     * Sets the border attribute for the HTML table tag.
     * @param border
     * @jsptagref.attributedescription The border attribute for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The border attribute for the HTML table tag."
     */
    public void setBorder(String border)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.BORDER, border);
    }

    /**
     * Sets the frame attribute for the HTML table tag.
     * @param frame the frame
     * @jsptagref.attributedescription The frame attribute for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_frame</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The frame for the HTML table tag."
     */
    public void setFrame(String frame)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.FRAME, frame);
    }

    /**
     * Sets the rules attribute for the HTML table tag.
     * @param rules the rules
     * @jsptagref.attributedescription The rules attribute for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_rules</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The rules attribute for the HTML table tag."
     */
    public void setRules(String rules)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.RULES, rules);
    }

    /**
     * Sets the cellspacing attribute for the HTML table tag.
     * @param cellspacing the cell spacing
     * @jsptagref.attributedescription The cellspacing for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellspacing</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cellspacing for the HTML table tag."
     */
    public void setCellspacing(String cellspacing)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CELLSPACING, cellspacing);
    }

    /**
     * Sets the cellpadding attribute for the HTML table tag.
     * @param cellpadding the cell padding
     * @jsptagref.attributedescription The cellpadding for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellpadding</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cellpadding for the HTML table tag."
     */
    public void setCellpadding(String cellpadding)
    {
        _tableState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CELLPADDING, cellpadding);
    }

    /**
     * Set the name of the tagId for the HTML table tag.
     *
     * @param tagId the the name of the tagId for the table tag.
     * @jsptagref.attributedescription The tagId for the HTML table tag.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
        throws JspException {
        applyTagId(_tableState, tagId);
    }

    /**
     * <p>
     * Implementation of the {@link IBehaviorConsumer} interface that extends the functionality of this
     * tag beyond that exposed via the JSP tag attributes.  This method accepts the following facets:
     * <table>
     * <tr><td>Facet Name</td><td>Operation</td></tr>
     * <tr><td><code>resource</code></td><td>Adds or overrides a data grid resource key with a new value.</td></tr>
     * </table>
     * A new resource key is added in order to override a value defined in
     * {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.IDataGridMessageKeys}.  When a message
     * is overridden or added here, the page author is able to override a single string resource such as a
     * pager mesage or sort href.
     * </p>
     * @param name the name of the behavior
     * @param value the value of the behavior
     * @param facet th ebehavior's facet
     * @throws JspException when the behavior's facet isnot recognized
    */
    public void setBehavior(String name, Object value, String facet)
            throws JspException {
        if(facet != null && facet.equals(FACET_RESOURCE)) {
            _dataGridTagModel.addResourceOverride(name, (value != null ? value.toString() : null));
        }
        else {
            String s = Bundle.getString("Tags_BehaviorFacetNotSupported", new Object[]{facet});
            throw new JspException(s);
        }
    }

    /**
     * <p>
     * Render a data grid.  This method implements the logic used to iterate through the data grid's rendering states
     * defined in {@link DataGridTagModel}.
     * </p>
     * @throws JspException when an error occurs evaluating the tag's body
     * @throws IOException when an error occurs writing to the output strema
     */
    public void doTag()
            throws JspException, IOException {

        // ensure the dataSource is a valid expression
        String dataSource = getDataSource();
        ExpressionHandling expr = new ExpressionHandling(this);
        String validExpr = expr.ensureValidExpression(dataSource, "dataSource", "DataSourceError");
        Object ds = expr.evaluateExpression(validExpr, "dataSource", getPageContext());
        Iterator iterator = IteratorFactory.createIterator(ds);

        JspContext jspContext = getJspContext();
        HttpServletRequest request = JspUtil.getRequest(jspContext);

        if(_dataGridConfig == null)
            _dataGridConfig = DataGridConfigFactory.getInstance();

        TableRenderer tableRenderer = new TableRenderer(request);
        PagedDataSet dataSet = new PagedDataSet(dataSource, iterator);

        StyleModel styleModel = _dataGridConfig.getStyleModel(_stylePolicyName, _styleClassPrefix);

        DataGridResourceProvider resourceProvider = null;
        if(_resourceBundlePath == null)
            resourceProvider = _dataGridConfig.getDefaultResourceProvider();
        else
            resourceProvider = _dataGridConfig.getResourceProvider(_resourceBundlePath);
        resourceProvider.setLocale(JspUtil.getLocale(jspContext));

        _dataGridTagModel = new DataGridTagModel(_name, _dataGridConfig, jspContext);
        _dataGridTagModel.setDataSet(dataSet);
        _dataGridTagModel.setStyleModel(styleModel);
        _dataGridTagModel.setTableRenderer(tableRenderer);
        _dataGridTagModel.setResourceProvider(resourceProvider);
        _dataGridTagModel.setRenderRowGroups(_renderRowGroups);

        JspFragment fragment = getJspBody();
        if(fragment != null) {
            String javascript = null;
            /* render any JavaScript needed to support framework features */
            if (_tableState.id != null) {
                javascript = renderNameAndId(request, _tableState, null);
            }

            boolean addedDataAccessProvider = false;
            try {
                InternalStringBuilder builder = new InternalStringBuilder(2048);
                AbstractRenderAppender appender = new StringBuilderRenderAppender(builder);

                /* todo: perf -- this doesn't need to happen when the data set is empty */
                DataAccessProviderStack.addDataAccessProvider(this, getJspContext());
                DataGridUtil.putDataGridTagModel(getJspContext(), _dataGridTagModel);
                addedDataAccessProvider = true;

                StringWriter sw = new StringWriter();
                /*
                   allow sub-tags to do work during START before rendering
                   this makes it possible to have tags out of order and to
                   have rendering work correctly
                 */
                /* todo: perf -- should you be able to turn this off for perf? */
                fragment.invoke(sw);

                /* todo: this needs to move into the DataGridTagModel */
                PagerModel pm = _dataGridTagModel.getState().getPagerModel();
                _dataGridTagModel.getDataSet().createWindow(pm.getRow(), pm.getPageSize());

                /* now that the model objects have been initialized, it's time to start rendering */
                _dataGridTagModel.changeRenderState(DataGridTagModel.RENDER_STATE_START);

                if(!_dataGridTagModel.isDisableDefaultPagerRendering())
                    _dataGridTagModel.renderPager(appender);

                _tableState.styleClass = styleModel.getTableClass();
                tableRenderer.openTable(_tableState, appender);

                /* render the caption */
                _dataGridTagModel.changeRenderState(DataGridTagModel.RENDER_STATE_CAPTION);
                sw = new StringWriter();
                fragment.invoke(sw);
                String caption = sw.toString();
                if(caption != null)
                    appender.append(caption);

                /* render the header */
                _dataGridTagModel.changeRenderState(DataGridTagModel.RENDER_STATE_HEADER);
                sw = new StringWriter();
                fragment.invoke(sw);
                String header = sw.toString();
                if(header != null)
                    appender.append(header);

                /* intermediate storage for the body and footer content
                   these are required by the HTML spec:
                       http://www.w3.org/TR/REC-html40/struct/tables.html#h-11.2.3
                   as when the row groups are used, they must be re-ordered so that
                   <tfoot> preceeds <tbody>
                 */

                String tbody = null;
                String tfoot = null;

                /* render the body */
                _dataGridTagModel.changeRenderState(DataGridTagModel.RENDER_STATE_GRID);
                sw = new StringWriter();
                fragment.invoke(sw);
                tbody = sw.toString();

                /* render the footer */
                _dataGridTagModel.changeRenderState(DataGridTagModel.RENDER_STATE_FOOTER);
                sw = new StringWriter();
                fragment.invoke(sw);
                String footer = sw.toString();
                String trimmed = footer.trim();
                if(footer != null && !trimmed.trim().equals(""))
                    tfoot = footer;

                if(_dataGridTagModel.isRenderRowGroups()) {
                    if(tfoot != null)
                        appender.append(tfoot);
                    appender.append(tbody);
                }
                else {
                    appender.append(tbody);
                    if(tfoot != null)
                        appender.append(tfoot);
                }

                tableRenderer.closeTable(appender);

                if(javascript != null)
                    appender.append(javascript);

                _dataGridTagModel.changeRenderState(DataGridTagModel.RENDER_STATE_END);

                write(builder.toString());
            }
            finally {
                if(addedDataAccessProvider) {
                    DataAccessProviderStack.removeDataAccessProvider(getJspContext());
                    DataGridUtil.removeDataGridTagModel(getJspContext());
                }
            }
        }
    }

    /* ===========================================================
     *
     * IDataAccessProvider implementation
     *
     * ===========================================================
     */

    /**
     * Get the index of the current item in the data set.  This is a zero-based absolute
     * index into the entire data set being rendered by the data grid.  This value
     * should only be data bound inside of the {@link Rows}.
     * @return the index of the current item
     */
    public int getCurrentIndex() {
        return _dataGridTagModel.getCurrentIndex();
    }

    /**
     * Get the current item in the data set.  As the data grid iterates over the data set, this
     * value will change to provide access to the current item in the data set.  This value
     * should only be data bound inside of the {@link Rows}.
     * @return the current item
     */
    public Object getCurrentItem() {
        return _dataGridTagModel.getCurrentItem();
    }

    /**
     * Get metadata for the current item.  This operation is unsupported on the data grid.
     * @return the metadata for the current item
     * @throws UnsupportedOperationException as this method is unsupported
     */
    public Object getCurrentMetadata() {
        throw new UnsupportedOperationException(Bundle.getErrorString("Tags_DataAccessProvider_metadataUnsupported",
                new Object[]{getTagName()}));
    }

    /**
     * Get the data source for the data grid.  This value returns a NetUI EL expression which can
     * be evaluated by the NetUI tag API.
     * @return the expression
     */
    public String getDataSource() {
        return "{" + _dataSource + "}";
    }

    /**
     * Get the parent data access provider.  This method requires access to the tag hierarchy and is not
     * usable across tag file or JSP include rendering boundaries.  The result of this method is used for
     * evaluating expressions of the form <code>${container.container}</code> where this tag's parent
     * repeating tag is referenced.
     * @return the parent data access provider
     */
    public IDataAccessProvider getProviderParent() {
        /* todo: support nested data grids.  this should be done via the stack of objects in the PageContext */
        return (IDataAccessProvider)SimpleTagSupport.findAncestorWithClass(this, IDataAccessProvider.class);
    }
}
