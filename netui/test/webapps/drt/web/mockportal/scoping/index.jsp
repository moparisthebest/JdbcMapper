<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
    <head>
        <title>
            Portal Scoping Test
        </title>
    </head>
    <body>
        <h3>Portal Scoping Test</h3>
        
        <netui:form action="submit" targetScope="a">
            data: <netui:textBox tagId="tb" dataSource="pageFlow.data"/>
            <netui:button value="submit"/>
        </netui:form>
        <p>
    </body>
</netui:html>
