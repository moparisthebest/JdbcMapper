<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>In-line Expression Errors</title>
</head>
<body>
<h4>In-line Expression Errors</h4>
<p style="color:green">
This test verifies that report binding errors to all dataSource attributes.  The errors are reported in-line.
This test is dupicated by HtmlExpression which reports the errors at the end of the page.
You only need to hit this page to see the errors.
</p>
<hr>
<netui:form action="/postback">
<table>
<tr><td>Hidden</td><td><netui:hidden dataSource="pageFlow.hidden"/></td></tr>
<tr><td>TextBox</td><td><netui:textBox dataSource="pageFlow.tbDataSource"/></td></tr>
<tr><td>TextArea</td><td><netui:textArea dataSource="pageFlow.taDataSource"/></td></tr>
<tr><td>Checkbox</td><td><netui:checkBox dataSource="pageFlow.cbDataSource"/></td></tr>
<tr><td>Checkbox Group</td><td><netui:checkBoxGroup dataSource="pageFlow.cbgDataSource"/></td></tr>
<tr><td>Radio Group</td><td><netui:radioButtonGroup dataSource="pageFlow.rgDataSource" /></td></tr>
<tr><td>Select</td><td><netui:select dataSource="pageFlow.sDataSource" /></td></tr>
</table>
</netui:form>
<hr />
</body>
</html>

	


			   
