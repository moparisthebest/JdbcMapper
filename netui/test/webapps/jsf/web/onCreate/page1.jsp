<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>


<html>
    <head>
    </head>
    <body>
        <f:view>
            <h:form>
                <h:panelGrid>
                    <h:outputText value="#{pageFlow.URI}" style="font-weight: bold"/>
                    <h:outputText value="Messages:"/>
                    <h:messages/>
                    <h:outputText value="#{backing.foo}"/>
                    <h:commandLink action="go2" value="go to page 2" />
                </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>

  
