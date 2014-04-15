<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>


<html>
<head>
<title>getActions test</title>
</head>
<body bgcolor="white">

<h3>getActions test</h3>

<font color="Blue"><netui:span value="${requestScope.message}"/></font><br><br>

Actions in this pageflow:
<ul>
<netui-data:repeater dataSource="pageFlow.sortedActions">
    <netui-data:repeaterItem>
        <li><netui:span value="${container.item}"/>
    </netui-data:repeaterItem>
</netui-data:repeater>
</ul>

<br>
<netui:anchor action="nestReturnNoForm">nest, and return to overloaded action with no form</netui:anchor><br>
<netui:anchor action="nestReturnForm1">nest, and return to overloaded action with Form1</netui:anchor><br>
<netui:anchor action="nestReturnForm2">nest, and return to overloaded action with Form2</netui:anchor><br>
<netui:anchor action="nestReturnUnknownForm">nest, and return to overloaded action with unknown form</netui:anchor><br>
<netui:anchor action="nestReturnNormalWithForm">nest, and return with a form</netui:anchor><br>
<netui:anchor action="nestReturnNormalWithoutForm">nest, and return with no form</netui:anchor><br>
</body>
</html>
