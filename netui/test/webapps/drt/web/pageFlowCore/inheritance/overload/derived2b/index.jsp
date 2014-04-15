<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
    <title>Page Flow Inheritance - Overriding Overloaded Actions</title>
</head>
<body>
    <h1>Page Flow Inheritance - Overriding Overloaded Actions</h1>
    <h3>Derived2b</h3>
    ${pageFlow.actionInfo}
    <br/>
    <netui:form action="/actionOne">
        <netui:textBox dataSource="actionForm.name" /><br />
        Input Value:<netui:span value="${actionForm.name}" /><br />
        <netui:button type="submit" action="actionOne" >Action One</netui:button>
    </netui:form>
    <br/>
    <a href="../begin.do">back to start</a>
</body>
</html>
