<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<netui:html>
  <head>
    <title>JdbcControl Sample Application</title>
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
      <p></p>
      <p>This page displays additional data for the specified product name.  Data is retrieved from the database
         by the JdbcControl SimpleDBControl.jcx using its getProductDetails() method.</p>
      <p></p>

      <netui-data:dataGrid dataSource="pageFlow.productDetails" name="productDetailsGrid">
        <netui-data:configurePager disableDefaultPager="true"/>
          <netui-data:header>
            <netui-data:headerCell headerText="Product Name"/>
            <netui-data:headerCell headerText="Product Description"/>
            <netui-data:headerCell headerText="Quantity"/>
          </netui-data:header>
        <netui-data:rows>
          <netui-data:spanCell value="${container.item.name}"/>
          <netui-data:spanCell value="${container.item.description}"/>
          <netui-data:spanCell value="${container.item.quantity}"/>
        </netui-data:rows>
      </netui-data:dataGrid>
      <br>
      <p><netui:anchor action="startDemo">back</netui:anchor></p>
  </netui:body>

</netui:html>
