<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
    <head>
        <title>
            Portal Scoping Test
        </title>
    </head>
    <body>
        data: <b><netui:span value="${pageFlow.data}"/></b>
        <p>
        <a href="javascript:window.close();">close window</a>
    </body>
</netui:html>