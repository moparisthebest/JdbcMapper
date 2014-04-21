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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

//java imports

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.tags.IAttributeConsumer;
import org.apache.beehive.netui.tags.rendering.AbstractAttributeState;
import org.apache.beehive.netui.tags.rendering.BaseTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * Provides the base for every URL on this page.
 * @jsptagref.tagdescription Provides the base for every URL on the page.
 * @example In this sample, the Base tag is simply dropped in and then automatically determines
 * the base for each URL on this page.
 * <pre>
 * &lt;head>
 *      &lt;netui:base />
 * &lt;/head>
 * </pre>
 * @netui:tag name="base" body-content="scriptless" description="Provides the base for every URL on this page."
 */
public class Base extends AbstractSimpleTag
        implements IAttributeConsumer, HtmlConstants
{
    private BaseTag.State _state = new BaseTag.State();

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "Base";
    }

    /**
     * Set the default window target.
     * @param target the window target.
     * @jsptagref.attributedescription The default window target.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_windowTarget</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The default window target."
     */
    public void setTarget(String target)
    {
        _state.target = setNonEmptyValueAttribute(target);
    }

    /**
     * Base support for the attribute tag.  The <code>href</code> may not bet set.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        boolean error = false;

        // validate the name attribute, in the case of an error simply return.
        if (name == null || name.length() <= 0) {
            String s = Bundle.getString("Tags_AttributeNameNotSet");
            registerTagError(s, null);
            error = true;
        }
        if (facet != null) {
            String s = Bundle.getString("Tags_AttributeFacetNotSupported", new Object[]{facet});
            registerTagError(s, null);
            error = true;
        }

        // it's not legal to set the href attributes this way
        if (name != null && name.equals(HREF)) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
            error = true;
        }
        if (error)
            return;

        // set the state variables that will override the tag settings...
        if (name.equals(TARGET)) {
            _state.target = value;
            return;
        }

        _state.registerAttribute(AbstractAttributeState.ATTR_GENERAL, name, value);
    }

    /**
     * Render the hyperlink.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException, IOException
    {
        PageContext pageContext = getPageContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.BASE_TAG, request);

        // evaluate the body, this is called basically so any attributes my be applied.
        getBufferBody(false);

        InternalStringBuilder buf = new InternalStringBuilder(64);

        // calculate the href
        buf.append(request.getScheme());
        buf.append("://");
        buf.append(request.getServerName());

        String scheme = request.getScheme();
        int port = request.getServerPort();
        if ("http".equals(scheme) && (80 == port)) {
            //Do nothing
        }
        else if ("https".equals(scheme) && (443 == port)) {
            //Do nothing
        }
        else {
            buf.append(":");
            buf.append(request.getServerPort());
        }
        buf.append(request.getRequestURI());
        _state.href = buf.toString();

        // render the tag.
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        br.doStartTag(writer, _state);
        br.doEndTag(writer);

        // This will produce invalid HTML/XHTML if there are errors
        // because we are going to put markup out into the head.
        if (hasErrors())
            reportErrors();
    }
}



