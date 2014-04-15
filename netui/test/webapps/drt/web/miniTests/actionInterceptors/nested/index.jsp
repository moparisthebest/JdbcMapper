<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body bgcolor="#CCFFFF">
        <h3>${pageFlow.URI}</h3>

        Now we're in the injected nested page flow.  Click
        <netui:anchor action="done">done</netui:anchor>
        to return to the original action that was being raised.

        <br/>
        <br/>
        Was the original action run yet? <b>${requestScope.actionRun == null ? "no" : "yes" }</b>
    </netui:body>
</netui:html>

  

