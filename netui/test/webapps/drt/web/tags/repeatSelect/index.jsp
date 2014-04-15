<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<netui:html>
  <head>
    <title>Basic Repeating Select Test</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <body>
    <h4>Basic Repeating Select Test</h4>
        <netui:form action="post">
        <table>
        <tr><td>
        <netui:select dataSource="pageFlow.resultsOne" optionsDataSource="${pageFlow.opts}" repeater="true" size="3"
                multiple="true">
            <netui:selectOption repeatingType="option" value="${container.item.optionValue}" styleClass="normalAttr">
                <netui:span value="${container.item.name}" />
            </netui:selectOption>
            <netui:selectOption repeatingType="null" value="null-opt" styleClass="normalAttr">
                <netui:span value="[Nothing]" />
            </netui:selectOption>
        </netui:select>
        </td></tr>
        <tr><td>
        <netui:select dataSource="pageFlow.resultsTwo" optionsDataSource="${pageFlow.opts}" repeater="true"
                nullable="true">
            <c:if test="${container.metadata.optionStage}">
                <netui:selectOption repeatingType="option" value="${container.item.optionValue}" styleClass="normalAttr">
                    <netui:span value="${container.item.name}" />
                </netui:selectOption>
            </c:if>
            <c:if test="${container.metadata.nullStage}">
                <netui:selectOption repeatingType="null" value="null-opt" styleClass="normalAttr">
                    <netui:span value="[Nothing]" />
                </netui:selectOption>
             </c:if>
        </netui:select>
        </td></tr>
        <tr><td><netui:button value="Post" /></td></tr>
        <table>
        </netui:form>
  </body>
</netui:html>

  
