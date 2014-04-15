<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>Checkbox test</title>
</head>
<netui:body>
<h4>Checkbox Tests</h4>
<p style="color:green">
This test binds checkboxes to both an actionForm and to properties in the page flow. Initially, one
variable is true and one false in each binding.  Each time the form is posted, the values should be updated.
This is a basic test that verifies that the checkbox state is correctly tracked and updated during a post.
</p>
<netui:form action="next">
Checkbox from form: <netui:checkBox dataSource="actionForm.checkBox1" /><br>
Checkbox from form (true): <netui:checkBox dataSource="actionForm.checkBox2" /><br>
<br>
Checkbox from page flow: <netui:checkBox dataSource="pageFlow.checkBox" /><br>
Checkbox from page flow (true): <netui:checkBox dataSource="pageFlow.checkBox2" /><br>
<netui:button type="submit">Submit</netui:button>
</netui:form>
<hr>
Checkbox from Form: <netui:span value="${pageFlow.form.checkBox1}" /><br>
Checkbox from Form (true): <netui:span value="${pageFlow.form.checkBox2}" /><br>
<br>
Checkbox from Page Flow: <netui:span value="${pageFlow.checkBox}" /><br>
Checkbox from Page Flow (true): <netui:span value="${pageFlow.checkBox2}" /><br>
</netui:body>
</netui:html>
