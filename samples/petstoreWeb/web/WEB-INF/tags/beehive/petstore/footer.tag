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
<%@ attribute name="contextPath" required="true" %>

<c:if test="${sharedFlow.rootSharedFlow.account != null && sharedFlow.rootSharedFlow.account.bannerOpt == true}">
  <p>
  <table background="${pageContext.request.contextPath}/images/bkg-topbar.gif" border="0" cellspacing="0" cellpadding="5" width="100%">
    <tbody>
    <tr>
      <td width="100%">
          <center>
          <netui:image border="0" src="${pageContext.request.contextPath}/images/banner_${sharedFlow.rootSharedFlow.account.favCategory}.gif"/>
          </center>
      </td>
    </tr>
    </tbody>
  </table>
  </p>
</c:if>
