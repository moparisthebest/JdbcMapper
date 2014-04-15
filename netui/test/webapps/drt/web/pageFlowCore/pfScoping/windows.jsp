<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html>
    <head>
        <title>
            Multiple Windows Example
        </title>
    </head>
    <body>
        <h3>Multiple Windows Example</h3>
        <netui:anchor href="a/FlowA.jpf" target="_a">
            <netui:parameter name="jpfScopeID" value="a"/>
            Page Flow A (scope "a")
        </netui:anchor>
        <br>
        <netui:anchor href="b/FlowB.jpf" target="_b">
            <netui:parameter name="jpfScopeID" value="b"/>
            Page Flow B (scope "b")
        </netui:anchor>
        <br>
        <br>
        
        <netui:tree dataSource="pageFlow.tree" selectionAction="updateTree" tagId="theTree" imageRoot='<%= request.getContextPath() + "/resources/images" %>'/>
        <br>
    </body>
</netui:html>
