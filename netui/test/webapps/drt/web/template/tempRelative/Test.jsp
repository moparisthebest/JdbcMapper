<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<temp:template templatePage="Template.jsp">
  <temp:setAttribute name="title" value="Main Page Flow"/>
  <temp:section name="content">
    <span style="color:red;">
    <h4>Main Page Flow</h4>
    <netui:image src="image/godzilla.gif" height="66" width="48"/><br />
    This is text in the main page flow.
    </span>
  </temp:section>
  <temp:section name="actions">
  <netui:anchor action="sub">Nest Sub Flow</netui:anchor>
  </temp:section>  
</temp:template>
