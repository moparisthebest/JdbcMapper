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
        <title>NavigateTo Current/Previous JSP with &lt;jsp:include&gt;</title>
        <netui:base/>
    </head>
    <netui:body>
        <h3>NavigateTo Current/Previous JSP with &lt;jsp:include&gt;</h3>
        <h3>Page2.jsp</h3>

        <br/><br/>&quot;NavigateTo&quot; this page:
        <netui:anchor action="returnToPage">current...</netui:anchor>

        <br/><br/>&quot;NavigateTo&quot; the previous page:
        <netui:anchor action="returnToPrevPage">Prev...</netui:anchor>

        <br/><br/>Now, test &quot;NavigateTo&quot; this page (via shared flow):
        <netui:anchor action="shared.returnToPage">shared.curr</netui:anchor>

        <br/><br/>Now, test &quot;NavigateTo&quot; the previous page (via shared flow):
        <netui:anchor action="shared.returnToPrevPage">shared.prev</netui:anchor>

        <br/>
        <jsp:include page="message.jsp" />
    </netui:body>
</netui:html>

  
