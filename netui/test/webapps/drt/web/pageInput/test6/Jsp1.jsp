<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test6</title>
    </head>
    <body>
        <h3 align="center">PageInput Test6 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="pgInput" type="java.lang.String" />
            <br/>
            <netui:form action="finish">
                {actionForm.fld1}: <font color="blue">"<netui:span value="${actionForm.fld1}"/>"</font>
                <br/>
                {actionForm.fld2}: <font color="blue">"<netui:span value="${actionForm.fld2}"/>"</font>
                <br/>
            </netui:form>
            <br/><br/>
            <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
