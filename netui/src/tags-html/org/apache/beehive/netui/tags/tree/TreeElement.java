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

import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.html.IUrlParams;
import org.apache.beehive.netui.util.ParamHelper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An individual node of a <code>Tree</code>, and rendered by that Tree instance.
 */
public class TreeElement implements IUrlParams, Serializable
{
    // These are the bits for the boolean state.  The boolean state is stored
    // in the _boolState variable.  All boolean values should be accessed through
    // their property accessors.
    private static final int EXPANDED = 0x01;
    private static final int SELECTED = 0x02;
    private static final int DISABLED = 0x04;
    private static final int EXPAND_ON_SERVER = 0x08;

    public static final String SELECTED_NODE = "netui_treeselected";
    public static final String EXPAND_NODE = "netui_treenode";
    public static final String TREE_ID = "netui_treeid";
    public static final String TREE_JAVASCRIPT_CLASS = "NetUITree";

    /**
     * Constant used to indicate an anchor is an expand/collapse tree node
     */
    public static final String TREE_ANCHOR = "netui:treeAnchor";
    public static final String TREE_ANCHOR_INIT = "netui:treeAnchorInit";
    public static final String TREE_ANCHOR_ID = "netui:treeId";
    public static final String TREE_EXPAND = "netui:expandOnServer";
    public static final String TREE_EXPAND_PATH = "netui:expandPath";
    public static final String TREE_NODE_LAST = "netui:expandLast";
    public static final String TREE_EXPAND_STATE = "expand";
    public static final String TREE_COLLAPSE_STATE = "collapse";
    public static final String TREE_LEVEL = "netui:treeLevel";
    public static final String TREE_AJAX_COMMAND_PREFIX = "netui:ajaxPrefix";
    public static final String TREE_AJAX_PARAMTER = "netui:ajaxParameter";

    public static final String TREE_EXPAND_IMAGE = "netui:imageExpand";
    public static final String TREE_COLLAPSE_IMAGE = "netui:imageCollapse";

    /**
     * Constant used to indicate that a tree node anchor should be set expanded.
     */
    public static final String TREE_EXPANDED = "expand";

    // this is used as the TreeElement list for leaf nodes because they don't have children
    private static final TreeElement[] EMPTY_TREE = new TreeElement[0];

    private String _content;         // The extra HTML content of the node
    private String _label;           // The label that will be displayed when this node is visible.
    private Boolean _labelAsAlt;    // flag indicating that the label should be used as alt text.
    private String _title;
    private String _name;            // The unique (within the entire tree) name of this node.
    private int _boolState;          // the boolean state of things

    private TreeElement _parent;     // The parent node of this node, null for the root
    private ArrayList _children;     // The children of this node
    public ArrayList _attribute;     // The attributes assocated with the element.

    private ExtendedInfo _info;      // The extended info

    /**
     * Default constructor for creating a simple tree.
     */
    public TreeElement()
    {
        _name = "0";
    }

    /**
     * Construct a new TreeElement with the specified parameters.
     * @param expanded Should this node be expanded?
     */
    public TreeElement(String label, boolean expanded)
    {
        this();
        _label = label;
        setExpanded(expanded);
    }

    //********************** PUBLICALLY EXPOSED PROPERTIES **********************************

    /**
     * Gets the pathname to the icon file displayed when this node is visible,
     * relative to the image directory for the images.
     * @return the icon pathname
     */
    public String getIcon()
    {
        return (_info == null) ? null : _info._icon;
    }

    /**
     * Set the pathname to the icon to display when this node is visible.  The name is relative to the
     * image directory.
     * @param icon The relative path to the icond.
     */
    public void setIcon(String icon)
    {
        ExtendedInfo info = getInfo(icon);
        if (info != null)
            info._icon = icon;
    }

    /**
     * Gets the label that will be displayed when this node is visible.
     * @return the label
     */
    public String getLabel()
    {
        return _label;
    }

    /**
     * Set the text of the label associated with this node.
     * @param label The text of the tree node.
     */
    public void setLabel(String label)
    {
        _label = label;
    }

    public boolean isLabelLegalAsAlt()
    {
        if (_labelAsAlt == null)
            _labelAsAlt = new Boolean(!HtmlUtils.containsHtml(_label));
        return _labelAsAlt.booleanValue();
    }

