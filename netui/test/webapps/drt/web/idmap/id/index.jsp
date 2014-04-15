<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This is the base test of the tagId features.  This
	is a basic form that will contain a bunch of tagIds.  These are then looked
	up in JavaScript and the results are reported below.  This has not ScriptContainer
	scoping.
        </p>
        <netui:form action="postForm">
        <span id="scopeOneSpan" />
            <table>
                <tr valign="top">
                    <td>Text1:</td>
                    <td>
                    <netui:textBox tagId="textbox" dataSource="actionForm.text1"></netui:textBox>
                    </td>
                </tr>
                <tr valign="top">
                    <td>Select:</td>
                    <td>
                    <netui:select tagId="select" dataSource="actionForm.text2" optionsDataSource="${pageFlow.selectOptions}" />
                    </td>
                </tr>
                <tr valign="top">
                    <td>TextArea:</td>
                    <td>
                    <netui:textArea tagId="textArea" dataSource="actionForm.text3"></netui:textArea>
                    </td>
                </tr>
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

    var val = "<b>Document Access</b><table><tr><td valign='top'><b>Lookup By Id</b><br>";
    val = val + "TextBox by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("textbox",s)) == null) + "</b><br>";
    val = val + "Select by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("select",s)) == null) + "</b><br>";
    val = val + "TextArea by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("textArea",s)) == null) + "</b><br>";
    val = val + "CheckBox 1 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("check1",s)) == null) + "</b><br>";
    val = val + "Button by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("button",s)) == null) + "</b><br>";

    val = val + "</td><td valign='top'><b>Lookup By Name</b><br>";
    val = val + "TextBox by Name is null: <b>" +
        (document.getElementsByName(lookupNameByTagId("textbox",s))[0] == null) + "</b><br>";
    val = val + "Select by Name is null: <b>" +
        (document.getElementsByName(lookupNameByTagId("select",s))[0] == null) + "</b><br>";
    val = val + "TextArea by Name is null: <b>" +
        (document.getElementsByName(lookupNameByTagId("textArea",s))[0] == null) + "</b><br>";
    val = val + "CheckBox 1 by Name is null: <b>" +
        (document.getElementsByName(lookupNameByTagId("check1",s))[0] == null) + "</b><br>";
    val = val + "</td></tr></table>";

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

  
