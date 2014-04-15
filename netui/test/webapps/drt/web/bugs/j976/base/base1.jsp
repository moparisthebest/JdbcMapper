<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>base1.jsp</h3>

        current page flow is: <b>${pageFlow.URI}</b>
        <br/>
        <br/>
        <netui:anchor action="baseAction2">baseAction2</netui:anchor>
        <br/>
        <netui:form action="baseAction2"><netui:button value="baseAction2"/></netui:form>

        Both the anchor and the button should forward to base2.jsp, while remaining in the context
        of the derived page flow.

    </netui:body>
</netui:html>

  

