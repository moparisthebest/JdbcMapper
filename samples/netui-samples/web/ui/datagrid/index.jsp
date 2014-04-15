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
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Data Grid Feature Samples"/>
  <netui-template:section name="main">
      <br/>
      <b>Basic Features</b><br/>
      <netui:anchor href="basic/basic.jsp">Basic</netui:anchor><br/>
      <netui:anchor href="basic/basic-with-header.jsp">Basic with Header</netui:anchor><br/>
      <netui:anchor href="basic/basic-with-formatter.jsp">Basic with Formatter</netui:anchor><br/>
      <netui:anchor href="basic/basic-with-style.jsp">Basic with Style</netui:anchor><br/>
      <netui:anchor href="basic/basic-with-css.jsp">Basic with CSS</netui:anchor><br/>
      <netui:anchor href="basic/basic-with-css-foo.jsp">Basic with CSS Named foo</netui:anchor><br/>
      <netui:anchor href="basic/basic-with-no-styles.jsp">Basic with No Styles</netui:anchor><br/>
      <netui:anchor href="basic/basic-default-pager.jsp">Basic with Default Pager</netui:anchor><br/>
      <netui:anchor href="basic/basic-fpnl-pager.jsp">Basic with First/Previous//Next/Last Pager</netui:anchor><br/>
      <netui:anchor href="basic/basic-pager-implicit-objects.jsp">Basic with Implicit Object Binding</netui:anchor><br/>
      <netui:anchor href="basic/basic-default-renderpagertag.jsp">Basic with Render Pager Tag</netui:anchor><br/>
      <br/>
      <br/>
      <b>Advanced Features</b><br/>
      <netui:anchor href="masterdetail/master.jsp">Master / Detail</netui:anchor><br/>
      <netui:anchor href="basic/basic-pager-jumptopage.jsp">"Jump To Page" Custom Pager</netui:anchor><br/>
      <netui:anchor href="sortandfilter/Controller.jpf">Using Data Grid Sorts and Filters</netui:anchor>
  </netui-template:section>
</netui-template:template>