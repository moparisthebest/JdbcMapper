<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Checkbox Binding Errors</title>
</head>
<body>
<h4>Checkbox Binding Errors</h4>
<p style="color:green">
This test contains two binding errors. One binds to the actionForm and one 
binds to the pageFlow.  These errors should be seend in the page.  This is the only page of the test.
</p>
<netui:form action="/next">
Checkbox from form: <netui:checkBox dataSource="actionForm.bad" /><br />
True Checkbox from form: <netui:checkBox dataSource="actionForm.checkBox2" /><br />
<br />
Checkbox from page flow: <netui:checkBox dataSource="pageFlow.bad" /><br />
True Checkbox from page flow: <netui:checkBox dataSource="pageFlow.checkBox2" /><br />
<netui:button type="submit">Submit</netui:button>
</netui:form>
<br />
Checkbox from Form: <netui:span value="${pageFlow.form.checkBox1}" /><br />
True Checkbox Form: <netui:span value="${pageFlow.form.checkBox2}" /><br />
<br />
Checkbox from Page Flow: <netui:span value="${pageFlow.checkBox}" /><br />
True Checkbox from Page Flow: <netui:span value="${pageFlow.checkBox2}" /><br />
</body>
</html>
