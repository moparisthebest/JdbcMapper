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

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * @jsptagref.tagdescription Set the text displayed for the selection link the given tree node (the TreeItem tag).
 *
 * <p>Note that a <code>&lt;netui:treeLabel></code> tag is required for all non-leaf nodes in the tree.</p> 
 * @netui:tag name="treeLabel"  body-content="scriptless" description="Set the label for the parent TreeItem."
 * @see org.apache.beehive.netui.tags.tree.Tree
 * @see org.apache.beehive.netui.tags.tree.TreeElement
 */
public class TreeLabel extends AbstractSimpleTag
{
    private static final Logger logger = Logger.getInstance(TreeLabel.class);

    private String _text;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "TreeLabel";
    }

    /**
     * Add this node to the parent.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException, IOException
    {
        String value = getBufferBody(true);
        if (value.length() > 0)
            _text = value;

        Object o = getParent();
        assert (o != null);
        if (!(o instanceof TreeItem)) {
            logger.error("Invalid Parent (expected a TreeItem):" + o.getClass().getName());
            return;
        }

        // assign the value to the parent's label value
        TreeItem ti = (TreeItem) o;
        ti.setItemLabel(_text);
    }
}
