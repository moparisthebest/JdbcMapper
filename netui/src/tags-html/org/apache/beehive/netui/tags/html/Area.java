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
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * @jsptagref.tagdescription
 * Generates a URL-encoded area to a specified URI.
 * @example In this sample, an area tag is written with the shape, coords, href, and alt attributes,
 * for an image map associated with the "someDefaultPic.jpg" image.
 * <p>The following &lt;netui> tags...</p> 
 * <pre>    &lt;netui:image src="someDefaultPic.jpg" alt="a default picture" usemap="#defaultMap"/>
 *    &lt;map id="defaultMap" name="defaultMap">
 *        &lt;netui:area shape="rect" coords="0,0,80,80" href="bigPicture.jsp" alt="big picture of the image"/>
 *    &lt;/map></pre>
 *
 * <p>...output the following HTML:</p>
 * <pre>    &lt;img src="someDefaultPic.jpg" usemap="#defaultMap" alt="a default picture">
 *    &lt;map id="defaultMap" name="defaultMap">
 *        &lt;area href="bigPicture.jsp" shape="rect" alt="big picture of the image" coords="0,0,80,80">
 *    &lt;/map></pre>
 * @netui:tag name="area" description="Generates a URL-encoded area to a specified URI."
 * @see Attribute
 * @see java.lang.String
 */
public class Area extends AnchorBase
{
    protected static final String REQUIRED_ATTR = "tagId, href, action";

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "Area";
    }

    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    public AbstractHtmlState getState()
    {
        return _state;
    }

    /**
     * Sets the property to specify the alt text of the image.
     * @param alt the image alignment.
     * @jsptagref.attributedescription Specifies alternate text for the area.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Specifies alternate text for the area."
     */
    public void setAlt(String alt)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt, true);
    }

    /**
     * Prepare the hyperlink for rendering
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        if (hasErrors())
            return SKIP_BODY;
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Render the hyperlink.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        // report errors that may have occurred when the required attributes are being set
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        // build the anchor into the results
        ByRef script = new ByRef();

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        TagRenderingBase trb = TagRenderingBase.Factory.getRendering(TagRenderingBase.AREA_TAG, request);

        if (!createAnchorBeginTag(request, script, trb, writer, REQUIRED_ATTR)) {
            if (!script.isNull())
                write(script.getRef().toString());
            return reportAndExit(EVAL_PAGE);
        }

        assert(trb != null) : "trb is null";
        trb.doEndTag(writer);

        if (!script.isNull())
            write(script.getRef().toString());

        // Render the remainder to the output stream
        localRelease();
        return EVAL_PAGE;
    }
}
