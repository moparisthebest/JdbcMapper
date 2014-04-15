<%@ page language="java"%>
<%@ page import="
  databinding.SimpleJavaBean" 
%>

<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-databinding"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>

<%
    if(request.getAttribute("multiItemDataSource") == null)
    {
        SimpleJavaBean[] multiItemDataSource = new SimpleJavaBean[10];

        for(int i = 0; i < multiItemDataSource.length; i++)
        {
            multiItemDataSource[i] = new SimpleJavaBean(i);

            request.setAttribute("multiItemDataSource", multiItemDataSource);
        }
    }

    if(request.getAttribute("singleItemDataSource") == null)
    {
        SimpleJavaBean[] singleItemDataSource = new SimpleJavaBean[1];
        singleItemDataSource[0] = new SimpleJavaBean(0);
        request.setAttribute("singleItemDataSource", singleItemDataSource);
    }

    if(request.getAttribute("zeroItemDataSource") == null)
        request.setAttribute("zeroItemDataSource", (new SimpleJavaBean[0]));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <title>Array Repeater Tests</title>
</head>
<body>

<%@ include file="repeaterBody.jsp" %>

<address><a href="mailto:ekoneil@bea.com">Report Problems</a></address>
<!-- Created: Wed Aug 07 10:07:00 Mountain Daylight Time 2002 -->
<!-- hhmts start -->
Last modified: Sun Jun 27 21:02:24 Mountain Standard Time 2004
<!-- hhmts end -->
  </body>
</html>
