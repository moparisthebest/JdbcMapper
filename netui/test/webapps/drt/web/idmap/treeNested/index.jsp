<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>

<netui:html generateIdScope="true">
    <head>
        <netui:base/>
    </head>
    <netui:body>
	<h4>Nested Tree with RunAtClient Tree</h4>
        <p style="color: green">This test sets the TagId on both a tree and an
	item within it then accesses those nodes through the JavaScript lookup
	methods.
        </p>
	<netui:anchor action="clearTree">Clear Tree</netui:anchor>&nbsp;
	<hr>
	<table width="100%"><tr><td width="50%" valign="top">
	<netui:scriptContainer generateIdScope="true" runAtClient="true">
        <netui:scriptHeader treeSupport='true'/>
        <span id="scopeOneSpan" />
        <netui:tree dataSource="pageFlow.tree" selectionAction="select" tagId="tree"
	        selectionStyle="color:red" unselectStyle="color:blue"
		renderJavaScript="true" runAtClient="true">
           <netui:treeItem expanded="true" tagId="item1">
	       <netui:treeLabel>Item-PageOne</netui:treeLabel>
              <netui:treeItem expanded="true" tagId="item2">Item-PageTwo</netui:treeItem>
              <netui:treeItem expanded="true" tagId="item3">Item-PageThree</netui:treeItem>
	   </netui:treeItem>
        </netui:tree>
        </netui:scriptContainer>
	</td><td>
	<netui:scriptContainer generateIdScope="true" runAtClient="true">
        <span id="scopeTwoSpan" />
        <netui:tree dataSource="pageFlow.treeTwo" selectionAction="select" tagId="tree"
	        selectionStyle="color:red" unselectStyle="color:blue"
		renderJavaScript="true">
           <netui:treeItem expanded="true" tagId="item1">
	       <netui:treeLabel>Item-PageOne</netui:treeLabel>
              <netui:treeItem expanded="true" tagId="item2">Item-PageTwo</netui:treeItem>
              <netui:treeItem expanded="true" tagId="item3">Item-PageThree</netui:treeItem>
	   </netui:treeItem>
        </netui:tree>
        </netui:scriptContainer>
	</td></tr></table>
	<hr>
        <p style="color: green">Below we access the tree and item using the
	lookup method.
        </p>
        <p id="javaOut"></p>
<netui:scriptBlock placement="after">

    var p = document.getElementById("javaOut");
    var s = document.getElementById("scopeOneSpan");

    var val = "<b>Document Access</b><br>";
    val = val + "Tree by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("tree",s)) == null) + "</b><br>";
    val = val + "Item by id is null: <b>" +
        (document.getElementById(lookupIdByTagId("item",s)) == null) + "</b><br>";

    p.innerHTML = val;
</netui:scriptBlock>
</netui:body>
</netui:html>

  
