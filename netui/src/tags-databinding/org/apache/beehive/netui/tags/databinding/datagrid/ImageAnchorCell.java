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

import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.html.IFormattable;
import org.apache.beehive.netui.tags.html.IUrlParams;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.CellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.ImageAnchorCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell.ImageAnchorCellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.util.ParamHelper;

/**
 * <p>
 * This tag is a data grid cell used to render an HTML image inside of an anchor.  This tag should be used
 * inside of a &lt;netui-data:rows&gt; tag when rendering a data set with the &lt;netui-data:dataGrid&gt; tag.
 * The rendered output is structured as:
 * <pre>
 *   &lt;a ...>&lt;img .../></a>
 * </pre>
 * If the {@link #setHref(String)} attribute is set, the href will be rendered on the anchor.  If the {@link #setAction(String)}
 * attribute is set, it must reference an action that is valid in the context of the current Page Flow.  Only
 * one of these two attributes may be set. The image source to render on the image tag is specified using the
 * {@link #setSrc(String)} attribute.
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
 * This tag is a data grid cell used to render an HTML image inside of an anchor.  This tag should be used
 * inside of a &lt;netui-data:rows&gt; tag when rendering a data set with the &lt;netui-data:dataGrid&gt; tag.
 * The rendered output is structured as:
 * <pre>
 *   &lt;a ...>&lt;img .../></a>
 * </pre>
 * If the {@link #setHref(String)} attribute is set, the href will be rendered on the anchor.  If the {@link #setAction(String)}
 * attribute is set, it must reference an action that is valid in the context of the current Page Flow.  Only
 * one of these two attributes may be set. The image source to render on the image tag is specified using the
 * {@link #setSrc(String)} attribute.
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
 * @netui:tag name="imageAnchorCell" body-content="scriptless"
 *            description="Renders an HTML table cell in a data grid that contains an HTML image inside of an anchor."
 */
