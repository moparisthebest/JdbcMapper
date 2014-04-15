<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<temp:template templatePage="../Template.jsp">
  <temp:setAttribute name="title" value="Nested Page Flow"/>
  <temp:section name="content">
    <span style="color:blue;">
    <h4>Nested Page Flow</h4>
    <netui:image src="../image/godzilla.gif" height="66" width="48"/><br />
    This is text in the nested page flow.<br/>
    <netui:anchor action="returnNow">Return</netui:anchor>
    </span>
  </temp:section>
</temp:template>
