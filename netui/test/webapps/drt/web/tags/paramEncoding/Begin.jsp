<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Encoding Parameter Binding</title>
</head>
<body>
<netui:anchor href="postback.do">Value in parameter
<netui:parameter name="foo" value="value with?bad&stuff+here"/><br />
</netui:anchor><br />
<netui:anchor href="postback.do">Value is databound
<netui:parameter name="foo" value="${pageFlow.param}"/><br />
</netui:anchor>
<hr />
Foo Value: <netui:span value="${pageFlow.foo}"/><br />
</body>
</html>
