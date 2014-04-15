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
<netui:html>
  <head>
    <title>Test for Jira-1120 Fix</title>
    <netui:base/>
  </head>
  <netui:body>

    <p>
      Click the save button, then click the sort column link.
      The sort column link should cause the column to be re-sorted.
    </p>
    <netui:form action="submit">
      <netui-data:dataGrid dataSource="pageFlow.pets" name="petGrid">
        <netui-data:header>
          <netui-data:headerCell sortAction="sortingAction"
                                 sortExpression="petid"
                                 headerText="Pet ID">
              Pet ID
          </netui-data:headerCell>
          <netui-data:headerCell headerText="Name"/>
          <netui-data:headerCell headerText="Description"/>
          <netui-data:headerCell headerText="Price"/>
        </netui-data:header>
        <netui-data:rows>
          <netui-data:spanCell value="${container.item.petId}"/>
          <netui-data:spanCell value="${container.item.name}"/>
          <netui-data:spanCell value="${container.item.description}"/>
          <netui-data:spanCell value="${container.item.price}"/>
        </netui-data:rows>
      </netui-data:dataGrid>
      <netui:button action="save" value="save"/>
    </netui:form>
  </netui:body>
</netui:html>
