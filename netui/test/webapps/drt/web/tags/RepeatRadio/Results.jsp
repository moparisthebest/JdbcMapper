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
        <h4>Results One: <netui:span value="${pageFlow.resultsOne}"/></h4>
        <h4>Results Two: <netui:span value="${pageFlow.resultsTwo}"/></h4>
        <br/>
    </body>
</html>
