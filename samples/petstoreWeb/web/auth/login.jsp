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

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.signinLabel}" />

	<!-- Turn off the leftnav -->
	<style>	#leftnav {display: none;} </style>

    <netui-template:section name="body">

	    <br/>
	    <center>
	    <netui:form action="securityCheck">
	        <table>
	            <tr>
	                <td colspan="2"><b>${bundle.view.enterUsernamePassword}</b><br/></td>
	            </tr>
	            <tr>
	                <td>${bundle.view.usernameLoginLabel}:</td>
	                <td>
	                <input type="text" name="j_username" value="beehive">
	                </td>
	            </tr>
	            <tr>
	                <td>${bundle.view.passwordLoginLabel}:</td>
	                <td>
			      <input type="password" name="j_password" value="beehive">
	                </td>
	            </tr>
	            <tr>
	                <td colspan="2" align="center">
	                <netui:button value="${bundle.view.buttonSubmit}" />
	                </td>
	            </tr>
	        </table>
	    </netui:form>
	    <netui:anchor value="${bundle.view.buttonRegisterNow}" action="rootSharedFlow.globalViewCreateAccount"/>
	    </center>

	    <%-- set focus on the username box--%>
	    <script language="javascript">
	    <!--
	    document.forms[1].j_username.focus();
	    -->
	    </script>

	    </netui-template:section>
</netui-template:template>
