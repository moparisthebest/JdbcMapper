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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Used within a template JSP page to define a placeholder for section content.
 * Within a template, one or more sections are defined within the overall
 * structure of the page.  Each section has a unique name identifying it.
 * The content page, through the <code>Section</code> tag, provides content
 * that is included into the defined sections.
 * <p>
 * All content found within the body of the <code>IncludeSection</code>
 * is ignored.

 * @jsptagref.tagdescription
 * Defines a content placeholder within a template.
 * Each placeholder must have a unique name identifying it.
 * Different content pages adopt the template page, set properties 
 * on its placeholders (using the {@link Section} tag), and render the 
 * completed HTML in the browser.
 * 
 * <p>For example, a template page can use the &lt;netui-template:includeSection> tag to 
 * define a content placeholder.
 * 
 * <p><b>In the template JSP page...</b>
 * 
 * <pre>      &lt;table>
 *          &lt;tr>
 *              &lt;td colspan="3">
 *                  &lt;netui-template:includeSection name="tableHeader"/>
 *              &lt;/td>
 *          &lt;/tr></pre>
 * 
 * <p>Then a content page can set HTML content in the placeholder using the {@link Section} tag.
 * 
 * <p><b>In a content JSP page...</b>
 * 
 * <pre>    &lt;netui-template:section name="tableHeader">
 *        &lt;h1>HEADER TEXT&lt;/h1>
 *    &lt;/netui-template:section></pre>
 * 
 * <p>The HTML rendered in the browser will appear as follows.
 * 
 * <pre>      &lt;table>
 *          &lt;tr>
 *              &lt;td colspan="3">
 *                  &lt;h1>HEADER TEXT&lt;/h1>
 *              &lt;/td>
 *          &lt;/tr></pre>
 * 
 * <p>If the content page does not define content to be placed in the placeholder, then 
 * the <code>defaultPage</code> attribute will be used.  The 
 * <code>defaultPage</code> attribute points at a stand-alone JSP page.  The entire contents of the page
 * will be placed in the placeholder, after any Java elements, such as scriptlets have been resolved.
 * 
 * @example 
 * In this sample a &lt;netui-template:includeSection> tag defines a place holder for a 
 * table row 
 *
 * <pre>    &lt;tr>
 *        &lt;netui-template:includeSection name="rowPlaceholder" defaultPage="defaultPage.jsp"/>
 *    &lt;/tr></pre>
 *    
 * <p>If there is no content page that sets content into this placeholder using a &lt;netui-template:section> 
 * tag, then the entire contents of the defaultPage.jsp will be used.
 * Assume that the defaultPage.jsp appears as follows.
 * 
 * <pre>    &lt;p>&lt;%= 1 + 1 %>&lt;/p></pre>
 * 
 * Then the HTML rendered in the browser will appear as follows. Note that the Java scriptlet 
 * <code>&lt;%= 1 + 1 %></code> has been resolved to the value <code>2</code>.
 * 
 * <pre>    &lt;tr>
 *        &lt;p>2&lt;/p>
 *    &lt;/tr></pre>
 *    
 * @netui:tag name="includeSection"
 *          description="Include this tag in a template file to mark out content that will be used in another JSP page."
 */
