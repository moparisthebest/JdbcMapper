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
package org.apache.beehive.netui.script.common;

import java.util.Stack;
import javax.servlet.jsp.JspContext;

/**
 * This class is used by the framework to store a stack of {@link IDataAccessProvider} objevcts.  This
 * is used when nesting user interface elements that all expose some {@link IDataAccessProvider} in
 * order to keep track of a parent's current item in a data set.
 */
public class DataAccessProviderStack {

    private static final String KEY = DataAccessProviderStack.class.getName();

    private Stack _stack = null;

    public static void addDataAccessProvider(IDataAccessProvider provider, JspContext jspContext) {
        assert jspContext != null;

        DataAccessProviderBean bean = new DataAccessProviderBean(provider);

        Object val = jspContext.getAttribute(KEY);
        DataAccessProviderStack curStack = null;
        if(val == null) {
            curStack = new DataAccessProviderStack();

            jspContext.setAttribute(KEY, curStack);
        }
        else curStack = (DataAccessProviderStack)val;

        curStack.push(bean);

        jspContext.setAttribute("container", bean);
    }

    public static DataAccessProviderBean removeDataAccessProvider(JspContext jspContext) {
        assert jspContext != null;

        Object val = jspContext.getAttribute(KEY);
        if(val != null) {
            DataAccessProviderStack curStack = (DataAccessProviderStack)val;
            DataAccessProviderBean lastTop = curStack.pop();

            if(!curStack.isEmpty())
                jspContext.setAttribute("container", curStack.peek());
            else
                jspContext.removeAttribute("container");

            return lastTop;
        }

        // todo: should this throw an IllegalStateException?

        return null;
    }

    public DataAccessProviderStack() {
        _stack = new Stack();
    }

    public boolean isEmpty() {
        assert _stack != null;
        return _stack.empty();
    }

    public DataAccessProviderBean peek() {
        assert _stack != null;
        return (DataAccessProviderBean)_stack.peek();
    }

    public DataAccessProviderBean pop() {
        assert _stack != null;
        return (DataAccessProviderBean)_stack.pop();
    }

    public void push(DataAccessProviderBean bean) {
        assert _stack != null;
        _stack.push(bean);
    }

}
