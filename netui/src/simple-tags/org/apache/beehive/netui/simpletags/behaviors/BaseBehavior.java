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

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.Behavior;
import org.apache.beehive.netui.simpletags.html.HtmlConstants;
import org.apache.beehive.netui.simpletags.rendering.AbstractAttributeState;
import org.apache.beehive.netui.simpletags.rendering.BaseTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides the base for every URL on this page, by generating the html &lt;base> element which appears in
 * the HTML header.
 */
public class BaseBehavior extends Behavior implements HtmlConstants
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
        _state.target = target;
    }

    /**
     * Base support for the attribute tag.  The <code>href</code> may not bet set.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     */
    public void setAttribute(String name, String value, String facet)
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
     * Render the HTML &lt;base> element with the href set.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderStart(Appender appender)
    {
        // This tag will use the HttpServlet request to see the scheme and port
        // that is being used.
        HttpServletRequest request = ContextUtils.getPageFlowContext().getRequest();

        // calculate the href
        InternalStringBuilder buf = new InternalStringBuilder(64);
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
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.BASE_TAG);
        br.doStartTag(appender, _state);
    }

    /**
     * This really doesn't do anything, but is called in case the HTML base element
     * may contain content in the future.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderEnd(Appender appender) {
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.BASE_TAG);
        br.doEndTag(appender);

        // This will produce invalid HTML/XHTML if there are errors
        // because we are going to put markup out into the head.
        if (hasErrors())
            reportErrors(appender);
    }
}
