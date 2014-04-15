<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html idScope="tree">
    <head>
        <title>dynamicClient.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>dynamicClient.jsp [goDynamicClient.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>dynamic</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree36}</td></tr>
        <tr><th>runAtClient</th><td>true</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        </table>
        </div>
        <!-- Postback information -->
        <div style='float:right;width:250px;border:solid 1pt gray;margin:2 4;'>
        <p style="font-weight:bold;text-align:center;margin: 0;padding:0;">Tree Postback Information</p>
        <table cellpadding='0' cellspacing='2' width="100%">
        <tr><th style="text-align:right" width="100pt">Expand:</th><td><netui:content value="${pageFlow.expand}"/></td></tr>
        <tr><th style="text-align:right" width="100pt">Selection:</th><td><netui:content value="${pageFlow.node}"/></td></tr>
        </table>
        </div>
        <hr style="clear:left">
        <div class="content">
	<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr><td widht="50%" valign="top">
	<netui:scriptContainer generateIdScope="true">
        <netui:scriptHeader />
        <netui:tree dataSource="pageFlow.tree36" selectionAction="postback"
	        selectedStyle="color:red" unselectedStyle="color:blue"
		tagId="tree" runAtClient="true" />
	</netui:scriptContainer>
	</td></tr>
	</table>
        </div>
    </netui:body>
</netui:html>
