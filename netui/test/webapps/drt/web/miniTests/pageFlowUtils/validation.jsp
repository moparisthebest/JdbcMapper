<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="org.apache.beehive.netui.pageflow.PageFlowUtils"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            PageFlowUtils - validation errors
        </title>
    </head>
    <body>
        <h3>PageFlowUtils - validation errors</h3>
        
        <%
            PageFlowUtils.addValidationError( "a", "msg1", request );
            PageFlowUtils.addValidationError( "b", "msg2", "arg1", request );
            Object[] args = new Object[]{ "arg1", "arg2", "arg3" };
            PageFlowUtils.addValidationError( "c", "msg3", args, request );
            PageFlowUtils.addActionError( request, "d", "msg3", new Object[]{ "arg1", "arg2", "arg3" } );
            PageFlowUtils.addActionErrorExpression( request, "e", "The page flow is ${pageFlow.URI}.  Some args are: {0}, {1}, {2}.", new Object[]{ "arg1", "arg2", "arg3" } );
        %>


        <code>&lt;netui:error&gt</code>:
        <blockquote>
            <netui:error key="a"/>
            <br>
            <netui:error key="b"/>
            <br>
            <netui:error key="c"/>
            <br>
            <netui:error key="d"/>
            <br>
            <netui:error key="e"/>
            <br>
        </blockquote>

        <code>&lt;netui:errors&gt</code>:
        <blockquote>
            <netui:errors/>
        </blockquote>
        
                       
        <br>
        <br>
        <netui:anchor action="begin">go back</netui:anchor>
    </body>
</netui:html>
