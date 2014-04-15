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
package org.apache.beehive.netui.simpletags.core;

import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.PageFlowContextActivator;
import org.apache.beehive.netui.simpletags.core.services.ErrorReporter;
import org.apache.beehive.netui.simpletags.core.services.BehaviorStack;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.core.services.IdScopeStack;

public class TagContext
{
    public static PageFlowContextActivator ACTIVATOR = new ContextActivator();

    private ErrorReporter _errorReporter;
    private ScriptReporter _scriptReporter;
    private IdScopeStack _idScopeStack;
    private BehaviorStack _behaviorStack;
    private int _nextId;
    private int _renderingType = -1;
    private IDocumentTypeProducer _docTypeProducer;

    public static final String TAG_CONTEXT_NAME = "netui.velocity.tags";

    /**
     * This is a static method that will return the TagContext.
     * @return The tagContext being used in this request.
     */
    public static TagContext getContext()
    {
        PageFlowContext pfCtxt = PageFlowContext.getContext();
        assert(pfCtxt != null) : "The PageFlowContext was not found";
        TagContext tagCtxt = (TagContext) pfCtxt.get(TAG_CONTEXT_NAME,ACTIVATOR);
        assert (tagCtxt != null) : "The TagContext was not found";
        return tagCtxt;
    }


    //**************************** Rendering type ***********************

    public int getTagRenderingType()
    {
        return _renderingType;
    }

    public void setTagRenderingType(int renderingType) {
        _renderingType = renderingType;
    }

    public IDocumentTypeProducer getDocTypeProducer()
    {
        return _docTypeProducer;
    }

    public void setDocTypeProducer(IDocumentTypeProducer docTypeProducer) {
        _docTypeProducer = docTypeProducer;
    }

    //******************************* Error Handling *******************************//

    public ErrorReporter getErrorReporter()
    {
        if (_errorReporter == null)
            _errorReporter = new ErrorReporter();
        return _errorReporter;
    }

    //******************************* Error Handling *******************************//
    public ScriptReporter getScriptReporter()
    {
        if (_scriptReporter == null)
            _scriptReporter = new ScriptReporter();
        return _scriptReporter;
    }

    //******************************* Tag Hierarchy ********************************//
    public BehaviorStack getBehaviorStack()
    {
        if (_behaviorStack == null)
            _behaviorStack = new BehaviorStack();
        return _behaviorStack;
    }

    //******************************* Id Scoping Support ********************************//
    public IdScopeStack getIdScopeStack()
    {
        if (_idScopeStack == null)
            _idScopeStack = new IdScopeStack();
        return _idScopeStack;
    }

    /**
     * This method will generate the next unique int within the HTML tag.
     *
     * @return the next unique integer for this request.
     */
    public int getNextId()
    {
        return _nextId++;
    }

    static class ContextActivator implements PageFlowContextActivator
    {

        public Object activate()
        {
            return new TagContext();
        }
    }
}
