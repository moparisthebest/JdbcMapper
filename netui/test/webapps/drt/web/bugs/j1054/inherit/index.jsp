<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<html>
<head>
    <title>Page Flow Inheritance - Inherit useFormBean annotation attribute</title>
</head>
<body>
    <h1>Page Flow Inheritance - Inherit useFormBean annotation attribute</h1>
    <h3>Inherit Simple Action - useFormBean attribute set</h3>
    <br/>
    <netui:form action="/actionOne">
        <netui:textBox dataSource="actionForm.name" /><br />
        Input Value:<netui:span value="${actionForm.name}" /><br />
        <netui:button type="submit" action="actionOne" >Action One</netui:button>
    </netui:form>
    <br/>
    <netui:anchor action="begin">begin</netui:anchor>
</body>
</html>
