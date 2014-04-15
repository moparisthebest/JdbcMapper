<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null binding in the FormatString tag</h4>
    <p style="color:green">There are two sets of tests here.  The first is a test of binding  null values to the 
    attributes.  Binding null to <b>pattern</b> results in errors, the others are ignored.  The final tests is 
    the only error raised by the <b>formatString</b> tag.    
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:span value="123456"><netui:formatString pattern="${pageFlow.nullValue}" /></netui:span> -- pattern</li>
    <li><netui:span value="123456"><netui:formatString pattern="###-###" country="${pageFlow.nullValue}" /></netui:span> -- country</li>
    <li><netui:span value="123456"><netui:formatString pattern="###-###" language="${pageFlow.nullValue}" /></netui:span> -- language</li>
    </ul>
    <hr>
    <ul>
    <li><netui:formatString pattern="###-###" country="${pageFlow.nullValue}" />-- no parent</li>
    </ul>
    </netui:body>
</netui:html>

  
