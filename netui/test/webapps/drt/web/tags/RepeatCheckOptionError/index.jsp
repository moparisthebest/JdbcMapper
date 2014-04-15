<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
  <head>
    <title>Repeating Checkbox Group Error</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal {color: #cc9999;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #99cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <netui:body>
    <h4>Repeating Checkbox Group Error</h4>
        <netui:form action="post">
        <table>
        <tr><td>
            <table width="200pt">
            <caption class="normalAttr">CheckBox Group</caption>
            <netui:checkBoxGroup dataSource="pageFlow.resultsOne" optionsDataSource="${pageFlow.opts}">
                <tr align="center"><td align="right" width="25%"><netui:checkBoxOption value="${container.item.optionValue}" /></td>
                    <td align="left"><netui:span styleClass="${container.item.style}" value="${container.item.name}" />
                </td></tr>
            </netui:checkBoxGroup>
            </table>
        </td></tr>
      <tr><td><netui:button value="Post" /></td></tr>
        </table>
        </netui:form>
  </netui:body></netui:html>

  
