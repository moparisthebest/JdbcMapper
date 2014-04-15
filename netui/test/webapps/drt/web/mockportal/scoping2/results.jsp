<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
    <head>
        <title>
            Portal Scoping Test 2
        </title>
    </head>
    <body>
        <h3>Portal Scoping Test 2</h3>

        data: <b><netui:span value="${pageFlow.data}"/></b>
        <br/>
        <netui:anchor action="launchPopUp" target="portletA2" targetScope="portletA2">
            launch window in 'portletA2' scope
        </netui:anchor>
        <br/>
        <netui:anchor action="submit">
            refresh results
        </netui:anchor>
        <br/>
        <netui:anchor action="begin">go back</netui:anchor>
    </body>
</netui:html>
