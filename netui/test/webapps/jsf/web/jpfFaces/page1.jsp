<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head> <title>Page Flow / JSF</title> </head>
    <body bgcolor="white">
    <h3>Page Flow / JSF: page 1</h3>
    <f:view>
        <h:form id="go2form">
          	<h:inputText id="foo" value="#{backing.foo}"/>
            <br/>
            <h:commandLink id="go2button" action="#{backing.commandHandler2}" value="go to page2 (pass a form)" />
                - type "stay" to make the CommandHandler return null
            <br/>
            <h:commandLink id="go3button" action="#{backing.commandHandler3}" value="go to page3 (no form)" />
            <br/>
            <h:commandLink id="go4button" action="go4" value="go to page4 (no CommandHandler)" />
        </h:form>
    </f:view>
</HTML>  
