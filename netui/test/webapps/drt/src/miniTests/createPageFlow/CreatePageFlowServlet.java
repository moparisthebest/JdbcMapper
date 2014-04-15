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
package miniTests.createPageFlow;

import org.apache.beehive.netui.pageflow.FlowControllerFactory;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.RequestContext;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.ActionResult;
import org.apache.beehive.netui.pageflow.internal.JavaControlUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedResponse;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This servlet creates a page flow containing a control by calling
 * FlowControllerFactory.createPageFlow() to create a page flow outside of
 * the usual PageFlowRequestProcessor or page filter. 
 */
public class CreatePageFlowServlet
    extends HttpServlet
{
    private static String initParam = "initializeControlContext";
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(response.getOutputStream(), "UTF8")));

        boolean doInitialization = false;
        String value = request.getParameter(initParam);
        if (value != null && value.equals("true")) {
            doInitialization = true;
        }

        outputPageStart(out);

        ServletContext servletContext = getServletContext();
        String pageFlow = "miniTests.createPageFlow.Controller";
        String pageFlowURI = "/miniTests/createPageFlow/Controller.jpf";
        ActionResult actionResult = null;
        try
        {
            ScopedRequest scopedRequest =
                    ScopedServletUtils.getScopedRequest(request, null, servletContext, "test", true);
            scopedRequest.setRequestURI(request.getContextPath() + pageFlowURI);
            ScopedResponse scopedResponse =
                    ScopedServletUtils.getScopedResponse(response, scopedRequest);

            if (doInitialization) {
                // Initialize the ControlBeanContext in the session.
                //JavaControlUtils.initializeControlContext( request, response, servletContext );
            }

            RequestContext requestContext = new RequestContext(scopedRequest, scopedResponse);
            PageFlowController pfc =
                    FlowControllerFactory.get(servletContext).createPageFlow(requestContext, pageFlow);

            String action = "begin";
            String modulePath = (pfc != null ? pfc.getModulePath() : null);
            action = modulePath + "/" + action;
            actionResult = PageFlowUtils.strutsLookup( servletContext, scopedRequest, scopedResponse, action, null );

            if (actionResult != null) {
                if ( actionResult.isError() ) {
                    out.println("<h3>ActionResult is Error</h3>");
                }

                RequestDispatcher rd = scopedRequest.getRequestDispatcher(actionResult.getURI());
                rd.include(scopedRequest, scopedResponse);
            }
            else {
                out.println("<h3>ActionResult is null</h3>");
            }
        }
        catch (Exception ex)
        {
            out.println("<h3>Exception:</h3>");
            out.println("<br>");
            out.println("The following exception was caught...");
            out.println("<br>");
            out.println(ex.getMessage());
            out.println("<pre>");
            ex.printStackTrace(out);
            out.println("</pre>");
        }
        finally
        {
            if (doInitialization) {
                // Clean up the ControlBeanContext in the session.
                //JavaControlUtils.uninitializeControlContext( request, response, servletContext );
            }
        }

        outputPageEnd(out);

        out.flush();
        out.close();
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private static void outputPageStart(PrintWriter out)
    {
        out.println("<html>");
        out.println("<head><title>CreatePageFlow Test</title></head>");
        out.println("<body>");
        out.println("<h1>CreatePageFlow Test</h1>");
        out.println("<p>");
        out.println("This servlet creates a page flow containing a control by calling ");
        out.println("FlowControllerFactory.createPageFlow() to create a page flow outside of ");
        out.println("the usual PageFlowRequestProcessor or page filter.");
        out.println("</p>");
        out.println("<hr>");
    }

    private static void outputPageEnd(PrintWriter out)
    {
        out.println("</body>");
        out.println("</html>");
    }
}

