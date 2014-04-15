<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Test of Iterators</title>
</head>
<body>
<h4>Test of Iterators</h4>
<netui:form action="/postback">
Select: 
  <netui:select dataSource="pageFlow.selected"
    optionsDataSource="${pageFlow.options}"
    defaultValue="${pageFlow.defaultValue}"/>
<br />
CheckBoxGroup:
   <netui:checkBoxGroup dataSource="pageFlow.checkGroup"
        defaultValue="${pageFlow.defaultCheckGroup}"
	optionsDataSource="${pageFlow.options}"/>
<br />
RadioButtonGroup: 
   <netui:radioButtonGroup dataSource="pageFlow.radioGroup"
        defaultValue="${pageFlow.defaultRadioGroup}"
	optionsDataSource="${pageFlow.options}"/><br />
<br/>
<netui:button type="submit">Submit</netui:button>
</netui:form>
<br />
</body>
</html>
