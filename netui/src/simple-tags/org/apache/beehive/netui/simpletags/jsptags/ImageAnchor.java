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
import org.apache.beehive.netui.simpletags.behaviors.ImageAnchorBehavior;
import org.apache.beehive.netui.simpletags.html.IHtmlAccessable;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Generates a URL-encoded hyperlink to a specified URI with an image
 * enclosed as the body.  ImageAnchor provides support for image rollovers.
 *
 * An imageAnchor must have one of five attributes to correctly create the hyperlink:
 * <ul>
 * <li>action - an action invoked by clicking the hyperlink.</li>
 * <li>href - a URL to go to</li>
 * <li>linkName - an internal place in the page to move to</li>
 * <li>clientAction - the action to run on the client</li>
 * <li>tagId - the ID of the tag</li>
 * <li>formSubmit - indicates whether or not the enclosing Form should be submitted</li>
 * </ul>
 * @jsptagref.tagdescription <p>
 * Generates a hyperlink with a clickable image.  Provides support for image rollovers.
 * <p>
 * The &lt;netui:imageAnchor> tag must have one of five attributes to correctly create the hyperlink:
 * <blockquote>
 * <ul>
 * <li><code>action</code> - an action method invoked by clicking the hyperlink</li>
 * <li><code>href</code> - an URL to go to</li>
 * <li><code>linkName</code> - an internal place in the page to move to</li>
 * <li><code>clientAction</code> - the action to run on the client</li>
 * <li><code>tagId</code> - the ID of the tag</li>
 * <li><code>formSubmit</code> - indicates whether or not the enclosing Form should be submitted</li>
 * </ul>
 * </blockquote>
 *
 * <p><b>JavaScript</b></p>
 * <p>If the <code>formSubmit</code> attribute is set to <code>true</code> and no
 * <code>onClick</code> attribute is set, the following JavaScript function will be written to the HTML page.
 * This JavaScript function will be referenced by the <code>onclick</code> attribute of the generated image
 * anchor tag.</p>
 * <pre>
 *  function anchor_submit_form(netuiName, newAction)
 *  {
 *    for (var i=0; i&lt;document.forms.length; i++) {
 *       if (document.forms[i].id == netuiName) {
 *          document.forms[i].method = "POST";
 *          document.forms[i].action = newAction;
 *          document.forms[i].submit();
 *       }
 *     }
 *  }</pre>
 * <p>It is possible to write a custom <code>onClick</code> JavaScript event handler that would
 * do additional work, for example form validation, and still submit the form correctly.  To
 * accomplish this, reference a JavaScript function in the <code>onClick</code>
 * attribute:</p>
 * <pre>    &lt;netui:imageAnchor formSubmit="true" <b>onClick="SubmitFromAnchor();return false;"</b>&gt;
 *        View Results
 *    &lt;/netui:imageAnchor&gt;</pre>
 *
 * <p>And add the referenced JavaScript function to the page:</p>
 * <pre>    function SubmitFromAnchor()
 *    {
 *        // implement custom logic here
 *
 *        for(var i=0; i&lt;document.forms.length; i++)
 *        {
 *            // submit to the action /aWebapp/formPost.do
 *            if (document.forms[i].action == "/aWebapp/formPost.do")
 *            {
 *                document.forms[i].method="POST";
 *                document.forms[i].action="/aWebapp/formPost.do";
 *                document.forms[i].submit();
 *            }
 *        }
 *    }</pre>
 *
 * <p>This will cause the JavaScript function to be executed before posting the form.</p>
 * @example In this sample, an &lt;netui:imageAnchor> shows "top.jpg" at 25 x 25 pixels and navigates to
 * index.jsp when clicked.
 * <pre>    &lt;netui:imageAnchor href="index.jsp" src="top.jpg" height="25" width="25" /&gt;</pre>
 * </p>
 * @netui:tag name="imageAnchor" body-content="scriptless" dynamic-attributes="true" description="Combines the functionality of the netui:image and netui:anchor tags."
 */
public class ImageAnchor
        extends Anchor
        implements IHtmlAccessable
{
    public ImageAnchor() {
        _behavior = new ImageAnchorBehavior();
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
        ((ImageAnchorBehavior) _behavior).setAlign(align);
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
        ((ImageAnchorBehavior) _behavior).setAlt(alt);
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
        ((ImageAnchorBehavior) _behavior).setLongdesc(longdesc);
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
        ((ImageAnchorBehavior) _behavior).setBorder(border);
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
        ((ImageAnchorBehavior) _behavior).setHeight(height);
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
        ((ImageAnchorBehavior) _behavior).setHspace(hspace);
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
        ((ImageAnchorBehavior) _behavior).setIsmap(ismap);
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
        ((ImageAnchorBehavior) _behavior).setRolloverImage(rolloverImage);
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
        ((ImageAnchorBehavior) _behavior).setSrc(src);
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
        ((ImageAnchorBehavior) _behavior).setUsemap(usemap);
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
        ((ImageAnchorBehavior) _behavior).setVspace(vspace);
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
        ((ImageAnchorBehavior) _behavior).setImageStyle(imageStyle);
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
        ((ImageAnchorBehavior) _behavior).setImageStyleClass(imageClass);
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
        ((ImageAnchorBehavior) _behavior).setWidth(width);
    }

    /**
     * Insert rollover javascript.
     * <p>
     * Support for indexed property since Struts 1.1
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
