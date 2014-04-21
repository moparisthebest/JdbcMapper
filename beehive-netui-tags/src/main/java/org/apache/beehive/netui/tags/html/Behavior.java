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
import org.apache.beehive.netui.tags.IBehaviorConsumer;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @jsptagref.tagdescription Add an attribute to the parent tag rendered.
 * @netui:tag name="behavior" body-content="empty" description="Add an attribute to the parent tag rendered."
 */
public class Behavior extends AbstractSimpleTag
{
    private String _name = null;
    private Object _value = null;
    private String _facet = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName() {
        return "Behavior";
    }

    /**
     * Sets the <code>name</code> behavior.
     * @param name the value of the <code>name</code> behavior.
     * @jsptagref.attributedescription The name of the behavior to add to the parent tag.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true"  rtexprvalue="true"
     * description="The name of the behavior to add to the parent tag."
     */
    public void setName(String name)
            throws JspException
    {
        _name = setRequiredValueAttribute(name, "name");
    }

    /**
     * Sets the <code>value</code> behavior.
     * @param value the value of the <code>name</code> behavior.
     * @jsptagref.attributedescription The value of the behavior to add to the parent tag.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute required="true"  rtexprvalue="true"
     * description="The value of the behavior to add to the parent tag."
     */
    public void setValue(Object value)
    {
        _value = value;
    }

    /**
     * Sets the <code>facet</code> behavior.
     * @param facet the value of the <code>facet</code> attribute.
     * @jsptagref.attributedescription The name of the facet targetted by the behavior.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute rtexprvalue="true"
     * description="The name of the facet targetted by the behavior."
     */
    public void setFacet(String facet)
            throws JspException
    {
        _facet = setRequiredValueAttribute(facet, "facet");
    }

    /**
     * Add the name/value pair to the IBehaviorConsumer parent of the tag.
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException
    {
        if (hasErrors()) {
            reportErrors();
            return;
        }

        JspTag tag = SimpleTagSupport.findAncestorWithClass(this, IBehaviorConsumer.class);
        if (tag == null) {
            String s = Bundle.getString("Tags_BehaviorInvalidParent");
            registerTagError(s, null);
            reportErrors();
            return;
        }

        IBehaviorConsumer ac = (IBehaviorConsumer) tag;
        ac.setBehavior(_name, _value, _facet);
        return;
    }
}
