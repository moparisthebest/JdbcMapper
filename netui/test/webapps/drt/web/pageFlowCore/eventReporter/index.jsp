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

        <netui:form action="throwException">
            <netui:button value="throwException (with form bean)"/>
        </netui:form>
        <netui:anchor action="removeMe">removeMe</netui:anchor>
        <br/>
        <br/>

        <netui:anchor action="finish">finish, and clear the history</netui:anchor>
        <br/>
        <hr/>
        Events:
        <ol>
            <netui-data:repeater dataSource="pageFlow.history">
                <netui-data:repeaterItem>
                    <li>${container.item}</li>
                </netui-data:repeaterItem>
            </netui-data:repeater>
        </ol>
        <br/>
        <hr/>
        Registration message: <b>${pageFlow.registrationMessage}</b>
    </netui:body>
</netui:html>

  

