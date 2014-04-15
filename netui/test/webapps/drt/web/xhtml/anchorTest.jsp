<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Anchor Tests</title>
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
    <h1 class="normalHead">Anchor Tests</h1>
    <netui:anchor action="begin" rel="contents" rev="chapter" title="Link to the Contents" styleClass="normal" charSet="iso-8859-1" hrefLang="en" type="text/html">Test Main</netui:anchor>
    <ul>
    <li><netui:anchor action="goAnchorTarget" location="bottom" styleClass="normal" >Goto Target Page # bottom</netui:anchor></li>
    <li><netui:anchor action="goAnchorTarget" location="top" styleClass="normal" >Goto Target Page # top</netui:anchor></li>
    <li>Image Map with Anchors (only works in Netscape)<br/>
    <img src="redblue.gif" alt="pick a color" usemap="#map1" width="200" height="100" />
        <map id="map1" name="map1">
            <table width="200"><tr><td width="50%" align="center"> 
                <netui:anchor action="goAnchorTarget" shape="rect" coords="10, 10, 90, 90" style="color:#ff0033;" styleClass="normal">Red
                    <netui:parameter name="color" value="red"/>
                </netui:anchor>
            </td><td width="50%" align="center">
                <netui:anchor action="goAnchorTarget" shape="rect" coords="110, 10, 190, 90" style="color:#0000cc;" styleClass="normal">Blue
                    <netui:parameter name="color" value="blue"/>
                </netui:anchor>
            </td></tr></table>
        </map>
    </li>
    </ul>
    <netui:anchor action="begin" rel="contents" rev="chapter"
                  title="Link to the Contents" styleClass="normal"
                  charSet="iso-8859-1" hrefLang="en" type="text/html">
        Multiple Parameters
        <netui:parameter name="color" value="red"/>
        <netui:parameter name="color" value="blue"/>
    </netui:anchor>
    </netui:body>
</netui:html>
