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

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.welcomeTitle}" />
	
    <%-- do not show any top menu --%>
    <netui-template:section name="topMenu" />

    <netui-template:section name="body">

		<%-- Turn off the left nav --%>
		<style> #leftnav {display: none;} </style>

        <table cellSpacing="50" width="100%">
            <tr>
                <td align="center">
	                <netui:span styleClass="boldlabel" value="${bundle.view.welcome}"/>
    	            <br/>
        	        <netui:anchor tagId="enter" href="shop/Controller.jpf">${bundle.view.enterTheStore}</netui:anchor>
                    <br/>
                    <br/>
                    <br/>
                    <b>${bundle.view.adminTitle}</b><br>
                    <netui:anchor tagId="initDB" action="initDB">${bundle.view.adminInit}</netui:anchor><br/>
                    <netui:anchor action="stopDB">${bundle.view.adminStop}</netui:anchor><br/>
                    <netui:label value="${requestScope.message}" />
                </td>
            </tr>
        </table>

    </netui-template:section>
</netui-template:template>
