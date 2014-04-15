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
package org.apache.beehive.netui.tags.rendering;

import org.apache.beehive.netui.tags.html.HtmlConstants;

import java.util.HashMap;

/**
 * Rendering for HTML anchor tags &lt;a>.  In HTML 4.01 the start and end tags are both required.
 * There are no required attributes.
 */
abstract public class AnchorTag extends TagHtmlBase implements HtmlConstants
{
    /**
     * Add the Renderer for the HTML and XHTML tokens.
     * @param html  The map of HTML Tag Renderers
     * @param xhtml The map of XHTML Tag Renderers
     */
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(ANCHOR_TAG, new Rendering());
        htmlQuirks.put(ANCHOR_TAG, new Rendering());
        xhtml.put(ANCHOR_TAG, new Rendering());
    }

    /**
     * The State assocated with the Anchor Tag.
     */
    public static class State extends AbstractHtmlState
    {
        public String name;
        public String href;

        public void clear()
        {
            super.clear();

            name = null;
            href = null;
        }
    }

    /**
     * Private class implementation of of the Anchor Tag.
     */
    private static class Rendering extends AnchorTag
    {
        public void doStartTag(AbstractRenderAppender sb, AbstractTagState renderState)
        {
            assert(sb != null) : "Parameter 'sb' must not be null";
            assert(renderState != null) : "Parameter 'renderState' must not be null";
            assert(renderState instanceof State) : "Paramater 'renderState' must be an instance of AnchorTag.State";

            // convert to AnchorTag.State
            State state = (State) renderState;

            renderTag(sb, ANCHOR);
            renderAttribute(sb, ID, state.id);
            renderAttribute(sb, NAME, state.name);
            renderAttribute(sb, HREF, state.href);

            renderAttribute(sb, CLASS, state.styleClass);
            renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
            renderAttribute(sb, STYLE, state.style);

            renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, sb, state);
            sb.append(">");
        }

        public void doEndTag(AbstractRenderAppender sb)
        {
            renderEndTag(sb, ANCHOR);
        }
    }
}
