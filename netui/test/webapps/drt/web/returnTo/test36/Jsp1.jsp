<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>ReturnTo Test36</title>
    </head>
    <body>
        <h3 align="center">ReturnTo Test36 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <br/>
            <!--
            test36GlobalAction is not handled by the Jpf1.jpf pageflow
            instead it is handled by the Global.app.  This is by design.
            -->
            <netui:anchor action="test36GlobalAction">Continue...</netui:anchor>
        </center>
    </body>
</html>
