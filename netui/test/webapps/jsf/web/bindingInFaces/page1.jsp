<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<html>
    <head>
    </head>
    <body>
        <f:view>



            <h:form>
                &#35;{pageInput.somePageInput}: <h:outputText value="#{pageInput.somePageInput}"/>
                <br/>
                &#35;{pageFlow.someProperty}: <h:inputText value="#{pageFlow.someProperty}"/>
                <br/>
                &#35;{sharedFlow.sf.someProperty}: <h:inputText value="#{sharedFlow.sf.someProperty}"/>
                <br/>
                &#35;{backing.someProperty}: <h:inputText value="#{backing.someProperty}"/>
                <br/>
                <br/>
                <h:commandButton value="postback"/> This ensures that the page inputs don't get lost over postback.
                <br/>
                <h:commandButton value="get page input from backing" action="#{backing.getPageInput}"/>
                    <span style="color:red"/><h:outputText binding="#{backing.outputField}" id="outputField"/></span>
                    This ensures that the backing bean has access to page inputs.
                    <br/>
                <h:commandButton action="submit" value="submit"/> Send everything to a page flow action.
            </h:form>
        </f:view>
    </body>
</html>
