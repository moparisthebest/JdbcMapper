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
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<netui:html>
    <head>
        <title>Validation with Action annotation attribute useFormBean</title>
    </head>
    <netui:body>
        <h2>Validation with Action annotation attribute useFormBean</h2>
        <h3>Tests:</h3>
        <p>External bean with ValidatableProperty and action with useFormBean</p>
        <netui:form action="testBeanOneU">
            Property A: <netui:textBox dataSource="actionForm.propertyA"/>
            <netui:error key="propertyA"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>External bean with ValidatableProperty and actions w/ and w/o useFormBean</p>
        <netui:form action="testBeanTwoU">
            Property B: <netui:textBox dataSource="actionForm.propertyB"/>
            <netui:error key="propertyB"/>
            <br/>
            Property C: 1234
            <netui:hidden dataSource="actionForm.propertyC" dataInput="1234"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <netui:form action="testBeanTwo">
            Property B: 1234
            <netui:hidden dataSource="actionForm.propertyB" dataInput="1234"/>
            <br/>
            Property C: <netui:textBox dataSource="actionForm.propertyC"/>
            <netui:error key="propertyC"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>External bean, Controller class with ValidatableBean and actions w/ and w/o useFormBean</p>
        <netui:form action="testBeanThreeU">
            Property D: <netui:textBox dataSource="actionForm.propertyD"/>
            <netui:error key="propertyD"/>
            <br/>
            Property E: 123456
            <netui:hidden dataSource="actionForm.propertyE" dataInput="123456"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <netui:form action="testBeanThree">
            Property D: 123456
            <netui:hidden dataSource="actionForm.propertyD" dataInput="123456"/>
            <br/>
            Property E: <netui:textBox dataSource="actionForm.propertyE"/>
            <netui:error key="propertyE"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>External bean with ValidatableProperty, Controller class with ValidatableBean and actions w/ and w/o useFormBean</p>
        <netui:form action="testBeanFourU">
            Property F: <netui:textBox dataSource="actionForm.propertyF"/>
            <netui:error key="propertyF"/>
            <br/>
            Property G: 12345678
            <netui:hidden dataSource="actionForm.propertyG" dataInput="12345678"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <netui:form action="testBeanFour">
            Property F: 12345678
            <netui:hidden dataSource="actionForm.propertyF" dataInput="12345678"/>
            <br/>
            Property G: <netui:textBox dataSource="actionForm.propertyG"/>
            <netui:error key="propertyG"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>Inner class bean with ValidatableProperty and action with useFormBean</p>
        <netui:form action="testInnerBeanOneU">
            Property H: <netui:textBox dataSource="actionForm.propertyH"/>
            <netui:error key="propertyH"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>Inner class bean with ValidatableProperty and actions w/ and w/o useFormBean</p>
        <netui:form action="testInnerBeanTwoU">
            Property I: <netui:textBox dataSource="actionForm.propertyI"/>
            <netui:error key="propertyI"/>
            <br/>
            Property J: 1234
            <netui:hidden dataSource="actionForm.propertyJ" dataInput="1234"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <netui:form action="testInnerBeanTwo">
            Property I: 1234
            <netui:hidden dataSource="actionForm.propertyI" dataInput="1234"/>
            <br/>
            Property J: <netui:textBox dataSource="actionForm.propertyJ"/>
            <netui:error key="propertyJ"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>Inner class bean, Controller class with ValidatableBean and actions w/ and w/o useFormBean</p>
        <netui:form action="testInnerBeanThreeU">
            Property K: <netui:textBox dataSource="actionForm.propertyK"/>
            <netui:error key="propertyK"/>
            <br/>
            Property L: 123456
            <netui:hidden dataSource="actionForm.propertyL" dataInput="123456"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <netui:form action="testInnerBeanThree">
            Property K: 123456
            <netui:hidden dataSource="actionForm.propertyK" dataInput="123456"/>
            <br/>
            Property L: <netui:textBox dataSource="actionForm.propertyL"/>
            <netui:error key="propertyL"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <p>Inner class bean with ValidatableProperty, Controller class with ValidatableBean and actions w/ and w/o useFormBean</p>
        <netui:form action="testInnerBeanFourU">
            Property M: <netui:textBox dataSource="actionForm.propertyM"/>
            <netui:error key="propertyM"/>
            <br/>
            Property N: 12345678
            <netui:hidden dataSource="actionForm.propertyN" dataInput="12345678"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
        <br/>
        <netui:form action="testInnerBeanFour">
            Property M: 12345678
            <netui:hidden dataSource="actionForm.propertyM" dataInput="12345678"/>
            <br/>
            Property N: <netui:textBox dataSource="actionForm.propertyN"/>
            <netui:error key="propertyN"/>
            <br/>
            <netui:button type="submit" value="Submit"/>
        </netui:form>
    </netui:body>
</netui:html>
