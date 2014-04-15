<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>ReturnTo Test40</title>
    </head>
    <body>
        <h3 align="center">ReturnTo Test40 - Jsp2.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <h3><font color="blue">
            We will visit this page three times.
            </font></h3>
             <br/><br/>
            <hr width="95%"/>
            <br/><br/>
            <!--
            test40GlobalAction_B is not handled by the Jpf1.jpf pageflow instead\
            it is handled by the Global.app.  This is by design.
            -->
            First time select here:
            <netui:anchor action="test40GlobalAction_B">Continue...</netui:anchor>
            <br/><br/>
            Second time select here:
            <netui:anchor action="action1">Continue...</netui:anchor>
            <br/><br/>
            Third time select here:
            <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
