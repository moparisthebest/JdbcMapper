<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<netui:html>
<head>
<title>Default Value Test</title>
</head>
<netui:body>
<h4>Test of Default Values</h4>
<p style="color:green">
This test binds two text boxes to values in the page flow.  The values in the first text box, the page flow value
has a defined value.  In the second textbox, the page flow value does not.  This means that the default value
should be displayed. Below the form are the actual values being bound to in the page flow.  The 'Default' value
is displayed after the bound value.
</p>
<netui:form action="postback">
Textbox 1: <netui:textBox dataSource="pageFlow.text1" defaultValue="${pageFlow.text1Default}"/><br>
Textbox 2: <netui:textBox dataSource="pageFlow.text2" defaultValue="${pageFlow.text2Default}"/> (null data source) <br>
<netui:button type="submit">Submit</netui:button>
</netui:form>
<hr>
<h4>Actual PageFlowController values</h4>
text1: <netui:span value="${pageFlow.text1}"/><br>
text1Default: <netui:span value="${pageFlow.text1Default}"/><br>
text2: <netui:span value="${pageFlow.text2}"/><br>
text2Default: <netui:span value="${pageFlow.text2Default}"/><br>
</netui:body>
</netui:html>
