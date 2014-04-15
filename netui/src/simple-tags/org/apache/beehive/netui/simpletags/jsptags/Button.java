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

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.appender.ResponseAppender;
import org.apache.beehive.netui.simpletags.behaviors.ButtonBehavior;
import org.apache.beehive.netui.simpletags.html.IHtmlAccessable;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Generates a button on the page with the specified attributes.  The
 * &lt;netui:button> tag must be enclosed in &lt;netui:form...> ...
 * &lt;/netui:form> tags. You can specify the action that the form will
 * raise on the &lt;netui:form> tag.
 * @jsptagref.tagdescription Renders an &lt;input type="button"> tag with the specified attributes.
 * To submit data or invoke a method on the Controller file, the
 * &lt;netui:button> tag must have a parent {@link Form} tag.
 * The action attribute on the &lt;netui:button> is for the purpose
 * of overriding the action attribute on the enclosing &lt;netui:form> tag.
 * If no action attribute is specified on the &lt;netui:button> tag,
 * the action attribute
 * on the &lt;netui:form> tag will determine which action method is invoked.
 * @example In this sample, the &lt;netui:button> submits data to
 * the Controller file's <code>processData</code> action method (specified on the &lt;netui:form>'s action
 * attribute).
 * <pre>
 *     &lt;netui:form action="processData">
 *        &lt;!--
 *        input elements here
 *        -->
 *        &lt;netui:button value="Submit" type="submit"/>
 *     &lt;/netui:form></pre>
 * @netui:tag name="button" body-content="scriptless" dynamic-attributes="true" description="Create a button on your JSP page."
 */
public class Button extends HtmlFocusBaseTag implements IHtmlAccessable
{
    public Button() {
        _behavior = new ButtonBehavior();
    }

    /**
     * Set the name of the action for the Button.
     * @param action the name of the action to set for the Button.
     * @jsptagref.attributedescription The action method invoked.  The value of this attribute will override
     * the <code>action</code>
     * attribute of the parent &lt;netui:form> tag.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The action method invoked.  The value of this attribute will override
     * the action attribute of the parent <netui:form> tag."
     */
    public void setAction(String action)
            throws JspException
    {
        ((ButtonBehavior) _behavior).setAction(action);
    }

    /**
     * Set the target "scope" for the button's action.  Multiple active page flows may exist concurrently within named
     * scopes.  This attribute selects which named scope to use.  If omitted, the default scope is assumed.
     * @param targetScope the name of the target scope in which the associated action's page flow resides.
     * @jsptagref.attributedescription The target scope in which the associated action's page flow resides.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_targetScope</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The target scope in which the associated action's page flow resides"
     */
    public void setTargetScope(String targetScope)
    {
        ((ButtonBehavior) _behavior).setTargetScope(targetScope);
    }

    /**
     * Set the type of the Button (submit, button, or reset).
     * @param type the type of the Button.
     * @jsptagref.attributedescription The type of the button.  Possible values are <code>submit</code>, <code>button</code>, or <code>reset</code>.
     * The default value is <code>submit</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The type of the button.  Possible values are submit, button, or reset.
     * The default value is submit."
     */
    public void setType(String type)
            throws JspException
    {
        ((ButtonBehavior) _behavior).setType(type);
    }

    /**
     * Set the value of the Button's text.
     * @param value the value of the Button's text.
     * @jsptagref.attributedescription The text displayed by the rendered HTML button.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_value</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The text displayed by the rendered HTML button."
     */
    public void setValue(String value)
            throws JspException
    {
        ((ButtonBehavior) _behavior).setValue(setNonEmptyValueAttribute(value));
    }

    /**
     * Sets the popup indicator.
     * @param popup whether or not the button should open a popup window.
     * @jsptagref.attributedescription Boolean.  If <code>popup</code> is set to true,
     * the button will open a popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_popup</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="If popup is set to true, the button will open a popup window."
     */
    public void setPopup(boolean popup)
    {
        ((ButtonBehavior) _behavior).setPopup(popup);
    }

    /**
     * When true, this button will disable itself before submitting.
     * @param disableSecondClick when true, this button will disable itself before submitting.
     * @jsptagref.attributedescription Boolean.  If <code>disableSecondClick</code> is set to true,
     * the button will disable itself before submitting.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_disableSecondClick</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="When true, this button will disable itself before submitting."
     */
    public void setDisableSecondClick(boolean disableSecondClick)
    {
        ((ButtonBehavior) _behavior).setDisableSecondClick(disableSecondClick);
    }


    /**
     * Sets the accessKey attribute value.  This should key value of the
     * keyboard navigation key.  It is recommended not to use the following
     * values because there are often used by browsers <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>.
     * @param accessKey the accessKey value.
     * @jsptagref.attributedescription The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_accessKey</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        if (accessKey == 0x00)
            return;
        ((ButtonBehavior) _behavior).setAccessKey(accessKey);
    }

    /**
     * Sets the alt attribute value.
     * @param alt the alt value.
     * @jsptagref.attributedescription The alt attribute of the element.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The alt attribute of the element."
     */
    public void setAlt(String alt)
    {
        ((ButtonBehavior) _behavior).setAlt(alt);
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        ((ButtonBehavior) _behavior).setTabindex(tabindex);
    }

    /**
     * Render the button.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();

        // The body will override the value if the body is specified.
        String value = getBufferBody(true);
        ((ButtonBehavior) _behavior).setValue(value);

        Appender appender = new ResponseAppender(getPageContext().getResponse());
        _behavior.preRender();
        _behavior.renderStart(appender);
        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
