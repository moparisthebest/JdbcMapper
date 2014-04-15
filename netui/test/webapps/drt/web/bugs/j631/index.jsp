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

        The "required" validation error comes out of the message bundle associated with the
        form bean.  There is no default message bundle associated with the page flow.
        <br/>
        <br/>

        <netui:form action="submit">
            (leave this blank and click Submit): <netui:textBox dataSource="actionForm.foo"/><netui:error key="foo"/>
            <br/>
            <netui:button value="Submit"/>
        </netui:form>
    </netui:body>
</netui:html>

  

