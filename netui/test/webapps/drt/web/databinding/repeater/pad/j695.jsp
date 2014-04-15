<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
<netui:body>
    <% pageContext.setAttribute("itemArray", new String[] {"zero", "one", "two", "three", "four"}); %>
    <netui-data:repeater dataSource="pageScope.itemArray">
        <netui-data:pad maxRepeat="2"/>
        <netui-data:repeaterItem>
            <netui:span value="${container.item}" />
        </netui-data:repeaterItem>
    </netui-data:repeater> 
</netui:body>
</netui:html>