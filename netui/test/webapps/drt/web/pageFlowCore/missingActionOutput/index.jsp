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

        This test ensures that the correct exceptions are thrown for missing action output
        (MissingActionOutputException) and null action output (NullActionOutputException).

        <br/>
        <br/>
        exception: <netui:exceptions/>

        <br/>
        <br/>
        <netui:anchor action="missingActionOutput">missingActionOutput</netui:anchor>
        <br/>
        <netui:anchor action="nullActionOutput">nullActionOutput</netui:anchor>
    </netui:body>
</netui:html>

  

