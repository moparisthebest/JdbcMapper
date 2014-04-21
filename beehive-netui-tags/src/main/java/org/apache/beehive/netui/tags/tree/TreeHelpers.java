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

import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class provides a set of static helper methods that deal with a trees.
 */
public class TreeHelpers
{
    private static final Logger logger = Logger.getInstance(TreeHelpers.class);

    /**
     * If this tree was selected or expanded this will handle that processing.
     * @param treeId
     * @param treeRoot
     * @param request
     */
    public static void processTreeRequest(String treeId, TreeElement treeRoot, HttpServletRequest request, HttpServletResponse response)
    {
        assert(treeId != null) : "parameter treeId must not be null.";
        assert(treeRoot != null) : "parameter treeRoot must not be null.";
        assert(request != null) : "paramater request must not be null.";

        // check the root node to see if it is a TreeRootElement.  if it is, then we
        // use it to change the selected node.  Otherwise we will do a recursive search
        // of the tree to select the node.
        String selectNode = request.getParameter(TreeElement.SELECTED_NODE);
        if (selectNode != null) {
            if (treeRoot instanceof ITreeRootElement) {
                ITreeRootElement root = (ITreeRootElement) treeRoot;
                root.changeSelected(selectNode, request);
            }
            else {
                setSelected(treeRoot, selectNode, request);
            }
        }

        // handle auto expand.
        String expandNode = null;

        // our we auto expanding this node?
        expandNode = request.getParameter(TreeElement.EXPAND_NODE);

        // if we are auto expanding flip the expand of this node if it was expanded
        if (expandNode != null) {
            TreeElement n = treeRoot.findNode(expandNode);
            if (n != null) {
                n.onExpand(request, response);
                n.setExpanded(!n.isExpanded());
            }
        }
    }

    /**
     * Recursive routine to set the selected node.  This will set the selected node
     * and clear the selection on all other nodes.  It will walk the full tree.
     * @param node
     * @param selected
     */
    protected static void setSelected(TreeElement node, String selected, ServletRequest request)
    {
        assert(node != null) : "parameter 'node' must not be null";
        assert(selected != null) : "parameter 'selected' must not be null";
        assert(request != null) : "parameter 'requested' must not be null";

        if (node.getName().equals(selected)) {
            node.onSelect(request);
            node.setSelected(true);
        }
        else {
            if (node.isSelected()) {
                node.onSelect(request);
                node.setSelected(false);
            }
        }

        TreeElement children[] = node.getChildren();
        assert(children != null);
        for (int i = 0; i < children.length; i++) {
            setSelected(children[i], selected, request);
        }
    }

    /**
     * This will return the currently selected node from a tree.
     *
     * <p>Implementation Note: The method that changes the selected node based on the request,
     * {@link #processTreeRequest(String, TreeElement, HttpServletRequest, HttpServletResponse)},
     * gets called during the processing of the {@link Tree} tag within a JSP. If the
     * <code>findSelected</code> method is called from an Action in a Page Flow Controller,
     * the value of the selected node will have not yet been updated.</p>
     *
     * @param root the root element of the tree.
     * @return a TreeElement that is the currently selected node.  This may return null.
     */
    public static TreeElement findSelected(TreeElement root)
    {
        assert (root != null) : "parameter 'root' must not be null";
        if (root instanceof ITreeRootElement) {
            return ((ITreeRootElement) root).getSelectedNode();
        }
        return recursiveFindSelected(root);
    }

    /**
     * Recursive method that will find the currently selected element in the tree.
     * @param elem The current element to search.
     * @return a TreeElement that is selected.
     */
    private static TreeElement recursiveFindSelected(TreeElement elem)
    {
        assert(elem != null);

        if (elem.isSelected())
            return elem;

        TreeElement children[] = elem.getChildren();
        assert(children != null);
        for (int i = 0; i < children.length; i++) {
            TreeElement e = recursiveFindSelected(children[i]);
            if (e != null)
                return e;
        }
        return null;
    }

    /**
     * This is a helper method that will change the selected node.  This is provided to
     * make implementation of ITreeRootElement easier.  This is called by the <code>changeSelected</code>
     * method there to do the work of changing the selected node.
     * @param root         The root of the tree
     * @param selectedNode The node that is currently selected, it may be null
     * @param selectNode   The String name of the node that will be selected
     * @param request      The ServletRequest
     * @return a TreeElement representing the new node selected.
     */
    public static TreeElement changeSelected(TreeElement root, TreeElement selectedNode, String selectNode, ServletRequest request)
    {
        assert(root != null) : "parameter 'root' must not be null";
        assert(selectNode != null) : "parameter 'selectNode' must not be null";
        assert(request != null) : "parameter 'request' must not be null";

        // if there is a selectedNode then we need to raise the onSelect
        // event on that node indicating it will soon not be selected
        TreeElement n = root.findNode(selectNode);
        if (n == null) {
            logger.warn("The tree element '" + selectNode + "' was not found.  Selection failed");
            return null;
        }

        // change the node that was selected so it is no longer selected
        if (selectedNode != null) {
            selectedNode.onSelect(request);
            selectedNode.setSelected(false);
        }

        // change the node that is to be selected
        n.onSelect(request);
        n.setSelected(true);
        return n;
    }

}
