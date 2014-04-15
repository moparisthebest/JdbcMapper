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

        <netui:anchor action="throw1">throw Exception1</netui:anchor> (@Jpf.Catch has message but no messageKey)<br/>
        <netui:anchor action="throw2">throw Exception2</netui:anchor> (@Jpf.Catch has message and messageKey="foo")<br/>
        <netui:anchor action="throw3">throw Exception3</netui:anchor> (@Jpf.Catch has messageKey="foo" but no message)<br/>
        <netui:anchor action="throw4">throw Exception4</netui:anchor> (@Jpf.Catch has message but no messageKey)<br/>
        <netui:anchor action="throw5">throw Exception5</netui:anchor> (@Jpf.Catch has message and messageKey="foo")<br/>
        <netui:anchor action="throw6">throw Exception6</netui:anchor> (@Jpf.Catch has messageKey="foo" but no message)<br/>
    </netui:body>
</netui:html>

  

