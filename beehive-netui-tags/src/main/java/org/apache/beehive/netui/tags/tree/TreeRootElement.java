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

import javax.servlet.ServletRequest;

/**
 * This is a concrete implementation of <code>ITreeRootElement</code>.  It is a subclass of the
 * <code>TreeElement</code> and is created for the root element when a tree is defined
 * through JSP tags.
 */
public class TreeRootElement extends TreeElement implements ITreeRootElement
{
    private TreeElement _selectedNode;      // The currently selected node
    private TreeRenderState _trs;           // The tree render state used by TreeRenderer
    private InheritableState _state;        // The tree's root inheritableState
    private String _name = null;            // The name of the tree
    private String _rootNodeExpandedImage;  // The image used when the root is expanded.
    private String _rootNodeCollapsedImage; // The image used when the root is collapsed


    /**
     * Default constructor for creating a simple tree.
     */
    public TreeRootElement()
    {
        super();
    }

    /**
     * Construct a new TreeElement with the specified parameters.
     * @param expanded Should this node be expanded?
     */
    public TreeRootElement(String label, boolean expanded)
    {
        super(label, expanded);
    }

    /**
     * Change the node that is selected.  This is an optimization were the
     * root node can track which node is currently selected so it can unselect
     * that node instead of searching the whole tree to find the selected node.
     * @param selectNode
     */
    public void changeSelected(String selectNode, ServletRequest request)
    {
        _selectedNode = TreeHelpers.changeSelected(this, _selectedNode, selectNode, request);
    }

    /**
     * Return the currently selected <code>TreeElement</code>.  This method
     * will return null if no element is currently selected.
     *
     * <p>Implementation Note: The method that changes the selected node based on the request,
     * {@link TreeHelpers#processTreeRequest(String, TreeElement, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)},
     * gets called during the processing of the {@link Tree} tag within a JSP. If the
     * <code>getSelectedNode</code> method is called from an Action in a Page Flow Controller,
     * the value of the selected node will have not yet been updated.</p>
     *
     * @return the currently selected node.
     */
    public TreeElement getSelectedNode()
    {
        return _selectedNode;
    }

    /**
     * return the TreeRenderState for this tree.
     * @return the <code>TreeRenderState</code>
     */
    public TreeRenderState getTreeRenderState()
    {
        return _trs;
    }

    /**
     * Set the TreeRenderState
     * @param trs
     */
    public void setTreeRenderState(TreeRenderState trs)
    {
        _trs = trs;
    }

    /**
     * Property that returns the InheritableState that was set on the Tree.
     * @return InheritableState
     */
    public InheritableState getInheritableState()
    {
        return _state;
    }

    /**
     * Property that sets the InheritableState that is set on the Tree tag.
     * @param state
     */
    public void setInheritableState(InheritableState state)
    {
        _state = state;
    }

    /**
     * Returns the expanded image for the root node.
     * @return the expanded image for the root.
     */
    public String getRootNodeExpandedImage()
    {
        return _rootNodeExpandedImage;
    }

    /**
     * Sets the expanded image for the root node.
     * @param rootNodeExpandedImage the name of the image to display.  This will be searched
     *                              for below the image root.
     */
    public void setRootNodeExpandedImage(String rootNodeExpandedImage)
    {
        _rootNodeExpandedImage = rootNodeExpandedImage;
    }

    /**
     * Returns the collapsed image for the root node.
     * @return the name of the collapsed image for the root.
     */
    public String getRootNodeCollapsedImage()
    {
        return _rootNodeCollapsedImage;
    }

    /**
     * Sets the name of the collapsed image for the root node.
     * @param rootNodeCollapsedImage the name of the collapsed image to display.  This will be searched
     *                               for below the image root.
     */
    public void setRootNodeCollapsedImage(String rootNodeCollapsedImage)
    {
        _rootNodeCollapsedImage = rootNodeCollapsedImage;
    }


    /**
     * Set the ObjectName of the INameable object.  This should only
     * be set once.  If it is called a second time an IllegalStateException
     * should be thrown
     * @param name the Object's name.
     * @throws IllegalStateException if this method is called more than once for an object
     */
    public void setObjectName(String name)
    {
        if (_name != null) {
            throw new IllegalStateException("Attempt to set the ObjectName twice");
        }
        _name = name;
    }

    /**
     * Returns the ObjectName of the INameable object.
     * @return the ObjectName.
     */
    public String getObjectName()
    {
        return _name;
    }
}