public class ImageAnchorCell
    extends AbstractHtmlTableCell
    implements IFormattable, IUrlParams {

    /*
      todo: support rolloverImage on the <img> tags
     */

    private static final ImageAnchorCellDecorator DECORATOR = new ImageAnchorCellDecorator();
    private static final String IMAGE_FACET_NAME = "image";
    private static final String ANCHOR_FACET_NAME = "anchor";

    private ImageAnchorCellModel _imageAnchorCellModel = new ImageAnchorCellModel();
    private AnchorTag.State _anchorState = _imageAnchorCellModel.getAnchorState();
    private ImageTag.State _imageState = _imageAnchorCellModel.getImageState();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "ImageAnchorCell";
    }

    /**
     * Sets the onClick JavaScript for the HTML anchor tag.
     *
     * @param onClick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript for the HTML anchor tag."
     */
    public void setOnClick(String onClick) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript for the HTML anchor tag.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript for the HTML anchor tag."
     */
    public void setOnDblClick(String onDblClick) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript for the HTML anchor tag.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript for the HTML anchor tag."
     */
    public void setOnKeyDown(String onKeyDown) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript for the HTML anchor tag.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript for the HTML anchor tag."
     */
    public void setOnKeyUp(String onKeyUp) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript for the HTML anchor tag.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript for the HTML anchor tag."
     */
    public void setOnKeyPress(String onKeyPress) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript for the HTML anchor tag.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript for the HTML anchor tag."
     */
    public void setOnMouseDown(String onMouseDown) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript for the HTML anchor tag.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript for the HTML anchor tag."
     */
    public void setOnMouseUp(String onMouseUp) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript for the HTML anchor tag.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript for the HTML anchor tag."
     */
    public void setOnMouseMove(String onMouseMove) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript for the HTML anchor tag.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript for the HTML anchor tag."
     */
    public void setOnMouseOut(String onMouseOut) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript for the HTML anchor tag.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript for the HTML anchor tag."
     */
    public void setOnMouseOver(String onMouseOver) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style of the rendered HTML anchor tag.
     *
     * @param style the html style.
     * @jsptagref.attributedescription The style for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style."
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _anchorState.style = style;
    }

    /**
     * Sets the style class of the rendered HTML anchor tag.
     *
     * @param styleClass the style class.
     * @jsptagref.attributedescription The style class for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the HTML anchor tag."
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _anchorState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML anchor tag.
     *
     * @param title the title
     * @jsptagref.attributedescription The title for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title for the HTML anchor tag."
     */
    public void setTitle(String title) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }


    /* ---------------------------------------------------------

        Anchor specifc attributes

       --------------------------------------------------------- */

    /**
     * Sets <code>charset</code> attribute for the HTML anchor tag
     *
     * @param charSet the charset
     * @jsptagref.attributedescription The character set for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_charset</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     *                  description="The character set for the HTML anchor tag."
     */
    public void setCharSet(String charSet) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHARSET, charSet);
    }

    /**
     * Sets <code>type</code> attribute for the HTML anchor tag.
     *
     * @param type the type
     * @jsptagref.attributedescription The type attribute for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The type attribute for the HTML anchor tag."
     */
    public void setType(String type) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TYPE, type);
    }

    /**
     * Sets <code>hreflang</code> attribute for the HTML anchor tag.
     *
     * @param hreflang the hreflang.
     * @jsptagref.attributedescription The hreflang attribute for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_hreflang</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The hreflang attribute for the HTML anchor tag."
     */
    public void setHrefLang(String hreflang) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HREFLANG, hreflang);
    }

    /**
     * Sets <code>rel</code> attribute for the HTML anchor tag.
     *
     * @param rel the rel attribute
     * @jsptagref.attributedescription The rel attribute for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_rel</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The rel attribute for the HTML anchor tag."
     */
    public void setRel(String rel) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.REL, rel);
    }

    /**
     * Sets <code>rev</code> attribute for the HTML anchor tag.
     *
     * @param rev the rev attribute.
     * @jsptagref.attributedescription The rev attribute for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_rev</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The rev attribute for the HTML anchor tag."
     */
    public void setRev(String rev) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.REV, rev);
    }


    /**
     * Sets the window target for the HTML anchor tag.
     *
     * @param target the window target
     * @jsptagref.attributedescription The window target for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The window target for the HTML anchor tag."
     */
    public void setTarget(String target) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TARGET, target);
    }

    /**
     * Sets the href of the HTML anchor tag. This attribute will accept the empty String as a legal value.
     *
     * @param href the hyperlink URI for the anchor.
     * @jsptagref.attributedescription The HREF for the HTML anchor tag.
     * @jsptagref.attributesyntaxvalue <i>string_href</i>
     * @netui:attribute required="false" rtexprvalue="true" reftype="url"
     * description="The HREF for the HTML anchor tag."
     */
    public void setHref(String href) {
        _imageAnchorCellModel.setHref(href);
    }

    /**
     * Set the target "scope" for the anchor's action.  Multiple active page flows may exist concurrently within named
     * scopes.  This attribute selects which named scope to use.  If omitted, the default scope is assumed.
     *
     * @param targetScope the name of the target scope in which the associated action's page flow resides.
     * @jsptagref.attributedescription The target scope in which the associated action's page flow resides.
     * @jsptagref.attributesyntaxvalue <i>string_targetScope</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The target scope in which the associated action's page flow resides"
     */
    public void setTargetScope(String targetScope) {
        _imageAnchorCellModel.setScopeId(targetScope);
    }

    /**
     * Set the name of the action for the HTML anchor tag.  This action name must be valid given the
     * current Page Flow.
     *
     * @param action the name of the action to set for the anchor tag.
     * @jsptagref.attributedescription The action method to invoke.  The action method must valid given the
     * ccurrent Page Flow.
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The action method to invoke.  The action method must be in the Controller file of the Page Flow directory."
     */
    public void setAction(String action)
            throws JspException {
        _imageAnchorCellModel.setAction(setRequiredValueAttribute(action, "action"));
    }

    /* ---------------------------------------------------------

        Image specifc attributes

       --------------------------------------------------------- */

    /**
     * Sets the alignment attribute for the HTML image tag.
     *
     * @param align the image alignment.
     * @jsptagref.attributedescription The alignment attribute for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The alignment for the HTML image tag."
     */
    public void setAlign(String align) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALIGN, align);
    }

    /**
     * Sets the alt text attribute for the HTML image tag.
     *
     * @param alt the alt attribute.
     * @jsptagref.attributedescription The alternative text of the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The alternative text of the HTML image tag."
     */
    public void setAlt(String alt) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ALT, alt);
    }

    /**
     * Sets the longdesc attribute for the HTML image tag.
     *
     * @param longdesc the longdesc attribute
     * @jsptagref.attributedescription The longdesc attribute for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="the longdesc for the HTML image tag."
     */
    public void setLongdesc(String longdesc) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LONGDESC, longdesc);
    }

    /**
     * Sets the border size attribute for the HTML image tag.
     *
     * @param border the border size.
     * @jsptagref.attributedescription The border size attribute for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>integer_pixelBorder</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The border size attribute for the HTML image tag."
     */
    public void setBorder(String border) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.BORDER, border);
    }

    /**
     * Sets the image height attribute for the HTML image tag.
     *
     * @param height the height.
     * @jsptagref.attributedescription The image height attribute for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>integer_height</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The image height attribute for the HTML image tag."
     */
    public void setHeight(String height) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HEIGHT, height);
    }

    /**
     * Sets the the horizontal spacing attribute for the HTML image tag.
     *
     * @param hspace the horizontal spacing.
     * @jsptagref.attributedescription The horizontal spacing for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>integer_hspace</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The horizontal spacing for the HTML image tag."
     */
    public void setHspace(String hspace) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HSPACE, hspace);
    }

    /**
     * Sets the server-side image map declaration for the HTML image tag.
     *
     * @param ismap the image map declaration.
     * @jsptagref.attributedescription The server-side map declaration for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_isMap</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The server-side map declaration for the HTML image tag."
     */
    public void setIsmap(String ismap) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ISMAP, ismap);
    }

    /**
     * Sets the image source URI for the HTML image tag.
     *
     * @param src the image source URI.
     * @jsptagref.attributedescription The image source URI for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_src</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The image source URI for the HTML image tag" reftype="img-url"
     */
    public void setSrc(String src)
            throws JspException {
        _imageState.src = src;
    }

    /**
     * Sets the client-side image map declaration for the HTML iage tag.
     *
     * @param usemap the map declaration.
     * @jsptagref.attributedescription The client-side image map declaration for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_useMap</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The client-side image map declaration for the HTML image tag."
     */
    public void setUsemap(String usemap) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.USEMAP, usemap);
    }

    /**
     * Sets the vertical spacing around the HTML image tag.
     *
     * @param vspace the vertical spacing.
     * @jsptagref.attributedescription The vertical spacing around the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_vspace</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The vertical spacing around the HTML image tag."
     */
    public void setVspace(String vspace) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.VSPACE, vspace);
    }

    /**
     * Set the &lt;img> style for the contained image. When the tag library is
     * running in legacy mode, this will override the <code>style</code> attribute if that is
     * set.  If this is not set, and <code>style</code> is set, then it will be applied to
     * the image.
     *
     * @param imageStyle the image style
     * @jsptagref.attributedescription The style for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style for the HTML image tag."
     */
    public void setImageStyle(String imageStyle) {
        _imageState.style = imageStyle;
    }

    /**
     * Set the label style class for each contained Image. When the tag library is
     * running in legacy mode, this will override the <code>styleClass</code> attribute if that is
     * set.  If this is not set, and <code>styleClass</code> is set, then it will be applied to
     * the image.
     *
     * @param imageClass the image class
     * @jsptagref.attributedescription The style class for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style class for each contained image."
     */
    public void setImageStyleClass(String imageClass) {
        _imageState.styleClass = imageClass;
    }

    /**
     * Sets the width attribute for the HTML image tag.
     *
     * @param width the image width.
     * @jsptagref.attributedescription The width for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>integer_pixelWidth</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The width attribute for the HTML image tag."
     */
    public void setWidth(String width) {
        _imageState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.WIDTH, width);
    }

    /**
     * Set the name of the tagId for the HTML image tag.
     *
     * @param tagId the the name of the tagId for the image.
     * @jsptagref.attributedescription The tagId for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
        throws JspException {
        applyIndexedTagId(_imageState, tagId);
    }

    /**
     * Set the name of the tagId for the HTML image tag..
     *
     * @param tagId the the name of the tagId for the image.
     * @jsptagref.attributedescription The tagId for the HTML image tag.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setAnchorTagId(String tagId)
        throws JspException {
        applyIndexedTagId(_anchorState, tagId);
    }

    /**
     * <p>
     * Implementation of the {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.  This
     * allows users of this tag to extend the attribute set that is rendered by the HTML image or
     * anchor tags.  This method accepts the following facets:
     * <table>
     * <tr><td>Facet Name</td><td>Operation</td></tr>
     * <tr><td><code>anchor</code></td><td>Adds an attribute with the provided <code>name</code> and <code>value</code> to the
     * attributes rendered on the &lt;a&gt; tag.</td></tr>
     * <tr><td><code>image</code></td><td>Adds an attribute with the provided <code>name</code> and <code>value</code> to the
     * attributes rendered on the &lt;img&gt; tag.</td></tr>
     * </table>
     * This tag defaults to the setting attributes on the anchor when the facet name is unset.
     * </p>
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @param facet the facet for the attribute
     * @throws JspException thrown when the facet is not recognized
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException {
        if(facet == null || facet.equals(ANCHOR_FACET_NAME))
            super.addStateAttribute(_anchorState, name, value);
        else if(facet.equals(IMAGE_FACET_NAME))
            super.addStateAttribute(_imageState, name, value);
        else
            super.setAttribute(name, value, facet);
    }

    /**
     * <p>
     * Implementation of the {@link IUrlParams} interface.  This allows this tag to accept &lt;netui:parameter&gt;
     * and &lt;netui:parameterMap&gt; in order to add URL parameters onto the rendered anchor.  For example:
     * <pre>
     *   <netui-data:imageAnchorCell href="foo.jsp" src="foo.png">
     *       <netui:parameter name="paramKey" value="paramValue"/>
     *   </netui-data:anchorCell>
     * </pre>
     * will render an HTML image anchor as:
     * <pre>
     *   <a href="foo.jsp?paramKey=paramValue><img src="foo.png"/></a>
     * </pre>
     * </p>
     * @param name the name of the parameter
     * @param value the value of the parameter
     * @param facet the facet for the parameter
     * @throws JspException thrown when the facet is unsupported
     */
    public void addParameter(String name, Object value, String facet)
            throws JspException {
        ParamHelper.addParam(_imageAnchorCellModel.getParams(), name, value);
    }

    /**
     * Render the contents of the HTML anchor and image.  This method calls to an
     * {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator} associated with this tag.
     * The result of renderingi is appended to the <code>appender</code>
     * @param appender the {@link AbstractRenderAppender} to which output should be rendered
     * @param jspFragmentOutput the result of having evaluated this tag's {@link javax.servlet.jsp.tagext.JspFragment}
     */
    protected void renderDataCellContents(AbstractRenderAppender appender, String jspFragmentOutput) {
        assert DECORATOR != null;
        assert appender != null;
        assert _imageAnchorCellModel != null;

        String script = null;
        /* render any JavaScript needed to support framework features */
        if (_imageState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            script = renderNameAndId(request, _imageState, null);
        }

        /* render any JavaScript needed to support framework features */
        if (_anchorState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            String anchorScript = renderNameAndId(request, _anchorState, null);
            if(anchorScript != null)
                script = (script != null ? script += anchorScript : anchorScript);                    
        }

        _imageAnchorCellModel.setJavascript(script);

        DECORATOR.decorate(getJspContext(), appender, _imageAnchorCellModel);
    }

    /**
     * Implementation of {@link AbstractHtmlTableCell#internalGetCellModel()} that exposes the
     * {@link org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.ImageAnchorCellModel}
     * which is storing state for this tag.
     * @return this tag's image anchor cell model
     */
    protected CellModel internalGetCellModel() {
        return _imageAnchorCellModel;
    }
}