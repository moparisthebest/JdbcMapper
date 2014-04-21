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

/**
 * <p>
 * Convenience utility used to execute an interceptor chain given an {@link InterceptorContext}.  This
 * class can be invoked to execute "pre" and "post" interceptor chains.
 * </p>
 */
public class Interceptors
{
    /**
     * Execute a "pre" interceptor chain.  This will execute the
     * {@link Interceptor#preInvoke(InterceptorContext, InterceptorChain)}
     * method to be invoked on each interceptor in a chain.
     *
     * @param context the context for a set of interceptors
     * @param interceptors the list of interceptors
     * @throws InterceptorException
     */
    public static void doPreIntercept( InterceptorContext context, List/*< Interceptor >*/ interceptors )
            throws InterceptorException
    {
        if ( interceptors != null )
        {
            PreInvokeInterceptorChain chain = new PreInvokeInterceptorChain( context, interceptors );
            chain.continueChain();
        }
    }

    /**
     * Execute a "post" interceptor chain.  This will execute the
     * {@link Interceptor#postInvoke(InterceptorContext, InterceptorChain)}
     * method to be invoked on each interceptor in a chain.
     * @param context the context for a set of interceptors
     * @param interceptors the list of interceptors
     * @throws InterceptorException
     */
    public static void doPostIntercept( InterceptorContext context, List/*< Interceptor >*/ interceptors )
        throws InterceptorException
    {
        if ( interceptors != null )
        {
            PostInvokeInterceptorChain chain = new PostInvokeInterceptorChain( context, interceptors );
            chain.continueChain();
        }
    }

    /**
     * Utility class used to configure an {@link InterceptorChain} and invoke an interceptor's
     * {@link Interceptor#preInvoke(InterceptorContext, InterceptorChain)} method.
     */
    private static final class PreInvokeInterceptorChain
        extends InterceptorChain
    {
        public PreInvokeInterceptorChain( InterceptorContext context, List/*< Interceptor >*/ interceptors )
        {
            super( context, interceptors );
        }
        
        protected Object invoke( Interceptor interceptor )
                throws InterceptorException
        {
            interceptor.preInvoke( getContext(), this );
            return null;
        }
    }

    /**
     * Utility class used to configure an {@link InterceptorChain} and invoke an interceptor's
     * {@link Interceptor#postInvoke(InterceptorContext, InterceptorChain)} method.
     */
    private static final class PostInvokeInterceptorChain
        extends InterceptorChain
    {
        public PostInvokeInterceptorChain( InterceptorContext context, List/*< Interceptor >*/ interceptors )
        {
            super( context, interceptors );
        }
        
        protected Object invoke( Interceptor interceptor )
                throws InterceptorException
        {
            interceptor.postInvoke( getContext(), this );
            return null;
        }
    }
}