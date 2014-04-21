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
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.ImageCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell.ImageCellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.IHtmlEvents;
import org.apache.beehive.netui.tags.IHtmlCore;
import org.apache.beehive.netui.tags.IHtmlI18n;

/**
 * <p>
 * Data grid cell that renders an HTML &lt;image&gt; tag containing the tag's <code>source</code> attribute.  The
 * span cell is rendered inside of an HTML table &lt;td&gt;.  The image cell supports various nested tags including
 * those that augment the available attribute set via the NetUI {@link org.apache.beehive.netui.tags.IAttributeConsumer}
 * interface.
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
 * Data grid cell that renders an HTML &lt;image&gt; tag containing the tag's <code>source</code> attribute.  The
 * span cell is rendered inside of an HTML table &lt;td&gt;.  The image cell supports various nested tags including
 * those that augment the available attribute set via the NetUI {@link org.apache.beehive.netui.tags.IAttributeConsumer}
 * interface.
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
 * @netui:tag name="imageCell" body-content="scriptless"
 *            description="Data grid cell tag that renders an HTML img inside of an HTML table cell."
 */
public class ImageCell
    extends AbstractHtmlTableCell
    implements IHtmlCore, IHtmlEvents, IHtmlI18n {

    private static final ImageCellDecorator DECORATOR = new ImageCellDecorator();
    private static final String IMAGE_FACET_NAME = "image";

    private ImageCellModel _imageCellModel = new ImageCellModel();
    private ImageTag.State _imageState = _imageCellModel.getImageState();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "ImageCell";
    }

    /**
     * Sets the onClick JavaScript event on the image tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event on the image tag."
     */
    public void setOnClick(String onClick) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event on the image tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event on the image tag."
     */
    public void setOnDblClick(String onDblClick) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event on the image tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event on the image tag."
     */
    public void setOnKeyDown(String onKeyDown) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event on the image tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event on the image tag."
     */
    public void setOnKeyUp(String onKeyUp) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event on the image tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event on the image tag."
     */
    public void setOnKeyPress(String onKeyPress) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event on the image tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event on the image tag."
     */
    public void setOnMouseDown(String onMouseDown) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event on the image tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event on the image tag."
     */
    public void setOnMouseUp(String onMouseUp) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event on the image tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event on the image tag."
     */
    public void setOnMouseMove(String onMouseMove) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event on the image tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event on the image tag."
     */
    public void setOnMouseOut(String onMouseOut) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event on the image tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event on the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event on the image tag."
     */
    public void setOnMouseOver(String onMouseOver) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style of the rendered html tag.
     *
     * @param style the html style.
     * @jsptagref.attributedescription The style.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style."
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _imageState.style = style;
    }

    /**
     * Sets the style class of the rendered html tag.
     *
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _imageState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute.
     *
     * @param title
     * @jsptagref.attributedescription The title.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title. "
     */
    public void setTitle(String title) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets the lang attribute for the HTML element.
     * @param lang
     * @jsptagref.attributedescription The lang.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The lang."
     */
    public void setLang(String lang)
    {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the dir attribute for the HTML image tag.
     * @param dir
     * @jsptagref.attributedescription The dir for the image tag.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The dir attribhte on the image tag."
     */
    public void setDir(String dir)
    {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Set the image source to render on the HTML image tag.
     *
     * @jsptagref.attributedescription The source of the image to display.
     * @jsptagref.attributesyntaxvalue <i>literal_or_expression_src</i>
     * @netui:attribute required="true" rtexprvalue="true" description="The image source to render on the HTML image tag"
     */
    public void setSrc(String src) {
        _imageState.src = src;
    }

    /**
     * Sets the property to specify where to align the image.
     *
     * @param align the image alignment.
     * @jsptagref.attributedescription The alignment of the image.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The alignment of the image."
     */
    public void setAlign(String align) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the the horizontal spacing around the image.
     *
     * @param hspace the horizontal spacing.
     * @jsptagref.attributedescription The horizontal spacing around the image.
     * @jsptagref.attributesyntaxvalue <i>integer_hspace</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The horizontal spacing around the image."
     */
    public void setHspace(String hspace) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HSPACE, hspace);
    }

    /**
     * Set the vertical spacing around the image.
     *
     * @jsptagref.attributedescription The width of the border around the image.
     * @jsptagref.attributesyntaxvalue <i>integer_border</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The vertical spacing around the image"
     */
    public void setVspace(String vspace) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VSPACE, vspace);
    }

    /**
     * Set the border attribute for the image.
     *
     * @jsptagref.attributedescription The border attribute of the image.
     * @jsptagref.attributesyntaxvalue <i>height</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The border attribute for the image"
     */
    public void setBorder(String border) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.BORDER, border);
    }

    /**
     * Set the height of the image to display.
     *
     * @jsptagref.attributedescription The height of the image to be displayed.
     * @jsptagref.attributesyntaxvalue <i>integer_height</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The height attribute for the image."
     */
    public void setHeight(String height) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HEIGHT, height);
    }

    /**
     * Set the width of the image to display.
     *
     * @jsptagref.attributedescription The width of the image to be displayed.
     * @jsptagref.attributesyntaxvalue <i>width</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The width attribute for the image"
     */
    public void setWidth(String width) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.WIDTH, width);
    }

    /**
     * Sets the property to specify the longdesc of the image tag.
     *
     * @param longdesc the longdesc attribute
     * @jsptagref.attributedescription The alternative text of the image tag
     * @jsptagref.attributesyntaxvalue <i>string_longdesc</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The longdesc of the image tag."
     */
    public void setLongdesc(String longdesc) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LONGDESC, longdesc);
    }

    /**
     * Sets the property to specify the alt text of the image tag.
     *
     * @param alt the image alignment.
     * @jsptagref.attributedescription The alternative text of the image tag
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The alternative text for the image tag."
     */
    public void setAlt(String alt) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALT, alt);
    }

    /**
     * Set the name of the tagId for the image tag.
     *
     * @param tagId the the name of the tagId for the image tag.
     * @jsptagref.attributedescription The tagId.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML image tag."
     */
    public void setTagId(String tagId)
        throws JspException {
        applyIndexedTagId(_imageState, tagId);
    }

    /**
     * <p>
     * Implementation of {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.  This
     * allows a page author to add additional attributes to the HTML rendered by this tag.  The attribute
     * facets which can be consumed include:
     * <ul>
     * <li><code>image</code> -- attributes set using this facet will be rendered as HTML attributes on the
     * rendered HTML &lt;image&gt; tag.</li>
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
        if(facet == null || facet.equals(IMAGE_FACET_NAME))
            super.addStateAttribute(_imageState, name, value);
        else
            super.setAttribute(name, value, facet);
    }

    /**
     * Render the cell's contents.  This method implements support for executing the image cell's decorator.
     *
     * @param appender the {@link AbstractRenderAppender} used to collect the rendered output
     * @param jspFragmentOutput the String result of having evaluated the image cell's {@link javax.servlet.jsp.tagext.JspFragment}
     */
    protected void renderDataCellContents(AbstractRenderAppender appender, String jspFragmentOutput) {
        /* render any JavaScript needed to support framework features */
        if (_imageState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            String script = renderNameAndId(request, _imageState, null);
            _imageCellModel.setJavascript(script);
        }

        DECORATOR.decorate(getJspContext(), appender, _imageCellModel);
    }

    /**
     * Implementation of {@link AbstractHtmlTableCell#internalGetCellModel()} that exposes the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.ImageCellModel}
     * which is storing state for this tag.
     * @return this tag's image cell model
     */
    protected CellModel internalGetCellModel() {
        return _imageCellModel;
    }
}