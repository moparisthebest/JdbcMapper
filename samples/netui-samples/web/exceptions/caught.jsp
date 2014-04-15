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

    <netui-template:setAttribute name="sampleTitle" value="Declarative Exception Handling"/>

    <netui-template:section name="main">
        The <code>netui:errors</code> (or <code>netui:error</code>, with the appropriate
        <code>key</code>) tag can print custom error messages for exceptions, which are found in
        message keys that are the exception class names, or which are overridden by the
        <code>@Jpf.Catch</code> annotations in the page flow:
        <blockquote>
            <b><netui:errors/></b>
        </blockquote>
        <br/>
        The <code>netui:exceptions</code> tag can print the result of <code>getMessage</code> on
        the exception, and/or the exception stack trace:
        <blockquote>
            <b><netui:exceptions showMessage="true" showStackTrace="false"/></b>
        </blockquote>
        <br/>
        <netui:anchor action="begin">try again</netui:anchor>
    </netui-template:section>

</netui-template:template>
