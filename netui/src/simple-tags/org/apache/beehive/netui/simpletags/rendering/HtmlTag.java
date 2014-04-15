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
abstract public class HtmlTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(HTML_TAG, new HtmlTag.HtmlRendering());
        htmlQuirks.put(HTML_TAG, new HtmlTag.HtmlQuirksRendering());
        xhtml.put(HTML_TAG, new HtmlTag.XhtmlRendering());
    }

    public static class State extends AbstractAttributeState
    {
        public String lang;
        public String dir;

        public void clear()
        {
            super.clear();

            lang = null;
            dir = null;
        }
    }

    public void doStartTag(Appender sb, AbstractTagState renderState)
    {
        State state = (State) renderState;

        renderDocType(sb);
        sb.append("\n");
        renderTag(sb, HTML);
        renderAttribute(sb, LANG, state.lang);
        renderAttribute(sb, DIR, state.dir);
        renderAdditionalAttributes(sb, state);
        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);

        sb.append(">");
    }

    public void doEndTag(Appender sb)
    {
        renderEndTag(sb, HTML);
    }

    abstract protected void renderAdditionalAttributes(Appender sb, State renderState);

    abstract protected void renderDocType(Appender sb);

    private static class HtmlRendering extends HtmlTag
    {
        protected void renderDocType(Appender sb)
        {
            sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n\t\"http://www.w3.org/TR/html4/loose.dtd\">");
        }

        protected void renderAdditionalAttributes(Appender sb, State renderState)
        {
        }
    }

    private static class HtmlQuirksRendering extends HtmlTag
    {
        protected void renderDocType(Appender sb)
        {
            sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n>");
        }

        protected void renderAdditionalAttributes(Appender sb, State renderState)
        {
        }
    }

    private static class XhtmlRendering extends HtmlTag
    {
        protected void renderDocType(Appender sb)
        {
            sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n\t\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        }

        protected void renderAdditionalAttributes(Appender sb, State renderState)
        {
            renderAttribute(sb, "xml:lang", renderState.lang);
            renderAttribute(sb, "xmlns", "http://www.w3.org/1999/xhtml");
        }
    }
}
