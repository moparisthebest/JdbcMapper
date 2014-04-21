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
package org.apache.beehive.netui.tags.template;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.AbstractPageError;
import org.apache.beehive.netui.tags.IErrorReporter;
import org.apache.beehive.netui.tags.html.Html;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This tags defines the template to use within a content page.  The
 * content page interacts with the template page through children tags
 * of the
 * <code>Template</code> tag.  The legal children are as follows:
 * <ul>
 * <li> <code>setAttribute</code> -- A Tag that will set an Attribute on the
 *      template.
 * <li> <code>section</code> -- A tag that defines the content of a section
 *      defined in the template.
 * </ul>
 * The URL of the template file is set as the <code>templatePage</code>
 *      attribute on the
 *      <code>Template</code> tag.  The Template file is included from
 *      the <code>Template</code> tag and will include sections defined
 *      in the content page.  The content is contained in one or more
 *      <code>Section</code> tags, which are children of the
 *      <code>Template</code> tag.  In addition, the content page can set
 *      attributes of the template page.

 * @jsptagref.tagdescription
 * Points a content page at its template page.  A
 * content page interacts with its template page through children tags
 * of the 
 * &lt;netui-template:template> tag.  The legal children are as follows:
 * <blockquote>
 * <ul>
 * <li> {@link SetAttribute} -- a tag that fills a 
 *      {@link Attribute} placeholder with content.
 * <li> {@link Section} -- a tag that fills a
 *      {@link IncludeSection} placeholder with content.
 * </ul>
 * </blockquote>
 * <p>The URL of the template page is specified by the <code>templatePage</code>
 *      attribute on the
 *      &lt;netui-template:template> tag.
 * 
 * @example
 * The following example shows a content page that adopts the template.jsp page as its template.
 * The content page also sets the "title" attribute on the template.
 * 
 *    <pre>    &lt;netui-template:template templatePage="./template.jsp">
 *    ...
 *        &lt;netui-template:setAttribute name="title" value="Template Tags Sample"/>
 *    ...
 *    &lt;/netui-template:template></pre>
 *    
 * @netui:tag name="template"
 *          description="Use this tag to associate a JSP page with a particular template file."
 */
