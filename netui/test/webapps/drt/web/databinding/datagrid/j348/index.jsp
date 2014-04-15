<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<netui:html>
    <head>
        <title>JIRA 348</title>
    </head>
    <netui:body>
    <script language="JavaScript" type="text/JavaScript" src="${pageContext.request.contextPath}/resources/beehive/version1/javascript/netui-datagrid.js"></script>
    <netui:form action="begin" tagId="filterForm">
        <table>
        <tr>
            <td>Customer ID</td><td><input type="text" value="" id="customerid"/></td>
            <td>Company Name</td><td><input type="text" value="" id="companyname"/></td>
        </tr>
        <tr><td colspan="2" align="left"><netui:anchor href="index.jsp" onClick="return doNetUIFilters('filterForm', 'customers');">Search</netui:anchor></td></tr>
        </table>
    </netui:form>
    <script language="JavaScript">
        doLoadNetUIFilters('filterForm', 'customers');
    </script>
    <br/>
    <netui:anchor action="begin" value="Link with Param">
        <netui:parameter name="foo" value="bar"/>
    </netui:anchor>
  </netui:body>
</netui:html>
