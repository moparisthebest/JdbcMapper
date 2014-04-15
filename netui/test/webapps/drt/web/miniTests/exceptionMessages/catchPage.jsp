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

        exceptions tag: <b><netui:exceptions showMessage="true" showStackTrace="false"/></b><br/>
        errors tag: <b><netui:errors/></b><br/>
        error tag (key="${pageFlow.class.name}$Exception1"): <b><netui:error key="${pageFlow.class.name}$Exception1"/></b><br/>
        error tag (key="foo"): <b><netui:error key="foo"/></b><br/>

        <br/>
        <netui:anchor action="begin">start over</netui:anchor>

    </netui:body>
</netui:html>

  

