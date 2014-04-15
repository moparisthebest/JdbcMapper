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
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ tag body-content="empty" %>
<%@ attribute name="languages" required="false" type="java.lang.String[]"%>
<%@ attribute name="categoryNames" required="true" type="java.lang.String[]"%>
<%--
  This tag also implicitly expects that the "actionForm" and "bundle"
  objects are passed in explicitly.

  Either NetUI or JSP needs to have an <assert> tag.  <g>
--%>

<netui:span styleClass="boldlabel" value="${bundle.default.accountInformation}"/>
<table class="tablegreen">
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.firstName}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="pageInput.actionForm.firstName"/>
        <br/>
        <netui:error key="firstName"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.lastName}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="actionForm.lastName"/>
        <br/>
        <netui:error key="lastName"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.email}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="actionForm.email" size="40"/>
        <br/>
        <netui:error key="email"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.phone}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="actionForm.phone"/>
        <br/>
        <netui:error key="phone"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.address1}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="actionForm.addr1" size="40"/>
        <br/>
        <netui:error key="address1"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.address2}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="actionForm.addr2" size="40"/>
        <br/>
        <netui:error key="address2"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.city}:"/></td>
        <td class="celldata">
        <netui:textBox dataSource="actionForm.city"/>
        <br/>
        <netui:error key="city"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.state}:"/></td>
        <td class="celldata">
        <netui:textBox size="4" dataSource="actionForm.state"/>
        <br/>
        <netui:error key="state"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.zip}:"/></td>
        <td class="celldata">
        <netui:textBox size="10" dataSource="actionForm.zip"/>
        <br/>
        <netui:error key="zip"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.country}"/></td>
        <td class="celldata">
        <netui:textBox size="15" dataSource="actionForm.country"/>
        <br/>
        <netui:error key="country"/>
        </td>
    </tr>
</table>

<netui:span styleClass="boldlabel" value="${bundle.default.profileInformation}"/>

<table class="tablegreen">
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.languagePreference}:"/></td>
        <td class="celldata">
        <netui:select dataSource="actionForm.langPref" optionsDataSource="${languages}"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td class="celldata"><netui:span value="${bundle.default.favoriteCategory}:"/></td>
        <td class="celldata">
        <netui:select dataSource="actionForm.favCategory" optionsDataSource="${categoryNames}"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td colspan=2>
        <netui:checkBox dataSource="actionForm.myListOpt"/>
        <netui:span value="${bundle.default.enableMyList}"/>
        </td>
    </tr>
    <tr class="rowyellow">
        <td colspan=2>
        <netui:checkBox dataSource="actionForm.bannerOpt"/>
        <netui:span value="${bundle.default.enableMyBanner}"/>
        </td>
    </tr>
</table>

<netui-data:repeater dataSource="pageInput.categoryNames">
    <br/>${container.item}
</netui-data:repeater>
