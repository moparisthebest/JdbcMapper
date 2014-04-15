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
package org.apache.beehive.netui.pageflow.interceptor;

import java.util.List;
import java.util.LinkedList;

/**
 * <p>
 * Abstract base class that represents an interceptor chain.
 * </p>
 */ 
public abstract class InterceptorChain
{
    private LinkedList/*< Interceptor >*/ _chain = new LinkedList/*< Interceptor >*/();
    private InterceptorContext _context;

    /**
     * Create an interceptor chain with the {@link InterceptorContext} and a {@link List} of
     * interceptors.
     *
     * @param context the context
     * @param interceptors the interceptors
     */
    protected InterceptorChain( InterceptorContext context, List/*< Interceptor >*/ interceptors )
    {
        _context = context;
        _chain.addAll( interceptors );
    }

    /**
     * Execute the next interceptor in the chain of interceptors.
     *
     * @return the object returned when the interceptor is invoked
     * @throws InterceptorException the exception thrown if an error occurs while invoking the interceptor
     */
    public Object continueChain()
        throws InterceptorException
    {
        if ( ! _chain.isEmpty() )
        {
            return invoke( ( Interceptor ) _chain.removeFirst() );
        }
        else
        {
            return null;
        }
    }

    /**
     * Invoke an interceptor.
     *
     * @param interceptor the interceptor to invoke
     * @return the interceptor's return value
     * @throws InterceptorException the exception thrown if an error occurs while invoking the interceptor
     */
    protected abstract Object invoke( Interceptor interceptor ) throws InterceptorException;

    /**
     * Get the {@link InterceptorContext}
     * @return the context
     */
    public InterceptorContext getContext()
    {
        return _context;
    }

    /**
     * Check to see if the interception chain is empty.
     * @return <code>true</code> if the chain is empty; <code>false</code> otherwise.
     */
    public boolean isEmpty()
    {
        return _chain.isEmpty();
    }

    /**
     * Remove the first {@link Interceptor} that is currently the first interceptor in the chain.
     * This method can be used to advance to the "next" interceptor in the chain when executing
     * a chain of interceptors.
     * @return the first interceptor if one exists
     */
    protected Interceptor removeFirst()
    {
        return ( Interceptor ) _chain.removeFirst();
    }
}
