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

        <span style="color:red">
            <ul>
                <li><netui:error key="foo"/></li>
                <li><netui:error key="bar"/></li>
            </ul>
        </span>
        <br/>
        <br/>
        <netui:anchor action="validateMethod">validateMethod</netui:anchor>
        <br/>
        <netui:anchor action="validateAnnotations">validateAnnotations</netui:anchor>
    </netui:body>
</netui:html>

  

