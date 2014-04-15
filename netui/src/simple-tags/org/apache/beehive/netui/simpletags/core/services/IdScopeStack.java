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
package org.apache.beehive.netui.simpletags.core.services;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.ArrayList;

/**
 * This class will manage a stack of scopeIds.  These scopes are added to the stack
 * by tags that implement scoping.
 */
public class IdScopeStack
{
    private ArrayList _scopes;

    public void push(String scopeId)
    {
        if (_scopes == null)
            _scopes = new ArrayList();
        _scopes.add(scopeId);
    }

    public String pop() {
        if (_scopes == null || _scopes.size() == 0)
            return null;
        return (String) _scopes.remove(_scopes.size() -1);
    }

    public String peek() {
        if (_scopes == null || _scopes.size() == 0)
            return null;
        return (String)_scopes.get(_scopes.size()-1);
    }

    public String getIdForTagId(String tagId)
    {
        if (_scopes == null || _scopes.size() == 0)
            return tagId;

        InternalStringBuilder sb = new InternalStringBuilder();
        for (int i=0;i<_scopes.size();i++) {
            sb.append((String)_scopes.get(i));
            sb.append('.');
        }
        sb.append(tagId);
        return sb.toString();
    }
}
