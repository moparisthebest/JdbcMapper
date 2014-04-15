<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <netui:scriptContainer idScope="one">
        <netui:form action="post">
             CheckBoxOption JS Output Test<br/>
             <netui:checkBoxGroup dataSource="actionForm.checks">
                 <netui:checkBoxOption tagId="cbo1" value="Text1" /><br>
                 <netui:checkBoxOption tagId="cbo2" value="Text2" /><br>
             </netui:checkBoxGroup>
	     <netui:radioButtonGroup dataSource="actionForm.radios">
	        <netui:radioButtonOption tagId="rbo1" value="Radio 1" /><br>
	        <netui:radioButtonOption tagId="rbo2" value="Radio 2" /><br>
	     </netui:radioButtonGroup>
             <netui:button value="Submit"/><br>
        </netui:form>
        </netui:scriptContainer>
        <hr>
        <p id="javaOut"></p>
    </netui:body>

    <script language="JavaScript" type="text/JavaScript">
    var p = document.getElementById("javaOut");
    var val = "<b>Document Access</b><br/>";
    var cboTag = document.getElementById(lookupIdByTagId("cbo1",document.forms[0]));
    val = val + "Scope Id: <b>" + lookupIdScope(cboTag,".") + "</b><br/>";
    val = val + "CheckBoxOption ID: <b>" + lookupIdByTagId("cbo1",cboTag) + "</b><br/>";
    val = val + "CheckBoxOption Name By Lookup: <b>" + lookupNameByTagId("cbo1",cboTag) + "</b><br/>";
    val = val + "RadioButtonOption ID: <b>" + lookupIdByTagId("rbo1",cboTag) + "</b><br/>";
    val = val + "RadioButtonOption Name By Lookup: <b>" + lookupNameByTagId("rbo1",cboTag) + "</b><br/>";

    p.innerHTML = val;
    </script>
</netui:html>
