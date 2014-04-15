<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Binding to URL (Request)</title>
</head>
<body>
<h4>Binding to {param}</h4>
<netui:anchor action="postback">Postback
<netui:parameter name="foo" value="param-foo"/>
<netui:parameter name="bar" value="param-bar"/>
<netui:parameter name="baz" value="param-baz"/>
</netui:anchor>
<hr />
Foo <netui:span value="${param.foo}"/><br />
Bar <netui:span value="${param.bar}"/><br />
Baz <netui:span value="${param.baz}"/><br />
</body>
</html>
