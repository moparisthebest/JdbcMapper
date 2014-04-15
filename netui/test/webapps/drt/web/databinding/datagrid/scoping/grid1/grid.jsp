<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<html>
  <head>
      <title>NetUI JSP</title>
  </head>
  <body>
  <b>This is a data grid!</b><br/>
  <datagrid:portfolioXmlBean/>
  <netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio">
      <netui-data:configurePager defaultPageSize="2" pageAction="begin"/>
      <netui-data:header>
          <netui-data:headerCell headerText="Symbol"/>
          <netui-data:headerCell headerText="Price"/>
      </netui-data:header>
      <netui-data:rows>
          <netui-data:spanCell value="${container.item.symbol}"/>
          <netui-data:spanCell value="${container.item.price}"/>
      </netui-data:rows>
  </netui-data:dataGrid>
  </body>
</html>