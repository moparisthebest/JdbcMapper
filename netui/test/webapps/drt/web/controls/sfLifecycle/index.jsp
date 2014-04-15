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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>Control Lifecycle Test 5</title>
    </head>
    <netui:body>
        <h4>Control Lifecycle Test 5</h4>
        <p style="color:green">This test verifies the life cycle of a shared flow containg a control.</p>
        <br>
        <netui:anchor action="sfFieldTest">access a shared flow field (and control)</netui:anchor>
        <br>
        <netui:anchor action="shared.sharedFlowAction">run an empty action</netui:anchor>
        <br>
        <netui:anchor action="runSharedFlow">execute a shared flow action</netui:anchor>
        <br>
        <netui:anchor action="removeSharedFlow">remove a shared flow</netui:anchor>
        <br>
        <netui:anchor action="navigateToCurrentPage">this page flow</netui:anchor>
        <br>
        <br>
        Main Flow results:
        <br>
        <netui:span value="${pageInput.controlResults}"/>
        <br>
        <br>
        Shared Flow results:
        <br>
        <netui:span value="${pageInput.sfControlResults}"/>
    </netui:body>
</netui:html>
