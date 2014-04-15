<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Image Tests</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalAttr {color: #ff9900;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalAttr2 {color: #0099ff;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalAttr3 {color: #9900ff;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalAttr4 {color: #99ff00;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalBold {color: #000099;font-family:Verdana; font-size:8pt; font-weight: bold;margin:0,0,0,0;}
        li {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;margin:5,5,5,5;}
        </style>
        <netui:scriptHeader />
    </head>        
  <netui:body>
    <h1 class="normalHead">Image Tests</h1>
    <netui:anchor action="begin" rel="contents" styleClass="normal" rev="chapter">Home</netui:anchor>
    <div class="resultDiv">
        <p class="normal">Images</p>
        <table width="100%">
        <tr><td align="right" class="normal">Image:</td><td align="left">
            <netui:image src="./image/Anieyes.gif" border="1" width="72" height="37" style="border-color:#00cccc;" alt="eyeballs"/>
        </td></tr>
        <tr><td align="right" class="normal">ImageAnchor:</td><td align="left">
                    <netui:imageAnchor action="postImageForm" src="image/activepython.gif" rolloverImage="image/activepythonroll.gif" border="0" alt="Anchor"/>
        </td></tr>
        </table>
    </div>
    <div class="resultDiv">
        <p class="normal">Images in Forms</p>
            <netui:form action="postImageForm">
                <netui:imageButton src="./image/back.gif" alt="Navigate" accessKey="G" value="foo" rolloverImage="/coreWeb/xhtml/image/backRoll.gif"/>
            </netui:form>
    </div>
    </netui:body>
</netui:html>


  
