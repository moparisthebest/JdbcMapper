<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base target="${pageFlow.nullValue}" />
    </head>
    <body>
    <h4>Null binding in the base tag</h4>
    <p style="color:green">
    This test simply binds the base tag to a value that results in null.  In the base 
    found in the head, there should not be a target.
    </p>
    </body>
</netui:html>

  
