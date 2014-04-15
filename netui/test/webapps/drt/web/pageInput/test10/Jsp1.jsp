<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test10</title>
    </head>
    <body>
        <h3 align="center">PageInput Test10 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="pgInputForm" type="pageInput.test10.Jpf1.FormOne" />

            {pageInput.pgInputForm.fld1}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputForm.fld1}"/>"
            </font>

            <br/>
            {pageInput.pgInputForm.fld2}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputForm.fld2}"/>"
            </font>
            <br/><br/>
            <hr width="95%"/>
            <br/>
            <netui:anchor action="action1">Continue...</netui:anchor>
        </center>
    </body>
</html>
