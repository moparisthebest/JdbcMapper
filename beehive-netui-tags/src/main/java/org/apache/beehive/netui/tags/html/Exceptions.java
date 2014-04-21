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

import org.apache.beehive.netui.pageflow.internal.AdapterManager;
import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.util.HtmlExceptionFormatter;
import org.apache.struts.Globals;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Renders formatted exception data, as found in the Request with the key:
 * <code>org.apache.struts.action.Action.EXCEPTION_KEY</code>.  Exceptions ignores its
 * body content.
 * @jsptagref.tagdescription Renders exception messages and stack traces inline on the JSP page.
 * @example In this sample, the &lt;netui:exceptions> tag will output the exception title and message,
 * but not the stacktraces.
 * <pre>&lt;netui:exceptions showMessage="true" showStackTrace="false" /></pre>
 * @netui:tag name="exceptions" body-content="empty" description="Displays formatted exception messages."
 */
public class Exceptions extends AbstractSimpleTag
{
    private boolean _showMessage = true;
    private boolean _showStackTrace = false;
    private boolean _showDevModeStackTrace = false;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Exceptions";
    }

    /**
     * Set whether or not the exception message is being shown.
     * @param showMessage true or false depending on the setting desired
     * @jsptagref.attributedescription Boolean. Determines whether or not the exception message is shown. Defaults to <code>true</code>
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_showMessage</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines whether or not the exception message is shown. Defaults to true."
     */
    public void setShowMessage(boolean showMessage)
    {
        _showMessage = showMessage;
    }

    /**
     * Set whether or not the stack trace is being shown.
     * @param showStackTrace true or false depending on the setting desired
     * @jsptagref.attributedescription Boolean.  Determines whether or not the stack trace is shown. Defaults to <code>false</code>
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_showStackTrace</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines whether or not the stack trace is shown. Defaults to false."
     */
    public void setShowStackTrace(boolean showStackTrace)
    {
        _showStackTrace = showStackTrace;
    }

    /**
     * Set whether or not the stack trace is being shown.
     * @param showStackTrace true or false depending on the setting desired
     * @jsptagref.attributedescription Boolean. Determine if the stack trace is display only when dev mode
     * is on.  Default is <code>true</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_showDevModeStackTrace</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determine if the stack trace is display only when dev mode is on.  Default is true."
     */
    public void setShowDevModeStackTrace(boolean showStackTrace)
    {
        _showDevModeStackTrace = showStackTrace;
    }

    /**
     * Render the exception text based on the display attributes.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        // First look for the exception in the pageflow/struts request attribute.  If it's not there,
        // look for it in the request attribute the container provides for web.xml-configured error
        // pages.
        InternalStringBuilder results = new InternalStringBuilder(128);
        PageContext pageContext = getPageContext();
        Throwable e = (Throwable) pageContext.getAttribute(Globals.EXCEPTION_KEY, PageContext.REQUEST_SCOPE);

        if (e == null) {
            ServletRequest req = pageContext.getRequest();
            e = (Throwable) req.getAttribute("javax.servlet.error.exception");
            if (e == null) {
                e = (Throwable) req.getAttribute("javax.servlet.jsp.jspException");
            }
        }

        if (!_showStackTrace && _showDevModeStackTrace) {
            boolean devMode = !AdapterManager.getServletContainerAdapter(pageContext.getServletContext()).isInProductionMode();
            if (devMode)
                _showStackTrace = true;
        }

        if (e != null) {
            if (_showMessage) {
                String msg = e.getMessage();
                // if we have message lets output the exception name and the name of the
                if ((msg != null) && (msg.length() > 0)) {
                    if (!_showStackTrace)
                        msg = e.getClass().getName() + ": " + msg;
                    results.append(HtmlExceptionFormatter.format(msg, e, _showStackTrace));
                }
                else {
                    results.append(HtmlExceptionFormatter.format(e.getClass().getName(), e, _showStackTrace));
                }
            }
            else {
                results.append(HtmlExceptionFormatter.format(null, e, _showStackTrace));
            }
            ResponseUtils.write(pageContext, results.toString());
        }
    }
}
