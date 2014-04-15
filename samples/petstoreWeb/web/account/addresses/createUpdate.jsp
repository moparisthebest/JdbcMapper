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

    <netui-template:section name="body">
    <netui:errors/>
	<style>
		#leftnav {display: none;}
	</style>
    <netui:form action="updateAddress">
	    <netui:hidden dataSource="actionForm.userId" />
	    <netui:hidden dataSource="actionForm.addressId" />
    <center>
	        <table cellpadding="0" cellspacing="0">
            <tr>
                <td>
		        <c:choose>
			        <c:when test="${actionForm.addressId == -1}">
		                <netui:span styleClass="boldlabel" value="${bundle.view.newAddress}"/>
			        </c:when>
			        <c:otherwise>
		                <netui:span styleClass="boldlabel" value="${bundle.view.editAddress}"/>
			        </c:otherwise>
			    </c:choose>
		        <table class="tableborder" cellpadding="0" cellspacing="1" width="400">
                    <tr>
                        <td class="cellgrey">${bundle.view.nameLabel}:</td>
                        <td class="cellltgrey">
                        	<netui:textBox dataSource="actionForm.name"/>
	                        <br/>
	                        <netui:error key="name"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.address1Label}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.addr1" />
	                        <br/>
	                        <netui:error key="address1"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.address2Label}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.addr2" />
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.cityLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.city" />
	                        <br/>
	                        <netui:error key="city"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.stateLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.state" />
	                        <br/>
	                        <netui:error key="state"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.zipLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.zip" />
	                        <br/>
	                        <netui:error key="zip"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.countryLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.country" />
	                        <br/>
	                        <netui:error key="country"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.view.phoneLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.phone" />
	                        <br/>
	                        <netui:error key="phone"/>
                        </td>
                    </tr>
                </td>
                </tr>
                </table>
		        <br/>
		        <CENTER>
		        <c:choose>
			        <c:when test="${actionForm.addressId == -1}">
						<netui:button value="${bundle.view.buttonInsert}" />
			        </c:when>
			        <c:otherwise>
						<netui:button value="${bundle.view.buttonUpdate}" />
			        </c:otherwise>
			    </c:choose>
		        <br/><br/>
				<netui:anchor action="begin">
					<img src="${pageContext.request.contextPath}/images/arrow.gif" border=0 /> ${bundle.view.buttonCancel}
				</netui:anchor>
		        </CENTER>
				</td>
			</tr>
		</table>	
        </center>
    </netui:form>
    
    </netui-template:section>
</netui-template:template>
