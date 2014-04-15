<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base />
    </head>
    <netui:body>
    <h4>Null binding in the FormatDate tag</h4>
    <p style="color:green">Binding various attributes of the formatDate tag to null values.  The 
    first two bindings should produce errors.  These are the <b>pattern</b> and <b>stringInputPattern</b>.
    The second two, don't produce errors.  These are the <b>country</b> and <b>language</b> attributes.
    These have defaults so do not produce errors.
    <br>
    This is a single page test.  
    </p>
    <ul>
    <li><netui:span value="01/28/63"><netui:formatDate pattern="${pageFlow.nullValue}" /></netui:span> -- pattern</li>
    <li><netui:span value="01/28/63"><netui:formatDate stringInputPattern="${pageFlow.nullValue}" /></netui:span> -- stringInputPattern</li>
    </ul>
    <li>
    <li><netui:span value="01/28/63"><netui:formatDate country="${pageFlow.nullValue}" /></netui:span> -- country</li>
    <li><netui:span value="01/28/63"><netui:formatDate language="${pageFlow.nullValue}" /></netui:span> -- language</li>
    </ul>
    </netui:body>
</netui:html>

  
