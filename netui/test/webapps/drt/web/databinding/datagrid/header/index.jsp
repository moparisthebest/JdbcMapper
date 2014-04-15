<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>

<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Basic Data Grid"/>
    <netui-template:section name="body">
    <p>
<datagrid:portfolioXmlBean/>
<br/>
<netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio">
        <netui-data:header>
            <netui-data:headerCell>
                <netui:anchor href="index.jsp">Symbol Anchor</netui:anchor>
            </netui-data:headerCell>
            <netui-data:headerCell headerText="Price">
                Should not render this.
            </netui-data:headerCell>
            <netui-data:headerCell>
                Web URL
            </netui-data:headerCell>
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
<br/>
<netui:anchor href="index.jsp">Reset</netui:anchor>
<br/>
    </p>
    </netui-template:section>
</netui-template:template>
