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
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.tags.IHtmlEvents;
import org.apache.beehive.netui.tags.IHtmlI18n;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.tags.rendering.TFootTag;
import org.apache.beehive.netui.tags.rendering.TrTag;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The Footer tag is used to render a table row after the data grid has rendered the
 * header and rows regions demarcated by the {@link Header} and {@link Rows} tags respectively.
 * The footer tag can also optionally render a &lt;tfoot&gt; HTML tag if the data grid is rendering
 * HTML table row groups.  To enable this functionality, set the {@link DataGrid#setRenderRowGroups(boolean)}
 * attribute.  The location of the footer tag inside of a data grid does not affect when its content renders.
 * </p>
 * <p>
 * The attribute setters for the footer tag are used to add HTML attributes to the &lg;tfoot&gt; HTML
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
 * @jsptagref.tagdescription
 * <p>
 * The Footer tag is used to render a table row after the data grid has rendered the
 * header and rows regions demarcated by the {@link Header} and {@link Rows} tags respectively.
 * The footer tag can also optionally render a &lt;tfoot&gt; HTML tag if the data grid is rendering
 * HTML table row groups.  To enable this functionality, set the {@link DataGrid#setRenderRowGroups(boolean)}
 * attribute.  The location of the footer tag inside of a data grid does not affect when its content renders.
 * </p>
 * <p>
 * The attribute setters for the footer tag are used to add HTML attributes to the &lg;tfoot&gt; HTML
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
 * @netui:tag name="footer" body-content="scriptless" description="Renders a footer into a NetUI data grid"
 */
public class Footer
    extends AbstractDataGridHtmlTag
    implements IHtmlEvents, IHtmlI18n {

    private boolean _renderRow = true;
    private TFootTag.State _tfootTag = new TFootTag.State();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "Footer";
    }

    /**
     * Sets the onClick JavaScript event for the HTML tfoot.
     *
     * @param onClick the onClick event
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event for the HTML tfoot."
     */
    public void setOnClick(String onClick) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML tfoot.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event for the HTML tfoot."
     */
    public void setOnDblClick(String onDblClick) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML tfoot.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event for the HTML tfoot."
     */
    public void setOnKeyDown(String onKeyDown) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML tfoot.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event for the HTML tfoot."
     */
    public void setOnKeyUp(String onKeyUp) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event for the HTML tfoot."
     */
    public void setOnKeyPress(String onKeyPress) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML tfoot.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event for the HTML tfoot."
     */
    public void setOnMouseDown(String onMouseDown) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML tfoot.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event for the HTML tfoot."
     */
    public void setOnMouseUp(String onMouseUp) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML tfoot.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event for the HTML tfoot."
     */
    public void setOnMouseMove(String onMouseMove) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML tfoot.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event for the HTML tfoot."
     */
    public void setOnMouseOut(String onMouseOut) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML tfoot.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML tfoot.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event for the HTML tfoot."
     */
    public void setOnMouseOver(String onMouseOver) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style of the HTML tfoot tag.
     *
     * @param style the style
     * @jsptagref.attributedescription The style for the HTML tfoot tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style for the HTML tfoot tag."
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _tfootTag.style = style;
    }

    /**
     * Sets the style class of the rendered html tag.
     *
     * @param styleClass the style class
     * @jsptagref.attributedescription The style class of the HTML tfoot tag.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class of the HTML tfoot tag."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _tfootTag.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute of the HTML tfoot tag.
     *
     * @param title
     * @jsptagref.attributedescription The title of the HTML tfoot tag.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title of the HTML tfoot tag."
     */
    public void setTitle(String title) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the value of the horizontal align attribute of the HTML tfoot tag.
     *
     * @param align the alignment
     * @jsptagref.attributedescription The horizontal alignment of the HTML tfoot tag.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment of the HTML tfoot tag"
     */
    public void setAlign(String align) {
        /* todo: should this enforce left|center|right|justify|char as in the spec? */
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the value of the horizontal alignment character attribute of the HTML tfoot tag.
     *
     * @param alignChar the alignment character
     * @jsptagref.attributedescription The horizontal alignment character of the HTML tfoot tag
     * @jsptagref.attributesyntaxvalue <i>string_alignChar</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's horizontal alignment character of the HTML tfoot tag"
     */
    public void setChar(String alignChar) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAR, alignChar);
    }

    /**
     * Sets the value of the horizontal alignment character offset attribute of the HTML tfoot tag
     *
     * @param alignCharOff the character alignment offset
     * @jsptagref.attributedescription The horizontal alignment character offset of the HTML tfoot tag.
     * @jsptagref.attributesyntaxvalue <i>string_alignCharOff</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The horizontal alignment character offset of the HTML tfoot tag"
     */
    public void setCharoff(String alignCharOff) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHAROFF, alignCharOff);
    }

    /**
     * Sets the value of the vertical alignment attribute.
     *
     * @param align
     * @jsptagref.attributedescription The vertical alignment.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's vertical alignment"
     */
    public void setValign(String align) {
        /* todo: should this enforce top|middle|bottom|baseline as in the spec? */
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VALIGN, align);
    }

    /**
     * Sets the value of the language attribute.
     *
     * @param lang
     * @jsptagref.attributedescription The language.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's language"
     */
    public void setLang(String lang) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the value of the text direction attribute.
     *
     * @param dir
     * @jsptagref.attributedescription The text direction attribute.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's text direction"
     */
    public void setDir(String dir) {
        _tfootTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
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
     * Set the name of the tagId for the HTML tfoot tag.
     *
     * @param tagId the tag id
     * @jsptagref.attributedescription The tagId of the HTML tfoot tag.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     *                  description="String value. Sets the id (or name) attribute of the HTML tfoot tag."
     */
    public void setTagId(String tagId)
        throws JspException {
        applyTagId(_tfootTag, tagId);
    }

    /**
     * Render this tag.  This method renders during the data grid's {@link DataGridTagModel#RENDER_STATE_FOOTER}
     * state in order to add table rows to the end of a data grid's HTML table.  If the data grid is rendering
     * HTML row groups, this tag will output an HTML &lt;tfoot&gt; tag.  Then, if this tag is rendering
     * a table row, it will produce an HTML &lt;tr&gt; tag.  Then the content of the body will be rendered.  If
     * table row rendering is disabled, the page author is responsible for rendering the appropriate HTML
     * table row tags as this tag renders inside of the HTML table opened by the data grid.
     * @throws IOException
     * @throws JspException when the {@link DataGridTagModel} can not be found in the {@link JspContext}
     */
    public void doTag()
            throws IOException, JspException {

        JspContext jspContext = getJspContext();

        DataGridTagModel dgm = DataGridUtil.getDataGridTagModel(jspContext);
        if(dgm == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        if(dgm.getRenderState() == DataGridTagModel.RENDER_STATE_FOOTER) {
            JspFragment fragment = getJspBody();
            if(fragment != null) {

                StringWriter sw = new StringWriter();

                TableRenderer tableRenderer = dgm.getTableRenderer();
                assert tableRenderer != null;

                StyleModel styleModel = dgm.getStyleModel();
                assert styleModel != null;

                AbstractRenderAppender appender = new WriteRenderAppender(jspContext);

                if(dgm.isRenderRowGroups()) {
                    if(_tfootTag.styleClass == null)
                        _tfootTag.styleClass = styleModel.getTableFootClass();
                    tableRenderer.openTableFoot(_tfootTag, appender);
                }

                TrTag.State trState = null;
                if(_renderRow) {
                    trState = new TrTag.State();
                    trState.styleClass = styleModel.getFooterRowClass();
                    tableRenderer.openFooterRow(trState, appender);
                }

                fragment.invoke(sw);
                appender.append(sw.toString());

                if(_renderRow) {
                    assert trState != null;
                    tableRenderer.closeFooterRow(appender);
                }

                if(dgm.isRenderRowGroups()) {
                    tableRenderer.closeTableFoot(appender);

                    String tfootScript = null;
                    if(_tfootTag.id != null) {
                        HttpServletRequest request = JspUtil.getRequest(getJspContext());
                        tfootScript = renderNameAndId(request, _tfootTag, null);
                    }

                    if(tfootScript != null)
                        appender.append(tfootScript);
                }
            }
        }
    }
}
