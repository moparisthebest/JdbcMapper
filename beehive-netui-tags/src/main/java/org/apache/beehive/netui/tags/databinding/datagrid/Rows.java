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

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.IHtmlEvents;
import org.apache.beehive.netui.tags.IHtmlI18n;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.TrTag;
import org.apache.beehive.netui.tags.rendering.TBodyTag;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The Rows tag is used to render a table row(s) that contain the data from a data set displayed
 * in a data grid.  The rows rendered here render after the header reders with the {@link Header} and before
 * the footer renders in the {@link Footer}.  The body of this tag usually contains the data grid's "cell" tags
 * which are used to render HTML table cells inside of the rows rendered by this tag.  Such tags include:
 * <ul>
 * <li>{@link AnchorCell} which is used to render anchors</li>
 * <li>{@link ImageAnchorCell} which is used to render clickable images</li>
 * <li>{@link ImageCell} which is used to render an image</li>
 * <li>{@link SpanCell} which is used to render an HTML span with data bound content</li>
 * <li>{@link TemplateCell} which can contain arbitrary content for {@link javax.servlet.jsp.tagext.SimpleTag}</li>
 * </ul>
 * </p>
 * <p>
 * The attribute setters in this class are used to add attributes to the &lt;tbody&gt; tag which will be rendered
 * when row groups are enabled.  When row group rendering is disabled, attributes set here do not render.
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
 * @jsptagref.tagdescription
 * <p>
 * The Rows tag is used to render a table row(s) that contain the data from a data set displayed
 * in a data grid.  The rows rendered here render after the header reders with the {@link Header} and before
 * the footer renders in the {@link Footer}.  The body of this tag usually contains the data grid's "cell" tags
 * which are used to render HTML table cells inside of the rows rendered by this tag.  Such tags include:
 * <ul>
 * <li>{@link AnchorCell} which is used to render anchors</li>
 * <li>{@link ImageAnchorCell} which is used to render clickable images</li>
 * <li>{@link ImageCell} which is used to render an image</li>
 * <li>{@link SpanCell} which is used to render an HTML span with data bound content</li>
 * <li>{@link TemplateCell} which can contain arbitrary content for {@link javax.servlet.jsp.tagext.SimpleTag}</li>
 * </ul>
 * </p>
 * <p>
 * The attribute setters in this class are used to add attributes to the &lt;tbody&gt; tag which will be rendered
 * when row groups are enabled.  When row group rendering is disabled, attributes set here do not render.
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
 * @netui:tag name="rows" body-content="scriptless"
 *            description="Renders the data rows into a NetUI data grid"
 */
