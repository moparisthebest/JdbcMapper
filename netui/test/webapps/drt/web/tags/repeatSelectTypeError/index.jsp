<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
  <head>
    <title>SelectOption without Repeater Type</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <netui:body>
    <h4>SelectOption without Repeater Type</h4>
        <netui:form action="post">
        <table>
        <tr><td>
        <netui:select dataSource="pageFlow.resultsOne" optionsDataSource="${pageFlow.opts}" repeater="true" size="3" 
                multiple="true">
            <netui:selectOption value="${container.item.optionValue}" styleClass="normalAttr">
                <netui:span value="${container.item.name}" />
            </netui:selectOption>
            <netui:selectOption repeatingType="null" value="null-opt" styleClass="normalAttr">
                <netui:span value="[Nothing]" />
            </netui:selectOption>
        </netui:select>
        </td></tr>
      <tr><td><netui:button value="Post" /></td></tr>
        <table>
        </netui:form>
  </netui:body>
</netui:html>

  
