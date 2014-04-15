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
    <table cellspacing="0" cellpadding="0" border="0" width="100%">
    <tr valign="top">
      <td width="35%">
        <!-- Begin content -->
        <tiles:insert attribute="panel"/>
        <!-- End content -->
      </td>
      <td width="2%">&nbsp;</td>
      <td width="63%">
        <!-- Begin body -->
        <tiles:insert attribute="body"/>
        <!-- End body -->
      </td>
    </tr>
    </table>
  </td>
</tr>
<tr>
  <td>
    <!-- Begin footer -->
    <tiles:insert attribute="footer"/>
    <!-- End footer -->
  </td>
</tr>
</table>

</body>
</html>

