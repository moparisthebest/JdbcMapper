<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
    <head>
        <title>
            Portal Scoping Test
        </title>
    </head>
    <body>
        <h3>Portal Scoping Test</h3>

        data: <b><netui:span value="${pageFlow.data}"/></b>
        <br/>
        <a href="window1.jsp?jpfScopeID=a" target="_a">launch pop-up window</a>
        <br/>
        <netui:anchor action="submit">
            <netui:parameter name="jpfScopeID" value="a"/>
            refresh results
        </netui:anchor>
        <br/>
        <netui:anchor action="begin">go back</netui:anchor>
    </body>
</netui:html>
