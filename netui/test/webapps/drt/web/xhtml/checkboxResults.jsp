<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui-data:declarePageInput name="formData" type="xhtml.Controller.CheckboxTests"/>
<netui-data:declarePageInput name="action" type="java.lang.String"/>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Checkbox Results</title>
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
    <table width="400pt"><tr><td align="left"><h1 class="normalHead">Checkbox Results</h1></td>
        <td align="right"><netui:anchor styleClass="normal" action="goCheckboxTest">Input Tests</netui:anchor></td>
    </tr></table>
    <p>
        Action: <netui:span value="${pageInput.action}"></netui:span> 
    </p>
        <table border="1" cellspacing="0">
        <tr class="tablehead" valign="top">
            <th class="normal">CheckBox Group</th>
            <th class="normal">Repeating CheckBox Group</th>
            <th class="normal">Check One</th>
            <th class="normal">Check Two</th>
            <th class="normal">Complex Repeater</th>
        </tr>
        <tr valign="top">
            <td>
            <netui-data:repeater dataSource="pageInput.formData.cbg">
                <netui-data:repeaterHeader><table class="tablebody" border="0"></netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <tr valign="top">
                        <td><netui:span value="${container.item}" defaultValue="&nbsp;" styleClass="normal"></netui:span></td>
                    </tr>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
            </netui-data:repeater>
            </td>
             <td>
            <netui-data:repeater dataSource="pageInput.formData.optCbg">
                <netui-data:repeaterHeader><table class="tablebody" border="0"></netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <tr valign="top">
                        <td><netui:span value="${container.item}" defaultValue="&nbsp;" styleClass="normal"></netui:span></td>
                    </tr>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
            </netui-data:repeater>
            </td>
            <td><netui:span value="${pageInput.formData.check1}" defaultValue="&nbsp;" styleClass="normal"></netui:span></td>
            <td><netui:span value="${pageInput.formData.check2}" defaultValue="&nbsp;" styleClass="normal"></netui:span></td>
            <td>
            <netui-data:repeater dataSource="pageInput.formData.optCbgMap">
                <netui-data:repeaterHeader><table class="tablebody" border="0"></netui-data:repeaterHeader>
                <netui-data:repeaterItem>
                    <tr valign="top">
                        <td><netui:span value="${container.item}" defaultValue="&nbsp;" styleClass="normal"></netui:span></td>
                    </tr>
                </netui-data:repeaterItem>
                <netui-data:repeaterFooter></table></netui-data:repeaterFooter>
            </netui-data:repeater>
            </td>
        </tr>
        </table>
  </netui:body>
</netui:html>


  
