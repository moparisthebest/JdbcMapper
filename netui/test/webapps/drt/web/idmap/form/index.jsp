<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This test sets the tagId on a form within
	a scoped html tag.  It then verifies that the JavaScript is written
	out correctly and that these methods work to find the form.
        </p>
        <netui:form tagId="form" action="postForm">
        <span id="scopeOneSpan" />
            <table>
                <tr valign="top">
                    <td>Check1:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.check1" />
                    </td>
                </tr>
                <tr valign="top">
                    <td>Check2:</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.check2" />
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postForm"/>
        </netui:form>
        <hr>
        <p style="color: green">
	These sections that follow are generated through JavaScript.  Both
	of the forms elemenets should be found so the value should be
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
    val = val + "Form by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("form",s)) == null) + "</b><br>";
    val = val + "Form by name is null: <b>" +
        (document[lookupNameByTagId("form",s)] == null) + "</b><br>";

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
           val = val + "Name '" + x + "' value '" + netui_tagIdNameMap[x] + "'";
       }
       val = val + "<br>";
    }
    else {
       val = val + "tagIdNameMap is <b>undefined</b><br>";
    }

    p.innerHTML = val;
    </script>
</netui:html>

  
