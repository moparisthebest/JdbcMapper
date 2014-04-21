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
package org.apache.beehive.netui.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;
import javax.servlet.ServletException;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

/**
 * Format a {@link java.lang.Throwable} so that it displays well in HTML.
 */
public class HtmlExceptionFormatter {

    /**
     * An XHTML line break
     */
    private static final String HTML_LINE_BREAK = "<br/>";

    /**
     * The end of line character to replace with an HTML line break
     */
    private static final String LINE_BREAK = "\n";

    private static final String CAUSED_BY = "caused by ";

    /**
     * Format an exception into XHTML.
     * <p/>
     * Optionally include a message and the stack trace.
     * <p/>
     * The formatted exception will have line breaks replaced with XHTML line breaks for display
     * in HTML.  The String message of the cause will be included, and the stack trace of the
     * cause is optionally included given the value of <code>includeStackTrace</code>
     *
     * @param message           the message to include with the formatted exception.  This is in addition to the message in the stack trace.
     * @param t                 a Throwable exception to format
     * @param includeStackTrace a boolean that determines whether to include the stack trace in the formatted output
     * @return the formatted error message, optionally including the stack trace.
     */
    public static String format(String message, Throwable t, boolean includeStackTrace) {
        InternalStringBuilder buf = new InternalStringBuilder();

        if(message != null) {
            buf.append(message);

            Throwable cause = discoverRootCause(t);
            if(cause != null) {
                buf.append(HTML_LINE_BREAK);
                buf.append(CAUSED_BY);
                buf.append(": ");
                buf.append(cause.toString());
            }
        }

        if(includeStackTrace) {
            if(message != null)
                buf.append(HTML_LINE_BREAK);

            String st = addStackTrace(t);
            buf.append(st);

            Throwable rootCause = null;
            Throwable tmp = t;
            while(hasRootCause(tmp) && (rootCause = discoverRootCause(tmp)) != null) {
                st = addStackTrace(rootCause);
                buf.append(HTML_LINE_BREAK);
                buf.append(st);
                tmp = rootCause;
            }
        }

        return buf.toString().replaceAll(LINE_BREAK, HTML_LINE_BREAK);
    }

    private static String addStackTrace(Throwable t) {
        InternalStringBuilder buf = new InternalStringBuilder();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        pw.close();

        String error = sw.toString();
        int pos = error.indexOf(LINE_BREAK);
        if(pos != -1) {
            String lineOne = error.substring(0, pos);
            String rest = error.substring(pos);

            buf.append("<span class='pfErrorLineOne'>");
            buf.append(lineOne);
            buf.append("</span>");
            buf.append(rest);
        } else {
            buf.append(sw.toString());
        }

        return buf.toString();
    }

    private static boolean hasRootCause(Throwable t) {
        return t.getCause() == null && (t instanceof JspException || t instanceof ServletException || t instanceof ELException);
    }

    private static Throwable discoverRootCause(Throwable t) {
        Throwable cause = null;
        if(t instanceof JspException)
            cause = ((JspException)t).getRootCause();
        else if(t instanceof ServletException)
            cause = ((ServletException)t).getRootCause();
        else if(t instanceof ELException)
            cause = ((ELException)t).getRootCause();
        else cause = t.getCause();

        return cause;
    }
}
