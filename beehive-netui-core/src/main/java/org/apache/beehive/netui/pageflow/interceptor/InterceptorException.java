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
 * Exception thrown while invoking {@link Interceptor}s.
 */ 
public class InterceptorException
    extends Exception
{
    /**
     * Default constructor.
     */
    public InterceptorException() {
    }

    /**
     * Exception with message.
     * @param message
     */
    public InterceptorException( String message )
    {
        super( message );
    }

    /**
     * Exception with message and cause.
     * @param message
     * @param cause
     */
    public InterceptorException( String message, Throwable cause )
    {
        super( message, cause );
    }

    /**
     * Exception with cause.
     * @param cause
     */
    public InterceptorException( Throwable cause )
    {
        super( cause );
    }
}
