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

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Page Inputs"/>
  <netui-template:section name="main">
      
      <netui-data:declarePageInput name="pet" type="org.apache.beehive.samples.netui.beans.PetType"/>

      <br/>
      <table>
          <tr><td colspan="2"><b>Pet Details</b></td></tr>
          <tr><td>Identifier:</td><td>${pageInput.pet.petId}</td></tr>
          <tr><td>Name:</td><td>${pageInput.pet.name}</td></tr>
          <tr><td>Price:</td><td>${pageInput.pet.price}</td></tr>
      <tr><td>Description:</td><td>${pageInput.pet.description}</td></tr>
      </table>
      <br/>
      Click <netui:anchor action="missingPageInput" value="here"/> to run this page
      via an action that <i>does not</i> provide the required page input.
      <br/>
      <br/>
      Click <netui:anchor action="begin" value="here"/> to run this page via an action
      that <i>does</i> provide the required page input.
      <br/>
  </netui-template:section>
</netui-template:template>