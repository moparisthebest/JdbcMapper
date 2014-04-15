<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test3</title>
    </head>
    <body>
        <h3 align="center">PageInput Test3 - Jsp12.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="value1" type="java.lang.String" />
            <br/>
            PageInput field: "<netui:span value="${pageInput.value1}" />"
            <br/>
            <hr width="95%"/>
            <br/>
            <netui:anchor action="action12">Continue...</netui:anchor>
        </center>
    </body>
</html>
