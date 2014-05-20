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
package org.apache.beehive.netui.tags.divpanel;

import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.script.ExpressionUpdateException;
import org.apache.beehive.netui.script.IllegalExpressionException;
import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.ExpressionHandling;
import org.apache.beehive.netui.tags.TagConfig;
import org.apache.beehive.netui.tags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.DivTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * A DivPanel creates an HTML &lt;div> tag that may contain additional &lt;div> tags.  There will only
 * be a single div that is visible at a time.
 * @jsptagref.tagdescription Creates an HTML &lt;div> tag that may contain additional &lt;div> tags.  Only a single section will be visible at a time.
 * @netui:tag name="divPanel"
 * description="A divPanel is an placeholder which may contain multiple sections.  Only a single section will be visible at a time."
 */
public class DivPanel extends AbstractClassicTag
{
    private String _tagId;
    private String _firstPage;
    private String _dataSource = null;       // The name of the tree.
    private DivTag.State _divState = new DivTag.State();

    private final String JAVASCRIPT_CLASS = "NetUIDivPanel";

    public static final String DIVPANEL_JAVASCRIPT_ATTR = "netui-div-panel";
    public static final String DIVPANEL_FIRST_PAGE = "netui-div-panel-first";

    public static final String DIVPANEL_DIV_ID = "netui_divpanel_";

    public static String getCurrentPage(ServletRequest req, String tagId)
    {
        String reqId = DIVPANEL_DIV_ID + tagId;
        String page = req.getParameter(reqId);
        return page;
    }

    /**
     * Returns the name of the Tag.  This is used to
     * identify the type of tag reporting errors.
     * @return a constant string representing the name of the tag.
     */
    public String getTagName()
    {
        return "DivPanel";
    }

    /**
     * Sets an expression which indentifies the DivPanelState which will store the state of the
     * DivPanel between posts to the server.
     * @param dataSource the tree attribute name
     * @jsptagref.attributedescription An expression which identifies which DivPanelState object will store state between posts to the server.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression</i>
     * @netui:attribute rtexprvalue="true" description="Sets an expression which indentifies the DivPanelState storing the state of the
     * DivPanel between posts."
     */
    public void setDataSource(String dataSource)
    {
        _dataSource = dataSource;
    }

    /**
     * Set the ID of the tag.
     * @param tagId the tagId.
     * @jsptagref.attributedescription Set the ID of the &lt;div> tag
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="Set the ID of the tag."
     */
    public void setTagId(String tagId)
    {
        _tagId = tagId;
    }

    /**
     * Set the name of the first page to display.
     * @param firstPage the name of the first page.
     * @jsptagref.attributedescription Set the name of the first page to display.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression</i>
     * @netui:attribute rtexprvalue="true"
     * description="Set the name of the first page to display."
     */
    public void setFirstPage(String firstPage)
    {
        _firstPage = firstPage;
    }

    /**
     * Causes the content of the section to be rendered into a buffer.
     * @return SKIP_BODY if the visible state is <code>false</code>,
     *         otherwise EVAL_BODY_BUFFERED to cause the body content to be buffered.
     * @throws javax.servlet.jsp.JspException if there are errors.
     */
    public int doStartTag()
            throws JspException
    {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        // if there was a dataSource defined we need to get it based upon the expression
        // if the variable comes back null, we then create a DivPanelState and set it back
        // to the property referred to by the expression.
        DivPanelState state = null;
        if (_dataSource != null) {
            ExpressionHandling _expr;
            _expr = new ExpressionHandling(this);
            try {
                state = getState(_expr);
            }
            catch (IllegalExpressionException iee) {
                String s = Bundle.getString("TreeRootError", new Object[]{_dataSource, iee.getMessage()});
                registerTagError(s, null);
                return SKIP_BODY;
            }
            if (hasErrors())
                reportAndExit(SKIP_BODY);

            // if we got here and the state is null then create a new divPanel, and push it back on
            // the expression
            if (state == null) {
                try {
                    state = new DivPanelState();
                    String datasource = "{" + _dataSource + "}";
                    _expr.updateExpression(datasource, state, pageContext);
                }
                catch (ExpressionUpdateException e) {
                    String s = Bundle.getString("Tags_UnableToWriteTree", new Object[]{_dataSource, e.getMessage()});
                    registerTagError(s, null);
                    reportErrors();
                    return SKIP_BODY;
                }

                if (hasErrors())
                    reportAndExit(SKIP_BODY);

                // name the divPanel so we can post state back to this state object.
                NameService ns = NameService.instance(pageContext.getSession());
                String name = state.getObjectName();
                if (name == null) {
                    ns.nameObject("DivPanel", state);
                    ns.put(state);
                }
                else if (ns.get(name) == null) {
                    // no longer stored in the NameService, add it.
                    ns.put(state);
                }
            }
        }

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);

        // verify hat we are in a container with run at client on...
        IScriptReporter sr = getScriptReporter();
        ScriptRequestState srs = ScriptRequestState.getScriptRequestState(req);
        if (!srs.isFeatureWritten(CoreScriptFeature.DYNAMIC_INIT)) {
            String s = Bundle.getString("Tags_DivPanelHtmlRunAtClient", null);
            registerTagError(s, null);
            reportAndExit(SKIP_BODY);
        }
        srs.writeFeature(sr, writer, CoreScriptFeature.DIVPANEL_INIT, true, false, null);

        // figure out if there is a page to render
        String page = _firstPage;
        if (state != null) {
            String fp = state.getFirstPage();
            if (fp != null)
                page = fp;
        }

        if (hasErrors())
            reportAndExit(EVAL_BODY_INCLUDE);

        _divState.id = this.getIdForTagId(_tagId);
        _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, DIVPANEL_JAVASCRIPT_ATTR, "true");
        if (page != null)
            _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, DIVPANEL_FIRST_PAGE, page);
        if (state != null)
            _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "netui:divName", state.getObjectName());

        TagRenderingBase divRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG, req);
        divRenderer.doStartTag(writer, _divState);
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Stores the buffered body content into the <code>TEMPLATE_SECTIONS
     * HashMap</code>.  The buffered body is
     * accessed by the template page to obtain
     * the content for <code>IncludeSection</code> tags.
     * @return EVAL_PAGE to continue evaluating the page.
     * @throws JspException on error.
     */
    public int doEndTag()
            throws JspException
    {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        if (!hasErrors()) {
            if (TagConfig.isDefaultJavaScript()) {
                if (_divState.id != null) {
                    ScriptRequestState srs = ScriptRequestState.getScriptRequestState(req);
                    srs.mapTagId(getScriptReporter(), _divState.id, _divState.id, null);
                }
            }
            WriteRenderAppender writer = new WriteRenderAppender(pageContext);
            TagRenderingBase divRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG, req);
            divRenderer.doEndTag(writer);
        }
        localRelease();
        return EVAL_PAGE;
    }

    protected void localRelease()
    {
        super.localRelease();
        _tagId = null;
        _divState.clear();
        _firstPage = null;
        _dataSource = null;
    }

    /**
     */
    protected DivPanelState getState(ExpressionHandling expr)
            throws JspException
    {
        String datasource = "{" + _dataSource + "}";
        Object state = expr.evaluateExpression(datasource, "dataSource", pageContext);
        if (state == null || hasErrors()) {
            return null;
        }

        if (!(state instanceof DivPanelState)) {
            String s = Bundle.getString("Tags_DivPanelInvalidAttribute", _dataSource);
            registerTagError(s, null);
            return null;
        }
        return (DivPanelState) state;
    }
}
