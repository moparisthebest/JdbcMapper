<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Content Tag Values</h4>
    <p style="color:green">This fully tests the content tags presentation of values and use of the
    default value.
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:content value="${pageFlow.content}" /> -- Value Binding to Content</li>
    <li><netui:content value="${pageFlow.nullValue}" defaultValue="${pageFlow.defaultValue}" /> -- Null Value, Value From Default</li>
    <li><netui:content value="${pageFlow.content}" defaultValue="${pageFlow.defaultValue}" /> -- Content and Default</li>
    <li><netui:content value="${pageFlow.nullValue}" /> -- Null Value, no default</li>
    </ul>
    </netui:body>
</netui:html>

  
