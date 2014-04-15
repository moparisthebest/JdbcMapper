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

        Validation Messages (Messages from Bean)
        <br/>
        <br/>

        <netui:form action="submitBean2">
            foo: <netui:textBox dataSource="actionForm.foo" defaultValue="x"/><netui:error key="foo"/>
            <br/>
            bar: <netui:textBox dataSource="actionForm.bar" defaultValue="x"/><netui:error key="bar"/>
            <br/>
            baz: <netui:textBox dataSource="actionForm.baz" defaultValue="x"/><netui:error key="baz"/>
            <br/>
            <netui:button value="Submit Bean2"/> (uses message resources from the form bean)
        </netui:form>

    </netui:body>
</netui:html>

  

