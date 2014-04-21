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

import org.apache.beehive.netui.pageflow.ServletContainerAdapter;
import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.pageflow.internal.BindingUpdateError;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Renders the set of error messages found during processPopulate when the
 * values to be updated cannot be resolved.  These errors are treated as
 * warnings.  By default, this tag is only on in Iterative Dev mode and
 * the warning are not displayed in production mode.  The tag is intended
 * for development use.
 * @jsptagref.tagdescription
 * Renders the set of error messages found during the process of resolving
 * data binding expressions (pageFlow.firstname, requestScope.firstname, etc.).
 * The tag is intended for development use and not for error reporting in deployed applications.  When the
 * NetUI framework is not in development mode, this tag will do nothing.
 *
 * @example
 * <p>
 * In this first sample, because the &lt;netui:bindingUpdateErrors/> tag is unqualified,
 * messages will be displayed if <b>any</b> data binding errors occurred when a form was
 * posted. The messages are displayed on the page and the command window.
 * </p>
 * <pre>&lt;netui:bindingUpdateErrors /></pre>
 * <p>
 * In this next sample, only binding errors for the expression <code>{actionForm.firstName}</code>
 * will be displayed on the page and the command window.
 * </p>
 * <pre>&lt;netui:bindingUpdateErrors expression="actionForm.firstName"/></pre>
 * <p>
 * Note, the expression used in this tag needs to match the expressions used to POST form data.  This
 * tag's <code>expression</code> attribute must resolve to a valid NetUI expression.
 * </p>
 * @netui:tag name="bindingUpdateErrors" body-content="empty"
 *            description="Will display a message for all binding update errors that occurred when a form was posted."
 */
public class BindingUpdateErrors extends AbstractSimpleTag
{
    private String _expression = null;
    private boolean _alwaysReport = false;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "BindingUpdateErrors";
    }

    /**
     * Sets the expression to match for binding errors.  If an expression
     * is set, only binding errors for that expression will be displayed.
     * Otherwise, all errors will be displayed.
     * @param expression The expression to match against.
     * @jsptagref.attributedescription String. The data binding expression to match for binding errors.
     * If a data binding expression is specified, only binding errors for that expression will be displayed.
     * Otherwise, all errors will be displayed.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_databinding_expression</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The data binding expression to match for binding errors.  If a data binding expression
     * is specified, only binding errors for that expression will be displayed.
     * Otherwise, all errors will be displayed."
     */
    public void setExpression(String expression)
            throws JspException
    {
        _expression = setRequiredValueAttribute(expression, "expression");
    }

    /**
     * Set the value which will override the default behavior of not showing
     * errors in production mode.
     * @param alwaysReport a boolean that if <code>true</code> will cause
     *                     the errors to always be displayed.  The default is <code>false</code>
     * @jsptagref.attributedescription Boolean. If <code>isAlwaysReport</code> is set to true, then the errors will be displayed in Production mode
     * as well as in Development mode.  Otherwise, the errors will be displayed only in Development mode.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_alwaysReport</i>
     * @netui:attribute required="false"  rtexprvalue="true"  type="boolean"
     * description="If isAlwaysReport is set to true, then the errors will be displayed in Production mode
     * as well as in Development mode.  Otherwise, the errors will be displayed only in Development mode."
     */
    public void setAlwaysReport(boolean alwaysReport)
    {
        _alwaysReport = alwaysReport;
    }

    /**
     * Render the specified error messages if there are any.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        // report error if there are any
        if (hasErrors()) {
            reportErrors();
            return;
        }

        PageContext pageContext = getPageContext();
        ServletContainerAdapter sa = AdapterManager.getServletContainerAdapter(pageContext.getServletContext());
        ServletRequest request = pageContext.getRequest();
        assert(sa != null);

        // check to see if we are supposed to report the error
        boolean prodMode = sa.isInProductionMode();
        if (prodMode && !_alwaysReport)
            return;

        LinkedHashMap map = (LinkedHashMap)
                InternalUtils.getBindingUpdateErrors(request);

        if (map == null)
            return;

        if (_expression != null) {
            String expr = "{" + _expression + "}";
            BindingUpdateError err = (BindingUpdateError) map.get(expr);
            if (err != null) {
                Throwable cause = err.getCause();
                String msg = (cause != null) ? cause.getMessage() : err.getMessage();
                String s = Bundle.getString("Tags_BindingUpdateError", new Object[]{_expression, msg});
                registerTagError(s, null);
                reportErrors();
            }
            return;
        }

        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            BindingUpdateError err = (BindingUpdateError) it.next();
            Throwable cause = err.getCause();
            String msg = (cause != null) ? cause.getMessage() : err.getMessage();
            String s = Bundle.getString("Tags_BindingUpdateError", new Object[]{err.getExpression(), msg});
            registerTagError(s, null);
        }
        reportErrors();
    }
}
