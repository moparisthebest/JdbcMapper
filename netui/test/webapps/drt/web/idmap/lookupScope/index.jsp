<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">
        </p>
	<netui:scriptContainer idScope="scope1">
           <netui:scriptContainer idScope="scope2">
               <span id="scopeOneSpan" />
               <netui:span tagId="span" value="span[1] inside scopeOneSpan" />
           </netui:scriptContainer>
	</netui:scriptContainer>
        <hr>
        <p style="color: green">
        </p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s1 = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "<b>ScopeId</b>: " + lookupIdScope(s1,".") + "<br>";

    val = val + "Span by name is null: <b>" +
        (document.getElementById(lookupIdByTagId("span",s1)) == null) + "</b><br>";

    if (typeof(netui_names) != "undefined") {
       val = val + "<b>netui_names (Legacy):</b><br>";
       for (var x in netui_names) {
           val = val + "Name '" + x + "' value '" + netui_names[x] + "'";
       }
       val = val + "<br>";
    }
    else {
       val = val + "netui_name (Legacy) is <b>undefined</b><br>";
    }

    p.innerHTML = val;

    </script>
</netui:html>

  