public class Rows
    extends AbstractDataGridHtmlTag
    implements IHtmlEvents, IHtmlI18n {

    private boolean _renderRow = true;
    private TBodyTag.State _tbodyTag = new TBodyTag.State();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "Rows";
    }

    /**
     * Sets the onClick JavaScript event for the HTML tbody tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event for the HTML tbody tag."
     */
    public void setOnClick(String onClick) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML tbody tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event for the HTML tbody tag."
     */
    public void setOnDblClick(String onDblClick) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML tbody tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event for the HTML tbody tag."
     */
    public void setOnKeyDown(String onKeyDown) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML tbody tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event for the HTML tbody tag."
     */
    public void setOnKeyUp(String onKeyUp) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event for the HTML tbody tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event for the HTML tbody tag."
     */
    public void setOnKeyPress(String onKeyPress) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML tbody tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event for the HTML tbody tag."
     */
    public void setOnMouseDown(String onMouseDown) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML tbody tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event for the HTML tbody tag."
     */
    public void setOnMouseUp(String onMouseUp) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML tbody tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event for the HTML tbody tag."
     */
    public void setOnMouseMove(String onMouseMove) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML tbody tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event for the HTML tbody tag."
     */
    public void setOnMouseOut(String onMouseOut) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML tbody tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event for the HTML tbody tag."
     */
    public void setOnMouseOver(String onMouseOver) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style of the HTML tbody tag.
     *
     * @param style the style
     * @jsptagref.attributedescription The style of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style of the HTML tbody tag."
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _tbodyTag.style = style;
    }

    /**
     * Sets the style class of the HTML tbody tag.
     *
     * @param styleClass the style class
     * @jsptagref.attributedescription The style class of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class of the HTML tbody tag."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _tbodyTag.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML tbody tag.
     *
     * @param title the title
     * @jsptagref.attributedescription The title for the HTML tbody tag
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title for the HTML tbody tag"
     */
    public void setTitle(String title) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the value of the horizontal alignment attribute of the HTML tbody tag.

     * @param align the horizontal alignment
     * @jsptagref.attributedescription The horizontal alignment of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment of the HTML tbody tag."
     */
    public void setAlign(String align) {
        /* todo: should this enforce left|center|right|justify|char as in the spec */
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the value of the horizontal alignment character attribute of the HTML tbody tag.
     *
     * @param alignChar the horizontal alignment character
     * @jsptagref.attributedescription The horizontal alignment character of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_alignChar</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment character of the HTML tbody tag."
     */
    public void setChar(String alignChar) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAR, alignChar);
    }

    /**
     * Sets the value of the horizontal alignment character offset attribute.
     *
     * @param alignCharOff
     * @jsptagref.attributedescription The horizontal alignment character offset
     * @jsptagref.attributesyntaxvalue <i>string_alignCharOff</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment character offset"
     */
    public void setCharoff(String alignCharOff) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAROFF, alignCharOff);
    }

    /**
     * Sets the value of the vertical alignment attribute of the HTML tbody tag.
     *
     * @param align the alignment
     * @jsptagref.attributedescription The vertical alignment.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The vertical alignment of the HTML tbody tag"
     */
    public void setValign(String align) {
        /* todo: should this enforce top|middle|bottom|baseline as in the spec */
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VALIGN, align);
    }

    /**
     * Sets the value of the language attribute of the HTML tbody tag.
     *
     * @param lang the language
     * @jsptagref.attributedescription The language of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The language of the HTML tbody tag."
     */
    public void setLang(String lang) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the value of the text direction attribute of the HTML tbody tag.
     *
     * @param dir the dir
     * @jsptagref.attributedescription The text direction attribute of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The text direction of the HTML tbody tag.
     */
    public void setDir(String dir) {
        _tbodyTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Set the name of the tagId for the HTML tbody tag.
     *
     * @param tagId - the the name of the tagId for the tbody tag.
     * @jsptagref.attributedescription The tagId of the HTML tbody tag.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the HTML tbody tag."
     */
    public void setTagId(String tagId)
        throws JspException {
        applyTagId(_tbodyTag, tagId);
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
     * Render this tag.  This method renders during the data grid's {@link DataGridTagModel#RENDER_STATE_GRID}
     * state in order to add table rows to the beginning of a data grid's HTML table.  If the data grid is rendering
     * HTML row groups, this tag will output an HTML &lt;tbody&gt; tag.  Then, if this tag is rendering
     * a table row, it will produce an HTML &lt;tr&gt; tag.  Then the content of the body will be rendered.  If
     * table row rendering is disabled, the page author is responsible for rendering the appropriate HTML
     * table row tags as this tag renders inside of the HTML table opened by the data grid.
     * @throws IOException
     * @throws JspException when the {@link DataGridTagModel} can not be found in the {@link JspContext}
     */
    public void doTag()
        throws IOException, JspException {

        JspContext jspContext = getJspContext();
        DataGridTagModel dataGridModel = DataGridUtil.getDataGridTagModel(jspContext);
        if(dataGridModel == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        if(dataGridModel.getRenderState() == DataGridTagModel.RENDER_STATE_GRID) {

            StyleModel styleModel = dataGridModel.getStyleModel();
            assert styleModel != null;

            TableRenderer tableRenderer = dataGridModel.getTableRenderer();
            assert tableRenderer != null;

            InternalStringBuilder content = new InternalStringBuilder();
            AbstractRenderAppender appender = new StringBuilderRenderAppender(content);
            JspFragment fragment = getJspBody();

            if(dataGridModel.isRenderRowGroups())
                tableRenderer.openTableBody(_tbodyTag, appender);

            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            while(dataGridModel.hasNextDataItem()) {

                StringWriter sw = new StringWriter();

                /* first things first -- advance to the next data item */
                dataGridModel.nextDataItem();

                fragment.invoke(sw);

                String trScript = null;
                if(_renderRow) {
                    TrTag.State trState = new TrTag.State();
                    int index = dataGridModel.getCurrentIndex();
                    if(index % 2 == 0)
                        trState.styleClass = styleModel.getRowClass();
                    else trState.styleClass = styleModel.getAltRowClass();

                    if(trState.id != null)
                        trScript = renderNameAndId(request, trState, null);

                    tableRenderer.openTableRow(trState, appender);
                }

                content.append(sw.toString());

                if(_renderRow) {
                    tableRenderer.closeTableRow(appender);

                    if(trScript != null)
                        appender.append(trScript);
                }
            }

            if(dataGridModel.isRenderRowGroups()) {
                tableRenderer.closeTableBody(appender);

                String tbodyScript = null;
                if(_tbodyTag.id != null) {
                    tbodyScript = renderNameAndId(request, _tbodyTag, null);
                }

                if(tbodyScript != null)
                    appender.append(tbodyScript);
            }

            jspContext.getOut().write(content.toString());
        }
    }
}