public class Template extends AbstractClassicTag
        implements TemplateConstants, IErrorReporter
{
    private static final Logger logger = Logger.getInstance(Template.class);

    public static class TemplateContext
    {
        HashMap secs = null;
    }

    /**
     * Saved context for the nested case
     */
    private TemplateContext _savedContext = null;

    private boolean _fatalError = false;

    /**
     * Inner class that is exposed to handle errors
     */
    private org.apache.beehive.netui.tags.IErrorReporter _innerErrors;

    /**
     * Returns the name of the Tag.  This is used to
     * identify the type of tag reporting errors.
     */
    public String getTagName() {
        return "Template";
    }

    /**
     * The URL of the template file...
     */
    private String _templatePage;

    /**
     * boolean indicating the error reporting.
     */
    private boolean _reportErrors = false;

    /**
     * Set the boolean indicating that the template should report errors.  The
     * errors will be reported through a HTML tag that must be found
     * in the template JSP.
     * @param reportErrors boolean indicating that errors should be reported
     *
     * @jsptagref.attributedescription
     * Boolean. Determines if the template should report errors.  
     * The errors will be reported inline on the JSP page.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>boolean_reportErrors</i>
     *
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Determines if the template should report errors.
     * The errors will be reported inline on the JSP page."
     */
    public void setReportErrors(boolean reportErrors) {
        _reportErrors = reportErrors;
    }

    /**
     * Set the URL of the template to use.  The <code>templatePage</code>
     * is an URL which
     * identifies the JSP template page.
     * @param templatePage - a URL pointing to a JSP page that represents the
     *  template.
     *
     * @jsptagref.attributedescription
     * The URL of the template page to use.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_urlToTemplatePage</i>
     *
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The URL of the template page to use."
     */
    public void setTemplatePage(String templatePage) {
        _templatePage = templatePage;
    }

    /**
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the document type (html4-loose or xhtml1-transitional) of the document."
     */
    public void setDocumentType(String docType)
    {
        int rendering = 0;
        if (docType != null) {
            if (docType.equals(Html.HTML_401))
                rendering = TagRenderingBase.HTML_RENDERING;
            else if (docType.equals(Html.HTML_401_QUIRKS))
                rendering = TagRenderingBase.HTML_RENDERING_QUIRKS;
            else if (docType.equals(Html.XHTML_10))
                rendering = TagRenderingBase.XHTML_RENDERING;
            else
                rendering = TagRenderingBase.getDefaultDocType();
        }
        else {
            rendering = TagRenderingBase.getDefaultDocType();
        }
        pageContext.getRequest().setAttribute(Html.DOC_TYPE_OVERRIDE, new Integer(rendering));
    }

    /**
     * the tag extension lifecycle method called when the tag is first
     * encountered.  This will cause the body of
     * the tag to be evaluated.
     * @return int indicating that the body should be evaluated.
     * @throws JspException on errors.
     */
    public int doStartTag()
            throws JspException {

        Tag parent = getParent();
        if (parent != null) {
            String s = Bundle.getString("TempExcp_ContainedTemplate");
            registerTagError(s,null);
            reportErrors();
            _fatalError = true;
            return SKIP_BODY;
        }

        ServletRequest req = pageContext.getRequest();
        _savedContext = (TemplateContext) req.getAttribute(TEMPLATE_SECTIONS);
        TemplateContext ctxt = new TemplateContext();
        req.setAttribute(TEMPLATE_SECTIONS,ctxt);

        return EVAL_BODY_INCLUDE;
    }

    /**
     * The tag extension lifecycle method called after the tag has
     * processed the body.  This method will include the template
     * JSP page specified by the <code>templatePage</code> attribute.  The
     * contents of the template are made available to the template page.
     * @return SKIP_PAGE to skip all processing after the template.
     * @throws JspException on all errors.  The most common error is
     *  an error indicating that the JSP page representing the Template
     *  isn't found.
     */
    public int doEndTag()
            throws JspException {

        // if there was an error, exit
        if (_fatalError) {
            localRelease();
            return EVAL_PAGE;
        }

        // get the request and response
        ServletRequest req = pageContext.getRequest();
        ServletResponse resp = pageContext.getResponse();

        if (_innerErrors != null) {
            req.setAttribute(CONTAINER_ERRORS,_innerErrors);
        }

        String realURI = getRealURI((HttpServletRequest) req,_templatePage);
        if (!templateExists(realURI)) {
            String s = Bundle.getString("TempExcp_MissingTemplate",
                    new Object[]{_templatePage});
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_BODY;
        }

        RequestDispatcher rd = req.getRequestDispatcher(realURI);
        if (rd == null) {
            String s = Bundle.getString("Temp_RequestDispatcherReturnNull",new Object[]{"realURI"});
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_PAGE;
        }
        try {
            // dispatch to the template itself...
            JspWriter out = pageContext.getOut();
            out.flush();
            // We have to make sure that the Page Flow framework doesn't use the Servlet Include path as the request
            // URI while we're rendering a template page.  This is so that rendered URLs are relative to the current
            // page, not the included template page.
            InternalUtils.setIgnoreIncludeServletPath( req, true );
            try {
                rd.include(req,resp);
            }
            finally {
                InternalUtils.setIgnoreIncludeServletPath( req, false );
            }
        }
        catch (IOException e) {
            String s = Bundle.getString("TempExcp_ExceptIncludeTemplate",
                    new Object[]{"IOException",
                                 _templatePage});
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_PAGE;
        }
        catch (ServletException se) {

            System.err.println("Servlet Excepiton");

            // Report the servlet exception
            String s = Bundle.getString("TempLog_ServletException",
                    new Object[]{se.getMessage()});
            registerTagError(s,null);

            // walk the servlet hierarchy
            Throwable t = se.getRootCause();
            if (t == null) {
                s = Bundle.getString("TempLog_ServletError",
                        new Object[]
                        {_templatePage,
                         se.getMessage()
                        });
                registerTagError(s,null);
                reportErrors();
                localRelease();
                return SKIP_PAGE;
            }

            // Walk all of the errors
            while (t != null && (t instanceof ServletException ||
                    t instanceof JspException)) {

                s = Bundle.getString("TempLog_Cause",
                        new Object[]
                        {t.getClass().getName(),
                         t.getMessage()
                        });
                logger.error(s);

                if (t.getMessage() == null) {
                    logger.error("Unwinding Servlet Exception",t);
                    t.printStackTrace();
                }
                if (t instanceof ServletException)
                    t = ((ServletException) t).getRootCause();
                else
                    t = ((JspException) t).getRootCause();
            }
            if (t == null) {
                s = Bundle.getString("TempLog_ServletError",
                        new Object[]
                        {_templatePage,
                         se.getMessage()
                        });
                registerTagError(s,null);
                reportErrors();
                logger.error(s);
                localRelease();
                return SKIP_PAGE;
            }
            if (t instanceof AssertionError) {
                s = Bundle.getString("TempLog_AssertCause",
                        new Object[]
                        {t.getStackTrace().toString(),
                        });
                registerTagError(s,null);
            }
            else {
                s = Bundle.getString("TempLog_Cause",
                        new Object[]
                        {t.getClass().getName(),
                         t.getMessage()
                        });
                registerTagError(s,null);
            }
            s = Bundle.getString("TempExcp_ExceptIncludeTemplate",
                    new Object[]{"ServletException",
                                 _templatePage});
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_PAGE;
        }

        // skip the page because on this pass we forwarded to the template
        // for rendering...
        req.setAttribute(TEMPLATE_SECTIONS,_savedContext);
        localRelease();
        return SKIP_PAGE;
    }

    private boolean templateExists(String realURI)
        throws JspException
    {
        try {
            URL uri = pageContext.getServletContext().getResource(realURI);
            return (uri != null);
        }
        catch (MalformedURLException e) {
            String s = Bundle.getString("TempExcp_ExceptIncludeDefault",
                                        new Object[]{"MalformedURLException",
                                            realURI});
            logger.error(s,e);
            JspException jspException = new JspException(s, e);
            // todo: future cleanup
            // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
            // this will cause an IllegalStateException on the following call.
            if (jspException.getCause() == null) {
                jspException.initCause(e);
            }
            throw jspException;
        }
    }

    static String getRealURI(HttpServletRequest req,String uri) {
        if (uri.charAt(0) == '/') {
            return uri;
        }
        String path = req.getServletPath();
        int pos = path.lastIndexOf('/');
        if (pos != -1) {
            path = path.substring(0,pos+1);
        }
        return path+uri;
    }

    /**
     * Reset all of the fields of the tag.
     */
    protected void localRelease() {
        super.localRelease();
        _fatalError = false;
        _templatePage = null;
        _innerErrors = null;
        _reportErrors = false;
        _savedContext = null;
    }

    /**
     * Add an error to the errors being reported by this tag.
     * @param ape - The AbstractPageError to add
     */
    public void addError(AbstractPageError ape) {
        if (_innerErrors == null) {
            _innerErrors = new InnerErrorReporter();
        }
        _innerErrors.addError(ape);
    }

    /**
     * Return an ArrayList of the errors
     * @return an <code>ArrayList</code> of all errors.
     */
    public ArrayList returnErrors() {
        if (_innerErrors == null) {
            _innerErrors = new InnerErrorReporter();
        }
        return _innerErrors.returnErrors();
    }

    /**
     * This boolean indicates if an ErrorReporter is reporting errors
     * or not.  The caller should check this before calling addError
     * because the ErrorReporter may be off for some reason.
     * @return a boolean indicating if the tag is reporting errors or not.
     */
    public boolean isReporting() {
        return _reportErrors;
    }

    static class InnerErrorReporter implements org.apache.beehive.netui.tags.IErrorReporter
    {
        /**
         * The errors reported by contained tags
         */
        private ArrayList _errors;

        /**
         * Add an error to the errors being reported by this tag.
         * @param ape - The AbstractPageError to add
         */
        public void addError(AbstractPageError ape) {
            assert (ape != null);
            if (_errors == null) {
                _errors = new ArrayList();
            }

            // add the error and update it
            _errors.add(ape);
            ape.errorNo = _errors.size();
        }

        /**
         * This boolean indicates if an ErrorReporter is reporting errors
         * or not.  The caller should check this before calling addError
         * because the ErrorReporter may be off for some reason.
         * @return a boolean indicating if the tag is reporting errors or not.
         */
        public boolean isReporting() {
            return true;
        }

        /**
         * Return an ArrayList of the errors
         * @return an <code>ArrayList</code> of all errors.
         */
        public ArrayList returnErrors() {
            ArrayList ret = _errors;
            _errors = null;
            return ret;
        }
    }
}
