<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="datagrid" tagdir="/WEB-INF/tags/org/apache/beehive/netui/test/databinding/tagfiles" %>
<netui-template:template templatePage="../site/template.jsp">
    <netui-template:setAttribute name="title" value="Filtered Style Test"/>
    <netui-template:section name="body">
    <p>
    <datagrid:portfolioXmlBean/>
    <script language="JavaScript" type="text/JavaScript" 
            src="${pageContext.request.contextPath}/resources/beehive/version1/javascript/netui-datagrid.js"></script>
<script language="JavaScript">
function doFilter(gridName) {
  var netuiFilterUrl = new NetUIFilterURL();
  netuiFilterUrl.init(window.location.search);

  var filter0 = new NetUIFilter('symbol', 'eq', 'BEAS');
  var netuiFilters = netuiFilterUrl.lookupFiltersForDataGrid(gridName);
  if(netuiFilters == null) {
    netuiFilters = new NetUIFilterList(gridName);
    netuiFilterUrl.addFilterList(netuiFilters);
  }

  /* add it to the filter array */
  var filterArray = [filter0];
  netuiFilters.replaceFilters(filterArray);
  var search = netuiFilterUrl.toQueryString();

  /* set the window's location */
  alert('search: ' + search);
  if(search != null && search.indexOf('?') < 0)
    search += '?' + search;
  window.location = window.location.protocol + "//" + location.host + window.location.pathname + (search != null ? search : '');

  return false;
}
</script>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true" pageSize="10"/>
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol" filterExpression="symbol"/>
            <netui-data:headerCell headerText="Price"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}" filterExpression="symbol"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    <br/>
    <netui:anchor onClick="return doFilter('stocks');" href="styleFilteredColumn.jsp">Filter</netui:anchor>
    </p>
    <p>
    <b>Styled / filtered header cell</b><br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true" pageSize="10"/>
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol" filterExpression="symbol"/>
            <netui-data:headerCell headerText="Price"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    </p>
    <p>
    <b>Styled / filtered data cell</b><br/>
    <netui-data:dataGrid dataSource="pageScope.stocks" name="stocks">
        <netui-data:configurePager pageHref="disabledPagerNPE.jsp" disableDefaultPager="true" pageSize="10"/>
        <netui-data:header>
            <netui-data:headerCell headerText="Symbol"/>
            <netui-data:headerCell headerText="Price"/>
        </netui-data:header>
        <netui-data:rows>
            <netui-data:spanCell value="${container.item.symbol}" filterExpression="symbol"/>
            <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
    </netui-data:dataGrid>
    </p>
    </netui-template:section>
</netui-template:template>
