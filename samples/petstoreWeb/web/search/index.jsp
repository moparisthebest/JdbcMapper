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

<netui-data:declarePageInput name="searchResults" type="org.apache.beehive.samples.petstore.model.Product[]" required="false"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.search" name="search"/>

<netui-template:template templatePage="/site/template.jsp">
    <netui-template:section name="leftnav">
        &nbsp;
    </netui-template:section>
    <netui-template:section name="body">
        <netui:span styleClass="boldlabel" value="${bundle.search.searchResultsLabel}"/>

        <netui-data:repeater dataSource="pageInput.searchResults" defaultText="<br/><br/>${bundle.search.noSearchResults}<br/><br/>">
            <netui-data:repeaterHeader>
                <table class="tableborder" cellpadding="0" cellspacing="1">
                <tr class="rowgrey">
                    <td>&nbsp;</td>
                    <td><b>${bundle.view.productIdLabel}</b></td>
                    <td><b>${bundle.view.nameLabel}</b></td>
                    <td><b>${bundle.view.descriptionLabel}</b></td>
                </tr>
            </netui-data:repeaterHeader>
            <netui-data:repeaterItem>
                <tr class="rowltgrey">
                    <td>
                        <netui:imageAnchor action="rootSharedFlow.globalViewProductById"border="0" src="${pageContext.request.contextPath}/images/${container.item.image}">
                            <netui:parameter name="productId" value="${container.item.productId}"/>
                        </netui:imageAnchor>
                    </td>
                    <td>
                        <b>
                            <netui:anchor action="rootSharedFlow.globalViewProductById">
                                <netui:parameter name="productId" value="${container.item.productId}"/>
                                <font color="black"><netui:span value="${container.item.productId}"/></font>
                            </netui:anchor>
                        </b>
                    </td>
                    <td>
                        <netui:span value="${container.item.name}"/>
                    </td>
                    <td>
                        <netui:span value="${container.item.description}"/>
                    </td>
                </tr>
            </netui-data:repeaterItem>
            <netui-data:repeaterFooter>
                </table>
            </netui-data:repeaterFooter>
        </netui-data:repeater>
    </netui-template:section>
</netui-template:template>
