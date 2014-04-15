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

      <netui-data:dataGrid dataSource="pageInput.productNames" name="productNameGrid">
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
