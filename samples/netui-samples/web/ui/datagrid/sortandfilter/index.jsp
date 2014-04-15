<%--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netuidata" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<netui-template:template templatePage="/resources/template/template.jsp">
<netui-template:setAttribute name="sampleTitle" value="NetUI Data Grid Sort and Filter"/>
<netui-template:section name="main">

<link rel="stylesheet" href="style.css">
<%-- ====================================================================== --%>
<div id="leftcolumn">
        <%-- The Filter Form --%>
    <netui:form action="filter" tagId="filterForm">
        <table class="customers-caption">
            <tr class="highlight"><td colspan="2"><netui:span styleClass="title" value="Filter"/></td></tr>
            <tr>
                <td>Customer ID</td><td><netui:textBox dataSource="actionForm.customerId"/></td>
            </tr>
            <tr>
                <td>Company Name</td><td><netui:textBox dataSource="actionForm.companyName"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <netui:anchor formSubmit="true" value="Filter"/>
                </td>
            </tr>
        </table>
    </netui:form>
</div>

<%-- ====================================================================== --%>
<div id="rightcolumn">

    <%-- The Data Grid --%>
    <netuidata:dataGrid dataSource="pageInput.customers" name="customers" styleClassPrefix="customers">
        <%-- Configure the pager and disable its default rendering --%>
        <netuidata:configurePager pageAction="grid" disableDefaultPager="true" defaultPageSize="5"/>

        <%-- The Data Grid's caption--%>
        <netuidata:caption>
            <table class="heavy-border title">
                <tr class="highlight">
                    <td>
                        <netui:span styleClass="title" value="Customers"/><br/>
                    </td>
                </tr>
                <tr class="highlight">
                    <td>
                        Displaying customer ${dataGrid.state.pagerModel.row+1}
                        to ${dataGrid.state.pagerModel.lastRowForPage+1}
                        of ${dataGrid.state.pagerModel.dataSetSize} matching customers.
                    </td>
                </tr>
                <tr>
                    <%-- Render the pager here --%>
                    <td><br/><netuidata:renderPager/><br/><br/></td>
                </tr>
            </table>
            <br/>
        </netuidata:caption>
        <%-- The Data Grid's header--%>
        <netuidata:header>
            <netuidata:headerCell sortAction="grid"
                                  sortExpression="customerid"
                                  cellOnMouseOver="this.className='headerCellOver'"
                                  cellOnMouseOut="this.className='headerCellOut'">
                <netui:anchor action="grid">
                    <netui:parameterMap
                        map="${netuidata:buildQueryParamsMapForSortExpression(dataGrid.urlBuilder, 'customerid')}"/>
                    <netui:span value="Customer ID"/>
                    <c:choose>
                        <c:when test="${netuidata:isSortedAscending(dataGrid.state.sortModel, 'customerid')}">
                            <netui:image src="images/up.gif" border="false"/>
                        </c:when>
                        <c:when test="${netuidata:isSortedDescending(dataGrid.state.sortModel, 'customerid')}">
                            <netui:image src="images/down.gif" border="false"/>
                        </c:when>
                        <c:otherwise>
                            <netui:image src="images/blank.gif" border="false"/>
                        </c:otherwise>
                    </c:choose>
                </netui:anchor>
            </netuidata:headerCell>
            <netuidata:headerCell sortAction="grid"
                                  sortExpression="companyname"
                                  cellOnMouseOver="this.className='headerCellOver'"
                                  cellOnMouseOut="this.className='headerCellOut'">
                <netui:anchor action="begin">
                    <netui:parameterMap
                        map="${netuidata:buildQueryParamsMapForSortExpression(dataGrid.urlBuilder, 'companyname')}"/>
                    <netui:span value="Company Name"/>
                    <c:choose>
                        <c:when test="${netuidata:isSortedAscending(dataGrid.state.sortModel, 'companyname')}">
                            <netui:image src="images/up.gif" border="false"/>
                        </c:when>
                        <c:when test="${netuidata:isSortedDescending(dataGrid.state.sortModel, 'companyname')}">
                            <netui:image src="images/down.gif" border="false"/>
                        </c:when>
                        <c:otherwise>
                            <netui:image src="images/blank.gif" border="false"/>
                        </c:otherwise>
                    </c:choose>
                </netui:anchor>
            </netuidata:headerCell>
            <netuidata:headerCell value="Contact Name"/>
            <netuidata:headerCell value="Contact Title"/>
        </netuidata:header>
        <%-- The Data Grid's data rows--%>
        <netuidata:rows>
            <netuidata:spanCell value="${container.item.customerId}" filterExpression="customerid"/>
            <netuidata:spanCell value="${container.item.companyName}"/>
            <netuidata:spanCell value="${container.item.contactName}"/>
            <netuidata:spanCell value="${container.item.contactTitle}"/>
        </netuidata:rows>
    </netuidata:dataGrid>
</div>
</netui-template:section>
</netui-template:template>