<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <p style="color:green">The JavaScript lookupIdScope method required
	a separator character.  Now it should default to the empty string.
	You see this below, in the second test, the value "one".
	</p>
        <netui:scriptContainer idScope="one" >
        <netui:form tagId="form" action="back">
                Form JS Output Test -- JIRA169<br/>
                <netui:textBox dataSource="actionForm.text1" /><br/>
                <netui:button value="Submit"/><br/>
       <p id="javaOut"></p>
       </netui:form>

       </netui:scriptContainer>

        <netui:scriptBlock placement="after">

        var p = document.getElementById("javaOut");
        var val = "<b>Document Access</b><br/>";
        var formTag = document.getElementById(lookupIdByTagId("form", p));
        val = val + "Form Scope Id (Legacy Get): <b>" + getScopeId(formTag) + "</b><br/>";
        val = val + "Form Scope Id (non-Legacy Lookup): <b>" + lookupIdScope(formTag) + "</b><br/>";
        val = val + "Form Name: <b>" + getNetuiTagName("form", formTag) + "</b><br/>";
        val = val + "Form ID: <b>" + lookupIdByTagId("form", formTag) + "</b><br/>";
        val = val + "Form Name By Lookup: <b>" + lookupNameByTagId("form", formTag) + "</b><br/> <br/>";
        val = val + "<b>Lookup by lookup*(\"tagId\") signature: </b><br/>";
        val = val + "Form ID: <b>" + lookupIdByTagId("form") + "</b><br/>";
        val = val + "Form Name: <b>" + getNetuiTagName("form",p) + "</b><br/>";
        val = val + "Form Name By Lookup: <b>" + lookupNameByTagId("form",p) + "</b><br/>";

        p.innerHTML = val;
        </netui:scriptBlock>
    </netui:body>
</netui:html>
  