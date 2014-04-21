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
package org.apache.beehive.netui.tags.tree;

import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Base class to help handle issues specific to rendering a tree
 * for certain paths of execution in NetUI. For example, the handling
 * of errors is different for the Tree tag rendering than the Serlvet
 * that renders a tree node for an XmlHttpRequest.
 */
abstract public class TreeRenderSupport implements HtmlConstants
{
    abstract protected void registerTagError(String message, Throwable e)
            throws JspException;

    abstract protected String renderTagId(HttpServletRequest request, String tagId, AbstractHtmlState state);

    protected void renderBeforeNode(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderAfterNode(AbstractRenderAppender writer, TreeElement node)
    {
    }
}
