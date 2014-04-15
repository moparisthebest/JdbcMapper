<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>callMethod Tag Test</title>
  </head>
  <body>
    <b>callMethod Tag Test</b>
<netui-data:callMethod object="${pageFlow}" method="saySomething" resultId="whatWasSaid"/>
<netui:span value="${pageScope.whatWasSaid}"/>
<br/>
<br/>
  </body>
</html>
