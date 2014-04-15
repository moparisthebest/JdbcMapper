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
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.Iterator;

/**
 * Renders an error message with a given error key if that key can be found in the
 * ActionMessages registered in the <code>pageContext</code> at
 * <code>org.apache.struts.action.Action.ERROR_KEY</code>. Error ignores its body content.
 * <p>
 * The following optional message keys will be utilized if corresponding
 * messages exist for them in the application resources:
 * <ul>
 * <li><b>error.prefix</b> - If present, the corresponding message will be
 * rendered before each individual error message.</li>
 * <li><b>error.suffix</b> - If present, the corresponding message will be
 * rendered after each individual error message.</li>
 * </ul>
 * @jsptagref.tagdescription Renders an error message with a given error key if that key can be found in the
 * ActionMessages registered in the <code>PageContext</code> at
 * <code>org.apache.struts.action.Action.ERROR_KEY</code>.
 *
 * <p>The following optional message keys will be utilized if corresponding
 * messages exist for them in the application resources:
 * <blockquote>
 * <ul>
 * <li><b>error.prefix</b> - If present, the corresponding message will be
 * rendered before each individual error message.</li>
 * <li><b>error.suffix</b> - If present, the corresponding message will be
 * rendered after each individual error message.</li>
 * </ul>
 * </blockquote>
 * @example In this sample, the "InvalidName" message from the errorMessages bundle will be used to output the error.
 * <pre>
 * &lt;netui:error bundleName="errorMessages" key="InvalidName"/></pre>
 * @netui:tag name="error" body-content="empty" description="Renders an error message with a given error key."
 */
public class Error extends ErrorBaseTag
{
    private String _key = null; // error key name, may be an expression

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Error";
    }

    /**
     * Set the key under which the error was stored (often the name of the form bean property associated with the error).
     * @param key the key under which the error was stored
     * @jsptagref.attributedescription The key under which the error was stored (often the name of the form bean property associated with the error)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_key</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The key under which the error was stored (often the name of the form bean property associated with the error)"
     */
    public void setKey(String key)
            throws JspException
    {
        _key = setRequiredValueAttribute(key, "key");
    }

    /**
     * Render the specified error message if it can be found.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        // Error will try an always work even if there are expression
        // errors.  The error will be reported at the end.

        PageContext pageContext = getPageContext();

        // Were any error messages specified?
        ActionMessages errors = null;
        try {
            errors = RequestUtils.getActionMessages(pageContext, Globals.ERROR_KEY);
        }
        catch (JspException e) {
            RequestUtils.saveException(pageContext, e);
            throw e;
        }

        // report any expression errors
        if ((errors == null) || errors.isEmpty()) {
            if (hasErrors())
                reportErrors();
            return;
        }

        String qualifiedBundle = InternalUtils.getQualifiedBundleName(_bundleName, pageContext.getRequest());

        boolean prefixPresent = false;
        boolean suffixPresent = false;
        String locale = _locale;

        if (!isMissingUserDefaultMessages(pageContext)) {
            try {
                // Check for presence of error prefix and suffix message keys
                prefixPresent =
                        RequestUtils.present(pageContext, qualifiedBundle, locale, "error.prefix");
                suffixPresent =
                        RequestUtils.present(pageContext, qualifiedBundle, locale, "error.suffix");
            }
            catch (JspException e) {
                String s = Bundle.getString("Tags_ErrorsException", new Object[]{e.getMessage()});
                registerTagError(s, null);
                reportErrors();
                return;
            }
        }

        // Render the error message appropriately
        InternalStringBuilder results = new InternalStringBuilder(128);

        String message = null;
        Iterator reports = null;
        reports = errors.get(_key);

        while (reports.hasNext()) {
            ActionMessage report = (ActionMessage) reports.next();
            if (prefixPresent) {
                message = RequestUtils.message(pageContext, qualifiedBundle, locale, "error.prefix");
                results.append(message);
            }

            message = getErrorMessage(report, qualifiedBundle);

            if (message != null) {
                results.append(message);
                results.append("\r\n");
            }
            if (suffixPresent) {

                message = RequestUtils.message(pageContext, qualifiedBundle, locale, "error.suffix");
                results.append(message);
            }
        }

        write(results.toString());

        // report any expression errors
        if (hasErrors())
            reportErrors();
    }
}
