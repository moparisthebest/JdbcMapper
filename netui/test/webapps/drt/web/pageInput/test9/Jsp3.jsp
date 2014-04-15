<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test9</title>
    </head>
    <body>
        <h3 align="center">PageInput Test9 - Jsp3.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <font color="green"><b>
                PageInput values.
            <b/></font>
            <netui-data:declarePageInput name="pgInput1"    type="java.lang.String" />
            <netui-data:declarePageInput name="pgInput2"    type="java.lang.String" />
            <netui-data:declarePageInput name="pgInputForm" type="pageInput.test9.Jpf1$MyForm" />
            <br/><br/>
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
            <br/>
            <font color="green">
                If any of the values above begin with the word "New" then the
                test has failed.
            </font>
            <br/><br/>
            <netui:anchor action="action3">Continue...</netui:anchor>
        </center>
    </body>
</html>
