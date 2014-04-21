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

import org.apache.beehive.netui.pageflow.requeststate.INameable;

import javax.servlet.ServletRequest;

/**
 * This interface provides additional behavior on the root node in a tree.  There
 * are certain optimizations that may be done if state is tracked in the root.
 * In addition, there are a number of features that are only applied to the root
 * such as images.  Finally, when the tree is run on the client, there are
 * additonal features required of the root.
 *
 * This is a pretty simple interface to implement.  Mostly the there are only
 * properties that are stored for the additional state.  The only method that
 * is complicated is the <code>changeSelected</code> method.
 */
public interface ITreeRootElement extends INameable
{
    /**
     * Change the node that is selected.  This is an optimization were the
     * root node can track which node is currently selected so it can unselect
     * that node instead of searching the whole tree to find the selected node.
     *
     * There is a helper method <code>TreeHelpers.changeSelected</code> that can
     * be used for delegation.  This requires the root, the currently selected node,
     * and will return the newly selected node.
     * @param selectNode a String value name of the new node selected
     * @param request    the ServletRequest.
     */
    void changeSelected(String selectNode, ServletRequest request);

    /**
     * Return the currently selected <code>TreeElement</code>.  This method
     * will return null if no element is currently selected.
     * @return the currently selected node.
     */
    TreeElement getSelectedNode();

    /**
     * return the TreeRenderState for this tree.
     * @return TreeRenderState
     */
    TreeRenderState getTreeRenderState();

    /**
     * Set the TreeRenderState
     * @param trs
     */
    void setTreeRenderState(TreeRenderState trs);

    /**
     * Property that returns the InheritableState that was set on the Tree.
     * @return InheritableState
     */
    InheritableState getInheritableState();

    /**
     * Property that sets the InheritableState that is set on the Tree tag.
     * @param state
     */
    void setInheritableState(InheritableState state);

    /**
     * @return String
     */
    String getRootNodeExpandedImage();

    /**
     * @param rootNodeExpandedImage
     */
    void setRootNodeExpandedImage(String rootNodeExpandedImage);

    /**
     * @return String
     */
    String getRootNodeCollapsedImage();

    /**
     * @param rootNodeCollapsedImage
     */
    void setRootNodeCollapsedImage(String rootNodeCollapsedImage);
}
