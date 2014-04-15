<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test7</title>
    </head>
    <body>
        <h3 align="center">PageInput Test7 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <!--
                I learned that the "type" attribute is required but is totaly
                ignored at run-time.  Note the garbage I've put in the first
                delarePageInput tag.
            -->
            <netui-data:declarePageInput name="pgInputVal_1"        type="blah.blah.blah" />
            <netui-data:declarePageInput name="pgInputVal_2"        type="java.lang.String" />
            <netui-data:declarePageInput name="pgInputForm"         type="pageInput.test7.Jpf1$FormOne" />
            <netui-data:declarePageInput name="pgInputFormInner"    type="pageInput.test7.Jpf1$FormOne$FormTwo" />
            <br/>
            {pageInput.pgInputVal_1}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputVal_1}"/>"
            </font>

            <br/>
            {pageInput.pgInputVal_2}:
            <font color="blue">"
                <netui:span value="${pageInput.pgInputVal_2}"/>"
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

            <br/>
            {pageInput.pgInputFormInner.fld1}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputFormInner.fld1}"/>"
            </font>

            <br/>
            {pageInput.pgInputFormInner.fld2}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputFormInner.fld2}"/>"
            </font>

            <br/>
            {pageInput.pgInputForm.fld3.fld1}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputForm.fld3.fld1}"/>"
            </font>

            <br/>
            {pageInput.pgInputForm.fld3.fld2}:
            <font color="blue">
                "<netui:span value="${pageInput.pgInputForm.fld3.fld2}"/>"
            </font>

            <br/><br/>
            <netui:anchor action="finish">Finish...</netui:anchor>
        </center>
    </body>
</html>
