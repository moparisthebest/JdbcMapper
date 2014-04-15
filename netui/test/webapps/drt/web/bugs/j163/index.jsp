<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
    <p style="color:green">This test verifies that an anchor with only a
    tagId will produce the proper id. Anchor with only the tagId set, no
    href or action, etc., produce an anchor that can be used as a location
    for navigating inside of the page.  Another anchor can set the
    linkName attribute to produce a fragment identifier which targets the
    first anchor.
    </p><p style="color:green">
    Because the tagId is scoped, the linkName should also be scoped.
    An anchor with only a tagId will produce a name attribute, but this is
    not added to the name table.  The reason for this, is that the name
    written for backward compatbility with old browsers.
    </p>
    <netui:scriptContainer idScope="one" >
       <netui:anchor tagId="top"/>
       <h4>Anchor Fragment Identifier Test</h4>
       <netui:anchor linkName="bottom">Bottom</netui:anchor>
       <div style="border: thin solid;height: 400px;">
       <p id="javaOut"></p>
       </div>
       <netui:anchor tagId="bottom"/>
       <netui:anchor linkName="top">Top</netui:anchor>
    </netui:scriptContainer>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var val = "<b>Document Access</b><br>";
    var anchorTag = document.getElementById(lookupIdByTagId("top",p));
    val = val + "Anchor Scope Id: <b>" + lookupIdScope(anchorTag,".") + "</b><br/>";
    val = val + "Anchor ID: <b>" + lookupIdByTagId("top",anchorTag) + "</b><br/>";
    p.innerHTML = val;
    </script>
</netui:html>
