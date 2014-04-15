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

        <netui:anchor action="goNested">goNested</netui:anchor>
            - should produce an onCreate and an onDestroy for the nested page flow.
        <br/>
        <netui:anchor action="goNestedWithDelay">goNestedWithDelay</netui:anchor>
            - this is for a manual test of rapid-clicking the link.  Should produce a bunch of
              onCreates, then a bunch of onDestroys.
        <br/>
        <netui:anchor action="clearMessages">clear messages</netui:anchor>
        <br/>
        <netui:anchor action="begin">refresh</netui:anchor>

        <br/>
        <br/>
        <hr/>
        Lifecycle:<br/>
        <ul>
        <netui-data:repeater dataSource="sessionScope.messages">
            <netui-data:repeaterItem><li>${container.item}</li></netui-data:repeaterItem>
        </netui-data:repeater>
        </ul>
    </netui:body>
</netui:html>

  

