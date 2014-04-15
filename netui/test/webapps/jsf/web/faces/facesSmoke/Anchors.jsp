<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<html>
    <head>
        <title>Anchor - Test Web</title>
        <link rel="stylesheet" href="styles.css" type="text/css" />
    </head>
    <body>
        <f:view>
              <h:form id="formBase">
                <h:panelGrid  cellpadding="0" cellspacing="0" columns="1" width="600pt" columnClasses="titleBar">
                    <h:outputText value="Anchor and Button Tests" />
                    <f:verbatim escape="false"><hr width="600pt" /></f:verbatim>
                </h:panelGrid>
                
                <h:panelGrid style="margin:5,0,5,0" columns="1">
                <h:outputLink value="Controller.jpf" >
                    <h:outputText value="Navigate Home" styleClass="normal" />
                </h:outputLink>
                </h:panelGrid>
                
                <h:outputText value="Links" styleClass="sectionTitle" />
                <h:panelGrid columns="2" width="400pt" style="margin:5,0,5,0">
                    <h:commandLink styleClass="normal" >
                        <h:outputText value="Command Link One" styleClass="normal" />            
                        <f:param name="foo" value="link1-bar" />
                        <f:param name="blee" value="link1-baz" />
                    </h:commandLink>
                    <h:commandLink styleClass="normal" >
                        <f:actionListener type="faces.facesSmoke.ActionListen" />
                        <h:outputText value="Command Link Two" styleClass="normal" />            
                        <f:param name="foo" value="link2-bar" />
                        <f:param name="blee" value="link2-baz" />
                    </h:commandLink>
                </h:panelGrid>

                <h:panelGrid columns="1" width="400pt" style="margin:5,0,5,0">
                
                <h:outputText value="Image Button" styleClass="sectionTitle" />
                <h:commandButton type="button" image="update.gif" />

                <h:outputText value="Command Button" styleClass="sectionTitle" />
                <h:commandButton value="Submit this Form" >
                    <f:param name="foo" value="button-bar" />
                    <f:param name="blee" value="button-baz" />
                </h:commandButton>
                </h:panelGrid>
               </h:form>
               
               <hr />
                <h:outputText value="Test of Attribute" >
                    <f:attribute name="styleClass" value="normal" />
                </h:outputText>
        </f:view>
        <hr />
        <%
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            String foo =  req.getParameter("foo");
            String blee =  req.getParameter("blee");
            if (foo == null)
                foo = "[Not Defined]";
            if (blee == null)
                blee = "[Not Defined]";
        %>
        Foo: <%= foo %><br />
        Blee: <%= blee %><br />
    </body>
</html>

