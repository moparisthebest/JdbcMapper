<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Basic Data Grid"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio" resourceBundlePath="databinding.datagrid.resources.simple-test">
        <netui:behavior facet="resource" name="pager.msg.next" value="Next Page (JSP)"/>
        <netui:behavior facet="resource" name="pager.msg.previous" value="Previous Page (JSP)"/>
        <netui:behavior facet="resource" name="pager.fmt.banner" value="The JSP thinks that you''re looking at page {0} of {1}"/>

        <netui-data:configurePager pageSize="2"/>
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol"/>
            <netui-data:headerCell headerText="Price"/>
            <netui-data:headerCell headerText="Web"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
            <netui-data:anchorCell value="${container.item.name}" href="${container.item.web}">
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
