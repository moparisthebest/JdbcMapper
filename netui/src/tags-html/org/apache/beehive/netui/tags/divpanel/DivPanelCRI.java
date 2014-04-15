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
package org.apache.beehive.netui.tags.divpanel;

import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext;
import org.apache.beehive.netui.pageflow.requeststate.INameable;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.AbstractClientRequestInterceptor;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.web.WebChainContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DivPanelCRI
    extends AbstractClientRequestInterceptor
    implements Command
{
    private static final String SWITCH_PAGE = "switchPage";

    public boolean execute(Context context)
        throws Exception {

        assert context != null;
        assert context instanceof WebChainContext;
        assert ((WebChainContext)context).getServletRequest() instanceof HttpServletRequest;
        assert ((WebChainContext)context).getServletResponse() instanceof HttpServletResponse;

        WebChainContext webChainContext = (WebChainContext)context;
        HttpServletRequest request = (HttpServletRequest)webChainContext.getServletRequest();
        HttpServletResponse response = (HttpServletResponse)webChainContext.getServletResponse();

        // Create the command by striping off the context path and the extension
        String uri = request.getRequestURI();
        String ctxtPath = request.getContextPath();

        String cmd = getCommand(uri, ctxtPath);

        return handleCommand(cmd, request, response);
    }

    public void preRequest(RequestInterceptorContext ctxt, InterceptorChain chain) throws InterceptorException
    {
        HttpServletRequest request = ctxt.getRequest();
        HttpServletResponse response = ctxt.getResponse();

        // Create the command by striping off the context path and the extension
        String uri = request.getRequestURI();
        String ctxtPath = request.getContextPath();

        String cmd = getCommand(uri, ctxtPath);

        handleCommand(cmd, request, response);

        chain.continueChain();
    }

    public void postRequest(RequestInterceptorContext context, InterceptorChain chain) throws InterceptorException
    {
        chain.continueChain();
    }

    private boolean handleCommand(String command, HttpServletRequest request, HttpServletResponse response) {
        if (SWITCH_PAGE.equals(command)) {
            handlePageSwitch(request, response);
            return true;
        }
        else return false;
    }

    private void handlePageSwitch(HttpServletRequest request, HttpServletResponse response)
    {
        // create an XML empty document, that isn't cached on the client
        response.setContentType("text/xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");

        String dp = request.getParameter("divPanel");
        String fp = request.getParameter("firstPage");
        //System.err.println("DivPanel Command: switch, DivPanel:" + dp + " Node:" + fp);

        NameService ns = NameService.instance(request.getSession());
        assert(ns != null);

        // get the DivPanel from the name service
        INameable n = ns.get(dp);
        if (n == null) {
            System.err.println("DivPanel '" + dp + "' was not found in the NameService");
            return;
        }
        if (!(n instanceof DivPanelState)) {
            System.err.println("Named dp was not an instance of a DivPanelState");
            return;
        }

        DivPanelState state = (DivPanelState) n;
        state.setFirstPage(fp);
    }
}
