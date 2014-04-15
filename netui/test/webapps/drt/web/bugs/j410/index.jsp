<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
   <head>
      <title>
	scriptHeader tag
      </title>
   </head>
<netui:body>
        <p style="color:green">This test will test various configurations
	of the ScriptBlock tag.  It verifies that we create valid
	script blocks.  you should see two alerts and no errors on
	the Java Console.</p>
<netui:scriptBlock>alert('alert one');</netui:scriptBlock>
<netui:scriptBlock></netui:scriptBlock>
<netui:scriptBlock>
alert('alert two');
</netui:scriptBlock>
</netui:body>
</netui:html>
