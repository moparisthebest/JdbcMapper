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
<%@ taglib prefix="beehive-petstore" tagdir="/WEB-INF/tags/beehive/petstore" %>

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.checkoutSummaryLabel}" />

    <%-- do not show any top menu --%>
	<netui-template:section name="topMenu" />

    <netui-template:section name="body">

		<!-- Turn off the leftnav -->
		<style> #leftnav {display: none;} </style>
	
	    <center>
	    <netui:span styleClass="boldlabel" value="${bundle.view.checkoutSummaryLabel}"/>
	    <br/>
	    <table class="tableborder" cellpadding="3" cellspacing="1">
	        <tr class="rowgrey">
	            <td><b>${bundle.view.itemIdLabel}</b></td>
	            <td><b>${bundle.view.productIdLabel}</b></td>
	            <td><b>${bundle.view.descriptionLabel}</b></td>
	            <td><b>${bundle.view.inStockLabel}</b></td>
	            <td><b>${bundle.view.quantityLabel}</b></td>
	            <td><b>${bundle.view.listPriceLabel}</b></td>
	            <td><b>${bundle.view.totalCostLabel}</b></td>
	        </tr>
	        <netui-data:repeater dataSource="sharedFlow.rootSharedFlow.cart.lineItems">
	            <tr class="rowltgrey">
	                <td>
	                <b>
	                <netui:anchor href="viewItem.do">
	                    <netui:parameter name="itemId" value="${container.item.item.itemId}"/>
	                    <netui:span value="${container.item.item.itemId}"/>
	                </netui:anchor>
	                </b>
	                </td>
	                <td>
	                <netui:span value="${container.item.item.productId}"/>
	                </td>
	                <td>
	                <netui:span value="${container.item.item.attr1}"/>
	                <netui:span value="${container.item.item.productName}"/>
	                </td>
	                <td align="center">
	                <netui:span value="${container.item.inStock}"/>
	                </td>
	                <td align="center">
	                <netui:span value="${container.item.quantity}"/>
	                </td>
	                <td align="right">
	                <netui:span value="${container.item.item.listPrice}">
	                <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	                </netui:span>
	                </td>
	                <td align="right">
	                <netui:span value="${container.item.total}">
	                <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	                </netui:span>
	                </td>
	            </tr>
	        </netui-data:repeater>
	        <tr class="rowyellow">
	            <td colspan="7" align="right">
	            <b>Sub Total: <netui:span value="${sharedFlow.rootSharedFlow.cart.subTotal}">
	            <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	            </netui:span> </b> <br/>
	            </td>
	        </tr>
	    </table>
	    <br/>
	    <netui:anchor styleClass="navigation" action="rootSharedFlow.globalViewCart">
	   		<img src="${pageContext.request.contextPath}/images/arrow_back.gif" border=0 /> ${bundle.view.buttonUpdateCart}
	   	</netui:anchor>
	   	&nbsp;&nbsp;&nbsp;	    
	   	<netui:anchor styleClass="navigation" action="createNewOrder">
	   		${bundle.view.buttonContinue} <img src="${pageContext.request.contextPath}/images/arrow_forward.gif" border=0 />
	   	</netui:anchor>
	    </center> 
	    <br/>
	</netui-template:section>
</netui-template:template>
