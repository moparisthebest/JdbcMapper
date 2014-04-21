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
import org.apache.beehive.netui.tags.html.IUrlParams;
import org.apache.beehive.netui.tags.internal.PageFlowTagUtils;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Instantiates a TreeElement object that will get added to the parent tag (either a Tree or
 * another TreeItem).
 * @jsptagref.tagdescription Adds a tree node to the parent node 
 * (either a {@link Tree} or another &lt;netui:treeItem>). 
 * @example In this first sample, a TreeItem contained by a parent TreeItem or Tree will 
 * display with
 * the label of "Login" and, when clicked, will navigate to the Login pageflow.
 * <pre>    &lt;netui:treeItem action="/netui/login/Login.jpf">Login&lt;/netui:treeItem></pre>
 * <p>In this next sample, a TreeItem contained by a parent TreeItem or Tree will display with
 * the label of "Human Resources" and icon "folder16.gif" and most likely will have
 * child TreeItem tags because it does not have an action and starts expanded.</p>
 * <pre>    &lt;netui:treeItem icon="folder16.gif" expanded="true">
 *        &lt;netui:treeLabel>Human Resources&lt;/netui:treeLabel>
 *        ...
 *    &lt;/netui:treeItem></pre>
 * @netui:tag name="treeItem" body-content="scriptless" description="Instantiates a TreeElement object that will get added to the parent tag (either a Tree or another TreeItem)."
 * @see Tree
 * @see org.apache.beehive.netui.tags.tree.TreeElement
 */
public class TreeItem extends AbstractSimpleTag implements IUrlParams
{
    private String _action;           // The action raised when the TreeItem is selected
    private String _clientAction;     // A client action that may run instead of the action.
    private boolean _expanded;        // boolean to indicate if the tree is expanded
    private boolean _expandOnServer;  // boolean to indicate if the tree should be expanded on the server
    private boolean _disabled;        // boolean to indicate if the tree should be expanded on the server
    private String _href;             // href link called when the TreeItem is selected
    private String _scope;            // the scope so that we support multiple page flows
    private String _icon;             // The icon representing the TreeItem
    private TreeElement _treeElement; // pointer to the tree node
    private String _target;           // The target window for the selection event
    private String _title;            // The title (if there is one)
    private String _tagId;            // tag that should uniquely indentify a tree.  Required for multiple tree using auto expand
    private ArrayList _attributes;    // array list of attributes
    private InheritableState _state;  // the state

