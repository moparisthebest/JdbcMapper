<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0"        prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data" %>

<html>
    <head>
        <title>PageInput Test4</title>
    </head>
    <body>
        <h3 align="center">PageInput Test4 - Jsp1.jsp</h3>
        <hr width="95%"/>
        <br/>
        <center>
            <netui-data:declarePageInput name="theForm" type="pageInput.test4.Jpf1$FormOne" />
            <h3>
                All fields being displayed below are from the same formBean just
                using different data-binding contexts.
            </h3>
            <b>These two are from the {actionForm} context.</b>
            <br/>
            <netui:form action="action1">
                {actionForm.fld1}: <font color="blue"><netui:span value="${actionForm.fld1}"/></font>
                <br/>
                {actionForm.fld2}: <font color="blue"><netui:span value="${actionForm.fld2}"/></font>
                <br/>
            </netui:form>
            <hr width="95%"/>

            <br/>
            <b>These two are from the {pageInput} context.</b>
            <br/><br/>
            {pageInput.theForm.fld1}: <font color="blue"><netui:span value="${pageInput.theForm.fld1}" /></font>
            <br/>
            {pageInput.theForm.fld2}: <font color="blue"><netui:span value="${pageInput.theForm.fld2}" /></font>
            <br/><br/>
            <hr width="95%"/>

            <br/>
            <font color="green">
                <strong>
                Please change the value of "{pageInput.theForm.fld1}" to "test1"
                and the value of "{pageInput.theForm.fld2}" to "test2".  Then
                press the "Submit changes" button.  (These values are supposed
                to be read-only, so we'll see about that.)
                </strong>
            </font>
            <br/>
            <netui:form action="action1">
                {pageInput.theForm.fld1}: <netui:textBox dataSource="pageInput.theForm.fld1"/>
                <br/>
                {pageInput.theForm.fld2}: <netui:textBox dataSource="pageInput.theForm.fld2"/>
                <br/>
                <br/>
                <netui:button action="action1" value="Submit changes" type="submit"/>
            </netui:form>
        </center>
    </body>
</html>
