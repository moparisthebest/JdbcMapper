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

        This test ensures that if an exception is handled by forwarding to an error page, and the error page itself throws an exception, the second exception is thrown out to the container, to avoid an infinite loop of exception handling.
        <br/>
        <br/>
        Here, an exception is thrown in the <code>throwException</code> action, and one is also thrown on error.jsp.
        <br/>
        <br/>
        <netui:anchor action="throwException">throwException</netui:anchor>
    </netui:body>
</netui:html>

  

