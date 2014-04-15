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
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.core.Behavior;
import org.apache.beehive.netui.simpletags.naming.FormDataNameInterceptor;
import org.apache.beehive.netui.simpletags.naming.IndexedNameInterceptor;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @netui:tag
 */
public abstract class AbstractSimpleTag extends SimpleTagSupport
{
    private static final Logger logger = Logger.getInstance(Behavior.class);
    protected Behavior _behavior;

    /**
     * This List represents the default naming chain for handling <code>dataSource</code> attributes.  The list
     * is a read-only list which may be used by any <code>dataSource</code> implementation.
     */
    public static final List DefaultNamingChain;

    static
    {
        List l = new ArrayList(2);
        l.add(new FormDataNameInterceptor());
        l.add(new IndexedNameInterceptor());
        DefaultNamingChain = Collections.unmodifiableList(l);
    }

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
     * Report an error if the value of <code>attrValue</code> is equal to the empty string, otherwise return
     * that value.  If <code>attrValue</code> is equal to the empty string, an error is registered and
     * null is returned.
     * @param attrValue The value to be checked for the empty string
     * @param attrName  The name of the attribute
     * @return either the attrValue if it is not the empty string or null
     */
    protected final String setRequiredValueAttribute(String attrValue, String attrName)
    {
        assert(attrValue != null) : "parameter '" + attrValue + "' must not be null";
        assert(attrName != null) : "parameter '" + attrName + "' must not be null";

        if ("".equals(attrValue)) {
            String s = Bundle.getString("Tags_AttrValueRequired", new Object[]{attrName});
            _behavior.registerTagError(s, null);
            return null;
        }
        return attrValue;
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
     * Return the closest <code>ScriptReporter</code> in the parental chain.  Searching starts
     * at this node an moves upward through the parental chain.
     * @return a <code>ScriptReporter</code> or null if there is not one found.
     */
    //protected IScriptReporter getScriptReporter()
    //{
    //    IScriptReporter sr = (IScriptReporter) SimpleTagSupport.findAncestorWithClass(this, IScriptReporter.class);
    //    return sr;
    //}

    /**
     * This method will return the scriptReporter that is represented by the HTML tag.
     * @return IScriptReporter
     */
    //protected IScriptReporter getHtmlTag(ServletRequest req)
    //{
    //    Html html = (Html) req.getAttribute(Html.HTML_TAG_ID);
    //    if (html != null && html instanceof IScriptReporter)
     //       return (IScriptReporter) html;
     //   return null;
    //}

    /**
     * This method will rewrite the name (id) by passing it to the
     * URL Rewritter and getting back a value.
     * @param name the name that will be rewritten
     * @return a name that has been rewritten by the URLRewriterService.
     */
    //final protected String rewriteName(String name)
    //{
    //    PageContext pageContext = getPageContext();
    //    return URLRewriterService.getNamePrefix(pageContext.getServletContext(), pageContext.getRequest(), name) + name;
    //}

    /**
     * This method will generate a real id based upon the passed in tagId.  The generated
     * id will be constucted by searching upward for all the script containers that have a
     * scope id set.  These will form a fully qualified id.
     * @param tagId The base tagId set on a tag
     * @return an id value formed by considering all of the scope id's found in the tag hierarchy.
     */
    //final protected String getIdForTagId(String tagId)
    //{
    //    HttpServletRequest req = (HttpServletRequest) getPageContext().getRequest();
    //    ArrayList/*<String>*/ list = (ArrayList/*<String>*/)
    //            org.apache.beehive.netui.tags.RequestUtils.getOuterAttribute(req,
    //                    ScriptContainer.SCOPE_ID);
    //    if (list == null)
    //        return tagId;
    //    InternalStringBuilder sb = new InternalStringBuilder();
    //    for (int i=0;i<list.size();i++) {
    //        sb.append((String) list.get(i));
   //         sb.append('.');
   //     }
   //     sb.append(tagId);
   //     return sb.toString();
    //}
}
