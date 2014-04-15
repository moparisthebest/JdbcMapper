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

//java imports

//internal imports

//external imports

/**
 * A formatter used to format strings.  FormatString uses the following pattern syntax:
 * <p>
 * The '#' character gets replaced by the next character in the string getting formatted,
 * while other characters get put in as literals.  For example:
 * <p>
 * String "5555555555" with pattern "(###)###-####" would result in: (555)555-5555.
 * <p>
 * The '*' character will display all characters in the string at that point in the pattern.  For example:
 * <p>
 * String "123456" with pattern "#-*!" would result in: 1-23456!
 * <p>
 * If a result with a '#' pr '*' character showing is desired, the '#' or '*' needs to
 * be escaped with the '$' character.  For example:
 * <p>
 * String "ABCD" with pattern "$#-####" would result in: #-ABCD.
 * <p>
 * To show a '$' in the result, the '$' character needs to be escaped.  For example:
 * <p>
 * String "1234" with pattern "$$#,###" would result in: $1,234
 * <p>
 * If the truncate attribute is set to "true", characters in the string that exceed the pattern
 * will be dropped.  Otherwise, they will be appended to the end of the formatted string.
 * @jsptagref.tagdescription A formatter used to format strings.
 *
 * <p>The &lt;netui:formatString> tag formats the output of its parent tag.  For example:
 *
 * <pre>    &lt;netui:span value="${pageFlow.phone}">
 *        &lt;netui:formatString pattern="phone number: (###) ###-####"/>
 *    &lt;/netui:span> </pre>
 *
 * <p>
 * &lt;netui:formatString> uses the following pattern syntax:
 * <p>
 * The <b>#</b> character is a placeholder for individual characters in the String to be formatted,
 * while other characters are treated as literals.  For example:
 * <p>
 * String "5555555555" with pattern "(###)###-####" would result in: (555)555-5555.
 * <p>
 * The <b>*</b> character displays all remaining characters in the String.  For example:
 * <p>
 * String "123456" with pattern "#-*!" would result in: 1-23456!
 * <p>
 * If a result with a '#' or '*' character showing is desired, the '#' or '*' needs to
 * be escaped with the '$' character.  For example:
 * <p>
 * String "ABCD" with pattern "$#-####" would result in: #-ABCD.
 * <p>
 * To show a '$' in the result, the '$' character needs to be escaped.  For example:
 * <p>
 * String "1234" with pattern "$$#,###" would result in: $1,234
 * <p>
 * If the <code>truncate</code> attribute is set to "true", characters in the String that exceed the pattern
 * will be dropped.  Otherwise, they will be appended to the end of the formatted String.
 * @example In this sample, the String "2125555555" will be formatted to this form: (212)555-5555.
 *
 * <pre>    &lt;netui:span value="2125555555">
 *        &lt;netui:formatString pattern="phone (###) ###-####"/>
 *    &lt;/netui:span> </pre>
 * @netui:tag name="formatString" body-content="empty" description="A formatter used to format strings."
 * @netui:attribute name="pattern" required="true" rtexprvalue="true"
 */
public class FormatString extends FormatTag
{
    protected boolean truncate = false;     // Whether or not pattern-exceeding characters should be dropped.

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "FormatString";
    }

    /**
     * Sets whether or not pattern-exceeding characters should be dropped.
     * @param truncate "true" or "false"
     * @jsptagref.attributedescription A boolean specifying whether characters that exceed the pattern's length should be dropped.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_truncate</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="A boolean specifying whether characters that exceed the pattern's length should be dropped."
     */
    public void setTruncate(boolean truncate)
    {
        this.truncate = truncate;
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
            StringFormatter formatter = new StringFormatter();
            formatter.setPattern(_pattern);
            formatter.setTruncate(truncate);
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
     * Internal FormatTag.Formatter which performs its own string parsing and formatting.
     */
    public static class StringFormatter extends FormatTag.Formatter
    {
        private boolean truncate;

        public boolean getTruncate()
        {
            return truncate;
        }

        public void setTruncate(boolean truncate)
        {
            this.truncate = truncate;
        }

        public String format(Object dataToFormat) throws JspException
        {
            if (dataToFormat == null) {
                return null;
            }
            InternalStringBuilder formattedString = new InternalStringBuilder(32);
            int index = 0;
            int patternIndex = 0;
            String unformattedString = dataToFormat.toString();

            int length = unformattedString.length();
            int patternLength = getPattern().length();

            int ignoreNumbers = 0;

            //Cycle through each character in the string
            for (index = 0; index < length; index++) {
                if (patternIndex < patternLength) {
                    boolean loop = true;
                    while (loop) {
                        char thisChar = getPattern().charAt(patternIndex);
                        if (thisChar == '#')
                            break;
                        else if (thisChar == '*') {
                            ignoreNumbers++;
                            break;
                        }
                        else if (thisChar == '$') {
                            if ((patternIndex + 1 < patternLength) && (getPattern().charAt(patternIndex + 1) == '$')) {
                                patternIndex++;
                            }
                            else if ((patternIndex + 1 < patternLength) && (getPattern().charAt(patternIndex + 1) == '#')) {
                                patternIndex++;
                            }
                            else if ((patternIndex + 1 < patternLength) && (getPattern().charAt(patternIndex + 1) == '*')) {
                                patternIndex++;
                            }
                        }
                        formattedString = formattedString.append(getPattern().charAt(patternIndex));
                        patternIndex++;
                        if (patternIndex >= patternLength)
                            loop = false;
                    }
                }

                if ((patternIndex >= patternLength) && (truncate))
                    break;

                if (ignoreNumbers == 1) {
                    formattedString.append(unformattedString.substring(index));
                    ignoreNumbers++;
                }
                else if (ignoreNumbers > 1) {
                    //Ignore
                }
                else {
                    formattedString.append(unformattedString.charAt(index));
                }
                patternIndex++;
            }

            if (patternIndex < patternLength) {
                //Throw in the rest of the pattern
                while (patternIndex < patternLength) {
                    char patternChar = getPattern().charAt(patternIndex);
                    if (patternChar == '#') {
                        formattedString.append(" ");
                    }
                    else {
                        formattedString.append(patternChar);
                    }
                    patternIndex++;
                }
            }
            return formattedString.toString();
        }
    }
}
