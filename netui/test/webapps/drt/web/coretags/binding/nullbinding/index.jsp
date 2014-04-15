<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null Binding to the BindingUpdateErrors tag</h4>
    <p style="color:green">This test will binding the expression to a null value.  The expression attribute
    is required so an error will be reported.
    <br>
    This is a single page test.
    </p>
    <ul>
    <li><netui:bindingUpdateErrors expression="${pageFlow.nullValue}" /> -- Binding Errors</li>
    </ul>
    </netui:body>
</netui:html>

  
