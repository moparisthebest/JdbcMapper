<%--
  Created by IntelliJ IDEA.
  User: ekoneil
  Date: May 4, 2006
  Time: 11:07:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<html>
  <head>
      <title>NetUI JSP</title>
  </head>
  <body>
  <netui:form action="submitJspInclude">
      <jsp:include page="form.jsp"/>
      <netui:button>Submit</netui:button>
  </netui:form>
  </body>
</html>