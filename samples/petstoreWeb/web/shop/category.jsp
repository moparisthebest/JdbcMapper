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

<netui-data:declarePageInput name="products" type="org.apache.beehive.samples.petstore.model.Product[]" required="false"/>
<netui-data:declarePageInput name="category" type="org.apache.beehive.samples.petstore.model.Category"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${pageInput.category.name}" />

    <netui-template:section name="leftnav">
        <beehive-petstore:catalogNav action="begin" labelValue="${bundle.view.mainMenuLabel}"/>
    </netui-template:section>

    <netui-template:section name="body">
	    <netui:errors/>
	    <netui:span styleClass="boldlabel" value="${pageInput.category.name}"/>
	    <br/>
	    <netui:span styleClass="meditaliclabel" value="(${pageInput.category.description})"/>
	    <br/>
	    <table class="tableborder" cellpadding="0" cellspacing="1">
	        <tr class="rowgrey">
	            <td><b>${bundle.view.productIdLabel}</b></td>
	            <td><b>${bundle.view.nameLabel}</b></td>
	            <td><b>${bundle.view.descriptionLabel}</b></td>
	        </tr>
	        <netui-data:repeater dataSource="pageInput.products">
	            <tr class="rowltgrey">
	                <td><netui:anchor action="viewProduct" value="${container.item.productId}">
	                    	<netui:parameter name="productId" value="${container.item.productId}"/>
	                	</netui:anchor>
	                </td>
	                <td><netui:span value="${container.item.name}"/></td>
	                <td><netui:span value="${container.item.description}"/></td>
	            </tr>
	        </netui-data:repeater>
	    </table>
	    <br/>
    </netui-template:section>
</netui-template:template>
