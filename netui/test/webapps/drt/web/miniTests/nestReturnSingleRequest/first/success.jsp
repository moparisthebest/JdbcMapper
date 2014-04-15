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

        return val from nested page flow: <b>${pageInput.retVal}</b>
        <br/>
        <netui:anchor action="begin">start over</netui:anchor>

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

  

