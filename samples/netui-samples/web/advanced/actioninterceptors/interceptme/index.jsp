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
        <b>${pageFlow.URI}</b>
        <br/>
        <br/>
        <table border="1">
            <tr>
                <td nowrap="true"><netui:anchor action="begin">begin</netui:anchor></td>
                <td>
                    Intercepted with a simple-action-interceptor, which "injects" a nested page
                    flow before allowing the action to run
                </td>
            </tr>
            <tr>
                <td nowrap="true">
                    <netui:anchor action="another">another intercepted action</netui:anchor>
                </td>
                <td>
                    Intercepted with <code>org.apache.beehive.samples.netui.actioninterceptor.MyInterceptor</code>
                    -- notice the output on the console.  This also "injects" a nested page flow
                    before allowing the action to run.
                </td>
            </tr>
            <tr>
                <td nowrap="true">
                    <netui:anchor action="notIntercepted">a non-intercepted action</netui:anchor>
                </td>
                <td>
                    Not intercepted.
                </td>
            </tr>
        </table>
    </netui-template:section>

</netui-template:template>