    /**
     * Return the content.  The content is extra HTML that will be output to the right of the
     * Nodes's label.
     * @return the content
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * Set the content of the node.  The content will be output to the right of the Nodes label.
     * @param content The text of the tree node.
     */
    public void setContent(String content)
    {
        _content = content;
    }

    /**
     * Gets the action invoked if this node
     * is selected by the user.
     * @return the action
     */
    public String getAction()
    {
        return (_info == null) ? null : _info._action;
    }

    /**
     * Set the action to be called when the node is selected.  A tree node may only have a <code>action</code>
     * or <code>href</code> or <code>clientAction</code> set but not both.
     * @param action an action in the page flow that will run when the node is selected.
     */
    public void setAction(String action)
    {
        ExtendedInfo info = getInfo(action);
        if (info != null)
            info._action = action;
    }

    /**
     * Gets the client action invoked if this node is selected by the user.
     * @return the action
     */
    public String getClientAction()
    {
        return (_info == null) ? null : _info._clientAction;
    }

    /**
     * Set the client action to be called when the node is selected.  A tree node may only have a <code>action</code>
     * or <code>href</code> or <code>clientAction</code> set but not both.
     * @param clientAction an action in the page flow that will run when the node is selected.
     */
    public void setClientAction(String clientAction)
    {
        ExtendedInfo info = getInfo(clientAction);
        if (info != null)
            info._clientAction = clientAction;
    }

    /**
     * Return the ID of the tag.  The id may be rewritten by the container (such
     * as a portal) to make sure it is unique.  JavaScript may lookup the actual id
     * of the element by looking it up in the <code>netui_names</code> table written
     * into the HTML.
     * @return the tagId.
     */
    public String getTagId()
    {
        return (_info == null) ? null : _info._tagId;
    }

    /**
     * Set the ID of the tag.
     * @param tagId the tagId.
     */
    public void setTagId(String tagId)
    {
        ExtendedInfo info = getInfo(tagId);
        if (info != null)
            info._tagId = tagId;
    }

    /**
     * Gets the hyperlink to which control will be directed if this node
     * is selected by the user.
     * @return the action
     */
    public String getHref()
    {
        return (_info == null) ? null : _info._href;
    }

    /**
     * Set the hyperlink which will be called when the node is selected.   A tree node may only have
     * a <code>action</code> or <code>href</code> or <code>clientAction</code> set but not both.
     * @param href The hyperlink called when the node is selected.
     */
    public void setHref(String href)
    {
        ExtendedInfo info = getInfo(href);
        if (info != null)
            info._href = href;
    }

    /**
     * Gets the window target for the hyperlink identified by the
     * <code>action</code> property, if this node is selected.
     * @return the window target
     */
    public String getTarget()
    {
        return (_info == null) ? null : _info._target;
    }

    /**
     * Set the window target for the hyperlink indentified by the <code>action</code>.
     * @param target the window target.
     */
    public void setTarget(String target)
    {
        ExtendedInfo info = getInfo(target);
        if (info != null)
            info._target = target;
    }

    /**
     * Gets if this node is currently expanded.
     * @return the expanded state
     */
    public boolean isExpanded()
    {
        return ((_boolState & EXPANDED) != 0);
    }

    /**
     * Sets if this node is currently expanded.
     * @param expanded the expanded state
     */
    public void setExpanded(boolean expanded)
    {
        if (expanded) {
            _boolState = _boolState | EXPANDED;
        }
        else
            _boolState = _boolState & (-1 ^ EXPANDED);
    }

    /**
     * This method will return the state of the expand on server attribute.
     * @return the boolean value of the expandOnServer attribute.
     */
    public boolean isExpandOnServer()
    {
        return ((_boolState & EXPAND_ON_SERVER) != 0);
    }

    /**
     * Set the value of the expandOnServer attribute.  If the attribute is <code>true</code>
     * and <code>runAtClient</code> is also true, then an expansion on this node will cause that
     * to happen on the server.  When runAtClient is false, all expansions will happen on the server.
     * @param expandOnServer boolean value indicating if the node should be expanded on the server.
     */
    public void setExpandOnServer(boolean expandOnServer)
    {
        if (expandOnServer)
            _boolState = _boolState | EXPAND_ON_SERVER;
        else
            _boolState = _boolState & (-1 ^ EXPAND_ON_SERVER);
    }

