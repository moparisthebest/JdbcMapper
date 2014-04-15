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
        <p>
            Tests the NavigateTo feature with a page containing a
            &lt;jsp:include&gt; to make sure we don't set current page
            info to the included page. (BEEHIVE-1115)
        </p>
        <br/>
        <p>Start the test by going to the first JSP.</p>
        <br/><br/>
        <netui:anchor action="page1">page one...</netui:anchor>
    </netui:body>
</netui:html>

  
