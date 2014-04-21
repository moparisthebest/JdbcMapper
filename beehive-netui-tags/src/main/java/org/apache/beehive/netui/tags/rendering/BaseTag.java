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
 * Render the HTML &lt;base> tag.  This is the document base URI.
 * In HTML 4.01 the start tag is required and the end tag forbidden.
 * The tag will be rendered as a single tag.  The href attribute is required in HTML 4.01 and optional
 * in XHTML.
 */
public abstract class BaseTag extends TagHtmlBase implements HtmlConstants
{
    public static void add(HashMap html, HashMap htmlQuirks, HashMap xhtml)
    {
        html.put(BASE_TAG, new HtmlRendering());
        htmlQuirks.put(BASE_TAG, new HtmlRendering());
        xhtml.put(BASE_TAG, new XhtmlRendering());
    }

    public static class State extends AbstractAttributeState
    {
        public String target;
        public String href;

        public void clear()
        {
            super.clear();

            target = null;
            href = null;
        }
    }

    public void doStartTag(AbstractRenderAppender sb, AbstractTagState renderState)
    {
        assert(sb != null) : "Parameter 'sb' must not be null";
        assert(renderState != null) : "Parameter 'renderState' must not be null";
        assert(renderState instanceof BaseTag.State) : "Paramater 'renderState' must be an instance of Base.State";

        State state = (State) renderState;

        renderTag(sb, BASE);
        renderAttribute(sb, HREF, state.href);
        renderAttribute(sb, TARGET, state.target);

        // These are not actually used by the <base> tag, but may be set by external sources.
        renderAttributes(AbstractHtmlState.ATTR_GENERAL, sb, state);
        writeEnd(sb);
    }

    public void doEndTag(AbstractRenderAppender sb)
    {
        // do nothing...
    }

    abstract protected void writeEnd(AbstractRenderAppender sb);


    private static class HtmlRendering extends BaseTag
    {
        protected void writeEnd(AbstractRenderAppender sb)
        {
            sb.append(">");
        }
    }

    private static class XhtmlRendering extends BaseTag
    {
        protected void writeEnd(AbstractRenderAppender sb)
        {
            sb.append(" />");
        }
    }
}
