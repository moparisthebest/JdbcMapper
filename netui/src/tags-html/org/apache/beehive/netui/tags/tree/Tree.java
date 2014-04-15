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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.pageflow.requeststate.INameable;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.script.ExpressionUpdateException;
import org.apache.beehive.netui.script.IllegalExpressionException;
import org.apache.beehive.netui.tags.*;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.DivTag;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.core.urls.AjaxUrlInfo;
import org.apache.beehive.netui.core.urls.URLRewriterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;


/**
 * Netui tag that renders a tree control represented by a set of
 * <code>TreeElement</code> objects.
 * @jsptagref.tagdescription Renders a navigable tree of {@link TreeElement}
 * objects.
 *
 * <p>The tree is bound to a variable through the <code>dataSource</code> attribute.
 * If the bound variable has been initialized, then the body content of this tag is not
 * processed and the dataSource's version of the tree is rendered. If the bound variable
 * has not been initialized, we will process the body, create the <code>TreeElement</code>
 * tree structure, and set the bound variable with the newly created tree.
 *
 * A &lt;netui:tree> tag cannot be nested within another &lt;netui:tree> element.
 *
 * <p>This tag can automatically handle display icons for the tree nodes
 * through the <code>imageRoot</code> attribute.  If you point the
 * <code>imageRoot</code> attribute at a folder containing appropriately
 * named image files, the correct images will be used for any given state
 * of the tree's nodes.  The default image names are:
 *
 * <blockquote>
 * <ul>
 * <li>folder.gif</li>
 * <li>lastLineJoin.gif</li>
 * <li>lastNodeCollapsed.gif</li>
 * <li>lastNodeExpanded.gif</li>
 * <li>lineJoin.gif</li>
 * <li>nodeCollapsed.gif</li>
 * <li>nodeExpanded.gif</li>
 * <li>rootCollapsed.gif</li>
 * <li>rootExpanded.gif</li>
 * <li>spacer.gif</li>
 * <li>verticalLine.gif</li>
 * </ul>
 * </blockquote>
 *
 * @example The following example shows a &lt;netui:tree> tag with a set of
 * children &lt;netui:treeLabel> and &lt;netui:treeItem> tags that form the
 * tree's navigational structure.  The <code>dataSource</code> attribute
 * identifies the {@link TreeElement} in the appropriate binding context; in this case,
 * the page flow context is used.  The <code>selectionAction</code> attribute
 * is required so there can be a postback to the tree.  <code>tagId</code> is
 * used to uniquely identify a tree on the page.
 *
 * <pre>  &lt;netui:tree
 *    dataSource="pageFlow.myTree"
 *    selectionAction="postback"
 *    tagId="myTree">
 *      &lt;netui:treeItem expanded="true" >
 *          &lt;netui:treeLabel>Root Folder&lt;/netui:treeLabel>
 *          &lt;netui:treeItem expanded="false">
 *              &lt;netui:treeLabel>I&lt;/netui:treeLabel>
 *              &lt;netui:treeItem expanded="false">
 *                  &lt;netui:treeLabel>A&lt;/netui:treeLabel>
 *                  &lt;netui:treeItem>1&lt;/netui:treeItem>
 *                  &lt;netui:treeItem>2&lt;/netui:treeItem>
 *              &lt;/netui:treeItem>
 *          &lt;/netui:treeItem>
 *      &lt;/netui:treeItem>
 *  &lt;/netui:tree></pre>
 * @netui:tag name="tree" body-content="scriptless" description="Renders a tree control represented by a set of TreeElement objects."
 */
