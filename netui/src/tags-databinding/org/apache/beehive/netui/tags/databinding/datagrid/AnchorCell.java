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
import org.apache.beehive.netui.databinding.datagrid.runtime.model.cell.AnchorCellModel;
import org.apache.beehive.netui.databinding.datagrid.runtime.rendering.cell.AnchorCellDecorator;
import org.apache.beehive.netui.databinding.datagrid.runtime.util.JspUtil;
import org.apache.beehive.netui.tags.html.IUrlParams;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.html.IFormattable;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.IHtmlI18n;
import org.apache.beehive.netui.tags.IHtmlEvents;
import org.apache.beehive.netui.tags.IHtmlCore;
import org.apache.beehive.netui.util.ParamHelper;
import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * Data grid cell used to render an HTML anchor.  This tag should be used inside of a &lt;netui-data:rows&gt; tag
 * when rendering a data set with the &lt;netui-data:dataGrid&gt; tag.  An AnchorCell is used to render an HTML
 * &lt;a&gt; tag.  The rendered href is provided with either the action or href attributes.  If an action is provided,
 * the action must exist inside of the 'current' Page Flow with which the JSP is associated.  If the href is provided,
 * the anchor will use this value as its rendered href.  For example:
 * <pre>
 *   &lt;netui-data:anchorCell href="foo.jsp" value="Go To Foo"/>
 * </pre>
 * will render:
 * <pre>
 *   &lt;a href="foo.jsp">Go To Foo&lt;/a>
 * </pre>
 * </p>
 * <p>
 * The AnchorCell tag can also accept NetUI parameter tags that implement the {@link IUrlParams} interface.  When
 * these tags are contained inside of this tag, they add URL parameters onto the rendered href.  For example:
 * <pre>
 *   <netui-data:anchorCell href="foo.jsp" value="Go To Foo">
 *     <netui:parameter name="paramKey" value="paramValue"/>
 *   </netui-data:anchorCell>
 * </pre>
 * will render:
 * <pre>
 *   &lt;a href="foo.jsp?paramKey=paramValue">Go To Foo&lt;/a>
 * </pre>
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
 * Data grid cell used to render an HTML anchor.  This tag should be used inside of a &lt;netui-data:rows&gt; tag
 * when rendering a data set with the &lt;netui-data:dataGrid&gt; tag.  An AnchorCell is used to render an HTML
 * &lt;a&gt; tag.  The rendered href is provided with either the action or href attributes.  If an action is provided,
 * the action must exist inside of the 'current' Page Flow with which the JSP is associated.  If the href is provided,
 * the anchor will use this value as its rendered href.  For example:
 * <pre>
 *   &lt;netui-data:anchorCell href="foo.jsp" value="Go To Foo"/>
 * </pre>
 * will render:
 * <pre>
 *   &lt;a href="foo.jsp">Go To Foo&lt;/a>
 * </pre>
 * </p>
 * <p>
 * The AnchorCell tag can also accept NetUI parameter tags that implement the {@link IUrlParams} interface.  When
 * these tags are contained inside of this tag, they add URL parameters onto the rendered href.  For example:
 * <pre>
 *   &lt;netui-data:anchorCell href="foo.jsp" value="Go To Foo">
 *     &lt;netui:parameter name="paramKey" value="paramValue"/>
 *   &lt;/netui-data:anchorCell>
 * </pre>
 * will render:
 * <pre>
 *   &lt;a href="foo.jsp?paramKey=paramValue">Go To Foo&lt;/a>
 * </pre>
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
 * @netui:tag name="anchorCell" body-content="scriptless"
 *            description="Renders an HTML table cell inside of a data grid that contains an anchor"
 */
