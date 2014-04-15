<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui:html>
  <head>
    <title>J242 - HashMaps with checkBoxGroup and radioButtonGroups</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal {color: #cc9999;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #99cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <netui:body>
  
        <netui:form action="post">
           <netui:select dataSource="pageFlow.resultsOne" optionsDataSource="${pageFlow.opts}" repeater="true">
                   <tr align="center"><td align="right" width="25%">
                   <td align="left"><netui:selectOption value="${container.item.value}" />
                  </td></tr>
            </netui:select>
            </table>
	   
        </netui:form>
  </netui:body>
</netui:html>

  
