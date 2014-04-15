<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>i18n Override Test</title>
  </head>
  <body>
    <b>i18n Override Test</b>
<br/>
<netui:span value="${bundle.default.message1}"/>
<br/>
<netui:anchor action="resetLocale">Reset Locale</netui:anchor>
<br/>
  </body>
</html>
