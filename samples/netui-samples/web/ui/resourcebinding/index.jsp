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
  <netui-template:setAttribute name="sampleTitle" value="Resource Bundle Binding"/>
  <netui-template:section name="main">
      <b>Binding to the default resource bundle</b>
      <br/>
      <table>
          <tr><td><i>Expression: \${bundle.default.message}</i></td></tr>
          <tr><td><netui:label value="${bundle.default.message}"/></td></tr>
      </table>
      <br/>
      <b>Binding to a resource bundle named via the @Jpf.Controller annotation</b>
      <br/>
      <table>
          <tr><td><i>Expression: \${bundle.moremessages.anothermessage}</i></td></tr>
          <tr><td><netui:label value="${bundle.moremessages.anothermessage}"/></td></tr>
      </table>
      <br/>
      <b>Binding to a resource bundle declared with the &lt;netui-data:declareBundle&gt; JSP tag</b>
      <br/>
      <netui-data:declareBundle name="pageBundle"
                                bundlePath="org.apache.beehive.samples.netui.resources.resourcebinding.jspmessages"/>
      <br/>
      <table>
          <tr><td><i>Expression: \${bundle.pageBundle.themessage}</i></td></tr>
          <tr><td><netui:label value="${bundle.pageBundle.themessage}"/></td></tr>
      </table>
  </netui-template:section>
</netui-template:template>