<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
        <p style="color:green">
        This test verifies that a data format can be applied to both a Span and a Label.
        </p>
        Span: <netui:span value="${pageFlow.date}"><netui:formatDate pattern="MM/dd/yyyy" /></netui:span><br>
        Label: <netui:label value="${pageFlow.date}"><netui:formatDate pattern="MM/dd/yyyy" /></netui:label>
    </netui:body>
</netui:html>