    /**
     * Get the target scope for this node's URI.
     * @return a String that identifies the target scope for this node's URI.
     * @see #setScope
     */
    public String getScope()
    {
        return (_info == null) ? null : _info._scope;
    }

    /**
     * Set the target scope for this anchor's URI.  Any page flow that handles the URI will be made active within the
     * given scope.  Scopes allow multiple page flows to be active within the same user session; page flows in different
     * scopes do not in general interact with each other.  This attribute is commonly used in conjunction with the
     * <code>target</code> attribute to invoke a new page flow in a separate window.
     * @param scope a String that identifies the scope in which the target page flow will be made active.
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the target scope for this anchor's URI."
     */
    public void setScope(String scope)
    {
        ExtendedInfo info = getInfo(scope);
        if (info != null)
            info._scope = scope;
    }

    /**
     * Set the title attribute for the node.
     * @return String
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Returns the current title attribute for the node.
     * @param title
     */
    public void setTitle(String title)
    {
        _title = title;
    }

    public InheritableState getInheritableState()
    {
        return (_info == null) ? null : _info._state;
    }

    public void setInheritableState(InheritableState state)
    {
        ExtendedInfo info = getInfo(state);
        if (info != null) {
            info._state = state;
        }
    }

    public void addParameter(String name, Object value, String facet)
    {
        // use this to force the creation of the _info variable.
        ExtendedInfo info = getInfo(this);
        if (info._params == null) {
            info._params = new HashMap();
        }
        ParamHelper.addParam(info._params, name, value);
    }

    public HashMap getParams()
    {
        return (_info == null) ? null : _info._params;
    }

    //***************************  READ-ONLY PROPERTIES SET BY INTERNAL METHODS **************************

    /**
     * Gets whether or not this is the last node in the set of children
     * for the parent node.
     * @return if this is the last or not
     */
    public boolean isLast()
    {
        if (_parent == null)
            return true;
        int last = _parent.size() - 1;
        assert(last >= 0);
        return (_parent.getChild(last) == this);
    }

    public int getLevel()
    {
        TreeElement t = getParent();
        int level = 0;
        while (t != null) {
            t = t.getParent();
            level++;
        }
        return level;
    }

    /**
     * Gets whether or not this a "leaf" node (i.e. one with no children)
     * @return if this is the last or not
     */
    public boolean isLeaf()
    {
        if (_children == null)
            return true;
        return (_children.size() < 1);
    }

    /**
     * Returns the unique name of the node. The unique name is set when the child is added to the tree.
     * @return The unique name of the node.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Gets the parent node of this node, or <code>null</code> if this
     * is the root node.
     * @return the TreeElement parent
     */
    public TreeElement getParent()
    {
        return _parent;
    }

    /**
     * Gets whether this node currently selected.  This is set by the tag render method.
     * @return the selected state
     */
    public boolean isSelected()
    {
        return ((_boolState & SELECTED) != 0);
    }

    /**
     * Sets whether this node currently selected.
     * @param selected the selected state
     */
    public void setSelected(boolean selected)
    {
        if (selected)
            _boolState = _boolState | SELECTED;
        else
            _boolState = _boolState & (-1 ^ SELECTED);
    }

    /**
     * Gets whether this node currently selected.  This is set by the tag render method.
     * @return the selected state
     */
    public boolean isDisabled()
    {
        return ((_boolState & DISABLED) != 0);
    }

    /**
     * Sets whether this node currently selected.
     * @param disabled the selected state
     */
    public void setDisabled(boolean disabled)
    {
        if (disabled)
            _boolState = _boolState | DISABLED;
        else
            _boolState = _boolState & (-1 ^ DISABLED);
    }

    /**
     * This method is called by the children tags of the TreeItem.  If there is a <code>TreeHtmlAttribute</code>
     * it will set the attribute name and value.
     * @param attr
     */
    public void addAttribute(TreeHtmlAttributeInfo attr)
    {
        if (_attribute == null) {
            _attribute = new ArrayList();
        }
        attr.setParent(this);
        _attribute.add(attr);
    }

