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

  <netui-template:setAttribute name="sampleTitle" value="Validation"/>

  <netui-template:section name="main">
        <i>
            The form bean <code>MyForm</code> uses annotations to define several validation rules:
            <ul>
                <li>Both fields are required.</li>
                <li>The 'value' field must be at least three characters long.</li>
                <li>The fields must match.</li>
            </ul>
        </i>
        <br/>
        <netui:form action="submit">
            <table>
                <tr>
                    <td>value:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.value"/>
                        <netui:error key="value"/>
                    </td>
                </tr>
                <tr>
                    <td>confirm value:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.confirmValue"/>
                        <netui:error key="confirmValue"/>
                    </td>
                </tr>
            </table>
            <br/>
            <netui:button value="Submit"/>
            <i>(submits to action 'submit', which just uses the rules defined on <code>MyForm</code>)</i>
            <br/>
            <netui:button value="Submit (extra rule)" action="submitWithExtraRule"/>
            <i>(submits to action 'submitWithExtraRule', which adds a rule that requires 'value' to be at most four characters long)</i>
        </netui:form>
  </netui-template:section>

</netui-template:template>
