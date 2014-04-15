<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test10</title>
    </head>
    <body>
        <h3 align="center">PageInput Test10 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <font color="green">
                This page is here because I don't want the "begin" action method
                to be the action "returned-to", so just click thru the page.
            </font>
            <br/><br/>
            <netui:anchor action="action1">Continue...</netui:anchor>
        </center>
    </body>
</html>
