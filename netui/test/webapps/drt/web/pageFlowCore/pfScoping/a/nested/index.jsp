<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Nested
        </title>
    </head>
    <body>
        <h3>Nested</h3>
        
        <netui:anchor action="begin">
            do nothing
        </netui:anchor>
        
        <br>
        
        <netui:anchor action="done">
            Exit nested
        </netui:anchor>
        
        <br>
        <br>
        <span style="color: red">
            <a href="../../b/FlowB.jpf?jpfScopeID=b">go to scope B</a>
        </span>
        
    </body>
</netui:html>
