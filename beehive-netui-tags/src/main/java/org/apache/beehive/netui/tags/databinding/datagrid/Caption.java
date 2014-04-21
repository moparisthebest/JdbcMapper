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

import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.CaptionTag;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.StyleModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.table.TableRenderer;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The Caption tag is used to render an HTML &lt;caption&gt; inside of a data grid.  The caption
 * tag should be placed inside of a &lt;netui-data:dataGrid&gt; tag and will produce the
 * caption for the HTML table that the dataGrid renders.
 * </p>
 * <p>
 * To set HTML attributes on the rendered caption tag, use the attribute setters defined in this tag
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
 * The Caption tag is used to render an HTML &lt;caption&gt; inside of a data grid.  The caption
 * tag should be placed inside of a &lt;netui-data:dataGrid&gt; tag and will produce the
 * caption for the HTML table that the dataGrid renders.
 * </p>
 * <p>
 * To set HTML attributes on the rendered caption tag, use the attribute setters defined in this tag
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * </ul>
 * </p>
 * @netui:tag name="caption" body-content="scriptless"
 *            description="Renders the caption for the HTML table produced by the data grid tag"
 */
public class Caption
    extends AbstractDataGridHtmlTag {

    private CaptionTag.State _captionTag = new CaptionTag.State();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "Caption";
    }

    /**
     * Sets the onClick JavaScript event for the HTML attribute.
     *
     * @param onClick the onClick event
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event for the HTML caption."
     */
    public void setOnClick(String onClick) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML caption.
     *
     * @param onDblClick the onDblClick event
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event for the HTML caption."
     */
    public void setOnDblClick(String onDblClick) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML caption.
     *
     * @param onKeyDown the onKeyDown event
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event for the HTML caption."
     */
    public void setOnKeyDown(String onKeyDown) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML caption.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event for the HTML caption."
     */
    public void setOnKeyUp(String onKeyUp) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event for the HTML caption.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event for the HTML caption."
     */
    public void setOnKeyPress(String onKeyPress) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML caption.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event for the HTML caption."
     */
    public void setOnMouseDown(String onMouseDown) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML caption.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event for the HTML caption."
     */
    public void setOnMouseUp(String onMouseUp) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML caption.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event for the HTML caption."
     */
    public void setOnMouseMove(String onMouseMove) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML caption.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event for the HTML caption."
     */
    public void setOnMouseOut(String onMouseOut) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML caption.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event for the HTML caption."
     */
    public void setOnMouseOver(String onMouseOver) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style of the HTML caption.
     *
     * @param style the caption attribute.
     * @jsptagref.attributedescription The style of the HTML caption
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style of the HTML caption"
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _captionTag.style = style;
    }

    /**
     * Sets the style class of the HTML caption.
     *
     * @param styleClass the style class
     * @jsptagref.attributedescription The style class of the HTML caption.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the HTML caption"
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _captionTag.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML caption.
     *
     * @param title the title attribute
     * @jsptagref.attributedescription The title attribute for the HTML caption
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title attribute for the HTML caption"
     */
    public void setTitle(String title) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the value of the language attribute for the HTML caption.
     *
     * @param lang the language attribute
     * @jsptagref.attributedescription The language attribute for the HTML caption
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The language attribute for the HTML caption."
     */
    public void setLang(String lang) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the value of the text direction attribute for the HTML caption.
     *
     * @param dir the dir attribute
     * @jsptagref.attributedescription The text direction attribute for the caption
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The cell's text direction"
     */
    public void setDir(String dir) {
        _captionTag.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Set the name of the tagId for the HTML caption.  The page author is responsible for ensuring that the
     * tagId value is unique within the current page scope.
     *
     * @param tagId the the name of the tagId for the caption.
     * @jsptagref.attributedescription The tagId.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
        throws JspException {
        applyTagId(_captionTag, tagId);
    }

    /**
     * <p>
     * Render the caption tag.  This tag renders during the data grid's {@link DataGridTagModel#RENDER_STATE_CAPTION}
     * state and produces an HTML &lt;caption&gt; which contains the result of having evaluated
     * the body of this tag.
     * </p>
     * @throws IOException
     * @throws JspException when the {@link DataGridTagModel} can not be found in the {@link JspContext}
     */
    public void doTag()
            throws IOException, JspException {

        JspContext jspContext = getJspContext();
        DataGridTagModel dgm = DataGridUtil.getDataGridTagModel(jspContext);
        if(dgm == null)
            throw new JspException(Bundle.getErrorString("DataGridTags_MissingDataGridModel"));

        if(dgm.getRenderState() == DataGridTagModel.RENDER_STATE_CAPTION) {
            JspFragment fragment = getJspBody();
            if(fragment != null) {
                String captionScript = null;
                if(_captionTag.id != null) {
                    HttpServletRequest request = JspUtil.getRequest(getJspContext());
                    captionScript = renderNameAndId(request, _captionTag, null);
                }

                StringWriter sw = new StringWriter();
                TableRenderer tableRenderer = dgm.getTableRenderer();
                StyleModel stylePolicy = dgm.getStyleModel();
                AbstractRenderAppender appender = new WriteRenderAppender(jspContext);

                if(_captionTag.styleClass == null)
                    _captionTag.styleClass = stylePolicy.getCaptionClass();

                tableRenderer.openCaption(_captionTag, appender);

                fragment.invoke(sw);
                appender.append(sw.toString());

                tableRenderer.closeCaption(appender);

                if(captionScript != null)
                    appender.append(captionScript);
            }
        }
    }
}
