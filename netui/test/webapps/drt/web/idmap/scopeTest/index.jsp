<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color: green">This verifies the getScopeId method works correctly.
	This is test is the result of a reported bug where the call to getScopeId 
	failed an reported a error on the JavaScript Console.
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
                    <td>Text2:</td>
                    <td>
                    <netui:select tagId="select" dataSource="actionForm.text2" optionsDataSource="${pageFlow.selectOptions}" />
                    </td>
                </tr>
                <tr valign="top">
                    <td>Text3:</td>
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
	Below are two calls, the first calls getScopeId directly.  This should return
	the empty string '' because there is no scope id.  The second verifies
	that we can find checkbox 1 by calling the lookup.
	</p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "Call getScopeId: <b>'" +
        getScopeId("check1",s) + "'</b><br>";
    val = val + "CheckBox 1 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("check1",s)) == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</netui:html>

  
