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

        <netui:anchor action="simpleAction">forward to an action through a @Jpf.SimpleAction</netui:anchor>
        <br/>
        <netui:anchor action="conditionalForward">forward to an action through a @Jpf.ConditionalForward (on a @Jpf.SimpleAction)</netui:anchor>
        <br/>
        <netui:anchor action="forward">forward to an action through a @Jpf.Forward (on a method action)</netui:anchor>
        <br/>
        <netui:anchor action="toSharedFlowAction">forward to a shared flow action</netui:anchor>
        <br/>
        <netui:anchor action="toAnotherPageFlowAction">forward to an action in another page flow</netui:anchor>
    </netui:body>
</netui:html>
