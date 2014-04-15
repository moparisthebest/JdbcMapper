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

import org.apache.beehive.netui.tags.AbstractPageError;
import org.apache.beehive.netui.tags.ErrorHandling;
import org.apache.beehive.netui.tags.IErrorReporter;
import org.apache.beehive.netui.tags.IDocumentTypeProducer;
import org.apache.beehive.netui.tags.javascript.ScriptContainer;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.HtmlTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.struts.Globals;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Generates the html element and performs error handling within its body.
 * @jsptagref.tagdescription <p>
 * Renders an &lt;html> tag.
 * </p>
 * @example In this sample, the &lt;netui:html> tag uses the default locale and the direction of the HTML is
 * left-to-right (LTR).
 * <pre>&lt;netui:html dir="LTR" useLocale="true"  /></pre>
 *
 * @netui:tag name="html" description="Generates the html element and performs error handling within its body."
 */
public class Html extends ScriptContainer
        implements IErrorReporter, IDocumentTypeProducer
{
    /**
     * The HTML tag is registered into the request with this name.  This allows tags reporting
     * errors to find the top level <code>ErrorReporter</code>.
     */
    public static final String HTML_TAG_ID = "netui:html";

    /**
     * This is an override of the Document type set in the request
     */
    public static final String DOC_TYPE_OVERRIDE = "netui:doctype";

    /**
     * Constant representing the document type html 4.01
     */
    public static final String HTML_401 = "html4-loose";

    /**
     * Constant representing the document type html 4.01
     */
    public static final String HTML_401_QUIRKS = "html4-loose-quirks";

    /**
     * Constant representing the document type XHTML 1.0 Transitional.
     */
    public static final String XHTML_10 = "xhtml1-transitional";

    private HtmlTag.State _state = new HtmlTag.State();
    private TagRenderingBase _br;
    private WriteRenderAppender _writer;

    private boolean _useLocale = false;     // include xml:lang=defaultLocale.getLanguage()
    private boolean _scopeEnded = false;
    private ArrayList _errors;              // errors
    private IErrorReporter _containerErrors; //Check to see if there is a parent error reporter

    private String _docType;                // The document type

    private int _rendering = TagRenderingBase.UNKNOWN_RENDERING;

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "Html";
    }

    /**
     * This method will return the TagRenderBase enum value for the document type.  The default
     * value is HTML 4.01.
     * @return int
     */
    public int getTargetDocumentType()
    {
        if (_rendering != TagRenderingBase.UNKNOWN_RENDERING)
            return _rendering;

        if (_docType != null) {
            if (_docType.equals(HTML_401))
                _rendering = TagRenderingBase.HTML_RENDERING;
            else if (_docType.equals(HTML_401_QUIRKS))
                _rendering = TagRenderingBase.HTML_RENDERING_QUIRKS;
            else if (_docType.equals(XHTML_10))
                _rendering = TagRenderingBase.XHTML_RENDERING;
            else
                _rendering = TagRenderingBase.getDefaultDocType();
        }
        else {
            _rendering = TagRenderingBase.getDefaultDocType();
        }
        return _rendering;
    }

    /////////////////////////// Attributes ////////////////////////////

    /**
     * Sets the dir value of the html.
     * @param dir the direction of text, "LTR" or "RTL"
     * @jsptagref.attributedescription Specifies the direction of text. (<code>LTR | RTL</code>)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the dir value (ltr or rtl) of the html."
     */
    public void setDir(String dir)
    {
        _state.dir = dir;
    }

    /**
     * Gets whether the default locale's language should be used.
     * @return true or false
     */
    public boolean isUseLocale()
    {
        return _useLocale;
    }

    /**
     * Sets whether the default locale's language should be used.
     * @param locale true or false
     * @jsptagref.attributedescription Sets whether the default locale's language should be used.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_locale</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Sets whether the default locale's language should be used."
     */
    public void setUseLocale(boolean locale)
    {
        _useLocale = locale;
    }

    /**
     * Set the document type (html4-loose or xhtml1-transitional) of the document.
     * @jsptagref.attributedescription Set the document type (html4-loose or xhtml1-transitional) of the document.
     * The default is html4-loose-quirks.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_doctype</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Set the document type (html4-loose or xhtml1-transitional) of the document."
     */
    public void setDocumentType(String docType)
    {
        _docType = docType;
    }


    public int doStartTag() throws JspException
    {
        // check to see if there is a scope id
        ServletRequest req = pageContext.getRequest();
        _containerErrors = (org.apache.beehive.netui.tags.IErrorReporter) req.getAttribute(CONTAINER_ERRORS);

        //Make this tag available to child tags
        req.setAttribute(HTML_TAG_ID, this);

        // set the local
        Locale currentLocale = currentLocale();
        if (currentLocale == null) {
            currentLocale = Locale.getDefault();
        }

        // write out the html...
        _state.lang = currentLocale.getLanguage();
        String idScope = getRealIdScope();
        pushIdScope();
        if (idScope != null) {
            _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, "netui:idScope", idScope);
        }

        _writer = new WriteRenderAppender(pageContext);
        _br = TagRenderingBase.Factory.getRendering(TagRenderingBase.HTML_TAG, req);
        _br.doStartTag(_writer, _state);
        _writer.append("\n");

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Write out the body content and report any errors that occured.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        popIdScope();
        
        // if there are errors then we should output the error table
        ErrorHandling.reportCollectedErrors(pageContext, this);

        // The body tag may cause the scope Div to be closed, if not we must do it now
        //if (getIdScope() != null && !_scopeEnded) {
        //    _writer.append("</div>");
        //}

        // the script can be written out by another tag,  typically this would be the <body> tag.
        writeScript(_writer);

        // close the html tag
        if (!_scopeEnded)
            _writer.append("\n");
        _br.doEndTag(_writer);
        localRelease();
        return EVAL_PAGE;
    }

    /**
     * This will close the HTML div associated with the idScope.  This may be
     * called by the Body tag so the div ends before the body ends.
     * @param _writer a writer to write the close tag into
     */
    public void endScope(WriteRenderAppender _writer)
    {
        // write the close tag and mark the fact that it is now closed.
        //_writer.append("</div>\n");
        _scopeEnded = true;
    }

    /**
     * Add an error to the errors being reported by this tag.
     * @param ape The AbstractPageError to add
     */
    public void addError(AbstractPageError ape)
    {
        assert (ape != null);

        // if this is not the error reporter, then find an error reporter above this one.
        if (_containerErrors == null && _errors == null) {
            Tag par = getParent();
            while (par != null) {
                if (par instanceof org.apache.beehive.netui.tags.IErrorReporter) {
                    _containerErrors = (org.apache.beehive.netui.tags.IErrorReporter) par;
                    break;
                }
                par = par.getParent();
            }
        }

        // if there is an error reporter, add the error and return
        if (_containerErrors != null) {
            _containerErrors.addError(ape);
            return;
        }

        // This is the error reporter.
        if (_errors == null) {
            _errors = new ArrayList();
        }

        // add the error and update it
        _errors.add(ape);
        ape.errorNo = _errors.size();
    }

    /**
     * Return an ArrayList of the errors
     * @return an <code>ArrayList</code> of all errors.
     */
    public ArrayList returnErrors()
    {
        if (_containerErrors != null)
            return _containerErrors.returnErrors();
        ArrayList e = _errors;
        _errors = null;
        return e;
    }

    /**
     * This boolean indicates if an ErrorReporter is reporting errors
     * or not.  The caller should check this before calling addError
     * because the ErrorReporter may be off for some reason.
     * @return a boolean indicating if the tag is reporting errors or not.
     */
    public boolean isReporting()
    {
        return true;
    }


    /**
     * Return the current Locale for this request, creating a new one if
     * necessary.  If there is no current Locale, and locale support is not
     * requested, return <code>null</code>.
     */
    protected Locale currentLocale()
    {
        ServletRequest req = pageContext.getRequest();

        // Create a new session if necessary
        HttpSession session = pageContext.getSession();
        if ((_useLocale) && (session == null))
            session = ((HttpServletRequest) req).getSession();
        if (session == null)
            return null;

        // Return any currently set Locale in our session
        Locale current = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (current != null)
            return current;

        // Configure a new current Locale, if requested
        if (!_useLocale)
            return (null);
        current = req.getLocale();
        if (current != null)
            session.setAttribute(Globals.LOCALE_KEY, current);
        return current;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _state.clear();
        _br = null;
        _writer = null;

        _useLocale = false;
        _scopeEnded = false;
        _errors = null;
        _containerErrors = null;
        pageContext.getRequest().removeAttribute(HTML_TAG_ID);
    }
}
