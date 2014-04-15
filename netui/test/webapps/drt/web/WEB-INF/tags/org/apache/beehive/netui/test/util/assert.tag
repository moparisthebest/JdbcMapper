<%@ tag body-content="empty" %>
<%@ attribute name="test" required="true" type="java.lang.Boolean"%>
<%@ attribute name="failureMessage" required="true" type="java.lang.String"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
Boolean attr = (Boolean)jspContext.getAttribute("test");
String msg = (String)jspContext.getAttribute("failureMessage");
if(!attr.booleanValue())
  throw new JspException("JSP Tagfile Assert failed!  Message: " + msg);
%>

