<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>Collected Expression Errors</title>
</head>
<netui:body>
<h4>Collected Expression Errors</h4>
<p style="color:green">
This test verifies that report binding errors to all dataSource attributes.  The errors are reported
at the bottom of the page. This is a replicated version of the Expression test which reports the
errors in-line.  You only need to hit this page to see the errors.
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
</netui:body>
</netui:html>

	


			   
