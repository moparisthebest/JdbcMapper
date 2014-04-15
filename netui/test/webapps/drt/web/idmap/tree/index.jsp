<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
	<h4>Page One</h4>
        <p style="color: green">This test sets the TagId on both a tree and an
	item within it then accesses those nodes through the JavaScript lookup
	methods.
        </p>
	<netui:anchor action="clearTree">Clear Tree</netui:anchor>&nbsp;
	<hr>
        <span id="scopeOneSpan" />
        <netui:tree dataSource="pageFlow.tree" selectionAction="select" tagId="tree"
		renderTagIdLookup="true">
           <netui:treeItem expanded="true" tagId="item">Item-PageOne</netui:treeItem>
        </netui:tree>
	<hr>
        <p style="color: green">Below we access the tree and item using the
	lookup method.
        </p>
        <p id="javaOut"></p>
    </netui:body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "Tree by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("tree",s)) == null) + "</b><br>";
    val = val + "Item by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("item",s)) == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</netui:html>

  
