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

        Page 1
        <br/>
        <br/>
        <netui:anchor action="curPage">curPage</netui:anchor>
            (will restore whatever query string was passed to the page originally)
        <br/>
        <netui:anchor action="goPage2">goPage2</netui:anchor>
        <br/>
        <br/>
        param: <b>${param.foo}</b>
    </netui:body>
</netui:html>

  

