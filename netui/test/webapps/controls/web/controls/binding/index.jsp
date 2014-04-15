<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
</head>

<netui:body>
    <p>Test the binding of a control interface to a implementation using various forms of
       explicitly setting the implementation for an interface.</p>
    <ul>
        <li>The <tt>BindingTestControl</tt>'s implementation is specified via a <tt>@BaseProperties</tt> annotation  in the page flow.</li>
        <li>The <tt>DefaultBindingTestControl</tt>'s implementation is specified in its <tt>@ControlInterface</tt> annotation with
            the <tt>defaultBinding</tt> annotation member.</li>
        <li>The <tt>ExternalBindingTestControl</tt>'s implementation is specified in its interface using the <tt>@BaseProperties</tt> annotation.</li>
    </ul>

    <h3>Test Results</h3>
    <table border="1">
        <tr><td>BindingTestControl Results:</td><td>'${requestScope.BindingTestControlStatus}'</td></tr>
        <tr><td>DefaultBindingTestControl Results:</td><td>'${requestScope.DefaultBindingTestControlStatus}'</td></tr>
        <tr><td>ExternalBindingTestControl Results:</td><td>'${requestScope.ExternalBindingTestControlStatus}'</td></tr>
    </table>
</netui:body>
</html>
