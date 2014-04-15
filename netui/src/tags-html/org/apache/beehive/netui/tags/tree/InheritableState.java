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
 * This is a set of tree properties that are used to render the tree markup.
 */
public class InheritableState implements java.io.Serializable
{
    /**
     * The default directory name for icon images.
     */
    public static final String DEFAULT_IMAGES = "resources/beehive/version1/images";

    /**
     * The names of tree state images that we need.
     */
    private static final String IMAGE_NODE_EXPAND_LAST = "lastNodeExpanded.gif";
    private static final String IMAGE_NODE_EXPAND = "nodeExpanded.gif";
    private static final String IMAGE_NODE_COLLAPSE_LAST = "lastNodeCollapsed.gif";
    private static final String IMAGE_NODE_COLLAPSE = "nodeCollapsed.gif";
    private static final String IMAGE_LINE_JOIN_LAST = "lastLineJoin.gif";
    private static final String IMAGE_LINE_JOIN = "lineJoin.gif";
    private static final String IMAGE_LINE_VERTICAL = "verticalLine.gif";
    private static final String IMAGE_SPACER = "spacer.gif";
    private static final String DEFAULT_ICON = "folder.gif";

    // These are accessed by the Tree tag
    static final String IMAGE_ROOT_EXPANDED = "rootExpanded.gif";
    static final String IMAGE_ROOT_COLLAPSED = "rootCollapsed.gif";


    // The private state of the state
    private String _lastNodeExpandedImage = null;
    private String _nodeExpandedImage = null;
    private String _lastNodeCollapsedImage = null;
    private String _nodeCollapsedImage = null;
    private String _lastLineJoinImage = null;
    private String _lineJoinImage = null;
    private String _verticalLineImage = null;
    private String _imageSpacer = null;
    private String _defaultIcon = null;
    private String _selectionAction = null;
    private String _expansionAction = null;
    private String _selectTarget = null;
    private String _imageRoot = null;
    private String _iconRoot = null;

    // A parent state object which we chain to if the state isn't set on this object.
    private InheritableState _parent;


    public void setParent(InheritableState parent)
    {
        _parent = parent;
    }

    public InheritableState getParent()
    {
        return _parent;
    }

    public String getLastNodeExpandedImage()
    {
        if (_lastNodeExpandedImage != null)
            return _lastNodeExpandedImage;
        if (_parent != null)
            return _parent.getLastNodeExpandedImage();
        return null;
    }

    public void setLastNodeExpandedImage(String lastNodeExpandedImage)
    {
        _lastNodeExpandedImage = lastNodeExpandedImage;
    }

    public String getNodeExpandedImage()
    {
        if (_nodeExpandedImage != null)
            return _nodeExpandedImage;
        if (_parent != null)
            return _parent.getNodeExpandedImage();
        return null;
    }

    public void setNodeExpandedImage(String nodeExpandedImage)
    {
        _nodeExpandedImage = nodeExpandedImage;
    }

    public String getLastNodeCollapsedImage()
    {
        if (_lastNodeCollapsedImage != null)
            return _lastNodeCollapsedImage;
        if (_parent != null)
            return _parent.getLastNodeCollapsedImage();
        return null;
    }

    public void setLastNodeCollapsedImage(String lastNodeCollapsedImage)
    {
        _lastNodeCollapsedImage = lastNodeCollapsedImage;
    }

    public String getNodeCollapsedImage()
    {
        if (_nodeCollapsedImage != null)
            return _nodeCollapsedImage;
        if (_parent != null)
            return _parent.getNodeCollapsedImage();
        return null;
    }

    public void setNodeCollapsedImage(String nodeCollapsedImage)
    {
        _nodeCollapsedImage = nodeCollapsedImage;
    }

    public String getLastLineJoinImage()
    {
        if (_lastLineJoinImage != null)
            return _lastLineJoinImage;
        if (_parent != null)
            return _parent.getLastLineJoinImage();
        return null;
    }

    public void setLastLineJoinImage(String imageLineLast)
    {
        _lastLineJoinImage = imageLineLast;
    }

