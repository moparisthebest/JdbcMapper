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

import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Derived class to help handle issues specific to rendering a tree
 * for the {@link Tree} tag.
 */
public class TagTreeRenderSupport extends TreeRenderSupport
{
    private Tree _tree;

    TagTreeRenderSupport(Tree tree)
    {
        _tree = tree;
    }

    protected void registerTagError(String message, Throwable e)
            throws JspException
    {
        _tree.registerTagError(message, e);
    }

    protected String renderTagId(HttpServletRequest request, String tagId, AbstractHtmlState state)
    {
        return _tree.renderTagId(request, tagId, state);
    }

}
