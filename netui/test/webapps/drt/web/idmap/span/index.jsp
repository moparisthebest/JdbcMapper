<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This tests that setting a tagId on span.  We
	verify that the JavaScript and lookup tables are written out correctly.
        </p>
        <span id="scopeOneSpan" />
        <netui:span tagId="span" value="This is a netui span with a tagid" />
        <hr>
        <p style="color: green">This section does two things, it will search
        for the span using the tagId and also dumps the lookup tables.  In this
	case the IdNameMap should be underfined because the span does not have a
	name;
        </p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "Span by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("span",s)) == null) + "</b><br>";


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

    if (typeof(netui_tagIdNameMap) != "undefined") {
       val = val + "<b>tagIdNameMap:</b><br>";
       for (var x in netui_tagIdNameMap) {
           val = val + "Name '" + x + "' value '" + netui_tagIdNameMap[x] + "'<br>";
       }
       val = val + "<br>";
    }
    else {
       val = val + "tagIdNameMap is <b>undefined</b><br>";
    }

    p.innerHTML = val;
    </script>
</netui:html>

  
