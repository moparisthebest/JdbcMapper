<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Post A Form</title>
<!-- target -->
<netui:base />
</head>
<body>
<h4>Post A Form</h4>
<netui:form action="postback" style="border: solid 1pt;padding: 10pt">
Text:<netui:textBox dataSource="actionForm.foo"/><br />
<netui:button>Submit</netui:button>
</netui:form>
</body>
</html>
