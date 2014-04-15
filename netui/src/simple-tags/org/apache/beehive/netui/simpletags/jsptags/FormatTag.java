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

import org.apache.beehive.netui.simpletags.behaviors.formatting.Formatter;

import javax.servlet.jsp.JspException;

/**
 * Abstract base class for formatting tags.  Provides the basic formatting properties,
 * as well as the base for the internal FormatTag.Formatter class.
 */
public abstract class FormatTag extends AbstractSimpleTag
{
    protected Formatter _formatter;

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
        _formatter.setLanguage(setNonEmptyValueAttribute(language));
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
        _formatter.setCountry(setNonEmptyValueAttribute(country));
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
        _formatter.setPattern(pattern);
    }
}
