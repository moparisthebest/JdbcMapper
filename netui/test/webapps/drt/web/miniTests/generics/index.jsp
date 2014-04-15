<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>

<html>
    <body>
        <h3>${pageFlow.URI}</h3>

        This page flow demonstrates an action that takes a generic-type-variable argument.
        <br/>
        <br/>
        current value: <b>${pageFlow.foo}</b>
        <netui:form action="submit">
            data: <netui:textBox dataSource="actionForm.foo"/>
            <netui:button/>
        </netui:form>
    </body>
</html>
