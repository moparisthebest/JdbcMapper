<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui:html>
  <head>
    <title>CheckboxGroup Singleton</title>
        <style type="text/css">
        .normalAttr {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal {color: #cc9999;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #99cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        </style>
  </head>
  <body>
    <h4>CheckboxGroup Singleton</h4>
    <p style="color:green">
    </p>
        <netui:form action="post">
	    <h4>defaultValue="checked"</h4>
            <netui:checkBoxGroup dataSource="pageFlow.resultsOne"
		defaultValue="checked" orientation="vertical">
                <netui:checkBoxOption value="Check One"/>
                <netui:checkBoxOption value="Check Two"/>
                <netui:checkBoxOption value="Check Three"/>
                <netui:checkBoxOption value="Check Four"/>
            </netui:checkBoxGroup>
	    <hr>
	    <h4>defaultValue="checked"</h4>
            <netui:checkBoxGroup dataSource="pageFlow.resultsTwo"
		defaultValue="unchecked" orientation="vertical">
                <netui:checkBoxOption value="Check One"/>
                <netui:checkBoxOption value="Check Two"/>
                <netui:checkBoxOption value="Check Three"/>
                <netui:checkBoxOption value="Check Four"/>
            </netui:checkBoxGroup>
	    <netui:button value="submit"/>
        </netui:form>
  </body>
</netui:html>

  