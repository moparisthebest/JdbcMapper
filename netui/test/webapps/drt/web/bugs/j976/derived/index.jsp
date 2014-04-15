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

        This test ensures that the netui:anchor and netui:button tags both render the correct URL
        for an action when:
        <ul>
            <li>A base class action is hit through a derived page flow.</li>
            <li>The action forwards to a page that's local to the base page flow.</li>
            <li>The page has a link to another base class action.</li>
            <li><code>inheritLocalPaths=true</code> on the <code>@Jpf.Controller</code> annotation.</li>
        </ul>
        <br/>
        <netui:anchor action="baseAction1">baseAction1</netui:anchor>
    </netui:body>
</netui:html>

  

