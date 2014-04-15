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

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.script.ExpressionEvaluationException;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptContainer;
import org.apache.beehive.netui.tags.naming.FormDataNameInterceptor;
import org.apache.beehive.netui.tags.naming.INameInterceptor;
import org.apache.beehive.netui.tags.naming.IndexedNameInterceptor;
import org.apache.beehive.netui.tags.html.Form;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.util.*;

/**
 * AbstractBaseTag is the base tag for most of the NetUI tags.  This tag provides default behavior
 * and services for NetUI tags.  There following categories of services are provided by this tag:
 * <ul>
 * <li><b>Generic Services</b> -- These are general services such as access to local, writting
 * to the response, and writting attributes.</li>
 * <li><b>Expression Management</b> -- This set of method provide evaluation and information about
 * expressions.  These methods allow tags to fully support expressions for attributes.</li>
 * <li><b>Naming and NamingInterceptor Services</b> -- This set of methods will apply
 * <code>INameInterceptor</code>s to a name to produce the name written out.  In addition, it allows
 * the URL Rewritter service to modify names.</li>
 * <li><b>Attribute Rendering Support</b> -- This set of routine allow tags to keep simple attributes
 * in a map that can be written into the generated markup.  There are two types of attributes, attributes
 * that contain expression and attributes that do not contain expression supported.</li>
 * <li><b>Error Reporting</b> -- This set of routines will report errors within the tags. In development
 * mode errors are reported in-page.</li>
 * <li><b>JavaScript Services</b> -- This set of routines provide simple access for JavaScript generation.</li>
 * </ul>
 * @netui:tag
 */
