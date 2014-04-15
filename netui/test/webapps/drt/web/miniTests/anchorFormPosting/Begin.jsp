<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>URL Parameter Mapping to Action Form</title>
</head>
<netui:body>
<h4>URL Parameter Mapping to an Action Form</h4>
<p style="color:green">
This test maps parameters in a URL to an action that takes a form.  The paramaters will automatically update
the form before the action is called.  When the link is pressed, the value will be updated to "LastName, FirstName".
You should press the anchor to update the value.
</p>
<netui:anchor href="postback.do">Value in parameter
<netui:parameter name="lastName" value="LastName"/><br />
<netui:parameter name="firstName" value="FirstName"/><br />
</netui:anchor>
<hr />
Value: <netui:span value="${pageFlow.value}"/><br />
</netui:body>
</netui:html>
