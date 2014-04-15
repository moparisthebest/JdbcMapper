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

        <netui:anchor action="delegateSimple1">delegateSimple1</netui:anchor><br/>
        <netui:anchor action="delegateSimple2">delegateSimple2</netui:anchor><br/>
        <netui:anchor action="delegateMethod1">delegateMethod1</netui:anchor><br/>
        <netui:anchor action="delegateMethod2">delegateMethod2</netui:anchor><br/>

    </netui:body>
</netui:html>
