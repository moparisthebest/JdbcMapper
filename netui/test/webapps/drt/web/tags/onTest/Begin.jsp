<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<html>
<head>
<script language="JavaScript">

var opCount = 0;
var textId = "text";
var textAreaId = "textArea";
var buttonId = "button";
var formId = "formOut";
var anchorId = "anchor";
var checkboxId = "checkbox";
var checkboxGroupId = "checkboxGroup";
var radioId = "radio";
var selectId = "select";
var selectOptionId = "selectOption";
var labelId = "label";
var hiddenId = "hidden";
var imageId = "image";
var imageButtonId = "imageButton";
var imageAnchorId = "imageAnchor";


function print_blur(txtId) {
   print_to_doc(txtId," blur");
}

function print_operation(txtId) {
  var t = document.getElementById(txtId + "_operation");
  var text = txtId + " operations:" + ++opCount;
  t.firstChild.data = text;
}

function print_reset(txtId) {
   print_to_doc(txtId," reset");
}

function print_submit(txtId) {
   print_to_doc(txtId," submit");
}

function print_change(txtId) {
   print_to_doc(txtId," change");
}

function print_click(txtId) {
   print_to_doc(txtId," click");
}

function print_dblClick(txtId) {
   print_to_doc(txtId," dblClick");
}

function print_focus(txtId) {
   print_to_doc(txtId," focus");
}

function print_keyDown(txtId) {
   print_to_doc(txtId," keyDown");
}

function print_keyPress(txtId) {
   print_to_doc(txtId," keyPress");
}

function print_keyUp(txtId) {
   print_to_doc(txtId," keyUp");
}

function print_mouseDown(txtId) {
   print_to_doc(txtId," mouseDown");
}

function print_mouseMove(txtId) {
   print_to_doc(txtId,".");
}

function print_mouseOver(txtId) {
   print_to_doc(txtId," over");
}

function print_mouseOut(txtId) {
   print_to_doc(txtId," out");
}

function print_mouseUp(txtId) {
   print_to_doc(txtId," mouseUp");
}

function print_select(txtId) {
   print_to_doc(txtId," select");
}

function print_to_doc(txtId,txt) {
  var t = document.getElementById(txtId);
  var text = t.firstChild.data;
  text += " " + txt;
  t.firstChild.data = text;
}
</script>
<title>On Test</title>
<!-- target -->
<netui:base />
</head>
<body>
<h4>On Test</h4>
<table border="1" cellspacing="1" cellpadding="1" width="100%">
<tr><td width="200pt" valign="top">
<!-- FORM: enctype, focus, location, method, name, scope, style, styleClass,
        tabindex, type
-->
<netui:form action="/postback"
        onClick="print_operation(formId);"
        onKeyDown="print_operation(formId);"
        onKeyPress="print_operation(formId);"
        onKeyUp="print_operation(formId);"
        onMouseDown="print_operation(formId);"
        onMouseMove="print_operation(formId);"
        onMouseOut="print_mouseOut(formId);"
        onMouseOver="print_mouseOver(formId);"
        onMouseUp="print_operation(formId);"
        onReset="print_reset(formId);"
        onSubmit="print_submit(formId);"
>
<!-- TEXTBOX: defaultValue, readonly, style, styleClass, tabindex
-->
<netui:textBox dataSource="actionForm.text"
        maxlength="30"
        onBlur="print_blur(textId);"
        onChange="print_change(textId);"
        onClick="print_click(textId);"
        onDblClick="print_dblClick(textId);"
        onFocus="print_focus(textId);"
        onKeyDown="print_keyDown(textId);"
        onKeyPress="print_keyPress(textId);"
        onKeyUp="print_keyUp(textId);"
        onMouseDown="print_mouseDown(textId);"
        onMouseMove="print_mouseMove(textId);"
        onMouseOver="print_mouseOver(textId);"
        onMouseOut="print_mouseOut(textId);"
        onMouseUp="print_mouseUp(textId);"
        onSelect="print_select(textId);"

/><br />
<!-- TEXTAREA: defaultValue, disabled, readonly, style, styleClass,
        tabindex
-->
<netui:textArea dataSource="actionForm.textArea"
        cols="20" rows="5"
        onBlur="print_blur(textAreaId);"
        onChange="print_change(textAreaId);"
        onClick="print_click(textAreaId);"
        onDblClick="print_dblClick(textAreaId);"
        onFocus="print_focus(textAreaId);"
        onKeyDown="print_keyDown(textAreaId);"
        onKeyPress="print_keyPress(textAreaId);"
        onKeyUp="print_keyUp(textAreaId);"
        onMouseDown="print_mouseDown(textAreaId);"
        onMouseMove="print_mouseMove(textAreaId);"
        onMouseOver="print_mouseOver(textAreaId);"
        onMouseOut="print_mouseOut(textAreaId);"
        onMouseUp="print_mouseUp(textAreaId);"
        onSelect="print_select(textAreaId);"

