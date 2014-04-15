<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>


<netui:html>
    <head>
        <netui:base/>
        <title>Return-To Current JSP</title>
    </head>
    <netui:body>
        <h3 align="center">Return-To Current JSP</h3>
        <p>Allows a hit to a .JSP page directly, without going through the 
        controller first, then hit an action that has a return-to:current 
        page.</p>
        <hr width="95%"/>
        <br/>
        <h3><font color="blue">
        Instructions:
        </font></h3>
        <p>
        Start the test by going directly to this JSP first without
        going to the page flow.
        <br/><br/>
        First, to test a &quot;return-to&quot; this page, select Continue:
        <netui:anchor action="returnToPage">Continue...</netui:anchor>
        <br/><br/>Then, to test a &quot;Forward&quot; to this page, select Again:
        <netui:anchor action="forwardToSamePage">Again...</netui:anchor><br>
    </netui:body>
</netui:html>

  