    /**
     * Return the list of attributes.  This method may return null if there are
     * no elements. In addition, the map may be exist but be empty.
     * @return a ArrayList of attribute values or null.
     */
    public ArrayList getAttributeList()
    {
        return _attribute;
    }

    ///*********************** PROTECTED PROPERTY SETTERS *************************************
    /**
     * Set the unique name of the node.  Uniqueness is not verified by this routine.  This routine is protected
     * because it is only called during add and remove of children.
     * @param name the name of the node.
     */
    protected void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets the parent node of this node.  This node will set the width value (which is really the
     * depth in the tree of the node).
     * @param parent the TreeElement parent
     */
    protected void setParent(TreeElement parent)
    {
        _parent = parent;
    }

    //*************************** EVENT METHODS *****************************

    /**
     * Code that runs when the node is expanded.
     */
    public void onExpand(ServletRequest request, ServletResponse response)
    {
        //do nothing
    }

    /**
     * Code that runs when the node is selected.
     */
    public void onSelect(ServletRequest request)
    {
        //do nothing
    }

    //***************** CHILD/PARENT MANAGEMENT METHODS **************************************

    /**
     * Add a new child node to the end of the list.
     * @param child The new child node
     * @throws IllegalArgumentException if the name of the new child
     *                                  node is not unique
     */
    public void addChild(TreeElement child)
            throws IllegalArgumentException
    {

        TreeElement theChild = child;
        theChild.setParent(this);
        if (getName() == null) {
            setName("0");
        }
        if (_children == null) {
            _children = new ArrayList();
        }
        //int n = _children.size();
        //if (n > 0) {
        //    TreeElement node = (TreeElement) _children.get(n - 1);
        //    node.setLast(false);
        //}
        //theChild.setLast(true);
        _children.add(child);
        int n = _children.size();
        theChild.updateName(this, n - 1);
    }

    /**
     * This method will return the number of children of the node.
     * @return the number of children of this tree element.
     */
    public int size()
    {
        if (_children == null)
            return 0;
        return _children.size();
    }

    /**
     * Add a new child node at the specified position in the child list.
     * @param offset Zero-relative offset at which the new node
     *               should be inserted
     * @param child  The new child node
     * @throws IllegalArgumentException if the name of the new child
     *                                  node is not unique
     */
    public void addChild(int offset, TreeElement child)
            throws IllegalArgumentException
    {
        child.setSelected(false);
        child.setParent(this);
        if (_children == null)
            _children = new ArrayList();
        _children.add(offset, child);

        //Need to rename all affected!
        int size = _children.size();
        for (int i = offset; i < size; i++) {
            TreeElement thisChild = (TreeElement) _children.get(i);
            thisChild.updateName(this, i);
        }

    }

    /**
     *
     */
    public void clearChildren()
    {
        if (_children != null)
            _children.clear();
    }

    /**
     * Return the set of child nodes for this node.
     * @return the child node array
     */
    public TreeElement[] getChildren()
    {
        if (_children == null)
            return EMPTY_TREE;
        TreeElement results[] = new TreeElement[_children.size()];
        return (TreeElement[]) _children.toArray(results);
    }

    /**
     * Return the child node at the given zero-relative index.
     * @param index The child node index
     * @return the child node
     */
    public TreeElement getChild(int index)
    {
        TreeElement childNode = null;
        if (_children == null)
            return null;
        Object child = _children.get(index);
        if (child != null) {
            childNode = (TreeElement) child;
        }
        return childNode;
    }

    /**
     * Remove the child node (and all children of that child) at the
     * specified position in the child list. If there are no children
     * or the specified child position is too large, then this method
     * just returns. (I.e. no runtime exception if the offset argument
     * is too large) 
     * @param offset Zero-relative offset at which the existing
     *               node should be removed
     */
    public void removeChild(int offset)
    {
        if (_children == null  || offset >= _children.size())
            return;

        TreeElement child = (TreeElement)_children.remove(offset);
        child.setParent(null);
        child.setName(null);

        // Rename all affected children
        int size = _children.size();
        for (int i = offset; i < size; i++) {
            TreeElement thisChild = (TreeElement) _children.get(i);
            thisChild.updateName(this, i);
        }
    }