/><br />
<!-- CHECKBOXGROUP: defaultValue, optionsDataSource,style, styleClass
-->
<netui:checkBoxGroup dataSource="actionForm.checkBoxGroup">
<!-- CHECKBOXOPTION: style, styleClass, tabindex
-->
<netui:checkBoxOption value="foo"
        onBlur="print_blur(checkboxGroupId);"
        onChange="print_change(checkboxGroupId);"
        onClick="print_click(checkboxGroupId);"
        onDblClick="print_dblClick(checkboxGroupId);"
        onFocus="print_focus(checkboxGroupId);"
        onKeyDown="print_keyDown(checkboxGroupId);"
        onKeyPress="print_keyPress(checkboxGroupId);"
        onKeyUp="print_keyUp(checkboxGroupId);"
        onMouseDown="print_mouseDown(checkboxGroupId);"
        onMouseMove="print_mouseMove(checkboxGroupId);"
        onMouseOut="print_mouseOut(checkboxGroupId);"
        onMouseOver="print_mouseOver(checkboxGroupId);"
        onMouseUp="print_mouseUp(checkboxGroupId);"
        onSelect="print_select(checkboxGroupId);"
/>
</netui:checkBoxGroup><br />
<!-- LABEL: dataSource, dataformatas, style, styleClass, tabindex
-->
<netui:span value="Checkbox 1"
        onClick="print_click(labelId);"
        onKeyDown="print_keyDown(labelId);"
        onKeyPress="print_keyPress(labelId);"
        onKeyUp="print_keyUp(labelId);"
        onMouseDown="print_mouseDown(labelId);"
        onMouseMove="print_mouseMove(labelId);"
        onMouseOut="print_mouseOut(labelId);"
        onMouseOver="print_mouseOver(labelId);"
        onMouseUp="print_mouseUp(labelId);"
/>
<!-- CHECKBOX: defaultValue, style, styleClass, tabindex
-->
<netui:checkBox
        dataSource="actionForm.checkBox1"
        onBlur="print_blur(checkboxId);"
        onChange="print_change(checkboxId);"
        onClick="print_click(checkboxId);"
        onDblClick="print_dblClick(checkboxId);"
        onFocus="print_focus(checkboxId);"
        onKeyDown="print_keyDown(checkboxId);"
        onKeyPress="print_keyPress(checkboxId);"
        onKeyUp="print_keyUp(checkboxId);"
        onMouseDown="print_mouseDown(checkboxId);"
        onMouseMove="print_mouseMove(checkboxId);"
        onMouseOut="print_mouseOut(checkboxId);"
        onMouseOver="print_mouseOver(checkboxId);"
        onMouseUp="print_mouseUp(checkboxId);"
        onSelect="print_select(checkboxId);"
/><br />
<!-- RADIOBUTTONGROUP: defaultValue, optionsDataSource, style, styleClass
-->
<netui:radioButtonGroup dataSource="actionForm.radio">
<!-- RADIOBUTTONOPTION: style, styleClass, tabindex
-->
<netui:radioButtonOption value="1"
        onBlur="print_blur(radioId);"
        onChange="print_change(radioId);"
        onClick="print_click(radioId);"
        onFocus="print_focus(radioId);"
        onKeyDown="print_keyDown(radioId);"
        onKeyPress="print_keyPress(radioId);"
        onKeyUp="print_keyUp(radioId);"
        onMouseDown="print_mouseDown(radioId);"
        onMouseMove="print_mouseMove(radioId);"
        onMouseOut="print_mouseOut(radioId);"
        onMouseOver="print_mouseOver(radioId);"
        onMouseUp="print_mouseUp(radioId);"
        onSelect="print_select(radioId);"
>
Choice 1</netui:radioButtonOption><br>
   <netui:radioButtonOption value="2">Choice 2</netui:radioButtonOption><br>
</netui:radioButtonGroup><br />
<!-- SELECT: defaultValue, multiple, optionsDataSource, style, styleClass
-->
<netui:select 
        dataSource="actionForm.select"
        size="3"
        onBlur="print_blur(selectId);"
        onChange="print_change(selectId);"
        onClick="print_click(selectId);"
        onFocus="print_focus(selectId);"
        onKeyDown="print_keyDown(selectId);"
        onKeyPress="print_keyPress(selectId);"
        onKeyUp="print_keyUp(selectId);"
        onMouseDown="print_mouseDown(selectId);"
        onMouseMove="print_mouseMove(selectId);"
        onMouseOut="print_mouseOut(selectId);"
        onMouseOver="print_mouseOver(selectId);"
        onMouseUp="print_mouseUp(selectId);"
>
<!-- SELECTOPTION, locale, style, styleClass, tabindex
-->
<netui:selectOption value="1"
        onClick="print_click(selectOptionId);"
        onKeyDown="print_keyDown(selectOptionId);"
        onKeyPress="print_keyPress(selectOptionId);"
        onKeyUp="print_keyUp(selectOptionId);"
        onMouseDown="print_mouseDown(selectOptionId);"
        onMouseMove="print_mouseMove(selectOptionId);"
        onMouseOut="print_mouseOut(selectOptionId);"
        onMouseOver="print_mouseOver(selectOptionId);"
        onMouseUp="print_mouseUp(selectOptionId);"
        value="Choice 1"
