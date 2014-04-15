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
import org.apache.beehive.netui.simpletags.core.IUrlParams;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.ImageTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.simpletags.util.PageFlowTagUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.ParamHelper;

import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ImageBehavior extends HtmlBaseBehavior
        implements IUrlParams
{
    private ImageTag.State _state = new ImageTag.State();

    private String _location = null;         // The location hash to append to the url.
    private Map _params;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Image";
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
     * attribute.
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALIGN, align);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt, true);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, LONGDESC, longdesc);
    }

    /**
     * Sets the border size around the image.
     * @param border the border size.
     * @jsptagref.attributedescription The border size around the image
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>integer_pixelBorder</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The border size around the image."
     */
    public void setBorder(String border)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, border);
    }

    /**
     * Sets the image height.
     * @param height the height.
     * @jsptagref.attributedescription The image height
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_height</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The image height."
     */
    public void setHeight(String height)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HEIGHT, height);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HSPACE, hspace);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ISMAP, ismap);
    }

    /**
     * Sets the location hash to append to the url.
     * @param location the location hash.
     * @jsptagref.attributedescription The location hash to append to the URL.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_location</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The location hash to append to the URL."
     */
    public void setLocation(String location)
    {
        _location = location;
    }

    /**
     * Sets the image source URI.
     * @param src the source URI.
     * @jsptagref.attributedescription The image source URI
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_src</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The image source URI"
     */
    public void setSrc(String src)
            throws JspException
    {
        _state.src = src;
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, USEMAP, usemap);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, VSPACE, vspace);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, WIDTH, width);
    }

    /**
     * Adds a URL parameter to the generated hyperlink.
     * @param name  the name of the parameter to be added.
     * @param value the value of the parameter to be added (a String or String[]).
     * @param facet
     */
    public void addParameter(String name, Object value, String facet)
    {
        assert(name != null) : "Parameter 'name' must not be null";

        if (_params == null) {
            _params = new HashMap();
        }
        ParamHelper.addParam(_params, name, value);
    }

    /**
     * This method will render the start tag for the markup generated by the behavior.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderStart(Appender appender)
    {
        // report errors that may have occurred
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        // Generate the name definition or image element
        String uri = null;
        if (_state.src != null) {
            try {
                uri = PageFlowTagUtils.rewriteResourceURL(_state.src, _params, _location);
            }
            catch (URISyntaxException e) {
                // report the error...
                String s = Bundle.getString("Tags_Image_URLException", new Object[]{_state.src, e.getMessage()});
                registerTagError(s, e);
            }
        }

        if (uri != null) {
            PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
            _state.src = pfCtxt.getResponse().encodeURL(uri);
        }

        // we assume that tagId will over have override id if both
        // are defined.
        if (_state.id != null) {
            renderNameAndId( _state, null);
        }

        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG);
        br.doStartTag(appender, _state);

    }

    /**
     * This method will render teh end tag for the markup generted by the behavior.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderEnd(Appender appender)
    {
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG);
        br.doEndTag(appender);
    }
}
