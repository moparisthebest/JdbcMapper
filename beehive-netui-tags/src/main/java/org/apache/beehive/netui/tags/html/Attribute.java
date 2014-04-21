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

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.tags.IAttributeConsumer;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @jsptagref.tagdescription <p>Adds an attribute to the parent tag rendered in the browser.</p>
 *
 * <p>The following &lt;netui:attribute> tags are rendered within the &lt;span> tag in the browser.
 *
 * <pre>    &lt;netui:span value="Some Text">
 *        &lt;netui:attribute name="a" value="aVal" />
 *        &lt;netui:attribute name="b" value="bVal" />
 *        &lt;netui:attribute name="c" value="cVal" />
 *    &lt;/netui:span></pre>
 *
 * <p>The HTML rendered in the browser appears as follows.
 *
 * <pre>    &lt;span a="aVal" b="bVal" c="cVal">Some Text&lt;/span></pre>
 *
 * Note that the <code>value</code> attribute can be dynamically determined using a databinding expression.
 *
 * <pre>    &lt;netui:attribute name="a" value="${pageFlow.aAttributeValue}" /></pre>
 * @netui:tag name="attribute" body-content="empty" description="Add an attribute to the parent tag which be rendered."
 */
public class Attribute extends AbstractSimpleTag
{
    private String _name = null;
    private String _value = null;
    private String _facet = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Attribute";
    }

    /**
     * Sets the <code>name</code> attribute.
     * @param name the name of the attribute.
     * @jsptagref.attributedescription The name of the attribute to add to the parent tag.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true"  rtexprvalue="true"
     * description="The name of the attribute to add to the parent tag."
     */
    public void setName(String name)
            throws JspException
    {
        _name = setRequiredValueAttribute(name, "name");
    }

    /**
     * Sets the <code>value</code> attribute.
     * @param value the value of the attribute.
     * @jsptagref.attributedescription The value of the attribute to add to the parent tag.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute required="true"  rtexprvalue="true"
     * description="The value of the attribute to add to the parent tag."
     */
    public void setValue(String value)
    {
        _value = setNonEmptyValueAttribute(value);
    }

    /**
     * Sets the <code>facet</code> attribute.
     * @param facet the value of the <code>facet</code> attribute.
     * @jsptagref.attributedescription The name of the facet targetted by the attribute.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute rtexprvalue="true"
     * description="The name of the facet targetted by the attribute."
     */
    public void setFacet(String facet)
            throws JspException
    {
        _facet = setRequiredValueAttribute(facet, "facet");
    }

    /**
     * Add the URL parameter to the Parameter's parent.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        if (hasErrors()) {
            reportErrors();
            return;
        }

        JspTag tag = SimpleTagSupport.findAncestorWithClass(this, IAttributeConsumer.class);
        if (!(tag instanceof IAttributeConsumer)) {
            String s = Bundle.getString("Tags_AttributeInvalidParent");
            registerTagError(s, null);
            reportErrors();
            return;
        }

        IAttributeConsumer ac = (IAttributeConsumer) tag;
        ac.setAttribute(_name, _value, _facet);
        return;
    }
}
