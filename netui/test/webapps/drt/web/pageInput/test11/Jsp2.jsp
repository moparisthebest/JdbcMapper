<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test11</title>
    </head>
    <body>
        <h3 align="center">PageInput Test11 - Jsp2.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="pgInput1"    type="java.lang.String" />
            <netui-data:declarePageInput name="pgInput2"    type="java.lang.String" />
            <netui-data:declarePageInput name="pgInputForm" type="pageInput.test11.Jpf1$FormOne" />
            <br/>
            {pageInput.pgInput1}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInput1}"/>"
            </font>

            <br/>
            {pageInput.pgInput2}:
            <font color="blue">"
                <netui:span value="${pageInput.pgInput2}"/>"
            </font>

            <br/>
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
            <br/><br/>
            <netui:anchor action="action2">Continue...</netui:anchor>
        </center>
    </body>
</html>
