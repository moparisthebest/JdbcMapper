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

public abstract class ScriptTag extends TagHtmlBase
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(SCRIPT_TAG, new ScriptTag.Rendering());
        htmlQuirks.put(SCRIPT_TAG, new ScriptTag.Rendering());
        xhtml.put(SCRIPT_TAG, new ScriptTag.Rendering());
    }

    public static class State extends AbstractAttributeState
    {
        public String type;
        public String src;
        public String language;
        public boolean suppressComments = true;

        public void clear()
        {
            super.clear();
            type = null;
            src = null;
            language = null;
            suppressComments = true;
        }
    }

    public abstract void doEndTag(Appender sb, boolean supressComments);

    private static class Rendering extends ScriptTag implements HtmlConstants
    {
        public void doStartTag(Appender sb, AbstractTagState renderState)
        {
            assert(sb != null) : "Parameter 'sb' must not be null";
            assert(renderState != null) : "Parameter 'renderState' must not be null";
            assert(renderState instanceof State) : "Paramater 'renderState' must be an instance of SpanTag.State";

            State state = (State) renderState;

            renderTag(sb, SCRIPT);

            if (state.language == null) {
                // @todo: this should be all lower case!
                state.language = "JavaScript";
            }
            if (state.type == null) {
                // @todo: this should be all lower case!
                state.type = "text/JavaScript";
            }

            renderAttribute(sb, LANGUAGE, state.language);
            renderAttribute(sb, TYPE, state.type);
            renderAttribute(sb, SRC, state.src);

            renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
            sb.append(">");

            // for javascript will will continue put the script itself into html comments
            if (!state.suppressComments)
                sb.append("\n<!--\n");
        }

        public void doEndTag(Appender sb, boolean supressComments)
        {
            if (!supressComments)
                sb.append("-->\n");
            doEndTag(sb);
        }

        public void doEndTag(Appender sb)
        {
            renderEndTag(sb, SCRIPT);
        }
    }
}
