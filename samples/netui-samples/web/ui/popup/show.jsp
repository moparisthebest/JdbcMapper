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
    <netui-template:setAttribute name="sampleTitle" value="Popup Windows"/>
    <netui-template:section name="main">
        <h3>You submitted the following information:</h3>
        <table>
            <tr>
                <td>Name:</td>
                <td><b>${pageInput.form.name}</b></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><b>${pageInput.form.address}</b></td>
            </tr>
            <tr>
                <td>City:</td>
                <td><b>${pageInput.form.city}</b></td>
            </tr>
            <tr>
                <td>Favorite Color:</td>
                <td><b>${pageInput.form.color}</b></td>
            </tr>
        </table>
        <br/>
        <netui:anchor action="begin">try again</netui:anchor>
    </netui-template:section>
</netui-template:template>
