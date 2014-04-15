<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<html>
    <head>
        <title>CustomRenderer - Test Web</title>
        <link rel="stylesheet" href="styles.css" type="text/css" />
    </head>
    <body style="border: 1px dotted;">
        <f:view>
            <h:form id="pageForm" >
            <h:panelGrid columns="1" width="600pt" columnClasses="titleBar">
                <h:outputText value="Custom Renderer Tests"></h:outputText>
            </h:panelGrid>
            <h:panelGrid columns="2" width="600pt">
                    
                <h:outputLink value="goPanels.do" styleClass="normal"><h:outputText value="Panel"/></h:outputLink>
                <h:outputText value="Test of the PanelGrid and related controls" styleClass="normal"/>
                
                <h:outputLink value="goImages.do" styleClass="normal"><h:outputText value="Images" /></h:outputLink>
                <h:outputText value="Test of the GraphicImage" styleClass="normal"/>

                <h:outputLink value="goAnchors.do" styleClass="normal"><h:outputText value="Anchors" /></h:outputLink>
                <h:outputText value="Test of Anchors and Buttons" styleClass="normal"/>

                <h:outputLink value="goConvert.do" styleClass="normal"><h:outputText value="Conversions" /></h:outputLink>
                <h:outputText value="Test of Converters" styleClass="normal"/>
                
                <h:outputLink value="goFormOne.do" styleClass="normal"><h:outputText value="Form One" /></h:outputLink>
                <h:outputText value="Test the Form Fields" styleClass="normal"/>
                
                <h:outputLink value="goFormTwo.do" styleClass="normal"><h:outputText value="Form Two" /></h:outputLink>
                <h:outputText value="Test the Select/Radio/Checkbox Form Fields" styleClass="normal"/>

                <h:outputLink value="goBundle.do" styleClass="normal"><h:outputText value="Bundle" /></h:outputLink>
                <h:outputText value="Test of the Bundle" styleClass="normal"/>

                <h:outputLink value="goDataTable.do" styleClass="normal"><h:outputText value="Data Table" /></h:outputLink>
                <h:outputText value="Test of the DataTable" styleClass="normal"/>

            </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>

  
