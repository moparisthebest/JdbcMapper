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
package org.apache.beehive.netui.simpletags.behaviors;

import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.ImageTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.simpletags.util.PageFlowTagUtils;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;

public class ImageAnchorBehavior extends AnchorBehavior
{
    private ImageTag.State _imgState = new ImageTag.State();
    private String _rolloverImage = null; // The roll-over image of the ImageAnchor.
    private TagRenderingBase _imgRb;
    private TagRenderingBase _anchorRb;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "ImageAnchor";
    }

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>src</code>
     * attribute. ImageAnchor supports two facets, <code>image</code> and <code>anchor<code>.  The default
     * facet is anchor, meaning if the facet is not specified, the attribute will be applied to the
     * &lt;a> element.  To apply an attribute to the &lt;img> element you must specify the
     * <code>image</code> facet.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     */
    public void setAttribute(String name, String value, String facet)
    {
        if (name != null && name.equals(SRC)) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
        }
        if (facet != null) {
            if (facet.equals("image")) {
                setStateAttribute(name, value, _imgState);
                return;
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
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The alignment of the image."
     */
    public void setAlign(String align)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALIGN, align);
    }

    /**
     * Sets the property to specify the alt text of the image.
     * @param alt the image alignment.
     * @jsptagref.attributedescription The alternative text of the image
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The alternative text of the image."
     */
    public void setAlt(String alt)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt);
    }

    /**
     * Sets the property to specify a link to the the long description to supplement
     * the short description in the <code>alt</code> attribute.
     * @param longdesc the longdesc.
     * @jsptagref.attributedescription Specifies a link to the the long description.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_longdesc</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Specifies a link to the the long description."
     */
    public void setLongdesc(String longdesc)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, LONGDESC, longdesc);
    }

    /**
     * Sets the border size around the image.
     * @param border the border size.
     * @jsptagref.attributedescription The border size around the image
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>integer_pixelBorder</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The border size around the image."
     */
    public void setBorder(String border)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, border);
    }

    /**
     * Sets the image height.
     * @param height the height.
     * @jsptagref.attributedescription The image height
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_height</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The image height."
     */
    public void setHeight(String height)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HEIGHT, height);
    }

    /**
     * Sets the the horizontal spacing around the image.
     * @param hspace the horizontal spacing.
     * @jsptagref.attributedescription The horizontal spacing around the image.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_hspace</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The horizontal spacing around the image."
     */
    public void setHspace(String hspace)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HSPACE, hspace);
    }

    /**
     * Sets the server-side image map declaration.
     * @param ismap the image map declaration.
     * @jsptagref.attributedescription The server-side map declaration.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_isMap</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The server-side map declaration."
     */
    public void setIsmap(String ismap)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ISMAP, ismap);
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
     * @param src the image source URI.
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
        _imgState.src = src;
    }

    /**
     * Sets the client-side image map declaration.
     * @param usemap the map declaration.
     * @jsptagref.attributedescription The client-side image map declaration
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_useMap</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The client-side image map declaration"
     */
    public void setUsemap(String usemap)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, USEMAP, usemap);
    }

    /**
     * Sets the vertical spacing around the image.
     * @param vspace the vertical spacing.
     * @jsptagref.attributedescription The vertical spacing around the image.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_vspace</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The vertical spacing around the image."
     */
    public void setVspace(String vspace)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, VSPACE, vspace);
    }

    /**
     * Set the &lt;img> style for the contained image. When the tag library is
     * running in legacy mode, this will override the <code>style</code> attribute if that is
     * set.  If this is not set, and <code>style</code> is set, then it will be applied to
     * the image.
     * @param imageStyle the label style
     * @jsptagref.attributedescription For legacy documents. Specifies style information for the
     * contained image. When the tag library is running in legacy mode, this will override the
     * <code>style</code> attribute.  If this is not set, and <code>style</code> is set,
     * then it will be applied to the image.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imagestyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style for the contained image."
     */
    public void setImageStyle(String imageStyle)
    {
        _imgState.style = imageStyle;
    }

    /**
     * Set the label style class for each contained Image. When the tag library is
     * running in legacy mode, this will override the <code>styleClass</code> attribute if that is
     * set.  If this is not set, and <code>styleClass</code> is set, then it will be applied to
     * the image.
     * @param imageClass the image class
     * @jsptagref.attributedescription For legacy documents. The style class (a style sheet selector).
     * When the tag library is running in legacy mode, this will override the <code>styleClass</code>
     * attribute. If this is not set, and <code>styleClass</code> is set, then it will be applied to
     * the image.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageclass</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the label style class for each contained image."
     */
    public void setImageStyleClass(String imageClass)
    {
        _imgState.styleClass = imageClass;
    }

    /**
     * Sets the image width.
     * @param width the image width.
     * @jsptagref.attributedescription The image width.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_pixelWidth</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The image width."
     */
    public void setWidth(String width)
    {
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, WIDTH, width);
    }

    //******************* Lifecycle Methods ************************************
    /**
     * This method will push the Behavior on the behavior stack.  All overrides of
     * this method should call this method so that the stack is maintained correctly.
     */
    public void preRender()
    {
    }

    /**
     * This method will render the start tag for the markup generated by the behavior.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderStart(Appender appender)
    {
        // report errors that may have occurred when the required attributes are being set
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        TagContext tagCtxt = ContextUtils.getTagContext();
        HttpServletResponse response = pfCtxt.getResponse();

        if (_rolloverImage != null && getJavaScriptAttribute(ONMOUSEOVER) == null) {
            // cause the roll over script to be inserted
            ScriptReporter sr = tagCtxt.getScriptReporter();
            sr.writeFeature(CoreScriptFeature.ROLLOVER, true, false, null);
        }

        // build the anchor into the results
        // render the anchor tag
        _anchorRb = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG);
        if (!createAnchorBeginTag(_anchorRb, appender, REQUIRED_ATTR)) {
            reportErrors(appender);
            return;
        }

        // set the source and lowsrc attributes
        // the lowsrc is deprecated and should be removed.
        if (_imgState.src != null) {
            try {
                String uri = PageFlowTagUtils.rewriteResourceURL(_imgState.src, null, null);
                _imgState.src = response.encodeURL(uri);
            }
            catch (URISyntaxException e) {
                // report the error...
                String s = Bundle.getString("Tags_Image_URLException",
                        new Object[]{_imgState.src, e.getMessage()});
                registerTagError(s, e);
            }
        }

        // set the rollover image
        if (_rolloverImage != null) {
            try {
                String uri = PageFlowTagUtils.rewriteResourceURL(_rolloverImage, null, null);
                _rolloverImage = response.encodeURL(uri);
            }
            catch (URISyntaxException e) {
                // report the error...
                String s = Bundle.getString("Tags_Rollover_Image_URLException",
                        new Object[]{_rolloverImage, e.getMessage()});
                registerTagError(s, e);
            }

            if (getJavaScriptAttribute(ONMOUSEOUT) == null) {
                String s = "swapImage(this,'" + response.encodeURL(_imgState.src) + "')";
                _imgState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEOUT, s);
            }
            if (getJavaScriptAttribute(ONMOUSEOVER) == null) {
                String s = "swapImage(this,'" + response.encodeURL(_rolloverImage) + "')";
                _imgState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEOVER, s);
            }
        }

        // render the image tag.
        _imgRb = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG);
        _imgRb.doStartTag(appender, _imgState);
    }

    /**
     * This method will render teh end tag for the markup generted by the behavior.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderEnd(Appender appender)
    {
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }
        
        _imgRb.doEndTag(appender);
        _anchorRb.doEndTag(appender);
    }
}
