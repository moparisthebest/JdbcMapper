<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        action-forward-handler: <b>${pageFlow.actionForwardHandler}</b><br/>
        exceptions-handler: <b>${pageFlow.exceptionsHandler}</b><br/>
        login-handler: <b>${pageFlow.loginHandler}</b><br/>
        <br/>
        The following action normally requires the role "HandlerTest", but we've set a LoginHandler
        to guarantee that we appear to be in that role.  If we return to this page, that means an
        error was <i>not</i> thrown, i.e., this test succeeded.
        <br/>
        <netui:anchor action="roleRequiredAction">hit it</netui:anchor>
    </netui:body>
</netui:html>

  

