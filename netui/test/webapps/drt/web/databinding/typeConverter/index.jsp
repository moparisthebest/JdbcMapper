<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Person Type Converter Test</title>
  </head>

  <body>
    <h1>Person Type Converter Test</h1>
<netui-html:form action="postback">
<table>
<tr><td><b>Current Person</b>:</td><td><netui-html:span value="${actionForm.person}"/></td></tr>
<tr><td>New Person</td><td><netui-html:textBox dataSource="actionForm.person"/></td></tr>
<tr><td colspan=2><netui-html:button>Submit</netui-html:button></td></tr>
</table>
</netui-html:form>

    <hr>
    <address><a href="mailto:ekoneil@bea.com"></a></address>
<!-- Created: Sat Feb 08 21:17:16 Mountain Standard Time 2003 -->
<!-- hhmts start -->
Last modified: Sat Feb 08 21:19:40 Mountain Standard Time 2003
<!-- hhmts end -->
  </body>
</html>