    /**
     * Remove the specified child node.  All of the
     * children of this child node will also be removed.
     * @param child Child node to be removed
     */
    public void removeChild(TreeElement child)
    {

        if (child == null || _children == null)
            return;

        int size = _children.size();
        int removeIndex = -1;
        for (int i = 0; i < size; i++) {
            if (child == (TreeElement) _children.get(i)) {
                _children.remove(i);
                child.setParent(null);
                removeIndex = i;
                break;
            }
        }
        if (removeIndex >= 0) {
            size = _children.size();
            for (int i = removeIndex; i < size; i++) {
                TreeElement thisChild = (TreeElement) _children.get(i);
                thisChild.updateName(this, i);
            }
        }
    }

    /**
     * This method will update the name of this node and all of the children node.  The name
     * of a node reflects it's position in the tree.
     * @param parentNode The parent node of this node.
     * @param index      the index position of this node within the parent node.
     */
    protected void updateName(TreeElement parentNode, int index)
    {
        setName(getNodeName(parentNode, index));
        TreeElement[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].updateName(this, i);
        }
    }

    //************  TREE BASED MANAGEMENT METHODS **************************************
    /**
     * Gets the root node of this tree.
     * @param node The TreeElement to start from
     * @return The root node
     */
    public static TreeElement getRoot(TreeElement node)
    {
        TreeElement parentNode = node.getParent();
        while (parentNode != null) {
            node = parentNode;
            parentNode = node.getParent();
        }
        return node;
    }

    /**
     * Given a node, find the named child.
     * @param nodeName the name of the child to find.
     * @return the node identified by the name
     */
    public TreeElement findNode(String nodeName)
    {
        TreeElement root = getRoot(this);
        return root.findNodeRecurse(nodeName, nodeName);
    }

    /**
     * @param parentNode
     * @param index
     * @return the name of the node, reflecting it's position in the tree
     */
    private String getNodeName(TreeElement parentNode, int index)
    {
        String nodeName = "" + index;
        if (parentNode != null) {
            nodeName = parentNode.getName() + "." + nodeName;
        }

        return nodeName;
    }

    /**
     * Helper routine that will recursively search the tree for the node.
     * @param fullName    The full name that we are looking for
     * @param currentName The name of the current node.
     * @return The node matching the name or <code>null</code>
     */
    private TreeElement findNodeRecurse(String fullName, String currentName)
    {
        String remainingName = null;

        if ((currentName == null) || (fullName == null)) {
            return null;
        }
        if (getName().equals(fullName)) {
            return this;
        }
        if (currentName.indexOf('.') > 0) {
            remainingName = currentName.substring(currentName.indexOf('.') + 1);
            int nextIndex = -1;
            if (remainingName.indexOf(".") > -1) {
                nextIndex = new Integer(remainingName.substring(0, remainingName.indexOf('.'))).intValue();
            }
            else {
                nextIndex = new Integer(remainingName).intValue();
            }

            TreeElement child = getChild(nextIndex);
            if (child != null) {
                return child.findNodeRecurse(fullName, remainingName);
            }
            else {
                return null;
            }
        }
        return null;
    }

    /**
     * This will get the ExtendedInfo object.  If it hasn't been created, this routine only
     * creates it if the object passed in is non-null.
     * @param o
     * @return
     */
    private ExtendedInfo getInfo(Object o)
    {
        if (_info == null && o != null) {
            _info = new ExtendedInfo();
        }
        return _info;
    }

    /**
     * This class contains the attributes which infrequently used.  This is an optimiation
     * where the tree only contains the things necessary to render the base tree.  Less commmonly used
     * features are not exposed by this structure.
     */
    private static class ExtendedInfo implements Serializable
    {
        public String _clientAction;    // Action to run on the client.
        public String _href;            // href to call if the node is selected
        public String _scope;           // The Scope
        public String _icon;            // The pathname to the icon file displayed when this node is visible
        public String _tagId;           // The tagId of the node
        public String _target;          // The window target for the hyperlink identified
        public String _action;          // The action invoked if this node is selected by the user.
        public HashMap _params;         // params to be applied to the selection action
        public InheritableState _state; // The inheritable state from the tree
    }
}
