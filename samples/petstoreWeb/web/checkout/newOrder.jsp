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

<netui-data:declarePageInput name="creditCardTypes" type="java.util.List"/>
<netui-data:declarePageInput name="addresses" type="org.apache.beehive.samples.petstore.model.Addresses"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.checkoutStep1}" />

    <%-- do not show any top menu --%>
	<netui-template:section name="topMenu" />

    <netui-template:section name="body">

		<!-- Turn off the leftnav -->
		<style> #leftnav {display: none;} </style>

        <center>
	    <netui:form action="viewConfirm">
	    <table>
	    <tr><td>
		    <netui:span styleClass="boldlabel" value="${bundle.view.paymentDetailsLabel}"/>
		    <table class="tableborder" cellpadding="0" cellspacing="1">
	            <tr>
	                <td class="cellgrey">${bundle.view.cardTypeLabel}:</td>
	                <td class="cellltgrey">
		                <netui:select dataSource="actionForm.order.cardType" optionsDataSource="${pageInput.creditCardTypes}"/>
	                </td>
	            </tr>
	            <tr>
	                <td valign="top" class="cellgrey">${bundle.view.cardNumberLabel}:</td>
	                <td class="cellltgrey">
		                <netui:textBox dataSource="actionForm.order.creditCard"/>
		                <netui:error key="creditCard"/>
		                <br/>
		                <font color=red size=2>Note: be sure to use an invalid number.</font></td>
	            </tr>
	            <tr>
	                <td class="cellgrey">${bundle.view.expirationDateLabel}:</td>
	                <td class="cellltgrey">
		                <netui:textBox dataSource="actionForm.order.exprDate"/>
		                <netui:error key="expirationDate"/>
	                </td>
	            </tr>
	        </table>

		    <br/>
		    <netui:span styleClass="boldlabel" value="${bundle.view.billingAddressLabel}"/>
	        <table class="tableborder" cellpadding="3" cellspacing="0">
		        <tr>
	    			<td colspan=2><netui:radioButtonGroup dataSource="actionForm.order.billingAddress">
	    				<table cellspacing="10" cellpadding="10"><tr>
				        <netui-data:repeater dataSource="pageInput.addresses">
				        	<td class="cellgrey" style="text-align: left;" valign="top">
				        		<netui:radioButtonOption value="${container.item.addressId}"> <b>${container.item.name}</b></netui:radioButtonOption><br/>
			                    <netui:span value="${container.item.addr1}" />
			                    <c:if test="${container.item.addr2 != ''}">			                    
				                    <br/>
			                    </c:if>
			                    <netui:span value="${container.item.addr2}" /><br/>
			                    <netui:span value="${container.item.city}" />,
			                    <netui:span value="${container.item.state}" />
			                    <netui:span value="${container.item.zip}" /><br/>
			                    <netui:span value="${container.item.country}" /><br/>
			                    <netui:span value="${container.item.phone}" />
			                </td>
			            </netui-data:repeater>
			            </tr></table>
			            </netui:radioButtonGroup>
		                <netui:error key="billingAddress"/>
			        </td>
			    </tr>
	        </table>

		    <br/>
		    <netui:span styleClass="boldlabel" value="${bundle.view.shippingAddressLabel}"/>
	        <table class="tableborder" cellpadding="3" cellspacing="0">
		        <tr>
	    			<td colspan=2><netui:radioButtonGroup dataSource="actionForm.order.shippingAddress">
	    				<table cellspacing="10" cellpadding="10"><tr>
				        <netui-data:repeater dataSource="pageInput.addresses">
				        	<td class="cellgrey" style="text-align: left;" valign="top">
				        		<netui:radioButtonOption value="${container.item.addressId}"> <b>${container.item.name}</b></netui:radioButtonOption><br/>
			                    <netui:span value="${container.item.addr1}" />
			                    <c:if test="${container.item.addr2 != ''}">			                    
			                    <br/>
			                    </c:if>
			                    <netui:span value="${container.item.addr2}" /><br/>
			                    <netui:span value="${container.item.city}" />,
			                    <netui:span value="${container.item.state}" />
			                    <netui:span value="${container.item.zip}" /><br/>
			                    <netui:span value="${container.item.country}" /><br/>
			                    <netui:span value="${container.item.phone}" />
			                </td>
			            </netui-data:repeater>
			            </tr></table>
			            </netui:radioButtonGroup>
		                <netui:error key="shippingAddress"/>
			        </td>
			    </tr>
	        </table>
	        <br/>
	        <center>
	        <netui:anchor styleClass="navigation" action="rootSharedFlow.globalViewCart">
	   			<img src="${pageContext.request.contextPath}/images/arrow_back.gif" border=0 /> ${bundle.view.buttonUpdateCart}
		   	</netui:anchor>
		   	&nbsp;&nbsp;&nbsp;	    
	   		<netui:anchor styleClass="navigation" formSubmit="true">
	   			${bundle.view.buttonContinue} <img src="${pageContext.request.contextPath}/images/arrow_forward.gif" border=0 />
		   	</netui:anchor>
		   	</center>
	    </td></tr>
		</table>
        </center>
        </netui:form>
    </netui-template:section>
</netui-template:template>
