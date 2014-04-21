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
package org.apache.beehive.netui.pageflow.xmlhttprequest;

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.core.chain.web.WebChainContext;
import org.apache.beehive.netui.core.chain.CatalogFactory;
import org.apache.beehive.netui.core.chain.Catalog;
import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.core.chain.Chain;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.Interceptors;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.internal.DefaultURLRewriter;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.internal.ServletUtils;
import org.apache.beehive.netui.util.config.bean.CatalogConfig;
import org.apache.beehive.netui.util.config.ConfigUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;

/**
 * Servlet to handle XMLHttpRequests sent from a client.
 */ 
public class XmlHttpRequestServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getInstance(XmlHttpRequestServlet.class);
    private static final String COMMAND_XHR = "xhr-commands";

    public void init() throws ServletException
    {
        ServletContext ctxt = getServletContext();

        /* Initialize the Command chain and add the ErrorCRI */

        Chain xhrServletCommand = null;
        CatalogConfig catalogConfig = ConfigUtil.getConfig().getCatalogConfig();
        if(catalogConfig != null) {
            /*
            todo: neaten up this initialization process.  because of the separation between
                  parsing and configuration, this is a second step.
                  Need to put this somewhere in the framework, preferably in a single
                  place that initializes the PageFlow runtime.
             */
            CatalogFactory catalogFactory = CatalogFactory.getInstance();
            if(catalogFactory.getCatalog() == null)
                catalogFactory = CatalogFactory.getInstance(catalogConfig);

            assert catalogFactory != null;
            Catalog catalog = catalogFactory.getCatalog();
            if(catalog != null) {
                xhrServletCommand = (Chain)catalog.getCommand(COMMAND_XHR);
            }

            if(xhrServletCommand != null) {
                xhrServletCommand.addCommand(new ErrorCRI());
            }
        }

        /*
        For compatibility, add the ErrorCRI to the list of global Interceptors only if the
        Command for the .xhr servlet wasn't found.  Note, this code will <em>disappear</em>
        in a subsequent release of Beehive.
         */
        if(xhrServletCommand != null) {
            RequestInterceptorContext.init(ctxt);
            RequestInterceptorContext.addInterceptor(ctxt, new ErrorCRI());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        //System.err.println("Inside the XmlHppRequestServlet:" + request.getRequestURI());

        // Register the default URLRewriter
        URLRewriterService.registerURLRewriter(0, request, new DefaultURLRewriter());

        ServletContext ctxt = getServletContext();
        Command xhrServletCommand = null;
        CatalogFactory catalogFactory = CatalogFactory.getInstance();
        if(catalogFactory != null && catalogFactory.getCatalog() != null) {
            xhrServletCommand = catalogFactory.getCatalog().getCommand(COMMAND_XHR);
        }

        // execute the Command if found or the interceptors if found
        if(xhrServletCommand != null) {
            /* todo: add a chain to create the Context object */
            WebChainContext webChainContext = new WebChainContext(ctxt, request, response);

            try {
                xhrServletCommand.execute(webChainContext);
            }
            catch(Exception e) {
                ServletUtils.throwServletException(e);
            }
        }
        else {
            RequestInterceptorContext context = new RequestInterceptorContext(request, response, ctxt);
            List interceptors = context.getRequestInterceptors();

            try {
                Interceptors.doPreIntercept(context, interceptors);
            }
            catch (InterceptorException e) {
                ServletUtils.throwServletException(e);
            }
        }

        /*
        Note that we're not worrying about post-intercept or whether any of the pre-interceptors cancelled the
        request, since there's no further processing in the request.
        */
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

    /**
     * Implementation of a {@link RequestInterceptor} and a {@link Command} that handle errors
     * sent from the client.
     *
     * Note, this class <i>should not</i> be referenced by user code; it is added by the framework.
     */
    class ErrorCRI
        extends RequestInterceptor
        implements Command
    {
        public void preRequest(RequestInterceptorContext ctxt, InterceptorChain chain)
            throws InterceptorException {

            HttpServletRequest request = ctxt.getRequest();
            String cmd = parseCommand(request);
            render(cmd, request);
            chain.continueChain();
        }

        public void postRequest(RequestInterceptorContext context, InterceptorChain chain)
            throws InterceptorException {
            chain.continueChain();
        }

        public boolean execute(Context context)
            throws Exception {

            assert context != null;
            assert context instanceof WebChainContext;
            assert ((WebChainContext)context).getServletRequest() instanceof HttpServletRequest;
            assert ((WebChainContext)context).getServletResponse() instanceof HttpServletResponse;

            WebChainContext webChainContext = (WebChainContext)context;
            HttpServletRequest request = (HttpServletRequest)webChainContext.getServletRequest();

            String cmd = parseCommand(request);
            return render(cmd, request);
        }

        private String parseCommand(HttpServletRequest request) {
            // Create the command by striping off the context path and the extension
            String cmd = request.getRequestURI();
            String ctxtPath = request.getContextPath();

            // catch any runtime errors here and return.
            try {
                cmd = cmd.substring(ctxtPath.length() + 1);
                int idx = cmd.lastIndexOf('.');
                if (idx != -1) {
                    cmd = cmd.substring(0, idx);
                }
            }
            catch (RuntimeException e) {
                LOG.error("Runtime Error creating XmlRequestServlet Command:" + e.getClass().getName(),e);

                // set the command string to null to prevent the ErrorCRI command handler from executing
                cmd = null;
            }

            return cmd;
        }

        private boolean render(String cmd, HttpServletRequest request) {
            try {
                if ("netuiError".equals(cmd)) {
                    String error = request.getParameter("error");
                    LOG.error("NetUI JavaScript Error:" + error);
                    //System.err.println("NetUI JavaScript Error:" + error);
                    return true;
                }
            }
            catch (RuntimeException e) {
                LOG.error("Runtime Error creating XmlRequestServlet Command:" + e.getClass().getName(),e);
            }

            /*
             If the command wasn't a "netuiError", don't assume that it was handled and let
             any additional Commands have a chance to take some action in response to
             an error.
              */
            return false;
        }

    }
}
