<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<html>
    <head>
    </head>
    <body>
        This is a test to ensure that sending a form bean from a JSF page to an action (where the
        action specifies useFormBean=<i>some member variable</i>) causes the page flow form bean
        member variable to be replaced with the one sent from the JSF page.

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
