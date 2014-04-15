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
        SharedFlowController field: <b><netui:label value="${pageFlow.sharedFlowField}"/></b>
        <br/>
        <br/>
        <netui:anchor action="sf2.sharedFlow2Action">sharedFlow2Action</netui:anchor>
 
    </body>
</netui:html>

  
