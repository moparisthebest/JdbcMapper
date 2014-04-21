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

/**
 * This structure is used to track the use of HTML attributes within a tree item.
 */
public class TreeHtmlAttributeInfo implements java.io.Serializable
{
    public static final int HTML_LOCATION_DIV = 0;
    public static final int HTML_LOCATION_ICON = 1;
    public static final int HTML_LOCATION_SELECTION_LINK = 2;
    public static final int HTML_LOCATION_CNT = 3;

    private static final int APPLY_TO_DESCENDENTS = 0x01;
    private static final int ON_ICON = 0x02;
    private static final int ON_SELECTION_LINK = 0x04;
    private static final int ON_DIV = 0x08;

    private String _attribute;
    private String _value;
    private int state;
    private TreeElement _parent;

    /**
     * Default constructor for creating a TreeHtmlAttributeInfo.
     */
    public TreeHtmlAttributeInfo()
    {
    }

    /**
     * Construct a new TreeHtmlAttributeInfo with the specified parameters.
     * @param attribute the name of the attribute.
     * @param value sets HTML attribute value.
     */
    public TreeHtmlAttributeInfo( String attribute, String value )
    {
        setAttribute(attribute);
        setValue(value);
    }

    /**
     * Return the name of the attribute.
     * @return the attribute name
     */
    public String getAttribute()
    {
        return _attribute;
    }

    /**
     * Set the name of the attribute.
     * @param attribute the name of the attribute.
     */
    public void setAttribute(String attribute)
    {
        _attribute = attribute;
    }

    /**
     * Return the value of the attribute.
     * @return the String value of the attribute.
     */
    public String getValue()
    {
        return _value;
    }

    /**
     * Set the HTML attribute value.
     * @param value the value of the HTML attribute.
     */
    public void setValue(String value)
    {
        _value = value;
    }

    /**
     * Return the parent node of the HtmlAttributeInfo item.
     * This is the node the HtmlAttributeInfo item is associated with.
     * @return the TreeElement parent.
     */
    public TreeElement getParent()
    {
        return _parent;
    }

    /**
     * Set the parent of the HtmlAttributeInfo item.
     * This sets the node the HtmlAttributeInfo item will be associated with.
     * @param parent the TreeElement parent.
     */
    public void setParent(TreeElement parent)
    {
        _parent = parent;
    }

    /**
     * Gets whether the attribute is applied to descendants of the node.
     * @return whether the attribute is applied to descendants of the node.
     */
    public boolean isApplyToDescendents()
    {
        return ((state & APPLY_TO_DESCENDENTS) != 0);
    }

    /**
     * Sets whether the attribute is applied to descendant nodes of the parent node.
     * @param applyToDescendents
     */
    public void setApplyToDescendents(boolean applyToDescendents)
    {
        if (applyToDescendents)
            state = state | APPLY_TO_DESCENDENTS;
        else
            state = state & (-1 ^ APPLY_TO_DESCENDENTS);

    }

    /**
     * Gets whether the HTML attribute is on the &lt;div> tag of the node.
     * @return whether the attribute is on the &lt;div> tag of the node.
     */
    public boolean isOnDiv()
    {
        return ((state & ON_DIV) != 0);
    }

    /**
     * Sets whether the HTML attribute is on the node item's &lt;div> tag.
     * @param onDiv whether the attribute is on the &lt;div> tag of the node.
     */
    public void setOnDiv(boolean onDiv)
    {
        if (onDiv)
            state = state | ON_DIV;
        else
            state = state & (-1 ^ ON_DIV);

    }

    /**
     * Gets whether the HTML attribute is on the &lt;img> tag for the icon of the node.
     * @return whether the attribute is on the &lt;img> tag of the node.
     */
    public boolean isOnIcon()
    {
        return ((state & ON_ICON) != 0);
    }

    /**
     * Sets whether the HTML attribute is on the node item's &lt;img> tag for the icon.
     * @param onIcon whether the attribute is on the &lt;img> tag of the node.
     */
    public void setOnIcon(boolean onIcon)
    {
        if (onIcon)
            state = state | ON_ICON;
        else
            state = state & (-1 ^ ON_ICON);

    }

    /**
     * Gets whether the HTML attribute is on the selection link of the node.
     * @return whether the attribute is on the selection link of the node.
     */
    public boolean isOnSelectionLink()
    {
        return ((state & ON_SELECTION_LINK) != 0);
    }

    /**
     * Sets whether the HTML attribute is on the node item's selection.
     * @param onSelectionLink whether the attribute is on the selection link of the node.
     */
    public void setOnSelectionLink(boolean onSelectionLink)
    {
        if (onSelectionLink)
            state = state | ON_SELECTION_LINK;
        else
            state = state & (-1 ^ ON_SELECTION_LINK);
    }
}