/>
<netui:selectOption value="2">Choice 2</netui:selectOption>
<netui:selectOption value="3" disabled="true">Choice 3</netui:selectOption>
</netui:select><br />
<!-- HIDDEN: style, styleClass, tabindex
-->
<netui:hidden dataSource="actionForm.hidden"
/>
<hr />
<netui:button type="reset">Reset</netui:button>
<!-- BUTTON: action, style, styleClass, tabindex, type, value
-->
<netui:button type="submit"
        value="submit"
        onBlur="print_blur(buttonId);"
        onChange="print_change(buttonId);"
        onClick="print_click(buttonId);"
        onFocus="print_focus(buttonId);"
        onKeyDown="print_keyDown(buttonId);"
        onKeyPress="print_keyPress(buttonId);"
        onKeyUp="print_keyUp(buttonId);"
        onMouseDown="print_mouseDown(buttonId);"
        onMouseMove="print_mouseMove(buttonId);"
        onMouseMove="print_mouseOut(buttonId);"
        onMouseMove="print_mouseOver(buttonId);"
        onMouseUp="print_mouseUp(buttonId);"
        onSelect="print_select(buttonId);"
/>
<br />
<!-- IMAGEBUTTON: page, style, styleClass, tabIndex, value
        (Internally the roll over stuff uses mouse over and mouse out)
-->
<netui:imageButton
        align="top"
        src="/coreWeb/images/godzilla.gif"
        rolloverImage="/coreWeb/images/godzillaRollover.gif"
        onBlur="print_blur(imageButtonId);"
        onChange="print_change(imageButtonId);"
        onClick="print_click(imageButtonId);"
        onKeyDown="print_keyDown(imageButtonId);"
        onKeyPress="print_keyPress(imageButtonId);"
        onKeyUp="print_keyUp(imageButtonId);"
        onMouseDown="print_mouseDown(imageButtonId);"
        onMouseUp="print_mouseUp(imageButtonId);"
        onMouseMove="print_mouseMove(imageButtonId);"
        onSelect="print_select(imageButtonId);"
/>
</netui:form>
<hr />
<!-- ANCHOR: formSubmit, forward, href, linkName, location, page, styleClass,
        tabindex, target
-->
<netui:anchor action="postback"
        onBlur="print_blur(anchorId);"
        onClick="print_click(anchorId);"
        onFocus="print_focus(anchorId);" 
        onKeyDown="print_keyDown(anchorId);"
        onKeyPress="print_keyPress(anchorId);"
        onKeyUp="print_keyUp(anchorId);"
        onMouseDown="print_mouseDown(anchorId);"
        onMouseMove="print_mouseMove(anchorId);"
        onMouseOut="print_mouseOut(anchorId);"
        onMouseOver="print_mouseOver(anchorId);"
        onMouseUp="print_mouseUp(anchorId);"
>Post Back</netui:anchor><br />
<!-- IMAGE: isMap, location, lowsrc, page, scope, style, styleClass, tabIndex
        usemap
-->
<netui:image src="/coreWeb/images/godzilla.gif"
        align="center" 
        border="2"
        tagId="Godzilla"
        hspace="10" vspace="10"
        height="66" width="48"
        onClick="print_click(imageId);"
        onKeyDown="print_keyDown(imageId);"
        onKeyPress="print_keyPress(imageId);"
        onKeyUp="print_keyUp(imageId);"
        onMouseDown="print_mouseDown(imageId);"
        onMouseMove="print_mouseMove(imageId);"
        onMouseOver="print_mouseOut(imageId);"
        onMouseOver="print_mouseOver(imageId);"
        onMouseUp="print_mouseUp(imageId);"
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
        border="6"
        hspace="10" vspace="10"
        height="66" width="48"
        tagId="Godzilla"
        onBlur="print_blur(anchorAnchorId);"
        onClick="print_click(imageAnchorId);"
        onKeyDown="print_keyDown(imageAnchorId);"
        onKeyPress="print_keyPress(imageAnchorId);"
        onKeyUp="print_keyUp(imageAnchorId);"
        onMouseDown="print_mouseDown(imageAnchorId);"
        onMouseUp="print_mouseUp(imageAnchorId);"
        onMouseMove="print_mouseMove(imageAnchorId);"
/>
</td><td>
<p id="formOut_operation">Operation:</p>
<p id="formOut">Form:</p>
<p id="text">Text:</p>
<p id="textArea">TextArea:</p>
<p id="checkboxGroup">Checkbox Group:</p>
<p id="label">Label:</p>
<p id="checkbox">Checkbox:</p>
<p id="radio">Radio:</p>
<p id="select">Select:</p>
<p id="selectOption">Select Option:</p>
<p id="hidden">Hidden:</p>
<p id="button">Button:</p>
<p id="imageButton">ImageButton:</p>
<p id="anchor">Anchor:</p>
<p id="image">Image:</p>
<p id="imageAnchor">ImageAnchor:</p>
</td></tr></table>
</body>
</html>
