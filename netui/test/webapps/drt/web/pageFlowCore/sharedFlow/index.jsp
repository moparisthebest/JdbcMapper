<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
        <h3>Shared Flow Test</h3>

        message: <b><netui:label value="${pageInput.message}"/></b>
        <br/>
        SharedFlow field: <b><netui:label value="${pageFlow.sharedFlowField}"/></b>
        <br/>
        <br/>
        <netui:anchor action="pageFlowCore_sharedFlow_globalAction">pageFlowCore_sharedFlow_globalAction</netui:anchor>
        <br/>
        <netui:anchor href="hasSharedFlow1/HasSharedFlow1.jpf">hasSharedFlow1/HasSharedFlow1.jpf</netui:anchor>
    </body>
</netui:html>

  
