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

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<table cellpadding="3" cellspacing="1" id="orderTable">
  <c:if test="${pageInput.order.orderId != -1}">
	  <tr>
	    <td align="center" colspan="3">
	      <netui:span styleClass="boldlabel" value="${bundle.view.orderNumberLabel} ${pageInput.order.orderId}"/>
	      <br/>
	      <netui:span styleClass="boldlabel" value="${pageInput.order.orderDate}">
	    	  <netui:formatDate pattern="${bundle.view.orderDateFormat}"/>
	      </netui:span>
	    </td>
	  </tr>
  </c:if>
  <tr>
    <td colspan="3">
      <netui:span styleClass="boldlabel" value="${bundle.view.paymentDetailsLabel}"/>
    </td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.cardTypeLabel}:</td>
    <td class="cellltgrey" colspan="2">
      <netui:span value="${pageInput.order.cardType}"/>
    </td>
  </tr>
  <tr>
      <td class="cellgrey">${bundle.view.cardNumberLabel}:</td>
      <td class="cellltgrey" colspan="2"><netui:span value="${pageInput.order.creditCard}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.expirationDateLabel}:</td>
    <td class="cellltgrey" colspan="2"><netui:span value="${pageInput.order.exprDate}"/></td>
  </tr>
  <tr>
    <td>
      <br/>
      <netui:span styleClass="boldlabel" value="${bundle.view.addresses}"/>
    </td>
    <td>
      <br/>
      <netui:span styleClass="boldlabel" value="${bundle.view.billingLabel}"/>
    </td>
    <td>
      <br/>
      <netui:span styleClass="boldlabel" value="${bundle.view.shippingLabel}"/>
    </td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.nameLabel}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.name}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.name}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.address1Label}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.addr1}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.addr1}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.address2Label}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.addr2}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.addr2}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.cityLabel}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.city}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.city}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.stateLabel}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.state}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.state}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.zipLabel}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.zip}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.zip}"/></td>
  </tr>
  <tr>
    <td class="cellgrey">${bundle.view.countryLabel}:</td>
    <td class="cellltgrey"><netui:span value="${pageInput.billingAddress.country}"/></td>
    <td class="cellltgrey"><netui:span value="${pageInput.shippingAddress.country}"/></td>
  </tr>
  <tr>
  <tr>
    <td colspan="3">
      <br/>
      <netui:span styleClass="boldlabel" value="${bundle.view.orderDetails}"/>
    
	  <table class="tableborder" width="100%" cellpadding="3" cellspacing="1">
	    <tr class="rowgrey">
	      <td><b>${bundle.view.itemIdLabel}</b></td>
	      <td><b>${bundle.view.descriptionLabel}</b></td>
	      <td><b>${bundle.view.quantityLabel}</b></td>
	      <td><b>${bundle.view.unitPriceLabel}</b></td>
	      <td><b>${bundle.view.totalCostLabel}</b></td>
	    </tr>
	    <netui-data:repeater dataSource="pageInput.lineItems">
	      <tr>
	        <td class="cellltgrey"><b>
	        <c:choose>
	          <c:when test="${pageScope.checkout == false}">
	            <netui:anchor action="viewItem.do">
	              <netui:parameter name="itemId" value="${container.item.item.itemId}"/>
	              <netui:span value="${container.item.item.itemId}"/>
	            </netui:anchor>
	          </c:when>
	          <c:otherwise>
	            <netui:span value="${container.item.item.itemId}"/>
	          </c:otherwise>
	        </c:choose>
	        </b>
	        </td>
	        <td class="cellltgrey">
	          <netui:span value="${container.item.item.attr1}"/>
	          <netui:span value="${container.item.item.productName}"/>
	        </td>
	        <td class="cellltgrey" align="center">
	          <netui:span value="${container.item.quantity}"/>
	        </td>
	        <td align="right" class="cellltgrey">
	          <netui:span value="${container.item.item.listPrice}">
	            <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	          </netui:span>
	        </td>
	        <td align="right" class="cellltgrey">
	          <netui:span value="${container.item.total}">
	            <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	          </netui:span>
	        </td>
	      </tr>
	    </netui-data:repeater>
	    <tr>
	      <td colspan="6" align="right">
	        <b>${bundle.view.totalCostLabel}:
	        <netui:span value="${pageInput.order.totalPrice}">
	          <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	        </netui:span>
	        </b><br/>
	      </td>
	    </tr>
	  </table>
  </td>
  </tr>
</table>
