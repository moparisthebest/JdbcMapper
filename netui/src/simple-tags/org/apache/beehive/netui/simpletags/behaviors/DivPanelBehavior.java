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
package org.apache.beehive.netui.simpletags.behaviors;

import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.script.ExpressionUpdateException;
import org.apache.beehive.netui.script.IllegalExpressionException;
import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.Behavior;
import org.apache.beehive.netui.simpletags.core.ExpressionHandling;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.IdScopeStack;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.divpanel.DivPanelState;
import org.apache.beehive.netui.simpletags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.DivTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.el.VariableResolver;

public class DivPanelBehavior extends Behavior
{
    private String _tagId;
    private String _firstPage;
    private String _dataSource = null;       // The name of the tree.
    private VariableResolver _vr;
    private DivPanelState _state = null;

    private DivTag.State _divState = new DivTag.State();

    public static final String DIVPANEL_JAVASCRIPT_ATTR = "netui-div-panel";
    public static final String DIVPANEL_FIRST_PAGE = "netui-div-panel-first";

    public static final String DIVPANEL_DIV_ID = "netui_divpanel_";


    //@todo: why?
    //public static String getCurrentPage(ServletRequest req, String tagId)
    //{
    //    String reqId = DIVPANEL_DIV_ID + tagId;
    //    return req.getParameter(reqId);
    //}

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
     * DivPanelBehavior between posts to the server.
     * @param dataSource the tree attribute name
     * @jsptagref.attributedescription An expression which identifies which DivPanelState object will store state between posts to the server.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression</i>
     * @netui:attribute description="Sets an expression which indentifies the DivPanelState storing the state of the
     * DivPanelBehavior between posts."
     */
    public void setDataSource(String dataSource)
    {
        _dataSource = dataSource;
    }

    /**
     * This method will set the variable resolver.  This required for all DataSource Behaviors in order
     * to resolve the expression.
     * @param vr The variable resolver used for this page...
     */
    public void setVariableResolver(VariableResolver vr)
    {
        _vr = vr;
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

    //******************* Lifecycle Methods ************************************

    /**
     * This method will push the Behavior on the behavior stack.  All overrides of
     * this method should call this method so that the stack is maintained correctly.
     */
    public void preRender()
    {
        // if there was a dataSource defined we need to get it based upon the expression
        // if the variable comes back null, we then create a DivPanelState and set it back
        // to the property referred to by the expression.

        if (_dataSource != null) {
            ExpressionHandling _expr;
            _expr = new ExpressionHandling(this, _vr);
            try {
                _state = getState(_expr);
            }
            catch (IllegalExpressionException iee) {
                String s = Bundle.getString("TreeRootError", new Object[]{_dataSource, iee.getMessage()});
                registerTagError(s, null);
                return;
            }
            if (hasErrors())
                return;

            // if we got here and the state is null then create a new divPanel, and push it back on
            // the expression
            if (_state == null) {
                try {
                    _state = new DivPanelState();
                    String datasource = "{" + _dataSource + "}";
                    _expr.updateExpression(datasource, _state);
                }
                catch (ExpressionUpdateException e) {
                    String s = Bundle.getString("Tags_UnableToWriteTree", new Object[]{_dataSource, e.getMessage()});
                    registerTagError(s, null);
                    return;
                }

                if (hasErrors())
                    return;

                // name the divPanel so we can post state back to this state object.
                NameService ns = ContextUtils.getNameService();
                ns.nameObject("DivPanelBehavior", _state);
                ns.put(_state);
            }
        }

    }

    /**
     * This method will render the start tag for the markup generated by the behavior.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderStart(Appender appender)
    {
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        TagContext tagCtxt = ContextUtils.getTagContext();

        // verify hat we are in a container with run at client on...
        ScriptReporter sr = tagCtxt.getScriptReporter();
        if (!sr.isFeatureWritten(CoreScriptFeature.DYNAMIC_INIT)) {
            String s = Bundle.getString("Tags_DivPanelHtmlRunAtClient", null);
            registerTagError(s, null);
            reportErrors(appender);
            return;
        }
        sr.writeFeature(CoreScriptFeature.DIVPANEL_INIT, true, false, null);

        // figure out if there is a page to render
        String page = _firstPage;
        if (_state != null) {
            String fp = _state.getFirstPage();
            if (fp != null)
                page = fp;
        }

        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        IdScopeStack idStack = tagCtxt.getIdScopeStack();
        _divState.id = idStack.getIdForTagId(_tagId);
        _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, DIVPANEL_JAVASCRIPT_ATTR, "true");
        if (page != null)
            _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, DIVPANEL_FIRST_PAGE, page);
        if (_state != null)
            _divState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "netui:divName", _state.getObjectName());

        TagRenderingBase divRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG);
        divRenderer.doStartTag(appender, _divState);
    }

    /**
     * This method will render teh end tag for the markup generted by the behavior.
     * @param appender The <code>Appender</code> to write the markup into.
     */
    public void renderEnd(Appender appender)
    {
        if (hasErrors())
            return;

        TagRenderingBase divRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG);
        divRenderer.doEndTag(appender);
    }

    /**
     * This method will pop the Behavior off of the Behavior stack.  It should always be called when
     * the method is overriden.
     */
    public void postRender()
    {
    }

    /**
     */
    private DivPanelState getState(ExpressionHandling expr)
    {
        String datasource = "{" + _dataSource + "}";
        Object state = expr.evaluateExpression(datasource, "dataSource");
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
