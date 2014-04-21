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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.TrTag;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

/**
 * <p>
 * This tag is optionally used to render HTML table roe tags inside of one of the data grid tags that
 * are used to denote data grid row rendering boundaries.  When the {@link Header}, {@link Rows}, or {@link Footer}
 * have their <code>renderRow</code> attribute set to <code>false</code>, no HTML table row element will render
 * before starting to render the body of one of these tags.  This tag should be used when <code>renderRows</code>
 * is <code>false</code> in order to render an HTML table row.  The Row tag is used this way in order to
 * allow a page author to set JSP tag attributes that can configure each rendered table row differently.
 * For example:
 * <pre>
 *   &lt;netui-data:rows renderRows="false">
 *     &lt;netui-data:row styleClass="rowStyle${container.index}">
 *       &lt;netui-data:spanCell value="${container.item}"/>
 *     &lt;/netui-data:row>
 *   &lt;/netui-data:rows>
 * </pre>
 * and a data set containing ["foo", "bar", "baz"] will render:
 * <pre>
 *   &lt;tr class="rowStyle0">foo&lt;/tr>
 *   &lt;tr class="rowStyle1">bar&lt;/tr>
 *   &lt;tr class="rowStyle2">baz&lt;/tr>
 * </pre>
 * If the &lt;netui-data:row$gt; were omitted, none of the &lt;tr&gt; elements would be rendered in the output.  Note,
 * this tag <b>should not</b> be used inside of the Header, Rows, or Footer tags unless their <code>renderRow</code>
 * attribute is set to <code>false</code>
 * </p>
 * @jsptagref.tagdescription
 * <p>
 * This tag is optionally used to render HTML table roe tags inside of one of the data grid tags that
 * are used to denote data grid row rendering boundaries.  When the {@link Header}, {@link Rows}, or {@link Footer}
 * have their <code>renderRow</code> attribute set to <code>false</code>, no HTML table row element will render
 * before starting to render the body of one of these tags.  This tag should be used when <code>renderRows</code>
 * is <code>false</code> in order to render an HTML table row.  The Row tag is used this way in order to
 * allow a page author to set JSP tag attributes that can configure each rendered table row differently.
 * For example:
 * <pre>
 *   &lt;netui-data:rows renderRows="false">
 *     &lt;netui-data:row styleClass="rowStyle${container.index}">
 *       &lt;netui-data:spanCell value="${container.item}"/>
 *     &lt;/netui-data:row>
 *   &lt;/netui-data:rows>
 * </pre>
 * and a data set containing ["foo", "bar", "baz"] will render:
 * <pre>
 *   &lt;tr class="rowStyle0">foo&lt;/tr>
 *   &lt;tr class="rowStyle1">bar&lt;/tr>
 *   &lt;tr class="rowStyle2">baz&lt;/tr>
 * </pre>
 * If the &lt;netui-data:row$gt; were omitted, none of the &lt;tr&gt; elements would be rendered in the output.  Note,
 * this tag <b>should not</b> be used inside of the Header, Rows, or Footer tags unless their <code>renderRow</code>
 * attribute is set to <code>false</code>
 * </p>
 * @netui:tag name="row" body-content="scriptless"
 *            description="Tag optionally used inside of a Header, Rows, or Footer tag to render HTML table row elements."
 */
