<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <netui:span value="${pageFlow.action}" />
        <netui:scriptContainer idScope="one" >
        <netui:form action="post">
              Form Submit Anchor JS Output Test
              <br/>
              <netui:textBox dataSource="actionForm.text1" /> <br/>
              <netui:textBox dataSource="actionForm.text2" /> <br/>
              <netui:anchor tagId="a1" formSubmit="true">Submit</netui:anchor><br/>
              <br/>
         </netui:form>
        <p id="javaOut"></p>
        </netui:scriptContainer>
        
    </netui:body>

    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var val = "<b>Document Access</b><br>";
    var anchorTag = document.getElementById(lookupIdByTagId("a1",p));
    val = val + "Anchor Scope Id: <b>" + getScopeId(anchorTag) + "</b><br/>";
    val = val + "TextBox Name: <b>" + getNetuiTagName("a1", anchorTag) + "</b><br/>";
    val = val + "TextBox ID: <b>" + lookupIdByTagId("a1",anchorTag) + "</b><br/>";

    p.innerHTML = val;
    </script>
    
</netui:html>
