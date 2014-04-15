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

<netui-data:declarePageInput name="orders" type="org.apache.beehive.samples.petstore.model.Order[]" required="false"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">
    
	<netui-template:setAttribute name="title" value="${bundle.view.myOrders}" />
 
    <netui-template:section name="topMenu"> 
		<beehive-petstore:accountNavHeader />
	</netui-template:section>

    <netui-template:section name="body">
		<!-- Turn off the leftnav -->
		<style> #leftnav {display: none;} </style>
		
		<center>
	    <netui:span styleClass="boldlabel" value="${bundle.view.myOrders}"/>
	    <netui-data:repeater dataSource="pageInput.orders" defaultText="<br/><br/>No orders found<br/><br/>">
	        <netui-data:repeaterHeader>
	            <table class="tableborder" cellpadding="0" cellspacing="1">
	                <tr class="rowgrey" valign="top">
	                    <td>${bundle.view.orderId}</td>
	                    <td>${bundle.view.orderDate}</td>
	                    <td>${bundle.view.orderPrice}</td>
	                </tr>
	        </netui-data:repeaterHeader>
	        <netui-data:repeaterItem>
	            <tr valign="top" class="rowltgrey">
	                <td>
	                    <netui:anchor action="viewOrder" value="${container.item.orderId}">
		                    <netui:parameter name="orderId" value="${container.item.orderId}"/>
	                    </netui:anchor>
	                </td>
	                <td>
	                    <netui:span value="${container.item.orderDate}" defaultValue="&nbsp;"/>
	                </td>
	                <td>
	                    <netui:span value="${container.item.totalPrice}" defaultValue="&nbsp;">
	                        <netui:formatNumber pattern="${bundle.view.priceFormat}"/>
	                    </netui:span>
	                </td>
	            </tr>
	        </netui-data:repeaterItem>
	        <netui-data:repeaterFooter>
	            </table>
	        </netui-data:repeaterFooter>
	    </netui-data:repeater>
	    </center>
	    <br/>
    </netui-template:section>
</netui-template:template>
