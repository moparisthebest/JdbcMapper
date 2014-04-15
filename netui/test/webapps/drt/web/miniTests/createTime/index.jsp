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

        This test ensures that the <code>getCreateTime</code> API exists and works properly
        (as much as can be verified in an automated test).
        <br/>
        <br/>
        Was the page flow created less than 10 seconds ago?
        <b>${pageFlow.currentTime - pageFlow.createTime < 10000}</b>

    </netui:body>
</netui:html>

  

