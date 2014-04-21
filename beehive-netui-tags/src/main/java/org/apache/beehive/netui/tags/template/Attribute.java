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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * The Attribute tag defines an attribute within a template that may be set
 * from a content page. For example, the page's title may be defined as an
 * attribute in the template and then provided by each content page using the
 * template.  The attribute has a name and default value.  If the content
 * page specifies a value for the attribute it will be used, otherwise
 * the default value is used.

 * @jsptagref.tagdescription
 * 
 * Defines a property placeholder within a template. The
 * value of these placeholders may be set
 * from a content page. 
 * 
 * <p>For example, a title placeholder may be defined 
 * in the template.
 * 
 * <p><b>In the template JSP page...</b>
 * 
 * <pre>    &lt;head>
 *        &lt;title>
 *            &lt;netui-template:attribute name="title"/>
 *        &lt;/title>
 *    &lt;/head></pre> 
 * 
 * <p>Then content pages may set the value of this placeholder using the 
 * {@link SetAttribute} tag.
 * 
 * <p><b>In a content JSP page...</b>
 * 
 * <pre>    &lt;netui-template:setAttribute name="title" value="myContentPage1.jsp"/></pre>
 * 
 * <p>The HTML rendered in the browser appears as follows.
 * 
 * <p><b>Rendered HTML in the browser...</b>
 * 
 * <pre>    &lt;head>
 *        &lt;title>
 *            myContentPage1.jsp
 *        &lt;/title>
 *    &lt;/head></pre>
 * 
 * If the &lt;netui-template:setAttribute> tag specifies no value to be set in the 
 * placeholder, then the
 * {@link Attribute} tag's <code>defaultValue</code> will be used.
 * 
 * <pre>    &lt;netui-template:attribute name="title" <b>defaultValue="My Page"</b>/></pre>
 * 
 * The &lt;netui-template:attribute> tag may also be used to define placeholders within 
 * JSP and HTML tags.
 * 
 * <p><b>In the template JSP page...</b>
 * 
 * <pre>    &lt;td colspan="3" bgcolor="<b>&lt;netui-template:attribute name="headerColor" defaultValue="#ffffff"/></b>"></pre>
 * 
 * @example 
 * <p>In this sample, a &lt;netui-template:attribute&gt; tag defines a value placeholder 
 * within a &lt;td> tag</p>
 * 
 * <pre>    &lt;td colspan="3" bgcolor="<b>&lt;netui-template:attribute name="headerColor" defaultValue="#ffffff"/></b>"></pre>
 * 
 * <p>Now a content JSP page can control the background color of the &lt;td>.
 * 
 * <pre>    &lt;netui-template:setAttribute name="headerColor" value="lightgreen"/></pre>
 * 
 * The HTML rendered in the browser will appear as follows.
 * 
 * <pre>    &lt;td colspan="3" bgcolor="lightgreen"></pre>
 *    
 * @netui:tag name="attribute" description="Place this tag in a template file, and then set its value with the netui-template:setAttribute tag."
 */
public class
        Attribute extends TagSupport
        implements TemplateConstants
{
    /**
     * The name of the attribute.
     */
    private String _name;

    /**
     * Default value
     */
    private String _defaultValue;

    /**
     * Sets the <code>name</code> for the <code>Attribute</code>.  An
     * attribute may be used more than once in a template page.
     * @param name The name of the attribute.  The name does
     *	not need to be unique because it may be used more than once
     *  on the page.
     *
     * @jsptagref.attributedescription
     * The <code>name</code> for the &lt;netui-template:attribute> placeholder.  The <code>name</code>
     * may be used more than once in a template page.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     *
     * @netui:attribute required="true"  rtexprvalue="true"
     * description="The name for the &lt;netui-template:attribute> placeholder.  The name
     * may be used more than once in a template page."
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Sets the <code>defaultValue</code> for the <code>Attribute</code>.
     * If the content page does not define a value for this attribute
     * through the <code>SetAttribute</code> tag, then the
     * <code>defaultValue</code> will be used.
     * If neither a value nor <code>defaultValue</code> is set, then the
     * empty String "" will be output.
     * @param defaultValue The value to set the defaultValue property.
     *
     * @jsptagref.attributedescription
     * The default value for &lt;netui-template:attribute> placeholder.
     * If a content page does not define a value for the placeholder
     * through its &lt;netui-template:setAttribute> tag, then the
     * <code>defaultValue</code> will be used.
     * If neither a value nor <code>defaultValue</code> is set, then the
     * empty String "" will be output.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_defaultValue</i>
     *
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The default value for <netui-template:attribute> placeholder."
     */
    public void setDefaultValue(String defaultValue) {
        _defaultValue = defaultValue;
    }

    /**
     * Renders the content of the attribute.
     * @return EVAL_PAGE to continue evaluation of the page.
     * @throws JspException If there is any failure in the tag.
     */
    public int doStartTag()
            throws JspException {
        ServletRequest req = pageContext.getRequest();
        HashMap atts = (HashMap) req.getAttribute(TEMPLATE_ATTRIBUTES);
        try {
            if (atts != null) {
                String val = (String) atts.get(_name);
                if (val != null) {
                    Writer out = pageContext.getOut();
                    out.write(val);
                }
                else {
                    Writer out = pageContext.getOut();
                    if (_defaultValue != null)
                        out.write(_defaultValue);
                    else
                        out.write("");
                }
            }
            else {
                Writer out = pageContext.getOut();
                if (_defaultValue != null)
                    out.write(_defaultValue);
                else
                    out.write("");
            }
        }
        catch (IOException e) {
            localRelease();
            JspException jspException = new JspException("Caught IO Exception:" + e.getMessage(),e);
            // todo: future cleanup
            // The 2.5 Servlet api will set the initCause in the Throwable superclass during construction,
            // this will cause an IllegalStateException on the following call.
            if (jspException.getCause() == null) {
                jspException.initCause(e);
            }
            throw jspException;
        }
        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Resets all of the fields of the tag.
     */
    // this is the root because this tag doesn't extend AbstractBaseTag
    protected void localRelease() {
        _name = null;
        _defaultValue = null;
    }
}
