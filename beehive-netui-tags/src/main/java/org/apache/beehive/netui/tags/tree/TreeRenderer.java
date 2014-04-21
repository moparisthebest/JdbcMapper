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

import org.apache.beehive.netui.core.URLCodec;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.html.HtmlConstants;
import org.apache.beehive.netui.tags.internal.PageFlowTagUtils;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * This class renders the HTML markup for the NetUI Tree. The {@link #render} method
 * recursively renders child nodes of the tree if they're expanded.
 *
 * <p>
 * By default, this predefined NetUI implementation is the class used
 * across the Web application. NetUI may be configured to use a different tree
 * renderer implementation as the renderer in the Web application. A custom
 * tree renderer is configured by setting the &lt;tree-renderer-class> element
 * in the beehive-netui-config.xml file with the name of a class that extends
 * this class.
 * </p>
 * <code>
 * &lt;tree-renderer-class>com.xyz.tree.CustomTreeRenderer&lt;/tree-renderer-class>
 * </code>
 */
public class TreeRenderer implements HtmlConstants
{
    protected static final String FORMAT_INDENT = "      ";
    protected static final String FORMAT_NBSP = "&nbsp;";
    protected static final String FORMAT_NEWLINE = "\n";
    protected static final String FORMAT_SHORT_INDENT = "   ";
    protected static final String FORMAT_NEWLINE_INDENT = FORMAT_NEWLINE + FORMAT_INDENT;
    protected static final String FORMAT_NEWLINE_SHORT_INDENT = FORMAT_NEWLINE + FORMAT_SHORT_INDENT;

    private TagRenderingBase _imageRenderer;
    private TagRenderingBase _anchorRenderer;
    private TagRenderingBase _divRenderer;
    private TagRenderingBase _spanRenderer;

    private ImageTag.State _imgState = new ImageTag.State();
    private AnchorTag.State _anchorState = new AnchorTag.State();
    private DivTag.State _divState = new DivTag.State();
    private SpanTag.State _spanState = new SpanTag.State();

    protected TreeRenderState _trs;

    protected ServletContext _servletContext;
    protected HttpServletRequest _req;
    protected HttpServletResponse _res;

    private TreeRenderSupport _treeRenderSupport;

    public void init(TreeRenderState trs, HttpServletRequest request,
                     HttpServletResponse response, ServletContext servletContext)
    {
        _trs = trs;
        _imageRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, request);
        _anchorRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG, request);
        _divRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG, request);
        _spanRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.SPAN_TAG, request);
        _servletContext = servletContext;
        _req = request;
        _res = response;
    }

    /**
     * This method is set by the NetUI internals to defined an object that
     * handles issues specific to rendering a tree for certain paths of
     * execution in NetUI.
     * @param treeRenderSupport the class to handle NetUI specific issues while
     *        rendering the tree.
     */
    protected void setTreeRenderSupport(TreeRenderSupport treeRenderSupport)
    {
        _treeRenderSupport = treeRenderSupport;
    }

    protected TreeRenderSupport getTreeRenderSupport()
    {
        return _treeRenderSupport;
    }

    protected void registerTagError(String message, Throwable e)
            throws JspException
    {
        _treeRenderSupport.registerTagError(message, e);
    }

    protected String renderTagId(HttpServletRequest request, String tagId, AbstractHtmlState state)
    {
        return _treeRenderSupport.renderTagId(request, tagId, state);
    }

    protected void renderBeforeNode(AbstractRenderAppender writer, TreeElement node)
    {
        _treeRenderSupport.renderBeforeNode(writer, node);
    }

    protected void renderAfterNode(AbstractRenderAppender writer, TreeElement node)
    {
        _treeRenderSupport.renderAfterNode(writer, node);
    }

    /**
     * This is a recursive method which generates the markup for the tree.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     * @param level the level or depth of the node within the tree
     * @param attrs renderer for supported attributes
     * @param state the set of tree properties that are used to render the tree markup
     * @throws javax.servlet.jsp.JspException
     */
    public void render(AbstractRenderAppender writer, TreeElement node, int level,
                       AttributeRenderer attrs, InheritableState state)
            throws JspException
    {
        // assert the values...
        assert(writer != null);
        assert(node != null);

        String nodeName = node.getName();
        assert(nodeName != null);

        // add any attributes to the renderer
        AttributeRenderer.RemoveInfo removes = attrs.addElement(node);

        // Render the beginning of this node
        _divState.clear();
        String tagId = node.getTagId();
        String script = null;
        if (tagId != null) {
            script = renderTagId(_req, tagId, _divState);
        }
        attrs.renderDiv(_divState, node);
        if (_trs.runAtClient) {
            _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_LEVEL, Integer.toString(level));
        }
        renderStartDivPrefix(writer, node);
        renderBeforeNode(writer, node);
        _divRenderer.doStartTag(writer, _divState);
        renderStartDivSuffix(writer, node);
        if (script != null)
            writer.append(script);

        // In devMode we will verify the structure of the tree.  This will not run in
        // production mode.
        ServletContext servletContext = InternalUtils.getServletContext(_req);
        boolean devMode = !AdapterManager.getServletContainerAdapter(servletContext).isInProductionMode();
        if (devMode) {
            boolean error = false;
            InternalStringBuilder errorText = new InternalStringBuilder(64);
            if (node.getName() == null) {
                errorText.append("name");
                error = true;
            }
            if (node.getParent() == null) {
                if (error)
                    errorText.append(", ");
                errorText.append("parent");
            }

            if (error)
                registerTagError(Bundle.getString("Tags_TreeStructureError", errorText.toString()), null);
        }

        // check for tree override properties, the second
        // case here is because the root runs through this an by definitions
        // has InheritableState == state
        InheritableState is = node.getInheritableState();
        if (is != null && is != state) {
            is.setParent(state);
            state = is;
        }

        // write out the images that create the leading indentation for the given node
        renderIndentation(writer, node, level, state);

        // write out the image which occurs next to the node icon
        renderConnectionImage(writer, node, nodeName, state);

        // If needed, render the selection link around the icon for this node.
        // Note that the tag rendered here needs to be closed after the actual
        // icon and label are rendered.
        TagRenderingBase endRender = renderSelectionLink(writer, node, nodeName, attrs, state);

        // render the actual icon for this node
        renderItemIcon(writer, node, attrs, state);

        // render the label for this node (if any)
        renderLabel(writer, node);

        // now, close the selection link (or span) tag
        if (endRender != null) {
            endRender.doEndTag(writer);
        }
        renderSelectionLinkSuffix(writer, node);

        // if there is content then we should render that here...
        renderContent(writer, node);

        // render the end of this node
        renderEndDivPrefix(writer, node);
        _divRenderer.doEndTag(writer);
        renderEndDivPrefix(writer, node);
        renderAfterNode(writer, node);

        // now remove all of the attributes scoped with this...
        attrs.removeElementScoped(node, removes);

        // Render the children of this node
        // If the node is expanded we render it
        //    If we are runAtClient and the node is Not expandOnServer then render it
        if (node.isExpanded() || (_trs.runAtClient && !node.isExpandOnServer())) {
            TreeElement children[] = node.getChildren();
            int newLevel = level + 1;
            for (int i = 0; i < children.length; i++) {
                render(writer, children[i], newLevel, attrs, state);
            }
        }
        attrs.removeElement(node, removes);
    }

    /**
     * Write out the images that create the leading indentation for the given node.
     * @param writer the appender where the node indentation images are appended
     * @param node the node to render
     * @param level the level or depth of the node within the tree
     * @param state the set of tree properties that are used to render the tree markup
     */
    protected void renderIndentation(AbstractRenderAppender writer, TreeElement node, int level, InheritableState state)
    {
        InternalStringBuilder img = new InternalStringBuilder(32);

        // Create the appropriate number of indents
        // These are either the spacer.gif if the parent is the last in the line or the
        // vertical line gif if the parent is not the last child.
        _imgState.clear();
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, WIDTH, "16px");
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, "0");
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, "", false);
        for (int i = 0; i < level; i++) {
            int levels = level - i;
            TreeElement parent = node;
            for (int j = 1; j <= levels; j++) {
                parent = parent.getParent();
            }

            img.setLength(0);
            img.append(state.getImageRoot());
            img.append('/');
            if (parent.isLast()) {
                renderSpacerPrefix(writer, node);
                img.append(state.getImageSpacer());
                _imgState.style = null;
            }
            else {
                renderVerticalLinePrefix(writer, node);
                img.append(state.getVerticalLineImage());
                _imgState.style = "vertical-align:bottom;";
            }
            _imgState.src = img.toString();
            _imageRenderer.doStartTag(writer, _imgState);
            _imageRenderer.doEndTag(writer);
            if (parent.isLast()) {
                renderSpacerSuffix(writer, node);
            }
            else {
                renderVerticalLineSuffix(writer, node);
            }
        }
    }

    /**
     * Write out the image which occurs next to the node icon. This is
     * usually some kind of connecting line, expand, or collapse image. 
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     * @param nodeName the unique name of the node
     * @param state the set of tree properties that are used to render the tree markup
     * @throws JspException
     */
    protected void renderConnectionImage(AbstractRenderAppender writer, TreeElement node, String nodeName, InheritableState state)
            throws JspException
    {
        InternalStringBuilder img = new InternalStringBuilder(32);

        // HACK to take into account special characters like = and &
        // in the node name, could remove this code if encode URL
        // and later request.getParameter() could deal with = and &
        // character in parameter values.
        String encodedNodeName = null;
        try {
            encodedNodeName = URLCodec.encode(nodeName, _res.getCharacterEncoding());
            assert(encodedNodeName != null);
        }
        catch (IOException e) {
            // report the exception and return.
            String s = Bundle.getString("Tags_TreeEncodingError", null);
            registerTagError(s, e);
            return;
        }

        // boolean flag that will indicate if there is an open anchor created
        boolean closeAnchor = false;
        if (!_trs.runAtClient) {
            closeAnchor = renderExpansionAnchor(writer, _anchorRenderer, node, nodeName, state);
        }
        else {
            // Render client expansion and initialize the tree JavaScript support
            closeAnchor = renderClientExpansionAnchor(writer, _anchorRenderer, node, encodedNodeName, state);
        }

        // place the image into the anchor....
        // The type of the image depends upon the position and the type of the node.
        String alt = "";
        img.setLength(0);
        img.append(state.getImageRoot());
        img.append('/');
        if (node.isLeaf()) {                        // leaf node either last or middle
            if (node.isLast())
                img.append(state.getLastLineJoinImage());
            else
                img.append(state.getLineJoinImage());
        }
        else if (node.isExpanded()) {               // interior node that is expanded
            alt = Bundle.getString("Tags_TreeAltTextCollapse", null);
            String rImg = null;
            if (node.getParent() == null && node instanceof ITreeRootElement) {
                rImg = ((ITreeRootElement) node).getRootNodeExpandedImage();
                if (rImg != null) {
                    img.append(rImg);
                }
            }
            if (rImg == null) {
                if (node.isLast())
                    img.append(state.getLastNodeExpandedImage());
                else
                    img.append(state.getNodeExpandedImage());
            }
        }
        else {                                      // interior not expanded
            alt = Bundle.getString("Tags_TreeAltTextExpand", null);
            String rImg = null;
            if (node.getParent() == null && node instanceof ITreeRootElement) {
                rImg = ((ITreeRootElement) node).getRootNodeCollapsedImage();
                if (rImg != null) {
                    img.append(rImg);
                }
            }
            if (rImg == null) {
                if (node.isLast())
                    img.append(state.getLastNodeCollapsedImage());
                else
                    img.append(state.getNodeCollapsedImage());
            }
        }

        if (!closeAnchor) {
            renderConnectionImagePrefix(writer, node);
        }

        // write out the image which occurs next to the icon
        _imgState.clear();
        _imgState.src = img.toString();
        _imgState.style = "vertical-align:bottom;";
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, "0");
        _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt, false);

        _imageRenderer.doStartTag(writer, _imgState);
        _imageRenderer.doEndTag(writer);

        // close the anchor if one was openned...
        if (closeAnchor) {
            _anchorRenderer.doEndTag(writer);
        }
        renderConnectionImageSuffix(writer, node);
    }

    private boolean renderExpansionAnchor(AbstractRenderAppender writer, TagRenderingBase anchorRenderer,
                                          TreeElement node, String nodeName, InheritableState state)
            throws JspException
    {
        // Render the tree state image for this node
        String action = state.getExpansionAction();
        if (action == null) {
            action = state.getSelectionAction();
        }
        boolean isAction = PageFlowTagUtils.isAction(_req, action);
        if (!isAction) {
            registerTagError(Bundle.getString("Tags_BadAction", action), null);
            return false;
        }

        // encode the tree parameters into the action.
        HashMap params = new HashMap();
        params.put(TreeElement.EXPAND_NODE, nodeName);
        assert (_trs.tagId != null);
        params.put(TreeElement.TREE_ID, _trs.tagId);
        String uri = null;
        try {
            boolean xml = TagRenderingBase.Factory.isXHTML(_req);
            uri = PageFlowUtils.getRewrittenActionURI(_servletContext, _req, _res, action, params, null, xml);
        }
        catch (URISyntaxException e) {
            // report the error...
            String s = Bundle.getString("Tags_Tree_Node_URLException",
                    new Object[]{action, e.getMessage()});
            registerTagError(s, e);
        }

        boolean ret = false;
        if ((uri != null) && !node.isLeaf()) {
            _anchorState.clear();
            _anchorState.href = _res.encodeURL(uri);
            renderConnectionImagePrefix(writer, node);
            anchorRenderer.doStartTag(writer, _anchorState);
            ret = true;
        }
        return ret;
    }

    private boolean renderClientExpansionAnchor(AbstractRenderAppender writer, TagRenderingBase anchorRenderer,
                                                TreeElement node, String encodedNodeName,
                                                InheritableState state)
    {
        boolean imgOverride = (state != state.getParent() && state.getParent() != null) ||
                (node.getParent() == null);

        if (!node.isLeaf()) {
            boolean expanded = node.isExpanded();
            _anchorState.clear();
            _anchorState.href = "#";
            _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_ANCHOR,
                    (expanded ? TreeElement.TREE_EXPAND_STATE : TreeElement.TREE_COLLAPSE_STATE));
            _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_ANCHOR_INIT, "true");
            _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_ANCHOR_ID, encodedNodeName);
            if (node.isLast()) {
                _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_NODE_LAST, "true");
            }

            // Does this node have it's images being overridden?
            if (imgOverride) {
                if (node.getParent() == null) {
                    String rootImg = ((ITreeRootElement) node).getRootNodeCollapsedImage();
                    if (rootImg != null)
                        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_COLLAPSE_IMAGE,
                                state.getImageRoot() + "/" + rootImg);
                    else
                        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_COLLAPSE_IMAGE,
                                state.getImageRoot() + "/" + state.getLastNodeCollapsedImage());
                    rootImg = ((ITreeRootElement) node).getRootNodeExpandedImage();
                    if (rootImg != null)
                        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_EXPAND_IMAGE,
                                state.getImageRoot() + "/" + rootImg);
                    else
                        _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_EXPAND_IMAGE,
                                state.getImageRoot() + "/" + state.getLastNodeExpandedImage());
                }
                else if (node.isLast()) {
                    _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_COLLAPSE_IMAGE,
                            state.getImageRoot() + "/" + state.getLastNodeCollapsedImage());
                    _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_EXPAND_IMAGE,
                            state.getImageRoot() + "/" + state.getLastNodeExpandedImage());
                }
                else {
                    _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_COLLAPSE_IMAGE,
                            state.getImageRoot() + "/" + state.getNodeCollapsedImage());
                    _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_EXPAND_IMAGE,
                            state.getImageRoot() + "/" + state.getNodeExpandedImage());
                }
            }

            if (node.isExpandOnServer() && !node.isExpanded()) {
                String path = _req.getServletPath();
                int idx = path.lastIndexOf('/');
                if (idx != -1) {
                    path = path.substring(1, idx);
                }
                _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_EXPAND, "true");
                _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TreeElement.TREE_EXPAND_PATH, path);
            }

            renderConnectionImagePrefix(writer, node);
            anchorRenderer.doStartTag(writer, _anchorState);
            return true;
        }
        return false;
    }

    /**
     * If needed, render the selection link around the icon for this node.
     * Note that the tag rendered here needs to be closed after the actual
     * icon and label are rendered.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     * @param nodeName the unique name of the node
     * @param attrs renderer for supported attributes
     * @param state the set of tree properties that are used to render the tree markup
     * @return the selection link (or span) tag renderer to close after the item
     *         icon and label rendered
     * @throws JspException
     */
    protected TagRenderingBase renderSelectionLink(AbstractRenderAppender writer,
                                                   TreeElement node, String nodeName,
                                                   AttributeRenderer attrs,
                                                   InheritableState state)
            throws JspException
    {
        // calculate the selection link for this node
        String selectionLink = getSelectionlink(node, nodeName, state);

        TagRenderingBase endRender = null;
        // if there is a selection link we need to put an anchor out.
        if (selectionLink != null) {
            _anchorState.clear();
            _anchorState.href = selectionLink;
            String target = node.getTarget();
            if (target == null) {
                target = state.getSelectionTarget();
            }
            _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TARGET, target);
            String title = node.getTitle();
            _anchorState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TITLE, title);

            // set the selection styles
            if (node.isSelected()) {
                _anchorState.style = _trs.selectedStyle;
                _anchorState.styleClass = _trs.selectedStyleClass;
            }
            else {
                _anchorState.style = _trs.unselectedStyle;
                _anchorState.styleClass = _trs.unselectedStyleClass;
            }
            if (_anchorState.style == null && _anchorState.styleClass == null) {
                _anchorState.style = "text-decoration: none";
            }

            // render any attributes applied to the HTML
            attrs.renderSelectionLink(_anchorState, node);

            // render the runAtClient attributes
            if (_trs.runAtClient) {
                String action = node.getClientAction();
                if (action != null) {
                    action = HtmlUtils.escapeEscapes(action);
                    action = ScriptRequestState.getString("netuiAction", new Object[]{action});
                }
                _anchorState.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK, action);
                // Jira 299
                //_anchorState.onClick = action;
            }

            // actually render the anchor.
            renderSelectionLinkPrefix(writer, node);
            _anchorRenderer.doStartTag(writer, _anchorState);
            endRender = _anchorRenderer;
        }
        else {
            // This node doesn's support selection.  This means we consider it disabled.  We will
            // put a span around it and set the style/class to indicate that it is disabled.
            _spanState.clear();
            _spanState.styleClass = _trs.disabledStyleClass;
            _spanState.style = _trs.disabledStyle;
            renderSelectionLinkPrefix(writer, node);
            _spanRenderer.doStartTag(writer, _spanState);
            endRender = _spanRenderer;
        }

        return endRender;
    }

    /**
     * Calculate the selection link for this node, if the node is disabled, we can skip
     * this because a disabled node may not be selected.
     * @param node the node to render
     * @param nodeName the unique name of the node
     * @param state the set of tree properties that are used to render the tree markup
     * @return the URL for the selection link
     * @throws JspException
     */
    protected String getSelectionlink(TreeElement node, String nodeName, InheritableState state)
            throws JspException
    {
        String selectionLink = null;

        if (!node.isDisabled()) {

            // The action on the node overrides all.  Otherwise, check to see if there
            // is either an href or clientAction.  If neither is set, then we inherit the
            // action defined on the trees inheritable state.
            String action = node.getAction();
            if (action == null) {
                selectionLink = node.getHref();
                if (selectionLink == null && node.getClientAction() != null) {
                    selectionLink = "";
                }
                if (selectionLink == null) {
                    action = state.getSelectionAction();
                }
            }

            // create the selection link
            if (action != null && selectionLink == null) {
                HashMap params = null;
                boolean remove = false;
                params = node.getParams();
                if (params == null) {
                    params = new HashMap();
                    remove = true;
                }
                params.put(TreeElement.SELECTED_NODE, nodeName);
                if (_trs.tagId != null) {
                    params.put(TreeElement.TREE_ID, _trs.tagId);
                }

                // Add the jpf ScopeID param if necessary.
                String scope = node.getScope();
                if (scope != null) {
                    params.put(ScopedServletUtils.SCOPE_ID_PARAM, scope);
                }

                String uri = null;
                try {
                    boolean xml = TagRenderingBase.Factory.isXHTML(_req);
                    uri = PageFlowUtils.getRewrittenActionURI(_servletContext, _req, _res, action, params, null, xml);
                }
                catch (URISyntaxException e) {
                    // report the error...
                    String s = Bundle.getString("Tags_Tree_Node_URLException",
                            new Object[]{action, e.getMessage()});
                    registerTagError(s, e);
                }

                if (remove) {
                    params.remove(TreeElement.SELECTED_NODE);
                    if (_trs.tagId != null) {
                        params.remove(TreeElement.TREE_ID);
                    }

                    if (scope != null) {
                        params.remove(ScopedServletUtils.SCOPE_ID_PARAM);
                    }
                }

                if (uri != null) {
                    selectionLink = _res.encodeURL(uri);
                }
            }
        }
        return selectionLink;
    }

    /**
     * Render the icon for this node.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     * @param attrs renderer for supported attributes
     * @param state the set of tree properties that are used to render the tree markup
     */
    protected void renderItemIcon(AbstractRenderAppender writer,
                                  TreeElement node,
                                  AttributeRenderer attrs,
                                  InheritableState state)
    {
        renderItemIconPrefix(writer, node);

        // There should always be one unless the tree turns off default
        // icons by setting the useDefaultIcons attribute to false.
        String icon = node.getIcon();
        if (icon == null) {
            icon = state.getIconRoot() + "/" + state.getItemIcon();
        }
        else {
            icon = state.getIconRoot() + "/" + icon;
        }

        // write out the icon
        if (icon != null) {
            _imgState.clear();
            _imgState.src = icon;
            _imgState.style = "vertical-align:text-top";
            String alt = null;
            String label = node.getLabel();
            if (label != null && node.isLabelLegalAsAlt())
                alt = label;
            else
                alt = node.getTitle();
            if (alt == null)
                alt = Bundle.getString("Tags_TreeAltText", null);
            _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt, false);
            _imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, "0");

            // set the inheritted attributes
            attrs.renderIconImage(_imgState, node);
            _imageRenderer.doStartTag(writer, _imgState);
            _imageRenderer.doEndTag(writer);
            renderItemIconSuffix(writer, node);
        }
    }

    /**
     * Render the label for this node (if any).
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderLabel(AbstractRenderAppender writer, TreeElement node)
    {
        String label = node.getLabel();
        if (label != null) {
            renderLabelPrefix(writer, node);
            if (_trs.escapeContent) {
                HtmlUtils.filter(label, writer);
            }
            else {
                writer.append(label);
            }
            renderLabelSuffix(writer, node);
        }
    }

    /**
     * Render the Content for this node (if any).
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderContent(AbstractRenderAppender writer, TreeElement node)
    {
        String ctnt = node.getContent();
        if (ctnt != null) {
            renderContentPrefix(writer, node);
            if (_trs.escapeContent) {
                HtmlUtils.filter(ctnt, writer);
            }
            else {
                writer.append(ctnt);
            }
            renderContentSuffix(writer, node);
        }
    }

    // Methods that control formatting (beautifying) the node markup. Override these to
    // manage the white space displayed in the page or to add additional markup.

    /**
     * Render the formatting before a spacer image.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderSpacerPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_INDENT);
    }

    /**
     * Render the formatting after a spacer image.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderSpacerSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
    }

    /**
     * Render the formatting before a vertical line image.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderVerticalLinePrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_INDENT);
    }

    /**
     * Render the formatting following a vertical line image.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderVerticalLineSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
    }

    /**
     * Render the formatting before the connecting/expand/collapse image.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderConnectionImagePrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_INDENT);
    }

    /**
     * Render the formatting after the connecting/expand/collapse image.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderConnectionImageSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
    }

    /**
     * Render the formatting before the node selection anchor.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderSelectionLinkPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_INDENT);
    }

    /**
     * Render the formatting after the node selection anchor.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderSelectionLinkSuffix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    /**
     * Render the formatting before the node icon.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderItemIconPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NBSP);
    }

    /**
     * Render the formatting after the node icon.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderItemIconSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NBSP);
    }

    /**
     * Render the formatting before the node label.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderLabelPrefix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    /**
     * Render the formatting after the node label.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderLabelSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NBSP);
    }

    /**
     * Render the formatting before the node content.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderContentPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE_INDENT);
    }

    /**
     * Render the formatting after the node content.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderContentSuffix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    /**
     * Render the indent formatting of the start div tag used before the node markup.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderStartDivPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_SHORT_INDENT);
    }

    /**
     * Render the formatting after the start div tag and before the node markup.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderStartDivSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
    }

    /**
     * Render the indent formatting of the end div tag used after the node markup.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderEndDivPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE_SHORT_INDENT);
    }

    /**
     * Render the formatting after the end div tag that follows the node markup.
     * @param writer the appender where the tree markup is appended
     * @param node the node to render
     */
    protected void renderEndDivSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
    }
}
