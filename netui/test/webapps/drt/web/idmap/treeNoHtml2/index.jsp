<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<html>
    <head>
        <netui:base/>
    </head>
    <body>
        <p style="color: green">This test will verify that a TreeItem will produce
	the proper JavaScript when there are no scrip containers.
        </p>
	<netui:anchor action="clearTree">Clear Tree</netui:anchor>&nbsp;
	<hr>
        <span id="scopeOneSpan" />
        <netui:tree dataSource="pageFlow.tree" selectionAction="select" tagId="tree">
           <netui:treeItem expanded="true" tagId="item">Item-PageOne</netui:treeItem>
        </netui:tree>
	<hr>
        <p style="color: green">Below we access the item using the
	lookup method, the result should be <b>false</b>.
        </p>
        <p id="javaOut"></p>
    </body>
    <script language="JavaScript" type="text/JavaScript">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "Item by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("item",s)) == null) + "</b><br>";

    p.innerHTML = val;
    </script>
</html>

  
