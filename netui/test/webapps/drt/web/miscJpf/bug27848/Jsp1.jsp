<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
    <head>
        <title>MiscJpf Bug 27848 test</title>
    </head>
    <body>
        <h3 align="center">MiscJpf Bug 27848 test - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui:span value="${pageFlow.field1}" />
            <br/><br/>
            <netui:anchor action="jpfAction1">continue</netui:anchor>
        </center>
    </body>
</html>
