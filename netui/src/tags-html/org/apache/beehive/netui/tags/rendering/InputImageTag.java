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
 * Body, Start Tag: required, End tag: forbidden
 * Required href
 */
abstract public class InputImageTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(INPUT_IMAGE_TAG, new HtmlRendering());
        htmlQuirks.put(INPUT_IMAGE_TAG, new HtmlRendering());
        xhtml.put(INPUT_IMAGE_TAG, new XhtmlRendering());
    }

    public static class State extends AbstractHtmlState
    {
        public String src;
        public String value;
        public boolean disabled;

        public void clear()
        {
            super.clear();
            src = null;
            value = null;
            disabled = false;
        }
    }

    public void doStartTag(AbstractRenderAppender sb, AbstractTagState renderState)
    {
        assert(sb != null) : "Parameter 'sb' must not be null";
        assert(renderState != null) : "Parameter 'renderState' must not be null";
        assert(renderState instanceof State) : "Paramater 'renderState' must be an instance of InputImageTag.State";

        State state = (State) renderState;

        // Generate an HTML element
        renderTag(sb, INPUT);
        renderAttribute(sb, TYPE, INPUT_IMAGE);
        renderAttribute(sb, ID, state.id);
        renderAttribute(sb, CLASS, state.styleClass);
        renderAttribute(sb, SRC, state.src);
        renderAttribute(sb, VALUE, state.value);

        renderDisabled(sb, state.disabled);


        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
        renderAttribute(sb, STYLE, state.style);
        renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, sb, state);

        writeEnd(sb);
    }

    public void doEndTag(AbstractRenderAppender sb)
    {
    }

    abstract protected void writeEnd(AbstractRenderAppender sb);

    abstract protected void renderDisabled(AbstractRenderAppender sb, boolean disabled);


    private static class HtmlRendering extends InputImageTag
    {
        protected void writeEnd(AbstractRenderAppender sb)
        {
            sb.append(">");
        }

        protected void renderDisabled(AbstractRenderAppender sb, boolean disabled)
        {
            if (disabled)
                sb.append(" disabled");

        }

    }

    private static class XhtmlRendering extends InputImageTag
    {
        protected void writeEnd(AbstractRenderAppender sb)
        {
            sb.append(" />");
        }

        protected void renderDisabled(AbstractRenderAppender sb, boolean disabled)
        {
            if (disabled)
                renderAttribute(sb, "disabled", "disabled");
        }
    }
}
