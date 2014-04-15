<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Errors In FormatNumber</h4>
    <p style="color:green">This test verifies the basic errors in the <b>formatNumber</b> tag.  The first
    is reported when the tag doesn't have a <b>Formattable</b> as a parent.  In the second, the value
    is not a number and in the third the pattern is not legal.
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:formatNumber /> -- no parent</li>
    <li><netui:span value="bad.number"><netui:formatNumber /></netui:span> -- bad number value</li>
    <li><netui:span value="12345"><netui:formatNumber pattern=";##0.0#"  /></netui:span> -- bad pattern value</li>
    </ul>
    </netui:body>
</netui:html>

  
