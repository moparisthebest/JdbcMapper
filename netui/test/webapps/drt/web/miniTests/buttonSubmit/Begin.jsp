<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>ButtonSubmit</title>
</head>
<body>
<netui:form action="/buttonOne">
<netui:button type="submit" action="/buttonOne" >Button One</netui:button>
<netui:button type="submit" action="/buttonTwo" >Button Two</netui:button>
</netui:form>
<br />
Last Action: <netui:span value="${pageFlow.action}"/>
</body>
</html>
