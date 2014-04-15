<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head> <title>Page Flow / JSF / Controls in Backing Classes</title> </head>
    <body bgcolor="white">
    <h3>Page Flow / JSF / Controls in Backing Classes</h3>
    <f:view>
        <h:form>
            Message from control: <b><h:outputText value="#{backing.message}"/></b>
            <br>
            <h:commandLink id="doit" action="#{backing.doit}" value="hit a control" />
        </h:form>
    </f:view>
</HTML>  
