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
package org.apache.beehive.netui.tags.internal;

import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.core.chain.web.WebChainContext;
import org.apache.beehive.netui.pageflow.PageFlowConstants;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext;
import org.apache.beehive.netui.pageflow.internal.ViewRenderer;
import org.apache.beehive.netui.tags.AbstractClientRequestInterceptor;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * An object to handle a command for rendering framework generated output.
 */
public final class ViewRendererCRI
    extends AbstractClientRequestInterceptor
    implements Command
{
    private static final Logger logger = Logger.getInstance(ViewRendererCRI.class);

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

        return handleCommand(request, response, webChainContext.getServletContext(), cmd);
    }

    public void preRequest(RequestInterceptorContext ctxt, InterceptorChain chain) throws InterceptorException
    {
        HttpServletRequest request = ctxt.getRequest();

        // Create the command by striping off the context path and the extension
        String uri = request.getRequestURI();
        String ctxtPath = request.getContextPath();
        String cmd = getCommand(uri, ctxtPath);

        handleCommand(request, ctxt.getResponse(), ctxt.getServletContext(), cmd);

        chain.continueChain();
    }

    public void postRequest(RequestInterceptorContext context, InterceptorChain chain) throws InterceptorException
    {
        chain.continueChain();
    }

    private boolean handleCommand(HttpServletRequest request,
                                  HttpServletResponse response,
                                  ServletContext servletContext,
                                  String command) {
        if (PageFlowConstants.VIEW_RENDERER_URI_COMMAND.equals(command)) {
            handleViewRendering(request, response, servletContext);
            return true;
        }
        else return false;
    }

    private void handleViewRendering(HttpServletRequest request,
                                     ServletResponse response,
                                     ServletContext servletContext)
    {
        // get the view renderer from the request attribute
        Object obj = request.getAttribute(PageFlowConstants.VIEW_RENDERER_ATTRIBUTE_NAME);
        if (obj == null) {
            logger.error("ViewRenderer was not found in the request attributes");
            return;
        }
        if (!(obj instanceof ViewRenderer)) {
            logger.error("Named view renderer was not an instance of a ViewRenderer");
            return;
        }

        ViewRenderer vr = (ViewRenderer) obj;

        // render the response
        try {
            /*
             * Note, this is our own framework response and just javascript
             * that calls a javascript routine in another window, passing
             * Unicode String values in a map.
             * For now, we're setting the content type, but this may be a
             * problem for people who whant to control the encoding and the
             * content type themselves, as with a JSP.
             * If so, then they can implement their own Command to handle the
             * ViewRenderer objects.
             */
            response.setContentType("text/html; charset=UTF-8");

            vr.renderView(request, response, servletContext);
            request.removeAttribute(PageFlowConstants.VIEW_RENDERER_ATTRIBUTE_NAME);
        }
        catch (IOException ioe) {
            logger.error("Named view renderer was not able to render.  Cause: " + ioe, ioe);
        }
    }
}
