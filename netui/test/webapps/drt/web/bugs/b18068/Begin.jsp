<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Format Error</title>
</head>
<body>
<h4>Format Error -- Should report an error for an invalid type</h4>
<netui:span value="12345.67">
     <netui:formatNumber language="EN" country = "US" type="foo"
     pattern="#.00"/>
</netui:span>
</body>
</html>
