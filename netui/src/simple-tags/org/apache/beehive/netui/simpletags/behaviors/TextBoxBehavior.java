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
package org.apache.beehive.netui.simpletags.behaviors;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.appender.StringBuilderAppender;
import org.apache.beehive.netui.simpletags.core.IFormattable;
import org.apache.beehive.netui.simpletags.html.HtmlUtils;
import org.apache.beehive.netui.simpletags.html.IHtmlAccessable;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.InputTextTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.behaviors.formatting.Formatter;
import org.apache.beehive.netui.simpletags.behaviors.formatting.FormatterException;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.ArrayList;

public class TextBoxBehavior extends HtmlDefaultableDataSourceBehavior
    implements IFormattable, IHtmlAccessable
{
    private InputTextTag.State _state = new InputTextTag.State();

    private ArrayList _formatters;
    private boolean _password = false;
    private boolean _formatErrors = false;
    private String _bodyContent;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "TextBox";
    }

    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    protected AbstractHtmlState getState()
    {
        return _state;
    }

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>type</code>,
     * and <code>value</code> attributes.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     */
    public void setAttribute(String name, String value, String facet)
    {
        if (name != null) {
            if (name.equals(TYPE) || name.equals(VALUE)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
            else {
                if (name.equals(DISABLED)) {
                    _state.disabled = new Boolean(value).booleanValue();
                    return;
                }
                else if (name.equals(READONLY)) {
                    _state.readonly = new Boolean(value).booleanValue();
                    return;
                }
                else if (name.equals(MAXLENGTH)) {
                    _state.maxlength = Integer.parseInt(value);
                    return;
                }
                else if (name.equals(SIZE)) {
                    _state.size = Integer.parseInt(value);
                    return;
                }
            }
        }

        super.setAttribute(name, value, facet);
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
        _state.maxlength = maxlength;
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
        _password = password;
    }

    /**
     * Get the password state.
     * @return <code>true</code> if the text box is used for passwords.  <code>false</code> otherwise.
     */
    protected boolean getPassword()
    {
        return _password;
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
        _state.readonly = readonly;
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
        _state.size = size;
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCESSKEY, Character.toString(accessKey));
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt);
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
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }

    public void preRender()
    {
        super.preRender();

        // Create an appropriate "input" element based on our parameters
        if (_password) {
            _state.type = INPUT_PASSWORD;
        }
        else {
            _state.type = INPUT_TEXT;
        }

        // Create the state for the input tag
        nameHtmlControl(_state);

        _state.disabled = isDisabled();
        // Create the text value of the TextBox
        Object textObject = evaluateDataSource();
        if ((textObject == null) || (textObject.toString().length() == 0)) {
            textObject = _defaultValue;
        }

        // Get the text value for the textbox, the result
        String text = null;
        if (textObject != null) {
            text = formatText(textObject);
            InternalStringBuilder sb = new InternalStringBuilder(text.length() + 16);
            Appender sbAppend = new StringBuilderAppender(sb);
            HtmlUtils.filter(text, sbAppend);
            text = sb.toString();
        }
        _state.value = text;

    }

    public void renderStart(Appender appender)
    {
        // if there were format errors then report them
        // @todo: how do we do this?
        if (_formatErrors) {
            if (_bodyContent != null) {
                String value = _bodyContent.trim();
                appender.append(value);
            }
        }
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        // create the input tag.
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_TEXT_TAG);
        assert(br != null) : "Renderer for Textbox was not found.";

        br.doStartTag(appender, _state);
    }

    public void renderEnd(Appender appender)
    {
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_TEXT_TAG);
        assert(br != null);

        br.doEndTag(appender);
    }

    //*************************************** TAG METHODS *****************************************

    /**
     * Adds a FormatTag.Formatter to the TextBox's set of formatters
     * @param formatter a FormatTag.Formatter added by a child FormatTag.
     */
    public void addFormatter(Formatter formatter)
    {
        if (_formatters == null)
            _formatters = new ArrayList();

        _formatters.add(formatter);
    }

    /**
     * Indicate that a formatter has reported an error so the formatter should output it's
     * body text.
     */
    public void formatterHasError()
    {
        _formatErrors = true;
    }

    /**
     * Apply the formatters to the text to produce the text that will be output.
     */
    private String formatText(Object text)
    {
        assert(text != null) : "parameter 'text' must not be null";

        if (_formatters == null)
            return text.toString();

        for (int i = 0; i < _formatters.size(); i++) {
            Formatter currentFormatter = (Formatter) _formatters.get(i);
            try {
                text = currentFormatter.format(text);
            }
            catch (FormatterException e) {
                registerTagError(e.getMessage(), null);
            }
        }
        return text.toString();
    }
}
