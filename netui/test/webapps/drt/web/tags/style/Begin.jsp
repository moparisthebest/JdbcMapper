<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Style</title>
<!-- target -->
<netui:base />
</head>
<body>
<h4>Style</h4>
<netui:form action="/postback" style="border: solid 1pt;padding: 10pt">
<!-- TEXTBOX -->
<netui:textBox dataSource="actionForm.text"
        maxlength="30" style="color: red"
/><br />
<!-- TEXTAREA -->
<netui:textArea dataSource="actionForm.textArea"
        cols="20" rows="5" style="color: green"
/><br />
<!-- CHECKBOXGROUP with Option-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup" style="border: solid 1pt red;color: red">
<!-- CHECKBOXOPTION -->
<netui:checkBoxOption value="foo" style="border:solid 1pt blue;color:blue"/>
</netui:checkBoxGroup><br />

<!-- CHECKBOXGROUP with OptionDataSource-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup2" style="border: solid 1pt red;color: red"
        optionsDataSource="${actionForm.checkOptions}" />

<!-- LABEL:-->
<netui:span value="Checkbox 1" style="Color: blue;font: bold 14pt sans-serif "
/>
<!-- CHECKBOX: defaultValue, style, styleClass, tabindex
-->
<netui:checkBox dataSource="actionForm.checkBox1" style="border: solid 1pt blue"
/><br /><br />
<!-- RADIOBUTTONGROUP with Option-->
<netui:radioButtonGroup dataSource="actionForm.radio">
<!-- RADIOBUTTONOPTION -->
<netui:radioButtonOption value="Choice 1" style="border: solid 1pt blue: color:blue"
/><br />
<netui:radioButtonOption value="Choice 2" style="border: solid 1pt red; color:red"
/>
</netui:radioButtonGroup><br />

<netui:radioButtonGroup dataSource="actionForm.radio2" optionsDataSource="${actionForm.checkOptions}"
        style="border: solid 1pt blue"/>
<br />
<!-- SELECT -->
<netui:select 
        dataSource="actionForm.select"
        size="3"
        style="margin:10;color: red;font: bold 14pt sans-serif"
>
<!-- SELECTOPTION, locale, style, styleClass, tabindex
-->
<netui:selectOption value="1"
        value="Choice 1" style="margin:10;color: blue;font: bold 12pt serif"
/>
<netui:selectOption value="2">Choice 2</netui:selectOption>
<netui:selectOption value="3" disabled="true">Choice 3</netui:selectOption>
</netui:select><br />
<!-- HIDDEN: style, styleClass, tabindex
-->
<netui:hidden dataSource="actionForm.hidden"/>
<hr />
<netui:button type="reset" style="color:red">Reset</netui:button>
<!-- BUTTON -->
<netui:button type="submit" style="color:blue;font: bold 12pt serif"
        value="submit"
/>
<br />
<!-- IMAGEBUTTON -->
<netui:imageButton
        src="/coreWeb/images/godzilla.gif"
        rolloverImage="/coreWeb/images/godzillaRollover.gif"
        style="border: solid 10pt red;padding: 10pt;margin:2pt"
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
        style="border: solid 2pt blue;"
/><br />
<!-- IMAGEANCHOR: formSubmit, forward, href, ismap, linkName, location,
        lowsrc, page, scope, style, styleClass, tabindex,
        target, usemap

        (Internally the roll over stuff uses mouse over and mouse out)
-->
<netui:imageAnchor action="postback"
        src="/coreWeb/images/godzilla.gif"
        rolloverImage="/coreWeb/images/godzillaRollover.gif"
        align="center"
        hspace="10" vspace="10"
        height="66" width="48"
        imageStyle="border: solid 4pt green;"
/>
</body>
</html>
