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
             <f:loadBundle basename="faces.facesSmoke.Text" var="Bundle" />
              <h:form>
                <h:panelGrid  cellpadding="0" cellspacing="0" columns="1" width="600pt" columnClasses="titleBar">
                    <h:outputText value="Bundle Tests" />
                    <f:verbatim escape="false"><hr width="600pt" /></f:verbatim>
                </h:panelGrid>
                <h:outputLink value="Controller.jpf" >
                    <h:outputText value="Navigate Home" styleClass="normal" />
                </h:outputLink><br />
                <h:outputText value="Bundle" styleClass="normal" />
                <f:subview id="sub1">
                    <h:panelGrid columns="1">
                        <h:outputText value="#{Bundle.message}" styleClass="normal" />
                        <h:outputFormat value="#{Bundle.replacement}" styleClass="normal"> 
                            <f:param value="Replacement Value From Param" />
                        </h:outputFormat>
                    </h:panelGrid>
                </f:subview>
               </h:form>
        </f:view>
    </body>
</html>

  

  
