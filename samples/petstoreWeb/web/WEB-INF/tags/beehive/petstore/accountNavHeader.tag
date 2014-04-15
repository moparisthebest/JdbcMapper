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
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ tag body-content="empty"%>

<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.account" name="account"/>

<table class="toolbar" cellpadding="1" cellspacing="0" width="100%" height="27">
	<tr><td><table cellpadding="1" cellspacing="0">
				<tr><td>
						<netui:anchor action="rootSharedFlow.globalEditAccount" value="${bundle.view.myAccountLabel}" styleClass="toolbar" />
					</td>
					<td class="separator">|</td>
					<td>
						<netui:anchor action="rootSharedFlow.globalViewAddresses" value="${bundle.account.listAddressesLabel}" styleClass="toolbar" />
					</td>
					<td class="separator">|</td>
					<td>
						<netui:anchor action="rootSharedFlow.globalViewOrders" value="${bundle.account.listOrdersLabel}" styleClass="toolbar" />
					</td>
					<td class="separator">|</td>
					<td>
						<netui:anchor action="rootSharedFlow.globalShop" value="${bundle.view.continueLabel}" styleClass="toolbar" />
					</td>
					<td class="separator">|</td>
				</tr>
			</table>
		</td>
	</tr>
</table>