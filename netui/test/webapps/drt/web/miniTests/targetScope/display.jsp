<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
    </head>
    <body>
        current scope: <b><%= request.getParameter( "jpfScopeID" ) %></b>
        <br/>
        string: <b>${pageFlow.str}</b>
        <br/>
        <netui:anchor action="begin">go to first page</netui:anchor>
    </body>
</html>
