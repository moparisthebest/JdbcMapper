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
        <title>SharedFlow Field</title>
        <netui:base/>
    </head>
    <body>
        <jsp:include page="../common/header.jsp"/>

        <h2>Shared Flow Field</h2>
        <br/>
        <netui:anchor action="sfFieldTest">Try Field Test</netui:anchor>
        <br/>
        <netui:span value="${pageInput.sharedFlowName}"/>

        <jsp:include page="../common/footer.jsp"/>
    </body>
</netui:html>


