<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>encodeContent.jsp</title>
        <link href="style.css" rel="stylesheet" type="text/css">
        <netui:scriptHeader />
        <style type="text/css">
        .alien {
            position: absolute;
            left: 170pt;
        }
        .leaf {
            font-family: "new century schoolbook", serif ; 
            font-size: 10pt;
            color: #c90000;
        }
        .container {
            font-family: "new century schoolbook", serif ; 
            font-size: 12pt;
            color: #990000;
        }
        </style>
        <netui:base/>
    </head>
    <netui:body>
        <h4 class="title"><netui:anchor action="begin" styleClass="homeAnchor">Home</netui:anchor>encodeContent.jsp [goEncodeContent.do] </h4>
        <!-- Attribute Information -->
        <div style='float:left;width:300px'>
        <table cellpadding="2" cellspacing="0" border="1" width="300px">
        <tr><th>Creation</th><td>Static</td></tr>
        <tr><th>tree</th><td>{pageFlow.tree20}</td></tr>
        <tr><th>action</th><td>postback</td></tr>
        <tr><th>runAtClient</th><td>false</td></tr>
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
	Verify that both the content and label may be encoded for HTML during tree
	processing.  In the tree node below both the label and content contains JavaScript
	which will causes an alert in the browser if not encoded.
        <hr style="clear:left">
        <div class="content">
        <netui:tree dataSource="pageFlow.tree20" selectionAction="postback" tagId="tree" escapeForHtml="true">
            <netui:treeItem expanded="true">
                <netui:treeLabel>JavaScript:
	        <script language="JavaScript" type="text/JavaScript">
		  alert("Label Script");
                </script>
                </netui:treeLabel>
                <netui:treeContent>Content with JavaScript:
	        <script language="JavaScript" type="text/JavaScript">
		  alert("Content Script");
                </script>
		</netui:treeContent>
            </netui:treeItem>
        </netui:tree>
        </div>
    </netui:body>
</netui:html>
