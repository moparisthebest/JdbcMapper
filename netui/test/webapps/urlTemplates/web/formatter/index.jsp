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
    <netui:anchor href="/urlTemplates/formatter/index.jsp?foo=bar">/urlTemplates/formatter/index.jsp?foo=bar</netui:anchor><br>
    <netui:anchor href="/urlTemplates/formatter/secure.jsp?foo=bar">/urlTemplates/formatter/secure.jsp?foo=bar</netui:anchor><br>
    <netui:anchor href="http://apache.org/">
        http://apache.org/?foo=bar
        <netui:parameter name="foo" value="bar"/>
    </netui:anchor><br>
    image.gif: <netui:image src="image.gif"/><br>
    /urlTemplates/formatter/image.gif: <netui:image src="/urlTemplates/formatter/image.gif"/><br>
    secureImage.gif: <netui:image src="secureImage.gif"/><br>
    /urlTemplates/formatter/secureImage.gif: <netui:image src="/urlTemplates/formatter/secureImage.gif"/><br>
  </body>
</netui:html>