    public String getLineJoinImage()
    {
        if (_lineJoinImage != null)
            return _lineJoinImage;
        if (_parent != null)
            return _parent.getLineJoinImage();
        return null;
    }

    public void setLineJoinImage(String imageLineMiddle)
    {
        _lineJoinImage = imageLineMiddle;
    }

    public String getVerticalLineImage()
    {
        if (_verticalLineImage != null)
            return _verticalLineImage;
        if (_parent != null)
            return _parent.getVerticalLineImage();
        return null;
    }

    public void setVerticalLineImage(String imageLineVertical)
    {
        _verticalLineImage = imageLineVertical;
    }

    public String getImageSpacer()
    {
        if (_imageSpacer != null)
            return _imageSpacer;
        if (_parent != null)
            return _parent.getImageSpacer();
        return null;
    }

    public void setImageSpacer(String imageSpacer)
    {
        _imageSpacer = imageSpacer;
    }

    public String getItemIcon()
    {
        if (_defaultIcon != null)
            return _defaultIcon;
        if (_parent != null)
            return _parent.getItemIcon();
        return null;
    }

    public void setItemIcon(String itemIcon)
    {
        _defaultIcon = itemIcon;
    }

    public String getSelectionAction()
    {
        if (_selectionAction != null)
            return _selectionAction;
        if (_parent != null)
            return _parent.getSelectionAction();
        return null;
    }

    public void setSelectionAction(String action)
    {
        _selectionAction = action;
    }

    public String getExpansionAction()
    {
        if (_expansionAction != null)
            return _expansionAction;
        if (_parent != null)
            return _parent.getExpansionAction();
        return null;
    }

    public void setExpansionAction(String action)
    {
        _expansionAction = action;
    }

    public String getSelectionTarget()
    {
        if (_selectTarget != null)
            return _selectTarget;
        if (_parent != null)
            return _parent.getSelectionTarget();
        return null;
    }

    public void setSelectionTarget(String target)
    {
        _selectTarget = target;
    }

    /**
     * Return the default location of all images.  It is used as the location
     * for the tree structure images.
     * @return String
     */
    public String getImageRoot()
    {
        if (_imageRoot != null)
            return _imageRoot;
        if (_parent != null)
            return _parent.getImageRoot();
        return null;
    }

    /**
     * Sets the default location of all the images.
     * @param imageRoot
     */
    public void setImageRoot(String imageRoot)
    {
        _imageRoot = imageRoot;
    }

    /**
     * This will return the location of the icon images.  When the location
     * is explicitly set, this works exactly the same as all other inheritable
     * properties.  When this is not set, it will return the <code>getImageRoot</code>
     * location.
     * @return a String value of the icon root.
     */
    public String getIconRoot()
    {
        String ret = getRealIconRoot();
        return (ret != null) ? ret : getImageRoot();
    }

    /**
     * This method will walk the inheritable state looking for an icon root.
     * It returns null if not found.
     * @return the icon root or null.
     */
    private String getRealIconRoot()
    {
        if (_iconRoot != null)
            return _iconRoot;
        if (_parent != null)
            return _parent.getRealIconRoot();
        return null;
    }

    /**
     * This will set the location of the icon images.
     * @param iconRoot the location of the icon images.
     */
    public void setIconRoot(String iconRoot)
    {
        _iconRoot = iconRoot;
    }

    /**
     * This method initalizes the state of the properties to their default values used by the
     * tree tag to create the tree markup.
     */
    public void initalizeTreeState()
    {
        _lastNodeExpandedImage = IMAGE_NODE_EXPAND_LAST;
        _nodeExpandedImage = IMAGE_NODE_EXPAND;
        _lastNodeCollapsedImage = IMAGE_NODE_COLLAPSE_LAST;
        _nodeCollapsedImage = IMAGE_NODE_COLLAPSE;
        _lastLineJoinImage = IMAGE_LINE_JOIN_LAST;
        _lineJoinImage = IMAGE_LINE_JOIN;
        _verticalLineImage = IMAGE_LINE_VERTICAL;
        _imageSpacer = IMAGE_SPACER;
        _defaultIcon = DEFAULT_ICON;
        _imageRoot = null;

        _selectionAction = null;
        _expansionAction = null;
    }
}
