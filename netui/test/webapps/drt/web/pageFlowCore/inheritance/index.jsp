<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<html>
<head>
<title>Page Flow Inheritance</title>
</head>
<body>
<h3>Page Flow Inheritance</h3>

    <b>Actions:</b>
    <ul>
    <netui-data:repeater dataSource="pageFlow.sortedActions">
        <netui-data:repeaterItem><li>${container.item}</li></netui-data:repeaterItem>
    </netui-data:repeater>
    </ul>

    <br/>
    <netui:anchor action="methodAction1">action in superclass</netui:anchor>
    <br/>
    <netui:anchor action="throwException">throw exception</netui:anchor>
    <br/>
    <netui:anchor action="overrideMe">hit overridden action</netui:anchor>
    
    <br/>
    <br/>
    Message: <b>${pageInput.message}</b>
        

</body>
</html>

	


			   
