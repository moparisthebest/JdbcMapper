<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

    <table cellspacing="0" cellpadding="0" border="0" width="100%">
    <tr valign="top">
      <td width="63%">
        <!-- Begin body -->
        <tiles:insert attribute="body"/>
        <!-- End body -->
      </td>
      <td width="2%">&nbsp;</td>
      <td width="35%">
        <!-- Begin panel1 -->
        <tiles:insert attribute="panel1"/>
        <!-- End panel1 -->
        <br>
        <!-- Begin panel2 -->
        <tiles:insert attribute="panel2"/>
        <!-- End panel2 -->
      </td>
    </tr>
    </table>

