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
public abstract class TextAreaTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(TEXT_AREA_TAG, new HtmlRendering());
        htmlQuirks.put(TEXT_AREA_TAG, new HtmlRendering());
        xhtml.put(TEXT_AREA_TAG, new XhtmlRendering());
    }

    public static class State extends AbstractHtmlControlState
    {
        public int rows;
        public int cols;
        public boolean readonly;
        public boolean disabled;

        public void clear()
        {
            super.clear();
            rows = 0;
            cols = 0;
            readonly = false;
            disabled = false;
        }
    }

    public void doStartTag(AbstractRenderAppender sb, AbstractTagState renderState)
    {
        assert(sb != null) : "Parameter 'sb' must not be null";
        assert(renderState != null) : "Parameter 'renderState' must not be null";
        assert(renderState instanceof State) : "Paramater 'renderState' must be an instance of TextAreaTag.State";

        State state = (State) renderState;

        renderTag(sb, TEXTAREA);
        renderAttribute(sb, NAME, state.name);
        renderAttribute(sb, ID, state.id);
        renderAttribute(sb, CLASS, state.styleClass);

        renderReadonly(sb, state.readonly);
        renderDisabled(sb, state.disabled);

        if (state.rows > 0)
            renderAttribute(sb, ROWS, Integer.toString(state.rows));
        if (state.cols > 0)
            renderAttribute(sb, COLS, Integer.toString(state.cols));

        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
        renderAttribute(sb, STYLE, state.style);
        renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, sb, state);
        sb.append(">");
    }

    public void doEndTag(AbstractRenderAppender sb)
    {
        renderEndTag(sb, TEXTAREA);
    }

    abstract protected void renderDisabled(AbstractRenderAppender sb, boolean disabled);

    abstract protected void renderReadonly(AbstractRenderAppender sb, boolean readonly);

    private static class HtmlRendering extends TextAreaTag
    {
        protected void renderDisabled(AbstractRenderAppender sb, boolean disabled)
        {
            if (disabled)
                sb.append(" disabled");
        }

        protected void renderReadonly(AbstractRenderAppender sb, boolean readonly)
        {
            if (readonly)
                sb.append(" readonly");
        }
    }

    private static class XhtmlRendering extends TextAreaTag
    {
        protected void renderDisabled(AbstractRenderAppender sb, boolean disabled)
        {
            if (disabled)
                renderAttribute(sb, "disabled", "disabled");
        }

        protected void renderReadonly(AbstractRenderAppender sb, boolean readonly)
        {
            if (readonly)
                renderAttribute(sb, "readonly", "readonly");
        }
    }
}

