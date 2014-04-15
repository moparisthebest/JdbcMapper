<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Begin</title>
</head>
<body>
<h4>binding directly to a pageFlow</h4>
<p style="color:green">
This test binds a textbox value directly to a pageflow property.  The value is found from a property
on the page flow.  The action called by the form takes a form bean which is ignored.  This is almost the exact same
test as <b>13803</b>, except there the action does not have a form bean. The value then updates that property.  
The results page shows the updated value.
</p>
<netui:form action="next1">
    <netui:textBox dataSource="pageFlow.string"/>
    <br>
    <netui:button type="submit">Submit</netui:button>
</netui:form>
</body>
</html>
