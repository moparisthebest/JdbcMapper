<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui:html>
    <head>
        <title></title>
    </head>
    <netui:body>
    <datagrid:portfolioXmlBean/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="customers">
        <netui-data:configurePager defaultPageSize="2" pageHref="pagerExplicitHref.jsp"/>
        <netui-data:caption>
            Customers
        </netui-data:caption>
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol"/>
            <netui-data:headerCell headerText="Price"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
  </netui:body>
</netui:html>
