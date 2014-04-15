<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
        <title>Image - Test Web</title>
        <link rel="stylesheet" href="styles.css" type="text/css" />
    </head>
    <body>
        <f:view>
              <h:form>
                <h:panelGrid  cellpadding="0" cellspacing="0" columns="1" width="600pt" columnClasses="titleBar">
                    <h:outputText value="Image Tests" />
                    <f:verbatim escape="false"><hr width="600pt" /></f:verbatim>
                </h:panelGrid>
                <h:outputLink value="Controller.jpf" >
                    <h:outputText value="Navigate Home" styleClass="normal" />
                </h:outputLink><br />
                <h:outputText value="Converter Tag" styleClass="sectionTitle" />
                <h:panelGrid columns="3" styleClass="normal">
                    <h:outputText value="Date:" /><h:outputText id="date" value="#{PageFlow.date}"><f:convertDateTime pattern="dd-MMM-yyyy" /></h:outputText> 
                        <h:message  styleClass="validationMessage" for="date" showDetail="true" showSummary="true"/>
                    <h:outputText value="Number:" /><h:outputText id="number" value="#{PageFlow.number}"><f:convertNumber currencyCode="usd" /></h:outputText> 
                        <h:message  styleClass="validationMessage" for="number" showDetail="true" showSummary="true"/>
                    <h:outputText value="No Formatting Date:" /><h:outputText value="#{PageFlow.date}" />
                        <h:message  styleClass="validationMessage" for="date" showDetail="true" showSummary="true"/>
                    <h:outputText value="No Formatting Number:" /><h:outputText value="#{PageFlow.number}" />
                        <h:message  styleClass="validationMessage" for="date" showDetail="true" showSummary="true"/>
                </h:panelGrid>
              </h:form>
        </f:view>
    </body>
</html>

  

  
