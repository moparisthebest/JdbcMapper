<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Anchor Target Page</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:10pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;height: 400px;}
        </style>
    </head>        
  <netui:body>
    <netui:anchor tagId="top"/>
    <table width="400pt"><tr><td valign="middle" align="left"><p class="normalHead">Anchor Target Page</p></td>
    <td valign="middle" align="right"><netui:anchor styleClass="normal" action="goAnchorTest">Anchor Tests</netui:anchor></td>
    </tr></table>
    <p class="normal">Targets for the anchor tests</p>
    <p class="normal">Color: <netui:span value="${param.color}" /></p>
    <p><netui:anchor styleClass="normal" linkName="bottomP">Go to Bottom</netui:anchor></p>
    <div style="height:300pt;width:200pt;border:thin solid;">
    </div>
    <netui:anchor tagId="bottom" />
    <p id="bottomP" class="normal"><netui:anchor linkName="top" >Bottom of the page...</netui:anchor></p>
  </netui:body>
</netui:html>


  
