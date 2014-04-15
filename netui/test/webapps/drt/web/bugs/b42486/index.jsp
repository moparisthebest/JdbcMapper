<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Radar 42486 Repro</title>
  </head>
  <body>
    <b>Radar 42486 Repro</b>

<netui-data:repeater dataSource="pageFlow.foo"> 
    <netui-data:repeaterHeader><ol></netui-data:repeaterHeader>
    <netui-data:repeaterItem> 
        <li><netui:span value="${container.item.key}"/> & <netui:span value="${container.item.value}"/></li>
    </netui-data:repeaterItem> 
    <netui-data:repeaterFooter></ol></netui-data:repeaterFooter>
</netui-data:repeater> 

  </body>
</html>
