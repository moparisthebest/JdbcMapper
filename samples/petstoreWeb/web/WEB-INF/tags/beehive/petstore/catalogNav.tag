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
<%@ tag body-content="empty" %>
<%@ attribute name="action" required="true" %>
<%@ attribute name="paramName" required="false" %>
<%@ attribute name="paramValue" required="false" %>
<%@ attribute name="labelValue" required="true" %>

<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="tableborder" cellpadding="3" cellspacing="0" bgcolor="#eeeeee" width="200">
	<tr><td>
	    <netui:anchor action="${action}">
		  <img src="${pageContext.request.contextPath}/images/arrow.gif" border=0 />  ${labelValue}
	      <c:if test="${paramName != null && paramValue != null}">
	        <netui:parameter name="${paramName}" value="${paramValue}"/>
	      </c:if>
	    </netui:anchor>
	</td></tr>
</table>
