<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<html>
    <head>
    </head>
    <body>
        <f:view>
            <h:form>
                <h:commandButton action="#{backing.checkFields}" value="check fields"/>
                <br/>
                page flow is of type: <h:outputText binding="#{backing.pageFlowField}"/>
                <br/>
                shared flow is of type: <h:outputText binding="#{backing.sharedFlowField}"/>
                <br/>
            </h:form>
        </f:view>
    </body>
</html>

  
