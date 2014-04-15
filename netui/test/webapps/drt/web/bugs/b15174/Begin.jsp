<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Bug 25</title>
</head>
<body>
<netui:anchor action="/postback">Postback through Action
<netui:parameter name="foo" value="bar"/><br />
</netui:anchor>
<netui:anchor href="hrefPostback.do">Postback through URL
<netui:parameter name="foo" value="baz"/><br />
</netui:anchor>
<hr />
Action: <netui:span value="${pageFlow.action}"/><br />
Foo: <netui:span value="${pageFlow.foo}"/><br />
</body>
</html>
