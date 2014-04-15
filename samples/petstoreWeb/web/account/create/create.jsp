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

<netui-data:declarePageInput name="categoryNames" type="java.lang.String[]"/>
<netui-data:declarePageInput name="languages" type="java.lang.String[]"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">
  
	<netui-template:setAttribute name="title" value="${bundle.view.createAccountLabel}" />

    <netui-template:section name="body">

	<!-- Turn off the leftnav -->
	<style> #leftnav {display: none;} </style>

    <netui:errors/>
    <netui:form action="createAccount">
    <center>
        <table cellpadding=5 cellspacing=0 width="460">
            <tr>
                <td>
                <netui:span styleClass="boldlabel" value="${bundle.default.userInformation}"/>
                <table class="tableborder" width="100%" cellpadding="0" cellspacing="1">
                    <tr>
                        <td class="cellgrey">${bundle.default.userIdLabel}:</td>
                        <td class="cellltgrey">
	                        <c:if test="${pageFlow.usernameTaken == true}">
		                        <netui:span styleClass="error" value="${bundle.default.usernameTaken}"/>
		                        <br/>
	                        </c:if>
	                        <netui:textBox dataSource="actionForm.userId"/>
	                        <br/>
	                        <netui:error key="userId"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.default.passwordLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox password="true" dataSource="actionForm.password"/>
	                        <br/>
	                        <netui:error key="password"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="cellgrey">${bundle.default.repeatPasswordLabel}:</td>
                        <td class="cellltgrey">
	                        <netui:textBox dataSource="actionForm.repeatedPassword" password="true"/>
	                        <br/>
	                        <netui:error key="password"/>
                        </td>
                    </tr>
                </table>
                <br/>
                <netui:span styleClass="boldlabel" value="${bundle.default.accountInformation}"/>
                <table class="tableborder" width="100%" cellpadding="0" cellspacing="1">
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.firstName}:"/></td>
				        <td class="cellltgrey">
					        <netui:textBox dataSource="actionForm.firstName"/>
					        <br/>
					        <netui:error key="firstName"/>
				        </td>
				    </tr>
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.lastName}:"/></td>
				        <td class="cellltgrey">
					        <netui:textBox dataSource="actionForm.lastName"/>
					        <br/>
					        <netui:error key="lastName"/>
				        </td>
				    </tr>
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.email}:"/></td>
				        <td class="cellltgrey">
					        <netui:textBox dataSource="actionForm.email" size="35"/>
					        <br/>
					        <netui:error key="email"/>
				        </td>
				    </tr>
				</table>
				
				<br/>
				<netui:span styleClass="boldlabel" value="${bundle.default.profileInformation}"/>
                <table class="tableborder" width="100%" cellpadding="0" cellspacing="1">
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.languagePreference}:"/></td>
				        <td class="cellltgrey">
					        <netui:select dataSource="actionForm.langPref" optionsDataSource="${pageInput.languages}"/>
				        </td>
				    </tr>
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.favoriteCategory}:"/></td>
				        <td class="cellltgrey">
					        <netui:select dataSource="actionForm.favCategory" optionsDataSource="${pageInput.categoryNames}"/>
				        </td>
				    </tr>
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.enableMyList}"/></td>
				        <td class="cellltgrey">
					        <netui:checkBox dataSource="actionForm.myListOpt"/>
				        </td>
				    </tr>
				    <tr>
				        <td class="cellgrey"><netui:span value="${bundle.default.enableMyBanner}"/></td>
				        <td class="cellltgrey">
					        <netui:checkBox dataSource="actionForm.bannerOpt"/>
				        </td>
				    </tr>
				</table>
				
                </td>
                </tr>
                </table>
        <br/>
        <netui:button value="${bundle.view.buttonSubmit}" />
        </center>
    </netui:form>
    </netui-template:section>
</netui-template:template>
