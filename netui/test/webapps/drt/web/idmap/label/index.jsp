<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This tests the use of tagid on the Label.
	This is a JavaScript test of the mapping between tagid and the
	real id.
        </p>
        <netui:form action="postForm">
        <span id="scopeOneSpan" />
            <table>
                <tr valign="top">
                    <td><netui:label tagId="cb1Label" for="checkbox1" value="Check One:" /></td>
                    <td>
                    <netui:checkBox tagId="checkbox1" dataSource="actionForm.check1" />
                    </td>
                </tr>
                <tr valign="top">
                    <td><netui:label tagId="cb2Label" for="checkbox1" value="Check Two:" /></td>
                    <td>
                    <netui:checkBox tagId="checkbox2" dataSource="actionForm.check2" />
                    </td>
                </tr>
            </table>
            <br>
            &nbsp;
            <netui:button type="submit" value="postForm"/>
        </netui:form>
        <hr>
        <p style="color: green">This section will use the JavaScript routines
	to lookup both the checkboxes and the labels associated with them.
        </p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");


    var val = "<b>Document Access</b><br>";

    val = val + "CheckBox 1 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("checkbox1",s)) == null) + "</b><br>";
    val = val + "CheckBox 1 Label by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("cb1Label",s)) == null) + "</b><br>";
    val = val + "CheckBox 2 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("checkbox2",s)) == null) + "</b><br>";
    val = val + "CheckBox 2 Label by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("cb2Label",s)) == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</netui:html>

  
