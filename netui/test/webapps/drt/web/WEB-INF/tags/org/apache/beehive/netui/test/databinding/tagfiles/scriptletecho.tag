<%@ tag body-content="empty" %>
<%@ attribute name="text" required="true" type="java.lang.String"%>
<%
  out.write((String)jspContext.getAttribute("text"));
%>

