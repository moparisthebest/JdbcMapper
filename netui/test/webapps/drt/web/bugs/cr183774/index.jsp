<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <body>
    <p style="color:#009900;">This test contains two hidden fields inside a form with the tagId attribute set.  The
    result should be an id attribute set in the resulting html.  No need to postback.
    <hr>
    <netui:form action="testAction">
            <netui:hidden dataSource="actionForm.bar" tagId="barId"/>
            <netui:hidden dataSource="actionForm.foo" tagId="fooId"/>
            <netui:button type="submit" value="TestAction"></netui:button>
        </netui:form>
    </body>
</netui:html>

  
