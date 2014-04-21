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
package org.apache.beehive.netui.tags.html;

import org.apache.beehive.netui.tags.IHtmlAccessable;
import org.apache.beehive.netui.tags.internal.PageFlowTagUtils;
import org.apache.beehive.netui.tags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.InputImageTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;
import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;

/**
 * Generates an <code>&lt;input type="image"></code> tag with the specified attributes. ImageButton
 * ignores its body content.
 * @jsptagref.tagdescription Renders an <code>&lt;input type="image"></code> tag with the specified attributes.
 *
 * <p><b>JavaScript</b></p>
 * <p>If the &lt;netui:imageButton> specifies
 * a <code>rolloverImage</code> attribute, the following JavaScript will be written to the page:</p>
 * <pre>    function swapImage(control, image)
 *    {
 *        control.src = image;
 *    }</pre>
 * @example In this sample, an &lt;netui:imageButton> tag displays the image "house.jpg".
 * On mouseover, the image "house_highlight.jpg" is displayed.
 * When clicked, the &lt;netui:imageButton> will invoke the action specified by its parent
 * &lt;netui:form> tag.
 * <pre>    &lt;netui:form action="formSubmit">
 *         &lt;netui:imageButton rolloverImage="house_highlight.jpg" src="house.jpg" />
 *     &lt;/netui:form></pre>
 * @netui:tag name="imageButton" description="Combines the functionality of the netui:image and netui:button tags."
 */
public class ImageButton
        extends HtmlFocusBaseTag
        implements IHtmlAccessable
{
    private InputImageTag.State _state = new InputImageTag.State();

    private String _page;            // The module-relative URI of the image.
    private String _rolloverImage;   // The roll-over image of the ImageButton.

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "ImageButton";
    }

    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    protected AbstractHtmlState getState()
    {
        return _state;
    }

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>src</code>
     * and <code>value</code> attributes
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        if (name != null) {
            if (name.equals(SRC) || name.equals(VALUE)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
            else {
                if (name.equals(DISABLED)) {
                    setDisabled(Boolean.parseBoolean(value));
                    return;
                }
            }
        }
        super.setAttribute(name, value, facet);
    }

    /**
     * Sets the property to specify where to align the image.
     * @param align the image alignment.
     * @jsptagref.attributedescription The alignment of the image.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_align</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the property to specify where to align the image."
     */
    public void setAlign(String align)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALIGN, align);
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
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_accessKey</i>
     * @netui:attribute required="false" rtexprvalue="true" type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCESSKEY, Character.toString(accessKey));
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }

    /**
     * Sets the usemap of for the map.
     * @param usemap the tab index.
     * @jsptagref.attributedescription Sets the usemap of for the map.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_usemap</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the usemap of for the map."
     */
    public void setUsemap(String usemap)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, USEMAP, usemap);
    }

    /**
     * Sets the ismap of for the map.
     * @param ismap the tab index.
     * @jsptagref.attributedescription Sets the ismap of for the map.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_ismap</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the ismap of for the map."
     */
    public void setIsmap(String ismap)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ISMAP, ismap);
    }

    /**
     * Sets the property to specify the alt text of the image.
     * @param alt the image alt text.
     * @jsptagref.attributedescription The alternative text of the image
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The alternative text of the image"
     */
    public void setAlt(String alt)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt);
    }

    /**
     * Sets the roll-over image of the ImageAnchor.
     * @param rolloverImage the rollover image.
     * @jsptagref.attributedescription The URI of the rollover image.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_rolloverImage</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The URI of the rollover image."
     */
    public void setRolloverImage(String rolloverImage)
    {
        _rolloverImage = rolloverImage;
    }

    /**
     * Sets the image source URI.
     * @param src the source URI.
     * @jsptagref.attributedescription The image source URI
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_src</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The image source URI"
     * reftype="img-url"
     */
    public void setSrc(String src)
            throws JspException
    {
        _state.src = src;
    }

    /**
     * Set the value of the ImageButton.
     * @param value the value of the ImageButton.
     * @jsptagref.attributedescription The value of the image button.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_value</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The value of the image button."
     */
    public void setValue(String value)
    {
        _state.value = value;
    }

    /**
     * Process the start of this tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {

        if (_rolloverImage != null && getJavaScriptAttribute(ONMOUSEOVER) == null) {
            // cause the roll over script to be inserted
            ScriptRequestState srs = ScriptRequestState.getScriptRequestState((HttpServletRequest) pageContext.getRequest());
            WriteRenderAppender writer = new WriteRenderAppender(pageContext);
            srs.writeFeature(getScriptReporter(), writer, CoreScriptFeature.ROLLOVER, true, false, null);
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * Save the body content of the ImageButton.
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException
    {

        if (bodyContent != null) {
            bodyContent.clearBody();
        }
        return SKIP_BODY;
    }

    /**
     * Process the end of this tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        ServletRequest req = pageContext.getRequest();
        String idScript = null;
        String tmp = null;

        // we assume that tagId will over have override id if both
        // are defined.
        if (getTagId() != null) {
            idScript = renderNameAndId((HttpServletRequest) req, _state, null);
        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        tmp = src();
        if (tmp != null) {
            try {
                String uri = PageFlowTagUtils.rewriteResourceURL(pageContext, tmp, null, null);
                _state.src = response.encodeURL(uri);
            }
            catch (URISyntaxException e) {
                // report the error...
                String s = Bundle.getString("Tags_Image_URLException",
                        new Object[]{_state.src, e.getMessage()});
                registerTagError(s, e);
            }
        }

        _state.disabled = isDisabled();

        if (_rolloverImage != null) {

            if (hasErrors()) {
                reportErrors();
                localRelease();
                return EVAL_PAGE;
            }

            try {
                String uri = PageFlowTagUtils.rewriteResourceURL(pageContext, _rolloverImage, null, null);
                _rolloverImage = response.encodeURL(uri);
            }
            catch (URISyntaxException e) {
                // report the error...
                String s = Bundle.getString("Tags_Rollover_Image_URLException",
                        new Object[]{_rolloverImage, e.getMessage()});
                registerTagError(s, e);
            }
        }

        if ((getJavaScriptAttribute(ONMOUSEOUT) == null) && (_rolloverImage != null)) {
            setOnMouseOut("swapImage(this,'" + _state.src + "')");
        }
        if ((getJavaScriptAttribute(ONMOUSEOVER) == null) && (_rolloverImage != null)) {
            setOnMouseOver("swapImage(this,'" + _rolloverImage + "')");
        }

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_IMAGE_TAG, req);
        br.doStartTag(writer, _state);
        br.doEndTag(writer);

        if (idScript != null)
            write(idScript);

        // Evaluate the remainder of this page
        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _state.clear();

        _page = null;
        _rolloverImage = null;
    }

    /**
     * Return the base source URL that will be rendered in the <code>src</code>
     * property for this generated element, or <code>null</code> if there is
     * no such URL.
     */
    private String src()
    {

        // Deal with a direct context-relative page that has been specified
        if (_page != null) {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            ModuleConfig config = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
            if (config == null)
                return (request.getContextPath() + _page);
            return (request.getContextPath() + config.getPrefix() + _page);
        }

        // Deal with an absolute source that has been specified
        if (_state.src != null)
            return _state.src;
        return null;
    }
}
