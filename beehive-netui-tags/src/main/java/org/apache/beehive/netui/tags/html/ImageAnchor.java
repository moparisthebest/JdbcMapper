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

import org.apache.beehive.netui.tags.ByRef;
import org.apache.beehive.netui.tags.IHtmlAccessable;
import org.apache.beehive.netui.tags.internal.PageFlowTagUtils;
import org.apache.beehive.netui.tags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;

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
 * @netui:tag name="imageAnchor" description="Combines the functionality of the netui:image and netui:anchor tags."
 */
public class ImageAnchor
        extends Anchor
        implements IHtmlAccessable
{
    private ImageTag.State _imgState = new ImageTag.State();
    private String _rolloverImage = null; // The roll-over image of the ImageAnchor.

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
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
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

    /**
     * Insert rollover javascript.
     * <p>
     * Support for indexed property since Struts 1.1
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        if (_rolloverImage != null && getJavaScriptAttribute(ONMOUSEOVER) == null) {
            // cause the roll over script to be inserted
            WriteRenderAppender writer = new WriteRenderAppender(pageContext);
            ScriptRequestState srs = ScriptRequestState.getScriptRequestState((HttpServletRequest) pageContext.getRequest());
            srs.writeFeature(getScriptReporter(), writer, CoreScriptFeature.ROLLOVER, true, false, null);
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * Render the image and hyperlink.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        // report errors that may have occurred when the required attributes are being set
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        // build the anchor into the results
        // render the anchor tag
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase trb = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG, req);
        ByRef script = new ByRef();
        if (!createAnchorBeginTag(req, script, trb, writer, REQUIRED_ATTR)) {
            reportErrors();
            if (!script.isNull())
                write(script.getRef().toString());
            localRelease();
            return EVAL_PAGE;
        }

        // set the source and lowsrc attributes
        // the lowsrc is deprecated and should be removed.
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        if (_imgState.src != null) {
            try {
                String uri = PageFlowTagUtils.rewriteResourceURL(pageContext, _imgState.src, null, null);
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
                String uri = PageFlowTagUtils.rewriteResourceURL(pageContext, _rolloverImage, null, null);
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
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, req);
        br.doStartTag(writer, _imgState);
        br.doEndTag(writer);

        // write the end tag
        trb.doEndTag(writer);
        if (!script.isNull())
            write(script.getRef().toString());

        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _imgState.clear();
        _rolloverImage = null;
    }
}
