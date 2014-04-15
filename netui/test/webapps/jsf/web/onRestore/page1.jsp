<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head> <title>Page Flow / JSF / NavigateTo</title> </head>
    <body bgcolor="white">
    <h3>Page Flow / JSF / NavigateTo: page 1</h3>
    <f:view>
        <h:form>
            <h:panelGrid>
                <h:outputText style="font-style: italic" value="This test ensures that onRestore() is called before restoring a page through Jpf.NavigateTo.{currentPage,previousPage}, and that page inputs are updated and available during onRestore()."/>
                <h:outputText value="Messages:"/>
                <h:messages/>
                <h:commandLink id="goCurrentNoNewMessage" action="goCurrentNoNewMessage" value="stay on the current page, via navigateTo=Jpf.NavigateTo.currentPage, with no new page inputs" />
                <h:commandLink id="goCurrent" action="goCurrent" value="stay on the current page, via navigateTo=Jpf.NavigateTo.currentPage" />
                <h:commandLink id="go2" action="go2" value="go to page 2" />
            </h:panelGrid>
        </h:form>
    </f:view>
</html>  
