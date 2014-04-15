<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
        <title>restoreQueryString Test</title>
    </head>
    <body>
        <h3>restoreQueryString Test</h3>

        foo: <b><%= request.getParameter( "foo" ) %></b>
        <br/>
        bar: <b><%= request.getParameter( "bar" ) %></b>

        <br/>
        <br/>
        <netui:anchor href="someAction.do?foo=hi">someAction.do?foo=hi</netui:anchor><br/>
        <netui:anchor action="rerunWithoutPreviousQueryString">rerunWithoutPreviousQueryString</netui:anchor><br/>
        <netui:anchor action="rerunWithPreviousQueryString">rerunWithPreviousQueryString</netui:anchor><br/>
        <netui:anchor href="rerunWithoutPreviousQueryString.do?bar=there">rerunWithoutPreviousQueryString.do?bar=there</netui:anchor><br/>
        <netui:anchor href="rerunWithPreviousQueryString.do?bar=there">rerunWithPreviousQueryString.do?bar=there</netui:anchor><br/>
        <netui:anchor href="rerunWithPreviousQueryString.do?foo=overridden">rerunWithPreviousQueryString.do?foo=overridden</netui:anchor><br/>
    </body>
</netui:html>

  
