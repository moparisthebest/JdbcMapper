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

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import java.util.Locale;

/**
 * Abstract base class for formatting tags.  Provides the basic formatting properties,
 * as well as the base for the internal FormatTag.Formatter class.
 */
public abstract class FormatTag extends AbstractSimpleTag
{
    private String _language = null;
    private String _country = null;
    protected String _pattern = null;        // The pattern used by a FormatTag to do its formatting.

    /**
     * Sets the language code for the locale.
     * @param language the language code
     * @jsptagref.attributedescription Sets the language code for the locale.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_language</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the language code for the locale."
     */
    public void setLanguage(String language)
    {
        _language = setNonEmptyValueAttribute(language);
    }

    /**
     * Sets the country code for the locale.
     * @param country the country code
     * @jsptagref.attributedescription Sets the country code for the locale.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_country</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the country code for the locale."
     */
    public void setCountry(String country)
    {
        _country = setNonEmptyValueAttribute(country);
    }

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
                registerTagError(s, null);
                return super.getUserLocale();
            }

            if (_country == null)
                loc = new Locale(_language);
            else
                loc = new Locale(_language, _country);
        }
        else
            loc = super.getUserLocale();

        return loc;
    }

    /**
     * Sets the pattern to be used by this FormatTag.
     * @param pattern the pattern to be used
     * @jsptagref.attributedescription Sets the pattern to be used by this format tag.
     * (See the tag description)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_pattern</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the pattern to be used by this FormatTag."
     */
    public void setPattern(String pattern)
            throws JspException
    {
        _pattern = setRequiredValueAttribute(pattern, "patttern");
    }

    /**
     * Internal FormatTag.Formatter which performs the actual formatting.
     */
    public abstract static class Formatter
    {
        private String pattern;

        public String getPattern()
        {
            return pattern;
        }

        public void setPattern(String pattern)
        {
            this.pattern = pattern;
        }

        public String format(Object dataToFormat) throws JspException
        {
            //Default implementation
            if (dataToFormat == null)
                return null;
            else
                return dataToFormat.toString();
        }

        public boolean hasError()
        {
            return false;
        }

        public String getErrorMessage()
        {
            return null;
        }
    }
}
