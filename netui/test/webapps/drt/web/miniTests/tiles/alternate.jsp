<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<html>
<head>
  <%-- We rely on tiles-defs.xml to set the title --%>
  <title><tiles:getAsString name="titleString"/></title>
</head>
<body>

<table width="750" cellspacing="0" cellpadding="0" border="0">
<tr>
  <td colspan="2">
    <!-- Begin header -->
    <tiles:insert attribute="header"/>
    <!-- End header -->
  </td>
</tr>
<tr>
  <td bgcolor="#CD5C5C" align="top">
    <!-- Begin sideMenu -->
    <tiles:insert attribute="menu"/>
    <!-- End sideMenu -->
  </td>
  <td>
    <br>
    <table cellspacing="0" cellpadding="0" border="0" width="100%">
    <tr valign="top">
      <td>
        <!-- Begin panel1 -->
        <tiles:insert attribute="panel1"/>
        <!-- End panel1 -->
      </td>
      <td>
        <!-- Begin panel2 -->
        <tiles:insert attribute="panel2"/>
        <!-- End panel2 -->
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <!-- Begin body -->
        <tiles:insert attribute="body"/>
        <!-- End body -->
      </td>
    </tr>
    </table>
    <br>
  </td>
</tr>
</table>

<!-- Begin footer -->
<tiles:insert attribute="footer"/>
<!-- End footer -->

</body>
</html>


