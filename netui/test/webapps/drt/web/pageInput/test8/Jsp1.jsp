<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test8</title>
    </head>
    <body>
        <h3 align="center">PageInput Test8 - Jsp1.jsp</h3>
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
            <netui:anchor action="action1">Press here</netui:anchor>
            <br/>
            The 1st time you see this page, none of the values above should
            begin with the word "New".

            <br/><br/>
            <hr width="95%"/>
            <br/>
            <font color="green"><b>
                Second time you see this page:&nbsp;&nbsp;
            </b></font>
            <netui:anchor action="action1">Press here</netui:anchor>
            <br/>
            The 2nd time you see this page, none of the values above should
            begin with the word "New".

            <br/><br/>
            <hr width="95%"/>
            <br/>
            <font color="green"><b>
                Third time you see this page:&nbsp;&nbsp;
            </b></font>
            <netui:anchor action="action1">Press here</netui:anchor>
            <br/>
            The 3rd time you see this page, all the values should begin with the
            word "New".

            <br/><br/>
            <hr width="95%"/>
            <br/>
            <font color="green"><b>
                Forth time you see this page:&nbsp;&nbsp;
            </b></font>
            <netui:anchor action="finish">Press here</netui:anchor>
            <br/>
            The 4th time you see this page, only the {pageInput.pgInput2} value
            should begin with the word "New".
        </center>
    </body>
</html>
