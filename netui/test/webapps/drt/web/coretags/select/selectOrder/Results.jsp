<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<html>
    <head>
    </head>
    <body>
        <netui:anchor action="begin">Home</netui:anchor>
        <h4>Results 1</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.results1">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
        <br>
        <h4>Results 2</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.results2">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
        <br>
        <h4>Results 3</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.results3">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
        <br>
        <h4>Results 4</h4>
        <ul>
        <netui-data:repeater dataSource="pageFlow.results4">
            <li><netui:span value="${container.item}"/></li>
        </netui-data:repeater>
        </ul>
        <br>

    </body>
</html>
