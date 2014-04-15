<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<html>
    <head>
    </head>
    <body>
        This is  a test of sending a form bean to a Page Flow action in two ways:
        <ul>
            <li>through the "submitFormBean" attribute (f:attribute tag) inside of h:commandLink or h:commandButton</li>
            <li>through an <code>outputFormBean</code> attribute on @Jpf.RaiseAction in a backing file command handler</li>
        </ul>
        <f:view>
            <h:form>
                &#35;{backing.theForm.foo}: <h:inputText value="#{backing.theForm.foo}"/>
                <br/>
                <br/>
                <h:commandButton action="submit" value="send bean through attribute">
                    <f:attribute name="submitFormBean" value="backing.theForm"/>
                </h:commandButton>
                <br/>
                <h:commandButton action="#{backing.raisePageFlowAction}" value="send bean through command handler"/>
                <br/>
                <h:commandLink action="submit" value="send bean through attribute">
                    <f:attribute name="submitFormBean" value="backing.theForm"/>
                </h:commandLink>
                <br/>
                <h:commandLink action="#{backing.raisePageFlowAction}" value="send bean through command handler"/>
            </h:form>
        </f:view>
    </body>
</html>
