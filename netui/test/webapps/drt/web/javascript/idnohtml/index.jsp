<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<html>
    <head>
        <netui:base/>
    </head>
    <body>
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
        <hr>
        Post Value: <netui:label value="${pageFlow.text}"/><br>
        Method: <netui:label value="${pageFlow.method}"/>
        <hr>
        <p id="javaOut"></p>
    </body>
    <script language="JavaScript" type="text/JavaScript">
    var p = document.getElementById("javaOut");
    var val = "<b>Document Access</b><br>";
    val = val + "Form By Id is null: <b>" + (document.form == null) + "</b><br>";
    val = val + "Form By Id is null: <b>" + (document["form"] == null) + "</b><br>";
    val = val + "<br><b>NetUI Access Methods</b><br>";

    val = val + "TextBox 1 by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("t1")) == null) + "</b><br>";
    val = val + "TextBox 1 by name is null: <b>" +
        (document["form"][lookupNameByTagId("t1")] == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</html>

  
