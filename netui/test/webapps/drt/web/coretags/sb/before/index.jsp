<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color:green">This test will verify that script blocks with
	before will be placed before the framework generated javascript.
        </p>
	<netui:label tagId="forceJavaScript" value="Force Framework JavaScript"/>
        <hr>
<netui:scriptBlock placement="before">
    // This JavaScript should appear before the framework generated
    // JavaScript.
    var p = document.getElementById("javaOut");

    var val = "<b>This is generated from the first code block</b><br>";
    p.innerHTML = val;
</netui:scriptBlock>

        <p id="javaOut"></p>
        <p id="javaOutTwo"></p>
        <hr>
<netui:scriptBlock placement="before">
    // This JavaScript should appear before the framework generated
    // JavaScript.
    var p = document.getElementById("javaOutTwo");
    var val = "<b>This is generated from the second code block</b><br>";
    p.innerHTML = val;
</netui:scriptBlock>

    </netui:body>
</netui:html>

  
