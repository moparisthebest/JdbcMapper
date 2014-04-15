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
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.appender.ResponseAppender;
import org.apache.beehive.netui.simpletags.behaviors.ImageBehavior;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Generates an image with the specified attributes.  Image ignores its
 * body content.
 * @jsptagref.tagdescription Renders an HTML &lt;img> tag with specified attributes.
 * @example In this sample, an Image shows "friends.jpg" at 150 x 175 pixels, with the id "Friends".
 * <pre>&lt;netui:image src="friends.jpg" tagId="Friends" height="150" width="175" /></pre>
 * @netui:tag name="image" body-content="scriptless" dynamic-attributes="true" description="Places an image file type on your page."
 */
public class Image extends HtmlBaseTag
{
    public Image() {
        _behavior = new ImageBehavior();
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
        ((ImageBehavior) _behavior).setAlign(align);
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
        ((ImageBehavior) _behavior).setAlt(alt);
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
        ((ImageBehavior) _behavior).setLongdesc(longdesc);
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
        ((ImageBehavior) _behavior).setBorder(border);
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
        ((ImageBehavior) _behavior).setHeight(height);
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
        ((ImageBehavior) _behavior).setHspace(hspace);
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
        ((ImageBehavior) _behavior).setIsmap(ismap);
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
        ((ImageBehavior) _behavior).setLocation(location);
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
        ((ImageBehavior) _behavior).setSrc(src);
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
        ((ImageBehavior) _behavior).setUsemap(usemap);
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
        ((ImageBehavior) _behavior).setVspace(vspace);
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
        ((ImageBehavior) _behavior).setWidth(width);
    }

    /**
     * Render the beginning of the IMG tag.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();

        // evaluate the body, this is called basically so any parameters my be applied.
        getBufferBody(false);

        Appender appender = new ResponseAppender(getPageContext().getResponse());
        _behavior.preRender();
        _behavior.renderStart(appender);
        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
