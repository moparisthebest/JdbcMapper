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
 * Renders the set of error messages found in the ActionMessages
 * registered in the pageContext at org.apache.struts.action.Action.ERROR_KEY.
 * Errors ignores its body content.
 * <p>
 * The following optional message keys will be utilized if corresponding
 * messages exist for them in the application resources:
 * <ul>
 * <li><b>errors.header</b> - If present, the corresponding message will be
 * rendered prior to the individual list of error messages.</li>
 * <li><b>errors.footer</b> - If present, the corresponding message will be
 * rendered following the individual list of error messages.</li>
 * <li><b>errors.prefix</b> - If present, the corresponding message will be
 * rendered before each individual error message.</li>
 * <li><b>errors.suffix</b> - If present, the corresponding message will be
 * rendered after each individual error message.</li>
 * </ul>
 * @jsptagref.tagdescription Renders the set of error messages found in the ActionMessages
 * registered in the <code>PageContext</code> at org.apache.struts.action.Action.ERROR_KEY.
 * <p>
 * The following optional message keys will be utilized if corresponding
 * messages exist for them in the application resources:
 * <blockquote>
 * <ul>
 * <li><b>errors.header</b> - If present, the corresponding message will be
 * rendered prior to the individual list of error messages.</li>
 * <li><b>errors.footer</b> - If present, the corresponding message will be
 * rendered following the individual list of error messages.</li>
 * <li><b>errors.prefix</b> - If present, the corresponding message will be
 * rendered before each individual error message.</li>
 * <li><b>errors.suffix</b> - If present, the corresponding message will be
 * rendered after each individual error message.</li>
 * </ul>
 * </blockquote>
 * @example In this sample, the messages from the <code>errorMessages</code> bundle will be used to
 * output the errors.
 * <pre>
 * &lt;netui:errors bundleName="errorMessages" /></pre>
 * @netui:tag name="errors" body-content="empty" description="Used to report multiple validation errors."
 */
public class Errors extends ErrorBaseTag
{
    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Errors";
    }

    /**
     * Render the specified error messages if there are any.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
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

        if ((errors == null) || errors.isEmpty()) {
            if (hasErrors())
                reportErrors();
            return;
        }

        String qualifiedBundle = InternalUtils.getQualifiedBundleName(_bundleName, pageContext.getRequest());

        boolean headerPresent = false;
        boolean footerPresent = false;
        boolean prefixPresent = false;
        boolean suffixPresent = false;

        String locale = _locale;
        if (!isMissingUserDefaultMessages(pageContext)) {
            try {
                // Check for presence of header and footer message keys
                headerPresent =
                        RequestUtils.present(pageContext, qualifiedBundle, locale, "errors.header");
                footerPresent =
                        RequestUtils.present(pageContext, qualifiedBundle, locale, "errors.footer");
                prefixPresent =
                        RequestUtils.present(pageContext, qualifiedBundle, locale, "errors.prefix");
                suffixPresent =
                        RequestUtils.present(pageContext, qualifiedBundle, locale, "errors.suffix");
            }
            catch (JspException e) {
                String s = Bundle.getString("Tags_ErrorsException",
                        new Object[]{e.getMessage()});
                registerTagError(s, null);
                reportErrors();
                return;
            }
        }

        // Render the error messages appropriately
        InternalStringBuilder results = new InternalStringBuilder(128);
        boolean headerDone = false;
        String message = null;
        Iterator reports = null;
        reports = errors.get();

        while (reports.hasNext()) {
            ActionMessage report = (ActionMessage) reports.next();
            if (!headerDone) {
                if (headerPresent) {
                    message = RequestUtils.message(pageContext, qualifiedBundle, locale, "errors.header");
                    results.append(message);
                    results.append("\r\n");
                }
                headerDone = true;
            }
            if (prefixPresent) {
                message = RequestUtils.message(pageContext, qualifiedBundle, locale, "errors.prefix");
                results.append(message);
            }
            message = getErrorMessage(report, qualifiedBundle);
            if (message != null) {
                results.append(message);
                results.append("\r\n");
            }
            if (suffixPresent) {
                message = RequestUtils.message(pageContext, qualifiedBundle, locale, "errors.suffix");
                results.append(message);
            }
        }
        if (headerDone && footerPresent) {
            message = RequestUtils.message(pageContext, qualifiedBundle, locale, "errors.footer");
            results.append(message);
            results.append("\r\n");
        }

        if (hasErrors())
            reportErrors();

        // Print the results to our output writer
        write(results.toString());
    }
}
