<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
        <title>Form One - Test Web</title>
        <link rel="stylesheet" href="styles.css" type="text/css" />
    </head>
    <body>
        <f:view>
              <h:form>
                <h:panelGrid  cellpadding="0" cellspacing="0" columns="1" width="600pt" columnClasses="titleBar">
                    <h:outputText value="Form One Tests" />
                    <f:verbatim escape="false"><hr width="600pt" /></f:verbatim>
                </h:panelGrid>
                <h:outputLink value="Controller.jpf" >
                    <h:outputText value="Navigate Home" styleClass="normal" />
                </h:outputLink><br />
                <h:outputText value="Form One" styleClass="sectionTitle" />
                <br />
                <h:panelGrid columns="2" columnClasses="columnRight, columnLeft" width="600pt">
                    <h:outputText value="SelectBooleanCheckbox" /> 
                    <h:selectBooleanCheckbox id="selectBooleanCheckbox" value="#{PageFlow.formTwo.checkbox}" />
                    
                    <h:outputText value="SelectManyCheckbox" />
                    <h:selectManyCheckbox value="#{PageFlow.formTwo.manyCheckbox}">
                        <f:selectItem itemValue="item1" itemLabel="Item One" /> 
                        <f:selectItem itemValue="item2" itemLabel="Item Two" />
                    </h:selectManyCheckbox> 
                    
                    
                    <h:outputText value="SelectManyCheckbox-SelectItems" />
                    <h:selectManyCheckbox value="#{PageFlow.formTwo.manyCheckboxTwo}">
                        <f:selectItems value="#{PageFlow.selectItems}" />
                    </h:selectManyCheckbox>
                    
                    <h:outputText value="SelectManyListbox" />
                    <h:selectManyListbox size="3" style="color: #000099;font-family:Verdana;font-size:8pt;"> 
                        <f:selectItem itemValue="list1" itemLabel="List One" /> 
                        <f:selectItem itemValue="list2" itemLabel="List Two" /> 
                        <f:selectItem itemValue="list3" itemLabel="List Three" /> 
                    </h:selectManyListbox>
                    
                    <h:outputText value="SelectManyMenu" />
                    <h:selectManyMenu style="color: #000099;font-family:Verdana;font-size:8pt;" > 
                        <f:selectItem itemValue="Menu1" itemLabel="Menu One" /> 
                        <f:selectItem itemValue="Menu2" itemLabel="Menu Two" /> 
                        <f:selectItem itemValue="Menu3" itemLabel="Menu Three" /> 
                    </h:selectManyMenu>
                     
                    <h:outputText value="SelectOneListbox" />
                    <h:selectOneListbox style="color: #000099;font-family:Verdana;font-size:8pt;"> 
                        <f:selectItem itemValue="One-list1" itemLabel="One-List One" /> 
                        <f:selectItem itemValue="One-list2" itemLabel="One-List Two" /> 
                        <f:selectItem itemValue="One-list3" itemLabel="One-List Three" /> 
                    </h:selectOneListbox>
                    
                    <h:outputText value="SelectOneMenu" />
                    <h:selectOneMenu value="#{PageFlow.menuOne}" style="color: #000099;font-family:Verdana;font-size:8pt;"> 
                        <f:selectItem itemLabel="One-Menu-1" itemValue="One-Menu One" />
                        <f:selectItem itemLabel="One-Menu-2" itemValue="One-Menu Two" />
                        <f:selectItem itemLabel="One-Menu-3" itemValue="One-Menu Three" />
                    </h:selectOneMenu>
                    
                    <h:outputText value="SelectOneMenu" /> 
                    <h:selectOneRadio> 
                        <f:selectItem itemLabel="One-Radio-1" itemValue="One-Radio One" />
                        <f:selectItem itemLabel="One-Radio-2" itemValue="One-Radio Two" />
                        <f:selectItem itemLabel="One-Radio-3" itemValue="One-Radio Three" />
                    </h:selectOneRadio>
                </h:panelGrid>
                <h:commandButton value="Submit" actionListener="#{CustomRenderBacking.formTwoSubmit}" />
                <hr>
                <h:messages styleClass="validationMessage" />
              </h:form>
        </f:view>
    </body>
</html>

