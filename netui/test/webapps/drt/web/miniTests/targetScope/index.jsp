<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>Page Flow Named Scopes</h3>
        current scope: <b><%= request.getParameter( "jpfScopeID" ) %></b>
        <br/>
        string: <b>${pageFlow.str}</b>
        <br/>
        <br/>
        <br/>
        <netui:form action="submit">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button value="submit to current scope (form)"/>
        </netui:form>
        <netui:form action="submit" targetScope="">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button value="submit to default scope (form)"/>
        </netui:form>
        <netui:form action="submit" targetScope="a">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button value="submit to scope 'a' (form)"/>
        </netui:form>
        <netui:form action="submit" targetScope="b">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button value="submit to scope 'b' (form)"/>
        </netui:form>
        <netui:form action="submit">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button action="submit" value="submit to current scope (button)"/>
        </netui:form>
        <netui:form action="submit">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button action="submit" targetScope="" value="submit to default scope (button)"/>
        </netui:form>
        <netui:form action="submit">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button action="submit" targetScope="a" value="submit to scope 'a' (button)"/>
        </netui:form>
        <netui:form action="submit">
            <netui:textBox dataSource="pageFlow.str"/>
            <netui:button action="submit" targetScope="b" value="submit to scope 'b' (button)"/>
        </netui:form>
        <netui:anchor action="display">display current scope</netui:anchor>
        <br/>
        <netui:anchor action="display" targetScope="">display default scope</netui:anchor>
        <br/>
        <netui:anchor action="display" targetScope="a">display scope 'a'</netui:anchor>
        <br/>
        <netui:anchor action="display" targetScope="b">display scope 'b'</netui:anchor>
        <br/>
        <br/>
        <netui:imageAnchor action="display" src="current.jpg">display current scope</netui:imageAnchor>
        <br/>
        <netui:imageAnchor action="display" targetScope="" src="default.jpg">display default scope</netui:imageAnchor>
        <br/>
        <netui:imageAnchor action="display" targetScope="a" src="a.jpg">display scope 'a'</netui:imageAnchor>
        <br/>
        <netui:imageAnchor action="display" targetScope="b" src="b.jpg">display scope 'b'</netui:imageAnchor>
        <br/>
        <br/>
        <netui:anchor action="killMe" targetScope="">remove default scope</netui:anchor>
        <br/>
        <netui:anchor action="killMe" targetScope="a">remove scope 'a'</netui:anchor>
        <br/>
        <netui:anchor action="killMe" targetScope="b">remove scope 'b'</netui:anchor>
        <br/>
    </netui:body>
</netui:html>

  