    private boolean _child = false;  // has a child set the content
    private String _childLabel;      // value set by a TreeLabel
    private String _childContent;    // value set by a TreeItemContent tag


    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "TreeItem";
    }

    //*********************************** TAG PROPERTIES ***************************************************************

    /**
     * Set the TreeItem's action.
     * @param action the action.
     * @jsptagref.attributedescription The action to invoke when this tree node is clicked.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the TreeItem's action."
     */
    public void setAction(String action)
            throws JspException
    {
        _action = setRequiredValueAttribute(action, "action");
    }

    /**
     * Sets an action to run on the client when the tree is selected.
     * @param action the client action.
     * @jsptagref.attributedescription Sets an action to run on the client when the tree node is selected.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets an action to run on the client when the tree is selected."
     */
    public void setClientAction(String action)
            throws JspException
    {
        _clientAction = setRequiredValueAttribute(action, "clientAction");
    }

    /**
     * Set the ID of the tag.
     * @param tagId the tagId.
     * @jsptagref.attributedescription Sets of the id attribute of the tree node
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the ID of the tag."
     */
    public void setTagId(String tagId)
            throws JspException
    {
        _tagId = setRequiredValueAttribute(tagId, "tagId");
    }

    /**
     * Sets the TreeItem expansion state.
     * @param expanded the expanded state.
     * @jsptagref.attributedescription Sets the expanded/collapsed state of the tree node.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_or_booleanExpression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the TreeItem expansion state."
     */
    public void setExpanded(boolean expanded)
    {
        _expanded = expanded;
    }

    /**
     * Disables the TreeItem.
     * @param disabled the enabled/disabled state.
     * @jsptagref.attributedescription Boolean. Determines if the tree node is enabled or disabled.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_or_booleanExpression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the TreeItem expansion state."
     */
    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /**
     * Set the value of the expandOnServer attribute.  If the attribute is <code>true</code>
     * and <code>runAtClient</code> is also true, then an expansion on this node will cause that
     * to happen on the server.  When runAtClient is false, all expansions will happen on the server.
     * @jsptagref.attributedescription If this attribute is <code>true</code>
     * and <code>runAtClient</code> is also true, then an expansion on this node will cause that
     * to happen on the server.  When runAtClient is false, all expansions will happen on the server.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_or_booleanExpression</i>
     * @param expandOnServer boolean value indicating if the node should be expanded on the server.
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="If running on the client, cause the node to be expanded and contracted on the server."
     */
    public void setExpandOnServer(boolean expandOnServer)
    {
        _expandOnServer = expandOnServer;
    }

    /**
     * Sets the TreeItem's href.
     * @param href the href
     * @jsptagref.attributedescription The href attribute of the node's link.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="sets the TreeItem's href."
     */
    public void setHref(String href)
            throws JspException
    {
        _href = setRequiredValueAttribute(href, "href");
    }

    /**
     * Sets the TreeItems title.
     * @param title
     * @jsptagref.attributedescription The node's title
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the TreeItems title."
     */
    public void setTitle(String title)
    {
        _title = setNonEmptyValueAttribute(title);
    }

    /**
     * Set the target scope for this tree node's URI.  Any page flow that handles the URI will be made active within the
     * given scope.  Scopes allow multiple page flows to be active within the same user session; page flows in different
     * scopes do not in general interact with each other.  This attribute is commonly used in conjunction with the
     * <code>target</code> attribute ({@link #setTarget}) to invoke a new page flow in a separate window.
     * @param scope a String that identifies the scope in which the target page flow will be made active.
     * @jsptagref.attributedescription Set the target scope for this tree node's URI.  
     * Any page flow that handles the URI will be made active within the given scope.  
     * Scopes allow multiple page flows to be active within the same user session; page flows 
     * in different scopes do not in general interact with each other.  This attribute is 
     * commonly used in conjunction with the <code>target</code> attribute
     * to invoke a new page flow in a separate window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the target scope for this tree node's URI."
     */
    public void setScope(String scope)
    {
        _scope = setNonEmptyValueAttribute(scope);
    }

    /**
     * Sets the TreeItem icon URI.
     * @param icon the icon URI
     * @jsptagref.attributedescription The icon URI.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the icon representing this tree item."
     */
    public void setIcon(String icon)
    {
        _icon = setNonEmptyValueAttribute(icon);
    }

    //**********************************  METHODS CALLED BY CHILD TAGS TO SET STATE **********************************

    /**
     * This method is called by children tags of the TreeItem.  If there is a <code>TreeLabel</code>
     * it will set the text of the label.  A child <code>TreeLabel</code> will override the <code>label</code>
     * attribute.
     * @param label The text of the Label to be displayed for this TreeItem.
     */
    public void setItemLabel(String label)
    {
        _child = true;
        _childLabel = label;
    }

    /**
     * @param content
     */
    public void setItemContent(String content)
    {
        _child = true;
        _childContent = content;
    }

    /**
     * This method is called by the children tags of the TreeItem.  If there is a <code>TreeHtmlAttribute</code>
     * it will set the attribute name and value.
     * @param attr
     */
    public void setItemAttribute(TreeHtmlAttributeInfo attr)
    {
        _child = true;
        if (_attributes == null)
            _attributes = new ArrayList();
        _attributes.add(attr);
    }

    public void setItemInheritableState(InheritableState state)
    {
        _child = true;
        _state = state;
    }

    public void setAddedChild()
    {
        _child = true;
    }

    /**
     * @return TreeElement
     */
    public TreeElement getNode()
    {
        return _treeElement;
    }

    /**
     * Sets the window target.
     * @param target the window target.
     * @jsptagref.attributedescription Sets the window target of the node's link.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the window target."
     */
    public void setTarget(String target)
    {
        _target = target;
    }


    public void addParameter(String name, Object value, String facet) throws JspException
    {
        _treeElement.addParameter(name, value, facet);
    }


    //************************************* TAG METHODS ****************************************************************

    /**
     * Instantiate a new TreeElement.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException, IOException
    {
        // It is only legal to set either an action or an href, this will verify we have exactly one.
        // report an error when there is more than one specified.
        int have = 0;
        if (_href != null) have++;
        if (_action != null) have++;
        if (have > 1) {
            String s = Bundle.getString("Tags_Node_InvalidNode", new Object[]{"href, action"});
            registerTagError(s, null);
        }

        // If we have specified an action, then we need to verify that the action is a valid action
        PageContext pageContext = getPageContext();
        if (_action != null) {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            boolean isAction = PageFlowTagUtils.isAction(request, _action);
            if (!isAction) {
                registerTagError(Bundle.getString("Tags_BadAction", _action), null);
            }
        }

        // create the tree node
        JspTag parentTag = getParent();
        if (parentTag instanceof Tree) {
            _treeElement = new TreeRootElement();
        }
        else {
            _treeElement = new TreeElement();
        }

        // the the values
        _treeElement.setIcon(_icon);
        _treeElement.setClientAction(_clientAction);
        _treeElement.setScope(_scope);
        _treeElement.setTitle(_title);
        _treeElement.setTagId(_tagId);
        _treeElement.setExpandOnServer(_expandOnServer);
        _treeElement.setExpanded(_expanded);
        _treeElement.setDisabled(_disabled);
        _treeElement.setTarget(_target);
        _treeElement.setHref(_href);
        _treeElement.setAction(_action);

        boolean inlineError = false;
        if (hasErrors()) {
            String s = getInlineError();
            if (s == null)
                s = Bundle.getString("Tags_TreeItemErrorNoInline");
            else
                inlineError = true;
            _treeElement.setContent(s);
        }

        // insert the node into the parent node or into the tree itself.
        if (parentTag instanceof Tree) {
            Tree parentTree = (Tree) parentTag;
            if (parentTree.getRootNode() != null) {
                String s = Bundle.getString("Tags_TreeMultipleRootNodes");
                parentTree.registerTagError(s, null);
                //reportErrors();
                return;
            }
            parentTree.setRootNode(_treeElement);
        }
        else if (parentTag instanceof TreeItem) {
            TreeItem parentNode = (TreeItem) parentTag;
            parentNode.getNode().addChild(_treeElement);
            parentNode.setAddedChild();
        }
        else {
            // The parent is neither a Tree or a TreeItem so we must report an error
            String s = Bundle.getString("Tags_InvalidNodeParent");
            registerTagError(s, null);
            reportErrors();
            return;
        }

        String content = getBufferBody(true);
        if (!_child) {
            if (content != null)
                _childLabel = content;
        }

        // if there was a childLabel set then we need to update the label value for the tag.
        if (_childLabel != null) {
            _treeElement.setLabel(_childLabel);
        }

        if (hasErrors()) {
            if (!inlineError)
                reportErrors();
            return;
        }

        // if there was content defined for the node then we will set it on the node...
        if (_childContent != null) {
            _treeElement.setContent(_childContent);
        }

        // if there was inherited state set it now...
        if (_state != null) {
            _treeElement.setInheritableState(_state);
        }

        // if there is attribute then we should insert that into the TreeElement
        if (_attributes != null && _attributes.size() > 0) {
            Iterator it = _attributes.iterator();
            while (it.hasNext()) {
                TreeHtmlAttributeInfo jsi = (TreeHtmlAttributeInfo) it.next();
                _treeElement.addAttribute(jsi);
            }
        }
    }
}
