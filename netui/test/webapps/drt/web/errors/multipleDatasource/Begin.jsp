<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
<head>
<title>Illegal Compound Expressions</title>
</head>
<netui:body>
<h4>Illegal Compound Expression</h4>
<netui:form action="postback">
Hidden: <netui:hidden dataSource="pageFlow.lastName, {pageFlow.firstName}" /><br />
TextBox: <netui:textBox dataSource="pageFlow.lastName, {pageFlow.firstName}" /><br/>
TextArea: <netui:textArea dataSource="pageFlow.lastName, {pageFlow.firstName}" /><br/>
Select: <netui:select dataSource="pageFlow.lastName, {pageFlow.firstName}" /><br/>
Checkbox: <netui:checkBox dataSource="pageFlow.lastName, {pageFlow.firstName}" /><br/>
CheckBoxGroup: <netui:checkBoxGroup dataSource="pageFlow.lastName, {pageFlow.firstName}" /><br/>
<netui:button type="submit">Submit</netui:button>
</netui:form>
</netui:body>
</netui:html>

	


			   
