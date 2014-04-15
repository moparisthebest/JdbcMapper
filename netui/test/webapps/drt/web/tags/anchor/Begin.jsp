<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Test of Anchor</title>
<netui:base />
</head>
<body>
<netui:anchor tagId="Top"/>
<h4>Anchor Tests</h4>
<p style="color:green">
This is a set of tests of anchors.  The following anchors will navigate to a target page.  At the
top and the bottom of the page are anchors which use the <b>linkName</b> attribute.
<br>
Anchor supports <b>action</b> and <b>href</b> as the primary means of navigation.  In addition
<b>linkName</b> can also be used to navigate within a page.  To create a named anchor, you simply
set the <b>tagId</b> attribute and nothing else.  This creates an anchor that can be a target
of another anchor.  Finally, the <b>location</b> attribute many be used with the <b>href</b>
attribute to create an anchor of the form of href#location.  The <b>location</b> attribute does
not work with an <b>action</b> attribute.
</p>
<ul>
<li><netui:anchor action="navigate">Navigate</netui:anchor> --
        Navigation through an <b>action</b></li>
<li><netui:anchor action="simpleAction">Navigate</netui:anchor> --
        Navigation through a <b>Simple Action</b></li>
<li><netui:anchor href="myForward.jsp">Navigate</netui:anchor> --
        Navigation through an <b>href</b> directly to the JSP</li>
<li><netui:anchor href="navigate.do">Navigate</netui:anchor> --
        Navigation through an <b>href</b> to the action</li>
<li><netui:anchor linkName="Label">Navigate</netui:anchor> --
        Navigate to a link on this page through an <b>linkName</b></li>
</ul>
<p>There is a bunch of blank space to create a target on the bottom of the page</p>
<hr />
<div style="height: 500px">
<p>Spacer Div</p>
</div>
<hr />
<p>
This is a named location 'Label'.  The link will take you back to the top of the page<br>
<netui:anchor tagId="Label"/>
<netui:anchor linkName="#Top">Go To Top</netui:anchor>
</p>
<p>
And test an opaque URI in an href...
<netui:anchor href="mailto:qrs@tuv.wxyz.com">mailto anchor</netui:anchor>
</p>
</body>
</html>
