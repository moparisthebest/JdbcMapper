<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<html>
  <head>
      <title>NetUI JSP</title>
  </head>
  <body>
  <netui:form action="submitTagFile">
      <tags:forminclude/>
      <netui:button>Submit</netui:button>
  </netui:form>
  </body>
</html>