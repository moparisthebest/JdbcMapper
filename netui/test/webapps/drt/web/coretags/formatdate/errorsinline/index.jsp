<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<html>
    <head>
        <netui:base />
    </head>
    <body>
    <h4>Format Error Reporting Inline</h4>
    <p style="color:green">For each HTML tag that allows a formatter, verify that the error is
    being reported.  The error will only be produced inline because there isn't a &lt;netui:html> 
    tag to collect the errors.
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:span value="01/28/63"><netui:formatDate pattern="${pageFlow.nullValue}"/></netui:span></li>
    <li><netui:select dataSource="pageFlow.select">
        <netui:formatDate pattern="${pageFlow.nullValue}"/>
        <netui:selectOption value="01/28/63" />
        <netui:selectOption value="01/29/63" />
        <netui:selectOption value="01/30/63" />
    </netui:select>
    <li><netui:textArea dataSource="pageFlow.textAreaDate"><netui:formatDate  pattern="${pageFlow.nullValue}"/></netui:textArea></li>
    <li><netui:textBox dataSource="pageFlow.textDate"><netui:formatDate  pattern="${pageFlow.nullValue}"/></netui:textBox></li>
    </ul>
    </body>
</html>

  
