<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Style</title>
<!-- target -->
<netui:base />
<style>
.red {
        color: red
}
.green {
        color: green
}
.redBorder {
        border: solid 1pt red;
        color: red
}
.blueBorder {
        border:solid 1pt blue;
        color:blue
}
.bigBlue {
        Color: blue;
        font: bold 14pt sans-serif
}
.select {
        margin:10;
        color: red;
        font: bold 14pt sans-serif
}
.selectOpt {
        margin:10;
        color: blue;
        font: bold 12pt serif
}
.blueButton {
        color:blue;
        font: bold 12pt serif
}
.redImage {
        border: solid 10pt red;
        padding: 10pt;
        margin:2pt
}
.blueImage {
        border: solid 2pt blue;
}
.greenImage {
        border: solid 4pt green;
}
.form {
        border: solid 1pt;
        padding: 10pt;
}
</style>
</head>
<body>
<h4>Style</h4>
<netui:form action="postback" styleClass="form">
<!-- TEXTBOX -->
<netui:textBox dataSource="actionForm.text"
        maxlength="30" styleClass="red"
/><br />
<!-- TEXTAREA -->
<netui:textArea dataSource="actionForm.textArea"
        cols="20" rows="5" styleClass="green"
/><br />
<!-- CHECKBOXGROUP with Option-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup" styleClass="redBorder">
<!-- CHECKBOXOPTION -->
<netui:checkBoxOption value="foo" styleClass="blueBorder"/>
</netui:checkBoxGroup><br />

<!-- CHECKBOXGROUP with OptionDataSource-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup2" styleClass="redBorder"
        optionsDataSource="${actionForm.checkOptions}" />

<!-- LABEL:-->
<netui:span value="Checkbox 1" styleClass="bigBlue"
/>
<!-- CHECKBOX: defaultValue, style, styleClass, tabindex
-->
<netui:checkBox dataSource="actionForm.checkBox1" styleClass="blueBorder"
/><br /><br />
<!-- RADIOBUTTONGROUP with Option-->
<netui:radioButtonGroup dataSource="actionForm.radio">
<!-- RADIOBUTTONOPTION -->
<netui:radioButtonOption value="Choice 1" styleClass="blueBorder"
/><br />
<netui:radioButtonOption value="Choice 2" styleClass="redBorder"
/>
</netui:radioButtonGroup><br />

<netui:radioButtonGroup dataSource="actionForm.radio2" optionsDataSource="${actionForm.checkOptions}"
        styleClass="blueBorder"/>
<br />
<!-- SELECT -->
<netui:select 
        dataSource="actionForm.select"
        size="3"
        styleClass="select"
>
<!-- SELECTOPTION, locale, style, styleClass, tabindex
-->
<netui:selectOption value="1"
        styleClass="selectOpt"
/>
<netui:selectOption value="2">Choice 2</netui:selectOption>
<netui:selectOption value="3" disabled="true">Choice 3</netui:selectOption>
</netui:select><br />
<!-- HIDDEN -->
<netui:hidden dataSource="actionForm.hidden"
/>
<hr />
<netui:button type="reset" styleClass="red">Reset</netui:button>
<!-- BUTTON -->
<netui:button type="submit" styleClass="blueButton"
        value="submit"
/>
<br />
<!-- IMAGEBUTTON -->
<netui:imageButton
        src="/coreWeb/images/godzilla.gif"
        rolloverImage="/coreWeb/images/godzillaRollover.gif"
        styleClass="redImage"
/>
</netui:form>
<hr />
<!-- ANCHOR -->
<netui:anchor action="postback"
>Post Back</netui:anchor><br />
<!-- IMAGE -->
<netui:image src="/coreWeb/images/godzilla.gif"
        align="center" 
        hspace="10" vspace="10"
        height="66" width="48"
        styleClass="blueImage"
/><br />
<!-- IMAGEANCHOR -->
<netui:imageAnchor action="postback"
        src="/coreWeb/images/godzilla.gif"
        rolloverImage="/coreWeb/images/godzillaRollover.gif"
        align="center"
        hspace="10" vspace="10"
        height="66" width="48"
        imageStyleClass="greenImage"
/>
</body>
</html>
