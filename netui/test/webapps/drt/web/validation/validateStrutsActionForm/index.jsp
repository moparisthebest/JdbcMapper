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

        This test ensures that declarative validation annotations (including form-bean-specific
        message resources) works correctly on beans that extend the raw Struts ActionForm.
        <br/>
        <br/>
        <netui:form action="submit">
            foo: <netui:textBox dataSource="actionForm.foo"/><netui:error key="foo"/>
            <br/>
            <netui:button value="submit"/>
        </netui:form>
    </netui:body>
</netui:html>

  

