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
<%@ tag body-content="empty" %>

<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>
<table class="tableborder" cellpadding="0" cellspacing="1" width="200">
    <tr class="rowgrey">
        <td>
        <netui:span styleClass="boldlabel" value="${bundle.view.petFavoritesLabel}"/>
        <br/>
        <i>${bundle.view.shopForFavoritesLabel}.</i>
        </td>
    </tr>
    <tr>
        <td  class="cellltgrey">
        <netui-data:repeater dataSource="sharedFlow.rootSharedFlow.myList">
            <netui:anchor action="viewProduct">
                <netui:parameter name="productId" value="${container.item.productId}"/>
                <netui:span value="${container.item.name}"/>
            </netui:anchor>
            <br/>
            <netui:span value="(${container.item.productId})"/>
            <br/>
        </netui-data:repeater>
        </td>
    </tr>
</table>



