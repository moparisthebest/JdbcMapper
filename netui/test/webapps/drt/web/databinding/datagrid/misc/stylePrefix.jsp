<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>

<netui:html>
  <head>
    <title>CSS Prefix Test</title>
        <style type="text/css">
        .foo-header {
            background-color: #5f7797;
        }
        .foo-even {
            background-color: #ffffff;
        }
        .foo-odd {
            background-color: #bfc4cb;
        }
        </style>
  </head>
  <netui:body>
    <p>
    <datagrid:portfolioXmlBean/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio" styleClassPrefix="foo" >
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Price"/>
        <netui-data:headerCell headerText="Web"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
            <netui-data:anchorCell href="${container.item.web}" value="${container.item.name}">
                <netui:parameter name="rowid" value="${container.index}"/>
                <netui:parameter name="symbol" value="${container.item.symbol}"/>
            </netui-data:anchorCell>
        </netui-data:rows>
    </netui-data:dataGrid>
    </p>
  </netui:body>
</netui:html>
