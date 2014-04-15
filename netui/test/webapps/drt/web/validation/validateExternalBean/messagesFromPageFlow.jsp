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

        Validation Messages (Message from Page Flow)
        <br/>
        <br/>

        <netui:form action="submitBean1">
            foo: <netui:textBox dataSource="actionForm.foo" defaultValue="x"/><netui:error key="foo"/>
            <br/>
            bar: <netui:textBox dataSource="actionForm.bar" defaultValue="x"/><netui:error key="bar"/>
            <br/>
            <netui:button value="Submit Bean1"/> (uses message resources from this page flow)
        </netui:form>

    </netui:body>
</netui:html>

  

