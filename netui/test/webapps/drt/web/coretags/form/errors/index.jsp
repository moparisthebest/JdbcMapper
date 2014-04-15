<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Form Errors</h4>
    <p style="color:green">This verifies the basic errors raised by the form tag.  
    </p>
    <ul style="color:green">
    <li>Nested Forms -- Forms may not be nested</li>
    <li>Bad Action -- Form has an invalid action</li>
    </ul>
    <p style="color:green">This verifies the basic errors raised by the form tag.  
    This is a single page test.  
    </p>
    <hr>
    <ul>
    <li><netui:form action="begin" style="display:inline"><netui:form action="begin" style="display:inline">
        </netui:form></netui:form><span> -- form inside a form</span></li>
    <li><netui:form action="badAction" style="display:inline"></netui:form><span> -- bad action</span></li>
    </ul>    
    </netui:body>
</netui:html>

  
