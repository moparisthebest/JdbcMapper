<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null Binding to the Content tag</h4>
    <p style="color:green">This is a simple test of null binding to the attributes of the content tag.
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:content value="content" defaultValue="${pageFlow.nullValue}" /> -- default value</li>
    <li><netui:content value="${pageFlow.nullValue}" /> -- value</li>
    </ul>
    </netui:body>
</netui:html>

  
