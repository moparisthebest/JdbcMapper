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
package org.apache.beehive.netui.simpletags.behaviors.formatting;

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This class is a <code>Formatter</code> for formatting dates.  It defers
 * to the underlying Java date formatting.
 */
public class DateFormatter extends Formatter
{
    private static final Logger logger = Logger.getInstance(DateFormatter.class);

    private static final String[] commonFormats = {"MM/dd/yy", "yyyy-MM-dd", "MMddyy",
            null};

    private Locale _locale;
    private String _inputPattern;

    /**
     * Set the pattern to use to convert a String value into a date.  This
     * will be used before the common formats.
     */
    public void setStringInputPattern(String inputPattern)
    {
        _inputPattern = inputPattern;
    }

    /**
     * Set the Locale which is used to format the date in some cases.
     * @param locale
     */
    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    /**
     * This method applies the formatting to the passed in object.
     * @param dataToFormat
     * @return A string representing the object formatted as a date.
     * @throws FormatterException When there is a failure in formatting 
     */
    public String format(Object dataToFormat)
            throws FormatterException
    {
        if (dataToFormat == null) {
            return null;
        }
        InternalStringBuilder formattedString = new InternalStringBuilder(32);

        SimpleDateFormat dateFormat = null;
        if (getPattern() != null) {
            try {
                if (_locale != null) {
                    dateFormat = new SimpleDateFormat(getPattern(), _locale);
                }
                else {
                    dateFormat = new SimpleDateFormat(getPattern());
                }
            }
            catch (IllegalArgumentException e) {
                String s = Bundle.getString("Tags_DateFormatPatternException", new Object[]{e.getMessage()});
                logger.warn(s);
                registerError(s,e);
                throw new FormatterException(s,e);
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
            if (_inputPattern != null) {
                try {
                    df = new SimpleDateFormat(_inputPattern);
                }
                catch (IllegalArgumentException e) {
                    String s = Bundle.getString("Tags_formatDate_StringPatternError",
                            new Object[]{_inputPattern, e.getMessage()});
                    logger.warn(s);
                    registerError(s,e);
                    throw new FormatterException(s,e);
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
                registerError(s,null);
                throw new FormatterException(s);
            }
        }
        else {
            String s = Bundle.getString("Tags_formatDate_Type_Error",
                    new Object[]{dataToFormat.getClass().getName()});
            logger.error(s);
            registerError(s,null);
            throw new FormatterException(s);
        }

        return formattedString.toString();
    }
}
