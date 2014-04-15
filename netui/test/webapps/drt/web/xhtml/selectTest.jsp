<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Select Tests</title>
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
    </head>        
  <netui:body>
    <h1 class="normalHead">Select Tests</h1>
    <netui:anchor action="begin" rel="contents" styleClass="normal" rev="chapter">Home</netui:anchor>
    <div class="resultDiv">
        <p class="normal">Select Tests</p>
            <netui:form action="postSelectForm">
                <table>
                    <tr valign="top">
                        <td class="normalBold" align="right">Select</td>
                        <td>
                        <netui:select dataSource="actionForm.sel" defaultValue="Select Opt 1">
                            <netui:selectOption value="Select Opt 1" styleClass="normalAttr" disabled="true">Select Option One</netui:selectOption>
                            <netui:selectOption value="Select Opt 2" styleClass="normalAttr2">Select Option Two</netui:selectOption>
                        </netui:select>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td class="normalBold" align="right">Select With Options</td>
                        <td>
                        <netui:select dataSource="actionForm.multiSel" optionsDataSource="${pageFlow.selectOptions}" multiple="true" size="3" styleClass="normalAttr" style="background-color:#00ffff;"/>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td class="normalBold" align="right">Map Select</td>
                        <td>
                        <netui:select dataSource="actionForm.selOpts" optionsDataSource="${pageFlow.selectMapOptions}"/>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td class="normalBold" align="right">Nullable Select</td>
                        <td>
                        <netui:select dataSource="actionForm.nullable" optionsDataSource="${pageFlow.nullSelectOptions}" 
                            size="7" nullable="true" defaultValue="${pageFlow.selectDefault}" repeater="true">
                            <netui:selectOption repeatingType="option" value="${container.item}" styleClass="normalAttr" />
                            <netui:selectOption repeatingType="dataSource" value="ds ${container.item}" styleClass="normalAttr2" />
                            <netui:selectOption repeatingType="default" value="def ${container.item}" styleClass="normalAttr3" />
                            <netui:selectOption repeatingType="null" value="Null" styleClass="normalAttr4"/>
                        </netui:select>
                        </td>
                    </tr>
                    
                </table>
                <br />
                <netui:button type="submit" value="Post Form"/>
            </netui:form>
    </div>
    </netui:body>
</netui:html>

  
