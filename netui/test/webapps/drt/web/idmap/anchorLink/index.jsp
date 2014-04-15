<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <netui:anchor tagId="top"/>
    <p style="color:green">This test verifies the behavior of anchors
    acting as fragment identifiers.  Anchor which only set the tagId
    act location that may be the target of another anchor.  When the
    linkName attribute is set, this acts to target a named anchor by
    creating a fragment identifier.  In this case the link name may or
    may not be qualified into a script container.  If the link name does
    not start with the '#', it will be qualified the same way a tagId
    is qualified.  If link name begins with '#' it is output as is.
    </p>
    <p style="color:green">In this test there are two anchors specifying
    'top' as their tagId value.  One is above this text and the other
    is inside a scoped ScriptContainer and appears below.  At the bottom
    of the page, two link go to the real top and the test top.
    </p>
    <div style="border: thin solid;height: 100px;"></div>
    <netui:scriptContainer idScope="one" >
       <netui:anchor tagId="top"/>
       <h4>Fragment Identifiers and Scoped Ids</h4>
       <netui:anchor linkName="bottom">Bottom</netui:anchor>
       <div style="border: thin solid;height: 400px;">
       <p id="javaOut"></p>
       </div>
       <netui:anchor tagId="bottom"/>
       <netui:anchor linkName="top">Test Top</netui:anchor><br>
       <netui:anchor linkName="#top">Real Top</netui:anchor>
    </netui:scriptContainer>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var val = "<b>Document Access</b><br>";
    var anchorTag = document.getElementById(lookupIdByTagId("top",p));
    val = val + "Test Top ScopeId: <b>" + lookupIdScope(anchorTag,".") + "</b><br/>";
    val = val + "Test Top ID: <b>" + lookupIdByTagId("top",anchorTag) + "</b><br/>";
    anchorTag = document.getElementById(lookupIdByTagId("top"));
    val = val + "Real Top Scope Id: <b>" + lookupIdScope(anchorTag,".") + "</b><br/>";
    val = val + "Real Top ID: <b>" + lookupIdByTagId("top") + "</b><br/>";
    p.innerHTML = val;
    </script>
</netui:html>
