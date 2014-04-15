<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>


<html>
    <head>
    </head>
    <body>
        <f:view>
            <h:form>
                <h:commandLink action="goPrev" value="go to page 1, using navigateTo (state is restored)" />
                <br/>
                <h:commandLink action="begin" value="go to page 1, using a path (state is not restored)" />
            </h:form>
        </f:view>
    </body>
</html>

  
