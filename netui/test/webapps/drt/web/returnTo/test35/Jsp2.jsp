<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>ReturnTo Test35</title>
    </head>
    <body>
        <h3 align="center">ReturnTo Test35 - Jsp2.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <br/>
            <!--
            test35GlobalAction is not handled by the Jpf2.jpf pageflow
            instead it is handled by the Global.app.  This is by design.
            -->
            <netui:anchor action="test35GlobalAction">Continue...</netui:anchor>
        </center>
    </body>
</html>
