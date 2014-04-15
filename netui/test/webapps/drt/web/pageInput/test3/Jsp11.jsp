<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test3</title>
    </head>
    <body>
        <h3 align="center">PageInput Test3 - Jsp11.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="pgInput" type="java.lang.String" />
            <br/>
            PageInput field: "<netui:span value="${pageInput.pgInput}" />"
            <br/>
            <font color="blue">
                <b>
                    Note: The above string is supposed to be an empty string.
                </b>
            </font>
            <br/>
            <hr width="95%"/>
            <br/>
            <netui:anchor action="action11">Continue...</netui:anchor>
        </center>
    </body>
</html>
