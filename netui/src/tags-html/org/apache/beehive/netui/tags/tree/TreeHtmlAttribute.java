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

/**
 * @jsptagref.tagdescription Set an attribute on the tree.
 * @example The following example shows a &lt;netui:tree> tag with both a
 * child &lt;netui:treeLabel> and &lt;netui:treeHtmlAttribute> tag that form the
 * label for the root folder of the tree. The &lt;netui:treeHtmlAttribute> 
 * outputs the attribute <code>name="A1"</code> on the anchor element used for
 * the selection link of the tree item.
 *
 * <pre>    &lt;netui:tree dataSource="pageFlow.myTree" selectionAction="postback" tagId="myTree">
 *        &lt;netui:treeItem expanded="true" >
 *            &lt;netui:treeLabel>Root Folder&lt;/netui:treeLabel>
              &lt;netui:treeHtmlAttribute attribute="name" value="A1" onSelectionLink="true"/>
 *        &lt;/netui:treeItem>
 *    &lt;/netui:tree></pre>
 * @netui:tag name="treeHtmlAttribute" body-content="empty" description="Set an attribute on the tree."
 * @see org.apache.beehive.netui.tags.tree.Tree
 * @see org.apache.beehive.netui.tags.tree.TreeElement
 */
public class TreeHtmlAttribute extends AbstractSimpleTag
{
    private static final Logger logger = Logger.getInstance(TreeHtmlAttribute.class);

    TreeHtmlAttributeInfo _info = new TreeHtmlAttributeInfo();

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "TreeHtmlAttribute";
    }

    /**
     * Sets the name of the attribute.  This must be in the proper HTML Attribute form.  For example,
     * "onmouseclick" or "href".
     * @param attr the name of the HTML attribute.
     * @jsptagref.attributedescription Sets the name of the attribute.  This must be in the proper HTML Attribute form.  For example,
     * "onmouseclick" or "href".
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="true"
     * description="Sets the name of the attribute."
     */
    public void setAttribute(String attr)
    {
        _info.setAttribute(attr);
    }

    /**
     * Sets HTML attribute value.
     * @param value The value of the HTML Attribute.
     * @jsptagref.attributedescription Sets the HTML attribute value.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="true"
     * description="Sets HTML attribute value."
     */
    public void setValue(String value)
    {
        _info.setValue(value);
    }

    /**
     * @param onIcon
     * @jsptagref.attributedescription Set the attribute value on the icon
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false"
     * description="Set the attribute value on the icon."
     */
    public void setOnIcon(boolean onIcon)
    {
        _info.setOnIcon(onIcon);
    }

    /**
     * @param onSelectionLink
     * @jsptagref.attributedescription Set the attribute value on the link around the icon.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false"
     * description="Set the attribute value on the link around the icon."
     */
    public void setOnSelectionLink(boolean onSelectionLink)
    {
        _info.setOnSelectionLink(onSelectionLink);
    }

    /**
     * @param onDiv
     * @jsptagref.attributedescription Set the attribute value on the item &lt;div>.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false"
     * description="Set the attribute value on the item div."
     */
    public void setOnDiv(boolean onDiv)
    {
        _info.setOnDiv(onDiv);
    }

    /**
     * @param applyToDescendents
     * @jsptagref.attributedescription Apply the attribute to descendents of this node.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean</i>
     * @netui:attribute required="false"
     * description="Apply the attribute value to descendents of this node."
     */
    public void setApplyToDescendents(boolean applyToDescendents)
    {
        _info.setApplyToDescendents(applyToDescendents);
    }

    public void doTag()
            throws JspException
    {
        Object o = getParent();
        assert (o != null);

        if (o instanceof TreeItem) {
            TreeItem ti = (TreeItem) o;
            ti.setItemAttribute(_info);
        }
        else {
            logger.error("Found an unexpected parent object'" + o.getClass().getName() + "' expected a TreeItem");
        }
    }
}

