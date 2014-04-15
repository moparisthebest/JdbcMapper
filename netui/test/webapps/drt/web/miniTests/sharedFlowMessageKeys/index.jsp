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

        <span style="color:red"><netui:errors/></span>
        <br/>
        <br/>

        <netui:form action="submit">
            <netui:button value="submit to page flow"/>
            <netui:button value="submit to shared flow" action="sf.submit"/>
        </netui:form>

        <netui:anchor action="sf.throwException1">sf.throwException1</netui:anchor>
        <br/>
        <netui:anchor action="sf.throwException2">sf.throwException2</netui:anchor>
    </netui:body>
</netui:html>

  

