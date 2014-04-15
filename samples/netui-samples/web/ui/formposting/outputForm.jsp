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
  <netui-template:setAttribute name="sampleTitle" value="Basic Form"/>
  <netui-template:section name="main">
      <i>This NetUI form has been pre-populated with a value from an "output form" from
         the Page Flow action postOutputForm.  <br/>Click the Submit button to POST the form
         or change then name and then click Submit.  <br/>Once the form has been submitted,
         the name will re-appear in the text box.</i>
      <br/>
      <br/>
      <netui:form action="postOutputForm">
          <netui:textBox dataSource="actionForm.name"/><br/>
          <br/>
          <netui:button value="Submit"/>
      </netui:form>
      <p><netui:anchor action="begin">Start over</netui:anchor></p>
  </netui-template:section>
</netui-template:template>
