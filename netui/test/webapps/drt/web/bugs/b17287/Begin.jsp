<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Cell Repeater</title>
</head>
<body>
<b>Error in Cell Repeater - Invalid rows value</b>
<%
    try
    {
%>
<db:cellRepeater dataSource="pageFlow.data" rows="${pageFlow.rows}">
  Item: <netui:span value="${container.item}" />
</db:cellRepeater>
<%
    }
    catch(Exception e)
    {
        if(e instanceof javax.servlet.jsp.el.ELException)
            out.write("success -- caught ELException; assumed to be related to invalid String to int type conversion");
        else out.write("fail -- exception was not servlet exception");      
    }
%>
</body>
</html>
