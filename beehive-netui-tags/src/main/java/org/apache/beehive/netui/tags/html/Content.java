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
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.jsp.JspException;

/**
 * The Content tag is used to display text, or the result of an expression, to the page.
 * @jsptagref.tagdescription Displays text or the result of an expression.
 * Note that &lt;netui:content> is similar to {@link Span}, except for the way
 * it processes HTML-sensitive text.  The &lt;netui:content> tag does not escape
 * HTML-sensitive characters, but the &lt;netui:span> tag filters the input
 * string for characters that are senstive to
 * HTML interpreters and replaces these characters
 * with the corresponding entity strings.  For example, if you pass the
 * string '&amp;amp;' to a &lt;netui:span> tag, the string '&amp;amp;amp;' will be written to
 * the HTML source file, and the following will be displayed
 * in the browser: '&amp;amp;'.
 *
 * <p>The following table shows how the &lt;netui:span> and &lt;netui:content> tags treat HTML-sensitive characters.
 * <blockquote>
 * <table border="1">
 * <tr>
 * <td width="30%"><b>tag</b></td>
 * <td width="30%"><b>generated HTML source</b></td>
 * <td width="30%"><b>displayed in browser</b></td>
 * </tr>
 * <tr>
 * <td>&lt;netui:content value="&amp;amp;"/></td>
 * <td>&amp;amp;</td>
 * <td>&</td>
 * </tr>
 * <tr>
 * <td>&lt;netui:span value="&amp;amp;"/></td>
 * <td>&amp;amp;amp;</td>
 * <td>&amp;amp;</td>
 * </tr>
 * </table>
 * </blockquote>
 * @example In this sample, the Content tag displays a Form Bean's <code>lastName</code> property.
 * <pre>
 * &lt;netui:content value="${actionForm.lastName}"/>
 * </pre>
 * @netui:tag name="content" body-content="empty" description="Used to display text or the result of an expression to the page."
 */
public class Content extends AbstractSimpleTag
{
    private static final Logger logger = Logger.getInstance(Content.class);

    private static final String DEFAULT_NULL_TEXT = "";
    private String _defaultValue = null;
    private Object _value = null;  // The value for this content.

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Content";
    }

    /**
     * Set the default value of this Content.
     * @param defaultValue the default value
     * @jsptagref.attributedescription The String literal or expression to be used as the default output.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_defaultOutput</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The String literal or expression to be used as the default output."
     */
    public void setDefaultValue(String defaultValue)
            throws JspException
    {
        _defaultValue = setRequiredValueAttribute(defaultValue, "defaultValue");
    }

    /**
     * Set the value of this Content.
     * @param value the Content value
     * @jsptagref.attributedescription The String literal or expression used to output the content.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_output</i>
     * @netui:attribute required="true" rtexprvalue="true" type="java.lang.Object"
     * description="The String literal or expression used to output the content."
     */
    public void setValue(Object value)
    {
        _value = value;
    }

    /**
     * Render the content.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        // report any errors...
        if (hasErrors()) {
            reportErrors();
            return;
        }

        // calculate the output value...
        String text;
        if (_value != null) {
            text = _value.toString();
        }
        else {
            if (_defaultValue != null) {
                text = _defaultValue;
            }
            else {
                logger.warn(Bundle.getString("Tags_ContentExpressionNull", _value));
                text = DEFAULT_NULL_TEXT;
            }
        }

        write(text);
    }
}
