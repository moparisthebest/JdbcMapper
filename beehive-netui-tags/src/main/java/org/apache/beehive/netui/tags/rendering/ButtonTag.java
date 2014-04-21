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
 * Render the HTML &lt;button> element.  In HTML 4.01 the start and end tags are required.
 *
 * <p>This renderer is just used by the NetUI Button as an alternative to the &ltinput>
 * HTML element rendered by the InputSubmitTag.
 * </p>
 */
public abstract class ButtonTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(BUTTON_TAG, new HtmlRendering());
        htmlQuirks.put(BUTTON_TAG, new HtmlRendering());
        xhtml.put(BUTTON_TAG, new XhtmlRendering());
    }

    public static class State extends InputSubmitTag.State
    {
    }

    public void doStartTag(AbstractRenderAppender writer, AbstractTagState renderState)
    {
        assert(writer != null) : "Parameter 'writer' must not be null";
        assert(renderState != null) : "Parameter 'renderState' must not be null";
        assert(renderState instanceof ButtonTag.State) : "Paramater 'renderState' must be an instance of ButtonTag.State";

        State state = (State) renderState;

        // Generate an HTML element
        renderTag(writer, BUTTON);
        if (state.type == null)
            state.type = INPUT_SUBMIT;
        renderAttribute(writer, TYPE, state.type);
        renderAttribute(writer, NAME, state.name);
        renderAttribute(writer, ID, state.id);
        renderAttribute(writer, CLASS, state.styleClass);
        renderAttribute(writer, VALUE, state.value);
        renderDisabled(writer, state.disabled);

        renderAttributes(AbstractHtmlState.ATTR_GENERAL, writer, state);
        renderAttribute(writer, STYLE, state.style);
        renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, writer, state);
        writer.append(">");
    }

    public void doEndTag(AbstractRenderAppender writer)
    {
        renderEndTag(writer, BUTTON);
    }

    abstract protected void renderDisabled(AbstractRenderAppender writer, boolean disabled);

    private static class HtmlRendering extends ButtonTag
    {
        protected void renderDisabled(AbstractRenderAppender writer, boolean disabled)
        {
            if (disabled)
                writer.append(" disabled");

        }

    }

    private static class XhtmlRendering extends ButtonTag
    {
        protected void renderDisabled(AbstractRenderAppender writer, boolean disabled)
        {
            if (disabled)
                renderAttribute(writer, "disabled", "disabled");
        }
    }
}
