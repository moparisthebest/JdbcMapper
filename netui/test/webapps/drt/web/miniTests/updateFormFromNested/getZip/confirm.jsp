<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
    </head>
    <body>
        You have chosen <b>${pageInput.zip}</b>, in <b>${pageInput.state}</b>.
        Is this correct?
        <br/>
        <br/>
        <netui:form action="done">
            <netui:button value="yes"/>
            <netui:button action="begin" value="no"/>
        </netui:form>
    </body>
</html>
