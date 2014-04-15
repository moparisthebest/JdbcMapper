<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui:html>
  <head>
    <title>CSS Prefix Test</title>
        <style type="text/css">
        tr.header {
            background-color: #5f7797;
        }
        tr.even {
            background-color: #ffffff;
        }
        tr.odd {
            background-color: #bfc4cb;
        }
        #stocks tr.header {
            background-color: #bfc4cb;
        }
        #stocks tr.even {
            background-color: #ffffff;
        }
        #stocks tr.odd {
            background-color: #5f7797;
        }
        </style>
  </head>
  <netui:body>
    <p>
    <datagrid:portfolioXmlBean/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks" styleClassPolicy="empty">
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <div id="stocks">
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks" styleClassPolicy="empty">
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    </div>
    </p>
  </netui:body>
</netui:html>
