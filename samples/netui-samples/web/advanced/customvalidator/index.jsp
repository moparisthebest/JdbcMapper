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


<netui-template:template templatePage="/resources/template/template.jsp">
    <netui-template:setAttribute name="sampleTitle" value="Custom Validator"/>
    <netui-template:section name="main">
        <p>
            This sample demonstrates the use of custom Commons Validator rules through annotations.
        </p>
        <p>
            The two custom rules used here ('palindrome' and 'divisibleBy') are defined in
            /WEB-INF/custom-validator-rules.xml.  These rules are merged into the page flow using
            the
            <code><a href="http://beehive.apache.org/docs/1.0.2/netui/apidocs/javadoc/org/apache/beehive/netui/pageflow/annotations/Jpf.Controller.html#customValidatorConfigs()">customValidatorConfigs</a></code>
            attribute on the
            <code><a href="http://beehive.apache.org/docs/1.0.2/netui/apidocs/javadoc/org/apache/beehive/netui/pageflow/annotations/Jpf.Controller.html">@Jpf.Controller</a></code>
            annotation in Controller.java.
        </p>
        <p>
            The rules are applied to various properties of the form bean
            (<code>org.apache.beehive.samples.netui.customValidator.MyFormBean</code>) through the
            <code><a href="http://beehive.apache.org/docs/1.0.2/netui/apidocs/javadoc/org/apache/beehive/netui/pageflow/annotations/Jpf.ValidateCustomRule.html">@Jpf.ValidateCustomRule</a></code>
            annotation.
        </p>

        <netui:form action="submit">
            <table>
                <tr>
                    <td>A palindrome (e.g., "bob"):</td>
                    <td><netui:textBox dataSource="actionForm.palindrome"/></td>
                    <td><span style="color:red"><netui:error key="palindrome"/></span>
                </tr>
                <tr>
                    <td>A number divisible by 3 (e.g., "6"):</td>
                    <td><netui:textBox dataSource="actionForm.divisibleBy3"/></td>
                    <td><span style="color:red"><netui:error key="divisibleBy3"/></span>
                </tr>
                <tr>
                    <td>A palindrome divisible by 5 (e.g., "54345"):</td>
                    <td><netui:textBox dataSource="actionForm.palindromeDivisibleBy5"/></td>
                    <td><span style="color:red"><netui:error key="palindromeDivisibleBy5"/></span>
                </tr>
            </table>
            <br/>
            <netui:button value="submit"/>
        </netui:form>
    </netui-template:section>
</netui-template:template>
