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

        <netui:anchor action="begin">begin</netui:anchor>

        <netui:form action="submit">
            foo: <netui:textBox dataSource="actionForm.foo"/><netui:error key="foo"/>
            <br/>
            <netui:button value="submit"/>
        </netui:form>
    </netui:body>
</netui:html>

  

