<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <p style="color:green">This test will verify that an anchor
	with just a linkName and tagId attribute will output the
	lookup JavaScript.
    </p>
    <netui:scriptContainer idScope="one" >
       <a id="one.top"></a>
       <h4>Anchor Fragment Identifier Test</h4>
       <netui:anchor tagId="linkBottom" linkName="bottom">Bottom</netui:anchor>
       <div style="border: thin solid;height: 400px;">
       <p id="javaOut"></p>
       </div>
       <a id="one.bottom"></a>
       <netui:anchor tagId="linkTop" linkName="top">Top</netui:anchor>
    </netui:scriptContainer>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var val = "<b>Document Access</b><br>";
    var anchorTag = document.getElementById(lookupIdByTagId("linkBottom",p));
    val = val + "LinkBottom Scope Id: <b>" + lookupIdScope(anchorTag,".") + "</b><br/>";
    val = val + "LinkBottom ID: <b>" + lookupIdByTagId("linkBottom",anchorTag) + "</b><br/>";
    anchorTag = document.getElementById(lookupIdByTagId("linkTop",p));
    val = val + "LinkTop Scope Id: <b>" + lookupIdScope(anchorTag,".") + "</b><br/>";
    val = val + "LinkTop ID: <b>" + lookupIdByTagId("linkTop",anchorTag) + "</b><br/>";

    p.innerHTML = val;
    </script>
</netui:html>
