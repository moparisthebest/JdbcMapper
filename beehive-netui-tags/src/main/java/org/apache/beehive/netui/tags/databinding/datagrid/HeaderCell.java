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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.tags.IBehaviorConsumer;
import org.apache.beehive.netui.tags.IAttributeConsumer;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.html.IFormattable;
import org.apache.beehive.netui.tags.rendering.ThTag;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.HeaderCellModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell.HeaderCellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell.SortedCellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.ExtensionUtil;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * This tag is used to render an HTML table header cell and should generally be used inside of the data grid's
 * {@link Header} tag.  Cells rendered with this tag usually appear at the top of a column of data displayed
 * in a grid.  Content for the body of a table header cell can be specified in one of two ways:
 * <ul>
 * <li>from the {@link #setHeaderText(String)} attribute</li>
 * <li>from the tag's body</li>
 * </ul>
 * This tag can accept in its body any content which is allowable inside of a JSP {@link javax.servlet.jsp.tagext.SimpleTag}.
 * If the header text attribute is provided, it will supercede the content rendered by the body, though the body
 * will still be evaluated.
 * </p>
 * <p>
 * The header cell is also able to display UI for showing and changing the sort and filter state of a data grid.
 * By default, this tag renders a clickable arrow indicating the sort state and allow ing a page user to change
 * the sort state.  The state of a sort is usually associated with the data displayed in a column though the
 * data in the column and the expression used to sort the data are loosely coupled via a 'sort expression'.  The
 * sort expression text is used to lookup the {@link org.apache.beehive.netui.databinding.datagrid.api.sort.Sort} state
 * from the {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridState} associated with the containing
 * data grid.  If sorts are found, this tag will render the appropriate UI for the sort.  Only a single sort
 * may be associated with the sort expression.  The clickable link rendered for changing the sort state is built
 * using either the {@link #setSortHref(String)} or the {@link #setSortAction(String)} attributes.  Sort UI rendering
 * can be disabled using the {@link #setDisableSortRendering(boolean)} attribute.  Custom UI can be rendered
 * for changing or displaying the sort state by providing a body for this tag and using the JSP EL data bindable
 * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridState} object available via the data
 * grid's <code>dataGrid</code> implicit object.
 * </p>
 * <p>
 * The header cell provides attributes for supporting authoring of filter UI, but this tag does not by default
 * implement a filter UI.  The attributes are provided here so that their values can be data bound when building
 * custom filter UI.
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGrid</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * <li><code>cellModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel}
 * containing state for the attributes of this tag.</li>
 * </ul>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * This tag is used to render an HTML table header cell and should generally be used inside of the data grid's
 * {@link Header} tag.  Cells rendered with this tag usually appear at the top of a column of data displayed
 * in a grid.  Content for the body of a table header cell can be specified in one of two ways:
 * <ul>
 * <li>from the {@link #setHeaderText(String)} attribute</li>
 * <li>from the tag's body</li>
 * </ul>
 * This tag can accept in its body any content which is allowable inside of a JSP {@link javax.servlet.jsp.tagext.SimpleTag}.
 * If the header text attribute is provided, it will supercede the content rendered by the body, though the body
 * will still be evaluated.
 * </p>
 * <p>
 * The header cell is also able to display UI for showing and changing the sort and filter state of a data grid.
 * By default, this tag renders a clickable arrow indicating the sort state and allow ing a page user to change
 * the sort state.  The state of a sort is usually associated with the data displayed in a column though the
 * data in the column and the expression used to sort the data are loosely coupled via a 'sort expression'.  The
 * sort expression text is used to lookup the {@link org.apache.beehive.netui.databinding.datagrid.api.sort.Sort} state
 * from the {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridState} associated with the containing
 * data grid.  If sorts are found, this tag will render the appropriate UI for the sort.  Only a single sort
 * may be associated with the sort expression.  The clickable link rendered for changing the sort state is built
 * using either the {@link #setSortHref(String)} or the {@link #setSortAction(String)} attributes.  Sort UI rendering
 * can be disabled using the {@link #setDisableSortRendering(boolean)} attribute.  Custom UI can be rendered
 * for changing or displaying the sort state by providing a body for this tag and using the JSP EL data bindable
 * {@link org.apache.beehive.netui.databinding.datagrid.api.DataGridState} object available via the data
 * grid's <code>dataGrid</code> implicit object.
 * </p>
 * <p>
 * The header cell provides attributes for supporting authoring of filter UI, but this tag does not by default
 * implement a filter UI.  The attributes are provided here so that their values can be data bound when building
 * custom filter UI.
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGrid</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * <li><code>cellModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel}
 * containing state for the attributes of this tag.</li>
 * </ul>
 * </p>
 * @netui:tag name="headerCell" body-content="scriptless"
 *            description="Render an HTML table head cell inside of a data grid"
 */
public class HeaderCell
    extends AbstractCell
    implements IFormattable, IBehaviorConsumer, IAttributeConsumer {

    private static final String TAG_NAME = "HeaderCell";
    private static final String ATTRIBUTE_HEADER_NAME = "header";
    private static final String BEHAVIOR_RENDERER_NAME = "renderer";
    private static final String BEHAVIOR_RENDERER_NAME_DEFAULT = "default";
    private static final String BEHAVIOR_RENDERER_NAME_SORT = "sort";
    private static final String BEHAVIOR_RENDERER_NAME_EXTENDS = "extends";

    private static final CellDecorator DECORATOR_HEADER_DEFAULT;
    private static final CellDecorator DECORATOR_HEADER_SORTED;

    static {
        DECORATOR_HEADER_DEFAULT = new HeaderCellDecorator();
        DECORATOR_HEADER_SORTED = new SortedCellDecorator(DECORATOR_HEADER_DEFAULT);
    }

    private Object _headerValue = null;
    private ThTag.State _cellState = new ThTag.State();
    private HeaderCellModel _headerCellModel = new HeaderCellModel();
    private CellDecorator _cellDecorator = null;

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return TAG_NAME;
    }

    /**
     * Sets the String text to render for this table header cell.  This text will be rendered inside of the
     * &lt;th&gt; tags.
     *
     * @param headerText The text to render.
     * @deprecated
     * @jsptagref.attributedescription
     * Sets the text to render for this table header cell.  This text will be rendered inside of the
     * &lt;th&gt; tags.
     * @jsptagref.attributesyntaxvalue <i>string_headerText</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The text to render inside of the HTML th tag."
     */
    public void setHeaderText(String headerText) {
        _headerValue = headerText;
    }

    /**
     * Sets the Object value to render for this table header cell.  This text will be rendered inside of the
     * &lt;th&gt; tags.  This value will be formatted when formatters are applied to header cells.
     *
     * @param value The text to format and render
     * @deprecated
     * @jsptagref.attributedescription
     * Sets the value to format and render for this table header cell.
     * @jsptagref.attributesyntaxvalue <i>object_headerText</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The value to format and render inside of the HTML th tag."
     */
    public void setValue(Object value) {
        _headerValue = value;
    }

    /**
     * Sets the scopeId for any anchors rendered by this header cell.
     *
     * @param scopeId
     * @jsptagref.attributedescription
     * Sets the scopeId for any anchors rendered by this header cell.
     * @jsptagref.attributesyntaxvalue <i>string_scopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Optional String scopeId that qualifies URL parameters into a particular scope"
     */
    public void setScopeId(String scopeId) {
        _headerCellModel.setScopeId(scopeId);
    }

    /**
     * Set a boolean which can be used to enable / disable rendering of UI associated with filtering inside
     * of the data grid.
     * @param disableFilterRendering boolean to enable / disable filter UI rendering
     * @jsptagref.attributedescription
     * Set a boolean which can be used to enable / disable rendering of UI associated with filtering inside
     * of the data grid.
     * @jsptagref.attributesyntaxvalue <i>string_disableFilterRendering</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Boolean to enable / disable filter UI rendering"
     */
    public void setDisableFilterRendering(boolean disableFilterRendering) {
        _headerCellModel.setDisableFilterRendering(disableFilterRendering);
    }

    /**
     * Set the filter expression for this header cell.  The value of this attribute is used to lookup
     * {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} state for a data grid
     * If filters are found matching this filter expression, the column is said to be 'filtered'.  When
     * a column is filtered, it is possible for the header cell UI to change to allow changing or
     * removing the filter.  By default, no UI is rendered for filtered columns.  To have data cells
     * rendered with filter information, this filter expression value should be set on the
     * {@link AbstractHtmlTableCell#setFilterExpression(String)} attribute for cell tags rendered
     * inside of the {@link Rows} tag.
     *
     * @param filterExpression the filter expression
     * @jsptagref.attributedescription
     * Set the filter expression for this header cell.  The value of this attribute is used to lookup
     * {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} state for a data grid
     * If filters are found matching this filter expression, the column is said to be 'filtered'.  When
     * a column is filtered, it is possible for the header cell UI to change to allow changing or
     * removing the filter.  By default, no UI is rendered for filtered columns.  To have data cells
     * rendered with filter information, this filter expression value should be set on the
     * {@link AbstractHtmlTableCell#setFilterExpression(String)} attribute for cell tags rendered
     * inside of the {@link Rows} tag.
     * @jsptagref.attributesyntaxvalue <i>string_filterExpression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="String filter expression that is used to lookup a data grid's Filter state"
     */
    public void setFilterExpression(String filterExpression) {
        _headerCellModel.setFilterExpression(filterExpression);
    }

    /**
     * Set the name of an action used to perform filtering on a data grid.  This action must exist in the context
     * of the current Page Flow.  It is used to build anchor URIs that when clicked change or display UI that allows
     * for change to the cell's filter state.  Only one of this and the {@link #setFilterHref(String)} may be
     * set on this tag.
     * @param filterAction the filter action
     * @jsptagref.attributedescription
     * Set the name of an action used to perform filtering on a data grid.  This action must exist in the context
     * of the current Page Flow.  It is used to build anchor URIs that when clicked change or display UI that allows
     * for change to the cell's filter state.  Only one of this and the {@link #setFilterHref(String)} may be
     * set on this tag.
     * @jsptagref.attributesyntaxvalue <i>string_filterAction</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The action used to build URIs used to change the filter state of this cell"
     */
    public void setFilterAction(String filterAction) {
        _headerCellModel.setFilterAction(filterAction);
    }

    /**
     * Set an href used to build URIs to change the filter state for this header cell.  Only one of this and
     * the {@link #setFilterAction(String)} attribute may be set on this tag.
     * @param filterHref the filter href
     * @jsptagref.attributedescription
     * Set an href used to build URIs to change the filter state for this header cell.  Only one of this and
     * the {@link #setFilterAction(String)} attribute may be set on this tag.
     * @jsptagref.attributesyntaxvalue <i>string_filterHref</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The href to use when performing a filter on this column"
     */
    public void setFilterHref(String filterHref) {
        _headerCellModel.setFilterHref(filterHref);
    }

    /**
     * Set a boolean which can be used to enable / disable rendering of UI associated with sorting inside
     * of the data grid.
     * @param disableSortRendering boolean to enable / disable sort UI rendering
     * @jsptagref.attributedescription
     * Set a boolean which can be used to enable / disable rendering of UI associated with sorting inside
     * of the data grid.
     * @jsptagref.attributesyntaxvalue <i>string_disableSortRendering</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Boolean to enable / disable sort UI rendering"
     */
    public void setDisableSortRendering(boolean disableSortRendering) {
        _headerCellModel.setDisableSortRendering(disableSortRendering);
    }

    /**
     * Set the sort expression for this header cell.  The value of this attribute is used to lookup
     * {@link org.apache.beehive.netui.databinding.datagrid.api.sort.Sort} state for a data grid
     * If sorts are found matching this filter expression, the column is said to be 'sorted'.  When
     * a column is sorted, it is possible for the header cell UI to change to allow changing or
     * removing the filter.  By default, the UI for sorting is a clickable arrow that indicates whether
     * a column of data is sorted ascending, descending, or not at all.  To have data cells
     * rendered with sort information, this sort expression value should be set on the
     * {@link AbstractHtmlTableCell#setSortExpression(String)} attribute for cell tags rendered
     * inside of the {@link Rows} tag.
     *
     * @param sortExpression the sort expression
     * @jsptagref.attributedescription
     * @jsptagref.attributesyntaxvalue <i>string_sortExpression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="String sort expression that is used to lookup a data grid's Sort state"
     */
    public void setSortExpression(String sortExpression) {
        _headerCellModel.setSortExpression(sortExpression);
    }

    /**
     * Set the name of an action used to perform sorting on a data grid.  This action must exist in the context
     * of the current Page Flow.  It is used to build anchor URIs that when clicked change or display UI that allows
     * for change to the cell's sort state.  Only one of this and the {@link #setSortHref(String)} may be
     * set on this tag.
     * @param sortAction the sort action
     * @jsptagref.attributedescription
     * @jsptagref.attributesyntaxvalue <i>string_sortAction</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The action used to build URIs used to change the sort state of this cell"
     */
    public void setSortAction(String sortAction) {
        _headerCellModel.setSortAction(sortAction);
    }

    /**
     * Set an href used to build URIs to change the sort state for this header cell.  Only one of this and
     * the {@link #setSortAction(String)} attribute may be set on this tag.
     * @param sortHref the sort href
     * @jsptagref.attributedescription
     * Set an href used to build URIs to change the sort state for this header cell.  Only one of this and
     * the {@link #setSortAction(String)} attribute may be set on this tag.
     * @jsptagref.attributesyntaxvalue <i>string_sortHref</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The href to use when performing a sort on this column"
     */
    public void setSortHref(String sortHref) {
        _headerCellModel.setSortHref(sortHref);
    }

    /* --------------------------------------------------------------
     *
     *    <th> attributes
     *
     * --------------------------------------------------------------
     */

    /**
     * Sets the onClick JavaScript for the HTML th tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript for the HTML th tag."
     */
    public void setCellOnClick(String onClick) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript for the HTML th tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript for the HTML th tag."
     */
    public void setCellOnDblClick(String onDblClick) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript for the HTML th tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript for the HTML th tag."
     */
    public void setCellOnKeyDown(String onKeyDown) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript for the HTML th tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript for the HTML th tag."
     */
    public void setCellOnKeyUp(String onKeyUp) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript for the HTML th tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript for the HTML th tag."
     */
    public void setCellOnKeyPress(String onKeyPress) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript for the HTML th tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript for the HTML th tag."
     */
    public void setCellOnMouseDown(String onMouseDown) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript for the HTML th tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript for the HTML th tag."
     */
    public void setCellOnMouseUp(String onMouseUp) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript for the HTML th tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript for the HTML th tag."
     */
    public void setCellOnMouseMove(String onMouseMove) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript for the HTML th tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript for the HTML th tag."
     */
    public void setCellOnMouseOut(String onMouseOut) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript for the HTML th tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript for the HTML th tag."
     */
    public void setCellOnMouseOver(String onMouseOver) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style for the HTML th tag.
     *
     * @param style the style
     * @jsptagref.attributedescription The style for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellStyle</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style for the HTML th tag."
     */
    public void setCellStyle(String style) {
        if("".equals(style)) return;

        _cellState.style = style;
    }

    /**
     * Sets the style class for the HTML th tag.
     *
     * @param styleClass the style class
     * @jsptagref.attributedescription The style class for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellStyleClass</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the HTML th tag."
     */
    public void setCellStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _cellState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML th tag.
     *
     * @param title the title
     * @jsptagref.attributedescription The title for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellTitle</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title for the HTML th tag."
     */
    public void setCellTitle(String title) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the value of the rowspan attribute for the HTML th tag.
     *
     * @param rowSpan the rowspan
     * @jsptagref.attributedescription The row span for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>int_cellRowspan</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The rowspan for the HTML th tag."
     */
    public void setCellRowspan(int rowSpan) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ROWSPAN, "" + rowSpan);
    }

    /**
     * Sets the value of the colspan attribute for the HTML th tag.
     *
     * @param colSpan the colspan
     * @jsptagref.attributedescription The colspan for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>int_cellColspan</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The colspan for the HTML th tag."
     */
    public void setCellColspan(int colSpan) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.COLSPAN, "" + colSpan);
    }

    /**
     * Sets the value of the horizontal align attribute for the HTML th tag..
     *
     * @param align the horizontal alignment
     * @jsptagref.attributedescription The horizontal alignment for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellAlign</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The horizontal alignment for the HTML th tag"
     */
    public void setCellAlign(String align) {
        /* todo: should this enforce left|center|right|justify|char as in the spec */
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the value of the horizontal alignment character attribute for the HTML th tag.
     *
     * @param alignChar the alignment character
     * @jsptagref.attributedescription The horizontal alignment character for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellAlignChar</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The horizontal alignment character for the HTML th tag."
     */
    public void setCellChar(String alignChar) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAR, alignChar);
    }

    /**
     * Sets the value of the horizontal alignment character offset for the HTML th tag..
     *
     * @param alignCharOff the horizontal alignment character offset
     * @jsptagref.attributedescription The horizontal alignment character offset for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellAlignCharOff</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The horizontal alignment character offset for the HTML th tag."
     */
    public void setCellCharoff(String alignCharOff) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAROFF, alignCharOff);
    }

    /**
     * Sets the value of the vertical alignment attribute for the HTML th tag.
     *
     * @param align the vertical alignment attribute
     * @jsptagref.attributedescription The vertical alignment for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellValign</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="The cell's vertical alignment for the HTML th tag."
     */
    public void setCellValign(String align) {
        /* todo: should this enforce top|middle|bottom|baseline as in the spec */
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VALIGN, align);
    }

    /**
     * Sets the value of the language attribute for the HTML th tag.
     *
     * @param lang the language attribute
     * @jsptagref.attributedescription The language for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellLang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The language attribute for the HTML th tag."
     */
    public void setCellLang(String lang) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the value of the text direction attribute for the HTML th tag.
     *
     * @param dir the text direction
     * @jsptagref.attributedescription The text direction attribute for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellDir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The text direction for the HTML th tag."
     */
    public void setCellDir(String dir) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Sets the value of the abbreviated form of the content for the HTML th tag.
     *
     * @param abbr the abbr
     * @jsptagref.attributedescription The abbreviated form of the cell's content for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellDir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The abbreviated form of the cell's content for the HTML th tag."
     */
    public void setCellAbbr(String abbr) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ABBR, abbr);
    }

    /**
     * Sets the value of the axis attribute for the HTML th tag.
     *
     * @param axis the axis
     * @jsptagref.attributedescription The axis attribute for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellAxis</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The axis attribute for the HTML th tag"
     */
    public void setCellAxis(String axis) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.AXIS, axis);
    }

    /**
     * Sets the value of the headers attribute for the HTML th tag.
     *
     * @param headers the headers
     * @jsptagref.attributedescription The headers attribute for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellHeaders</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The headers attribute for the HTML th tag."
     */
    public void setCellHeaders(String headers) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HEADERS, headers);
    }

    /**
     * Sets the value of the scope attribute for the HTML th tag.
     *
     * @param scope the scope
     * @jsptagref.attributedescription The scope attribute for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellScope</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The scope attribute for the HTML th tag.
     */
    public void setCellScope(String scope) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.SCOPE, scope);
    }

    /**
     * Set the name of the tagId for the HTML th tag.
     *
     * @param tagId the the name of the tagId for the th tag.
     * @jsptagref.attributedescription The tagId for the HTML th tag.
     * @jsptagref.attributesyntaxvalue <i>string_cellTagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the HTML th tag."
     */
    public void setCellTagId(String tagId)
        throws JspException {
        applyTagId(_cellState, tagId);
    }

    /**
     * <p>
     * Implementation of the {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.  This
     * allows users of the anchorCell tag to extend the attribute set that is rendered by the HTML
     * anchor.  This method accepts the following facets:
     * <table>
     * <tr><td>Facet Name</td><td>Operation</td></tr>
     * <tr><td><code>header</code></td><td>Adds an attribute with the provided <code>name</code> and <code>value</code> to the
     * attributes rendered on the &lt;th&gt; tag.</td></tr>
     * </table>
     * The HeaderCell tag defaults to the setting attributes on the header when the facet name is unset.
     * </p>
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @param facet the facet for the attribute
     * @throws JspException thrown when the facet is not recognized
     */
    public void setAttribute(String name, String value, String facet) throws JspException {
        if(facet == null || facet.equals(ATTRIBUTE_HEADER_NAME)) {
            super.addStateAttribute(_cellState, name, value);
        }
        else {
            String s = Bundle.getString("Tags_AttributeFacetNotSupported", new Object[]{facet});
            throw new JspException(s);
        }
    }

    /**
     * <p>
     * Implementation of the {@link IBehaviorConsumer} interface that extends the functionality of this
     * tag beyond that exposed via the JSP tag attributes.  This method accepts the following facets:
     * <table>
     * <tr><td>Facet Name</td><td>Operation</td></tr>
     * <tr><td><code>renderer</code></td><td>Extends the cell decorator chain used to implement rendering for this tag</td></tr>
     * </table>
     * The <code>renderer</code> facet accepts the following behavior names.  The value of each should be a String classname
     * of a class that extends the {@link CellDecorator} base class.
     * <table>
     * <tr><td>extends</td><td>Add an additional decorator to the currently configured cell decorator chain.</td></tr>
     * <tr><td>sort</td><td>Replace the currently configured sort decorator with one created from this class name.</td></tr>
     * </table>
     * </p>
     * @param name the name of the behavior
     * @param value the value of the behavior
     * @param facet th ebehavior's facet
     * @throws JspException when the behavior's facet isnot recognized
     */
    public void setBehavior(String name, Object value, String facet) throws JspException {
        if(facet != null && facet.equals(BEHAVIOR_RENDERER_NAME)) {
            String className = value != null ? value.toString() : null;
            /* provides a way to extend the existing decorators */
            CellDecorator cellDecorator = (CellDecorator)ExtensionUtil.instantiateClass(className, CellDecorator.class);
            if(name.equals(BEHAVIOR_RENDERER_NAME_EXTENDS)) {
                cellDecorator.setNestedDecorator(getCellDecorator());
            }
            else if(name.equals(BEHAVIOR_RENDERER_NAME_SORT)) {
                cellDecorator.setNestedDecorator(DECORATOR_HEADER_SORTED);
            }
            /* replace the core cell decorator with a new default */
            else if(name.equals(BEHAVIOR_RENDERER_NAME_DEFAULT)) {
                /* nyi */
            }

            _cellDecorator = cellDecorator;
        }
        else {
            String s = Bundle.getString("Tags_BehaviorFacetNotSupported", new Object[]{facet});
            throw new JspException(s);
        }
    }

    /**
     * Implementation of {@link AbstractHtmlTableCell#internalGetCellModel()} that exposes the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.HeaderCellModel}
     * which is storing state for this tag.
     * @return this tag's cell model
     */
    protected CellModel internalGetCellModel() {
        return _headerCellModel;
    }

    /**
     * Render the header cell's contents.  This method sets the style information on the HTML
     * th tag and then calls the
     * {@link #renderHeaderCellContents(org.apache.beehive.netui.tags.rendering.AbstractRenderAppender, String)} 
     * method to render the contents of the cell.
     * @param appender the {@link AbstractRenderAppender} to which the output from this tag should be added
     * @throws IOException
     * @throws JspException
     */
    protected void renderCell(AbstractRenderAppender appender)
            throws IOException, JspException {

        DataGridTagModel dataGridModel = DataGridUtil.getDataGridTagModel(getJspContext());
        assert dataGridModel != null;

        TableRenderer tableRenderer = dataGridModel.getTableRenderer();
        assert tableRenderer != null;

        ArrayList/*<String>*/ styleClasses = new ArrayList/*<String>*/();

        /* todo: refactor.  add a chain of style decorators here; easier to extend this way. */
        FilterModel filterModel = dataGridModel.getState().getFilterModel();
        if(filterModel.isFiltered(_headerCellModel.getFilterExpression()))
            styleClasses.add(dataGridModel.getStyleModel().getHeaderCellFilteredClass());

        SortModel sortModel = dataGridModel.getState().getSortModel();
        if(sortModel.isSorted(_headerCellModel.getSortExpression()))
            styleClasses.add(dataGridModel.getStyleModel().getHeaderCellSortedClass());

        if(_headerCellModel.isSortable())
            styleClasses.add(dataGridModel.getStyleModel().getHeaderCellSortableClass());

        if(_cellState.styleClass == null)
            styleClasses.add(dataGridModel.getStyleModel().getHeaderCellClass());
        else
            styleClasses.add(_cellState.styleClass);

        _cellState.styleClass = dataGridModel.getStyleModel().buildStyleClassValue(styleClasses);

        JspFragment fragment = getJspBody();
        StringWriter sw = new StringWriter();
        String jspFragmentOutput = null;
        if(fragment != null) {
            fragment.invoke(sw);
            jspFragmentOutput = sw.toString();
        }

        tableRenderer.openHeaderCell(_cellState, appender);
        renderHeaderCellContents(appender, jspFragmentOutput);
        tableRenderer.closeHeaderCell(appender);

        /* render any JavaScript needed to support framework features */
        if (_cellState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            String script = renderNameAndId(request, _cellState, null);

            if(script != null)
                appender.append(script);
        }
    }

    /**
     * Render the contents of the body of the HTML th tag into the given {@link AbstractRenderAppender}
     * @param appender the {@link AbstractRenderAppender} for the output
     * @param jspFragmentOutput the result of having evaluated the tag's {@link JspFragment}
     * @throws IOException when an error occurs writing to the output stream
     * @throws JspException when an error occurs evaluating the {@link JspFragment} for this body
     */
    protected void renderHeaderCellContents(AbstractRenderAppender appender, String jspFragmentOutput)
            throws IOException, JspException {

        if(_headerValue == null) {
            appender.append(jspFragmentOutput);
        }
        else {
            _headerCellModel.setDataGridTagModel(DataGridUtil.getDataGridTagModel(getJspContext()));
                _headerCellModel.setBodyContent(jspFragmentOutput);
            _headerCellModel.setValue(_headerValue);

            getCellDecorator().decorate(getJspContext(), appender, _headerCellModel);
        }
    }

    /**
     * Apply the tag's attributes to the {@link HeaderCellModel}.
     * @throws JspException when errors are encountered applying the attributes
     */
    protected void applyAttributes()
            throws JspException {
        super.applyAttributes();

        if(_headerCellModel.getFilterHref() != null && _headerCellModel.getFilterAction() != null)
            throw new JspException(Bundle.getErrorString("HeaderCell_CantSetHrefAndAction"));

        if(_headerCellModel.getSortHref() != null && _headerCellModel.getSortAction() != null)
            throw new JspException(Bundle.getErrorString("HeaderCell_CantSetSortHrefAndAction"));

        if(_headerCellModel.isSortable()) {
            if(_headerCellModel.getSortExpression() == null)
                throw new JspException(Bundle.getErrorString("HeaderCell_CantEnableSorting"));

            /* set an intelligent default for the sorting HREF */
            if(_headerCellModel.getSortHref() == null && _headerCellModel.getSortAction() == null)
                _headerCellModel.setSortHref(JspUtil.getRequest(getJspContext()).getRequestURI());
        }

        if(_headerCellModel.isFilterable()) {
            if(_headerCellModel.getFilterExpression() == null)
                throw new JspException(Bundle.getErrorString("HeaderCell_CantEnableFiltering"));

            /* set an intelligent default for the filtering HREF */
            if(_headerCellModel.getFilterHref() == null && _headerCellModel.getFilterAction() == null)
                _headerCellModel.setFilterHref(JspUtil.getRequest(getJspContext()).getRequestURI());
        }
    }

    /**
     * Internal method used to get the outer {@link CellDecorator}.
     * @return the outer cell decorator
     */
    private CellDecorator getCellDecorator() {
        if(_cellDecorator != null)
            return _cellDecorator;
        else {
            assert DECORATOR_HEADER_SORTED != null;
            return DECORATOR_HEADER_SORTED;
        }
    }
}
