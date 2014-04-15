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

        This test ensures that if a base class action forwards to another action, it forwards to the
        action in the derived page flow, if there is an overriding one there.
        <br/>
        <br/>
        <netui:anchor action="baseAction">baseAction</netui:anchor> (works)
        <br/>
        <netui:anchor action="fwdToBaseAction">fwdToBaseAction</netui:anchor> (busted, until this bug is fixed)
    </netui:body>
</netui:html>

  

