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
package org.apache.beehive.netui.pageflow.interceptor.action;

import org.apache.beehive.netui.pageflow.PageFlowController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

/**
 * Context passed to {@link ActionInterceptor#afterNestedIntercept}.
 */ 
public class AfterNestedInterceptContext
        extends ActionInterceptorContext
{
    private String _returnAction;

    public AfterNestedInterceptContext( HttpServletRequest request, HttpServletResponse response,
                                        ServletContext servletContext, PageFlowController controller,
                                        InterceptorForward originalForward, String actionName,
                                        String returnAction )
    {
        super( request, response, servletContext, controller, originalForward, actionName );
        _returnAction = returnAction;
    }
    
    /**
     * Get the return action from the nested page flow that intercepted the original page flow's action.
     */ 
    public String getReturnAction()
    {
        return _returnAction;
    }
}
