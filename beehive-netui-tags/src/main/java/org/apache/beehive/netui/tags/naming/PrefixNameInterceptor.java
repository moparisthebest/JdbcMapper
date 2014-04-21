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
package org.apache.beehive.netui.tags.naming;

import org.apache.beehive.netui.script.ExpressionEvaluationException;

import javax.servlet.jsp.tagext.Tag;

/**
 * A {@link INameInterceptor} that is used to add a prefix handler onto a
 * NetUI tag that is being written to the client.  Occasionally, NetUI
 * tags need to be pre-processed before passing the name and value
 * to the {@link org.apache.beehive.netui.pageflow.ProcessPopulate} handler
 * in order to update a bean property.  This preprocessing is done by
 * implementing a handler implementing the interface
 * {@link org.apache.beehive.netui.pageflow.RequestParameterHandler}, registering
 * this interface with the {@link org.apache.beehive.netui.pageflow.ProcessPopulate#registerPrefixHandler(String, org.apache.beehive.netui.pageflow.RequestParameterHandler)}
 * method, and adding a prefix which references this RequestParameterHandler to the
 * name of each paramter that should be handled by the implementation before
 * updating the bean property.
 */
public class PrefixNameInterceptor implements INameInterceptor
{
    private String tagKey;

    public PrefixNameInterceptor(String key)
    {
        tagKey = key;
    }

    protected PrefixNameInterceptor()
    {
        super();
    }

    public String rewriteName(String name, Tag currentTag)
            throws ExpressionEvaluationException
    {
        return rewriteNameInternal(name, tagKey);
    }

    protected String rewriteNameInternal(String name, String key)
            throws ExpressionEvaluationException
    {
        return org.apache.beehive.netui.pageflow.ProcessPopulate.writeHandlerName(key, name);
    }
}
