<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui:html>
    <head>
        <title>JIRA 886 -- adding a 'value' attribute to the headerCell tag</title>
    </head>
    <datagrid:portfolioXmlBean/>
    <netui:body>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol"/>
            <netui-data:headerCell value="Price"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}">
                 <netui:formatNumber language="EN" country = "US" type="currency"/>
            </netui-data:spanCell>
        </netui-data:rows>
    </netui-data:dataGrid>
  </netui:body>
</netui:html>