public class IncludeSection extends AbstractClassicTag
        implements TemplateConstants
{
    private static final Logger logger = Logger.getInstance(IncludeSection.class);

    /**
     * The name of the section.
     */
    private String _name;

    /**
     * The name of a JSP that will act as a default page
     */
    private String _default;

    /**
     * Returns the name of the Tag.  This is used to
     * identify the type of tag reporting errors.
     */
    public String getTagName() {
        return "IncludeSection";
    }

    /**
     * Sets the name of the section.  This name must be unique within
     * the template page.
     * @param name The name of the defined section within the template.
     *	This name must be unique within the template.
     *
     * @jsptagref.attributedescription
     * The name of the section.  This name must be unique within the template page.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     *
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The name of the section.  This name must be unique within the template page."
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Sets a default JSP page to provide content for the section if
     * the content page does not define the content.
     * @param defaultPage a URL identifying a JSP or HTML page
     *	providing default content to the defined section.
     *
     * @jsptagref.attributedescription
     * A default JSP page to provide content for the placeholder if
     * the content page fails to define the content.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_defaultPage</i>
     *
     * @netui:attribute required="false" rtexprvalue="true"
     * description="A default JSP page to provide content for the placeholder if
     * the content page fails to define the content."
     */
    public void setDefaultPage(String defaultPage) {
        _default = defaultPage;
    }

    /**
     * Renders the content of the section into the template.  Errors
     * are reported inline within the template in development
     * mode.  If no sections are defined an error is reported.  If
     * a section is not defined and no default URL is provided an
     * error is reported.
     * @return SKIP_BODY to skip any content found in the tag.
     * @throws JspException on Errors.
     */
    public int doStartTag()
            throws JspException {
        ServletRequest req = pageContext.getRequest();
        Template.TemplateContext tc = (Template.TemplateContext)
                req.getAttribute(TEMPLATE_SECTIONS);
        if (tc == null) {

            String s = Bundle.getString("Tags_TemplateContextMissing");
            // report the error.  If this returns a value then we throw an
            // exception
            logger.warn(stripBold(s));
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_BODY;
        }
        if (tc.secs == null) {
            if (_default != null) {
                return callDefault(req);
            }
            String s = Bundle.getString("Tags_TemplateSectionMissing",
                    new Object[]{_name});
            // report the error.  If this returns a value then we throw an
            // exception
            logger.warn(stripBold(s));
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_BODY;
        }

        String val = (String) tc.secs.get(_name);
        if (val == null) {

            if (_default == null) {
                String s = Bundle.getString("Tags_TemplateSectionMissing",
                        new Object[]{_name});
                logger.warn(stripBold(s));

                // report the error.  If this returns a value then we throw an
                // exception
                registerTagError(s,null);
                reportErrors();
                localRelease();
                return SKIP_BODY;
            }
            return callDefault(req);

        }

        try {
            Writer out = pageContext.getOut();
            out.write(val);
        }
        catch (IOException e) {
            String reason = Bundle.getString("TempExcp_WritingContent");
            String s = Bundle.getString("TempExcp_Except",
                    new Object[]{"IOException",
                                 reason});
            logger.error(s);

            JspException jspException = new JspException(s, e);
            // todo: future cleanup
            // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
            // this will cause an IllegalStateException on the following call.
            if (jspException.getCause() == null) {
                jspException.initCause(e);
            }
            throw jspException;
        }
        // continue to evalue the page...(This should be a template)
        localRelease();
        return SKIP_BODY;
    }

    private int callDefault(ServletRequest req)
            throws JspException {
        if (!defaultExists()) {
            String s = Bundle.getString("TempExcp_MissingDefaultPage",
                    new Object[]{_default});
            logger.error(s);
            registerTagError(s,null);
            reportErrors();
            localRelease();
            return SKIP_BODY;
        }

        try {
            HttpServletResponse resp = (HttpServletResponse)
                    pageContext.getResponse();

            RequestDispatcher rd = req.getRequestDispatcher(_default);
            // This is now causing an IOException in Weblogic.  This code was added because Tomcat didn't work
            // without it.  I'm going to catch and ignore the IOException because this really should affect
            // things.
            try {
                JspWriter out = pageContext.getOut();
                out.flush();
            }
            catch (IOException ignore) {}
            rd.include(req,resp);
            localRelease();
            return SKIP_BODY;
        }
        catch (IOException e) {
            String s = Bundle.getString("TempExcp_ExceptIncludeDefault",
                    new Object[]{"IOException",
                                 _default});
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
        catch (ServletException se) {
            String s = Bundle.getString("TempExcp_ExceptIncludeDefault",
                    new Object[]{"ServletException",
                                 _default});
            logger.error(s,se);
            JspException jspException = new JspException(s, se);
            // todo: future cleanup
            // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
            // this will cause an IllegalStateException on the following call.
            if (jspException.getCause() == null) {
                jspException.initCause(se);
            }
            throw jspException;
        }
    }

    private boolean defaultExists()
        throws JspException
    {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        String realURI = Template.getRealURI(req,_default);
        try {
            URL uri = pageContext.getServletContext().getResource(realURI);
            return (uri != null);
        }
        catch (MalformedURLException e) {
           String s = Bundle.getString("TempExcp_ExceptIncludeDefault",
                    new Object[]{"MalformedURLException",
                                 _default});
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

    /**
     * Resets all of the fields of the tag.
     */
    protected void localRelease() {
        super.localRelease();
        _name = null;
        _default = null;
    }

    /**
     * This will strip any html out of a warning
     */
    static String stripBold(String in) {
        String boldStart = "<b>";
        String boldEnd = "</b>";
        int pos = in.indexOf(boldStart);
        if (pos == -1)
            return in;
        InternalStringBuilder sb = new InternalStringBuilder(in.substring(0,pos));
        int fill = pos+boldStart.length();
        pos = in.indexOf(boldEnd,fill);
        if (pos == -1)
            return in;
        sb.append(in.substring(fill,pos));
        pos += boldEnd.length();
        sb.append(in.substring(pos));
        return sb.toString();
    }
}
