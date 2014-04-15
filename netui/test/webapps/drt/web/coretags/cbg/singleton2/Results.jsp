<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
    </head>
    <body>
        <h4>Results One</h4>
        <netui:anchor action="begin">Home</netui:anchor>
	<h4>ResultsOne</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.resultsOne">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
	<h4>ResultsTwo</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.resultsTwo">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    </body>
</html>