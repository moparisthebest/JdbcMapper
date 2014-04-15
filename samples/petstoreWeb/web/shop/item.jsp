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

<netui-data:declarePageInput name="item" type="org.apache.beehive.samples.petstore.model.Item"/>
<netui-data:declarePageInput name="product" type="org.apache.beehive.samples.petstore.model.Product"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${pageInput.item.attr1} ${pageInput.product.name}" />

    <netui-template:section name="leftnav">
        <beehive-petstore:catalogNav action="viewProduct"
                                paramName="productId" paramValue="${pageInput.product.productId}"
                                labelValue="${pageInput.product.name}"/>
    </netui-template:section>

    <netui-template:section name="body">
    	<netui:errors/>
        <netui:span styleClass="boldlabel" value="${pageInput.item.attr1} ${pageInput.product.name}"/>
        <br/>
        <netui:span styleClass="meditaliclabel" value="${pageInput.product.description}"/>
        <br/>
    
      <table class="tableborder" cellpadding="0" cellspacing="1">
      <tr class="rowgrey">
        <td colspan=2>
          <b><netui:span value="${pageInput.item.itemId}"/></b>
        </td>
      </tr>
      <tr>
        <td class="cellltgrey">
          <netui:image src="${pageContext.request.contextPath}/images/${pageInput.product.image}"/>
        </td>
        <td class="cellltgrey">
          <c:choose>
          <c:when test="${pageInput.item.qty <= 0}">
            <netui:span styleClass="error" value="${bundle.view.backOrderedMessage}."/>
          </c:when>
          <c:otherwise>
            <netui:span value="${pageInput.item.qty} ${bundle.view.inStockMessage}"/>
          </c:otherwise>
          </c:choose><br/>
          <netui:span value="${pageInput.item.listPrice}">
            <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
          </netui:span>
          <br/>
      	  <netui:anchor action="addItemToCart">
            <netui:parameter name="workingItemId" value="${pageInput.item.itemId}"/>
            ${bundle.view.buttonAddToCart} <img src="${pageContext.request.contextPath}/images/cart.gif" border="0" />
	  	  </netui:anchor>
        </td>
      </tr>
      </table>
    </netui-template:section>
</netui-template:template>
