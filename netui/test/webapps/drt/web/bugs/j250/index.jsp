<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
        <p style="color:green">
        This test verifies that you can choose to apply formatters to the default value or not.  The first
        two elements below bind to a default value that is a Calendar and format that.  The second two
        have the value in the tag and do not apply the formatter.
        </p>
        Span: <netui:span value="${pageFlow.date}" formatDefaultValue="true" defaultValue="${pageFlow.defaultDate}">
            <netui:formatDate pattern="MM/dd/yyyy" /></netui:span><br>
        Label: <netui:label value="${pageFlow.date}" formatDefaultValue="true" defaultValue="${pageFlow.defaultDate}">
            <netui:formatDate pattern="MM/dd/yyyy" /></netui:label><br>
         Span: <netui:span value="${pageFlow.date}" defaultValue="[No Date Specified]">
             <netui:formatDate pattern="MM/dd/yyyy" /></netui:span><br>
         Label: <netui:label value="${pageFlow.date}" defaultValue="[No Date Specified]">
             <netui:formatDate pattern="MM/dd/yyyy" /></netui:label>
    </netui:body>
</netui:html>
