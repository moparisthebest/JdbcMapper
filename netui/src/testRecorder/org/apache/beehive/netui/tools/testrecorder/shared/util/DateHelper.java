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
package org.apache.beehive.netui.tools.testrecorder.shared.util;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

/**
 * Utility to help handle Date/Calendar formatting in the test recorder.
 */
public final class DateHelper {
    /**
     * Default {@link java.util.Locale} used to format dates that the test recorder uses.
     * This defaults to the <code>US</code> Locale for the time being so that dates
     * are handled in an internally consistent way.
     */
    private static final Locale DEFAULT_LOCALE = Locale.US;

    /**
     * Format for how dates are parsed and written.
     */
    private static final String DATE_FORMAT_STRING = "dd MMM yyyy, hh:mm:ss.SSS aa zzz";

    /**
     * Format for how dates are parsed and written in the JSP UI for the test recorder.
     *
     * These are just more readable than those that are used for the test record XML files.
     */
    private static final String READABLE_DATE_FORMAT_STRING = "dd MMM, hh:mm:ss aa";

    /**
     * {@link java.text.SimpleDateFormat} used to parse Strings to {@link java.util.Date} objects.
     */
    private static final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING, DEFAULT_LOCALE);

    /**
     * {@link java.text.SimpleDateFormat} used to parse Strings to {@link java.util.Date} objects.
     */
    private static final SimpleDateFormat READABLE_DATE_FORMAT = new SimpleDateFormat(READABLE_DATE_FORMAT_STRING, DEFAULT_LOCALE);


    // Do not construct
    private DateHelper() {}

    /**
     * Get a {@link java.util.Calendar} instance that represents <code>now</code>.
     *
     * @return said Calendar instance
     */
    public static final Calendar getCalendarInstance()
    {
        return Calendar.getInstance(DEFAULT_LOCALE);
    }

    /**
     * Get a {@link java.util.Calendar} instance that represents the date parsed
     * from the <code>dateString</code> parameter.
     *
     * @param dateString String date to parse
     * @return the parsed Calendar instance
     * @throws ParseException thrown when the dateString can't be parsed
     */
    public static Calendar getCalendarInstance(String dateString)
        throws ParseException {
        assert dateString != null;

        Calendar calendar = Calendar.getInstance(DEFAULT_LOCALE);
        calendar.setTime(XML_DATE_FORMAT.parse(dateString));

        return calendar;
    }

    /**
     * Format the given {@link java.util.Calendar} instance into a String
     * for printing / display.
     *
     * @param calendar {@link java.util.Calendar} instance to format.
     * @return the formatted String
     */
    public static final String formatToString(Calendar calendar)
    {
        assert calendar != null;
        return XML_DATE_FORMAT.format(calendar.getTime());
    }

    /**
     * Format the given {@link java.util.Date} instance into a String
     * for use in a test's recorded XML file.
     *
     * @param date {@link java.util.Date} instance to format.
     * @return the formatted String
     */
    public static final String formatToString(Date date)
    {
        assert date != null;
        return XML_DATE_FORMAT.format(date);
    }

    /**
     * Format the given {@link java.util.Date} into a String for display.
     *
     * @param date the {@link java.util.Date} to format
     * @return the formatted String
     */
    public static final String formatToReadableString(Date date)
    {
        assert date != null;
        return READABLE_DATE_FORMAT.format(date);
    }
}
