<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html idScope="html">
    <head>
        <netui:base/>
        <netui:scriptHeader />
    </head>
    <netui:body>
        <p style="color:green">Basic test of reporting error from JavaScript to the server using
	XmlHttpRequest.
        </p>
<netui:scriptBlock placement="after">
reportNetUIError("BVT Test of ClientSide Error reporting");
</netui:scriptBlock>
    </netui:body>
</netui:html>

  
