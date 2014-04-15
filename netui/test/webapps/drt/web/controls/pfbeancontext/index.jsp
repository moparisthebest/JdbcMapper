<%@ page language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%--
--%>
<html>
  <head><title>PageFlowBeanContext Test</title></head>
  <body>
  Page Flow and Control's Context Page Flow match: ${pageFlow.match}
  <br/>
  <br/>
  <a href="${pageContext.request.contextPath}">Home</a>
  </body>
</html>