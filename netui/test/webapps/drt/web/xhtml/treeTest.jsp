<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Tree Test</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        li {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;height: 400px;}
        </style>
        <netui:base />
    </head>        
  <netui:body>
    <h1 class="normalHead">Tree Tests</h1>
    <netui:tree dataSource="pageFlow.tree1"
           selectionAction="postback" tagId="tree">
       <netui:treeItem expanded="true" action="postback">
           <netui:treeLabel>0</netui:treeLabel>
           <netui:treeItem expanded="true" action="postback">
              <netui:treeLabel>0.0</netui:treeLabel>
              <netui:treeItem action="postback">
                        <netui:treeLabel>0.0.0</netui:treeLabel>
                        <netui:treeItem action="postback">0.0.0.0</netui:treeItem>
                        <netui:treeItem action="postback">0.0.0.1</netui:treeItem>
                    </netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">
                    <netui:treeLabel>0.1</netui:treeLabel>
                    <netui:treeItem action="postback">0.1.0</netui:treeItem>
                    <netui:treeItem action="postback">0.1.1</netui:treeItem>
                </netui:treeItem>
                <netui:treeItem expanded="true" action="postback">0.2</netui:treeItem>
            </netui:treeItem>
    </netui:tree>
    </netui:body>
</netui:html>
