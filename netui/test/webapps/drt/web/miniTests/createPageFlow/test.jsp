<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h1>Test CreatePageFlow</h1>
        <h3>${pageFlow.URI}</h3>

        <p>
        This page flow is created from a servlet calling
        FlowControllerFactory.createPageFlow() to create a page flow outside of
        the usual PageFlowRequestProcessor or page filter. The servlet also
        initializes/uninitializes the ControlContainerContext.
        </p>

        <br/>
        <pre>${pageFlow.data}</pre>
    </netui:body>
</netui:html>

  

