<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <h4>Button Null Binding</h4>
    <p style="color:green">There are two sets of tests.  The first set set all the attributes
    that should result in errors [action, tagId and type].  In the last set of tests, the first
    simply ignores the default value.  The second and third both will output the default value.
    <br>
    This is a single page test.  
    </p>
    <hr>
    <ul>
    <li><netui:button action="${pageFlow.nullValue}"/> -- action</li>
    <li><netui:button tagId="${pageFlow.nullValue}"/> -- tagId</li>
    <li><netui:button type="${pageFlow.nullValue}"/> -- type</li>
    </ul>
    <hr>
    <ul>
    <li><netui:button value="${pageFlow.nullValue}"/> -- value</li>
    <li><netui:button tabindex="${pageFlow.nullValue}"/> -- tabindex</li>
    <li><netui:button accessKey="${pageFlow.nullValue}"/> -- accessKey</li>
    </ul>
    <hr>
    
    </netui:body>
</netui:html>

  
