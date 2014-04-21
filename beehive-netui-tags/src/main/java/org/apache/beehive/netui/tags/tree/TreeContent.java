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
 * @jsptagref.tagdescription Set the additional content that will be displayed on the right of the label of a tree.
 * @example The following example shows a &lt;netui:tree> tag with both a
 * child &lt;netui:treeLabel> and &lt;netui:treeContent> tag that form the
 * label and the additional text displayed to the righ of the label for the
 * root folder of the tree.
 *
 * <pre>    &lt;netui:tree dataSource="pageFlow.myTree" selectionAction="postback" tagId="myTree">
 *        &lt;netui:treeItem expanded="true" >
 *            &lt;netui:treeLabel>Root Folder&lt;/netui:treeLabel>
 *            &lt;netui:treeContent>Content to right of label: Root Folder&lt;/netui:treeContent>
 *        &lt;/netui:treeItem>
 *    &lt;/netui:tree></pre>
 * @netui:tag name="treeContent" body-content="scriptless"
 * description="Set the additional content that will be displayed on the right of the label of a tree."
 * @see org.apache.beehive.netui.tags.tree.Tree
 * @see org.apache.beehive.netui.tags.tree.TreeElement
 */
public class TreeContent extends AbstractSimpleTag
{
    private static final Logger logger = Logger.getInstance(TreeContent.class);
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
        if (value != null)
            _text = value;

        Object o = getParent();
        assert (o != null);
        if (!(o instanceof TreeItem)) {
            logger.error("Invalid Parent (expected a TreeItem):" + o.getClass().getName());
            return;
        }

        // assign the value to the parent's label value
        TreeItem ti = (TreeItem) o;
        ti.setItemContent(_text);
    }
}
