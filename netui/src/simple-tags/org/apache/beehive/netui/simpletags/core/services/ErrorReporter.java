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
package org.apache.beehive.netui.simpletags.core.services;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.AbstractPageError;
import org.apache.beehive.netui.simpletags.core.EvalErrorInfo;
import org.apache.beehive.netui.simpletags.core.TagErrorInfo;
import org.apache.beehive.netui.util.Bundle;

import java.util.ArrayList;

public class ErrorReporter
{
    private ArrayList _errors;

    /**
     * This will report an error from a tag.  The error will
     * contain a message.  If error reporting is turned off,
     * the message will be returned and the caller should throw
     * a JspException to report the error.
     * @param message - the message to register with the error
     */
    public AbstractPageError registerTagError(String message, String tagName, Throwable e)
    {
        assert (message != null) : "parameter 'message' must not be null.";

        TagErrorInfo tei = new TagErrorInfo();
        tei.tagType = tagName;
        tei.message = message;

        // add the error to the ErrorReporter tag
        addError(tei);
        assert (tei.errorNo > 0);
        return tei;
    }

    /**
     * This method will add an error to the errors begin tracked by the tag. After the first time this method
     * is called, <code>hasErrors</code> will return true.
     * @param error The <code>EvalErrorInfo</code> describing the error.
     */
    public AbstractPageError registerTagError(AbstractPageError error)
    {
        assert (error != null) : "The parameter 'error' must not be null";
        addError(error);
        assert (error.errorNo > 0);
        return error;
    }

    /**
     * This method get the current errors and write the formated output
     *
     * @param renderer
     */
    public void reportCollectedErrors(Appender renderer)
    {
        assert (renderer != null) : "Parameter 'renderer' must not be null.";
        ArrayList errors = returnErrors();
        if (errors == null || errors.size() == 0)
            return;

        assert(errors.size() > 0);

        // write the error header
        String s = Bundle.getString("Footer_Error_Header");
        renderer.append(s);

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
                renderer.append(s);
            }
            else if (o instanceof TagErrorInfo) {
                TagErrorInfo tei = (TagErrorInfo) o;
                args[0] = Integer.toString(tei.errorNo);
                args[1] = tei.tagType;
                args[2] = tei.message;
                s = Bundle.getString("Footer_Error_Tag_Body", args);
                renderer.append(s);
            }
        }

        // write the error footer
        s = Bundle.getString("Footer_Error_Footer");
        renderer.append(s);
    }

    /**
     * This will return the errors currently being reported.  The errors
     * collection will be reset to allow a new set of errors to be tracked.
     *
     * @return An ArrayList containing all of the collected errors.
     */
    public ArrayList returnErrors()
    {
        ArrayList e = _errors;
        _errors = null;
        return e;
    }

    /**
     * This method will add an error in the form
     * of an AbstractPageError to the collection of errors
     * being handled by this ErrorReporter
     *
     * @param ape
     */
    private void addError(AbstractPageError ape)
    {
        assert (ape != null) : "Parameter 'ape' must not be null";

        // This is the error reporter.
        if (_errors == null)
            _errors = new ArrayList();

        // add the error and update it
        _errors.add(ape);
        ape.errorNo = _errors.size();
    }
}
