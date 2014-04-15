<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<netui:html documentType="xhtml1-transitional">
    <head>
        <title>Form Label Tests</title>
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
    <h1 class="normalHead">Form Label Tests</h1>
    <netui:anchor action="begin" rel="contents" styleClass="normal" rev="chapter">Home</netui:anchor>
    <div class="resultDiv">
            <netui:form action="postFormLabelResults">
                <table>
                    <tr valign="top">
                        <td><netui:label value="Value Three:" for="labelOne" styleClass="normal" /></td>
                        <td>
                        <netui:textBox dataSource="actionForm.valueOne" tagId="labelOne"/>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td><netui:label value="Value Two:" for="labelTwo" styleClass="normal" /></td>
                        <td>
                        <netui:textBox dataSource="actionForm.valueTwo" tagId="labelTwo"/>
                        </td>
                    </tr>
                    <tr valign="top">
                        <td><netui:label value="Value Three:" for="labelThree" styleClass="normal" /></td>
                        <td>
                        <netui:textBox dataSource="actionForm.valueThree" tagId="labelThree"/>
                        </td>
                    </tr>
                </table>
                <br />
                <netui:button type="submit" value="Post Form"/>
            </netui:form>
     </div>
    </netui:body>
</netui:html>
  
