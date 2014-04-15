<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head> <title>Page Flow / JSF / NavigateTo</title> </head>
    <body bgcolor="white">
    <h3>Page Flow / JSF / NavigateTo: page 2</h3>
    <f:view>
        <h:form>
            <h:commandLink id="go1" action="go1" value="go to page 1" /> (won't restore its backing bean)
            <br/>
            <h:commandLink id="goPrev" action="goPrev" value="go to previous page" /> (will restore its backing bean)
        </h:form>
    </f:view>
</HTML>  
