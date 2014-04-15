<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<temp:template templatePage="Template.jsp">
  <temp:setAttribute name="title" value="Template Test"/>
  <temp:section name="content">
    <h4>Template Test</h4>
    Test of the templates includes a side bar with actions from the Page Flow.
    In addition, all Page Flows contain a "home" action that is called from
    the template itself.
  </temp:section>
  <temp:section name="actions">
  <netui:anchor action="main">Main</netui:anchor><br />
  <netui:anchor action="sub">Nested Page Flow</netui:anchor>
  </temp:section>  
</temp:template>
