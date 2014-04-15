<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI} (scope: ${param.jpfScopeID})</h3>

        This test ensures that shared flows in different target scopes (controlled by the jpfScopeID
        parameter, or inferred in portlets) are independent.

        <br/>

        <netui:form action="begin">
            foo property in shared flow:
                <netui:textBox dataSource="sharedFlow.sf.foo"/>
                <netui:error key="foo"/>
            <br/>
            <netui:button value="submit"/>
        </netui:form>

        <netui:anchor action="begin">refresh</netui:anchor>
    </netui:body>
</netui:html>

  

