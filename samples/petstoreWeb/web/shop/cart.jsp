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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="beehive-petstore" tagdir="/WEB-INF/tags/beehive/petstore" %>

<netui-data:declarePageInput name="myList" type="org.apache.beehive.samples.petstore.model.Product[]" required="false"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.shoppingCartLabel}" />

    <netui-template:section name="leftnav">
        <beehive-petstore:catalogNav action="begin" labelValue="${bundle.view.mainMenuLabel}"/>
        <c:if test="${sharedFlow.rootSharedFlow.account != null}">
            <c:if test="${sharedFlow.rootSharedFlow.account.myListOpt == true}">
                <beehive-petstore:productFavorites/>
            </c:if>
        </c:if>
    </netui-template:section>

    <netui-template:section name="body">
	    <netui:errors/>
	    <netui:span styleClass="boldlabel" value="${bundle.view.shoppingCartLabel}"/>
	    <netui:form action="updateCartQuantities" tagId="cart">
	        <table class="tableborder" cellpadding="3" cellspacing="1">
	            <tr class="rowgrey">
	                <td><b>${bundle.view.itemIdLabel}</b></td>
	                <td><b>${bundle.view.productIdLabel}</b></td>
	                <td><b>${bundle.view.descriptionLabel}</b></td>
	                <td><b>${bundle.view.inStockLabel} </b></td>
	                <td><b>${bundle.view.quantityLabel}</b></td>
	                <td><b>${bundle.view.listPriceLabel}</b></td>
	                <td><b>${bundle.view.totalCostLabel}</b></td>
	                <td>&nbsp;</td>
	            </tr>
	            <c:choose>
	            <c:when test="${actionForm.cart.lineItems != null && fn:length(actionForm.cart.lineItems) > 0}">
	            <netui-data:repeater dataSource="actionForm.cart.lineItems">
	                <tr class="rowltgrey">
	                    <td>
	                    <b>
	                    <netui:anchor action="viewItem">
	                        <netui:parameter name="itemId" value="${container.item.item.itemId}"/>
	                        <netui:span value="${container.item.item.itemId}"/>
	                    </netui:anchor>
	                    </b>
	                    </td>
	                    <td><netui:span value="${container.item.item.productId}"/></td>
	                    <td>
	                    <netui:span value="${container.item.item.attr1}"/>
	                    <netui:span value="${container.item.item.productName}"/>
	                    </td>
	                    <td>
	                    <netui:span value="${container.item.inStock}"/>
	                    </td>
	                    <td align="center">
	                    <netui:textBox size="3" dataSource="container.item.quantity"/>
	                    <br/>
	                    <netui:error key="invalidQuantity${container.index}"/>
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
	                    <td>
	                    <netui:anchor action="removeItemFromCart" value="${bundle.view.buttonRemove}">
	                    	<netui:parameter name="workingItemId" value="${container.item.item.itemId}" />
	                    </netui:anchor>
	                    </td>
	                </tr>
	            </netui-data:repeater>
	            <tr>
	                <td colspan="8" align="right">
	                <b>${bundle.view.subTotalLabel}:
	                <netui:span value="${actionForm.cart.subTotal}">
	                <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	                </netui:span> </b><br />
				    <netui:button value="${bundle.view.buttonUpdateCart}" />
	                </td>
	            </tr>
	            </c:when> 
	            <c:otherwise>
	            <tr>
	                <td colspan="8"><b>${bundle.view.emptyCartMessage}.</b></td>
	            </tr>
	            </c:otherwise> 
	            </c:choose>
	        </table>
	        <c:if test="${actionForm.cart.lineItems != null && fn:length(actionForm.cart.lineItems) > 0}">
		        <br/>
		        <netui:anchor action="checkout" styleClass="navigation">
		        	${bundle.view.buttonProceedToCheckout} <img src="${pageContext.request.contextPath}/images/arrow_forward.gif" border=0 />
		        </netui:anchor>
	        </c:if>
	    </netui:form>
    </netui-template:section>
</netui-template:template>
