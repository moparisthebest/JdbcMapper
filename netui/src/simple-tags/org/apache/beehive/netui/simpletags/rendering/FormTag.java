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

/**
 * Renderthe HTML &lt;form> element.  In HTML 4.01 both the start and end tag are rquired.
 * The action is a required attribute.
 */
public abstract class FormTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(FORM_TAG, new HtmlRendering());
        htmlQuirks.put(FORM_TAG, new HtmlRendering());
        xhtml.put(FORM_TAG, new XhtmlRendering());
    }

    public static class State extends AbstractHtmlState
    {
        public String name;
        public String method;
        public String action;

        public void clear()
        {
            super.clear();

            name = null;
            method = null;
            action = null;
        }
    }

    public void doStartTag(Appender sb, AbstractTagState renderState)
    {
        State state = (State) renderState;

        renderTag(sb, FORM);
        renderNameAndId(sb, state);
        renderAttribute(sb, ACTION, state.action);
        renderAttribute(sb, CLASS, state.styleClass);
        renderAttribute(sb, METHOD, state.method);

        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
        renderAttribute(sb, STYLE, state.style);
        renderAttributes(AbstractHtmlState.ATTR_JAVASCRIPT, sb, state);

        sb.append(">");
    }

    public void doEndTag(Appender sb)
    {
        renderEndTag(sb, FORM);
    }

    abstract void renderNameAndId(Appender sb, State renderState);

    private static class HtmlRendering extends FormTag
    {
        public void renderNameAndId(Appender sb, State state)
        {
            renderAttribute(sb, NAME, state.name);
            renderAttribute(sb, ID, state.id);
        }
    }

    private static class XhtmlRendering extends FormTag
    {
        public void renderNameAndId(Appender sb, State state)
        {
            renderAttribute(sb, ID, state.name);
        }
    }
}
