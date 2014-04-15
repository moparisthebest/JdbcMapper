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
<%@ tag body-content="empty"%>

<%-- 
    todo: need to get the category list here and use a repeater
           to render each of the category links with a lookup
           to find the readable name for a category given an id
  --%>
<table class="toolbar" cellpadding="1" cellspacing="0" width="100%" height="27">
	<tr><td><table cellpadding="1" cellspacing="0">
				<tr><td><netui:anchor action="rootSharedFlow.globalViewCategory" value="Fish" styleClass="toolbar">
						    <netui:parameter name="catId" value="FISH"/>
						</netui:anchor>
					</td>
					<td class="separator">|</td>
					<td><netui:anchor action="rootSharedFlow.globalViewCategory" value="Dogs" styleClass="toolbar">
						    <netui:parameter name="catId" value="DOGS"/>
						</netui:anchor>
					</td>
					<td class="separator">|</td>
					<td><netui:anchor action="rootSharedFlow.globalViewCategory" value="Reptiles" styleClass="toolbar">
						    <netui:parameter name="catId" value="REPTILES"/>
						</netui:anchor>
					</td>
					<td class="separator">|</td>
					<td><netui:anchor action="rootSharedFlow.globalViewCategory" value="Cats" styleClass="toolbar">
						    <netui:parameter name="catId" value="CATS"/>
						</netui:anchor>
					</td>
					<td class="separator">|</td>
					<td><netui:anchor action="rootSharedFlow.globalViewCategory" value="Birds" styleClass="toolbar">
						    <netui:parameter name="catId" value="BIRDS"/>
						</netui:anchor>
					</td>
					<td class="separator">|</td>
				</tr>
			</table>
		</td>
	</tr>
</table>