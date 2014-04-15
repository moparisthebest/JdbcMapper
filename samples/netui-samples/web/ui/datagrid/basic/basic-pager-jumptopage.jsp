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
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib prefix="sampledata" tagdir="/WEB-INF/tags/data" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Data Grid with Jump-to-Page Pager"/>
  <netui-template:section name="main">
    <sampledata:petdata/>
    <br/>
    <br/>
      <netui-data:dataGrid dataSource="pageScope.pets" name="pets">
          <netui-data:configurePager defaultPageSize="2" pagerFormat="firstPrevNextLast" disableDefaultPager="true"/>
          <netui-data:header>
              <netui-data:headerCell headerText="Name"/>
              <netui-data:headerCell headerText="Description"/>
              <netui-data:headerCell headerText="Price"/>
          </netui-data:header>
          <netui-data:rows>
              <netui-data:spanCell value="${container.item.name}"/>
              <netui-data:spanCell value="${container.item.description}"/>
              <netui-data:spanCell value="${container.item.price}"/>
          </netui-data:rows>
          <netui-data:footer>
            <td colspan="2" align="left">
              <netui-data:renderPager/>
            </td>
            <td colspan="1" align="right">
                <%-- This is the basic pager that uses its own <select> tag --%>
                <c:if test="${dataGrid.dataSet.size > 0}">
                  <form name="pageForm" action="basic-pager-jumptopage.jsp">
                    Jump to Page:
                <script type="text/javascript">
                  function doPagerSubmit(comp)
                  {
                    var form = document.forms["pageForm"];
                    form.method="GET";
                    form.submit();
                  }
                  </script>
                  <select name="${dataGrid.urlBuilder.pagerRowQueryParamKey}" onchange="doPagerSubmit(this); return true;">
                    <netui-data:repeater dataSource="dataGrid.urlBuilder.pagerParamValues">
                    <c:choose>
                      <c:when test="${container.index == dataGrid.state.pagerModel.page}">
                        <option value="${container.item}" selected="true">${container.index+1}</option>
                      </c:when>
                      <c:otherwise>
                        <option value="${container.item}">${container.index+1}</option>
                      </c:otherwise>
                    </c:choose>
                    </netui-data:repeater>
                  </select>
                </c:if>
            </td>
          </netui-data:footer>
      </netui-data:dataGrid>
      <p><netui:anchor href="../index.jsp">Start over</netui:anchor></p>
  </netui-template:section>
</netui-template:template>
