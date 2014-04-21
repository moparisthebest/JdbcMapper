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
package org.apache.beehive.netui.pageflow.handler;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ExceptionConfig;
import org.apache.beehive.netui.pageflow.PreviousPageInfo;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowStack;
import org.apache.beehive.netui.pageflow.config.PageFlowExceptionConfig;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;

/**
 * Handler for action forward processing.
 */ 
public interface ActionForwardHandler
        extends Handler
{
    /**
     * Perform additional processing on a given Struts ActionForward, and perform any necessary updates to the request
     * and user session (including updates to the PageFlowController nesting stack).  This method may <em>replace</em>
     * the given ActionForward with a new one.
     * 
     * @param context the current FlowControllerHandlerContext.
     * @param fwd the ActionForward object to process.
     * @param actionMapping the Struts config object for the current action, if there is one (<code>null</code> 
     *            if there is none).
     * @param exceptionConfig the Struts config object for the current exception-handler, if one is being run
     *            <code>null</code> if there is none).
     * @param actionName the name of the currently-requested action.
     * @param altModuleConfig an alternate Struts module configuration object for resolving a forward, if it can't be
     *            resolved from the current ActionMapping (or if there is no current ActionMapping).
     * @param form the Struts ActionForm created for the current action.  May be <code>null</code>.
     * @return the modified ActionForward object, or a replacement one.
     */
    ActionForward processForward( FlowControllerHandlerContext context, ActionForward fwd, ActionMapping actionMapping, 
                                  ExceptionConfig exceptionConfig, String actionName,
                                  ModuleConfig altModuleConfig, ActionForm form );

    ActionForward doAutoViewRender( FlowControllerHandlerContext context, ActionMapping mapping, ActionForm form );

    ActionForward doReturnToPage( FlowControllerHandlerContext context, PreviousPageInfo prevPageInfo,
                                  PageFlowController currentPageFlow, ActionForm currentForm,
                                  String actionName, Forward pageFlowFwd );

    ActionForward doReturnToAction( FlowControllerHandlerContext context, String actionName, Forward pageFlowFwd );

    ActionForward doNestingReturn( FlowControllerHandlerContext context, Forward pageFlowFwd, 
                                   ActionMapping mapping, ActionForm form );

    ActionForward handleInterceptorReturn( FlowControllerHandlerContext context, PageFlowController poppedPageFlow,
                                           PageFlowStack.PushedPageFlow pushedPageFlowWrapper, String returnAction,
                                           ActionMapping actionMapping, ActionForm form, ActionInterceptor interceptor );
}
