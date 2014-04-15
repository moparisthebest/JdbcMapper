<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>

<netui:html>
    <head>
        <title>
            Portal Scoping Test
        </title>
    </head>
    <body>
        data: <b><netui:span value="${pageFlow.data}"/></b>
        <p>
        <netui:form action="showResults" targetScope="a">
	    new data: <netui:textBox tagId="tb" dataSource="pageFlow.data"/>
	    <netui:button value="submit"/>
        </netui:form>
    </body>
</netui:html>