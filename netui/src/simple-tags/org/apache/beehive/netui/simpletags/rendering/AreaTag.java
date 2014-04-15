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
package org.apache.beehive.netui.simpletags.rendering;

import org.apache.beehive.netui.simpletags.html.HtmlConstants;
import org.apache.beehive.netui.simpletags.appender.Appender;

import java.util.HashMap;

abstract public class AreaTag extends TagHtmlBase implements HtmlConstants
{
    /**
     * Add the Renderer for the HTML and XHTML tokens.
     * @param html  The map of HTML Tag Renderers
     * @param xhtml The map of XHTML Tag Renderers
     */
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(AREA_TAG, new HtmlRendering());
        htmlQuirks.put(AREA_TAG, new HtmlRendering());
        xhtml.put(AREA_TAG, new XhtmlRendering());
    }

    public void doStartTag(Appender sb, AbstractTagState renderState)
    {
        assert(sb != null) : "Parameter 'sb' must not be null";
        assert(renderState != null) : "Parameter 'renderState' must not be null";
        assert(renderState instanceof AnchorTag.State) : "Paramater 'renderState' must be an instance of AnchorTag.State:"
                + renderState.getClass().getName();

        AnchorTag.State state = (AnchorTag.State) renderState;

        renderTag(sb, AREA);
        renderAttribute(sb, ID, state.id);
        renderAttribute(sb, NAME, state.name);
        renderAttribute(sb, HREF, state.href);

        renderAttribute(sb, CLASS, state.styleClass);
        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
        renderAttribute(sb, STYLE, state.style);

        //String onclick = state.getAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK);
        //if (onclick != null)
        //    state.removeAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK);
        renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, sb, state);

        // backward compat for a second
        //renderAttributeSingleQuotes(sb, ONCLICK, onclick);

        writeEnd(sb);
    }

    public void doEndTag(Appender sb)
    {
    }

    abstract protected void writeEnd(Appender sb);

    private static class HtmlRendering extends AreaTag
    {
        protected void writeEnd(Appender sb)
        {
            sb.append(">");
        }
    }

    private static class XhtmlRendering extends AreaTag
    {
        protected void writeEnd(Appender sb)
        {
            sb.append(" />");
        }
    }
}
