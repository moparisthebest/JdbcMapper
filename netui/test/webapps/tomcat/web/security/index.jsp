<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>


<netui:html>
<head>
<title>Security/Proxies</title>
</head>
<body bgcolor="white">

<h3>Secure check</h3>

<netui:form tagId="form" action="unsecure">

    <netui:anchor action="secure" >Secure</netui:anchor>
    <netui:anchor action="unsecure" >Unsecure</netui:anchor>
</netui:form>


</body>
</netui:html>
