<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
    <head>
        <title>FormBean Test1</title>
    </head>
    <body>
        <h3 align="center">FormBean Test1 - Jsp3.jsp</h3>
        <hr width="95%"/>
        <br/>
        <font color="blue">
            <h3 align="center">Page Flow jsp page.</h3>
        </font>
        <center>
            <netui:form action="jpfAction3">
               Field1 value: <netui:span value="${actionForm.field1}"/>
            </netui:form>
            <br/>
            <netui:anchor action="jpfAction3">Continue</netui:anchor>
        </center>
    </body>
</html>
