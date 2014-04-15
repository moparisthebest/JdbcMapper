<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
  <head>
    <title>URL Templates Test</title>
  </head>
  <body>
    <netui:anchor action="begin"><i>begin action</i></netui:anchor><br>
    <netui:anchor action="secure"><i>secure action</i></netui:anchor><br>
    <netui:anchor action="unsecure"><i>unsecure action</i></netui:anchor><br>
    <netui:anchor href="index.jsp">index.jsp</netui:anchor><br>
    <netui:anchor href="secure.jsp">secure.jsp</netui:anchor><br>
    <netui:anchor href="/urlTemplates/testUrlTemplates/index.jsp?foo=bar">/urlTemplates/testUrlTemplates/index.jsp?foo=bar</netui:anchor><br>
    <netui:anchor href="/urlTemplates/testUrlTemplates/secure.jsp?foo=bar">/urlTemplates/testUrlTemplates/secure.jsp?foo=bar</netui:anchor><br>
    <netui:anchor href="http://apache.org/">
        http://apache.org/?foo=bar
        <netui:parameter name="foo" value="bar"/>
    </netui:anchor><br>
    image.gif: <netui:image src="image.gif"/><br>
    /urlTemplates/testUrlTemplates/image.gif: <netui:image src="/urlTemplates/testUrlTemplates/image.gif"/><br>
    secureImage.gif: <netui:image src="secureImage.gif"/><br>
    /urlTemplates/testUrlTemplates/secureImage.gif: <netui:image src="/urlTemplates/testUrlTemplates/secureImage.gif"/><br>
  </body>
</netui:html>
