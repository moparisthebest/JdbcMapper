/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
*/
package org.apache.beehive.samples.netui.actioninterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptorContext;
import org.apache.beehive.netui.pageflow.interceptor.action.AfterNestedInterceptContext;
import org.apache.beehive.netui.pageflow.interceptor.action.InterceptorForward;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;

/**
 * This class is used by the "actioninterceptors" sample.  It is registered in
 * WEB-INF/beehive-netui-config.xml.
 */
public class MyInterceptor extends ActionInterceptor {
    /**
     * Return a URI to a nested page flow, which will be "injected" before the desired action is run.
     */
    public void preAction(ActionInterceptorContext context, InterceptorChain chain)
        throws InterceptorException {
        System.out.println("in preAction() in " + getClass().getName());
        InterceptorForward fwd = new InterceptorForward("/advanced/actioninterceptors/nested/Controller.jpf");
        setOverrideForward(fwd, context);
        chain.continueChain();
    }

    /**
     * This is called after our "injected" nested page flow is done, and before the original desired
     * action is run.
     */
    public void afterNestedIntercept(AfterNestedInterceptContext context) throws InterceptorException {
        System.out.println("in afterNestedIntercept() in " + getClass().getName());
    }

    /**
     * This is called after the original desired action is run.
     */
    public void postAction(ActionInterceptorContext context, InterceptorChain chain)
        throws InterceptorException {
        System.out.println("in postAction() in " + getClass().getName());
        chain.continueChain();
    }
}
