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
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>

<netui-template:template templatePage="/resources/template/template.jsp">
    <netui-template:setAttribute name="sampleTitle" value="Client-side Validation"/>
    <netui-template:section name="main">
        <p>
            This sample demonstrates enabling client-side validation for declarative validation
            annotations.  NetUI does not provide its own client-side validation framework, but
            the Struts JavaScriptValidatorTag can be used to enable client-side validation.
        </p>
        <p>
            In general, to use client-side validation with NetUI, you must do the following...
        </p>
        <p>
            <b>...in your page flow controller class:</b>
        </p>
            <ul>
                <li>
                    Add a
                    <code><a href="http://beehive.apache.org/docs/1.0/apidocs/classref_netui/org/apache/beehive/netui/pageflow/annotations/Jpf.MessageBundle.html">@Jpf.MessageBundle</a></code>
                    annotation to the page flow controller, even if your validation annotations
                    use hardcoded messages (without message keys).
                </li>
                <li>
                    Provide a message for each validation annotation, using either its
                    <code><a href="http://beehive.apache.org/docs/1.0/apidocs/classref_netui/org/apache/beehive/netui/pageflow/annotations/Jpf.ValidateRequired.html#messageKey()">messageKey</a></code>
                    or its
                    <code><a href="http://beehive.apache.org/docs/1.0/apidocs/classref_netui/org/apache/beehive/netui/pageflow/annotations/Jpf.ValidateRequired.html#message()">message</a></code>
                    attribute.
                </li>
            </ul>
        </p>
        <p>
            <b>...in your JSP:</b>
        </p>
        <ul>
            <li>
                Import the Struts HTML tag library: <code>&lt;%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %&gt;</code>.
            </li>
            <li>
                Add the Struts <code>&lt;html:javascript&gt;</code> somewhere after the form on the page.
            <li>
                Give your <code>&lt;netui:form&gt;</code> a <code>tagId</code>, and use this ID
                as the <code>formName</code> in your <code>&lt;html:javascript&gt;</code> tag.
            </li>
            <li>
                Choose a unique validation method name for the <code>method</code> attribute on
                <code>&lt;html:javascript&gt;</code>, and add a call to this method in your
                <code>&lt;netui:form&gt;</code>'s <code>onSubmit</code>, like this:
                <code>onSubmit="return myValidationMethod(this)"</code>.
            </li>
            <li>
                Give each form input a <code>tagId</code>, which must be the
                <strong>property name</strong> of the input field.  For example, if a text box
                has a datasource of <code>actionForm.myProperty</code>, you would use
                <code>myProperty</code> as the <code>tagId</code>.
            </li>
        </ul>

        <br/>
        <hr/>
        <netui:form action="submit" tagId="myForm" onSubmit="return doValidation(this)">
            <table>
                <tr>
                    <td>Full Name (required, 3 or more characters, letters and spaces, e.g., "Jane Doe"):</td>
                    <td><netui:textBox dataSource="actionForm.fullName" tagId="fullName"/></td>
                    <td><span<netui:error key="fullName"/></td>
                <tr>
                    <td>Email Address (required, must be a valid email address, e.g., "jane@doe.com"):</td>
                    <td><netui:textBox dataSource="actionForm.email" tagId="email"/></td>
                    <td><span<netui:error key="email"/></td>
                </tr>
            </table>
            <br/>
            <netui:button value="submit"/>
        </netui:form>

        <html:javascript formName="myForm" method="doValidation"/>
    </netui-template:section>
</netui-template:template>
