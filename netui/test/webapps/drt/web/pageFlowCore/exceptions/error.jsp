<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
<head>
<title>Error Page</title>
</head>
<body bgcolor="white">

<h3>Error Page</h3>

<netui:form action="goBack">

    Message from method: <b><netui:span value="${pageFlow.messageFromMethod}" /></b>
    <br>
    <br>
    Exception message: <b><netui:exceptions showStackTrace="false" showMessage="true"/></b>
    <br>
    <br>
    <netui:button type="submit">Go Back</netui:button>

</netui:form>

</body>
</html>
