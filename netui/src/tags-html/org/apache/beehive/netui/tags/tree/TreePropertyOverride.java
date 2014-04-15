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
 * 
 * @jsptagref.tagdescription Overrides the tree properties.
 *
 * <p>Note that this tag automatically applies to descendent nodes in the tree.</p>
 * @netui:tag name="treePropertyOverride" body-content="empty" description="Overrides the tree properties"
 */
public class TreePropertyOverride extends AbstractSimpleTag
{
    private static final Logger logger = Logger.getInstance(TreePropertyOverride.class);
    private InheritableState _iState = new InheritableState();

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "TreePropertyOverride";
    }

    /**
     * Sets the action used for expanding and contracting tree nodes.
     * @param action the action
     * @jsptagref.attributedescription Sets the action used for expanding and contracting tree nodes.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the action used for expanding and contracting tree nodes."
     */
    public void setSelectionAction(String action)
    {
        _iState.setSelectionAction(action);
    }

    /**
     * Sets the action used for expanding and contracting tree nodes.
     * @param action the action
     * @jsptagref.attributedescription Sets the action used for expanding and contracting tree nodes.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false"
     * description="Sets the action used for expanding and contracting tree nodes."
     */
    public void setExpansionAction(String action)
    {
        _iState.setExpansionAction(action);
    }

    /**
     * Sets the image name for an open non-leaf node with no
     * line below it.  (Defaults to "lastNodeExpanded.gif").
     * @param lastNodeExpandedImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for an open non-leaf node with no
     * line below it.  (Defaults to "lastNodeExpanded.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for an open non-leaf node with no
     * line below it."
     */
    public void setLastNodeExpandedImage(String lastNodeExpandedImage)
    {
        _iState.setLastNodeExpandedImage(lastNodeExpandedImage);
    }

    /**
     * Sets the image name for an open non-leaf node with a
     * line below it.  (Defaults to "nodeExpanded.gif").
     * @param nodeExpandedImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for an open non-leaf node with a
     * line below it.  (Defaults to "nodeExpanded.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for an open non-leaf node with a
     * line below it."
     */
    public void setNodeExpandedImage(String nodeExpandedImage)
    {
        _iState.setNodeExpandedImage(nodeExpandedImage);
    }

    /**
     * Sets the image name for a closed non-leaf node with no
     * line below it.  (Defaults to "lastNodeCollapsed.gif").
     * @param lastNodeCollapsedImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for a closed non-leaf node with no
     * line below it.  (Defaults to "lastNodeCollapsed.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for a closed non-leaf node with no
     * line below it."
     */
    public void setLastNodeCollapsedImage(String lastNodeCollapsedImage)
    {
        _iState.setLastNodeCollapsedImage(lastNodeCollapsedImage);
    }

    /**
     * Sets the image name for a closed non-leaf node with a
     * line below it.  (Defaults to "nodeCollapsed.gif").
     * @param nodeCollapsedImage the image name (including extension)
     * @jsptagref.attributedescription  Sets the image name for a closed non-leaf node with a
     * line below it.  (Defaults to "nodeCollapsed.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for a closed non-leaf node with a
     * line below it."
     */
    public void setNodeCollapsedImage(String nodeCollapsedImage)
    {
        _iState.setNodeCollapsedImage(nodeCollapsedImage);
    }

    /**
     * Sets the image name for a blank area of the tree.
     * (Defaults to "lastLineJoin.gif").
     * @param lastLineJoinImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for a blank area of the tree.
     * (Defaults to "lastLineJoin.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for a blank area of the tree."
     */
    public void setLastLineJoinImage(String lastLineJoinImage)
    {
        _iState.setLastLineJoinImage(lastLineJoinImage);
    }

    /**
     * Sets the image name for an image used to align the other images inside of a tree.  (Defaults to "spacer.gif").
     * @param spacerImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for an image used to align the other images inside of a tree.
     * (Defaults to "spacer.gif")
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for an image used to align the other images inside of a tree."
     */
    public void setSpacerImage(String spacerImage)
    {
        _iState.setImageSpacer(spacerImage);
    }

    /**
     * Sets the default icon for TreeElements for a blank area of the tree.
     * (Defaults to "folder.gif").
     * @param itemIcon the image name of the itemIcon
     * @jsptagref.attributedescription Sets the default icon for tree nodes 
     * for a blank area of the tree. (Defaults to "folder.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the default icon for TreeElements for a blank area of the tree."
     */
    public void setItemIcon(String itemIcon)
    {
        _iState.setItemIcon(itemIcon);
    }

    /**
     * Sets the image name for an area with a line through it.
     * (Defaults to "lineJoin.gif").
     * @param lineJoinImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for an area with a line through it.
     * (Defaults to "lineJoin.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for an area with a line through it."
     */
    public void setLineJoinImage(String lineJoinImage)
    {
        _iState.setLineJoinImage(lineJoinImage);
    }

    /**
     * Sets the image name for an area with a line through it.
     * (Defaults to "verticalLine.gif").
     * @param verticalLineImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for an area with a line through it.
     * (Defaults to "verticalLine.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string</i>
     * @netui:attribute required="false"
     * description="Sets the image name for an area with a line through it."
     */
    public void setVerticalLineImage(String verticalLineImage)
    {
        _iState.setVerticalLineImage(verticalLineImage);
    }

    /**
     * Sets the name of the directory containing the images for our icons,
     * relative to the page including this tag.
     * @param imageRoot the directory name
     * @jsptagref.attributedescription Sets the name of the directory containing the images for our icons,
     * relative to the page including this tag.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the name of the directory containing the images for our icons,
     * relative to the page including this tag."
     */
    public void setImageRoot(String imageRoot)
    {
        _iState.setImageRoot(imageRoot);
    }

    /**
     * This will set the location of the icon images.  When the location
     * is explicitly set, this works exactly the same as all other inheritable
     * properties.  When this is not set, it will return the <code>getImageRoot</code>
     * location.
     * @param iconRoot the directory name for the icons
     * @jsptagref.attributedescription The directory containing the icon images for tree items.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_iconRoot</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the name of the directory containing the images for our icons."
     */
    public void setIconRoot(String iconRoot)
    {
        _iState.setIconRoot(iconRoot);
    }

    /**
     * Render this Tree control.
     * @throws JspException if a processing error occurs
     */
    public void doTag()
            throws JspException
    {
        Object o = getParent();
        assert (o != null);
        if (!(o instanceof TreeItem)) {
            logger.error("Invalid Parent (expected a TreeItem):" + o.getClass().getName());
            return;
        }

        // assign the value to the parent's label value
        TreeItem ti = (TreeItem) o;
        ti.setItemInheritableState(_iState);
    }
}
