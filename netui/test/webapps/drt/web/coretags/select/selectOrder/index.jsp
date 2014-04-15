<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<netui:html>
  <head>
    <title>Order Repeating Select</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <netui:body>
    <h4>Order Repeating Select</h4>
    <p style="color:green">This will test some combination of the ordered
    repeating options.  The repeating select allows you to specify the
    order of options within the select.  In this case, we order them in
    various ways.
    </p>
    <netui:form action="post">
        <table>
        <tr><td width="50%">
	<b>Null, Default, Options -> results1</b><br>
	<!-- 5 options, default value and null -->
        <netui:select dataSource="pageFlow.results1" defaultValue="default Value"
		optionsDataSource="${pageFlow.opts}" repeater="true" size="6"
		repeatingOrder="null, default, option"		
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
	</td><td width="50%">
	<b>Options -> results2</b><br>
	<!-- 5 options, default value and null -->
        <netui:select dataSource="pageFlow.results2" defaultValue="default Value"
		optionsDataSource="${pageFlow.opts}" repeater="true" size="6"
		repeatingOrder="option"		
                multiple="true" nullable="true">
            <c:if test="${container.metadata.optionStage}">
                <netui:selectOption repeatingType="option"
		   value="${container.item.optionValue}" styleClass="normalAttr">
                   <netui:span value="${container.item.name}" />
                </netui:selectOption>
            </c:if>
        </netui:select>
	</td></tr>
	<tr>
	<td width="50%">
	<b>Options,Null -> results3</b><br>
	<!-- 5 options, default value and null -->
        <netui:select dataSource="pageFlow.results3" defaultValue="default Value"
		optionsDataSource="${pageFlow.opts}" repeater="true" size="6"
		repeatingOrder="option, null"		
                multiple="true" nullable="true">
            <c:if test="${container.metadata.optionStage}">
                <netui:selectOption repeatingType="option"
		   value="${container.item.optionValue}" styleClass="normalAttr">
                   <netui:span value="${container.item.name}" />
                </netui:selectOption>
            </c:if>
            <netui:selectOption repeatingType="null" value="null-opt"
		 styleClass="normalAttr">
                <netui:span value="[Nothing]" />
            </netui:selectOption>

        </netui:select>
	</td>
	<td width="50%">
	<b>DataSource,Options,Null -> results4</b><br>
	<!-- 5 options, default value and null -->
        <netui:select dataSource="pageFlow.results4" defaultValue="default Value"
		optionsDataSource="${pageFlow.opts}" repeater="true" size="6"
		repeatingOrder="dataSource,option,null"		
                multiple="true" nullable="true">
            <c:if test="${container.metadata.optionStage}">
                <netui:selectOption repeatingType="option"
		   value="${container.item.optionValue}" styleClass="normalAttr">
                   <netui:span value="${container.item.name}" />
                </netui:selectOption>
            </c:if>
            <c:if test="${container.metadata.dataSourceStage}">
                <netui:selectOption repeatingType="dataSource"
		   value="${container.item}" styleClass="normalAttr">
                   <netui:span value="${container.item}" />
                </netui:selectOption>
            </c:if>
            <netui:selectOption repeatingType="null" value="null-opt"
		 styleClass="normalAttr">
                <netui:span value="[Nothing]" />
            </netui:selectOption>

        </netui:select>
	</td>
	</tr>
        <tr><td colspan="2"><netui:button value="Post" /></td></tr>
        <table>
     </netui:form>
  </netui:body>
</netui:html>

  