public class Row
    extends AbstractDataGridHtmlTag {

    private TrTag.State _trState = new TrTag.State();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public final String getTagName() {
        return "Row";
    }

    /**
     * Sets the onClick JavaScript for the HTML tr tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript for the HTML tr tag."
     */
    public void setOnClick(String onClick) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript for the HTML tr tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript for the HTML tr tag."
     */
    public void setOnDblClick(String onDblClick) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript for the HTML tr tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript for the HTML tr tag."
     */
    public void setOnKeyDown(String onKeyDown) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript for the HTML tr tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript for the HTML tr tag."
     */
    public void setOnKeyUp(String onKeyUp) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript for the HTML tr tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript for the HTML tr tag."
     */
    public void setOnKeyPress(String onKeyPress) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript for the HTML tr tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript for the HTML tr tag."
     */
    public void setOnMouseDown(String onMouseDown) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript for the HTML tr tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript for the HTML tr tag."
     */
    public void setOnMouseUp(String onMouseUp) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript for the HTML tr tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript for the HTML tr tag."
     */
    public void setOnMouseMove(String onMouseMove) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript for the HTML tr tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript for the HTML tr tag."
     */
    public void setOnMouseOut(String onMouseOut) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript for the HTML tr tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript for the HTML tr tag."
     */
    public void setOnMouseOver(String onMouseOver) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style attribute for the HTML tr tag.
     *
     * @param style the style
     * @jsptagref.attributedescription The style attribute for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     *                  description="The style attribute for the HTML tr tag."
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _trState.style = style;
    }

    /**
     * Sets the style class for the HTML tr tag.
     *
     * @param styleClass the style class.
     * @jsptagref.attributedescription The style class for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the HTML tr tag."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _trState.styleClass = styleClass;
    }

    /**
     * Sets the title attribute for the HTML tr tag.
     *
     * @param title the title
     * @jsptagref.attributedescription The title for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title for the HTML tr tag."
     */
    public void setTitle(String title) {
        _trState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the lang attribute for the HTML tr tag.
     * @param lang the lang
     * @jsptagref.attributedescription The lang for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The lang for the HTML tr tag."
     */
    public void setLang(String lang)
    {
        _trState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the lang attribute for the HTML element tr tag.
     * @param dir the dir
     * @jsptagref.attributedescription The dir for the HTML tr tag.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The dir for the HTML tr tag.
     */
    public void setDir(String dir)
    {
        _trState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Set the name of the tagId for the HTML tr tag.
     *
     * @param tagId the the name of the tagId for the table row.
     * @jsptagref.attributedescription The tagId for the HTML tr tat.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the HTML tr tag."
     */
    public void setTagId(String tagId)
        throws JspException {
        JspContext jspContext = getJspContext();
        DataGridTagModel dataGridModel = DataGridUtil.getDataGridTagModel(jspContext);
        if(dataGridModel == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        int renderState = dataGridModel.getRenderState();

        if(renderState == DataGridTagModel.RENDER_STATE_GRID)
            applyIndexedTagId(_trState, tagId);
        else applyTagId(_trState, tagId);
    }

    /**
     * <p>
     * Render this tag.  This tag renders during the data grid's {@link DataGridTagModel#RENDER_STATE_HEADER},
     * {@link DataGridTagModel#RENDER_STATE_GRID}, and the {@link DataGridTagModel#RENDER_STATE_FOOTER} render
     * states.  This tag will always render the an HTML table row tag and its body.  The result is added
     * to the output stream.
     * </p>
     * <p>
     * Unless the {@link #setStyleClass(String)} attribute has been set and is non-null, the following style
     * classes are used during the various supported rendering states:
     * <table>
     * <tr><td>Render State</td><td>Style Class</td></tr>
     * <tr><td>{@link DataGridTagModel#RENDER_STATE_HEADER}</td>
     * <td>{@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getHeaderRowClass()}</td></tr>
     * <tr><td>{@link DataGridTagModel#RENDER_STATE_GRID}</td>
     * <td>{@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getRowClass()} for
     * an even row {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getAltRowClass()}
     * for odd rows.</td></tr>
     * <tr><td>{@link DataGridTagModel#RENDER_STATE_FOOTER}</td>
     * <td>{@link org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel#getFooterRowClass()}</td></tr>
     * </table>
     * </p>
     * @throws JspException when the {@link DataGridTagModel} can not be found in the {@link JspContext}
     * @throws IOException
     */
    public void doTag()
        throws JspException, IOException {

        JspContext jspContext = getJspContext();
        DataGridTagModel dataGridModel = DataGridUtil.getDataGridTagModel(jspContext);
        if(dataGridModel == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        int renderState = dataGridModel.getRenderState();

        if(!(renderState == DataGridTagModel.RENDER_STATE_HEADER ||
           renderState != DataGridTagModel.RENDER_STATE_GRID ||
           renderState != DataGridTagModel.RENDER_STATE_FOOTER))
            return;

        JspFragment fragment = getJspBody();

        StyleModel styleModel = dataGridModel.getStyleModel();
        assert styleModel != null;

        TableRenderer tableRenderer = dataGridModel.getTableRenderer();
        assert tableRenderer != null;

        HttpServletRequest request = JspUtil.getRequest(getJspContext());
        InternalStringBuilder content = new InternalStringBuilder();
        AbstractRenderAppender appender = new StringBuilderRenderAppender(content);

        if(_trState.styleClass == null) {
            if(renderState == DataGridTagModel.RENDER_STATE_GRID) {
                int index = dataGridModel.getCurrentIndex();
                if(index % 2 == 0)
                    _trState.styleClass = styleModel.getRowClass();
                else _trState.styleClass = styleModel.getAltRowClass();
            }
            else if(renderState == DataGridTagModel.RENDER_STATE_HEADER)
                _trState.styleClass = styleModel.getHeaderRowClass();
            else if(renderState == DataGridTagModel.RENDER_STATE_FOOTER)
                _trState.styleClass = styleModel.getFooterRowClass();
            else assert false : "Attempting to apply style information during an invalid render state";
        }

        String trScript = null;
        if(_trState.id != null)
            trScript = renderNameAndId(request, _trState, null);

        tableRenderer.openTableRow(_trState, appender);

        StringWriter sw = new StringWriter();
        if(fragment != null)
            fragment.invoke(sw);
        appender.append(sw.toString());

        tableRenderer.closeTableRow(appender);

        if(trScript != null)
            appender.append(trScript);

        jspContext.getOut().write(content.toString());

        return;
    }
}
