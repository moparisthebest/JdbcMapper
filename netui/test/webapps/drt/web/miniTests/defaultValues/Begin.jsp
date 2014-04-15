<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Default Values</title>
</head>
<body>
<h4>Default Values</h4>
</body>
<netui:form action="/postback">
   <netui:textBox dataSource="actionForm.text" defaultValue="${pageFlow.defaultText}"/><br />
   <netui:textArea dataSource="actionForm.textArea" defaultValue="${pageFlow.defaultTextArea}"/><br />
   <netui:select dataSource="actionForm.select" defaultValue="${pageFlow.defaultSelect}"
	optionsDataSource="${pageFlow.selectOptions}"/><br />
   <netui:radioButtonGroup dataSource="actionForm.radioGroup" defaultValue="${pageFlow.defaultRadioGroup}"
	optionsDataSource="${pageFlow.radioGroupOptions}"/><br />
   <netui:checkBoxGroup dataSource="actionForm.checkGroup" defaultValue="${pageFlow.defaultCheckGroup}"
	optionsDataSource="${pageFlow.checkGroupOptions}"/><br />
   <netui:button type="submit">Submit</netui:button>
</netui:form>
</html>

	


			   
