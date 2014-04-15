<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        Causes Script error <br />
        <netui:anchor onMouseDown="alert(\'OnMOUSEDOWN\')" href="index.jsp">Script Error For onmousedown when formatted this way.</netui:anchor>
    <br />
        The following generate valid javascript:
    <br />
        <netui:anchor onMouseDown='alert("onmousedown")' href="index.jsp">No script error when double quotes switched with single quotes</netui:anchor>
        <a href="index.jsp" onmousedown="alert('onmousedown')">No script error when html anchor tag is used with the same formatting as netui:anchor which returns a script error</a>
        <a href="index.jsp" onmousedown='alert("onmousedown")'>No script error</a>
   </netui:body>
</netui:html> 