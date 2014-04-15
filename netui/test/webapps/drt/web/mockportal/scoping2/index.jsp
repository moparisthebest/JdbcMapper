<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
    <head>
        <title>
            Portal Scoping Test 2
        </title>
    </head>
    <body>
        <h3>Portal Scoping Test 2</h3>
        
        <netui:form action="submit">
            data: <netui:textBox tagId="tb" dataSource="pageFlow.data"/>
            <netui:button value="submit"/>
        </netui:form>
        <p>
    </body>
</netui:html>
