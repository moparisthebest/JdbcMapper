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

import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.SpanCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell.SpanCellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.html.IFormattable;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.SpanTag;
import org.apache.beehive.netui.tags.IHtmlCore;
import org.apache.beehive.netui.tags.IHtmlI18n;
import org.apache.beehive.netui.tags.IHtmlEvents;

/**
 * <p>
 * Data grid cell that renders an HTML &lt;span&gt; tag containing the tag's <code>value</code> attribute.  The
 * span cell is rendered inside of an HTML table &lt;td&gt;.  The span cell supports various nested tags including
 * those that provide formatting via the NetUI {@link IFormattable} interface and those that augment the
 * available attribute set via the NetUI {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * <li><code>container</code> -- the {@link org.apache.beehive.netui.script.common.IDataAccessProvider} instance
 * that exposes the current data item and the current item's index</li>
 * </ul>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * Data grid cell that renders an HTML &lt;span&gt; tag containing the tag's <code>value</code> attribute.  The
 * span cell is rendered inside of an HTML table &lt;td&gt;.  The span cell supports various nested tags including
 * those that provide formatting via the NetUI {@link IFormattable} interface and those that augment the
 * available attribute set via the NetUI {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.
 * </p>
 * <p>
 * The set of JSP implicit objects available to the body include:
 * <ul>
 * <li><code>dataGridModel</code> -- the {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel}
 * for the cell's containing data grid.</li>
 * <li><code>container</code> -- the {@link org.apache.beehive.netui.script.common.IDataAccessProvider} instance
 * that exposes the current data item and the current item's index</li>
 * </ul>
 * </p>
 *
 * @netui:tag name="spanCell" body-content="scriptless"
 *            description="Data grid cell that renders its value attribute into an HTML &lt;span&gt;"
 */
public class SpanCell
    extends AbstractHtmlTableCell
    implements IFormattable, IHtmlCore, IHtmlEvents, IHtmlI18n {

    private static final SpanCellDecorator DECORATOR = new SpanCellDecorator();
    private static final String SPAN_FACET_NAME = "span";

    private SpanCellModel _spanCellModel = new SpanCellModel();
    private SpanTag.State _spanState = _spanCellModel.getSpanState();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "SpanCell";
    }

    /**
     * Sets the onClick JavaScript event for the HTML span.
     *
     * @param onClick the onClick event for the HTML span.
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event."
     */
    public void setOnClick(String onClick) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML span.
     *
     * @param onDblClick the onDblClick event for the HTML span.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event for the HTML span."
     */
    public void setOnDblClick(String onDblClick) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML span.
     *
     * @param onKeyDown the onKeyDown event for the HTML span.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event for the HTML span."
     */
    public void setOnKeyDown(String onKeyDown) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML span.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event for the HTML span."
     */
    public void setOnKeyUp(String onKeyUp) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event for the HTML span.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event for the HTML span."
     */
    public void setOnKeyPress(String onKeyPress) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML span.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event for the HTML span."
     */
    public void setOnMouseDown(String onMouseDown) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML span.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event for the HTML span."
     */
    public void setOnMouseUp(String onMouseUp) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML span.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event for the HTML span."
     */
    public void setOnMouseMove(String onMouseMove) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML span.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event for the HTML span."
     */
    public void setOnMouseOut(String onMouseOut) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML span.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event for the HTML span."
     */
    public void setOnMouseOver(String onMouseOver) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style for the HTML span.
     *
     * @param style the html style.
     * @jsptagref.attributedescription The style for the HTML span
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style for the HTML span"
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _spanState.style = style;
    }

    /**
     * Sets the style class for the HTML span.
     *
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class for the HTML span
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the HTML span."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _spanState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML span.
     *
     * @param title
     * @jsptagref.attributedescription The title for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title for the HTML span."
     */
    public void setTitle(String title) {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the lang attribute for the HTML span.
     * @param lang
     * @jsptagref.attributedescription The lang for the HTML span.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The lang for the HTML span"
     */
    public void setLang(String lang)
    {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the dir attribute for the HTML span.
     * @param dir
     * @jsptagref.attributedescription The dir.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The dir for the HTML span."
     */
    public void setDir(String dir)
    {
        _spanState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * <p>
     * Set the name of the tagId for the HTML span.  This value will have the index of the current data item
     * in the data set appended to the tagId so that the identifiers are unique in the rendered JSP.
     * </p>
     * <p>
     * For example, if a data set contains {"foo", "bar", "baz"} rendered using a spanCell with a tagId
     * "theTagId", the resulting tagId values will be {"theTagId0", "theTagId1", "theTagId2"}.
     *
     * @param tagId the the name of the tagId for the HTML span.
     * @jsptagref.attributedescription The tagId.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML span."
     */
    public void setTagId(String tagId)
        throws JspException {
        applyIndexedTagId(_spanState, tagId);
    }

    /**
     * <p>
     * Set the value rendered inside of the HTML span.  This is the visible text that is displayed in the HTML
     * rendered by this tag.
     * </p>
     *
     * @jsptagref.attributedescription The value of the visible text rendered inside of the HTML span.
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setValue(Object value) {
        _spanCellModel.setValue(value);
    }

    /**
     * <p>
     * Implementation of {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.  This
     * allows a page author to add additional attributes to the HTML rendered by this tag.  The attribute
     * facets which can be consumed include:
     * <ul>
     * <li><code>span</code> -- attributes set using this facet will be rendered as HTML attributes on the
     * rendered HTML &lt;span&gt; tag.</li>
     * </ul>
     * </p>
     * <p>
     * This tag also accepts facets supported by {@link AbstractHtmlTableCell#setAttribute(String, String, String)}
     * </p>
     *
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @param facet the facet for the attribute; this value must be match one of the facets supported by the JSP tags
     * @throws JspException thrown when the given facet String is not recognized as a valid facet name
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException {
        if(facet == null || facet.equals(SPAN_FACET_NAME))
            super.addStateAttribute(_spanState, name, value);
        else
            super.setAttribute(name, value, facet);
    }

    /**
     * Render the cell's contents.  This method implements support for executing the span cell's decorator.
     * @param appender the {@link AbstractRenderAppender} used to collect the rendered output
     * @param jspFragmentOutput the String result of having evaluated the span cell's {@link javax.servlet.jsp.tagext.JspFragment}
     */
    protected void renderDataCellContents(AbstractRenderAppender appender, String jspFragmentOutput) {
        /* render any JavaScript needed to support framework features */
        if (_spanState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            String script = renderNameAndId(request, _spanState, null);
            if(script != null)
                _spanCellModel.setJavascript(script);
        }

        DECORATOR.decorate(getJspContext(), appender, _spanCellModel);
    }

    /**
     * Implementation of {@link AbstractHtmlTableCell#internalGetCellModel()} that exposes the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.SpanCellModel}
     * which is storing state for this tag.
     * @return this tag's span cell model
     */
    protected CellModel internalGetCellModel() {
        return _spanCellModel;
    }
}
