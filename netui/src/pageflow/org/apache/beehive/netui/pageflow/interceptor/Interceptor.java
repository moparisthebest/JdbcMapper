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

/**
 * <p>
 * Base interface for all NetUI / Page Flow interceptors.  An interceptor consists of an
 * initialization step and two blocks of code that run before and after some block of code
 * or event that is being intercepted.
 * </p>
 */ 
public interface Interceptor
{
    /**
     * Method used to initialize the interceptor.
     *
     * @param config the {@link InterceptorConfig} object
     */
    public void init( InterceptorConfig config );

    /**
     * Method invoked during "pre" interception of some block of code / event.
     *
     * @param context the interception context
     * @param chain the chain of interceptors
     * @throws InterceptorException an exception thrown when an error occurs during interception
     */
    public void preInvoke( InterceptorContext context, InterceptorChain chain )
        throws InterceptorException;

    /**
     * Method invoked during "post" interception of some block of code / event.
     *
     * @param context the interceptor context
     * @param chain the chain of interceptors
     * @throws InterceptorException an exception thrown when an error occurs during interception
     */
    public void postInvoke( InterceptorContext context, InterceptorChain chain )
        throws InterceptorException;
}

