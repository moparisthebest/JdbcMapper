<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head> <title>Page Flow / JSF</title> </head>
    <body bgcolor="white">
    <h3>Page Flow / JSF: page 2</h3>
    <f:view>
        Message from page flow: <i><h:outputText id="msg" value="#{requestScope.message}"/></i>
        <br/>

        <h:form id="go1form" >
            <h:commandButton id="go1button" action="go1" value="back to page 1" />
        </h:form>
    </f:view>
</HTML>  
