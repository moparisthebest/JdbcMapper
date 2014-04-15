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

<netui-data:declarePageInput name="addresses" type="org.apache.beehive.samples.petstore.model.address[]" required="true"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.addresses}" />

    <netui-template:section name="topMenu"> 
		<beehive-petstore:accountNavHeader />
	</netui-template:section>

    <netui-template:section name="body">
	<!-- Turn off the leftnav -->
	<style> #leftnav {display: none;} </style>
	
    <center> 
        <table cellpadding="3" cellspacing="0">
        <tr>
            <td>
			    <netui:span styleClass="boldlabel" value="${bundle.view.addresses}"/>
			    <netui-data:repeater dataSource="pageInput.addresses" defaultText="<br/><br/>No addresses found<br/><br/>">
			        <netui-data:repeaterItem>
			        <table class="tableborder" cellpadding="3" cellspacing="0" width="100%" id="address_${container.item.name}">
			            <tr>
			                <td class="cellltgrey">
			                    <netui:span value="${container.item.name}" /><br/>
			                    <netui:span value="${container.item.addr1}" /><br/>
			                    <netui:span value="${container.item.addr2}" />
			                    <c:if test="${container.item.addr2 != ''}">			                    
			                    <br/>
			                    </c:if>
			                    <netui:span value="${container.item.city}" />,
			                    <netui:span value="${container.item.state}" />
			                    <netui:span value="${container.item.zip}" /><br/>
			                    <netui:span value="${container.item.country}" /><br/>
			                    <netui:span value="${container.item.phone}" />
			                    <hr/>
			                    <center>
								<netui:anchor action="createUpdateAddress" value="${bundle.view.buttonEdit}">
									<netui:parameter name="addressId" value="${container.item.addressId}"/>
								</netui:anchor>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<netui:anchor action="deleteAddress" value="${bundle.view.buttonRemove}" >
									<netui:parameter name="addressId" value="${container.item.addressId}"/>
								</netui:anchor>
					            </center>
			                </td>
			            </tr>
			        </table>
			        </netui-data:repeaterItem>
			    </netui-data:repeater>
			    <br/>
			    <center>
		        <c:if test="${sharedFlow.rootSharedFlow.account.status == 'checking_out'}">
				    <netui:anchor action="rootSharedFlow.globalCheckOut.do">
				    	<img src="${pageContext.request.contextPath}/images/arrow_forward.gif" border=0 /> ${bundle.view.checkoutReturn}
				    </netui:anchor>&nbsp;&nbsp;&nbsp;
				</c:if>
				<netui:anchor styleClass="navigation" action="createUpdateAddress">
					${bundle.view.buttonAddAddress} <img src="${pageContext.request.contextPath}/images/arrow_forward.gif" border=0 />
				</netui:anchor>
			    </center>
			</td>
		</tr>
	</table>
    </center>
    </netui-template:section>
</netui-template:template>
