<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body tagId="body">
    <span id="scopeSpan" />
        <p style="color: green">This test sets the tagId on all of the
	non-HTML Controls.  This we lookup each and verify that we find
	them based upon the tagId.
        </p>
	<netui:anchor tagId="anchor" action="begin">Begin</netui:anchor><br>
	<netui:area tagId="area" action="begin"/><br>
        <netui:span tagId="span" value="span" /><br>
	<netui:image tagId="image" src="alien.gif" /><br>
	<netui:imageButton tagId="imageButton" src="alien.gif" /><br>
	<netui:imageAnchor tagId="imageAnchor" action="begin" src="alien.gif" /><br>
        <hr>
        <p style="color: green">Below we sure for each of the non-HTML Controls
	using the tagId.  All should be false.  <b>Note:</b> under FireFox
	the area tag will be reported as true.  This does work under IE.  After
	the lookups we will dump the IdMap.
        </p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeSpan");

    var val = "<b>Document Access</b><br>";

    val = val + "Body by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("body",s)) == null) + "</b><br>";
    val = val + "Anchor by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("anchor",s)) == null) + "</b><br>";
    val = val + "Area by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("area",s)) == null) + "</b><br>";
    val = val + "Span by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("span",s)) == null) + "</b><br>";
    val = val + "Image by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("image",s)) == null) + "</b><br>";
    val = val + "ImageButton by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("imageButton",s)) == null) + "</b><br>";
    val = val + "ImageAnchor by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("imageAnchor",s)) == null) + "</b><br>";

    val = val + "<br>";
    if (typeof(netui_tagIdMap) != "undefined") {
       val = val + "<b>tagIdMap:</b><br>";
       for (var x in netui_tagIdMap) {
           val = val + "Name '" + x + "' value '" + netui_tagIdMap[x] + "'<br>";
       }
       val = val + "<br>";
    }
    else {
       val = val + "tagIdMap is <b>undefined</b><br>";
    }

    p.innerHTML = val;

    </script>
</netui:html>

  
