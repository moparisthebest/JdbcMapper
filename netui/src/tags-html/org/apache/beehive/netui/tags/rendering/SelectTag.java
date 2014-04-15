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
 * Body, Start Tag: optional, End tag: optional
 * Required href
 */
public abstract class SelectTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(SELECT_TAG, new HtmlRendering());
        htmlQuirks.put(SELECT_TAG, new HtmlRendering());
        xhtml.put(SELECT_TAG, new XhtmlRendering());
    }

    public static class State extends AbstractHtmlControlState
    {
        public boolean disabled;
        public boolean multiple;
        public int size;

        public void clear()
        {
            super.clear();
            disabled = false;
            multiple = false;
            size = 0;
        }
    }

    public void doStartTag(AbstractRenderAppender sb, AbstractTagState renderState)
    {
        assert(sb != null) : "Parameter 'sb' must not be null";
        assert(renderState != null) : "Parameter 'renderState' must not be null";
        assert(renderState instanceof State) : "Paramater 'renderState' must be an instance of SelectTag.State";

        State state = (State) renderState;

        renderTag(sb, SELECT);
        renderAttribute(sb, NAME, state.name);
        renderAttribute(sb, ID, state.id);
        renderAttribute(sb, CLASS, state.styleClass);
        renderMultiple(sb, state.multiple);
        renderDisabled(sb, state.disabled);
        if (state.size > 0)
            renderAttribute(sb, SIZE, Integer.toString(state.size));

        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
        renderAttribute(sb, STYLE, state.style);
        renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, sb, state);
        sb.append(">");
    }

    public void doEndTag(AbstractRenderAppender sb)
    {
        renderEndTag(sb, SELECT);
    }

    abstract protected void renderDisabled(AbstractRenderAppender sb, boolean disabled);

    abstract protected void renderMultiple(AbstractRenderAppender sb, boolean readonly);

    private static class HtmlRendering extends SelectTag
    {
        protected void renderDisabled(AbstractRenderAppender sb, boolean disabled)
        {
            if (disabled)
                sb.append(" disabled");

        }

        protected void renderMultiple(AbstractRenderAppender sb, boolean multiple)
        {
            if (multiple)
                sb.append(" multiple");

        }
    }

    private static class XhtmlRendering extends SelectTag
    {
        protected void renderDisabled(AbstractRenderAppender sb, boolean disabled)
        {
            if (disabled)
                renderAttribute(sb, "disabled", "disabled");

        }

        protected void renderMultiple(AbstractRenderAppender sb, boolean multiple)
        {
            if (multiple)
                renderAttribute(sb, "multiple", "multiple");

        }
    }
}
