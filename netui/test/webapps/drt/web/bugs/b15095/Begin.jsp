<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>check box bug</title>
</head>
<netui:form action="/postback">
   Checkbox <netui:checkBox dataSource="actionForm.check" defaultValue="${pageFlow.defaultCheck}"/><br />
   <netui:button type="submit">Submit</netui:button>
</netui:form>
<hr />
Value: <netui:span value="${pageFlow.value}"/>
</body>
</html>

	


			   
