<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Format Error Reporting</h4>
    <p style="color:green">There are two sets of test, the first verify errors in the FormatDate tag.
    <br>
    The second verify that error reporting works against the HTML tags.
    For each HTML tag that allows a formatter, verify that the error is
    being reported.  Each of these should produce an in-line error and also an error in the collected
    errors at the end of the page.
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:formatDate /> -- No Parent</li>
    <li><netui:span value="01/28/63"><netui:formatDate stringInputPattern="x//ssd" /></netui:span> -- bad input pattern</li>
    <li><netui:span value="01/28/63"><netui:formatDate pattern="x//ssd" /></netui:span> -- bad pattern</li>
    <li><netui:span value="bad-date"><netui:formatDate /></netui:span> -- bad date</li>
    <li><netui:span value="${pageFlow.charValue}"><netui:formatDate /></netui:span> -- bad type</li>
    </ul>
    <hr>
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
    </netui:body>
</netui:html>

  
