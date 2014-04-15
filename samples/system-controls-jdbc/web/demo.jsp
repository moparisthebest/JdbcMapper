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

<netui:html>

  <head>
    <title>JspControlApp Sample Application</title>
    <netui:base/>
    <style>

.datagrid-header{
    background-color: #eeeeee;
}
.datagrid-header-cel{
    background-color: #f0f0f0;
}
.custom-datagrid-header-cell{
    background-color: #0f0f0f;
}
.datagrid-data-cell{
}
.datagrid-even{
    background-color: #ffffff;
}
.datagrid-even a {
    text-decoration: none;
    color: #000000;   
}
.datagrid-odd{
    background-color: #ffffde;
}
.datagrid-odd a {
    text-decoration: none;
    color: #000000;
}
    </style>
  </head>

  <netui:body>

      <h1>JdbcControl Sample</h1>
      <p></p>
      <p>This is a tiny sample application which uses a JdbcControl inside of a Beehive PageFlow. When 
         this page is accessed for the first time, a small Derby database is created and populated by the 
         JdbcControl PopulateDBCtrl.</p>

      <p>The data grid of product names below has been provided by the SimpleJdbcCtrl's getProductNames() 
         method. Clicking in the second column will trigger another call to the SimpleJdbcCtrl to get
         more detailed information from the database for the selected item.</p>

      <netui-data:dataGrid dataSource="pageInput.products" name="productNameGrid">
        <netui-data:configurePager disableDefaultPager="true"/>
          <netui-data:header>
            <netui-data:headerCell headerText="Product Name"/>
            <netui-data:headerCell headerText="Detailed Product Information"/>
          </netui-data:header>
          <netui-data:rows>
            <netui-data:spanCell value="${container.item}"/>
            <netui-data:anchorCell action="productDetails" value="click for details...">
              <netui:parameter name="key" value="${container.item}"/>
            </netui-data:anchorCell>
          </netui-data:rows>

      </netui-data:dataGrid>


  </netui:body>

</netui:html>
