<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>CallMethod Null Return Test</title>
  </head>

  <body>
    <b>CallMethod Null Return Test</b><br/>
    <br/>
<b>Non-Null return value</b><br/>
<netui-data:callPageFlow method="getNonNullValue" resultId="jpfSays"/>
\${pageScope.jpfSays}: <netui:span value="${pageScope.jpfSays}"/>

<br/>
<br/>
<b>Null return value</b><br/>
<netui-data:callMethod object="${pageFlow}" method="getNullValue" resultId="jpfSays"/>
\${pageScope.jpfSays}: <netui:span value="${pageScope.jpfSays}"/>
<br/>
<br/>
    <b>Via code:</b> <%= pageContext.getAttribute("jpfSays") %>

    <hr>
  </body>
</html>
