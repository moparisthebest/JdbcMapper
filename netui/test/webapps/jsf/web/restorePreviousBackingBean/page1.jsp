<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>


<html>
    <head>
    </head>
    <body>
        <f:view>
            <h:form>
                text: <h:inputText value="#{backing.foo}" />
                <br/>
                <h:commandLink action="goCur" value="stay here, using navigateTo (backing bean is restored)" />
                <br/>
                <h:commandLink action="begin" value="stay here, using a path (backing bean is not restored)" />
                <br/>
                <h:commandLink action="go2" value="go to page 2" />
            </h:form>
        </f:view>
    </body>
</html>

  
