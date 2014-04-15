<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body tagId="body">
        <p style="color: green">This test verifies that a Area can be used to
	submit a form.  The image and the map containing the area tags must be
	within the form.  Below, by pushing on the red square you will submit
	the form to the "post" action.
        </p>
        <netui:form action="post">
           Text: <netui:textBox dataSource="actionForm.text1" /><br><br>
	   <netui:image src="redblue.gif" alt="pick a color" usemap="#map1"
		width="200" height="100"/>
           <map id="map1" name="map1">
             <netui:area shape="rect" coords="10, 10, 90, 90"/>
             <netui:area shape="rect" coords="110, 10, 190, 90"/>
           </map>
        </netui:form>
        <hr>
	<netui:span value="Action: ${pageFlow.action}"/><br>
	<netui:span value="Text: ${pageFlow.text}"/><br>
        </p>
    </netui:body>
</netui:html>
