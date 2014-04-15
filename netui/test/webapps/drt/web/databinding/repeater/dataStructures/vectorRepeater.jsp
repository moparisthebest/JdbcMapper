<%@ page language="java"%>
<%@ page import="
  databinding.SimpleJavaBean,
  java.util.Vector" 
%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-databinding"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>

<%
    if(request.getAttribute("multiItemDatasource") == null)
    {
        List multiItemDataSource = new Vector();  

        for(int i = 0; i < 10; i++)
        {
            multiItemDataSource.add(new SimpleJavaBean(i));

            request.setAttribute("multiItemDataSource", multiItemDataSource);
        }
    }

    if(request.getAttribute("singleItemDataSource") == null)
    {
        List singleItemDataSource = new Vector();
        singleItemDataSource.add(new SimpleJavaBean(0));
        request.setAttribute("singleItemDataSource", singleItemDataSource);
    }

    if(request.getAttribute("zeroItemDataSource") == null)
        request.setAttribute("zeroItemDataSource", new Vector());
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <title>List Repeater Tests</title>
</head>
<body>

<%@ include file="repeaterBody.jsp" %>

<address><a href="mailto:ekoneil@bea.com">Report Problems</a></address>
<!-- Created: Wed Aug 07 10:07:00 Mountain Daylight Time 2002 -->
<!-- hhmts start -->
Last modified: Sun Jun 27 21:02:55 Mountain Standard Time 2004
<!-- hhmts end -->
  </body>
</html>
