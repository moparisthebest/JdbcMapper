<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        <netui:form action="submit">
            bar: <netui:textBox dataSource="actionForm.bar"/> <netui:errors/>
            <br/>
            <netui:button value="submit"/>
        </netui:form>
    </netui:body>
</netui:html>
