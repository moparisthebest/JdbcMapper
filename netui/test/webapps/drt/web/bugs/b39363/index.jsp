<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>List Update Test</title>
  </head>

  <body>
    <h1>List Update Test</h1>

<netui:form action="postback">

<b>List Data</b><br/>
<netui-data:repeater dataSource="actionForm.list">
    <netui-data:repeaterHeader><table><tr><td>List</td></tr></netui-data:repeaterHeader>
    <netui-data:repeaterItem>
      <tr><td>
      <netui:textBox dataSource="container.item"/>
      </td></tr>
    </netui-data:repeaterItem>
    <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
</netui-data:repeater>
  <br/>
  <br/>
  <netui:button>Submit</netui:button>
</netui:form>
<br/>
<br/>
<br>
  </body>
</html>
