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
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>

<%@ tag body-content="empty" %>
<%@ attribute name="contextPath" required="true" %>

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<table background="${contextPath}/images/bkg-topbar.gif" border="0" cellspacing="0" cellpadding="5" width="100%">
  <tr>
    <td align="left" width="60%">
      <%-- shop --%>
      <netui:imageAnchor action="rootSharedFlow.globalShop" border="0" src="${contextPath}/images/logo-topbar.gif" height="60" width="287"/>
    </td>
    <td valign="middle" align="right" nowrap="nowrap">
      <%-- view cart --%>
      <netui:form action="rootSharedFlow.search">
	      <netui:imageAnchor action="rootSharedFlow.globalViewCart" border="0" src="${contextPath}/images/cart.gif"/>
	      <netui:image border="0" src="${contextPath}/images/separator.gif"/>
	      <c:choose>
	        <c:when test="${sharedFlow.rootSharedFlow.account == null}">
	          <%-- sharedFlow.signon --%>
	          <netui:anchor value="${bundle.view.signinLabel}" styleClass="boldanchor" action="rootSharedFlow.signon" />
	        </c:when>
	        <c:otherwise>
	          <%-- sharedFlow.signoff --%>
	          <netui:anchor value="${bundle.view.signoffLabel}" styleClass="boldanchor" action="rootSharedFlow.signoff" />
	          <netui:image border="0" src="${contextPath}/images/separator.gif"/>
	          <netui:anchor value="${bundle.view.myAccountLabel}" styleClass="boldanchor" action="rootSharedFlow.globalEditAccount" />
	        </c:otherwise>
	      </c:choose>
	      <netui:image border="0" src="${contextPath}/images/separator.gif"/>
		  <%-- help --%>
		  <netui:imageAnchor action="rootSharedFlow.globalShowHelp" border="0" src="${contextPath}/images/help.gif"/>
		  <%-- search --%>
	      <netui:textBox dataSource="actionForm.keyword" size="14"/>
	      <netui:anchor value="${bundle.view.searchLabel}" styleClass="boldanchor" formSubmit="true" />
      </netui:form>
    <td>
  </tr>
</table>
