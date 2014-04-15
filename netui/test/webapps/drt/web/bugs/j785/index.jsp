<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        This test confirms that there are no exceptions when using a button to override the form
        action, where the button adds a request parameter that <i>looks like a deeply-nested
        property</i> (like "foo.bar", below).  This was blowing up in commons-beanutils, which was
        trying to resolve the property.
        <br/>
        <br/>
        <netui:form action="begin">
            <netui:button value="submit" action="begin">
                <netui:parameter name="foo.bar" value="bar"/>
            </netui:button>
        </netui:form>
    </netui:body>
</netui:html>

  

