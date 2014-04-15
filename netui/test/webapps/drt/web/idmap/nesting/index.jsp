<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This test will generate multiple nested ScriptContainers
	which requires fully qualifying the lookup to find the proper span or textbox.  There
	are two spans and textboxes with the same tagId inside a ScriptContainer with the same
	scopeId.  These are then found in a outer ScriptContainer that has 
	generateScopeId = 'true' so that we create a unique id.  This will not
	work in the legacy lookups.
        </p>
	<netui:scriptContainer generateIdScope="true">
           <netui:scriptContainer idScope="scope">
               <span id="scopeOneSpan" />
                  <netui:textBox tagId="textBox" dataSource="pageFlow.textOne"/>
	          <netui:span tagId="span" value="span[1] inside scopeOneSpan">
	             <netui:attribute name="spanId" value="[1]"/>
	          </netui:span>
               </span>
           </netui:scriptContainer>
	</netui:scriptContainer>
	<netui:scriptContainer generateIdScope="true">
           <netui:scriptContainer idScope="scope">
              <span id="scopeTwoSpan" />
                  <netui:textBox tagId="textBox" dataSource="pageFlow.textTwo"/>
	         <netui:span tagId="span" value="span[2] inside scopeTwoSpan">
	            <netui:attribute name="spanId" value="[1]"/>
	         </netui:span>
              </span>
           </netui:scriptContainer>
	</netui:scriptContainer>
        <hr>
        <p style="color: green">Below we search for each span by the tagId name.  We
	should find the individual spans.  Below the search results there is a dump
	of the lookup tables.
        </p>
        <p id="javaOut"></p>
    </netui:body>
<script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s1 = document.getElementById("scopeOneSpan");
    var s2 = document.getElementById("scopeTwoSpan");

    var val = "<b>Document Access</b><br>";

    val = val + "Span[1] by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("span",s1)) == null) + "</b><br>";
    val = val + "Span[2] by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("span",s2)) == null) + "</b><br>";
    val = val + "TextBox[1] by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("textBox",s1)) == null) + "</b><br>";
    val = val + "TextBox[2] by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("textBox",s2)) == null) + "</b><br>";
    var tmp = document.getElementsByName(lookupNameByTagId("textBox",s1));
    val = val + "<br>TextBox[1] by name is == 0: <b>" +
        (tmp.length == 0) +
	"</b><br>";
    if (tmp.length > 0) {
       val = val + "TextBox[1] id: <b>" + tmp[0].id + "</b><br>";
    }
    else {
       val = val + "TextBox[1] <b>not found</b><br>";
    }

    tmp = document.getElementsByName(lookupNameByTagId("textBox",s2));
    val = val + "<br>TextBox[2] by name is == 0: <b>" +
        (tmp.length == 0) +
	"</b><br>";
    if (tmp.length > 0) {
       val = val + "TextBox[2] id: <b>" + tmp[0].id + "</b><br>";
    }
    else {
       val = val + "TextBox[2] <b>not found</b><br>";
    }

    val = val + "<br>";
    if (typeof(netui_tagIdNameMap) != "undefined") {
       val = val + "<b>tagIdNameMap:</b><br>";
       for (var x in netui_tagIdNameMap) {
           val = val + "Name '" + x + "' value '" + netui_tagIdNameMap[x] + "'<br>";
       }
       val = val + "<br>";
    }
    else {
       val = val + "tagIdNameMap is <b>undefined</b><br><br>";
    }

    if (typeof(netui_names) != "undefined") {
       val = val + "<b>netui_names (Legacy):</b><br>";
       for (var x in netui_names) {
           val = val + "Name '" + x + "' value '" + netui_names[x] + "'<br>";
       }
       val = val + "<br>";
    }
    else {
       val = val + "netui_name (Legacy) is <b>undefined</b><br>";
    }

    p.innerHTML = val;

</script>
</netui:html>

  
