<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Date Format Test</title>
</head>
<body>
<h4>Date Format Test</h4>
<b>Select:</b> <br />
  <netui:select size="5" dataSource="pageFlow.select"> 
    <netui:formatDate pattern="MM/dd/yyyy" country="USA" /> 
    <netui:selectOption value="1">07/10/96 4:5 PM, PDT</netui:selectOption> 
    <netui:selectOption value="2">01/28/2063</netui:selectOption> 
    <netui:selectOption value="3">01/28/63</netui:selectOption> 
    <netui:selectOption value="4">01012063</netui:selectOption> 
    <netui:selectOption value="5">010163</netui:selectOption> 
  </netui:select> 
<hr />
<b>TextBox:</b><br />
  <netui:textBox dataSource="pageFlow.textBox"> 
     <netui:formatNumber pattern="####.00"/> 
  </netui:textBox> 
<hr />
<b>TextArea:</b> 
 <netui:textArea cols="20" rows="3" dataSource="pageFlow.textArea" 
                 defaultValue="${pageFlow.textDefault}"> 
   <netui:formatString pattern="###.## "/> 
   <netui:formatString pattern="###### USD"/> 
 </netui:textArea> 
</body>
</html>
