<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html idScope="html">
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color:green">Test of the ScriptContainer tag with a scopeId set on
	it.
        </p>
        <netui:scriptContainer idScope="scopeOne">
        <span id="scopeOneSpan" />
        <netui:form tagId="form" action="post" focus="b1">
            <table>
                <tr valign="top">
                    <td>Text:</td>
                    <td>
                    <netui:textBox tagId="t1" dataSource="actionForm.text1" /><br>
                    <netui:textBox tagId="t2" dataSource="actionForm.text2" />
                    </td>
                </tr>
            </table>
            <br>
            <netui:button tagId="b1" value="Submit" />&nbsp;
            <netui:button tagId="b2" value="Override" action="override"/>&nbsp;

        </netui:form>
        </netui:scriptContainer>
        <hr>
        <netui:scriptContainer idScope="Results">
        Post Value: <netui:span tagId="postValue" value="${pageFlow.text}"/><br>
        Method: <netui:span tagId="method" value="${pageFlow.method}"/>
	</netui:scriptContainer>
        <hr>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "JavaScript lookup real name is null: <b>" +
	(document["bean"] == null) + "</b><br>";
    val = val + "Form by name is null: <b>" +
        (document[lookupNameByTagId("form",s)] == null) + "</b><br>";


    val = val + "<br><b>ScopeId value</b><br>";
    val = val + "ScopeId: <b>" + getScopeId(s) + "</b><br>";

    val = val + "<br><b>NetUI Access Methods</b><br>";
    val = val + "TextBox 1 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("t1",s)) == null) + "</b><br>";
    val = val + "TextBox 1 by name is null: <b>" +
        (document["bean"][lookupNameByTagId("t1",s)] == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</netui:html>

  
