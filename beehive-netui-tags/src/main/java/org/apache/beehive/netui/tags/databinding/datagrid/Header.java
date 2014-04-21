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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.io.StringWriter;
import java.io.IOException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.tags.IHtmlI18n;
import org.apache.beehive.netui.tags.IHtmlEvents;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.THeadTag;
import org.apache.beehive.netui.tags.rendering.TrTag;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The Header tag is used to render a table row(s) before the data grid has rendered data rows as demarcated
 * by the {@link Rows} tag.  The header can also optionally render a &lt;thead&gt; HTML tag if the data grid
 * is rendering HTML table row groups.  To enable this functionality, set the
 * {@link DataGrid#setRenderRowGroups(boolean)} attribute.  The location of this tag inside of a
 * data grid does not affect when its content renders.  Table cells containing header describing columns
 * of data along with sort and filter state / UI is commonly placed inside of a data grid's header.
 * </p>
 * <p>
 * The attribute setters for the footer tag are used to add HTML attributes to the &lg;thead&gt; HTML
 * tag.  When row group rendering is disabled, attributes set here do not render.
 * </p>
 * <p>
 * Because this tag renders inside of an HTML table, it by default renders an HTML
 * &lt;tr&gt; tag to represent a table row.  Table row tag rendering can be disabled using the {@link #setRenderRow(boolean)}
 * attribute.  When this is disabled, the page author is responsible for maintaining the integrity of the
 * HTML table by writing &lt;tr&gt; tags manually or by using the {@link Row} tag.  When this tag is rendering
 * it does not produce table cells; the contents of the table row in the footer is entirely left to the
 * page author.  With row rendering disabled, it is also possible to add multiple table rows to the
 * end of a data grid.
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * </ul>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * The Header tag is used to render a table row(s) before the data grid has rendered data rows as demarcated
 * by the {@link Rows} tag.  The header can also optionally render a &lt;thead&gt; HTML tag if the data grid
 * is rendering HTML table row groups.  To enable this functionality, set the
 * {@link DataGrid#setRenderRowGroups(boolean)} attribute.  The location of this tag inside of a
 * data grid does not affect when its content renders.  Table cells containing header describing columns
 * of data along with sort and filter state / UI is commonly placed inside of a data grid's header.
 * </p>
 * <p>
 * The attribute setters for the footer tag are used to add HTML attributes to the &lg;thead&gt; HTML
 * tag.  When row group rendering is disabled, attributes set here do not render.
 * </p>
 * <p>
 * Because this tag renders inside of an HTML table, it by default renders an HTML
 * &lt;tr&gt; tag to represent a table row.  Table row tag rendering can be disabled using the {@link #setRenderRow(boolean)}
 * attribute.  When this is disabled, the page author is responsible for maintaining the integrity of the
 * HTML table by writing &lt;tr&gt; tags manually or by using the {@link Row} tag.  When this tag is rendering
 * it does not produce table cells; the contents of the table row in the footer is entirely left to the
 * page author.  With row rendering disabled, it is also possible to add multiple table rows to the
 * end of a data grid.
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * </ul>
 * </p>
 * @netui:tag name="header" description="Renders the header into a NetUI data grid" body-content="scriptless"
 */
public class Header
    extends AbstractDataGridHtmlTag
    implements IHtmlEvents, IHtmlI18n {

    private boolean _renderRow = true;
    private THeadTag.State _theadTag = new THeadTag.State();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "Header";
    }

    /**
     * Sets the onClick JavaScript event for the HTML thead tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event for the HTML thead tag."
     */
    public void setOnClick(String onClick) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML thead tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event for the HTML thead tag."
     */
    public void setOnDblClick(String onDblClick) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML thead tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event for the HTML thead tag."
     */
    public void setOnKeyDown(String onKeyDown) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML thead tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event for the HTML thead tag."
     */
    public void setOnKeyUp(String onKeyUp) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event for the HTML thead tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event for the HTML thead tag."
     */
    public void setOnKeyPress(String onKeyPress) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML thead tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event for the HTML thead tag."
     */
    public void setOnMouseDown(String onMouseDown) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML thead tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event for the HTML thead tag."
     */
    public void setOnMouseUp(String onMouseUp) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML thead tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event for the HTML thead tag."
     */
    public void setOnMouseMove(String onMouseMove) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML thead tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event for the HTML thead tag."
     */
    public void setOnMouseOut(String onMouseOut) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML thead tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event for the HTML thead tag."
     */
    public void setOnMouseOver(String onMouseOver) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style rendered by the HTML thead tag.
     *
     * @param style the style
     * @jsptagref.attributedescription The style rendered by the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style rendered by the HTML thead tag"
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _theadTag.style = style;
    }

    /**
     * Sets the style class of the rendered by the HTML thead tag.
     *
     * @param styleClass the style class
     * @jsptagref.attributedescription The style class rendered by the HTML thead tag
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class rendered by the HTML thead tag"
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _theadTag.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute rendered by the HTML thead tag.
     *
     * @param title the title
     * @jsptagref.attributedescription The title rendered by the HTML thead tag
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title rendered by the HTML thead tag."
     */
    public void setTitle(String title) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the value of the horizontal align attribute rendered by the HTML thead tag.
     *
     * @param align the alignment
     * @jsptagref.attributedescription The horizontal alignment rendered by the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The horizontal alignment attribute rendered by the HTML thead tag."
     */
    public void setAlign(String align) {
        /* todo: should this enforce left|center|right|justify|char as in the spec */
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the value of the horizontal alignment character attribute rendered by the HTML thead tag.
     *
     * @param alignChar the alignment character
     * @jsptagref.attributedescription The horizontal alignment character rendered by the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_alignChar</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The horizontal alignment character rendered by the HTML thead tag."
     */
    public void setChar(String alignChar) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAR, alignChar);
    }

    /**
     * Sets the value of the horizontal alignment character offset attribute rendered by the HTML thead tag.
     *
     * @param alignCharOff the alignment character offset
     * @jsptagref.attributedescription The horizontal alignment character offset rendered by the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_alignCharOff</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The horizontal alignment character offset rendered by the HTML thead tag."
     */
    public void setCharoff(String alignCharOff) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAROFF, alignCharOff);
    }

    /**
     * Sets the value of the vertical alignment attribute rendered by the HTML thead tag.
     *
     * @param valign the vertical alignment
     * @jsptagref.attributedescription The vertical alignment rendered by the HTML thead tag
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The vertical alignment rendered by the HTML thead tag.
     */
    public void setValign(String valign) {
        /* todo: should this enforce top|middle|bottom|baseline as in the spec */
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VALIGN, valign);
    }

    /**
     * Sets the value of the language attribute rendered by the HTML thead tag
     *
     * @param lang the language
     * @jsptagref.attributedescription The language attribute rendered by the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The language attribute rendered by the HTML thead tag."
     */
    public void setLang(String lang) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the value of the text direction attribute rendered by the HTML thead tag.
     *
     * @param dir the dir attribute
     * @jsptagref.attributedescription The text direction attribute rendered by the HTML thead tag.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The text direction rendered by the HTML thead tag."
     */
    public void setDir(String dir) {
        _theadTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Set a boolean that enables / disables rendering HTML table rows by this tag.  If the
     * value is enabled, an HTML &lt;tr&gt; will be rendered when this tag renders its body.  If
     * the value is disabled, no &lt;tr&gt; tags will be rendered and the page author is responsible
     * for maintaining the integrity of the HTML table.
     * @jsptagref.attributedescription
     * Set a boolean that enables / disables rendering HTML table rows by this tag.  If the
     * value is enabled, an HTML &lt;tr&gt; will be rendered when this tag renders its body.  If
     * the value is disabled, no &lt;tr&gt; tags will be rendered and the page author is responsible
     * for maintaining the integrity of the HTML table.
     * @jsptagref.attributesyntaxvalue <i>boolean_renderRow</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="Boolean to enable / disable rendering HTML table row tags"
     */
    public void setRenderRow(boolean renderRow) {
        _renderRow = renderRow;
    }

    /**
     * Set the name of the tagId for the HTML thead tag.
     *
     * @param tagId the the name of the tagId for the thead tag.
     * @jsptagref.attributedescription The tagId.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
        throws JspException {
        applyTagId(_theadTag, tagId);
    }

    /**
     * Render this tag.  This method renders during the data grid's {@link DataGridTagModel#RENDER_STATE_HEADER}
     * state in order to add table rows to the beginning of a data grid's HTML table.  If the data grid is rendering
     * HTML row groups, this tag will output an HTML &lt;thead&gt; tag.  Then, if this tag is rendering
     * a table row, it will produce an HTML &lt;tr&gt; tag.  Then the content of the body will be rendered.  If
     * table row rendering is disabled, the page author is responsible for rendering the appropriate HTML
     * table row tags as this tag renders inside of the HTML table opened by the data grid.
     * @throws IOException
     * @throws JspException when the {@link DataGridTagModel} can not be found in the {@link JspContext}
     */
    public void doTag()
            throws JspException, IOException {

        JspContext jspContext = getJspContext();
        DataGridTagModel dgm = DataGridUtil.getDataGridTagModel(jspContext);
        if(dgm == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        int gridRenderState = dgm.getRenderState();
        if(gridRenderState == DataGridTagModel.RENDER_STATE_HEADER) {

            InternalStringBuilder content = new InternalStringBuilder();
            AbstractRenderAppender appender = new StringBuilderRenderAppender(content);

            StyleModel styleModel = dgm.getStyleModel();
            assert styleModel != null;

            TableRenderer tableRenderer = dgm.getTableRenderer();
            assert tableRenderer != null;

            if(dgm.isRenderRowGroups()) {
                _theadTag.styleClass = (_theadTag.styleClass != null ? _theadTag.styleClass : styleModel.getTableHeadClass());
                tableRenderer.openTableHead(_theadTag, appender);
            }

            TrTag.State trState = null;
            if(_renderRow) {
                trState = new TrTag.State();
                trState.styleClass = styleModel.getHeaderRowClass();
                tableRenderer.openHeaderRow(trState, appender);
            }

            JspFragment fragment = getJspBody();
            if(fragment != null) {
                StringWriter sw = new StringWriter();
                fragment.invoke(sw);
                appender.append(sw.toString());
            }

            if(_renderRow)
                tableRenderer.closeHeaderRow(appender);

            if(dgm.isRenderRowGroups()) {
                tableRenderer.closeTableHead(appender);
                String tfootScript = null;
                if(_theadTag.id != null) {
                    HttpServletRequest request = JspUtil.getRequest(getJspContext());
                    tfootScript = renderNameAndId(request, _theadTag, null);
                }

                if(tfootScript != null)
                    appender.append(tfootScript);
            }

            jspContext.getOut().write(content.toString());
        }
    }
}
