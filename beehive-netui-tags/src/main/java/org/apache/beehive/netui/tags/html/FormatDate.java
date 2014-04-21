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
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

//external imports

/**
 * A formatter used to format dates.  This formatter uses patterns that conform to
 * <code>java.text.SimpleDateFormat</code> pattern syntax.  Valid types for formatting are:
 * <ul>
 * <li>String</li>
 * <li>java.sql.Date</li>
 * <li>java.util.Date</li>
 * <li>java.util.Calendar</li>
 * </ul>
 *
 * <p>
 * If the type is a String, we attempt to create a date out of the String.  The
 * String must be defined by a common format list below.  If the string is
 * equal the the empty string, it will be returned as the empty string.  See
 * java.text.SimpleDateFormat for more information.
 * <p>
 * The valid formats are:
 * <ul>
 * <li>MM/dd/yy</li>
 * <li>yyyy-MM-dd</li>
 * <li>MMddyy</li>
 * <li>and the local default</li>
 * </ul>
 * @jsptagref.tagdescription A formatter used to format dates.  This formatter uses patterns that conform to
 * {@link java.text.SimpleDateFormat java.text.SimpleDateFormat} pattern syntax.
 * Valid types for formatting are:
 * <blockquote>
 * <ul>
 * <li>String</li>
 * <li>java.sql.Date</li>
 * <li>java.util.Date</li>
 * <li>java.util.Calendar</li>
 * </ul>
 * </blockquote>
 *
 * <p>The &lt;netui:formatDate> tag formats the output of its parent tag.  For example:
 *
 * <pre>    &lt;netui:content value="${pageScope.euroDate}">
 *        &lt;netui:formatDate pattern="dd-MM-yyyy" />
 *    &lt;/netui:content></pre>
 *
 * <p>
 * The following table summarizes the pattern letters that can be used.
 * <table border=0 cellspacing=3 cellpadding=0>
 * <tr bgcolor="#ccccff">
 * <th align=left>Letter
 * <th align=left>Date or Time Component
 * <th align=left>Examples
 * <tr>
 * <td><code>G</code>
 * <td>Era designator
 * <td><code>AD</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>y</code>
 * <td>Year
 * <td><code>1996</code>; <code>96</code>
 * <tr>
 * <td><code>M</code>
 * <td>Month in year
 * <td><code>July</code>; <code>Jul</code>; <code>07</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>w</code>
 * <td>Week in year
 * <td><code>27</code>
 * <tr>
 * <td><code>W</code>
 * <td>Week in month
 * <td><code>2</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>D</code>
 * <td>Day in year
 * <td><code>189</code>
 * <tr>
 * <td><code>d</code>
 * <td>Day in month
 * <td><code>10</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>F</code>
 * <td>Day of week in month
 * <td><code>2</code>
 * <tr>
 * <td><code>E</code>
 * <td>Day in week
 * <td><code>Tuesday</code>; <code>Tue</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>a</code>
 * <td>Am/pm marker
 * <td><code>PM</code>
 * <tr>
 * <td><code>H</code>
 * <td>Hour in day (0-23)
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>k</code>
 * <td>Hour in day (1-24)
 * <td><code>24</code>
 * <tr>
 * <td><code>K</code>
 * <td>Hour in am/pm (0-11)
 * <td><code>0</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>h</code>
 * <td>Hour in am/pm (1-12)
 * <td><code>12</code>
 * <tr>
 * <td><code>m</code>
 * <td>Minute in hour
 * <td><code>30</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>s</code>
 * <td>Second in minute
 * <td><code>55</code>
 * <tr>
 * <td><code>S</code>
 * <td>Millisecond
 * <td><code>978</code>
 * <tr bgcolor="#eeeeff">
 * <td><code>z</code>
 * <td>Time zone
 * <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code>
 * <tr>
 * <td><code>Z</code>
 * <td>Time zone
 * <td><code>-0800</code>
 * </table>
 * </p>
 *
 * The number of pattern letters used determines the final presentation.  For example,
 * yy specifies a 2 digit year, while yyyy specifies a four digit year.  For detailed information see
 * {@link java.text.SimpleDateFormat java.text.SimpleDateFormat}.
 *
 * <p>
 * If the input type is a String, &lt;netui:formatDate> attempts to
 * convert the String into a java.util.Date object before formatting.
 * For the conversion to succeed, the
 * String must conform to a format listed below.
 * <p>
 * The valid formats are:
 * <blockquote>
 * <ul>
 * <li>MM/dd/yy</li>
 * <li>yyyy-MM-dd</li>
 * <li>MMddyy</li>
 * <li>the local default</li>
 * </ul>
 * </blockquote>
 * @example In this sample, the &lt;netui:span> tag's output will be formatted to something like 08/29/1957.
 * <pre>    &lt;netui:span value="${pageScope.today}">
 *        &lt;netui:formatDate pattern="MM/dd/yyyy" />
 *    &lt;/netui:span></pre>
 * @netui:tag name="formatDate" body-content="empty" description="A formatter used to format dates."
 */
