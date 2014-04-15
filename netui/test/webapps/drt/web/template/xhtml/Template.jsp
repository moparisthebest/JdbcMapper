<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html documentType="xhtml1-transitional">
   <head>
      <title><temp:attribute name="title"/></title>
      <netui:base />
   </head>
   <netui:body>
      <h1><temp:attribute name="title"/></h1>
      <p><netui:form action="search" style="display:inline">
	<netui:textBox dataSource="pageFlow.search"/>
	<netui:button value="Search"/>
      </netui:form></p>
      <table width="100%">
         <tr><td valign="top">
	 <div style="height="200px">
	 <h4>'left' Section</h4>
         <temp:includeSection name="left"/>
	 </div>
	 </td><td valign="top">
	 <div style="height="200px">
	 <h4>'right Section</h4>
         <temp:includeSection name="right"/>
	 </div>
         </td>
      </tr>
      </table>
      <hr>
      Selected Value: <netui:span value="${pageFlow.selectedValue}"/>
   </netui:body>
</netui:html>
