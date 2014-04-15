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
<%@ taglib prefix="sampledata" tagdir="/WEB-INF/tags/data" %>

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Cell Repeater"/>
  <netui-template:section name="main">
    <sampledata:petdata/>
    <br/>
    <netui-data:cellRepeater dataSource="pageScope.pets" columns="2">
        ID: <netui:span value="${container.item.petId}"/><br/>
        Name: <netui:span value="${container.item.name}"/><br/>
        Description: <netui:span value="${container.item.description}"/><br/>
        Price: <netui:span value="${container.item.price}"/><br/>
    </netui-data:cellRepeater>
  </netui-template:section>
</netui-template:template>
