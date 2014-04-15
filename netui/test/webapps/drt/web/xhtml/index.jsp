<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>XHTML Verification Tests</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;height: 400px;}
        </style>
        <netui:base />
    </head>        
  <netui:body>
    <h1 class="normalHead">XHTML Verification Tests</h1>
    <ul>
    <li><netui:anchor styleClass="normal" action="goAnchorTest" rel="start">Anchor Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goCheckboxTest" rel="start">Checkbox Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goRadioTest" rel="start">RadioButton Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goSelectTest" rel="start">Select Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goImageTest" rel="start">Image Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goLabelTest" rel="start">Label Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goFormLabelTest" rel="start">Form Label Test</netui:anchor></li>
    <li><netui:anchor styleClass="normal" action="goTreeTest" rel="start">Tree Test</netui:anchor></li>
    </ul>
  </netui:body>
</netui:html>

  
