<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Page Flow B
        </title>
    </head>
    <body>
        <h3>Page Flow B</h3>
        scope <b><netui:span value="${param.jpfScopeID}"/></b>
        <br>
                
        <netui:form action="begin">
            foo: <netui:textBox dataSource="pageFlow.foo"/>
            <br>
            <netui:button value="update"/>
        </netui:form>
        
        <netui:anchor action="begin">refresh</netui:anchor>
        <br>
        <br>
        <span style="color: red">
            <a href="../a/FlowA.jpf?jpfScopeID=a">go to scope A</a>
        </span>
        <br>
        <span style="color: red">
            <a href="../a/nested/NestedController.jpf?jpfScopeID=a">go back into nested A</a>
        </span>
    </body>
</netui:html>
