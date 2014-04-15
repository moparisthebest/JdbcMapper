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
import org.apache.beehive.netui.simpletags.behaviors.TextBoxBehavior;
import org.apache.beehive.netui.simpletags.html.IHtmlAccessable;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

/**
 * Renders a databound TextBox with the given attributes.
 * @jsptagref.tagdescription Renders an HTML &lt;input type="text"> tag.
 * @example In this sample, a &lt;netui:textBox> reads from and writes to the Form Bean's firstname property.
 * If the submitted text is NULL, the default value is specified by the Page Flow's firstName property.
 * <pre>     &lt;netui:textBox dataSource="actionForm.firstName"
 *           defaultValue="${pageFlow.defaultFirstName}"
 *           size="20" /></pre>
 * @netui:tag name="textBox" body-content="scriptless" dynamic-attributes="true" description="Renders a databound TextBox with the given attributes."
 */
public class TextBox extends HtmlDefaultableDataSourceTag
        implements IHtmlAccessable
{
    public TextBox() {
        _behavior = new TextBoxBehavior();;
    }

    //********************************  ATTRIBUTES *******************************

    /**
     * Set the maximum length (in characters) of the TextBox.
     * @param maxlength the max length
     * @jsptagref.attributedescription Integer. The maximum number of character that can be entered in the rendered &lt;input> element.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_maxLength</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The maximum number of character that can be entered in the rendered <input> element."
     */
    public void setMaxlength(int maxlength)
    {
        ((TextBoxBehavior) _behavior).setMaxlength(maxlength);
    }

    /**
     * Set the password state (true means this is a password field).
     * @param password the password state
     * @jsptagref.attributedescription Boolean. Determines whether the password characters that the user enters into the &lt;input> element will be disguised in the browser.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_password</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines whether the password characters that the user enters into the &lt;input> element will be disguised in the browser."
     */
    public void setPassword(boolean password)
    {
        ((TextBoxBehavior) _behavior).setPassword(password);
    }

    /**
     * Set if this TextBox is read-only.
     * @param readonly the read-only state
     * @jsptagref.attributedescription Boolean. Determines if the rendered &lt;input> element is read-only.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_readOnly</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines if the rendered &lt;input> element is read-only"
     */
    public void setReadonly(boolean readonly)
    {
        ((TextBoxBehavior) _behavior).setReadonly(readonly);
    }

    /**
     * Set the size (in characters) of the TextBox.
     * @param size the size
     * @jsptagref.attributedescription Integer. The number of characters visible in the &lt;input> element.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>integer_size</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The number of characters visible in the <input> element."
     */
    public void setSize(int size)
    {
        ((TextBoxBehavior) _behavior).setSize(size);
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
     * @netui:attribute required="false" rtexprvalue="true" type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        ((TextBoxBehavior) _behavior).setAccessKey(accessKey);
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
        ((TextBoxBehavior) _behavior).setAlt(alt);
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.databindable false
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key.
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        ((TextBoxBehavior) _behavior).setTabindex(tabindex);
    }

    //*************************************** TAG METHODS *****************************************

    /**
     * Render the TextBox.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();

        // it is always required to set the variable resolver on a datasource.
        ((TextBoxBehavior) _behavior).setVariableResolver(getPageContext().getVariableResolver());

        // process the body so formatters can be applied to the text
        ServletResponse response = getPageContext().getResponse();
        Appender appender = new ResponseAppender(getPageContext().getResponse());

        JspFragment frag = getJspBody();
        if (frag != null) {
            frag.invoke(response.getWriter());
        }

        _behavior.preRender();
        _behavior.renderStart(appender);

        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
