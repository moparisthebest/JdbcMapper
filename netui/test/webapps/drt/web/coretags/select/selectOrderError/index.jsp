<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<netui:html>
  <head>
    <title>Error in repeating select</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <netui:body>
    <h4>Error in Repeating Select</h4>
    <p style="color:green">In this test we set invalid values for the
    stage.  This verifies that they are reported.
    </p>
    <netui:form action="post">
        <table>
        <tr><td>
	<b>Null, Default, Options -> results1</b><br>
	<!-- 5 options, default value and null -->
        <netui:select dataSource="pageFlow.results1" defaultValue="default Value"
		optionsDataSource="${pageFlow.opts}" repeater="true" size="5"
		repeatingOrder="nulls, TheDefault, option"		
                multiple="true" nullable="true">
            <c:if test="${container.metadata.optionStage}">
                <netui:selectOption repeatingType="option"
		   value="${container.item.optionValue}" styleClass="normalAttr">
                   <netui:span value="${container.item.name}" />
                </netui:selectOption>
            </c:if>
            <c:if test="${container.metadata.defaultStage}">
                <netui:selectOption repeatingType="default"
	   	   value="${container.item}" styleClass="normalAttr">
                   <netui:span value="${container.item}" />
                </netui:selectOption>
            </c:if>
            <netui:selectOption repeatingType="null" value="null-opt"
		 styleClass="normalAttr">
                <netui:span value="[Nothing]" />
            </netui:selectOption>
        </netui:select>
        <tr><td><netui:button value="Post" /></td></tr>
        <table>
     </netui:form>
  </netui:body>
</netui:html>

  
