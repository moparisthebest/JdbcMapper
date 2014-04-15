<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="formData" type="xhtml.Controller.SelectTests" required="false"/>
<netui-data:declarePageInput name="action" type="java.lang.String" required="false"/>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Image Post Results</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        p {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;height: 400px;}
        </style>
    </head>        
  <netui:body>
    <table><tr><td align="left"><h1 class="normalHead">Image Post Results</h1></td>
        <td align="right"><netui:anchor styleClass="normal" action="goImageTest">Image Tests</netui:anchor></td>
    </tr></table>
  </netui:body>
</netui:html>


  
