<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<html>
    <head>
    </head>
    <body>
        <h4>Results One</h4>
        <netui:anchor action="begin">Home</netui:anchor>
        <ul>
        <netui-data:repeater dataSource="pageFlow.resultsOne">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
        <br/>
        <h4>Results Two:</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.resultsTwo">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
    </body>
</html>
