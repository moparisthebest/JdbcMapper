<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Simple JSP -&gt; JPF Test</title>
  </head>

  <body>
    <b>Call Page Flow Tests</b><br/>
<netui-data:callPageFlow resultId="jpfText" method="getDefaultText"/>
PageFlow defaultText = <netui:span value="${pageScope.jpfText}"/><br/>
<br/>

<netui-data:callPageFlow resultId="jpfSaid" method="echo">
    <netui-data:methodParameter value="Hello JSP Page"/>
</netui-data:callPageFlow>

PageFlow said: <netui:span value="${pageScope.jpfSaid}"/><br/>
<br/>
<br/>
<netui:anchor action="begin">Home</netui:anchor>
  </body>
</html>