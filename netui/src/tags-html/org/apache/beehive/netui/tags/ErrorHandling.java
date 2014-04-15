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
package org.apache.beehive.netui.tags;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ErrorHandling
{
    /**
     * static flag indicating if we are reporting errors in-page or throwing JspExceptions.
     */
    private static boolean reportErrorInPage = true;
    private static final Logger logger = Logger.getInstance(ErrorHandling.class);

    private List _errors;

    /**
     * This will report an error from a tag.  The error will
     * contain a message.  If error reporting is turned off,
     * the message will be returned and the caller should throw
     * a JspException to report the error.
     * @param message - the message to register with the error
     * @throws javax.servlet.jsp.JspException - if in-page error reporting is turned off this method will always
     *                                        throw a JspException.
     */
    public void registerTagError(String message, String tagName, JspTag tag, Throwable e)
            throws JspException
    {
        assert (message != null) : "parameter 'message' must not be null.";

        // add the error to the list of errors
        if (_errors == null)
            _errors = new ArrayList();

        TagErrorInfo tei = new TagErrorInfo();

        tei.tagType = tagName;
        tei.message = message;
        _errors.add(tei);

        IErrorReporter er = getErrorReporter(tag);
        if (er == null) {
            tei.errorNo = -1;
            if (!reportErrorInPage) {
                String s = Bundle.getString("Tags_NoInPageErrorReporting", new Object[]{message});
                if (e == null)
                    logger.error(s);
                else
                    logger.error(s, e);
                //localRelease();
                throw new JspException(message);
            }
            return;
        }

        // add the error to the ErrorReporter tag
        er.addError(tei);
        assert (tei.errorNo > 0);
        if (!reportErrorInPage) {
            String s = Bundle.getString("Tags_NoInPageErrorReporting", new Object[]{message});
            if (e == null)
                logger.error(s);
            else
                logger.error(s, e);
            //localRelease();
            throw new JspException(s);
        }
        return;
    }

    /**
     * This method will add an error to the errors begin tracked by the tag. After the first time this method
     * is called, <code>hasErrors</code> will return true.
     * @param error The <code>EvalErrorInfo</code> describing the error.
     */
    public void registerTagError(AbstractPageError error, JspTag tag)
            throws JspException
    {
        assert (error != null);

        // add the error to the list of errors
        if (_errors == null)
            _errors = new ArrayList();

        _errors.add(error);

        IErrorReporter er = getErrorReporter(tag);
        if (er == null) {
            error.errorNo = -1;
            return;
        }

        // add the error to the ErrorReporter tag
        er.addError(error);
        assert (error.errorNo > 0);
    }

    /**
     * This method will return <code>true</code> if there have been any errors registered on this
     * tag.  Otherwise it returns <code>false</code>
     * @return <code>true</code> if errors have been reported on this tag.
     * @see #registerTagError
     */
    public boolean hasErrors()
    {
        return (_errors != null);
    }

    /**
     * This method will write out the <code>String</code> returned by <code>getErrorsReport</code> to the
     * response output stream.
     * @throws JspException if <code>write</code> throws an exception.
     * @see #getErrorsReport
     */
    public void reportErrors(Writer writer, String tagName)
            throws JspException
    {
        try {
            writer.write(getErrorsReport(tagName));
        }
        catch (IOException e) {
            JspException jspException = new JspException(e.getMessage(), e);

            // todo: future cleanup
            // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
            // this will cause an IllegalStateException on the following call.
            if (jspException.getCause() == null) {
                jspException.initCause(e);
            }
            throw jspException;
        }
    }

    public String getInlineError(String tagName)
    {
        if (isInlineErrors()) {
            AbstractPageError info = (AbstractPageError) _errors.get(0);
            return getInlineError(info, tagName);
        }
        return null;
    }


    /**
     * This method will return a <code>String<code> that represents all of the errors that were
     * registered for the tag.  This method assumes that there are errors in the tag and asserts
     * this is true.  Code will typically call <code>hasErrors</code> before calling this method.
     * @return A <code>String</code> that contains all of the errors registered on this tag.
     * @see #registerTagError
     */
    public String getErrorsReport(String tagName)
    {
        assert _errors != null;
        assert _errors.size() > 0;

        int cnt = _errors.size();

        InternalStringBuilder sb = new InternalStringBuilder(128);

        // check the first error to see if we are reporting errors at the end
        AbstractPageError info = (AbstractPageError) _errors.get(0);
        if (isInlineErrors()) {
            String s = getInlineError(info, tagName);
            return s;
        }

        // create the errors
        String s;
        s = Bundle.getString("Tag_Header",
                new Object[]{tagName, Integer.toString(cnt)});
        sb.append(s);

        Object[] args = new Object[4];
        for (int i = 0; i < cnt; i++) {
            Object o = _errors.get(i);
            if (o instanceof EvalErrorInfo) {
                EvalErrorInfo e = (EvalErrorInfo) o;
                assert info != null;

                args[0] = Bundle.getString("Expression_Error");
                args[1] = e.attr;
                args[2] = e.expression;
                args[3] = e.evalExcp.getMessage();
                s = Bundle.getString("Expression_Error_Line", args);
            }
            else if (o instanceof TagErrorInfo) {
                TagErrorInfo e = (TagErrorInfo) o;
                assert info != null;

                args[0] = Bundle.getString("Tag_Error");
                args[1] = e.message;
                s = Bundle.getString("Tag_Error_Line", args);
            }
            sb.append(s);
        }

        s = Bundle.getString("Tag_Footer");
        sb.append(s);
        return sb.toString();
    }

    /**
     * This method get the current errors and write the formated output
     * @param sb
     */
    public static void reportCollectedErrors(InternalStringBuilder sb, JspTag tag)
    {
        IErrorReporter er = getErrorReporter(tag);
        if (er == null)
            return;

        assert (sb != null);
        ArrayList errors = er.returnErrors();
        if (errors == null || errors.size() == 0)
            return;

        assert(errors.size() > 0);

        String s;
        // write the error header
        s = Bundle.getString("Footer_Error_Header");
        sb.append(s);

        int cnt = errors.size();
        Object[] args = new Object[5];
        for (int i = 0; i < cnt; i++) {
            Object o = errors.get(i);
            assert (o != null);
            if (o instanceof EvalErrorInfo) {
                EvalErrorInfo err = (EvalErrorInfo) o;
                args[0] = Integer.toString(err.errorNo);
                args[1] = err.tagType;
                args[2] = err.attr;
                args[3] = err.expression;
                args[4] = err.evalExcp.getMessage();
                s = Bundle.getString("Footer_Error_Expr_Body", args);
                sb.append(s);
            }
            else if (o instanceof TagErrorInfo) {
                TagErrorInfo tei = (TagErrorInfo) o;
                args[0] = Integer.toString(tei.errorNo);
                args[1] = tei.tagType;
                args[2] = tei.message;
                s = Bundle.getString("Footer_Error_Tag_Body", args);
                sb.append(s);
            }
        }

        // write the error footer
        s = Bundle.getString("Footer_Error_Footer");
        sb.append(s);
    }

    /**
     * This method get the current errors and write the formated output
     * @param pc
     */
    public static void reportCollectedErrors(PageContext pc, JspTag tag)
    {
        IErrorReporter er = getErrorReporter(tag);
        if (er == null)
            return;

        assert (pc != null);
        ArrayList errors = er.returnErrors();
        if (errors == null || errors.size() == 0)
            return;

        assert(errors.size() > 0);

        String s;
        // write the error header
        s = Bundle.getString("Footer_Error_Header");
        write(pc, s);

        int cnt = errors.size();
        Object[] args = new Object[5];
        for (int i = 0; i < cnt; i++) {
            Object o = errors.get(i);
            assert (o != null);
            if (o instanceof EvalErrorInfo) {
                EvalErrorInfo err = (EvalErrorInfo) o;
                args[0] = Integer.toString(err.errorNo);
                args[1] = err.tagType;
                args[2] = err.attr;
                args[3] = err.expression;
                args[4] = err.evalExcp.getMessage();
                s = Bundle.getString("Footer_Error_Expr_Body", args);
                write(pc, s);
            }
            else if (o instanceof TagErrorInfo) {
                TagErrorInfo tei = (TagErrorInfo) o;
                args[0] = Integer.toString(tei.errorNo);
                args[1] = tei.tagType;
                args[2] = tei.message;
                s = Bundle.getString("Footer_Error_Tag_Body", args);
                write(pc, s);
            }
        }

        // write the error footer
        s = Bundle.getString("Footer_Error_Footer");
        write(pc, s);
    }

    private boolean isInlineErrors()
    {
        AbstractPageError info = (AbstractPageError) _errors.get(0);
        return (info.errorNo > 0);
    }

    private String getInlineError(AbstractPageError info, String tagName)
    {
        String s;
        if (info instanceof EvalErrorInfo) {
            s = Bundle.getString("Expression_Error");
            s = Bundle.getString("Inline_error",
                    new Object[]{
                        s,
                        Integer.toString(info.errorNo),
                        tagName,
                    });
        }
        else if (info instanceof TagErrorInfo) {
            s = Bundle.getString("Tag_Error");
            s = Bundle.getString("Inline_error",
                    new Object[]{
                        s,
                        Integer.toString(info.errorNo),
                        tagName,
                    });
        }
        else {
            s = null;
            assert true : "Unhandled type";
        }
        return s;
    }

    /**
     * This method will return the first <code>ErrorReporter</code> in the parental chain of the
     * tag.  Searching starts with this tag.
     * @return an <code>ErrorReporter</code> if one is found in the parental chain, otherwise null.
     */
    private static IErrorReporter getErrorReporter(JspTag tag)
    {
        if (tag instanceof IErrorReporter && ((IErrorReporter) tag).isReporting())
            return (IErrorReporter) tag;

        // check to see if this tag has is an ErrorReporter or has an ErrorReporter as a parent
        IErrorReporter er = (IErrorReporter) SimpleTagSupport.findAncestorWithClass(tag, IErrorReporter.class);
        while (er != null) {
            if (er.isReporting())
                return er;
            er = (IErrorReporter) SimpleTagSupport.findAncestorWithClass((JspTag) er, IErrorReporter.class);
        }
        return null;
    }

    private static final void write(PageContext pc, String string)
    {
        try {
            ResponseUtils.write(pc, string);
        }
        catch (JspException e) {
            logger.error(Bundle.getString("Tags_WriteException"), e);
        }
    }
}