public class AnchorCell
    extends AbstractHtmlTableCell
    implements IFormattable, IUrlParams, IHtmlCore, IHtmlEvents, IHtmlI18n {

    private static final AnchorCellDecorator DECORATOR = new AnchorCellDecorator();
    private static final String REQUIRED_ATTR = "href, action, linkName";
    private static final String ANCHOR_FACET_NAME = "anchor";

    private AnchorCellModel _anchorCellModel = new AnchorCellModel();
    private AnchorTag.State _anchorState = _anchorCellModel.getAnchorState();

    /**
     * The name of this tag; this value is used for error reporting.
     * @return the String name of this tag
     */
    public String getTagName() {
        return "AnchorCell";
    }

    /**
     * Sets the onClick JavaScript event for the HTML anchor.
     *
     * @param onClick the onClick event for the HTML anchor.
     * @jsptagref.attributedescription The onClick JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onClick JavaScript event."
     */
    public void setOnClick(String onClick) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONCLICK, onClick);
    }

    /**
     * Sets the onDblClick JavaScript event for the HTML anchor.
     *
     * @param onDblClick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onDblClick JavaScript event."
     */
    public void setOnDblClick(String onDblClick) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONDBLCLICK, onDblClick);
    }

    /**
     * Sets the onKeyDown JavaScript event for the HTML anchor.
     *
     * @param onKeyDown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyDown JavaScript event."
     */
    public void setOnKeyDown(String onKeyDown) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYDOWN, onKeyDown);
    }

    /**
     * Sets the onKeyUp JavaScript event for the HTML anchor.
     *
     * @param onKeyUp the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyUp JavaScript event."
     */
    public void setOnKeyUp(String onKeyUp) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYUP, onKeyUp);
    }

    /**
     * Sets the onKeyPress JavaScript event for the HTML anchor.
     *
     * @param onKeyPress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onKeyPress JavaScript event."
     */
    public void setOnKeyPress(String onKeyPress) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONKEYPRESS, onKeyPress);
    }

    /**
     * Sets the onMouseDown JavaScript event for the HTML anchor.
     *
     * @param onMouseDown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseDown JavaScript event."
     */
    public void setOnMouseDown(String onMouseDown) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEDOWN, onMouseDown);
    }

    /**
     * Sets the onMouseUp JavaScript event for the HTML anchor.
     *
     * @param onMouseUp the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseUp JavaScript event."
     */
    public void setOnMouseUp(String onMouseUp) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEUP, onMouseUp);
    }

    /**
     * Sets the onMouseMove JavaScript event for the HTML anchor.
     *
     * @param onMouseMove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseMove JavaScript event."
     */
    public void setOnMouseMove(String onMouseMove) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEMOVE, onMouseMove);
    }

    /**
     * Sets the onMouseOut JavaScript event for the HTML anchor.
     *
     * @param onMouseOut the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOut JavaScript event."
     */
    public void setOnMouseOut(String onMouseOut) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOUT, onMouseOut);
    }

    /**
     * Sets the onMouseOver JavaScript event for the HTML anchor.
     *
     * @param onMouseOver the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The onMouseOver JavaScript event."
     */
    public void setOnMouseOver(String onMouseOver) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONMOUSEOVER, onMouseOver);
    }

    /**
     * Sets the style of the HTML anchor.
     *
     * @param style the style attribute
     * @jsptagref.attributedescription The style for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style for the HTML anchor"
     */
    public void setStyle(String style) {
        if("".equals(style)) return;

        _anchorState.style = style;
    }

    /**
     * Sets the style class of the HTML anchor.
     *
     * @param styleClass the style attribute.
     * @jsptagref.attributedescription The style class for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_style_class</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The style class for the anchor"
     */
    public void setStyleClass(String styleClass) {
        if("".equals(styleClass)) return;

        _anchorState.styleClass = styleClass;
    }

    /**
     * Sets the value of the title attribute for the HTML anchor
     *
     * @param title the title attribute
     * @jsptagref.attributedescription The title attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The title attribute for the anchor"
     */
    public void setTitle(String title) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TITLE, title);
    }

    /**
     * Sets <code>charset</code> attribute for the HTML anchor.
     *
     * @param charSet the charSet attribute for the HTML anchor.
     * @jsptagref.attributedescription The charSet attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_charset</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The character set."
     */
    public void setCharset(String charSet) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.CHARSET, charSet);
    }

    /**
     * Sets <code>type</code> attribute for the HTML anchor.
     *
     * @param type the type attribute
     * @jsptagref.attributedescription The type attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The attribute for the HTML anchor"
     */
    public void setType(String type) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TYPE, type);
    }

    /**
     * Sets <code>hreflang</code> attribute for the anchor.
     *
     * @param hreflang the hreflang attribute
     * @jsptagref.attributedescription The hrefLang attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_hreflang</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The HREF lang attribute for the HTML anchor."
     */
    public void setHrefLang(String hreflang) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.HREFLANG, hreflang);
    }

    /**
     * Sets <code>rel</code> attribute for the HTML anchor.
     *
     * @param rel the rel attribute
     * @jsptagref.attributedescription The rel attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_rel</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The rel for the HTML anchor."
     */
    public void setRel(String rel) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.REL, rel);
    }

    /**
     * Sets <code>rev</code> attribute for the HTML anchor.
     *
     * @param rev the rev target for the HTML anchor
     * @jsptagref.attributedescription The rev for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_rev</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The rev for the HTML anchor."
     */
    public void setRev(String rev) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.REV, rev);
    }

    /**
     * Sets the window target for the HTML anchor.
     *
     * @param target the window target for the HTML anchor.
     * @jsptagref.attributedescription The window target for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_target</i>
     * @netui:attribute required="false"  rtexprvalue="true" description="The window target."
     */
    public void setTarget(String target) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TARGET, target);
    }

    /**
     * Sets the href of the HTML anchor. This attribute will accept the empty String as a legal value.
     *
     * @param href the hyperlink URI for the HTML anchor.
     * @jsptagref.attributedescription The URL to go to; if the URL needs
     * parameters, use the parameter tag to get proper encoding of special
     * characters in the parameters.
     * @jsptagref.attributesyntaxvalue <i>string_href</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The href of the HTML anchor."
     */
    public void setHref(String href) {
        _anchorCellModel.setHref(href);
    }

    /**
     * Set the target "scope" for the anchor's action.  Multiple active page flows may exist concurrently within named
     * scopes.  This attribute selects which named scope to use.  If omitted, the default scope is assumed.
     *
     * @param targetScope the name of the target scope in which the associated action's page flow resides.
     * @jsptagref.attributedescription The target scope in which the associated action's page flow resides.
     * @jsptagref.attributesyntaxvalue <i>string_targetScope</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The target scope in which the associated action's page flow resides"
     */
    public void setTargetScope(String targetScope) {
        _anchorCellModel.setScopeId(targetScope);
    }

    /**
     * The text displayed for the HTML anchor.
     * @param value the text displayed for the HTML anchor
     * @jsptagref.attributedescription the text displayed for the HTML anchor
     * @jsptagref.attributesyntaxvalue <i>string_value</i>
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setValue(Object value) {
        _anchorCellModel.setValue(value);
    }

    /**
     * Set the name of the action for the HTML anchor.  The action method must be in the Page Flow with which
     * the containing JSP is associated.
     *
     * @param action the name of the action to set for the Area.
     * @jsptagref.attributedescription The action method to invoke.  The action method must be in Page Flow with which
     * the containing JSP is associated.
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The action method to invoke.  The action method must be in the Page Flow with wihch the containing JSP
     * is associated.
     */
    public void setAction(String action)
        throws JspException {
        _anchorCellModel.setAction(setRequiredValueAttribute(action, "action"));
    }

    /**
     * Sets the lang attribute for the HTML anchor.
     * @param lang
     * @jsptagref.attributedescription The lang attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The lang for the HTML anchor."
     */
    public void setLang(String lang)
    {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.LANG, lang);
    }

    /**
     * Sets the dir attribute for the HTML anchor.
     * @param dir
     * @jsptagref.attributedescription The dir attribute for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The dir attribute for the HTML anchor."
     */
    public void setDir(String dir)
    {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.DIR, dir);
    }

    /**
     * Sets the accessKey attribute value.  This should key value of the
     * keyboard navigation key.  It is recommended not to use the following
     * values because there are often used by browsers <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>.
     * @param accessKey the accessKey value.
     * @jsptagref.attributedescription The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>
     * @jsptagref.attributesyntaxvalue <i>string_accessKey</i>
     * @netui:attribute required="false" rtexprvalue="true"  type="char"
     * description=" The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow."
     */
    public void setAccessKey(char accessKey) {
        if (accessKey == 0x00)
            return;
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.ACCESSKEY, Character.toString(accessKey));
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key.
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex) {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TABINDEX, Integer.toString(tabindex));
    }

    /**
     * Sets the onBlur JavaScript event for the HTML anchor.
     * @param onblur the onBlur event.
     * @jsptagref.attributedescription The onBlur JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onBlur</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onBlur JavaScript event."
     */
    public void setOnBlur(String onblur)
    {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONBLUR, onblur);
    }

    /**
     * Sets the onFocus JavaScript event for the HTML anchor.
     * @param onfocus the onFocus event.
     * @jsptagref.attributedescription The onFocus JavaScript event for the HTML anchor.
     * @jsptagref.attributesyntaxvalue <i>string_onFocus</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onFocus JavaScript event."
     */
    public void setOnFocus(String onfocus)
    {
        _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, HtmlConstants.ONFOCUS, onfocus);
    }

    /**
     * <p>
     * Set the name of the tagId for the HTML anchor.  The provided tagId will have the index of the current
     * row added as a suffix in order to keep the anchor tagIds unique in the page.
     * </p>
     *
     * @param tagId the the tagId for the HTML anchor.
     * @jsptagref.attributedescription The tagId.
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
        throws JspException {
        applyIndexedTagId(_anchorState, tagId);
    }

    /**
     * <p>
     * Implementation of the {@link org.apache.beehive.netui.tags.IAttributeConsumer} interface.  This
     * allows users of the anchorCell tag to extend the attribute set that is rendered by the HTML
     * anchor.  This method accepts the following facets:
     * <table>
     * <tr><td>Facet Name</td><td>Operation</td></tr>
     * <tr><td><code>anchor</code></td><td>Adds an attribute with the provided <code>name</code> and <code>value</code> to the
     * attributes rendered on the &lt;a&gt; tag.</td></tr>
     * </table>
     * The AnchorCell tag defaults to the setting attributes on the anchor when the facet name is unset.
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
        else
            super.setAttribute(name, value, facet);
    }

    /**
     * <p>
     * Implementation of the {@link IUrlParams} interface.  This allows this tag to accept &lt;netui:parameter&gt;
     * and &lt;netui:parameterMap&gt; in order to add URL parameters onto the rendered anchor.  For example:
     * <pre>
     *   <netui-data:anchorCell href="foo.jsp" value="Go To Foo">
     *       <netui:parameter name="paramKey" value="paramValue"/>
     *   </netui-data:anchorCell>
     * </pre>
     * will render an HTML anchor as:
     * <pre>
     *   <a href="foo.jsp?paramKey=paramValue>Go To Foo</a>
     * </pre>
     * </p>
     * @param name the name of the parameter
     * @param value the value of the parameter
     * @param facet the facet for the parameter
     * @throws JspException thrown when the facet is unsupported
     */
    public void addParameter(String name, Object value, String facet)
            throws JspException {
        ParamHelper.addParam(_anchorCellModel.getParams(), name, value);
    }

    /**
     * <p>
     * Interanl method overriding the {@link AbstractHtmlTableCell#applyAttributes()} method
     * to handle setting attributes on the {@link AnchorCellModel} associated with an instance of this tag.
     * </p>
     * @throws JspException when an error occurs applying attributes to the cell model.
     */
    protected void applyAttributes()
            throws JspException {
        super.applyAttributes();

        int have = 0;
        if(_anchorCellModel.getAction() != null)
            have++;
        if(_anchorCellModel.getHref() != null)
            have++;
        if(_anchorCellModel.getLinkName() != null)
            have++;

        /* todo: allow creation of JavaScript anchors */
        if(have == 0 || have > 1) {
            String s = Bundle.getString("Tags_Anchor_InvalidAnchorURI", new Object[]{getTagName(),REQUIRED_ATTR});
            throw new JspException(s);
        }
    }

    /**
     * Render the contents of the HTML anchor.  This method calls to an
     * {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.CellDecorator} associated with this tag.
     * The result of renderingi is appended to the <code>appender</code>
     * @param appender the {@link AbstractRenderAppender} to which output should be rendered
     * @param jspFragmentOutput the result of having evaluated this tag's {@link javax.servlet.jsp.tagext.JspFragment}
     */
    protected void renderDataCellContents(AbstractRenderAppender appender, String jspFragmentOutput) {

        /* render any JavaScript needed to support framework features */
        if (_anchorState.id != null) {
            HttpServletRequest request = JspUtil.getRequest(getJspContext());
            String script = renderNameAndId(request, _anchorState, null);
            _anchorCellModel.setJavascript(script);
        }

        DECORATOR.decorate(getJspContext(), appender, _anchorCellModel);
    }

    /**
     * Implementation of {@link AbstractHtmlTableCell#internalGetCellModel()} that exposes the {@link AnchorCellModel}
     * which is storing state for the tag's anchor.
     * @return this tag's anchor cell model
     */
    protected CellModel internalGetCellModel() {
        return _anchorCellModel;
    }
}
