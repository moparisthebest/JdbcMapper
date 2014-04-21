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

import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * A formatter used to format numbers.  This formatter uses patterns that conform to
 * <code>java.text.NumberFormat</code> pattern syntax.  FormatNumber calls <code>toString()</code> on
 * the object to be formatted to get the value the pattern is applied to.
 *
 * The valid FormatNumber types are:
 * <ul>
 * <li>number</li>
 * <li>currency</li>
 * <li>percent</li>
 * </ul>
 * @jsptagref.tagdescription A formatter used to format numbers.
 *
 * <p>The &lt;netui:formatNumber> tag formats the output of its parent tag. For example:
 *
 * <pre>    &lt;netui:span value="${pageFlow.price}">
 *        &lt;netui:formatNumber country="FR" language="fr" type="currency" />
 *    &lt;/netui:span></pre>
 *
 * <p>The <code>pattern</code> attribute conforms to
 * {@link java.text.DecimalFormat java.text.DecimalFormat} pattern syntax.
 *
 * <p>The <code>pattern</code> attribute uses the comma as a grouping separater.
 * If many different grouping sizes are specified in one <code>pattern</code>,
 * the right-most grouping interval will be used throughout; the other grouping intervals
 * will be ignored.  For example, the following format patterns all produce the same result.
 * If the number to format is 123456789, each will produce 123,456,789.
 * <blockquote>
 * <ul>
 * <li>pattern="#,##,###,###"</li>
 * <li>pattern="######,###"</li>
 * <li>pattern="##,####,###"</li>
 * </ul>
 * </blockquote>
 *
 * <p>The <code>type</code> attribute specifies three common
 * kinds of formatting to be applied to the number.
 * The valid values for the <code>type</code> attribute are:
 * <blockquote>
 * <ul>
 * <li><code>number</code></li>
 * <li><code>currency</code></li>
 * <li><code>percent</code></li>
 * </ul>
 * </blockquote>
 *
 * <p>The <code>country</code> attribute takes an upper-case,
 * two-letter code as defined by ISO-3166.
 *
 * <p>The <code>language</code> attribute takes a lower-case,
 * two-letter code as defined by ISO-639.
 * @example In this first example, the value "12345678" is formatted
 * to 12,345,678.00.
 * <pre>    &lt;netui:span value="12345678">
 *        &lt;netui:formatNumber pattern="#,###.00" />
 *    &lt;/netui:span></pre>
 *
 * <p>In the next sample, the value ".33" is formatted to 33%.</p>
 * <pre>    &lt;netui:span value=".33">
 *        &lt;netui:formatNumber type="percent" />
 *    &lt;/netui:span></pre>
 *
 * <p>In the next sample, the value "14.317" is formatted
 * to $14.32.</p>
 * <pre>    &lt;netui:span value="14.317">
 *        &lt;netui:formatNumber country="US" language="en" type="currency" />
 *    &lt;/netui:span></pre>
 * @netui:tag name="formatNumber" body-content="empty" description="A formatter used to format numbers."
 */
public class FormatNumber extends FormatTag
{
    /**
     * The type of number format to be used.
     */
    protected String _type;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "FormatNumber";
    }

    /**
     * Sets the type of number format to be used (number, currency, or percent).
     * @param type the number format type.
     * @jsptagref.attributedescription The type of the format to apply. Possible values are <code>number</code>, <code>currency</code>, or <code>percent</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The type of the format to apply. Possible values are number, currency, or percent."
     */
    public void setType(String type)
            throws JspException
    {
        _type = setRequiredValueAttribute(type, "type");
        if (_type != null) {
            if (!type.equals("number") && !type.equals("currency") && !type.equals("percent")) {
                String s = Bundle.getString("Tags_NumberFormatWrongType");
                registerTagError(s, null);
            }
        }
    }

    /**
     * Create the internal Formatter instance and perform the formatting.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        JspTag parentTag = SimpleTagSupport.findAncestorWithClass(this, IFormattable.class);

        // if there are errors we need to either add these to the parent AbstractBastTag or report an error.
        if (hasErrors()) {
            if (parentTag instanceof IFormattable) {
                IFormattable parent = (IFormattable) parentTag;
                parent.formatterHasError();
            }
            reportErrors();
            return;
        }

        // if there are no errors then add this to the parent as a formatter.
        if (parentTag instanceof IFormattable) {
            NumberFormatter formatter = new NumberFormatter();
            formatter.setPattern(_pattern);
            formatter.setType(_type);
            formatter.setLocale(getLocale());
            IFormattable parent = (IFormattable) parentTag;
            parent.addFormatter(formatter);
        }
        else {
            String s = Bundle.getString("Tags_FormattableParentRequired");
            registerTagError(s, null);
            reportErrors();
        }
    }

    /**
     * Internal FormatTag.Formatter which uses NumberFormat.
     */
    public static class NumberFormatter extends FormatTag.Formatter
    {
        private String type;
        private Locale locale;

        public void setType(String type)
        {
            this.type = type;
        }

        public void setLocale(Locale locale)
        {
            this.locale = locale;
        }

        public String format(Object dataToFormat) throws JspException
        {
            if (dataToFormat == null) {
                return null;
            }
            InternalStringBuilder formattedString = new InternalStringBuilder(32);
            DecimalFormat numberFormat = null;

            // get the number format.  The type has been validated when it was set on the tag.
            if (locale == null) {
                if ((type == null) || (type.equals("number"))) {
                    numberFormat = (DecimalFormat) java.text.NumberFormat.getNumberInstance();
                }
                else if (type.equals("currency")) {
                    numberFormat = (DecimalFormat) java.text.NumberFormat.getCurrencyInstance();
                }
                else if (type.equals("percent")) {
                    numberFormat = (DecimalFormat) java.text.NumberFormat.getPercentInstance();
                }
                else {
                    assert(false) : "Invalid type was found:" + type;
                }
            }
            else {
                if ((type == null) || (type.equals("number"))) {
                    numberFormat = (DecimalFormat) java.text.NumberFormat.getNumberInstance(locale);
                }
                else if (type.equals("currency")) {
                    numberFormat = (DecimalFormat) java.text.NumberFormat.getCurrencyInstance(locale);
                }
                else if (type.equals("percent")) {
                    numberFormat = (DecimalFormat) java.text.NumberFormat.getPercentInstance(locale);
                }
                else {
                    assert(false) : "Invalid type was found:" + type;
                }
            }

            // format the number, apply the pattern specified
            try {
                if (getPattern() != null)
                    numberFormat.applyPattern(getPattern());
            }
            catch (Exception e) {
                JspException jspException = new JspException(Bundle.getString("Tags_NumberFormatPatternException", e.getMessage()), e);

                // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
                // initCause() throws an IllegalStateException if the cause is already set.
                if (jspException.getCause() == null) {
                    jspException.initCause(e);
                }
                throw jspException;
            }

            // parse the number
            if (dataToFormat.toString().length() == 0) {
                return "";
            }
            try {
                double number = Double.parseDouble(dataToFormat.toString());
                formattedString.append(numberFormat.format(number));
            }
            catch (Exception e) {
                JspException jspException = new JspException(Bundle.getString("Tags_NumberFormatParseException", e.getMessage()), e);

                // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
                // initCause() throws an IllegalStateException if the cause is already set.
                if (jspException.getCause() == null) {
                    jspException.initCause(e);
                }
                throw jspException;
            }

            return formattedString.toString();

        }
    }
}
