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
        <title>Control Composition Test</title>
    </head>
    <netui:body>
        <h4>Control Composition Test</h4>
        <p style="color:green">This test verifies various aspects of control composition.</p>
        <br>
        <netui:anchor action="testInstantiation">Instantiation of a nested inner control</netui:anchor>
        <br>
        <netui:anchor action="testInstantiationP">Instantiation instantiation of a nested inner control (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testInstantiationWithProperty">Instantiation of inner control with property</netui:anchor>
        <br>
        <netui:anchor action="testInstantiationWithPropertyP">Programmatic instantiation of inner control with property (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetPropertyByContext">Get a control property value via the control context</netui:anchor>
        <br>
        <netui:anchor action="testGetPropertyByContextP">Get a control property value via the control context (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testGetPropertyByGetter">Get a control property value via a control bean getter method</netui:anchor>
        <br>
        <netui:anchor action="testGetPropertyByGetterP">Get a control property value via a control bean getter method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testSetPropertyBySetter">Set a control property via its control bean setter method</netui:anchor>
        <br>
        <netui:anchor action="testSetPropertyBySetterP">Set a control property via its control bean setter method (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testEventHandler">Test inner control event handler</netui:anchor>
        <br>
        <netui:anchor action="testEventHandlerP">Test inner control event handler (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testEventListener">Test inner control event listener</netui:anchor>
        <br>
        <netui:anchor action="testEventListenerP">Test inner control event listener (programmatic instantiation)</netui:anchor>
        <br>
        <netui:anchor action="testInnerClassListener">Test inner control event listener</netui:anchor>
        <br>
        <netui:anchor action="testInnerClassListenerP">Test inner control event listener (programmatic instantiation)</netui:anchor>
        <br>
    </netui:body>
</netui:html>
