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
package org.apache.beehive.netui.tags.tree;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext;
import org.apache.beehive.netui.pageflow.requeststate.INameable;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.TagConfig;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.core.chain.web.WebChainContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.Writer;

public class TreeCRI
    extends RequestInterceptor
    implements Command
{
    private static final String TREE_COLLAPSE = "treeCollapse";
    private static final String TREE_EXPAND = "treeExpand";

    private static final Logger logger = Logger.getInstance(TreeCRI.class);

    // The elements we will create in the document
    private static final String TREE_EXPAND_ELEM = "treeExpand";

    /**
     * Implementation of the {@link Command} interface for using this class as part of a
     * Chain of Responsibility.
     *
     * @param context the Chain's context object
     * @return <code>true</code> if the request was handled by this command; <code>false</code> otherwise
     * @throws Exception any exception that is throw during processing
     */
    public boolean execute(Context context)
        throws Exception {

        assert context != null;
        assert context instanceof WebChainContext;
        assert ((WebChainContext)context).getServletRequest() instanceof HttpServletRequest;
        assert ((WebChainContext)context).getServletResponse() instanceof HttpServletResponse;

        WebChainContext webChainContext = (WebChainContext)context;
        HttpServletRequest request = (HttpServletRequest)webChainContext.getServletRequest();
        HttpServletResponse response = (HttpServletResponse)webChainContext.getServletResponse();

        String cmd = parseCommand(request.getRequestURI());
        return render(request, response, webChainContext.getServletContext(), cmd);
    }

    /**
     * Implementation of the {@link RequestInterceptor#preRequest(org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext, org.apache.beehive.netui.pageflow.interceptor.InterceptorChain)}
     * method for using this class as part of a request interceptor chain.
     *
     * @param ctxt the interceptor's context object
     * @param chain the interceptor chain
     * @throws InterceptorException any exception thrown during processing
     */
    public void preRequest(RequestInterceptorContext ctxt, InterceptorChain chain) throws InterceptorException
    {
        HttpServletRequest request = ctxt.getRequest();

        String cmd = parseCommand(request.getRequestURI());
        render(request, ctxt.getResponse(), ctxt.getServletContext(), cmd);

        chain.continueChain();
    }

    /**
     * Implementation of the {@link RequestInterceptor#postRequest(org.apache.beehive.netui.pageflow.interceptor.request.RequestInterceptorContext, org.apache.beehive.netui.pageflow.interceptor.InterceptorChain)}
     * method for using this class as part of a request interceptor chain.
     *
     * @param context the interceptor's context object
     * @param chain the interceptor chain
     * @throws InterceptorException any exception thrown during processing
     */
    public void postRequest(RequestInterceptorContext context, InterceptorChain chain) throws InterceptorException
    {
        chain.continueChain();
    }

    private String parseCommand(String cmd) {
        // Create the command by striping off the context path and the extension
        // catch any runtime errors here and return.
        try {
            int dot = cmd.lastIndexOf('.');
            int slash = cmd.lastIndexOf('/');
            if (dot != -1 && slash != -1 && slash < dot) {
                cmd = cmd.substring(slash + 1, dot);
            }
        }
        catch (RuntimeException e) {
            System.err.println("Runtime Error creating XmlRequestServlet Command:" + e.getClass().getName());
        }

        return cmd;
    }

    private boolean render(HttpServletRequest request,
                           HttpServletResponse response,
                           ServletContext servletContext,
                           String cmd) {

        // check to see if we handle this command
        if (TREE_COLLAPSE.equals(cmd)) {
            handleExpandCollapse(false, request, response, servletContext);
            return true;
        }
        else if (TREE_EXPAND.equals(cmd)) {
            handleExpandCollapse(true, request, response, servletContext);
            return true;
        }

        return false;
    }

    private void handleExpandCollapse(boolean expand,
                                      HttpServletRequest req,
                                      HttpServletResponse response,
                                      ServletContext ctxt)
    {
        // create an XML empty document, that isn't cached on the client
        response.setContentType("text/xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");

        String tree = req.getParameter("tree");
        String node = req.getParameter("node");
        String expandSvr = req.getParameter("expandOnServer");
        //System.err.println("TreeCommand:" + ((expand) ? "expand" : "collapse") +
        //        " Tree:" + tree + " Node:" + node + " expandSvr:" + expandSvr);

        NameService ns = NameService.instance(req.getSession());
        assert(ns != null);

        // get the tree from the name service
        INameable n = ns.get(tree);
        if (n == null) {
            logger.error("Tree '" + tree + "' was not found in the NameService");
            return;
        }
        if (!(n instanceof ITreeRootElement)) {
            logger.error("Named Tree was not an instance of a ITreeRootElement");
            return;
        }

        ITreeRootElement root = (ITreeRootElement) n;
        TreeElement elem = ((TreeElement) root).findNode(node);
        if (elem == null) {
            System.err.println("Element '" + node + "' not found in the tree");
            return;
        }
        elem.onExpand(req, response);
        elem.setExpanded(expand);

        if (expandSvr != null) {
            InternalStringBuilder sb = new InternalStringBuilder(1024);
            StringBuilderRenderAppender writer = new StringBuilderRenderAppender(sb);
            // Start the document
            writeStartElement(writer, TREE_EXPAND_ELEM);

            // add a tree identifier
            writeElement(writer, "node", node);
            writeElement(writer, "treeId", tree);

            try {
                TreeElement children[] = elem.getChildren();
                AttributeRenderer extraAttrs = new AttributeRenderer();
                int newLevel = elem.getLevel() + 1;
                InternalStringBuilder nodeSB = new InternalStringBuilder();
                StringBuilderRenderAppender childRendering = new StringBuilderRenderAppender(nodeSB);
                TreeElement tmp = elem;
                InheritableState iState = null;
                while (iState == null && tmp != null) {
                    iState = tmp.getInheritableState();
                    tmp = tmp.getParent();
                }
                if (iState == null) {
                    System.err.println("Unable to find InheritableState");
                    iState = root.getInheritableState();
                }

                TreeRenderState trs = root.getTreeRenderState();
                String treeRendererClassName = TagConfig.getTreeRendererClassName();
                TreeRenderer tr = TreeRendererFactory.getInstance(treeRendererClassName);
                if (tr == null) {
                    tr = new TreeRenderer();
                }
                tr.init(trs, req, (HttpServletResponse) response, ctxt);
                tr.setTreeRenderSupport(new ServletTreeRenderSupport(writer, nodeSB));
                for (int i = 0; i < children.length; i++) {
                    nodeSB.setLength(0);
                    tr.render(childRendering, children[i], newLevel, extraAttrs, iState);
                }
            }
            catch (JspException se) {
                //se.printStackTrace();
                logger.error("Received a JSP excpetion Rendering the Tree", se);
                return;
            }
            catch (RuntimeException re) {
                //re.printStackTrace();
                logger.error("Received a JSP excpetion Rendering the Tree", re);
                return;
            }

            // add the tree text
            writeEndElement(writer, TREE_EXPAND_ELEM);
            write(response, sb.toString());
        }
    }

    public static void writeStartElement(AbstractRenderAppender writer, String elementName)
    {
        writer.append("<");
        writer.append(elementName);
        writer.append(">");

    }

    public static void writeEndElement(AbstractRenderAppender writer, String elementName)
    {
        writer.append("</");
        writer.append(elementName);
        writer.append(">");
    }

    public static void writeElement(AbstractRenderAppender writer, String elementName, String value)
    {
        writeStartElement(writer, elementName);
        writer.append(value);
        writeEndElement(writer, elementName);

    }

    private void write(ServletResponse response, String string)
    {
        try {
            Writer writer = response.getWriter();
            writer.write(string);
        }
        catch (IOException e) {
        }
    }
}
