<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null binding in the FormatNumber tag</h4>
    <p style="color:green">This test verifies the results of binding to a null value the attributes
    of the <b>FormatNumber</b> tag.  Both the <b>pattern</b> and <b>type</b> attributes will report
    an error.  The final two <b>country</b> and </b>language</b> ignore the null value.
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:span value="12345"><netui:formatNumber pattern="${pageFlow.nullValue}" /></netui:span> -- pattern</li>
    <li><netui:span value="12345"><netui:formatNumber type="${pageFlow.nullValue}" /></netui:span> -- type</li>
    <li><netui:span value="12345"><netui:formatNumber country="${pageFlow.nullValue}" /></netui:span> -- country</li>
    <li><netui:span value="12345"><netui:formatNumber language="${pageFlow.nullValue}" /></netui:span> -- language</li>
    </ul>
    </netui:body>
</netui:html>

  
