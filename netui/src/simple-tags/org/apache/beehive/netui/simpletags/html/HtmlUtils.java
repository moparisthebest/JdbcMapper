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
package org.apache.beehive.netui.simpletags.html;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.core.URLCodec;
import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.pageflow.PageFlowContext;

//import javax.servlet.jsp.JspException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

/**
 * This class provides a set of static methods that provide HTML utility code.
 */
public class HtmlUtils
{
    public static boolean containsHtml(String value)
    {
        int numChars = value.length();
        char c;

        for (int i = 0; i < numChars; i++) {
            c = value.charAt(i);
            switch (c) {
                case '<':
                    return true;
                case '&':
                    return true;
                case '"':
                    return true;
            }
        }
        return false;

    }

    /**
     * Filter the specified value for characters that are sensitive to
     * HTML interpreters.  It will return a string with these characters replaced
     * with HTML entities.  This method calls the overloaded method with <code>markupHTMLSpaceReturn</code>
     * set to <code>false</code>.
     * @param value The <code>String</code> value to be filtered and returned.
     */
    public static void filter(String value, Appender results)
    {
        filter(value, results, false);
    }

    /**
     * Filter the specified string for characters that are sensitive to
     * HTML interpreters, returning the string with these characters replaced
     * by the corresponding character entities.
     * @param value                 The <code>String</code> value to be filtered and returned.
     * @param markupHTMLSpaceReturn convert space characters and return characters
     *                              to &amp;nbsp; and &lt;br /&gt; marketup for html.
     */
    public static void filter(String value, Appender result, boolean markupHTMLSpaceReturn)
    {
        // if the value is null, return
        if (value == null)
            return;

        // convert the string
        int numChars = value.length();
        char c;
        char prev = 0;

        for (int i = 0; i < numChars; i++) {
            c = value.charAt(i);
            switch (c) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                case '\'':
                    result.append("&#39;");
                    break;
                case ' ':
                    if (markupHTMLSpaceReturn) {
                        if (prev == ' ') {
                            result.append("&nbsp;");
                        }
                        else
                            result.append(c);
                    }
                    else
                        result.append(c);
                    break;
                case '\n':
                    if (markupHTMLSpaceReturn) {
                        result.append("<br />");
                    }
                    else
                        result.append(c);
                    break;
                default:
                    result.append(c);
            }
            prev = c;
        }
    }

    public static String escapeEscapes(String val)
    {
        assert(val != null);
        InternalStringBuilder sb = new InternalStringBuilder(val.length());
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            if (c == '"') {
                sb.append("&quot;");
                continue;
            }
            if (c == '\\') {
                sb.append("\\\\");
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String legacyEscapeEscapes(String val)
    {
        assert(val != null);
        InternalStringBuilder sb = new InternalStringBuilder(val.length());
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            if (c == '"') {
                sb.append("\\\"");
                continue;
            }
            if (c == '\\') {
                sb.append("\\\\");
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * @param url
     * @param params
     * @return String
     */
    public static String addParams(String url, Map params)
    {
        InternalStringBuilder urlBuffer = new InternalStringBuilder(url);

        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        String encoding = pfCtxt.getResponse().getCharacterEncoding();

        try {
            // Add dynamic parameters if requested
            if ((params != null) && (params.size() > 0)) {

                // Add the required request parameters
                boolean question = url.indexOf('?') >= 0;
                Iterator keys = params.keySet().iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Object value = params.get(key);
                    if (value == null) {
                        if (!question) {
                            urlBuffer.append('?');
                            question = true;
                        }
                        else {
                            urlBuffer.append("&");
                        }
                        urlBuffer.append(URLCodec.encode(key, encoding));
                        urlBuffer.append('='); // Interpret null as "no value"
                    }
                    else if (value instanceof String) {
                        if (!question) {
                            urlBuffer.append('?');
                            question = true;
                        }
                        else {
                            urlBuffer.append("&");
                        }
                        urlBuffer.append(URLCodec.encode(key, encoding));
                        urlBuffer.append('=');
                        urlBuffer.append(URLCodec.encode((String) value, encoding));
                    }
                    else if (value instanceof String[]) {
                        String values[] = (String[]) value;
                        for (int i = 0; i < values.length; i++) {
                            if (!question) {
                                urlBuffer.append('?');
                                question = true;
                            }
                            else {
                                urlBuffer.append("&");
                            }
                            urlBuffer.append(URLCodec.encode(key, encoding));
                            urlBuffer.append('=');
                            urlBuffer.append(URLCodec.encode(values[i], encoding));
                        }
                    }
                    else /* Convert other objects to a string */ {
                        if (!question) {
                            urlBuffer.append('?');
                            question = true;
                        }
                        else {
                            urlBuffer.append("&");
                        }
                        urlBuffer.append(URLCodec.encode(key, encoding));
                        urlBuffer.append('=');
                        urlBuffer.append(URLCodec.encode(value.toString(), encoding));
                    }
                }
            }
        }
        catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            // @todo: what to throw here?
            throw new RuntimeException("Unsupported Encoding" + encoding, uee);
        }

        return urlBuffer.toString();
    }

    /**
     * This method will determine if the value passed in contains an entity.
     * @return boolean
     */
    public static boolean containsEntity(String value)
    {
        assert (value != null) : "Parameter 'value' must not be null";

        int pos = value.indexOf('&');
        if (pos == -1)
            return false;

        int end = value.indexOf(';');
        if (end != -1 && pos < end) {
            // extract the entity and then verify it is
            // a valid unicode identifier.
            String entity = value.substring(pos + 1, end);
            if (entity.length() == 0)
                return false;
            char[] chars = entity.toCharArray();

            // verify the start is an indentifier start
            // and the rest is a part.
            if (!Character.isUnicodeIdentifierStart(chars[0])) {
                if (chars[0] == '#' && chars.length > 1) {
                    for (int i = 1; i < chars.length; i++) {
                        if (!Character.isDigit(chars[i]))
                            return false;
                    }
                    return true;
                }
                return false;
            }
            for (int i = 1; i < chars.length; i++) {
                if (!Character.isUnicodeIdentifierPart(chars[i]))
                    return false;
            }
            // good indentifier
            return true;
        }
        return false;
    }
}
