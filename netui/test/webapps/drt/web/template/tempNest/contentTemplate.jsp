<%@ page language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="temp" %>
<table>
<tr><th><temp:includeSection name="title"/></th></tr>
<tr><td><span style='color:<temp:attribute name='color'/>'>
<temp:includeSection name="content"/>
</span>
</td></tr>
</table>
