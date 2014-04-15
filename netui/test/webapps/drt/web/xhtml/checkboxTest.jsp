<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Input Test Page</title>
        <style type="text/css">
        .normal {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal2 {color: #cc0099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normal3 {color: #00cc99;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalBold {color: #000099;font-family:Verdana; font-size:8pt; font-weight: bold;margin:0,0,0,0;}
        li {color: #000099;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalError {color: #ff0033;font-family:Verdana; font-size:8pt;margin:0,0,0,0;}
        .normalHead {color: #000099;font-family:Verdana; font-size:8pt;font-weight: strong;margin:0,0,0,0;}
        .title {color: #000099;font-family:Verdana; font-size:12pt;margin:2,0,5,0;}
        .resultDiv {border: thin solid;margin:5,5,5,5;}
        </style>
    </head>        
  <netui:body>
    <h1 class="normalHead">CheckBox and CheckBox Group tests</h1>
    <netui:anchor action="begin" rel="contents" styleClass="normal" rev="chapter">Home</netui:anchor>
    <div class="resultDiv">
        <p class="normal">CheckBox and CheckBox Group tests</p>
        <netui:form action="postCheckForm">
            <table>
                <tr valign="top">
                    <td class="normalBold" align="right">CheckBox Group</td>
                    <td>
                    <netui:checkBoxGroup dataSource="actionForm.cbg" orientation="horizontal">
                            <netui:checkBoxOption value="CheckBox Option 1" labelStyleClass="normal" title="Title Text" dir="ltr" lang="en" alt="Alt Text" disabled="true">Text CB Option One</netui:checkBoxOption>
                            <netui:checkBoxOption value="CheckBox Option 2" labelStyleClass="normal">Text CB Option Two</netui:checkBoxOption>
                    </netui:checkBoxGroup>
                    </td>
                </tr>
                <tr valign="top" >
                    <td class="normalBold" align="right">Repeating CheckBox Group</td>
                    <td>
                    <table>
                    <netui:checkBoxGroup dataSource="actionForm.optCbg" 
                            optionsDataSource="${pageFlow.checkBoxOptions}" repeater="true">
                            <tr><td align="right" class="normal2">
                            <netui:span value="${container.item}" />
                            </td><td>
                            <netui:checkBoxOption value="${container.item}" />
                            </td></tr>
                    </netui:checkBoxGroup>
                    </table>
                    </td>
                </tr>
                <tr valign="top">
                    <td class="normalBold" align="right">CheckBox 1</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.check1" dir="ltr" lang="en"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td class="normalBold" align="right">CheckBox 2</td>
                    <td>
                    <netui:checkBox dataSource="actionForm.check2"/>
                    </td>
                </tr>
                <tr valign="top">
                    <td colspan="2" align="center">
                        <table cellspacing="0" border="1">
                        <tr><th class="normalBold" valign="top" align="center">Complex Repeater</th><td>
                        <netui:checkBoxGroup dataSource="actionForm.optCbgMap" optionsDataSource="${pageFlow.opts}" 
                            repeater="true">
                            <netui:checkBoxOption value="${container.item.optionValue}" />
                                <netui:span styleClass="${container.item.style}" value="${container.item.name}" /><br/>
                        
                        </netui:checkBoxGroup>
                        </td></tr></table>
                    </td>
                </tr>                        
            </table>
            <br />
            <netui:button type="submit" value="Post To Results One" styleClass="normal" dir="ltr" lang="en" title="Post to Results One" alt="Post to Results One"/>
            <netui:button type="submit" value="Post To Results Two" styleClass="normal" action="postCheckFormTwo"/>
        </netui:form>
    </div>
    </netui:body>
</netui:html>

  
