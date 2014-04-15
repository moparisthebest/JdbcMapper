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

import org.apache.beehive.netui.simpletags.core.Behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * This class implements a stack that represents the current stack of behaviors.  A
 * stack is maintained that allows a behavior to find their ancestors.
 */
public class BehaviorStack
{
    private ArrayList _tagStack;
    private List _readonlyList;

    /**
     * Push a new Behavior onto the stack.
     * @param behavior This is the behavior that will be placed on the stack.
     */
    public void push(Behavior behavior) {
        if (_tagStack == null)
            _tagStack = new ArrayList();
        _tagStack.add(behavior);
    }

    /**
     * Peek will return the current top of stack or null if the stack is empty.
     * @return the current top of stack of null when the stack is empty.
     */
    public Behavior peek() {
        if (_tagStack == null || _tagStack.size() == 0)
            return null;
        return (Behavior) _tagStack.get(_tagStack.size() - 1);
    }

    /**
     * This method will pop the top of stack off and return it to the
     * caller.  If the stack is empty, then null is returned.
     * @return The top of stack or null if the stack is empty.
     */
    public Behavior pop() {
        if (_tagStack == null || _tagStack.size() == 0)
            return null;
        return (Behavior) _tagStack.remove(_tagStack.size() - 1);
    }

    /**
     * This method will return the current stack as a readonly <code>list</code>.  The bottom of
     * the stack is at positon 0, and the top of stack is size()-1.
     * @return a readonly <code>List</code> that contains entries in the stack.
     */
    public List getStackAsList()
    {
        if (_readonlyList == null) {
            if (_tagStack == null) {
                _tagStack = new ArrayList();
            }
            _readonlyList = Collections.unmodifiableList(_tagStack);
        }
        return _readonlyList;
    }

    /**
     * Return the parent of the element that is passed in.
     * @param start The element to find the parent for, if this is null, then we will find we return
     * the parent of the last element returned by peek().
     * @return The Behavior that is the parent of <code>start</code>
     */
    public Behavior getParent(Behavior start)
    {
        if (_tagStack == null || _tagStack.size() == 0)
            return null;

        Object s = (start != null) ? start : _tagStack.get(_tagStack.size() - 1);
        int idx = _tagStack.size() - 1;
        while (idx >= 0) {
            if (_tagStack.get(idx) == s)
                break;
            idx--;
        }
        idx--;
        return (idx >= 0) ?(Behavior) _tagStack.get(idx) : null;
    }

    /**
     * This method will return an ancestor on the stack that is an instanceof
     * the passed in class.  The ancestor to start the search from is passed to
     * this.  Only the parents of <code>start</code> will be checked.
     * @param start The element who's ancestors will be checked
     * @param cls The class we are looking for
     * @return A Behavior that is an instanceof the passed in class or null if not found.
     */
    public Behavior findAncestorWithClass(Behavior start, Class cls)
    {
        if (_tagStack == null || _tagStack.size() == 0)
            return null;

        int adjust = (start != null) ? 1 : 0;
        Object s = (start != null) ? start : _tagStack.get(_tagStack.size() - 1);
        int idx = _tagStack.size() - 1;
        while (idx >= 0) {
            if (_tagStack.get(idx) == s)
                break;
            idx--;
        }

        if (idx <= 0)
            return null;
        idx -= adjust;

        while (idx >= 0) {
            Object o = _tagStack.get(idx);
            if (cls.isInstance(o))
                return (Behavior) o;
            idx--;
        }
        return null;
    }
}
