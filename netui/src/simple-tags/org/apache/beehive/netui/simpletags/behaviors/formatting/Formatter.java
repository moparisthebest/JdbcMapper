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

import org.apache.beehive.netui.simpletags.util.PageFlowAdaptor;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import java.util.Locale;

/**
 * This is the base class for all formatters.
 */
abstract public class Formatter
{
    private String _language = null;
    private String _country = null;
    private String _pattern = null;

    private String _errorMsg = null;
    private Throwable _errorCause = null;

    /**
     * Sets the language code for the locale.
     */
    public void setLanguage(String language)
    {
        _language = language;
    }

    /**
     * Sets the country code for the locale.
     */
    public void setCountry(String country)
    {
        _country = country;
    }

    /**
     * Sets the pattern to be used by this FormatTag.
     * @param pattern the pattern used during formatting.
     */
    public void setPattern(String pattern)
    {
        _pattern = pattern;
    }


    /**
     * This method will return the pattern that was set on the Formatter.
     * @return The pattern that was set.
     */
    public String getPattern()
    {
        return _pattern;
    }

    /**
     * If formatting causes an error, then the formatter will set
     * an error message.  This indicates that an error has occurred.
     * This method will return a boolean value indicating if an error has
     * occurred or not.
     * @return returns <code>true</code> if an Error has occurred.
     */
    public boolean hasError()
    {
        return (_errorMsg != null);
    }

    /**
     * This method returns the text of the error messages if an error has occurred
     * during the formatting of the text.
     * @return The text of the error message or null if no error has occurred.
     */
    public String getErrorMessage()
    {
        return _errorMsg;
    }

    /**
     * This method will return the <code>Throwable</code> that was register
     * when an error occurred during formatting.
     * @return a <code>Throwable</code> that resulted in an error during formatting or null
     */
    public Throwable getErrorCause()
    {
        return _errorCause;
    }

    /**
     * This is method is called by <code>Formatter</code>s when an error occurs during
     * formatting.  When this method is called, it will implicitly set the <code>hasError</code>
     * to true if the <code>errorMsg</code> parameter is non-null.
     * @param errorMsg The error message
     */
    protected void registerError(String errorMsg, Throwable cause) {
        _errorMsg = errorMsg;
        _errorCause = cause;
    }

    /**
     * A <code>Formatter</code> must implement this method to format the passed
     * in <code>Object</code>.  If an error occurs during formatting, the <code>
     * Formatter</code> should call the <code>setErrorMessage</code> method with
     * the text of the error.
     * @param dataToFormat An object that is is being formatted.
     * @return A string representing the formatted object.
     */
    abstract public String format(Object dataToFormat)
        throws FormatterException;

    /**
     * Returns the locale based on the country and language.
     * @return the locale
     */
    public Locale getLocale()
            throws JspException
    {
        Locale loc = null;
        if (_language != null || _country != null) {
            // language is required
            if (_language == null) {
                String s = Bundle.getString("Tags_LocaleRequiresLanguage", new Object[]{_country});
                registerError(s, null);
                return PageFlowAdaptor.getUserLocale();
            }

            if (_country == null)
                loc = new Locale(_language);
            else
                loc = new Locale(_language, _country);
        }
        else
            loc =PageFlowAdaptor.getUserLocale();

        return loc;
    }
}
