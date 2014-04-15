<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Struts Interop i18n Tests</title>
  </head>
  <body>
    <b>Struts Interop i18n Tests</b>
    <p>
    default bundle: <netui:span value="${bundle.default.myStrutsMessage}"/><br/>
    double quotes: <netui:span value="${bundle[\"namedStrutsMessages/databinding/bundle/struts\"].myStrutsMessage}"/><br/>
    single quotes: <netui:span value="${bundle['namedStrutsMessages/databinding/bundle/struts'].myStrutsMessage}"/><br/>
    <br/>
    <br/>
    </p>
  </body>
</html>
