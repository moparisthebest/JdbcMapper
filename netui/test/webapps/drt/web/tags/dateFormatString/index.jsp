<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Web Application Page
        </title>
    </head>
    <h4>Date Format Tests</h4>
    <p style="color:green">
    This test verifies formating of the date.  The first set of contains the
    pattern inside the tag and binds to various dates that are properties of
    the pageflow.  The second set binds to the pattern in the page flow.
    The final set are errors and generate error messages.
    </p>
    <hr>
    <netui:body><h4>In tags</h4>
        2003/10/25: <netui:span value="${pageFlow.dateOne}"><netui:formatDate pattern="MM/dd/yyyy" stringInputPattern="yy/MM/dd"/></netui:span> <br>
        2003-10-25: <netui:span value="${pageFlow.dateTwo}"><netui:formatDate pattern="MM/dd/yyyy"/></netui:span> <br>
        2003.10.25: <netui:span value="${pageFlow.dateThree}"><netui:formatDate pattern="MM/dd/yyyy" stringInputPattern="yy.MM.dd"/></netui:span> <br>
        <hr ><h4>Data Binding</h4>
        2003/10/25: <netui:span value="${pageFlow.dateOne}"><netui:formatDate pattern="MM/dd/yyyy" stringInputPattern="${pageFlow.pattOne}"/></netui:span> <br>
        2003-10-25: <netui:span value="${pageFlow.dateTwo}"><netui:formatDate pattern="MM/dd/yyyy"/></netui:span> <br>
        2003.10.25: <netui:span value="${pageFlow.dateThree}"><netui:formatDate pattern="MM/dd/yyyy" stringInputPattern="${pageFlow.pattTwo}"/></netui:span> <br>
        <hr><h4>Errors</h4>
        2003.10.25: <netui:span value="${pageFlow.dateThree}"><netui:formatDate pattern="MM/dd/yyyy" stringInputPattern="${pageFlow.pattError}"/></netui:span> <br>
        2003.10.25: <netui:span value="${pageFlow.dateThree}"><netui:formatDate pattern="MM/dd/yyyy" stringInputPattern="${pageFlow.pattOne}"/></netui:span> <br>
    </netui:body>
</netui:html>
