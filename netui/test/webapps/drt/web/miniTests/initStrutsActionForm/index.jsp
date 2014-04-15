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

        This test ensures that you can use the expected Struts request attribute to access a
        Struts ActionForm that has been passed to the page (and that the bean is not wrapped in an
        AnyBeanActionForm).

        <br/>
        <br/>
        myForm class: <b>${myForm.class.name}</b>

        <br/>
        <br/>
        <netui:form action="submit">
            foo: <netui:textBox dataSource="actionForm.foo"/>
        </netui:form>
    </netui:body>
</netui:html>

  

