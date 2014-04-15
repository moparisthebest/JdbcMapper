<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Form Parameters</title>
</head>
<body>
<netui:form action="postback">
<netui:parameter name="foo" value="form-parameter-bar"/><br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
<hr />
Action: <netui:span value="${pageFlow.action}"/><br />
Foo: <netui:span value="${pageFlow.foo}"/><br />
</body>
</html>
