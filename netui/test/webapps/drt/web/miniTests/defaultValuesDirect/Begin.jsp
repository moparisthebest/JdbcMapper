<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Default Values Direct</title>
</head>
<body>
<h4>Default Values Direct</h4>
</body>
<netui:form action="/postback">
   <netui:textBox dataSource="pageFlow.text"
	defaultValue="${pageFlow.defaultText}"/><br />
   <netui:textArea dataSource="pageFlow.textArea"
	defaultValue="${pageFlow.defaultTextArea}"/><br />
   <netui:select dataSource="pageFlow.select"
	defaultValue="${pageFlow.defaultSelect}"
	optionsDataSource="${pageFlow.selectOptions}"/><br />
   <netui:radioButtonGroup dataSource="pageFlow.radioGroup"
	defaultValue="${pageFlow.defaultRadioGroup}"
	optionsDataSource="${pageFlow.radioGroupOptions}"/><br />
   <netui:checkBoxGroup dataSource="pageFlow.checkGroup"
	defaultValue="${pageFlow.defaultCheckGroup}"
	optionsDataSource="${pageFlow.checkGroupOptions}"/><br />
   <netui:button type="submit">Submit</netui:button>
</netui:form>
</html>

	


			   
