<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Databinding from an Anchor</title>
</head>
<body>
<h4>Databinding URL paramters to page flow properties</h4>
<p style="color:green">
This test has an anchor with a couple of parameters.  The parameter names are databinding expressions '{pageFlow.name}'.
When these are posted, they will update the page flow property 'name'.  Below the anchor is a binding to the page flow
name and type properties.  When the anchor is pressed, the values "foo" and "bar" should appear.
</p>
<hr>
<netui:anchor href="postback.do">Value in parameter
<netui:parameter name="{pageFlow.name}" value="foo"/><br>
<netui:parameter name="{pageFlow.type}" value="bar"/><br>
</netui:anchor>
Name: <netui:span value="${pageFlow.name}"/><br>
Type: <netui:span value="${pageFlow.type}"/><br>
<hr>
</body>
</html>