public class FormatDate extends FormatTag
{
    private static final Logger logger = Logger.getInstance(FormatDate.class);

    private static final String[] commonFormats = {"MM/dd/yy", "yyyy-MM-dd", "MMddyy",
                                                   null};

    private String _stringInput;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "FormatDate";
    }

    /**
     * Set the pattern to use to convert a String value into a date.  This
     * will be used before the common formats.
     * @param inputPattern the pattern representing the string input
     * @jsptagref.attributedescription The pattern used to convert a String value into a date.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_stringInputPattern</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The pattern used to convert a String value into a date."
     */
    public void setStringInputPattern(String inputPattern)
            throws JspException
    {
        _stringInput = setRequiredValueAttribute(inputPattern, "stringInputPattern");
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

        if (parentTag instanceof IFormattable) {
            IFormattable parent = (IFormattable) parentTag;
            DateFormatter dateFmt = new DateFormatter();
            dateFmt.setPattern(_pattern);
            dateFmt.setLocale(getLocale());
            dateFmt.setInputPattern(_stringInput);
            parent.addFormatter(dateFmt);
        }
        else {
            String s = Bundle.getString("Tags_FormattableParentRequired");
            registerTagError(s, null);
            reportErrors();
        }
    }

    /**
     * Internal FormatTag.Formatter which uses SimpleDateFormat.
     */
    public static class DateFormatter extends FormatTag.Formatter
    {
        private Locale locale;
        private String inputPattern;

        public void setLocale(Locale locale)
        {
            this.locale = locale;
        }

        public void setInputPattern(String pattern)
        {
            inputPattern = pattern;
        }

        public String format(Object dataToFormat) throws JspException
        {
            if (dataToFormat == null) {
                return null;
            }
            InternalStringBuilder formattedString = new InternalStringBuilder(32);

            SimpleDateFormat dateFormat = null;
            if (getPattern() != null) {
                try {
                    if (locale != null) {
                        dateFormat = new SimpleDateFormat(getPattern(), locale);
                    }
                    else {
                        dateFormat = new SimpleDateFormat(getPattern());
                    }
                }
                catch (IllegalArgumentException e) {
                    String s = Bundle.getString("Tags_DateFormatPatternException", new Object[]{e.getMessage()});
                    logger.warn(s);
                    throw new JspException(s);
                }
            }
            else {
                dateFormat = new SimpleDateFormat();
            }

            if (dataToFormat instanceof java.sql.Date) {

                java.sql.Date date = (java.sql.Date) dataToFormat;
                formattedString.append(dateFormat.format(date));
            }
            else if (dataToFormat instanceof java.util.Date) {

                java.util.Date date = (java.util.Date) dataToFormat;
                formattedString.append(dateFormat.format(date));
            }
            else if (dataToFormat instanceof java.util.Calendar) {
                java.util.Calendar c = (java.util.Calendar) dataToFormat;
                java.util.Date date = new java.util.Date(c.getTimeInMillis());
                formattedString.append(dateFormat.format(date));
            }
            else if (dataToFormat instanceof String) {
                if (dataToFormat.equals("")) {
                    return "";
                }

                DateFormat df = null;
                if (inputPattern != null) {
                    try {
                        df = new SimpleDateFormat(inputPattern);
                    }
                    catch (IllegalArgumentException e) {
                        String s = Bundle.getString("Tags_formatDate_StringPatternError",
                                new Object[]{inputPattern, e.getMessage()});
                        logger.warn(s);
                        throw new JspException(s);
                    }

                    // let try and convert this to some type of date
                    java.util.Date date = df.parse((String) dataToFormat,
                            new ParsePosition(0));
                    if (date != null) {
                        formattedString.append(dateFormat.format(date));
                        return formattedString.toString();
                    }
                }


                // this will loop through all of the formats and
                // try to convert the date to one of them.
                int i;
                for (i = 0; i < commonFormats.length; i++) {

                    if (commonFormats[i] != null) {
                        df = new SimpleDateFormat(commonFormats[i]);
                    }
                    else {
                        df = new SimpleDateFormat();

                    }

                    // let try and convert this to some type of date
                    java.util.Date date = df.parse((String) dataToFormat,
                            new ParsePosition(0));
                    if (date != null) {
                        formattedString.append(dateFormat.format(date));
                        break;
                    }
                }
                if (i == commonFormats.length) {
                    String s = Bundle.getString("Tags_formatDate_String_Error",
                            new Object[]{dataToFormat});
                    logger.error(s);
                    throw new JspException(s);
                }
            }
            else {
                String s = Bundle.getString("Tags_formatDate_Type_Error",
                        new Object[]{dataToFormat.getClass().getName()});
                logger.error(s);
                throw new JspException(s);
            }

            return formattedString.toString();
        }
    }
}
