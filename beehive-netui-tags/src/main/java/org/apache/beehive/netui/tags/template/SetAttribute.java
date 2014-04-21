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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.util.HashMap;

/**
 * Set an <code>Attribute</code> value defined in a template.  This tag is
 * used in content pages to set the value of attributes defined in a template.
 * The attribute value will override any default value defined on the
 * <code>Attribute</code>.

 * @jsptagref.tagdescription
 * 
 * Sets a property value in a template page.
 * 
 * <p>The &lt;netui-template:setAttribute> tag must have a parent 
 * {@link Template} tag.
 * 
 * <p>The target placeholder is defined by a {@link Attribute} tag.  For a value to be set
 * in the placeholder, the &lt;netui-template:attribute> and 
 * &lt;netui-template:setAttribute> tags must have matching <code>name</code> attributes.
 * 
 * <p>For example, a placeholder may be defined 
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
 * &lt;netui-template:setAttribute> tag.
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
 * If the &lt;netui-template:setAttribute> tag specifies no value to be set in the placeholder, then the
 * &lt;netui-template:attribute> tag's <code>defaultValue</code> will be used.
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
 * <p>Assume a &lt;netui-template:attribute&gt; tag defines a value placeholder 
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
 * @netui:tag name="setAttribute"
 *          description="Use this tag to set the value of an netui-template:attribute element in a template file."
 */
public class SetAttribute extends AbstractClassicTag
        implements TemplateConstants
{
    /**
     * The name of the attribute.
     */
    private String _name;

    /**
     * The value of the attribute.
     */
    private String _value;

    /**
     * Return the name of the tag.  This is used by error reporting
     * in the base class <code>AbstractBaseTag</code>.
     */
    public String getTagName() {
        return "SetAttribute";
    }

    /**
     * Set the <code>name</code> of the attribute.
     * @param name The name of the <code>Attribute</code> in the
     *  template for which this tags sets the value.
     *
     * @jsptagref.attributedescription
     * The name of the attribute to set.
     * 
     * @jsptagref.databindable false
     * 
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     *
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The name of the attribute to set."
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Set the value of the <code>Attribute</code>.  This attribute
     * may be assigned a read only expression.
     * @param value The value to use for the <code>Attribute</code>
     *  in the template.
     *
     * @jsptagref.attributedescription
     * Sets the value of the attribute.
     * 
     * @jsptagref.databindable Read Only
     * 
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     *
     * @netui:attribute required="true" rtexprvalue="true"
     * description="Sets the value of the attribute."
     */
    public void setValue(String value)
        throws JspException {
        _value = value;
    }

    /**
     * Tag Lifecycle method called when the tag is first seen.  This method
     * will add the <code>Attribute</code> value to a <code>HashMap</code>
     * stored in the request allowing the template to access the value.  If
     * there are errors, the error text will be placed into the attribute
     * value.  Nothing is written into the <code>ServletResponse</code>
     * @return EVAL_PAGE to continue processing the page.
     * @throws JspException on error
     */
    public int doStartTag()
            throws JspException {
        ServletRequest req = pageContext.getRequest();
        HashMap atts = (HashMap) req.getAttribute(TEMPLATE_ATTRIBUTES);
        if (atts == null) {
            atts = new HashMap();
            req.setAttribute(TEMPLATE_ATTRIBUTES,atts);
        }
        if (hasErrors()) {
            String s = getErrorsReport();
            atts.put(_name,s);
            localRelease();
            return EVAL_PAGE;
        }

        atts.put(_name,_value);
        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Reset all of the fields of the tag.
     */
    protected void localRelease() {
        super.localRelease();
        _name = null;
        _value = null;
    }
}