public abstract class AbstractClassicTag
        extends BodyTagSupport implements INetuiTag
{
    //@todo: need to implement the flag to turn errors into JSP exceptions

    private static final Logger logger = Logger.getInstance(AbstractClassicTag.class);

    /**
     * This List represents the default naming chain for handling <code>dataSource</code> attributes.  The list
     * is a read-only list which may be used by any <code>dataSource</code> implementation.
     */
    public static final List DefaultNamingChain;

    /**
     * This is the name of a request scoped attribute which creates a unique id for processing
     * a request.
     */
    public static final String NETUI_UNIQUE_CNT = "netui.unique.id";

    /**
     * This is a private formRewriter which is used by <code>qualifyName</code> to provide
     * struts naming behavior.
     */
    private static final INameInterceptor formRewriter = new FormDataNameInterceptor();

    /**
     * String constant for the empty string.
     */
    protected static final String EMPTY_STRING = "";

    // create the default naming chain.
    static
    {
        List l = new ArrayList(2);
        l.add(new FormDataNameInterceptor());
        l.add(new IndexedNameInterceptor());
        DefaultNamingChain = Collections.unmodifiableList(l);
    }

    private ErrorHandling _eh;                  // This class will track and handle errors

    /////////////////////////// Generic Services support ////////////////////////////

    /**
     * Return the name of the tag.  Used by error reporting to get the name of the tag.
     * @return the name of the tag.
     */
    public abstract String getTagName();

    /**
     * This is a method that will reinitialize all temporary state on a
     * tag and should be called in the doEndTag method.
     */
    protected void localRelease()
    {
        _eh = null;
    }

    /**
     * This method will return the user local of the request.
     * @return the Locale object to use when rendering this tag
     */
    protected Locale getUserLocale() 
    {
        return InternalUtils.lookupLocale(pageContext.getRequest());
    }

    /**
     * This mehod will write the passed string to the response.
     * @param string to be written to the response.
     */
    protected final void write(String string)
    {
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(string);
        }
        catch (IOException e) {
            logger.error(Bundle.getString("Tags_WriteException"), e);
            org.apache.struts.util.RequestUtils.saveException((PageContext) pageContext, e);
        }
    }

    ///////////////////////////  Naming and NamingInterceptor support ////////////////////////////

    /**
     * Return an <code>List</code> which represents a chain of <code>INameInterceptor</code>
     * objects.  This method by default returns <code>null</code> and should be overridden
     * by objects that support naming.
     * @return an <code>List</code> that will contain <code>INameInterceptor</code> objects.
     */
    protected List getNamingChain()
    {
        return null;
    }

    /**
     * This method walks all of the naming chains and allows them to rewrite the <code>name</code> parameter.
     * After the naming chain processes the name, it will be passed to <code>rewriteName</code> for final processing.
     * If the naming chaing returned from <code>getNamingChain</code> returns null, the name will be passed to
     * <code>rewriteName</code> and returned.  If there is an <code>ExpressionEvaluationException</code> thrown
     * by a <code>INameInterceptor</code>, the error will be registered with the tag and <code>null</code> will
     * be returned.
     * @param name the name to rewrite
     * @return the name after it was passed to all <code>INameInterceptor</code>s in the naming chain.
     * @see #rewriteName
     * @see org.apache.beehive.netui.tags.naming.INameInterceptor
     */
    protected String applyNamingChain(String name)
            throws JspException
    {
        assert (name != null) : "The name parameter may not be null";

        List namingChain = getNamingChain();
        if (namingChain == null)
            return rewriteName(name);

        //if (logger.isDebugEnabled())
        //    logger.debug("rewrite name \"" + name + "\" on tag of type \"" + getClass().getName() + " with namingChain " +
        //            (namingChain != null ? "size " + namingChain.size() : "null"));

        try {
            String newName = name;
            int cnt = namingChain.size();
            for (int i = 0; i < cnt; i++) {
                //if (logger.isDebugEnabled())
                //    logger.debug("rewriteName: \"" + newName + "\" with INameInterceptor: " + namingChain.get(i).getClass().getName());

                newName = ((INameInterceptor) namingChain.get(i)).rewriteName(newName, this);

                //if (logger.isDebugEnabled())
                //    logger.debug("rewrite result: " + newName);
            }

            return rewriteName(newName);
        }
        catch (ExpressionEvaluationException ee) {
            // if there is an expression evaluation error set the error and return null;
            logger.error(Bundle.getString("Tags_ExpressionQualifyingFailure", name));

            // create the expression info an add it to the error tracking
            EvalErrorInfo info = new EvalErrorInfo();
            info.evalExcp = ee;
            info.expression = name;
            info.attr = "dataSource";
            info.tagType = getTagName();

            // report the error
            registerTagError(info);
            return null;
        }
    }

    /**
     * An internal method that allows a tag to qualify the <code>name</code> paramater by converting
     * it from a struts style naming convention to an explicit databinding expression.  The qualified
     * name will be returned. This method may report an error if there is an error in the expression.
     * @param name the name to be qualified
     * @return the name which has been qualified
     * @throws JspException throws a JspException if in-page error reporting is turned off.
     * @see org.apache.beehive.netui.tags.naming.FormDataNameInterceptor
     */
    protected String qualifyAttribute(String name)
            throws JspException
    {
        if (name == null)
            return null;

        // if this is a Struts style name, convert it to an expression
        try {
            name = formRewriter.rewriteName(name, this);
        }
        catch (ExpressionEvaluationException e) {
            String s = Bundle.getString("Tags_DataSourceExpressionError", new Object[]{name, e.toString()});
            registerTagError(s, null);
        }
        return name;
    }

    /**
     * This method will rewrite the name (id) by passing it to the
     * URL Rewritter and getting back a value.
     * @param name the name that will be rewritten
     * @return a name that has been rewritten by the URLRewriterService.
     */
    final protected String rewriteName(String name)
    {
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
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        ArrayList/*<String>*/ list = (ArrayList/*<String>*/)
                RequestUtils.getOuterAttribute(req, ScriptContainer.SCOPE_ID);
        if (list == null)
            return tagId;
        InternalStringBuilder sb = new InternalStringBuilder();
        for (int i=0;i<list.size();i++) {
            sb.append((String)list.get(i));
            sb.append('.');
        }
        sb.append(tagId);
        return sb.toString();


        /*
        Tag tag = this;
        while (tag != null) {
            if (tag instanceof ScriptContainer) {
                String sid = ((ScriptContainer) tag).getIdScope();
                if (sid != null) {
                    tagId = sid + "." + tagId;
                }
            }
            tag = tag.getParent();
        }
        return tagId;
        */
    }

    ///////////////////////////  Generic Attribute Setting Support  ////////////////////////////

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
        assert(attrValue != null) : "parameter 'attrValue' must not be null";
        assert(attrName != null) : "parameter 'attrName' must not be null";

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

    ///////////////////////////  Generic Error Reporting Support  ////////////////////////////

    /**
     * This is a simple routine which will call the error reporter if there is an
     * error and then call local release before returning the <code>returnValue</code>.
     * This is a very common code sequence in the Classic Tags so we provide this routine.
     * @param returnValue The value that will be returned.
     * @return <code>returnValue</code> is always returned.
     * @throws JspException
     */
    protected int reportAndExit(int returnValue)
            throws JspException
    {
        if (hasErrors()) {
            reportErrors();
        }
        localRelease();
        return returnValue;
    }

    /**
     * This will report an error from a tag.  The error will
     * contain a message.  If error reporting is turned off,
     * the message will be returned and the caller should throw
     * a JspException to report the error.
     * @param message - the message to register with the error
     * @throws JspException - if in-page error reporting is turned off this method will always
     *                      throw a JspException.
     */
    public void registerTagError(String message, Throwable e)
            throws JspException
    {
        ErrorHandling eh = getErrorHandling();
        eh.registerTagError(message, getTagName(), this, e);
    }

    /**
     * This will report an error from a tag.  The error must
     * be be an AbstractPageError.
     * @param error The <code>AbstractPageError</code> to add to the error list.
     * @throws JspException - if in-page error reporting is turned off this method will always
     *                      throw a JspException.
     */
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

    /**
     * This method will return a <code>String<code> that represents all of the errors that were
     * registered for the tag.  This method assumes that there are errors in the tag and asserts
     * this is true.  Code will typically call <code>hasErrors</code> before calling this method.
     * @return A <code>String</code> that contains all of the errors registered on this tag.
     */
    protected String getErrorsReport()
    {
        assert _eh != null;
        return _eh.getErrorsReport(getTagName());
    }


    /**
     * This method will return an ErrorHandling instance.
     * @return Return the ErrorHandling object
     */
    private ErrorHandling getErrorHandling()
    {
        if (_eh == null) {
            _eh = new ErrorHandling();
        }
        return _eh;
    }

    ///////////////////////////  JavaScript Support Support  ////////////////////////////

    /**
     * Return the closest <code>ScriptReporter</code> in the parental chain.  Searching starts
     * at this node an moves upward through the parental chain.
     * @return a <code>ScriptReporter</code> or null if there is not one found.
     */
    protected IScriptReporter getScriptReporter()
    {
        return (IScriptReporter) SimpleTagSupport.findAncestorWithClass(this, IScriptReporter.class);
    }

    ///////////////////////////  Misc Features Support  ////////////////////////////

    /**
     * This method will generate the next unique int within the HTML tag.
     * @param req the Request
     * @return the next unique integer for this request.
     */
    protected int getNextId(ServletRequest req)
    {
        Integer i = (Integer) RequestUtils.getOuterAttribute((HttpServletRequest) req,NETUI_UNIQUE_CNT);
        if (i == null) {
            i = new Integer(0);
        }

        int ret = i.intValue();
        RequestUtils.setOuterAttribute((HttpServletRequest) req,NETUI_UNIQUE_CNT, new Integer(ret + 1));
        return ret;
    }

    /**
     * Returns the closest parent form tag, or null if there is none.
     */
    protected Form getNearestForm()
    {
        Tag parentTag = getParent();
        while (parentTag != null) {
            if (parentTag instanceof Form)
                return (Form) parentTag;
            parentTag = parentTag.getParent();
        }
        return null;
    }
}
