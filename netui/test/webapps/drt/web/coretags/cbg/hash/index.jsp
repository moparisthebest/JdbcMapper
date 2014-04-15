<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui:html>
  <head>
    <title>HashMap CheckBoxGroup</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal {color: #cc9999;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #99cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <body>
    <h4>HashMap CheckBoxGroup</h4>
    <p style="color:green">This is a test of the CheckBoxGroup repeating
    against a HashMap.  We use JSTL to filter out the key "junk" key.  This
    test verifies repeating CheckBoxGroups working against a HashMap and
    using JSTL inside the body of the repeater.</p>
        <netui:form action="post">
            <table width="200pt">
            <caption class="normalAttr">CheckBox Group</caption>
            <netui:checkBoxGroup dataSource="pageFlow.resultsOne"
		optionsDataSource="${pageFlow.opts}" repeater="true">
                <c:if test="${container.item.key != 'junk'}">
                   <tr align="center"><td align="right" width="25%">
                      <netui:checkBoxOption value="${container.item.key}" /></td>
                   <td align="left"><netui:span value="${container.item.value}" />
                  </td></tr>
	       </c:if>
            </netui:checkBoxGroup>
            </table>
	    <netui:button value="submit"/>
        </netui:form>
  </body>
</netui:html>

  
