<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Begin</title>
</head>
<body>
<h4>Directly Bind to a {pageFlow}</h4>
<p style="color:green">
This test will directly bind a textBox to a pageFlow.  When the form is submitted, the page flow will be updated.
The results page will also directly bind to the pageFlow to get the value posted.  There is a default value specified 
which should be shown when the form is first displayed.  The value of the default is '<b>Default</b>'.
</p>
<netui:form action="/next1">
<netui:textBox dataSource="pageFlow.string" defaultValue="${pageFlow.default}"/>
<br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
</body>
</html>
