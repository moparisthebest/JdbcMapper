<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
    <head>
        <title>
            NestReturnSingleRequest
        </title>
    </head>
    <body>
        <h3>NestReturnSingleRequest</h3>
        
        <% request.getSession().removeAttribute( "messages" ); %>
        (This page clears the "messages" attribute which is used in the test.)
        <br/>
        <br/>
        <a href="first/Controller.jpf">start the test</a>
    </body>
</netui:html>
