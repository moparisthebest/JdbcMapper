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

        <p>
            This test ensures that two forms on the same page can submit to two actions that accept
            the same form bean type, and that form-bean-specific messages from the bean (defined in
            <code>@Jpf.FormBean</code>) are applied in each case.
        </p>
        <netui:form action="submit">
            foo: <netui:textBox dataSource="actionForm.foo"/><netui:error key="foo"/>
            <br/>
            <netui:button value="submit"/>
        </netui:form>
        <netui:form action="submit2">
            bar: <netui:textBox dataSource="actionForm.bar"/><netui:error key="bar"/>
            <br/>
            <netui:button value="submit2"/>
        </netui:form>
    </netui:body>
</netui:html>

  

