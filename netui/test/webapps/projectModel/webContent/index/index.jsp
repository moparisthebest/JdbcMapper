<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<netui:html>
    <head>
        <title>Beehive NetUI Tests</title>
        <netui:base/>
        <link href="style.css" rel="stylesheet" type="text/css">
        <style type="text/css">
        .nameColumn {
            width: 140pt;
            vertical-align: top;
            text-align: right;
        }

        .descrColumn {
        }

        .diffColumn {
            width: 30pt;
            text-align: center;
            vertical-align: top;
        }

        .detailColumn {
            width: 40pt;
            text-align: center;
            vertical-align: top;
        }

        </style>
    </head>

    <netui:body>
    <div style="height:80px">
        <netui:image style="z-index:1" src="Beehive.gif" />
    <div class="title">Beehive NetUI Tests</div>
    <div class="subTitle">${pageFlow.title}</div>
    </div>
    <hr>
    <div id="navlist">
    <ul>
    <netui-data:repeater dataSource="pageFlow.categories">
        <li><netui:anchor styleClass="${container.item.selection}" action="selectList">${container.item.description}
            <netui:parameter name="target" value="${container.item.name}" />
            </netui:anchor></li>
     </netui-data:repeater>
    </ul>
    </div>
    <div id="content">
     <netui-data:repeater dataSource="pageFlow.tests">
         <netui-data:repeaterHeader><table border='1px' cellspacing='0'></netui-data:repeaterHeader>
         <netui-data:repeaterItem>
             <tr class="${pageFlow.rowStyle}">
             <td class="nameColumn"><netui:anchor href="/projectModelWeb/testRecorder?mode=displayLink">${container.item.name}
                 <netui:parameter name="file" value="${container.item.name}"/>
                 </netui:anchor></td>
             <td class="diffColumn">
                 <c:if test="${container.item.diff == true}">
                 <netui:anchor href="/projectModelWeb/testRecorder?mode=diff">Diff
                     <netui:parameter name="file" value="${container.item.name}"/>
                 </netui:anchor>
                 </c:if>
             &nbsp;
             </td>
              <td class="descrColumn">${container.item.description}
              </td>
              <td class="detailColumn"><netui:anchor href="/projectModelWeb/testRecorder?mode=details">Details
                 <netui:parameter name="file" value="${container.item.name}"/>
                 </netui:anchor></td>
            </tr>
         </netui-data:repeaterItem>
         <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
     </netui-data:repeater>
    </div>
    </netui:body>
</netui:html>

