<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <netui:form action="testAction">
            <netui:span value="Name:"/>
            <netui:textBox dataSource="actionForm.name"/>
            <netui:error key="name"/>
            <br>To throw the exception, enter a string that includes an X...
            <br>
            <netui:button type="submit" action="testAction" value="Test Action"/>
        </netui:form>
        <netui:anchor action="begin">begin</netui:anchor>
        <hr>
        Errors:
        <br>
        <netui:errors/>
    </netui:body>
</netui:html>

  