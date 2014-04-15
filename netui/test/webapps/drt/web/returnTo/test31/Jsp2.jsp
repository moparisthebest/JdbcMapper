<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>ReturnTo Test31</title>
    </head>
    <body>
        <h3 align="center">ReturnTo Test31 - Jsp2.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <h3><font color="blue">
            We will visit this page twice.
            <br/><br/>
            Page visit: <netui:span value="${pageFlow.string}"/>
            </font></h3>
             <br/><br/>
            <hr width="95%"/>
            <br/><br/>
            First time select continue:
            <netui:anchor action="action2">Continue...</netui:anchor>
            <br/><br/>
            Second time select finish:
            <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
