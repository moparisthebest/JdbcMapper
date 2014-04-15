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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="beehive-petstore" tagdir="/WEB-INF/tags/beehive/petstore" %>

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">
	<netui-template:setAttribute name="title" value="${bundle.view.newOrderLabel}" />

    <%-- do not show any top menu --%>
	<netui-template:section name="topMenu" />

    <netui-template:section name="body">

		<!-- Turn off the leftnav -->
		<style> #leftnav {display: none;} </style>

		<center>
		<netui:span styleClass="boldlabel" value="${bundle.view.confirmMessage}"/>
		<br/><br/>
		<beehive-petstore:orderSummary />
		
		<br/>
	    <netui:anchor action="viewCreateOrder" styleClass="navigation">
   			<img src="${pageContext.request.contextPath}/images/arrow_back.gif" border=0 /> ${bundle.view.buttonPrevious}
	   	</netui:anchor>
	   	&nbsp;&nbsp;&nbsp;	    
   		<netui:anchor action="confirm" styleClass="navigation">
   			${bundle.view.buttonContinue} <img src="${pageContext.request.contextPath}/images/arrow_forward.gif" border=0 />
	   	</netui:anchor>
		</center>
    </netui-template:section>
</netui-template:template>
