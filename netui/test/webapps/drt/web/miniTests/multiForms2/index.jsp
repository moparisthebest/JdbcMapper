<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <netui:form action="foo">
            foo: <netui:textBox dataSource="actionForm.fooText"/>
            <netui:button/>
            <br/>
        </netui:form>
        <br>
        <netui:form action="bar">
            bar: <netui:textBox dataSource="actionForm.barText"/>
            <netui:button/>
            <br/>
        </netui:form>
        Last input: <netui:span value="${pageFlow.text}"/>
        <br/>
    </body>
</netui:html>

  
