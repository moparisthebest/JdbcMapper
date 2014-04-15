<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.beehive.netui.pageflow.ActionResult"%>
<%@ page import="org.apache.beehive.netui.pageflow.PageFlowUtils"%>
<%@ page import="org.apache.beehive.netui.pageflow.scoping.ScopedRequest"%>
<%@ page import="org.apache.beehive.netui.pageflow.scoping.ScopedResponse"%>
<%@ page import="org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<netui:html>
  <head>
    <title>PageFlowUtils.strutsLookup()</title>
  </head>
  <netui:body>
    <h1>Test PageFlowUtils.strutsLookup()</h1>

    <% response.flushBuffer(); %>

    <br/>
    Response.isCommitted() = <%= response.isCommitted() %>

    <%
    String requestURI = request.getContextPath() + request.getServletPath();
    ScopedRequest scopedRequest = 
            ScopedServletUtils.getScopedRequest(request, requestURI, application, "_testID", true);
    ScopedResponse scopedResponse =
            ScopedServletUtils.getScopedResponse(response, scopedRequest);

    String actionOverride = "/bugs/j909/begin";
    ActionResult ar = 
            PageFlowUtils.strutsLookup(application, scopedRequest, scopedResponse, actionOverride, null);

    String result = null;
    if (ar != null) {
        result = ar.getURI();
    }
    %>

    <br/>
    PageFlowUtils.strutsLookup(&quot;<%= actionOverride %>&quot;).getURI() = 
    &quot;<%= result %>&quot;

  </netui:body>
</netui:html>
