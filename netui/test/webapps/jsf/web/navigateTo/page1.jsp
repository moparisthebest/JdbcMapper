<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head> <title>Page Flow / JSF / NavigateTo</title> </head>
    <body bgcolor="white">
    <h3>Page Flow / JSF / NavigateTo: page 1</h3>
    <f:view>
        <h:form>
            Text in backing file: <b><h:inputText value="#{backing.message}"/></b>
            <br/>
            Text in view state: <b><h:inputText/></b>
            <h:commandButton id="stayHere" value="submit" />
            <br>
            <h:commandLink id="go2" action="go2" value="go to page 2" />
        </h:form>
    </f:view>
</HTML>  
