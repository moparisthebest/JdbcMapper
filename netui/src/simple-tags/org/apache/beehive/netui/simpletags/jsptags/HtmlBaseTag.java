/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.behaviors.HtmlBaseBehavior;
import org.apache.beehive.netui.simpletags.html.HtmlConstants;
import org.apache.beehive.netui.simpletags.html.IHtmlAttrs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

/**
 * [Base] Anchor, Form, Image, ImageAnchor (Image), Label, SelectOption
 * [FocusBase] Button, CheckBoxOption, ImageButton, RadioButtonOption
 * [DataSource] FileUpload
 * [DefaultableDatSource] CheckBox, TextArea, TextBox
 * [OptionsDataSource] Select
 * [GroupDataSource] CheckBoxGroup, RadioButtonGroup
 */
abstract public class HtmlBaseTag extends AbstractSimpleTag
        implements HtmlConstants, DynamicAttributes, IHtmlAttrs
{
    //***************************** The IHtmlCore properties *********************************/

    /**
     * Sets the style of the rendered html tag.
     * @param style the html style.
     * @jsptagref.attributedescription Specifies style information for the current element.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Specifies style information for the current element."
     */
    public void setStyle(String style)
    {
        ((HtmlBaseBehavior) _behavior).setStyle(style);
    }

    /**
     * Sets the style class of the rendered html tag.
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class (a style sheet selector).
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_styleClass</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The style class (a style sheet selector)."
     */
    public void setStyleClass(String styleClass)
    {
        ((HtmlBaseBehavior) _behavior).setStyleClass(styleClass);
    }

    /**
     * Set the ID of the tag.
     * @param tagId the tagId. A value is required.
     * @jsptagref.attributedescription <p>String value. Sets the <code>id</code> (or <code>name</code>) attribute of the rendered HTML tag.
     * Note that the real id attribute rendered in the browser may be
     * changed by the application container (for example, Portal containers may change
     * the rendered id value to ensure the uniqueness of
     * id's on the page). In this case, the real id rendered
     * in the browser may be looked up
     * through the JavaScript function <code>lookupIdByTagId( tagId, tag )</code>.
     *
     * <p>For example, assume that some tag's <code>tagId</code> attribute is set to <code>foo</code>.
     *
     * <pre>    &lt;netui:textBox <b>tagId="foo"</b> /></pre>
     *
     * <p>Then the following JavaScript function will return the real id attribute rendered in the browser:
     *
     * <pre>    lookupIdByTagId( "foo", this )</pre>
     *
     * <p>To get a &lt;netui:form> element and all of its children elements in JavaScript, use
     * the same JavaScript function <code>lookupIdByTagId( tagId, tag )</code>.  For example,
     * assume that there is a &lt;netui:form> whose
     * tagId attribute is set to <code>bar</code>.
     *
     * <pre>    &lt;netui:form <b>tagId="bar"</b> ></pre>
     *
     * <p>Then the following JavaScript function will return the &lt;netui:form> element
     * and its children (packaged as an array).
     *
     * <pre>    document[lookupIdByTagId( "bar", this )]</pre>
     *
     * <p>To retreive the value entered into a &lt;netui:textBox> within the &lt;netui:form> tag, use the following
     * JavaScript expression.
     *
     * <pre>    document[lookupIdByTagId("bar", this)][lookupIdByTagId("foo", this)].value</pre>
     *
     * <p>The second parameter ensures that the JavaScript function
     * begins its search within the correct Portlet scope.  Pass the
     * JavaScript keyword <code>this</code> as the second parameter.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
            throws JspException
    {
        ((HtmlBaseBehavior) _behavior).setTagId(setRequiredValueAttribute(tagId, "tagId"));
    }

    /**
     * Return the ID of the tag.  The id may be rewritten by the container (such
     * as a portal) to make sure it is unique.  JavaScript my lookup the actual id
     * of the element by looking it up in the <code>netui_names</code> table written
     * into the HTML.
     * @return the tagId.
     */
    public String getTagId()
    {
        return ((HtmlBaseBehavior) _behavior).getTagId();
    }

    /**
     * Sets the value of the title attribute.
     * @param title
     * @jsptagref.attributedescription The title.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The title. "
     */
    public void setTitle(String title)
    {
        ((HtmlBaseBehavior) _behavior).setTitle(title);
    }

    /******************** the HtmlI18n properties ******************************************/
    /**
     * Sets the lang attribute for the HTML element.
     * @param lang
     * @jsptagref.attributedescription Sets the language code for the base language of an
     * element's attribute values and text content.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the language code for the base language of an element's attribute values and text content."
     */
    public void setLang(String lang)
    {
        ((HtmlBaseBehavior) _behavior).setLang(lang);
    }

    /**
     * Sets the dir attribute for the HTML element.
     * @param dir
     * @jsptagref.attributedescription Specifies the direction of text. (<code>LTR | RTL</code>)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Specifies the direction of text. (LTR | RTL)"
     */
    public void setDir(String dir)
    {
        ((HtmlBaseBehavior) _behavior).setDir(dir);
    }

    //******************* the HtmlEvent Properties  ****************************************
    /**
     * Gets the onClick javascript event.
     * @return the onClick event.
     */
    public String getOnClick()
    {
        return ((HtmlBaseBehavior) _behavior).getOnClick();
    }

    /**
     * Sets the onClick javascript event.
     * @param onclick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onClick JavaScript event."
     */
    public void setOnClick(String onclick)
    {
        ((HtmlBaseBehavior) _behavior).setOnClick(onclick);
    }

    /**
     * Sets the onDblClick javascript event.
     * @param ondblclick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onDblClick JavaScript event."
     */
    public void setOnDblClick(String ondblclick)
    {
        ((HtmlBaseBehavior) _behavior).setOnDblClick(ondblclick);
    }

    /**
     * Sets the onKeyDown javascript event.
     * @param onkeydown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onKeyDown JavaScript event."
     */
    public void setOnKeyDown(String onkeydown)
    {
        ((HtmlBaseBehavior) _behavior).setOnKeyDown(onkeydown);
    }

    /**
     * Sets the onKeyPress javascript event.
     * @param onkeypress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onKeyPress JavaScript event."
     */
    public void setOnKeyPress(String onkeypress)
    {
        ((HtmlBaseBehavior) _behavior).setOnKeyPress(onkeypress);
    }

    /**
     * Sets the onKeyUp javascript event.
     * @param onkeyup the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onKeyUp JavaScript event."
     */
    public void setOnKeyUp(String onkeyup)
    {
        ((HtmlBaseBehavior) _behavior).setOnKeyUp(onkeyup);
    }

    /**
     * Sets the onMouseDown javascript event.
     * @param onmousedown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseDown JavaScript event."
     */
    public void setOnMouseDown(String onmousedown)
    {
        ((HtmlBaseBehavior) _behavior).setOnMouseDown(onmousedown);
    }

    /**
     * Sets the onMouseMove javascript event.
     * @param onmousemove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseMove JavaScript event."
     */
    public void setOnMouseMove(String onmousemove)
    {
        ((HtmlBaseBehavior) _behavior).setOnMouseMove(onmousemove);
    }

    /**
     * Sets the onMouseOut javascript event.
     * @param onmouseout the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseOut JavaScript event."
     */
    public void setOnMouseOut(String onmouseout)
    {
        ((HtmlBaseBehavior) _behavior).setOnMouseOut(onmouseout);
    }

    /**
     * Sets the onMouseOver javascript event.
     * @param onmouseover the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseOver JavaScript event."
     */
    public void setOnMouseOver(String onmouseover)
    {
        ((HtmlBaseBehavior) _behavior).setOnMouseOver(onmouseover);
    }

    /**
     * Sets the onMouseUp javascript event.
     * @param onmouseup the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseUp JavaScript event."
     */
    public void setOnMouseUp(String onmouseup)
    {
        ((HtmlBaseBehavior) _behavior).setOnMouseUp(onmouseup);
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException
    {
        ((HtmlBaseBehavior) _behavior).setAttribute(localName, value.toString(), uri);
    }
}
