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
<netui:html>
    <head>
        <title>conditional forwards with duplicate expressions</title>
    </head>
    <netui:body>
        <h3>Jira1146 - conditional forwards with duplicate expressions</h3>

        <netui:form action="begin">
            choice ("page1", "page2", or something unrecognized):
            <netui:textBox dataSource="pageFlow.choice" />
            <netui:button value="submit"/>
        </netui:form>
        
        <netui:anchor action="anAction">
            conditional action
        </netui:anchor>

        <table border="1">
            <tr>
                <td>choice</td>
                <td>result</td>
            </tr>
            <tr>
                <td>page1</td>
                <td>page1.jsp</td>
            </tr>
            <tr>
                <td>page2</td>
                <td>page2.jsp</td>
            </tr>
            <tr>
                <td><i>anything else</i></td>
                <td>this page, index.jsp</td>
            </tr>
        </table>
    </netui:body>
</netui:html>

  
