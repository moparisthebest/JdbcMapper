<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
    <body>
        action called: <netui:span value="${requestScope.actionMapping.path}"/>
        <br>
        scope: <netui:span value="${requestScope.actionMapping.scope}"/>
        <br>
        <br>
        <netui:anchor action="toAction">toAction</netui:anchor>
        <br>
        <netui:anchor action="toMe1">toMe1</netui:anchor>
        <br>
        <netui:anchor action="toMe2">toMe2</netui:anchor>
        <br>
        <netui:anchor action="mergeAction">mergeAction</netui:anchor>
        <br>
    </body>
</netui:html>

  
