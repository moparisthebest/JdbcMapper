<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test5</title>
    </head>
    <body>
        <h3 align="center">PageInput Test5 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <br/>
            <netui-data:declarePageInput name="pgInput1" type="java.lang.String" />
            <netui-data:declarePageInput name="pgInput2" type="java.lang.String" />

            PageInput value1: <font color="blue">"<netui:span value="${pageInput.pgInput1}" />"</font>
            <br/>
            PageInput value2: <font color="blue">"<netui:span value="${pageInput.pgInput2}" />"</font>
            <br/><br/>
            <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
