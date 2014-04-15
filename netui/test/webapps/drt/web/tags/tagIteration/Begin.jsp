<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Single Item Iteration</title>
</head>
<body>
<netui:form action="/postback">
   <netui:select dataSource="pageFlow.select" defaultValue="${pageFlow.defaultSelect}"
	optionsDataSource="${pageFlow.options}"/><br />
   <netui:radioButtonGroup dataSource="pageFlow.radioGroup" defaultValue="${pageFlow.defaultRadioGroup}"
	optionsDataSource="${pageFlow.options}"/><br />
   <netui:checkBoxGroup dataSource="pageFlow.checkGroup" defaultValue="${pageFlow.defaultCheckGroup}"
	optionsDataSource="${pageFlow.options}"/><br />
   <netui:button type="submit">Submit</netui:button>
</netui:form>
<hr />
</body>
</html>
