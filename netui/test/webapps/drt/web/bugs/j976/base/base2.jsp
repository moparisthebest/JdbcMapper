<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>base2.jsp</h3>

        current page flow is: <b>${pageFlow.URI}</b>
        <br/>
        <br/>
        <netui:anchor action="begin">begin</netui:anchor>
        <br/>
        <netui:form action="begin"><netui:button value="begin"/></netui:form>
    </netui:body>
</netui:html>

  

