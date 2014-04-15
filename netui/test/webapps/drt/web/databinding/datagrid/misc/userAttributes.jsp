<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>

<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Basic Data Grid"/>
    <netui-template:section name="body">
    <script language="javascript">
function doAlert(node, index)
{
  alert("hello from item: " + index);
}
    </script>
    <p>
    <datagrid:portfolioXmlBean/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="portfolio">
    <netui-data:header>
        <netui-data:headerCell headerText="Symbol"/>
        <netui-data:headerCell headerText="Shares"/>
        <netui-data:headerCell headerText="Web"/>
    </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}" onClick="javascript:alert(this)">
              <netui:attribute name="attr-test-symbol" value="${container.item.symbol}"/>                                         
            </netui-data:spanCell>
            <netui-data:spanCell value="${container.item.price}">
              <netui:attribute name="attr-onlyeven" value="${container.index % 2 == 0 ? 'foo' : null}"/>
              <netui:attribute name="attr-index" value="${container.index}"/>
            </netui-data:spanCell>
            <netui-data:anchorCell href="${container.item.web}" value="${container.item.name}" onMouseOver="doAlert(this, ${container.index})">
                <netui:parameter name="rowid" value="${container.index}"/>
                <netui:parameter name="symbol" value="${container.item.symbol}"/>
            </netui-data:anchorCell>
        </netui-data:rows>
    </netui-data:dataGrid>
    </netui-template:section>
</netui-template:template>
