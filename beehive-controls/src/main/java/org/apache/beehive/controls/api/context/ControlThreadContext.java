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
package org.apache.beehive.controls.api.context;

import java.util.Stack;

/**
 * The ControlThreadContext class manages the association between ControlContainerContexts 
 * and threads of execution.   For a given thread of execution, the beginning and ending of
 * contexts will always be nested (never interleaved), so each thread will maintain its own 
 * stack of currently executing contexts.  This can be used to reassociate with the current 
 * active context.
 */
public class ControlThreadContext
{
    /**
     * This thread local maintains a per-thread stack of ControlContainerContext instances.
     */
    private static ThreadLocal<Stack<ControlContainerContext>> _threadContexts =
                                            new ThreadLocal<Stack<ControlContainerContext>>(); 

    /**
     * Returns the active ControlContainerContext for the current thread, or null if no
     * context is currently active.
     * @return the current active ControlContainerContext
     */ 
    public static ControlContainerContext getContext() 
    {
        Stack<ControlContainerContext> contextStack = _threadContexts.get();
        if (contextStack == null || contextStack.size() == 0)
            return null;

        return contextStack.peek();
    }

    /**
     * Defines the beginning of a new control container execution context.
     */
    public static void beginContext(ControlContainerContext context)
    {
        Stack<ControlContainerContext> contextStack = _threadContexts.get();
        if (contextStack == null)
        {
            contextStack = new Stack<ControlContainerContext>();
            _threadContexts.set(contextStack);
        }
        contextStack.push(context);
    }

    /**
     * Ends the current control container execution context
     * @throws IllegalStateException if there is not current active context or it is not
     *         the requested context. 
     */
    public static void endContext(ControlContainerContext context)
    {
        Stack<ControlContainerContext> contextStack = _threadContexts.get();
        if (contextStack == null || contextStack.size() == 0)
            throw new IllegalStateException("No context started for current thread");

        if (contextStack.peek() != context)
            throw new IllegalStateException("Context is not the current active context");

        contextStack.pop();
    }
}
