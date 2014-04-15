<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<html>
    <head>
        <netui:base/>
    </head>
    <body>
        <p style="color: green">This will test the result of generating JavaScript
	from when it is on the tree itself.  The result should be the inline method
	for looking up the tagId set on the tree.
        </p>
	<netui:anchor action="clearTree">Clear Tree</netui:anchor>&nbsp;
	<hr>
        <span id="scopeOneSpan" />
        <netui:tree dataSource="pageFlow.tree" selectionAction="select" tagId="tree"
		renderTagIdLookup="true">
           <netui:treeItem expanded="true">Item-PageOne</netui:treeItem>
        </netui:tree>
	<hr>
        <p style="color: green">Below we access the tree, the result should be
	a <b>false</b>.
        </p>
        <p id="javaOut"></p>
    </body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "Tree by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("tree",s)) == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</html>

  
