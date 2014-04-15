<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test1</title>
    </head>
    <body>
        <h3 align="center">PageInput Test1 - Jsp3.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <br/>
            <netui-data:declarePageInput name="pgKey" type="java.lang.String" />

            PageInput value2: <netui:span value="${pageInput.pgKey}" />
            <br/><br/>
            <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
