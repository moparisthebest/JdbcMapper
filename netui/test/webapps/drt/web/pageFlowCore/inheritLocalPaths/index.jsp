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

        This test ensures that local paths can be inherited from a base class page flow, when
        <code>inheritLocalPaths=true</code> on <code>@Jpf.Controller</code>.
        <br/>
        <br/>
        <netui:anchor action="doInheritLocalPaths">try with <code>inheritLocalPaths=true</code></netui:anchor>
        <br/>
        <netui:anchor action="doNoInheritLocalPaths">try without <code>inheritLocalPaths=true</code></netui:anchor>
    </netui:body>
</netui:html>

  

