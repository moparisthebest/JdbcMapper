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
import org.apache.beehive.netui.util.internal.ServletUtils;

//import org.apache.beehive.netui.pageflow.util.URLRewriterService;
import org.apache.beehive.netui.tags.html.Html;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptContainer;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.ArrayList;

/**
 * @netui:tag
 */
public abstract class AbstractSimpleTag extends SimpleTagSupport implements INetuiTag
{
    private static final Logger logger = Logger.getInstance(AbstractSimpleTag.class);
    private ErrorHandling _eh;                          // This class will track and handle errors

    /**
     * Return the name of the tag.  Used by error reporting to get the name of the tag.
     * @return the name of the tag.
     */
    public abstract String getTagName();

    /**
     * @param trim
     * @return String
     * @throws JspException
     * @throws IOException
     */
    protected String getBufferBody(boolean trim)
            throws JspException, IOException
    {
        Writer body = new StringWriter(32);
        JspFragment frag = getJspBody();
        if (frag == null)
            return null;
        frag.invoke(body);
        String text = body.toString();
        if (trim && text != null)
            text = text.trim();
        return (text.length() == 0) ? null : text;
    }

    /**
     * Report an error if the value of <code>attrValue</code> is equal to the empty string, otherwise return
     * that value.  If <code>attrValue</code> is equal to the empty string, an error is registered and
     * null is returned.
     * @param attrValue The value to be checked for the empty string
     * @param attrName  The name of the attribute
     * @return either the attrValue if it is not the empty string or null
     * @throws JspException A JspException will be thrown if inline error reporting is turned off.
     */
    protected final String setRequiredValueAttribute(String attrValue, String attrName)
            throws JspException
    {
        assert(attrValue != null) : "parameter '" + attrValue + "' must not be null";
        assert(attrName != null) : "parameter '" + attrName + "' must not be null";

        if ("".equals(attrValue)) {
            String s = Bundle.getString("Tags_AttrValueRequired", new Object[]{attrName});
            registerTagError(s, null);
            return null;
        }
        return attrValue;
    }

    /**
     * Filter out the empty string value and return either the value or null.  When the value of
     * <code>attrValue</code> is equal to the empty string this will return null, otherwise it will
     * return the value of <code>attrValue</code>.
     * @param attrValue This is the value we will check for the empty string.
     * @return either the value of attrValue or null
     */
    protected final String setNonEmptyValueAttribute(String attrValue)
    {
        return ("".equals(attrValue)) ? null : attrValue;
    }

    /**
     * This method will return the user local of the request.
     * @return the Locale object to use when rendering this tag
     */
    protected Locale getUserLocale() {
        return InternalUtils.lookupLocale(getJspContext());
    }

    /**
     * This method will attempt to cast the JspContext into a PageContext.  If this fails,
     * it will log an exception.
     * @return PageContext
     */
    protected PageContext getPageContext()
    {
        JspContext ctxt = getJspContext();
        if (ctxt instanceof PageContext)
            return (PageContext) ctxt;

        // assert the page context and log an error in production
        assert(false) : "The JspContext was not a PageContext";
        logger.error("The JspContext was not a PageContext");
        return null;
    }

    /**
     * This mehod will write the passed string to the response.
     * @param string to be written to the response.
     */
    protected final void write(String string)
            throws JspException
    {
        JspContext ctxt = getJspContext();
        JspWriter writer = ctxt.getOut();
        try {
            writer.print(string);
        }
        catch (IOException e) {
            logger.error(Bundle.getString("Tags_WriteException"), e);
            if (ctxt instanceof PageContext)
                RequestUtils.saveException((PageContext) ctxt, e);
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

    /**
     * This will report an error from a tag.  The error will
     * contain a message.  If error reporting is turned off,
     * the message will be returned and the caller should throw
     * a JspException to report the error.
     * @param message - the message to register with the error
     * @throws javax.servlet.jsp.JspException - if in-page error reporting is turned off this method will always
     *                                        throw a JspException.
     */
    public void registerTagError(String message, Throwable e)
            throws JspException
    {
        ErrorHandling eh = getErrorHandling();
        eh.registerTagError(message, getTagName(), this, e);
    }

    public void registerTagError(AbstractPageError error)
            throws JspException
    {
        ErrorHandling eh = getErrorHandling();
        eh.registerTagError(error, this);
    }

    /**
     * This method will return <code>true</code> if there have been any errors registered on this
     * tag.  Otherwise it returns <code>false</code>
     * @return <code>true</code> if errors have been reported on this tag.
     */
    protected boolean hasErrors()
    {
        return (_eh != null);
    }

    /**
     * This method will write out the <code>String</code> returned by <code>getErrorsReport</code> to the
     * response output stream.
     * @throws JspException if <code>write</code> throws an exception.
     * @see #write
     */
    protected void reportErrors()
            throws JspException
    {
        assert(_eh != null);
        String err = _eh.getErrorsReport(getTagName());
        IErrorCollector ec = (IErrorCollector) SimpleTagSupport.findAncestorWithClass(this, IErrorCollector.class);
        if (ec != null) {
            ec.collectChildError(err);
        }
        else {
            write(err);
        }
    }

    protected String getInlineError()
    {
        return _eh.getInlineError(getTagName());
    }

    /**
     * This method will return an ErrorHandling instance.
     * @return Return the error handler
     */
    private ErrorHandling getErrorHandling()
    {
        if (_eh == null) {
            _eh = new ErrorHandling();
        }
        return _eh;
    }

    /**
     * Return the closest <code>ScriptReporter</code> in the parental chain.  Searching starts
     * at this node an moves upward through the parental chain.
     * @return a <code>ScriptReporter</code> or null if there is not one found.
     */
    protected IScriptReporter getScriptReporter()
    {
        IScriptReporter sr = (IScriptReporter) SimpleTagSupport.findAncestorWithClass(this, IScriptReporter.class);
        return sr;
    }

    /**
     * This method will return the scriptReporter that is represented by the HTML tag.
     * @return IScriptReporter
     */
    protected IScriptReporter getHtmlTag(ServletRequest req)
    {
        Html html = (Html) req.getAttribute(Html.HTML_TAG_ID);
        if (html != null && html instanceof IScriptReporter)
            return (IScriptReporter) html;
        return null;
    }

    /**
     * This method will rewrite the name (id) by passing it to the
     * URL Rewritter and getting back a value.
     * @param name the name that will be rewritten
     * @return a name that has been rewritten by the URLRewriterService.
     */
    final protected String rewriteName(String name)
    {
        PageContext pageContext = getPageContext();
        return URLRewriterService.getNamePrefix(pageContext.getServletContext(), pageContext.getRequest(), name) + name;
    }

    /**
     * This method will generate a real id based upon the passed in tagId.  The generated
     * id will be constucted by searching upward for all the script containers that have a
     * scope id set.  These will form a fully qualified id.
     * @param tagId The base tagId set on a tag
     * @return an id value formed by considering all of the scope id's found in the tag hierarchy.
     */
    final protected String getIdForTagId(String tagId)
    {
        HttpServletRequest req = (HttpServletRequest) getPageContext().getRequest();
        ArrayList/*<String>*/ list = (ArrayList/*<String>*/)
                org.apache.beehive.netui.tags.RequestUtils.getOuterAttribute(req,
                        ScriptContainer.SCOPE_ID);
        if (list == null)
            return tagId;
        InternalStringBuilder sb = new InternalStringBuilder();
        for (int i=0;i<list.size();i++) {
            sb.append((String) list.get(i));
            sb.append('.');
        }
        sb.append(tagId);
        return sb.toString();
    }
}
