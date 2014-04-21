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

/**
 * This class will render the constant HTML used by the tags
 */
abstract public class ConstantRendering
{
    abstract public void BR(AbstractRenderAppender sb);

    public void TR_TD(AbstractRenderAppender sb)
    {
        sb.append("<tr><td>");
    }

    public void end_TD_TR(AbstractRenderAppender sb)
    {
        sb.append("</td></tr>");
    }

    public void TABLE(AbstractRenderAppender sb)
    {
        sb.append("<table>");
    }

    public void end_TABLE(AbstractRenderAppender sb)
    {
        sb.append("</table>");
    }

    public void NBSP(AbstractRenderAppender sb)
    {
        sb.append("&nbsp;");
    }

    private static class HtmlConstants extends ConstantRendering
    {
        public void BR(AbstractRenderAppender sb)
        {
            sb.append("<br>");
        }
    }

    private static class XhtmlConstants extends ConstantRendering
    {
        public void BR(AbstractRenderAppender sb)
        {
            sb.append("<br />");
        }
    }

    public static ConstantRendering getRendering(int type)
    {

        if (type == TagRenderingBase.HTML_RENDERING)
            return new HtmlConstants();
        if (type == TagRenderingBase.XHTML_RENDERING)
            return new XhtmlConstants();
        assert (false) : "Didn't find the requested contant renderer:" + type;
        return null;
    }
}
