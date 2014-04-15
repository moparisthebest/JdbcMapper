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

        This test verifies that we can initialize the form bean on a JSP even when the destination
        action accepts an <i>interface</i> as its form bean argument.  The previous action passes a
        concrete implementation of that interface on the Forward to this page.
        <br/>
        <br/>

        <netui:form action="submit">
            foo: <netui:textBox dataSource="actionForm.foo"/><netui:error key="foo"/>
        </netui:form>
    </netui:body>
</netui:html>

  

