<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>

<html>
<head>
<title>Form Test</title>
<netui:base />
</head>
<body>
  <h1>Form Test</h1>
  <netui:form action="/submit">
  <table width="100%">
  <tr><td>TextBox</td><td><netui:textBox dataSource="actionForm.text" /></td>
    <td>CheckboxGroup:</td>
    <td>Checkbox1 <netui:checkBox dataSource="actionForm.checkBox1" /><br />
        Checkbox2 <netui:checkBox dataSource="actionForm.checkBox2" /></td>
  </tr>
  <tr><td>TextArea</td><td><netui:textArea dataSource="actionForm.textArea" /></td>
    <td>Select:</td>
    <td><netui:select size="3" dataSource="actionForm.select" multiple="false" >
        <netui:selectOption value="1">Choice 1</netui:selectOption>
        <netui:selectOption value="2">Choice 2</netui:selectOption>
        <netui:selectOption value="3">Choice 3</netui:selectOption>
    </netui:select>
    </td>
  </tr>

  <tr>
  <td>A sample radiogroup:</td><td>
    <netui:radioButtonGroup dataSource="actionForm.radio">
        <netui:radioButtonOption value="1">Choice 1</netui:radioButtonOption><br>
        <netui:radioButtonOption value="2">Choice 2</netui:radioButtonOption><br>
        <netui:radioButtonOption value="3">Choice 3</netui:radioButtonOption>
    </netui:radioButtonGroup>
    </td>
  </tr>
  <tr><td colspan="4"><netui:button type="submit" >Change</netui:button></td></tr>
  </table>
  </netui:form>
  <hr />
  <table width="100%" border="1" cellspacing="0">
  <tr><th>Form Control</th><th>Value</th><th>Change</th></tr>
  <db:repeater dataSource="pageFlow.info">
        <db:repeaterItem>
        <tr>
          <td width="100px" ><netui:span value="${container.item.name}"/></td>
          <td width="300px"><netui:span value="${container.item.value}"/></td>
          <td><netui:span value="${container.item.change}"/></td>
        </tr>
        </db:repeaterItem>
  </db:repeater>
  </table>
</body>
</html>

