<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This will test the autogeneration of scope
	id's on both the HTML and ScriptContainer tags.  Below, JavaScript will
	perform a search using the built in methods to find a checkbox.  The
	result should never be null.
        </p>
        <netui:scriptContainer generateIdScope="true">
        <netui:form action="postForm">
        <span id="scopeOneSpan" />
            <table>
                <tr valign="top">
                    <td>Check1:</td>
                    <td>
                    <netui:checkBox tagId="check1" dataSource="actionForm.check1"></netui:checkBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Check2:</td>
                    <td>
                    <netui:checkBox tagId="check2" dataSource="actionForm.check2"></netui:checkBox>
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button tagId="button" type="submit" value="postForm"/>
        </netui:form>
        </netui:scriptContainer>
        <hr>
        <p style="color: green">
	These sections that follow are generated through JavaScript.  All of the
	elemenets should be found so the value should be
	<b>false</b>.  The following section contains a dump of the constructed
	IdMap and IdName tables.  These results should run on all browsers and
	are not verified by the TestRecorder.
        </p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "CheckBox 1 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("check1",s)) == null) + "</b><br>";
    val = val + "CheckBox 1 by name is null: <b>" +
        (document.getElementsByTagName(lookupNameByTagId("check1",s)) == null) + "</b><br>";

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

  
