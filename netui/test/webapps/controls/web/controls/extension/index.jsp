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
        <title>Control Extension Test</title>
    </head>
    <netui:body>
        <h4>Control Extension Test</h4>
        <p style="color:green">This test verifies various aspects of control inheritance and extension.</p>
        <br>
        <netui:anchor action="testInheritedMethod">Test an inherited method.</netui:anchor>
        <br>
        <netui:anchor action="testInheritedMethodP">Test an inherited method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testExtendedMethod">Test an overridden method</netui:anchor>
        <br>
        <netui:anchor action="testExtendedMethodP">Test an overridden method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetInheritedPropertyByContext">Get an inherited control property value via the control context</netui:anchor>
        <br>
        <netui:anchor action="testGetInheritedPropertyByContextP">Get an inherited control property value via the control context (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetInheritedPropertyByGetter">Get an inherited control property value via a control bean getter method</netui:anchor>
        <br>
        <netui:anchor action="testGetInheritedPropertyByGetterP">Get an inherited control property value via a control bean getter method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testSetInheritedPropertyBySetter">Set an inherited control property via its control bean setter method</netui:anchor>
        <br>
        <netui:anchor action="testSetInheritedPropertyBySetterP">Set an inherited control property via its control bean setter method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetExtendedPropertyByContext">Get an extended control property value via the control context</netui:anchor>
        <br>
        <netui:anchor action="testGetExtendedPropertyByContextP">Get an extended control property value via the control context (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetExtendedPropertyByGetter">Get an extended control property value via getter method</netui:anchor>
        <br>
        <netui:anchor action="testGetExtendedPropertyByGetterP">Get an extended control property value via a getter method(programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testSetExtendedPropertyBySetter">Set an extended control property via its control bean setter method</netui:anchor>
        <br>
        <netui:anchor action="testSetExtendedPropertyBySetterP">Set an extended control property via its control bean setter method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetReconfiguredPropertyByContext">Get a reconfigured control property value via the control context</netui:anchor>
        <br>
        <netui:anchor action="testGetReconfiguredPropertyByContextP">Get a reconfigured control property value via the control context (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testSetReconfiguredPropertyBySetter">Set a reconfigured control property via its control bean setter method</netui:anchor>
        <br>
        <netui:anchor action="testSetReconfiguredPropertyBySetterP">Set a reconfigured control property via its control bean setter method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testInvokeExtendedEvent">Test the invocation of an extended event set</netui:anchor>
        <br>
        <netui:anchor action="testInvokeInheritedEvent">Test the invocation of an inherited event set</netui:anchor>
        <br>
    </netui:body>
</netui:html>
