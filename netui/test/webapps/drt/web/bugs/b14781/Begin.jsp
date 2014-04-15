<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Select Binding to PageFlow</title>
</head>
<body>
<h4>Select Binding to PageFlow</h4>
<p style="color:green">
This is a test of the select binding directly to the page flow.  The
dataSource, optionsDataSource and defaultValue all come from the page flow.
This page then posts the form in a postback to the same page.  You should
option selected below the form.
</p>
<netui:form action="postback">
Select Box: <netui:select dataSource="pageFlow.selected"
 optionsDataSource="${pageFlow.options}" defaultValue="${pageFlow.defaultValue}"/>
 <br>
<netui:button type="submit">Submit</netui:button>
</netui:form><br>
Selected: <netui:span value="${pageFlow.selected}"/>
</body>
</html>
