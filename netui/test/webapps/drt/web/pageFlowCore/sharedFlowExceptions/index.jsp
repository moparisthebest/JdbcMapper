<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        Message: <b><netui:span value="${pageInput.message}"/></b>
        <br/>
        <br/>
        <netui:anchor action="throwSharedFlow1Exception">throwSharedFlow1Exception</netui:anchor>
        <br/>
        <netui:anchor action="throwSharedFlow2Exception">throwSharedFlow2Exception</netui:anchor>
        <br/>
        <br/>
        <i>sharedFlow1 is </i><b><netui:span value="${sharedFlow.sf1.displayName}"/></b>
        <br/>
        <i>sharedFlow2 is </i><b><netui:span value="${sharedFlow.sf2.displayName}"/></b>
    </netui:body>
</netui:html>

  

