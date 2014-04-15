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
package org.apache.beehive.netui.simpletags.divpanel;

import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext;
import org.apache.beehive.netui.pageflow.requeststate.INameable;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.simpletags.ajax.AbstractClientRequestInterceptor;

import javax.servlet.http.HttpServletRequest;

public class DivPanelCRI extends AbstractClientRequestInterceptor
{
    private static final String SWITCH_PAGE = "switchPage";

    public void preRequest(RequestInterceptorContext ctxt, InterceptorChain chain)
    {
        HttpServletRequest request = ctxt.getRequest();

        // Create the command by striping off the context path and the extension
        String uri = request.getRequestURI();
        String ctxtPath = request.getContextPath();

        String cmd = getCommand(uri, ctxtPath);


        // check to see if we handle this command
        if (SWITCH_PAGE.equals(cmd)) {
            handlePageSwitch(request);
        }

    }

    private void handlePageSwitch(HttpServletRequest req)
    {
        String dp = req.getParameter("divPanel");
        String fp = req.getParameter("firstPage");
        //System.err.println("DivPanelBehavior Command: switch, DivPanelBehavior:" + dp + " Node:" + fp);

        NameService ns = NameService.instance(req.getSession());
        assert(ns != null);

        // get the tree from the name service
        INameable n = ns.get(dp);
        if (n == null) {
            System.err.println("DivPanelBehavior '" + dp + "' was not found in the NameService");
            return;
        }
        if (!(n instanceof DivPanelState)) {
            System.err.println("Named dp was not an instance of a DivPanelState");
            return;
        }

        DivPanelState state = (DivPanelState) n;
        state.setFirstPage(fp);
    }

    public void postRequest(RequestInterceptorContext context, InterceptorChain chain) throws InterceptorException
    {
        chain.continueChain();
    }
}
