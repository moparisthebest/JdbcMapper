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
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.databinding.datagrid.api.sort.SortModel;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterModel;
import org.apache.beehive.netui.tags.IAttributeConsumer;
import org.apache.beehive.netui.tags.IBehaviorConsumer;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.TdTag;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * Abstract base class that supports rendering an HTML &lt;td&gt;.  This tag should be used as the base class
 * for all tags which render content during the data grid's {@link DataGridTagModel#RENDER_STATE_GRID} render
 * state.  Subclasses must implement the {@link #renderDataCellContents(org.apache.beehive.netui.tags.rendering.AbstractRenderAppender, String)}
 * which will be invoked after the opening table cell tag and before the closing table cell tag.
 * </p>
 * <p>
 * State attributes set via the <code>setCell*</code> methods are added to the attribute set used
 * when rendering the &lt;td&gt; tag.
 * </p>
 */
public abstract class AbstractHtmlTableCell
    extends AbstractCell
    implements IAttributeConsumer, IBehaviorConsumer {

    private String _sortExpression = null;
    private String _filterExpression = null;

    private TdTag.State _cellState = new TdTag.State();

    /**
     * <p>
     * Set the sort expression with which this cell should be associated.  Cells use this value
     * to lookup any {@link org.apache.beehive.netui.databinding.datagrid.api.sort.Sort} state
     * that may apply to this cell.  Often, this value matches a {@link HeaderCell#setSortExpression(String)}
     * set on a header cell.  It is used by data cells to render styles representing sorted data.
     * </p>
     * @param sortExpression the sort expression
     * @jsptagref.attributedescription
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setSortExpression(String sortExpression) {
        _sortExpression = sortExpression;
    }

    /**
     * <p>
     * Set the filter expression with which this cell should be associated.  Cells use this value
     * to lookup any {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} state
     * that may apply to this cell.  Often, this value matches a {@link HeaderCell#setFilterExpression(String)}
     * set on a header cell.  It is used by data cells to render styles representing filtered data.
     * </p>
     * @param filterExpression the filter expression
     * @jsptagref.attributedescription
     * <p>
     * Set the filter expression with which this cell should be associated.  Cells use this value
     * to lookup any {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter} state
     * that may apply to this cell.  Often, this value matches a {@link HeaderCell#setFilterExpression(String)}
     * set on a header cell.  It is used by data cells to render styles representing filtered data.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setFilterExpression(String filterExpression) {
        _filterExpression = filterExpression;
    }

    /* --------------------------------------------------------------
     *
     *    <td> attributes
     *
     * --------------------------------------------------------------
     */

    /**

     * Sets the onClick JavaScript event for the HTML table cell.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event."
     */
    public void setCellOnClick(String onClick) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick javascript event for the HTML table cell.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event."
     */
    public void setCellOnDblClick(String onDblClick) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown javascript event for the HTML table cell.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event."
     */
    public void setCellOnKeyDown(String onKeyDown) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp javascript event for the HTML table cell.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event."
     */
    public void setCellOnKeyUp(String onKeyUp) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress javascript event for the HTML table cell.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event."
     */
    public void setCellOnKeyPress(String onKeyPress) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown javascript event for the HTML table cell.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event."
     */
    public void setCellOnMouseDown(String onMouseDown) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp javascript event for the HTML table cell.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event."
     */
    public void setCellOnMouseUp(String onMouseUp) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove javascript event for the HTML table cell.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event."
     */
    public void setCellOnMouseMove(String onMouseMove) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut javascript event for the HTML table cell.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event."
     */
    public void setCellOnMouseOut(String onMouseOut) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver javascript event for the HTML table cell.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellOnMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event."
     */
    public void setCellOnMouseOver(String onMouseOver) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style attribute for the HTML table cell.
     *
     * @param style the html style.
     * @jsptagref.attributedescription The style attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellStyle</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style attribute."
     */
    public void setCellStyle(String style) {
        if("".equals(style)) return;

        _cellState.style = style;
    }

    /**
     * Sets the style class attribute for the HTML table cell.
     *
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellStyleClass</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class."
     */
    public void setCellStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _cellState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML table cell.
     *
     * @param title the title
     * @jsptagref.attributedescription The title for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellTitle</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title."
     */
    public void setCellTitle(String title) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the rowspan attribute for the HTML table cell.
     *
     * @param rowSpan the rowspan
     * @jsptagref.attributedescription The row span attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellRowspan</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The rowspan."
     */
    public void setCellRowspan(int rowSpan) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ROWSPAN, "" + rowSpan);
    }

    /**
     * Sets the colspan attribute of the HTML table cell.
     *
     * @param colSpan the colspan
     * @jsptagref.attributedescription The colspan attribute of the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellColspan</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The colspan."
     */
    public void setCellColspan(int colSpan) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.COLSPAN, "" + colSpan);
    }

    /**
     * Sets the horizontal alignment of the HTML table cell.
     *
     * @param align the alignment
     * @jsptagref.attributedescription The horizontal alignment of the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellAlign</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment"
     */
    public void setCellAlign(String align) {
        /* todo: should this enforce left|center|right|justify|char as in the spec */
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the value of the horizontal alignment character attribute for the HTML table cell.
     *
     * @param alignChar the horizontal alignment character
     * @jsptagref.attributedescription The horizontal alignment character for the HTML table cell
     * @jsptagref.attributesyntaxvalue <i>string_cellAlignChar</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment character"
     */
    public void setCellChar(String alignChar) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAR, alignChar);
    }

    /**
     * Sets the value of the horizontal alignment character offset attribute for the HTML table cell.
     *
     * @param alignCharOff the alingnment character offset
     * @jsptagref.attributedescription The horizontal alignment character offset for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellAignCharOff</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment character offset"
     */
    public void setCellCharoff(String alignCharOff) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAROFF, alignCharOff);
    }

    /**
     * Sets the value of the vertical alignment attribute for the HTML table cell.
     *
     * @param align the vertical alignment
     * @jsptagref.attributedescription The vertical alignment for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellAlign</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's vertical alignment"
     */
    public void setCellValign(String align) {
        /* todo: should this enforce top|middle|bottom|baseline as in the spec */
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VALIGN, align);
    }

    /**
     * Sets the value of the language attribute for the HTML table cell.
     *
     * @param lang the language
     * @jsptagref.attributedescription The language for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellLang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's language"
     */
    public void setCellLang(String lang) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the value of the text direction attribute for the HTML table cell.
     *
     * @param dir the text direction
     * @jsptagref.attributedescription The text direction attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellDir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's text direction"
     */
    public void setCellDir(String dir) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Sets the value of the abbreviated form of the HTML table cell's content.
     *
     * @param abbr the abbreviation
     * @jsptagref.attributedescription The abbreviated form of the HTML table cell's content.
     * @jsptagref.attributesyntaxvalue <i>string_cellAbbr</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The abbreviated form of the cell's content"
     */
    public void setCellAbbr(String abbr) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ABBR, abbr);
    }

    /**
     * Sets the value of the axis attribute for the HTML table cell.
     *
     * @param axis the axis
     * @jsptagref.attributedescription The axis attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellAxis</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The axis attribute"
     */
    public void setCellAxis(String axis) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.AXIS, axis);
    }

    /**
     * Sets the value of the headers attribute for the HTML table cell.
     *
     * @param headers the headers
     * @jsptagref.attributedescription The headers attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellHeaders</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The headers attribute"
     */
    public void setCellHeaders(String headers) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HEADERS, headers);
    }

    /**
     * Sets the value of the scope attribute for the HTML table cell.
     *
     * @param scope the scope
     * @jsptagref.attributedescription The scope attribute for the HTML table cell.
     * @jsptagref.attributesyntaxvalue <i>string_cellScope</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The scope attribute"
     */
    public void setCellScope(String scope) {
        _cellState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.SCOPE, scope);
    }

    /**
     * Set the name of the tagId for the HTML table cell.  The user is responsible for ensuring that
     * this tag id is unique in the rendered page.
     *
     * @param tagId the tag id
     * @jsptagref.attributedescription The tagId for the HTML table cell.  The user is responsible for
     * ensuring that this tag id is unique in the rendered page.
     * @jsptagref.attributesyntaxvalue <i>string_cellTagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setCellTagId(String tagId)
        throws JspException {
        applyTagId(_cellState, tagId);
    }

    /**
     * <p>
     * Base support for setting attributes on a tag via the {@link IAttributeConsumer} interface.  The
     * AbstractHtmlTableCell does not support any attributes by default.  Attributes set via this interface
     * are used to extend the HTML attributes exposed through the JSP tags themselves.  This allows
     * tag users to add arbitrary attributes to the HTML tags rendered by the data grid.
     * </p>
     *
     * @param name  The name of the attribute.
     * @param value The value of the attribute.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException {
        String s = Bundle.getString("Tags_AttributeFacetNotSupported", new Object[]{facet});
        throw new JspException(s);
    }

    /**
     * <p>
     * Base support for setting behavior values via the {@link IBehaviorConsumer} interface.  The
     * AbstractHtmlTableCell does not support any attributes by default.  Attributes set via this
     * interface are used to configure internal functionality of the tags which is not exposed
     * via JSP tag attributes.
     * </p>
     * @param name the name of the behavior
     * @param value the value of the behavior
     * @param facet the name of a facet of the tag to which the behavior will be applied.  This is optional.
     * @throws JspException
     */
    public void setBehavior(String name, Object value, String facet)
            throws JspException {
        String s = Bundle.getString("Tags_BehaviorFacetNotSupported", new Object[]{facet});
        throw new JspException(s);
    }

    /**
     * <p>
     * Base HTML table cell rendering functionality which opens and closes the HTML &lt;td&gt; tags with
     * the correct style and attribute information.  Between the table cell tags, the tag
     * calls the {@link #renderDataCellContents(org.apache.beehive.netui.tags.rendering.AbstractRenderAppender, String)}
     * method so that subclasses implementing this method can provide content inside of the table cell.
     * </p>
     * <p>
     * The style information rendered here includes the following in order:
     * <ol>
     * <li>the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getDataCellFilteredClass()}
     * if the cell has a filter expression and is filtered
     * </li>
     * <li>the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getDataCellSortedClass()}
     * if the cell has a sort expression and is sorted
     * <li>the {@link #setCellStyleClass(String)} attribute if set;
     * {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getDataCellClass()} otherwise</li>
     * </ol>
     * </p>
     * @param appender the {@link AbstractRenderAppender} to which any output should be rendered
     * @throws IOException
     * @throws JspException
     */
    protected void renderCell(AbstractRenderAppender appender)
            throws IOException, JspException {

        DataGridTagModel dataGridModel = DataGridUtil.getDataGridTagModel(getJspContext());
        if(dataGridModel == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        TableRenderer tableRenderer = dataGridModel.getTableRenderer();
        assert tableRenderer != null;

        /* todo: refactor. extensibility in supporting style decorators */
        ArrayList/*<String>*/ styleClasses = new ArrayList/*<String>*/();
        FilterModel filterModel = dataGridModel.getState().getFilterModel();
        if(_filterExpression != null && filterModel.isFiltered(_filterExpression))
            styleClasses.add(dataGridModel.getStyleModel().getDataCellFilteredClass());

        SortModel sortModel = dataGridModel.getState().getSortModel();
        if(_sortExpression != null && sortModel.isSorted(_sortExpression))
            styleClasses.add(dataGridModel.getStyleModel().getDataCellSortedClass());

        if(_cellState.styleClass == null)
            styleClasses.add(dataGridModel.getStyleModel().getDataCellClass());
        else
            styleClasses.add(_cellState.styleClass);

        _cellState.styleClass = dataGridModel.getStyleModel().buildStyleClassValue(styleClasses);

        /*
           note, this runs in order to allow any nested tags to do their work.  
           this provides support for formatters, parameters, etc
         */
        JspFragment fragment = getJspBody();
        StringWriter sw = new StringWriter();
        if(fragment != null)
            fragment.invoke(sw);

        tableRenderer.openTableCell(_cellState, appender);
        renderDataCellContents(appender, sw.toString());
        tableRenderer.closeTableCell(appender);

        /* todo: need to add the JavaScript rendering for any tagIds that were set on <td>s */
        if (_cellState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            String script = renderNameAndId(request, _cellState, null);
            if(script != null)
                appender.append(script);
        }

    }

    /**
     * <p>
     * Abstract method implemented by subclasses in order to render a cell's content.
     * </p>
     * @param appender the {@link AbstractRenderAppender} to which any output should be rendered
     * @param output the output produced from having evaluated this tag's {@link JspFragment}
     * @throws IOException
     * @throws JspException
     */
    protected abstract void renderDataCellContents(AbstractRenderAppender appender, String output)
            throws IOException, JspException;
}
