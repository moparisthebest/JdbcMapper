<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Mock Portal Smoke Test
        </title>
    </head>
    <body>
        <h3>Mock Portal Smoke Test</h3>
        
        <netui:form action="submit">
            data: <netui:textBox tagId="tb" dataSource="pageFlow.data"/>
            <netui:button value="submit"/>
        </netui:form>

        <netui:anchor action="goNested">goNested</netui:anchor>
    </body>
</netui:html>
