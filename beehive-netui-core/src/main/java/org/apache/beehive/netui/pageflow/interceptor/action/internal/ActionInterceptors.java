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
package org.apache.beehive.netui.pageflow.interceptor.action.internal;

import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorContext;
import org.apache.beehive.netui.pageflow.interceptor.Interceptor;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptorContext;
import org.apache.struts.action.ActionForward;

import javax.servlet.ServletException;
import java.util.List;
import java.io.IOException;

public class ActionInterceptors
{
    private static final class WrapActionInterceptorChain
        extends InterceptorChain
    {
        private ActionExecutor _actionExecutor;
        
        public WrapActionInterceptorChain( InterceptorContext context, List/*< Interceptor >*/ interceptors,
                                           ActionExecutor actionExecutor )
        {
            super( context, interceptors );
            _actionExecutor = actionExecutor;
        }
        
        protected Object invoke( Interceptor interceptor )
                throws InterceptorException
        {
            return ( ( ActionInterceptor ) interceptor ).wrapAction( ( ActionInterceptorContext ) getContext(), this );
        }

        public Object continueChain()
                throws InterceptorException
        {
            if ( ! isEmpty() )
            {
                return invoke( removeFirst() );
            }
            else
            {
                try
                {
                    return _actionExecutor.execute();
                }
                catch ( ServletException e )
                {
                    throw new InterceptorException( e );
                }
                catch ( IOException e )
                {
                    throw new InterceptorException( e );
                }
            }
        }
    }
    
    public static ActionForward wrapAction( ActionInterceptorContext context, List/*< Interceptor >*/ interceptors,
                                            ActionExecutor actionExecutor )
        throws InterceptorException, IOException, ServletException
    {
        try
        {
            if ( interceptors != null )
            {
                WrapActionInterceptorChain chain = new WrapActionInterceptorChain( context, interceptors, actionExecutor );
                return ( ActionForward ) chain.continueChain();
            }
            else
            {
                return actionExecutor.execute();
            }
        }
        catch ( InterceptorException e )
        {
            Throwable cause = e.getCause();
            
            if ( cause instanceof ServletException )
            {
                throw ( ServletException ) cause;
            }
            else if ( cause instanceof IOException )
            {
                throw ( IOException ) cause;
            }
            
            throw e;
        }
    }
    
    public interface ActionExecutor
    {
        public ActionForward execute() throws IOException, ServletException, InterceptorException;
    }
}
