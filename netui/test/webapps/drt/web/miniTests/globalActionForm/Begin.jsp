<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Basic Global App</title>
</head>
<body>
<h4>Basic Global App</h4>
  <netui:form action="globalAction_form">
     Search <netui:textBox dataSource="actionForm.search"/>
     <netui:button type="submit">Submit</netui:button>
  </netui:form>
</body>
</html>
