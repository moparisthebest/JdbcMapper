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
<%@ taglib prefix="beehive-petstore" tagdir="/WEB-INF/tags/beehive/petstore"%>

<netui-data:declarePageInput name="items" type="org.apache.beehive.samples.petstore.model.Item[]"/>
<netui-data:declarePageInput name="product" type="org.apache.beehive.samples.petstore.model.Product"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${pageInput.product.name}" />

    <netui-template:section name="leftnav">
        <beehive-petstore:catalogNav action="viewCategory" 
                                     paramName="catId" 
                                     paramValue="${pageInput.product.category}"
                                     labelValue="${pageInput.product.category}"/>
    </netui-template:section>

    <netui-template:section name="body">
	    <netui:errors/>
        <netui:span styleClass="boldlabel" value="${pageInput.product.name}"/>
          <br/>
          <netui:span styleClass="meditaliclabel" value="(${pageInput.product.description})"/>
          <br/>
        <table class="tableborder" cellpadding="0" cellspacing="1">
          <tr class="rowgrey">
            <td><b>${bundle.view.itemIdLabel}</b></td>
            <td><b>${bundle.view.productIdLabel}</b></td>
            <td><b>${bundle.view.descriptionLabel}</b></td>
            <td><b>${bundle.view.listPriceLabel}</b></td>
            <td>&nbsp;</td>
          </tr>
          <netui-data:repeater dataSource="pageInput.items">
          <tr class="rowltgrey">
            <td>
              <netui:anchor action="viewItem">
                <netui:parameter name="itemId" value="${container.item.itemId}"/>
                <b><netui:span value="${container.item.itemId}"/></b>
              </netui:anchor>
            </td>
            <td><netui:span value="${container.item.productId}"/></td>
            <td>
              <netui:span value="${container.item.attr1}"/>
              <netui:span value="${pageInput.product.name}"/>
            </td>
            <td>
              <netui:span value="${container.item.listPrice}">
                <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
              </netui:span>
            </td>
            <td>
            	<netui:anchor action="addItemToCart">
		          <netui:parameter name="workingItemId" value="${container.item.itemId}"/>
		          ${bundle.view.buttonAddToCart} <img src="${pageContext.request.contextPath}/images/cart.gif" border="0" />
        		</netui:anchor>
            </td>
          </tr>
          </netui-data:repeater>
        </table>
        <br/>
    </netui-template:section>
</netui-template:template>
