<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
        <title>PanelGrid - Test Web</title>
        <link rel="stylesheet" href="styles.css" type="text/css" />
    </head>
    <body>
        <f:view>
              <h:form>
                <h:panelGrid  cellpadding="0" cellspacing="0" columns="1" width="600pt" columnClasses="titleBar">
                    <h:outputText value="PanelGrid Tests" />
                    <f:verbatim escape="false"><hr width="600pt" /></f:verbatim>
                </h:panelGrid>
                <h:outputLink value="Controller.jpf" >
                    <h:outputText value="Navigate Home" styleClass="normal" />
                </h:outputLink>
                <h:outputText value="Bordered Table, Backgroup color" styleClass="normal" />
                <h:panelGrid bgcolor="#c0c0c0" width="600pt" columns="2" border="1" cellpadding="0" cellspacing="0" columnClasses="normal">
                    <h:outputText value="Object One" />
                    <h:outputText value="Object Two" /> 
                    <h:outputText value="Object Three" /> 
                    <h:outputText value="Object Four" /> 
                </h:panelGrid>
                <h:panelGrid cellpadding="5" cellspacing="5"><f:verbatim escape="false"><hr width="590pt" /></f:verbatim></h:panelGrid>
                <h:outputText value="Alternating Row Colors" styleClass="normal" />
                <h:panelGrid width="600pt" cellpadding="0" cellspacing="0" columnClasses="normal" rowClasses="rowOne, rowTwo">
                    <h:outputText value="Object One" />
                    <h:outputText value="Object Two" /> 
                    <h:outputText value="Object Three" /> 
                    <h:outputText value="Object Four" /> 
                </h:panelGrid>
                <h:panelGrid cellpadding="5" cellspacing="5"><f:verbatim escape="false"><hr width="590pt" /></f:verbatim></h:panelGrid>
                <h:outputText value="Header/Footers" styleClass="normal" />
                <h:panelGrid width="600pt" cellpadding="0" cellspacing="0" columnClasses="normal" columns="2" headerClass="titleBar" footerClass="footerBar">
                    <f:facet name="header">
                        <h:outputText value="Header" />
                    </f:facet>
                    <f:facet name="footer">
                        <h:outputText value="Footer" />
                    </f:facet>
                    <h:outputText value="Object One" />
                    <h:outputText value="Object Two" /> 
                    <h:outputText value="Object Three" /> 
                    <h:outputText value="Object Four" /> 
                </h:panelGrid>
                <h:panelGrid cellpadding="5" cellspacing="5"><f:verbatim escape="false"><hr width="590pt" /></f:verbatim></h:panelGrid>
                <h:outputText value="panelGroup" styleClass="normal" />
                <h:panelGrid width="600pt" cellpadding="0" cellspacing="0" columnClasses="normal" columns="2" rowClasses="rowOne">
                    <h:panelGroup>
                        <h:outputText value="Object One " style="color:#ff00ff;" /><h:outputText value="Object Two" style="color:#ff9900;" />
                    </h:panelGroup>
                    <h:outputText value="Object Three" /> 
                </h:panelGrid>
             </h:form>
        </f:view>
    </body>
</html>

  

  
