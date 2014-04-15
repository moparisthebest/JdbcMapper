<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
    </head>
    <body>
        <b>${pageInput.zip}</b> is not a registered zip code.
        <br/>
        <br/>
        <netui:form action="begin">
            <netui:button value="try again"/>
            <netui:button action="cancel" value="cancel"/>
        </netui:form>
    </body>
</html>
