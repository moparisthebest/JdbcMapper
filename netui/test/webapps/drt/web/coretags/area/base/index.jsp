<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body tagId="body">
        <p style="color: green">
        </p>
	<netui:image src="redblue.gif" alt="pick a color" usemap="#map1"
		width="200" height="100"/>
        <map id="map1" name="map1">
             <netui:area action="red" shape="rect" coords="10, 10, 90, 90"/>
             <netui:area action="blue" shape="rect" coords="110, 10, 190, 90"/>
        </map>
        <hr>
	<netui:span value="Action: ${pageFlow.action}"/>
        </p>
    </netui:body>
</netui:html>
