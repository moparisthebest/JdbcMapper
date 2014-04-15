<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<html>
<head>
  <%-- We rely on tiles-defs.xml to set the title --%>
  <title><tiles:getAsString name="titleString"/></title>
</head>
<body>

<table width="750" cellspacing="0" cellpadding="0" border="0">
<tr>
  <td>
    <!-- Begin header -->
    <tiles:insert attribute="header"/>
    <!-- End header -->
  </td>
</tr>
<tr>
  <td>
    <!-- Begin topMenu -->
    <tiles:insert attribute="menu"/>
    <!-- End topMenu -->
  </td>
</tr>
<tr>
  <td>
    <br>
    <!-- Begin content -->
    <tiles:insert attribute="content"/>
    <!-- End content -->
    <br>
  </td>
</tr>
</table>

<!-- Begin footer -->
<tiles:insert attribute="footer"/>
<!-- End footer -->

</body>
</html>

