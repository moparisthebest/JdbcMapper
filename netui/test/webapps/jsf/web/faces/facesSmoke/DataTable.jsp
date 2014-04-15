<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
    <head>
        <title>DataTable</title>
        <link rel="stylesheet" href="styles.css" type="text/css" />
    </head>
    <body>
        <f:view>
          <h:form>
                <h:panelGrid  cellpadding="0" cellspacing="0" columns="1" width="600pt" columnClasses="titleBar">
                    <h:outputText value="DataTable Tests" />
                    <f:verbatim escape="false"><hr width="600pt" /></f:verbatim>
                </h:panelGrid>
                <h:outputLink value="Controller.jpf" >
                    <h:outputText value="Navigate Home" styleClass="normal" />
                </h:outputLink><br />

            <h:dataTable id="table" rows="20" value="#{PageFlow.list}" var="l" border="1" 
                cellpadding="2" cellspacing="0" headerClass="dataHeaders"
                columnClasses="dataColumnOne, dataColumnTwo" footerClass="dataHeaders" 
                >  
            
            <!-- The columns contain facets which act as headers -->      
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Name"/>
                </f:facet>
                <f:facet name="footer">
                    <h:outputText value="Last Name"/>
                </f:facet>
                    <h:outputText value="#{l.name}"/>
            </h:column>
            
            <!-- The columns contain facets which act as headers -->      
            <h:column>
                <f:facet name="header">
                    <h:outputText value="Type"/>
                </f:facet>
                <f:facet name="footer">
                    <h:outputText value="Last Type"/>
                </f:facet>
                    <h:outputText value="#{l.type}"/>
            </h:column>
            </h:dataTable>
        </h:form>
    </f:view>
    </body>
</html>
