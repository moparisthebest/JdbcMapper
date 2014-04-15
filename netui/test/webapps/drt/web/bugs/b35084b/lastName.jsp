<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>

<html>
  <head>
    <netui:base/>
  </head>
  <body style="margin:0">
   <netui:form action="next" focus="lastname">
        <table border="0" style="font-size:10">
            <tr>
                <td>
                    Text: <netui:textBox dataSource="actionForm.lastname" defaultValue="${actionForm.lastname}" />   
                </td>
            </tr>
            <tr>
                <td>         
                    <netui:imageAnchor action="beginbogus" src="../../resources/images/back.gif" formSubmit="true" />
                    !!!!
                    <netui:imageAnchor action="next" src="../../resources/images/insert.gif" formSubmit="true" />
                </td>
            </tr>
        </table>
    </netui:form>         
</body>
</html>
