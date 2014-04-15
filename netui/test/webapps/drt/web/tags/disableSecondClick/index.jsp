<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        This test merely verifies that the right JavaScript is rendered; it can't actually test the disabling behavior.
        <br/>
        <br/>
        <netui:form action="someAction">
            <netui:button value="submit"/>
            <br/>
            <netui:button value="submit (disableSecondClick)" disableSecondClick="true"/>
            <br/>
            <br/>
            <netui:button value="submit with action override" action="anotherAction"/>
            <br/>
            <netui:button value="submit with action override (disableSecondClick)" action="anotherAction" disableSecondClick="true"/>
            <br/>
            <br/>
            <netui:anchor formSubmit="true">submit</netui:anchor>
            <br/>
            <netui:anchor formSubmit="true" disableSecondClick="true">submit (disableSecondClick)</netui:anchor>
        </netui:form>

        <netui:anchor action="someAction">non-submit</netui:anchor>
        <br/>
        <netui:anchor action="someAction" disableSecondClick="true">non-submit (disableSecondClick)</netui:anchor>
        <br/>
        <br/>
        non-submit: <netui:imageAnchor action="someAction" src="cool.gif"/>
        <br/>
        non-submit (disableSecondClick): <netui:imageAnchor action="someAction" disableSecondClick="true" src="cool.gif"/>
    </netui:body>
</netui:html>

  

