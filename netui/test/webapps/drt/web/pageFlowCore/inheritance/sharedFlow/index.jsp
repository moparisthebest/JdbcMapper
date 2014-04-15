<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h1>Shared Flow Action Inheritance</h1>

        <p>
            This test checks that delegating action mappings for actions and
            simple actions in shared flows behave correctly.
        </p>

        <netui:anchor action="toSimpleSharedFlowAction">forward to a shared flow simple action</netui:anchor>
        <br/>
        <netui:anchor action="toSharedFlowAction">forward to a shared flow action</netui:anchor>
        <br/>
        <netui:anchor action="toSimpleSuperSharedFlowAction">forward to an inherited shared flow simple action</netui:anchor>
        <br/>
        <netui:anchor action="toSuperSharedFlowAction">forward to an inherited shared flow action</netui:anchor>
        <br/>
    </netui:body>
</netui:html>
