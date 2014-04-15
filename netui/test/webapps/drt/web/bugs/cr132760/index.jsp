<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
    <p style="color:#339900;">This test will result in an error message because there is a &lt;netui:button> that
    points to an action that doesn't exist.  This is reported as an error.
    <hr>
        <netui:form action="post">
            <netui:button type="submit" action="buttonPost" value="Post"></netui:button>
        </netui:form>
        
    </body>
</netui:html>

  
