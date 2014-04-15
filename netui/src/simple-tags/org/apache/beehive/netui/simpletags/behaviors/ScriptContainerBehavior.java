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

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.Behavior;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.IdScopeStack;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.DivTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.simpletags.util.RequestUtils;

public class ScriptContainerBehavior extends Behavior
{
    private String _idScope = null;
    private boolean _genScope = false;

    public final static String SCOPE_ID = "netui:scopeId";

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "ScriptContainer";
    }

    /////////////////////////// Attributes ////////////////////////////

    /**
     * Set the idScope associated with the code methods
     * @jsptagref.attributedescription The id that is associated with the script methods.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_scopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The id that is associated with the script methods."
     */
    public void setIdScope(String idScope)
    {
        _idScope = idScope;
    }

    /**
     * return the scopeId associated with the ScriptContainer
     */
    public String getIdScope()
    {
        return _idScope;

    }

    /**
     * If true generate a scope id for this script container.  If this is set to true
     * and a scopeId is also set, the scopeId set will be written.
     * @jsptagref.attributedescription Automatically generate a scopeId for this script container.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_generateScopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Automatically generate a ScopeId."
     */
    public void setGenerateIdScope(boolean genScopeValue)
    {
        _genScope = genScopeValue;
    }

    //******************* Lifecycle Methods ************************************

    public void preRender()
    {
        super.preRender();

        // push the scopeId onto the stack
        TagContext tagContext = ContextUtils.getTagContext();
        setRealIdScope(tagContext);
        pushIdScope(tagContext);
    }

    public void renderStart(Appender appender)
    {
        TagContext tagContext = ContextUtils.getTagContext();
        ScriptReporter sr = tagContext.getScriptReporter();

        sr.writeBeforeBlocks(appender);

        // if there is a scopeId, then we need to create a div to contains everything
        if (_idScope != null) {
            TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG);
            DivTag.State state = new DivTag.State();
            state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "netui:idScope", _idScope);
            br.doStartTag(appender,state);
        }
    }

    public void renderEnd(Appender appender)
    {
        // if we wrote out the scopeId then we end it.
        if (_idScope != null) {
            TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.DIV_TAG);
            br.doEndTag(appender);
        }

        TagContext tagContext = ContextUtils.getTagContext();
        ScriptReporter sr = tagContext.getScriptReporter();

        sr.writeFrameworkScript(appender);
        sr.writeAfterBlocks(appender);
    }

    public void postRender()
    {
        TagContext tagContext = ContextUtils.getTagContext();
        popIdScope(tagContext);
    }

    /////////////////////////////////// Private Support ////////////////////////////////////

    /**
     * If the <code>idScope</code> is set, then we push this onto the id stack.  This method
     * is called in the <code>preRender</code> method and adds the scope to the stack.  Before this
     * method is called, the <code>setRealIdScope</code> method must be called to transform the
     * <code>idScope</code> into the real scoped value.
     * @param tagCtxt
     */
    private void pushIdScope(TagContext tagCtxt)
    {
        if (_idScope != null) {
            IdScopeStack scopeStack = tagCtxt.getIdScopeStack();
            scopeStack.push(_idScope);
        }
    }

    /**
     * This method will pop the <code>idScope</code> value off of the id stack.  This methd is
     * called in the <code>postRender</code> method as the behavior goes out of scope.
     * @param tagCtxt
     */
    private void popIdScope(TagContext tagCtxt)
    {
        if (_idScope != null) {
            IdScopeStack scopeStack = tagCtxt.getIdScopeStack();
            String idScope = scopeStack.pop();
            assert(idScope.equals(_idScope)) : "Mismatch between Id Scopes during pop, found '" +
                    idScope + "', expected: '" + _idScope + "'";
        }
    }

    /**
     * This method will transform the <code>idScope</code> property into a real
     * scoped version of the value.  If the <code>idScope</code> is set, that is the scoped value.
     * Otherwise, if we are genarating a <code>idScope</code> then we generate that here.  The final
     * place a scope can come from is the <code>ScopeKey</code> set on the request.
     */
    private void setRealIdScope(TagContext tagCtxt)
    {
        if (_idScope != null)
                return;

        // if there isn't a set idScope and generate scope is on, generate the scope id.
        if (_genScope) {
            int id = tagCtxt.getNextId();
            _idScope = "n" + Integer.toString(id);
            return;
        }

        // check to see if we have a scopeKey
        _idScope = RequestUtils.getScopeKey();
    }
}
