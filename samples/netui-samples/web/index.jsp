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
  <netui-template:setAttribute name="sampleTitle" value="Home Page"/>
  <netui-template:section name="main">
      <p>
          The following samples demonstrate a variety of
          <netui:anchor href="http://beehive.apache.org" value="Beehive"/> NetUI features.
      </p>
      <br/>
      <b>NetUI Page Flow Core</b>
      <br/>
      <dl>
          <dt><netui:anchor href="basicnesting/main/MainFlow.jpf" value="Basic Nested Page Flow"/></dt>
          <dd>Demonstrates the most basic nested page flow.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="exceptions/Controller.jpf" value="Exception Handling"/></dt>
          <dd>Demonstrates Page Flow declarative exception handling.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="fileupload/Controller.jpf" value="File Upload"/></dt>
          <dd>Demonstrates file upload to a page flow.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="lifecycle/normal/NormalPageFlow.jpf" value="Lifecycle"/></dt>
          <dd>Demonstrates the lifecycle (creation/destruction) of "normal" and "long-lived" page
              flows.  Also demonstrates inheritance of actions.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="loginexample/start/Controller.jpf" value="Login"/></dt>
          <dd>Demonstrates defining your own LoginHandler, as well as nested page flows and Page Flow
              inheritance.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="nesting/Controller.jpf" value="Nested Page Flow"/></dt>
          <dd>Demonstrates a simple nested page flow that returns data back to the original page
              flow.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="templateactions/MainFlow.jpf" value="Template Actions"/></dt>
          <dd>Demonstrates the use of a shared flow to provide actions for a template page (like a
              template that contains a menu bar).</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="tiles/MainFlow.jpf" value="Tiles"/></dt>
          <dd>Demonstrates the use of tiles with page flows.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="validation/Controller.jpf" value="Validation"/></dt>
          <dd>This sample demonstrates Page Flow declarative field validation.</dd>
      </dl>
      <br/>
      <b>NetUI JSP Tags</b>
      <br/>
      <dl>
          <dt><netui:anchor href="ui/formposting/Controller.jpf" value="Form Posting"/></dt>
          <dd>Demonstrates how to use a page flow and NetUI JSP tags to handle an HTML form POST.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/pageinput/Controller.jpf" value="Page Input"/></dt>
          <dd>Demonstrates how to use a page flow and page inputs to pass data from an action to a JSP page.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/resourcebinding/Controller.jpf" value="Message Resource Binding"/></dt>
          <dd>Demonstrates how to use JSP EL expressions to data bind to message resources</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/repeaterediting/Controller.jpf" value="Editing a Data Set using the Repeater"/></dt>
          <dd>Demonstrates how to use a repeater tag to edit text / boolean properties of items in a repeated data set with the repeater</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/cellrepeater/index.jsp" value="Cell repeater"/></dt>
          <dd>Demonstrates use of the cell repeater to render the contents of cells in an HTML table</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/datagrid/index.jsp" value="Data Grid"/></dt>
          <dd>Demonstrates how to use the &lt;netui-data:dataGrid> tags to render data sets and customize
              the appearance of the grid's styles, pager, cells, etc.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/tree/Controller.jpf" value="Tree"/></dt>
          <dd>Demonstrates how to use &lt;netui:tree> and related tags.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/popup/Controller.jpf" value="Popup Window"/></dt>
          <dd>Demonstrates a nested page flow shown in a popup window, with values passed back to the
              original page flow.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/select/Controller.jpf" value="The Select Tag"/></dt>
          <dd>Demonstrates how to use the select tag</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="ui/formintagfile/Controller.jpf" value="JSP Tag File as a Form"/></dt>
          <dd>Demonstrates how to use a JSP 2.0 .tag file to contain a NetUI form</dd>
      </dl>
      <br/>
      <b>Advanced / Specialty</b>
      <dl>
          <dt><netui:anchor href="advanced/actioninterceptors/Controller.jpf" value="Action Interceptors"/></dt>
          <dd>Demonstrates Page Flow action interceptors, which can be configured to run before or
              after all actions (or specific actions), and can change an action's destination.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="advanced/clientsidevalidator/Controller.jpf" value="Client-side Validation"/></dt>
          <dd>Demonstrates the enabling of Struts client-side validation in NetUI.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="advanced/customvalidator/Controller.jpf" value="Custom Validator"/></dt>
          <dd>Demonstrates the use of custom Commons Validator rules through annotations.</dd>
      </dl>
      <dl>
          <dt><netui:anchor href="advanced/dynaforms/Controller.jpf" value="DynaForms"/></dt>
          <dd>Demonstrates the use of Struts DynaActionForms in page flows</dd>
      </dl>
      <br/>
  </netui-template:section>
</netui-template:template>
