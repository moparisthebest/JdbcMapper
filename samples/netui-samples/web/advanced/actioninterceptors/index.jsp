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

  <netui-template:setAttribute name="sampleTitle" value="Page Flow Action Interceptors"/>

    <netui-template:section name="main">
        <p>
            This sample demonstrates action interceptors, which can be configured to run before or
            after all actions (or specific actions).  Action interceptors can alter the resulting
            Forward of an action, and can "inject" an entire nested page flow to be shown before
            the action is invoked.
        </p>
        <p>
            Action interceptors are configured in WEB-INF/beehive-netui-config.xml.
        </p>
        <netui:anchor action="start">hit interceptme/Controller.jpf, whose begin action is intercepted</netui:anchor>

    </netui-template:section>

</netui-template:template>
