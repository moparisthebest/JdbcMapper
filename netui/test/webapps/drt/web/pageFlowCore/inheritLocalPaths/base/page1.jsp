<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}, Page 1</h3>

        Got here through an inherited @Jpf.SimpleAction.
        <br/>
        <br/>
        <netui:anchor action="goPage2">goPage2 (inherited @Jpf.Action Forward)</netui:anchor>
        <br/>
        <hr/>
        action count: <b>${pageFlow.actionCount}</b>
    </netui:body>
</netui:html>

  

