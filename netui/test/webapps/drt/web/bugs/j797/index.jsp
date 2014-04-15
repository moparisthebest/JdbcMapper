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

        This test ensures that when an ActionInterceptor is used to "inject" a nested page flow,
        the original query parameters are restored when the desired action is finally run.

        <br/>
        <br/>

        <netui:anchor action="doRestoreQueryString">
            <netui:parameter name="foo" value="yes"/>
            doRestoreQueryString?foo=yes
        </netui:anchor>
        <br/>
        <netui:anchor action="doNotRestoreQueryString">
            <netui:parameter name="foo" value="yes"/>
            doNotRestoreQueryString?foo=yes
        </netui:anchor>
        (uses an interceptor that explicitly prevents parameters from being restored)

    </netui:body>
</netui:html>

  

