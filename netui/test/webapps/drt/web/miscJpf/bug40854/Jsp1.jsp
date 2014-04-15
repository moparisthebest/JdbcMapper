<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<html>
    <head>
        <title>MiscJpf Bug40854 test</title>
    </head>
    <body>
        <h3 align="center">MiscJpf Bug40854 test - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <font color="green">
            We will visit this page 3 times.  Check the URL bar on the browser
            for a parameter.
            <br/>If the parameter is not there the tests has failed.
            </font>
            <br/><br/>
            First visit: <netui:anchor action="action1">Continue...</netui:anchor>
            <br/><br/>
            Second visit: <netui:anchor action="action2">Continue...</netui:anchor>
            <br/><br/>
            Third visit: <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