public class Tree extends AbstractSimpleTag implements HtmlConstants,
        IAttributeConsumer, IErrorCollector
{
    /**
     * The name of the directory containing the images for our icons,
     * relative to the page including this tag.
     */
    private TreeElement _rootNode = null;

    private TreeRenderState _trs;

    // The new style and class attributes
    private String _treeStyle;           // tree style
    private String _treeStyleClass;           // tree class

    private InternalStringBuilder _errorText;           // Text of any error that may have occurred.

    private String _dataSource = null;       // The name of the tree.
    private boolean _renderTagIdLookup = false; // cause the base javascript support to be output

    private InheritableState _iState = new InheritableState();
    private String _rootNodeCollapsedImage;
    private String _rootNodeExpandedImage;

    // These are used in a stateless manner.  They do not have to be cleared
    // in the local release because each state is cleared before it is set to write a start tag.
    private DivTag.State _divState = new DivTag.State();

    private ExpressionHandling _expr;

    public Tree()
    {
        _trs = new TreeRenderState();
        _iState.initalizeTreeState();
    }

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Tree";
    }

    /**
     * Sets the action used for expanding and contracting tree nodes.  This action will be
     * inherited by the <code>TreeElements</code> inside the tree.
     * @param action the action
     * @jsptagref.attributedescription Sets the action used for expanding and contracting tree nodes.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the action used for expanding and contracting tree nodes."
     */
    public void setExpansionAction(String action)
            throws JspException
    {
        _iState.setExpansionAction(setRequiredValueAttribute(action, "expansionAction"));
    }

    /**
     * Sets the action used for selecting tree nodes.  This action will be
     * inherited by the <code>TreeElements</code> inside the tree.
     * @param action the action
     * @jsptagref.attributedescription Sets the action used when a tree node is selected.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="Sets the action used when a tree node is selected."
     */
    public void setSelectionAction(String action)
            throws JspException
    {
        _iState.setSelectionAction(setRequiredValueAttribute(action, "selectionAction"));
    }

    /**
     * Return the action for selection action on the tree.  This action will be called
     * when a TreeElement is selected.
     * @return the selection action name.
     */
    public String getSelectionAction()
    {
        return _iState.getSelectionAction();
    }

    /**
     * Sets the target for a selection action.  This can specify the name
     * of the frame where the document is to be opened.
     * @param target the target for selection
     * @jsptagref.attributedescription Sets the frame target used for selecting tree nodes.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_target</i>
     * @netui:attribute rtexprvalue="true"
     * description="Sets the frame target used for selecting tree nodes."
     */
    public void setSelectionTarget(String target)
    {
        _iState.setSelectionTarget(setNonEmptyValueAttribute(target));
    }


    /**
     * Set the ID of the tag.  This is required and will not generate the
     * standard JavaScript lookup code unless <code>RenderTagIdLookup</code>
     * is set to <code>true</code>.
     * @param tagId the tagId.
     * @jsptagref.attributedescription Set the id of the tree.  This appears on the containing &lt;div>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="Set the id of the tree.  This appears on the containing &lt;div>."
     */
    public void setTagId(String tagId)
            throws JspException
    {
        _trs.tagId = setRequiredValueAttribute(tagId, "tagId");
    }

    /**
     * Sets the image name for an open non-leaf node with no
     * line below it.  (Defaults to "lastNodeExpanded.gif").
     * @param lastNodeExpandedImage the image name (including extension)
     * @jsptagref.attributedescription The image name for an open non-leaf node with no line below it. (Defaults to "lastNodeExpanded.gif".)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageHandleDownLast</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the image name for an open non-leaf node with no
     * line below it."
     */
    public void setLastNodeExpandedImage(String lastNodeExpandedImage)
    {
        String val = setNonEmptyValueAttribute(lastNodeExpandedImage);
        if (val != null)
            _iState.setLastNodeExpandedImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for an open non-leaf node with a
     * line below it.  (Defaults to "nodeExpanded.gif").
     * @param nodeExpandedImage the image name (including extension)
     * @jsptagref.attributedescription The image name for an open non-leaf node with a
     * line below it. (Defaults to "nodeExpanded.gif".)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageHandleDownMiddle</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the image name for an open non-leaf node with a
     * line below it."
     */
    public void setNodeExpandedImage(String nodeExpandedImage)
    {
        String val = setNonEmptyValueAttribute(nodeExpandedImage);
        if (val != null)
            _iState.setNodeExpandedImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for a closed non-leaf node with no
     * line below it.  (Defaults to "lastNodeCollapsed.gif").
     * @param lastNodeCollapsedImage the image name (including extension)
     * @jsptagref.attributedescription The image name for a closed non-leaf node with no
     * line below it. (Defaults to "lastNodeCollapsed.gif".)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageHandleRightLast</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the image name for a closed non-leaf node with no
     * line below it."
     */
    public void setLastNodeCollapsedImage(String lastNodeCollapsedImage)
    {
        String val = setNonEmptyValueAttribute(lastNodeCollapsedImage);
        if (val != null)
            _iState.setLastNodeCollapsedImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for a closed non-leaf node with a
     * line below it.  (Defaults to "nodeCollapsed.gif").
     * @param nodeCollapsedImage the image name (including extension)
     * @jsptagref.attributedescription The image name for a closed non-leaf node with a
     * line below it. (Defaults to "nodeCollapsed.gif".)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageHandleRightMiddle</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the image name for a closed non-leaf node with a
     * line below it."
     */
    public void setNodeCollapsedImage(String nodeCollapsedImage)
    {
        String val = setNonEmptyValueAttribute(nodeCollapsedImage);
        if (val != null)
            _iState.setNodeCollapsedImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for a blank area of the tree.
     * (Defaults to "lastLineJoin.gif").
     * @param lastLineJoinImage the image name (including extension)
     * @jsptagref.attributedescription The image name for a blank area of the tree.
     * (Defaults to "lastLineJoin.gif")
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageLineLast</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the image name for a blank area of the tree."
     */
    public void setLastLineJoinImage(String lastLineJoinImage)
    {
        String val = setNonEmptyValueAttribute(lastLineJoinImage);
        if (val != null)
            _iState.setLastLineJoinImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for a spacer image used to align the other images inside the tree.  (Defaults to "spacer.gif").
     * @param spacerImage the image name (including extension)
     * @jsptagref.attributedescription Sets the image name for a spacer image used to align the other images inside the tree.
     * (Defaults to "spacer.gif").
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_spacerImage</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the image name for a spacer image used to align the other images inside the tree."
     */
    public void setSpacerImage(String spacerImage)
    {
        String val = setNonEmptyValueAttribute(spacerImage);
        if (val != null)
            _iState.setImageSpacer(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the default icon for TreeElements for a blank area of the tree.
     * (Defaults to "folder.gif").
     * @param itemIcon the image name of the itemIcon
     * @jsptagref.attributedescription Sets the default icon for TreeElements for a blank area of the tree.
     * (Defaults to "folder.gif").
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_itemIcon</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the default icon for TreeElements for a blank area of the tree."
     */
    public void setItemIcon(String itemIcon)
    {
        String val = setNonEmptyValueAttribute(itemIcon);
        if (val != null)
            _iState.setItemIcon(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for an area with a line through it.
     * (Defaults to "lineJoin.gif").
     * @param lineJoinImage the image name (including extension)
     * @jsptagref.attributedescription The image name for an area with a line through it.
     * (Defaults to "lineJoin.gif").
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageLineMiddle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the image name for an area with a line through it."
     */
    public void setLineJoinImage(String lineJoinImage)
    {
        String val = setNonEmptyValueAttribute(lineJoinImage);
        if (val != null)
            _iState.setLineJoinImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the image name for an area with a line through it.
     * (Defaults to "verticalLine.gif").
     * @param verticalLineImage the image name (including extension)
     * @jsptagref.attributedescription The image name for an area with a line through it.
     * (Defaults to "verticalLine.gif").
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageLIneVertical</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the image name for an area with a line through it."
     */
    public void setVerticalLineImage(String verticalLineImage)
    {
        String val = setNonEmptyValueAttribute(verticalLineImage);
        if (val != null)
            _iState.setVerticalLineImage(setNonEmptyValueAttribute(val));
    }

    /**
     * Sets the name of the directory containing the images.  This should be specified
     * absolutely within the webapp.  This target will provide images for the structure of
     * the tree.  If the <code>iconRoot</code> is not set, then this will also specify where
     * the icons come are found.
     * @param imageRoot the directory name
     * @jsptagref.attributedescription The directory containing the images for tree icons.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_imageRoot</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the name of the directory containing the images for our icons,
     * and structure of the tree."
     */
    public void setImageRoot(String imageRoot)
    {
        _iState.setImageRoot(setNonEmptyValueAttribute(imageRoot));
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
     * Sets the image that will be used for the root when it is Collapsed.  The root must implement
     * ITreeRootElement to support this. (Defaults to "rootCollapsed.gif").
     * @param rootNodeCollapsedImage the image representing a root that is collapsed.
     * @jsptagref.attributedescription The image representing the root when it is collapsed.
     * (Defaults to "rootCollapsed.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_rootNodeCollapsedImage</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the image representing the root when it is collapsed."
     */
    public void setRootNodeCollapsedImage(String rootNodeCollapsedImage)
    {
        _rootNodeCollapsedImage = setNonEmptyValueAttribute(rootNodeCollapsedImage);
    }

    /**
     * Sets the image that will be used for the root when it is Expanded.  The root must implement
     * ITreeRootElement to support this. (Defaults to "rootExpanded.gif").
     * @param rootNodeExpandedImage the image representing a root that is expanded.
     * @jsptagref.attributedescription The image representing the root when it is expanded.
     * (Defaults to "rootExpanded.gif").
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_rootNodeExpandedImage</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the image representing the root when it is expanded."
     */
    public void setRootNodeExpandedImage(String rootNodeExpandedImage)
    {
        _rootNodeExpandedImage = setNonEmptyValueAttribute(rootNodeExpandedImage);
    }

    /**
     * Cause expansion and contraction on the client.  When this is set, the
     * expand and collapse events will be handled on the client.  TreeElements which are hidden
     * becauses other TreeElements are collapsed will be rendered to the page and then displayed
     * by JavaScript on the page.  These expansion and contraction events will not call
     * the server's expansion action.
     * @param runAtClient
     * @jsptagref.attributedescription Indicates whether that base expand and contraction will happen
     * on the client.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_runAtClient</i>
     * @netui:attribute required="false" rtexprvalue="false" type="boolean"
     * description="Indicates whether that base expand and contraction will happen on the client."
     */
    public void setRunAtClient(boolean runAtClient)
    {
        _trs.runAtClient = runAtClient;
    }

    /**
     * This attribue will cause the content of labels to be escaped when the value if <i>true</i>.
     * The default value is <i>false</i>.
     * @param htmlEscape
     * @jsptagref.attributedescription When true the content of labels will be escaped for HTML.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_htmlEscape</i>
     * @netui:attribute required="false" type="boolean"
     * description="When true the content of labels will be escaped for HTML."
     */
    public void setEscapeForHtml(boolean htmlEscape)
    {
        _trs.escapeContent = htmlEscape;
    }

    /**
     * This will cause the standard tagId to Id JavaScript to be output.  For most
     * of the HTML tags this is automatically output.  For the tree, because the tagId
     * is a required attribute, we make javascript support optional.
     * @param renderTagIdLookup
     * @jsptagref.attributedescription Output the standard tagId to Id JavaScript.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_renderTagIdLookup</i>
     * @netui:attribute required="false" type="boolean"
     * description="Output the standard tagId to Id JavaScript."
     */
    public void setRenderTagIdLookup(boolean renderTagIdLookup)
    {
        _renderTagIdLookup = renderTagIdLookup;
    }

    /**
     * Sets the root <code>TreeElement</code> of this tree.
     * @param rootNode the root treeNode
     */
    public void setRootNode(TreeElement rootNode)
    {
        _rootNode = rootNode;
        if (rootNode.getName() == null) {
            rootNode.setName("0");
        }
    }

    /**
     * Return the root node of the tree.
     * @return returns the root node of the tree.
     */
    public TreeElement getRootNode()
    {
        return _rootNode;
    }

    /**
     * Set the style of a tree element when is is selected.  This results in a <code>style</code>
     * attribute being generated for this tree node.
     * @param selectedStyle
     * @jsptagref.attributedescription Set the style of a tree element when is is selected.  This results in a
     * <code>style</code> attribute being generated for this tree node.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_selectedStyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style of a tree element when is is selected.  This results in a <code>style</code>
     * attribute being generated for this tree node."
     */
    public void setSelectedStyle(String selectedStyle)
    {
        _trs.selectedStyle = setNonEmptyValueAttribute(selectedStyle);
    }

    /**
     * Set the style class of a tree element when is is disabled.  A disabled element will have
     * a style that can be expanded/contracted, but may not be selected.
     * @param disableStyleClass
     * @jsptagref.attributedescription Set the style class of a tree element when is is disabled.  A
     * disabled element will have a style that may be expanded/contracted, but may not be selected.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_disableStyleClass</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style class of a tree element when is is disabled.  A disabled element will have
     * a style that can be expanded/contracted, but may not be selected."
     */

    public void setDisabledStyleClass(String disableStyleClass)
    {
        _trs.disabledStyleClass = setNonEmptyValueAttribute(disableStyleClass);
    }

    /**
     * Set the style of a tree element when is is disabled.  A disabled element will have
     * a style that can be expanded/contracted, but may not be selected.
     * @param disabledStyle
     * @jsptagref.attributedescription Set the style of a tree element when is is disabled.  A
     * disabled element will have a style that can be expanded/contracted, but may not be selected.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_disableStyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style of a tree element when is is disabled.  A disabled element will have
     * a style that can be expanded/contracted, but may not be selected."
     */
    public void setDisabledStyle(String disabledStyle)
    {
        _trs.disabledStyle = setNonEmptyValueAttribute(disabledStyle);
    }

    /**
     * Set the style class of a tree element when is is selected.  This results in a <code>class</code>
     * attribute being generated for this tree node.
     * @param selectedStyleClass
     * @jsptagref.attributedescription Set the style class of a tree element when is is selected.  This
     * results in a <code>class</code> attribute being generated for this tree node.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_selectedStyleClass</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style class of a tree element when is is selected.  This results in a <code>class</code>
     * attribute being generated for this tree node."
     */
    public void setSelectedStyleClass(String selectedStyleClass)
    {
        _trs.selectedStyleClass = setNonEmptyValueAttribute(selectedStyleClass);
    }

    /**
     * Set the style class of a tree element when is is not selected.  This results in a <code>style</code>
     * attribute being generated for this tree node.
     * @param unselectedStyle
     * @jsptagref.attributedescription Set the style of a tree element when is is not selected.  This
     * results in a <code>style</code> attribute being generated for this tree node.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_unselectedStyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style of a tree element when is is not selected.  This results in a <code>style</code>
     * attribute being generated for this tree node."
     */
    public void setUnselectedStyle(String unselectedStyle)
    {
        _trs.unselectedStyle = setNonEmptyValueAttribute(unselectedStyle);
    }

    /**
     * Set the style class of a tree element when is is selected.  This results in a <code>class</code>
     * attribute being generated for this tree node.
     * @param unselectedStyleClass
     * @jsptagref.attributedescription Set the style class of a tree element when is is not selected.  This
     * results in a <code>style</code> attribute being generated for this tree node.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_unselectedStyleClasss</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the style class of a tree element when is is selected.  This results in a <code>class</code>
     * attribute being generated for this tree node."
     */
    public void setUnselectedStyleClass(String unselectedStyleClass)
    {
        _trs.unselectedStyleClass = setNonEmptyValueAttribute(unselectedStyleClass);
    }

    /**
     * Sets the <code>style</code> attribute of the tree.
     * @param treeStyle the style
     * @jsptagref.attributedescription Sets the <code>style</code> attribute of the tree.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_treeStyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the <code>style</code> attribute of the tree."
     */
    public void setTreeStyle(String treeStyle)
    {
        _treeStyle = setNonEmptyValueAttribute(treeStyle);
    }

    /**
     * Sets the <code>class</code> attribute of the tree.  This will be set
     * on the containing &lt;div> for the tree.
     * @param treeStyleClass the style
     * @jsptagref.attributedescription Sets the <code>class</code> attribute of the tree.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_treeStyleClass</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the <code>class</code> attribute of the tree."
     */
    public void setTreeStyleClass(String treeStyleClass)
    {
        _treeStyleClass = setNonEmptyValueAttribute(treeStyleClass);
    }

    /**
     * Sets an expression which indentifies the TreeElement that represents the root of
     * the tree.  When the variable being bound to is not null, the body of the Tree
     * will be ignored and the tree being bound to will be rendered.  If the variable
     * being bound to is null, then the body of the tree will be processed to create
     * the initial tree state and the bound variable will be set.  In this situation
     * the property must support both read and write.
     * @param dataSource the tree attribute name
     * @jsptagref.attributedescription sets an expression which indentifies the TreeElement
     * that represents the root of the tree.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_dataSource</i>
     * @netui:attribute required="true"
     * description="Sets an expression which indentifies the TreeElement that represents the root of
     * the tree."
     */
    public void setDataSource(String dataSource)
    {
        _dataSource = dataSource;
    }

    /**
     * Set an attribute value on the implementing class.  The <code>name</code> represents
     * the name of the attribute.  The <code>value</code> represents the value and may contains
     * an expression.  The <code>facet</code> is optional and may be used by complex types to
     * target the attribute to a sub part of the generated markup. This method may result in errors
     * being generated.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws javax.servlet.jsp.JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        // validate the name attribute, in the case of an error simply return.
        if (name == null || name.length() <= 0) {
            String s = Bundle.getString("Tags_AttributeNameNotSet");
            registerTagError(s, null);
            return;
        }
        // it's not legal to set the id or name attributes this way
        if (name != null && (name.equals("netui:treeName"))) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
            return;
        }

        _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, name, value);
    }

    /**
     * Prepare the Tree for rendering.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException, IOException
    {
        if (hasErrors()) {
            reportErrors();
            return;
        }

        // See if there is a TreeRoot already defined.
        _expr = new ExpressionHandling(this);
        TreeElement root = null;
        try {
            root = getTreeRoot(_expr);
        }
        catch (IllegalExpressionException iee) {
            String s = Bundle.getString("TreeRootError", new Object[]{_dataSource, iee.getMessage()});
            registerTagError(s, null);
            return;
        }
        if (hasErrors()) {
            reportErrors();
            return;
        }

        // If we don't have a root element, then we need to create it by processing the body which contains
        // /*<TreeItems>*/ tags that define the tree.
        if (root == null) {
            getBufferBody(false);

            // check to see if we should exit due to an error occuring
            // write out any error text and return.
            if (_errorText != null) {
                write(_errorText.toString());
                if (hasErrors())
                    reportErrors();
            }
        }

        // Set the image Root if it is not set.
        PageContext pageContext = getPageContext();
        if (_iState.getImageRoot() == null)
            _iState.setImageRoot(((HttpServletRequest) pageContext.getRequest()).getContextPath() + "/" +
                    TagConfig.getTreeImageLocation());

        //  errors should have been caught above
        TreeElement treeRoot = getTreeRoot(_expr);

        // if the tree root hasn't been defined, then we need to update the object that is
        // pointed at by the dataSource expression.
        if (treeRoot == null) {
            if (_rootNode != null) {
                try {
                    String datasource = "{" + _dataSource + "}";
                    _expr.updateExpression(datasource, _rootNode, pageContext);
                }
                catch (ExpressionUpdateException e) {
                    String s = Bundle.getString("Tags_UnableToWriteTree", new Object[]{_dataSource, e.getMessage()});
                    registerTagError(s, null);
                    reportErrors();
                    return;
                }
                treeRoot = _rootNode;
            }

            // indicate an update error and return
            if (treeRoot == null) {
                String s = Bundle.getString("Tags_TreeNoAttribute", _dataSource);
                registerTagError(s, null);
                reportErrors();
                return;
            }

        }

        // set the root image
        if (treeRoot instanceof ITreeRootElement) {
            ITreeRootElement tre = (ITreeRootElement) treeRoot;
            if (tre.getRootNodeExpandedImage() == null) {
                tre.setRootNodeExpandedImage((_rootNodeExpandedImage != null) ?
                        _rootNodeExpandedImage : InheritableState.IMAGE_ROOT_EXPANDED);
            }
            if (tre.getRootNodeCollapsedImage() == null) {
                tre.setRootNodeCollapsedImage((_rootNodeCollapsedImage != null) ?
                        _rootNodeCollapsedImage : InheritableState.IMAGE_ROOT_COLLAPSED);
            }
        }

        // if we are running the tree at the client, then
        // we need to register the tree with the NameService
        if (_trs.runAtClient) {
            // it's currently not legal to have a runAtClient but not be an instance of INameable which is
            // implemented by the ITreeRootElement.
            if (!(treeRoot instanceof INameable)) {
                String s = Bundle.getString("Tags_TreeRunAtClientRoot", null);
                registerTagError(s, null);
                reportErrors();
                return;
            }

            // name the tree if it hasn't been named already
            // or if no longer stored in the NameService, add it.
            INameable in = (INameable) treeRoot;
            String o = in.getObjectName();
            NameService ns = NameService.instance(pageContext.getSession());
            if (o == null) {
                ns.nameObject("Tree", in);
                ns.put(in);
            }
            else if (ns.get(o) == null) {
                ns.put(in);
            }
        }


        // prepare to render the tree
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        InternalStringBuilder sb = new InternalStringBuilder(1024);
        StringBuilderRenderAppender writer = new StringBuilderRenderAppender(sb);

        // this is the treeId from the request.  If there was an tree expansion this will be
        // non-null and it identifies what tree had the expansion request.
        // we need to qualify the tree based upon the tagId
        assert(_trs.tagId != null);
        _trs.tagId = getIdForTagId(_trs.tagId);

        String treeId = request.getParameter(TreeElement.TREE_ID);
        if (treeId != null && _trs.tagId.equals(treeId)) {
            TreeHelpers.processTreeRequest(treeId, treeRoot, request, response);
        }

        // check for the nodes that are expanded...
        // Add the script support for the tree.
        if (_trs.runAtClient) {
            IScriptReporter sr = getScriptReporter();
            if (sr == null) {
                String s = Bundle.getString("Tags_TreeRunAtClientSC", null);
                registerTagError(s, null);
                reportErrors();
                return;
            }

            ScriptRequestState srs = ScriptRequestState.getScriptRequestState(request);
            if (!srs.isFeatureWritten(CoreScriptFeature.DYNAMIC_INIT)) {
                String s = Bundle.getString("Tags_TreeHtmlRunAtClient", null);
                registerTagError(s, null);
                reportErrors();
                return;
            }

            assert(treeRoot instanceof ITreeRootElement);
            ITreeRootElement tre = (ITreeRootElement) treeRoot;

            Object[] args = new Object[8];
            args[0] = _iState.getImageRoot() + "/";
            args[1] = tre.getObjectName();
            args[2] = _iState.getNodeCollapsedImage();
            args[3] = _iState.getNodeExpandedImage();
            args[4] = _iState.getLastNodeCollapsedImage();
            args[5] = _iState.getLastNodeExpandedImage();
            args[6] = Bundle.getString("Tags_TreeAltTextExpand", null);
            args[7] = Bundle.getString("Tags_TreeAltTextCollapse", null);
            srs.writeFeature(sr, writer, CoreScriptFeature.TREE_INIT, false, false, args);

            AjaxUrlInfo ajaxInfo = URLRewriterService.getAjaxUrl(pageContext.getServletContext(),request,treeRoot);
            if (ajaxInfo.getCommandPrefix() != null) {
                args = new Object[2];
                args[0] = tre.getObjectName();
                args[1] = ajaxInfo.getCommandPrefix();
                srs.writeFeature(sr,writer, CoreScriptFeature.AJAX_PREFIX, false,false,args);
            }
            if (ajaxInfo.getAjaxParameter() != null) {
                args = new Object[2];
                args[0] = tre.getObjectName();
                args[1] = ajaxInfo.getAjaxParameter();
                srs.writeFeature(sr,writer, CoreScriptFeature.AJAX_PARAM, false,false,args);
            }

            tre.setTreeRenderState(_trs);
            tre.setInheritableState(_iState);
        }

        // create a containing tree level <div> and place the tree level styles on it.
        _divState.styleClass = _treeStyleClass;
        _divState.style = _treeStyle;
        String divId = null;
        if (_renderTagIdLookup) {
            _divState.id = _trs.tagId;
            divId = _divState.id;
        }

        // if we are running on the client then we need to output the tree name into the top level tree <div> tag
        if (_trs.runAtClient) {
            _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "netui:treeName",
                    ((INameable) treeRoot).getObjectName());
        }

        TagRenderingBase divRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG, request);
        divRenderer.doStartTag(writer, _divState);
        sb.append("\n");

        // Render the tree.
        AttributeRenderer extraAttrs = new AttributeRenderer();
        String treeRendererClassName = TagConfig.getTreeRendererClassName();
        TreeRenderer tr = TreeRendererFactory.getInstance(treeRendererClassName);
        if (tr == null) {
            tr = new TreeRenderer();
        }
        tr.init(_trs, request, response, pageContext.getServletContext());
        tr.setTreeRenderSupport(new TagTreeRenderSupport(this));
        tr.render(writer, treeRoot, 0, extraAttrs, _iState);
        if (hasErrors()) {
            reportErrors();
            return;
        }

        // finish the tree representation and write it
        divRenderer.doEndTag(writer);
        sb.append("\n");
        write(sb.toString());

        if (!(treeRoot instanceof ITreeRootElement)) {
            boolean error = false;
            if (_rootNodeExpandedImage != null) {
                String s = Bundle.getString("Tags_TreeRootImageError", null);
                registerTagError(s, null);
                error = true;
            }
            if (_rootNodeCollapsedImage != null) {
                String s = Bundle.getString("Tags_TreeRootImageError", null);
                registerTagError(s, null);
                error = true;
            }
            if (error) {
                reportErrors();
            }            
        }

        // check to see if we are writing out the java.
        if (_renderTagIdLookup) {
            String jsOut = renderDefaultJavaScript(request, divId);
            if (jsOut != null)
                write(jsOut);
        }
    }

    /**
     * Return the <code>TreeControl</code> instance for the tree control that
     * we are rendering.
     * @throws JspException if no TreeControl instance can be found
     */
    protected TreeElement getTreeRoot(ExpressionHandling expr)
            throws JspException
    {
        String datasource = "{" + _dataSource + "}";
        Object treeNode = expr.evaluateExpression(datasource, "dataSource", getPageContext());
        if (treeNode == null || hasErrors()) {
            return null;
        }

        if (!(treeNode instanceof TreeElement)) {
            String s = Bundle.getString("Tags_TreeInvalidAttribute", _dataSource);
            registerTagError(s, null);
            return null;
        }
        return (TreeElement) treeNode;
    }

    /**
     * Replace any occurrence of the specified placeholder in the specified
     * template string with the specified replacement value.
     * @param template    Pattern string possibly containing the placeholder
     * @param placeholder Placeholder expression to be replaced
     * @param value       Replacement value for the placeholder
     */
    protected String replace(String template, String placeholder, String value)
    {
        if (template == null)
            return null;
        if ((placeholder == null) || (value == null))
            return template;

        while (true) {
            int index = template.indexOf(placeholder);
            if (index < 0)
                break;
            InternalStringBuilder temp = new InternalStringBuilder(template.substring(0, index));
            temp.append(value);
            temp.append(template.substring(index + placeholder.length()));
            template = temp.toString();
        }
        return template;
    }

    /**
     * This method will handle creating the tagId attribute.  The tagId attribute indentifies the
     * tag in the generated HTML.  There is a lookup table created in JavaScript mapping the <coe>tagId</code>
     * to the actual name.  The tagId is also run through the naming service so it can be scoped.  Some tags will
     * write that <code>tagid</code> out as the <code>id</code> attribute of the HTML tag being generated.
     * @param tagId
     * @param state
     * @return String
     */
    protected final String renderTagId(HttpServletRequest request, String tagId, AbstractHtmlState state)
    {
        assert(_trs.tagId != null);
        state.id = getIdForTagId(tagId);
        String script = renderDefaultJavaScript(request, state.id);
        return script;
    }

    /**
     * This method will report all collected errors.
     * @param error
     */
    public void collectChildError(String error)
    {
        if (_errorText == null) {
            _errorText = new InternalStringBuilder(32);
        }
        _errorText.append(error);
    }

    //****************************
    /**
     * Much of the code below is taken from the HtmlBaseTag.  We need to eliminate this duplication
     * through some type of helper methods.
     */
    private String renderDefaultJavaScript(HttpServletRequest request, String realId)
    {
        String idScript = null;

        // map the tagId to the real id
        if (TagConfig.isDefaultJavaScript()) {
            ScriptRequestState srs = ScriptRequestState.getScriptRequestState(request);
            idScript = srs.mapTagId(getScriptReporter(), _trs.tagId, realId, null);
        }
        return idScript;
    }
}
