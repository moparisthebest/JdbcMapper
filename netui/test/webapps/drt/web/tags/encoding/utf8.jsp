<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>UTF-8 Multibyte Test</title>
<netui:base />
</head>
<body>
<h1>UTF-8 Multibyte Test</h1>
<p>
Request Character Encoding: <%= request.getCharacterEncoding() %>
<br>
Response Character Encoding: <%= response.getCharacterEncoding() %>
</p>

<br>
pageFlow.foo...
<netui:span value="${pageFlow.foo}"/>
<br>
Page Flow processing...
<netui:span value="${pageFlow.barUTF8}"/>

<br>
JSP processing...
<%
    StringBuilder result = new StringBuilder(256);
    String name = "foo";
    String value = request.getParameter(name);
    if (value != null) {
        try {
            //
            // encode the bytes correctly for a Java unicode String
            // ...depends on the environment.
            //
            byte[] bytes = value.getBytes("ISO-8859-1");
            result.append("\n<br>Request parameter " + name + " encoded using "
                          + "UTF8: " + new String(bytes, "UTF8"));
        } catch (Exception e) {
            result.append("\nException: " + e.getMessage());
        }
    }
%>
<%= result.toString() %>

<p>
Return <netui:anchor action="begin">home</netui:anchor> or back to the
<netui:anchor action="beginUTF8">UTF-8 tests</netui:anchor>.
</p>

</body>
</html>

