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
package org.apache.beehive.netui.simpletags.core;

import org.apache.beehive.netui.util.Bundle;

public class TagErrorHandling
{
    /**
     * static flag indicating if we are reporting errors in-page or throwing JspExceptions.
     */
    private AbstractPageError _error;

    /**
     * Set the error that will be used to generate the inline error representation
     * within the page.
     * @param error The error that will be used to report the error.
     */
    public void setError(AbstractPageError error) {
        _error = error;
    }

    /**
     * This method will return a <code>String<code> that represents all of the errors that were
     * registered for the tag.  This method assumes that there are errors in the tag and asserts
     * this is true.  Code will typically call <code>hasErrors</code> before calling this method.
     * @return A <code>String</code> that contains all of the errors registered on this tag.
     */
    public String getErrorsReport(String tagName)
    {
        assert (_error != null) : "No Errors have been reported.";

        String s;
        if (_error instanceof EvalErrorInfo) {
            s = Bundle.getString("Expression_Error");
            s = Bundle.getString("Inline_error",
                    new Object[]{
                        s,
                        Integer.toString(_error.errorNo),
                        tagName,
                    });
        }
        else if (_error instanceof TagErrorInfo) {
            s = Bundle.getString("Tag_Error");
            s = Bundle.getString("Inline_error",
                    new Object[]{
                        s,
                        Integer.toString(_error.errorNo),
                        tagName,
                    });
        }
        else {
            s = null;
            assert true : "Unhandled type";
        }
        return s;
    }
}
