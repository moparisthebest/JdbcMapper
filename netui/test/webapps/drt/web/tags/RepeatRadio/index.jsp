<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
  <head>
    <title>Basic Repeating Checkbox Group</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal {color: #cc9999;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #99cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <body>
    <h4>Basic Repeating Checkbox Group</h4>
        <netui:form action="post">
        <table>
        <tr><td>
            <table width="200pt">
            <caption class="normalAttr">RadioButton Group</caption>
            <netui:radioButtonGroup dataSource="pageFlow.resultsOne" optionsDataSource="${pageFlow.opts}" repeater="true">
                <tr align="center"><td align="right" width="25%"><netui:radioButtonOption value="${container.item.optionValue}" /></td>
                    <td align="left"><netui:span styleClass="${container.item.style}" value="${container.item.name}" />
                </td></tr>
            </netui:radioButtonGroup>
            </table>
        </td></tr>
        <tr><td>
            <table>
            <caption class="normalAttr">RadioButton Group Two</caption>
            <tr><td>
            <netui:radioButtonGroup dataSource="pageFlow.resultsTwo" optionsDataSource="${pageFlow.opts}" repeater="true" >
                 <netui:span styleClass="${container.item.style}"  value="${container.item.name}" />
                 <netui:radioButtonOption value="${container.item.optionValue}" />&nbsp;                
            </netui:radioButtonGroup>
            </td></tr>
            </table>
        </td></tr>
        <tr><td><netui:button value="Post" /></td></tr>
        </table>
        </netui:form>
  </body>
</netui:html>

  
