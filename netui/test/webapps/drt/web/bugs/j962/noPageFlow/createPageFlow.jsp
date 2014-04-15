<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.beehive.netui.pageflow.FlowControllerFactory"%>
<%@ page import="org.apache.beehive.netui.pageflow.PageFlowController"%>
<%@ page import="org.apache.beehive.netui.pageflow.RequestContext"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <p>
        This test ensures that we don't get an assertion error if we create a page flow controller
        through FlowControllerFactory, even when its onCreate() throws an exception.
        </p>

        <%
            FlowControllerFactory f = FlowControllerFactory.get(pageContext.getServletContext());
            PageFlowController pfc = f.createPageFlow(new RequestContext(request, response), "bugs.j962.Controller");
        %>

        Page flow instance is: <%= pfc.getClass().getName() %>
           
    </body>
</netui:html>

  

