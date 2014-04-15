<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Page Flow Scoping
        </title>
    </head>
    <body>
        <h3>Page Flow Scoping</h3>
        
        <a href="frames.jsp">frames</a>
        <br>
        <a href="a/FlowA.jpf?jpfScopeID=a">go to FlowA.jpf, in scope A</a>
    </body>
</netui:html>
