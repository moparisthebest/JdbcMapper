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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Derived class to help handle issues specific to rendering the nodes
 * of a tree for an XmlHttpRequest via the {@link TreeCRI}. 
 */
public class ServletTreeRenderSupport extends TreeRenderSupport
{
    private static final Logger logger = Logger.getInstance(ServletTreeRenderSupport.class);
    AbstractRenderAppender _writer;
    InternalStringBuilder _sb;

    ServletTreeRenderSupport(AbstractRenderAppender writer, InternalStringBuilder sb)
    {
        _writer = writer;
        _sb = sb;
    }

    protected void renderBeforeNode(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append("<![CDATA[");
    }

    protected void renderAfterNode(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append("]]>");
        TreeCRI.writeElement(_writer, "treeDiv", writer.toString().trim());
        _sb.setLength(0);
    }

    /**
     * Errors during rendering will call through this method.  During the XmlHttpRequest, these
     * will just be logged and we will go on.
     * @param message
     * @param e
     * @throws JspException
     */
    protected void registerTagError(String message, Throwable e)
            throws JspException
    {
        System.err.println("Error in rendering tree:" + message);
        logger.error(message, e);
    }

    protected String renderTagId(HttpServletRequest request, String tagId, AbstractHtmlState state)
    {
        // @todo: this needs to be implemented
        return null;
    }
}
