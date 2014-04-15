<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test8</title>
    </head>
    <body>
        <h3 align="center">PageInput Test8 - Jsp2.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="pgInput1"    type="java.lang.String" />
            <netui-data:declarePageInput name="pgInput2"    type="java.lang.String" />
            <netui-data:declarePageInput name="pgInputForm" type="pageInput.test8.Jpf1$FormOne" />
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
            <br/>
            <font color="green"><b>
                First time you see this page:&nbsp;&nbsp;
            </b></font>
            <netui:anchor action="action2">Press here</netui:anchor>

            <br/><br/>
            <hr width="95%"/>
            <br/>
            <font color="green"><b>
                Second time you see this page:&nbsp;&nbsp;
            </b></font>
            <netui:anchor action="action3">Press here</netui:anchor>

            <br/><br/>
            <hr width="95%"/>
            <br/>
            <font color="green"><b>
                Third time you see this page:&nbsp;&nbsp;
            </b></font>
            <netui:anchor action="action4">Press here</netui:anchor>
        </center>
    </body>
</html>
