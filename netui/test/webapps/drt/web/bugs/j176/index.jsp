<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html documentType="xhtml1-transitional">
    <head>
        <netui:base/>
    </head>
    <netui:body>
         <p style="color:green">verify two forms on a page going to
		the same action produce legal XHTML.
         </p>
         <netui:span value="${pageFlow.action}" />
         <netui:form action="post"></netui:form>
         <netui:form action="post"></netui:form>
    </netui:body>
</netui:html>
