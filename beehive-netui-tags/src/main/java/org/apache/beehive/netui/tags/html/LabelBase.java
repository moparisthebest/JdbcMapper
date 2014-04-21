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
package org.apache.beehive.netui.tags.html;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;

/**
 * This is a base class providing implementation for both the Label and FormLabel.
 */
public abstract class LabelBase extends HtmlBaseTag
        implements IFormattable
{
    protected static final String DEFAULT_NULL_TEXT = "";

    protected Object _defaultValue;               // The attribute value of the defaultValue attribute
    protected Object _value;                      // The text of the Label.
    protected boolean _escapeWhiteSpace = true;   // escape white space flag
    protected boolean _formatterErrors = false;  // The formatter has errors.
    protected boolean _formatDefaultValue = false; // Format the defaultValue
    private ArrayList _formatters;               // The formatters

    /**
     * Set the default value of this Label.
     * This can be an expression.  If the default value is an expression
     * all formatters will be applied, otherwise the default value will be output
     * without being formatted.
     * @param defaultValue the default value
     * @jsptagref.attributedescription The String literal or expression to be used as the
     * default output. If the default value is an expression all formatters will be applied,
     * otherwise the default value will be output without being formatted.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_defaultValue</i>
     * @netui:attribute required="false" rtexprvalue="true" type="java.lang.Object"
     * description="Set the default value of this Label or Span."
     */
    public void setDefaultValue(Object defaultValue)
    {
        _defaultValue = defaultValue;
    }

    /**
     * Boolean indicating whether the formatter should be applied to the defaultValue.
     * The default is "false" meaning formatters will not be applied.
     * @param formatDisplay Apply formatting to the default value.
     * @jsptagref.attributedescription Boolean indicating whether the formatter should be applied
     * to the defaultValue. The default is "false" meaning formatters will not be applied.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue boolean_formatDisplay
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Apply formatting to the default value of this Label or Span."
     */
    public void setFormatDefaultValue(boolean formatDisplay)
    {
        _formatDefaultValue = formatDisplay;
    }

    /**
     * Sets the text of the Label.
     * @param value the Label value or expression.
     * @jsptagref.attributedescription The String literal or expression used as the text of the Label.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_output</i>
     * @netui:attribute required="true" rtexprvalue="true" type="java.lang.Object"
     * description="Sets the text of the Label or Span."
     */
    public void setValue(Object value)
    {
        _value = value;
    }

    /**
     * Sets a <code>boolean</code> flag indicating if we will escape
     * white space for HTML.  If this is <code>true</code> the white space
     * charcters ' ' will be converted into "&nbsp;" and '\n' converted into
     * "<br />".  The result is that in HTML white space will be represented
     * correctly.  If this is <code>false</code> then white space will be
     * output as it is found in the value.
     * @param escapeWhiteSpace boolean indicating if we are escaping for white space.
     * @jsptagref.attributedescription Sets a boolean flag indicating if we will escape
     * white space for HTML.
     * @jsptagref.attributesyntaxvalue <i>boolean_escapeWhiteSpace</i>
     * @netui:attribute required="false" rtexprvalue="true"  type="boolean"
     * description="Sets a boolean flag indicating if we will escape
     * white space for HTML."
     */
    public void setEscapeWhiteSpaceForHtml(boolean escapeWhiteSpace)
    {
        _escapeWhiteSpace = escapeWhiteSpace;

    }

    /**
     * Adds a FormatTag.Formatter to the Label's set of formatters
     * @param formatter a FormatTag.Formatter added by a child FormatTag.
     */
    public void addFormatter(FormatTag.Formatter formatter)
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
        _formatterErrors = true;
    }

    /**
     * Filter the specified string for characters that are senstive to
     * HTML interpreters, returning the string with these characters replaced
     * by the corresponding character entities.
     * @param value                 The string to be filtered and returned
     * @param markupHTMLSpaceReturn convert space characters and return characters
     *                              to &amp;nbsp; and &lt;br /&gt; marketup for html.
     */
    protected void filter(String value, AbstractRenderAppender writer, boolean markupHTMLSpaceReturn)
    {
        if (value.equals(" ")) {
            writer.append("&nbsp;");
            return;
        }

        HtmlUtils.filter(value, writer, markupHTMLSpaceReturn);
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _defaultValue = null;
        _escapeWhiteSpace = true;
        _formatters = null;
        _formatterErrors = false;
        _formatDefaultValue = false;
        _value = null;
    }

    /**
     *
     */
    protected String formatText(Object text)
            throws JspException
    {
        InternalStringBuilder errors = null;
        if (text == null)
            return null;

        if (_formatters == null)
            return text.toString();

        for (int i = 0; i < _formatters.size(); i++) {
            FormatTag.Formatter currentFormatter = (FormatTag.Formatter) _formatters.get(i);

            // if there are errors in the formatter, we need to report them
            // and continue to the next one.
            if (currentFormatter.hasError()) {
                if (errors == null) {
                    errors = new InternalStringBuilder(32);
                }
                assert(errors != null);
                errors.append(currentFormatter.getErrorMessage());
                continue;
            }

            // apply the formatter.
            try {
                text = currentFormatter.format(text);
            }
            catch (JspException e) {
                registerTagError(e.getMessage(), e);
            }
        }
        // if there were errors we will return the errors followed by the text,
        // otherwise just return the text.
        if (errors != null) {
            return errors.toString() + text.toString();
        }
        return text.toString();
    }
}
