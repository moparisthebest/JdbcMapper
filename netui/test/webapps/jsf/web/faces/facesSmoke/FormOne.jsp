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
                <h:panelGrid columns="3" styleClass="normal" columnClasses="columnRight, columnLeft" width="600pt">
                    <h:outputLabel for="text"><h:outputText value="Text:"/></h:outputLabel>
                    <h:inputText id="text" value="#{PageFlow.formOne.text}" size="25" styleClass="normal">
                        <f:valueChangeListener type="faces.facesSmoke.ValueChangeListen" />
                    </h:inputText>     
                    <h:message  styleClass="validationMessage" for="text" showDetail="true"/>
                    
                    <h:outputLabel for="textArea" ><h:outputText value="Text Area:"/></h:outputLabel>
                    <h:inputTextarea id="textArea" value="#{PageFlow.formOne.textArea}" cols="25" rows="10" 
                        style="color: #000099;font-family:Verdana; font-size:8pt; margin:5,5,5,5; "> </h:inputTextarea>
                    <h:outputText value=""/>
                    
                    <h:outputLabel for="password" value="Secret:"><h:outputText value="Secret:"/></h:outputLabel>
                    <h:inputSecret id="password" value="#{PageFlow.formOne.password}"> </h:inputSecret>
                    <h:outputText value=""/>

                    <h:outputLabel for="textInt"  >
                        <h:outputText value="Binding to Number (#{PageFlow.formOne.textInt / 100}%)"/>
                    </h:outputLabel>
                    <h:inputText id="textInt" value="#{PageFlow.formOne.textInt}" styleClass="normal" size="3">
                        <f:validateLongRange maximum="100" minimum="0" />
                    </h:inputText>
                    <h:message  styleClass="validationMessage" for="textInt" showDetail="true"/>
                </h:panelGrid>
                <h:inputHidden value="PostFromHidden"> </h:inputHidden>
                <h:commandButton value="Submit" />
                <hr>
                <h:messages styleClass="validationMessage" />
              </h:form>
        </f:view>
    </body>
</html>
  
