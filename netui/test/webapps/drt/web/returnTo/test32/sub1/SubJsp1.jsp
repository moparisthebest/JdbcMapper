<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>ReturnTo Test32</title>
    </head>
    <body>
        <h3 align="center">ReturnTo Test32 - SubJsp1.jsp</h3>
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
            <netui:anchor action="action1">Continue...</netui:anchor>
            <br/><br/>
            Second time select return:
            <netui:anchor action="finish">Return...</netui:anchor>
            <br/><br/>
            <font color="green">
            The first visit is kind of a negative test.  It tests that if you do
            a return-to="previousPage" and there is no previous page for the
            pageFlow that you go back to the current page in the pageFlow.
            </font>
        </center>
    </body>
</html>
