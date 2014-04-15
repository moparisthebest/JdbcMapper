<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Styles on Options</title>
<!-- target -->
<netui:base />
<style>
.redCheck {
        Color: red;
        font: bold 14pt sans-serif
}
.redRadio {
        Color: red;
        font: bold 10pt sans-serif
}
.blueCheck {
        Color: blue;
        font: bold 8pt sans-serif
}
.blueRadio {
        Color: blue;
        font: bold 16pt sans-serif
}
</style>
</head>
<body>
<h4>Style</h4>
<netui:form action="/postback" style="border: solid 1pt;padding: 10pt">

<!-- CHECKBOXGROUP with Option-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup" labelStyle="font: bold 14pt sans-serif">
<netui:checkBoxOption value="foo"/><br />
<netui:checkBoxOption value="bar" labelStyle="font: 8pt sans-serif"/>
</netui:checkBoxGroup>
<hr />

<!-- CHECKBOXGROUP with Option-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup1" labelStyleClass="redCheck">
<netui:checkBoxOption value="foo"/><br />
<netui:checkBoxOption value="bar"/>
</netui:checkBoxGroup><br />
<hr />

<!-- CHECKBOXGROUP with OptionDataSource-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup2"
        labelStyle="color: red;font: bold 14pt sans-serif"
        optionsDataSource="${actionForm.checkOptions}" />
<hr />
<!-- CHECKBOXGROUP with OptionDataSource-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup3"
        labelStyleClass="blueCheck"
        optionsDataSource="${actionForm.checkOptions}" />
<hr />

<!-- RADIOBUTTONGROUP with Option-->
<netui:radioButtonGroup dataSource="actionForm.radio" labelStyle="font: bold 14pt sans-serif">
<netui:radioButtonOption value="Radio 1" /><br />
<netui:radioButtonOption value="Radio 2" labelStyle="font: 10pt sans-serif"/>
</netui:radioButtonGroup><br />
<hr />
<!-- RADIOBUTTONGROUP with Option-->
<netui:radioButtonGroup dataSource="actionForm.radio1" labelStyleClass="redRadio">
<netui:radioButtonOption value="Radio 1" /><br />
<netui:radioButtonOption value="Radio 2" labelStyle="font: 10pt sans-serif"/>
</netui:radioButtonGroup><br />
<hr />

<!-- RADIOBUTTONGROUP with OptionDataSource-->
<netui:radioButtonGroup dataSource="actionForm.radio2" optionsDataSource="${actionForm.radioOptions}"
        labelStyle="color: red;font: bold 6pt sans-serif"/>
<hr />

<!-- RADIOBUTTONGROUP with OptionDataSource-->
<netui:radioButtonGroup dataSource="actionForm.radio3" optionsDataSource="${actionForm.radioOptions}"
        labelStyleClass="blueRadio"/>
<hr />

<!-- BUTTON -->
<netui:button type="submit" style="color:blue;font: bold 12pt serif"
        value="submit"
/>
</netui:form>
</body>
</html>
