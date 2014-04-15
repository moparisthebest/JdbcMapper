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

// java imports

// internal imports

import org.apache.beehive.netui.simpletags.core.IUrlParams;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.BehaviorStack;
import org.apache.beehive.netui.simpletags.util.ContextUtils;

/**
 * Writes a URL parameter to a URL on its parent tag.  The parent tag must implement IUrlParams.
 * @jsptagref.tagdescription Writes a name/value pair to the URL or the parent tag.
 *
 * You can dynamically determine the value of the &lt;netui:parameter> through
 * the <code>value</code> attribute.
 * @example In this sample, the hyperlink is amended with the parameter <code>q=Socrates</code>
 *
 * <pre>      &lt;netui:anchor href="http://www.google.com/search">
 *          Search Google with the query "Socrates"
 *          &lt;netui:parameter name="q" value="Socrates" />
 *      &lt;/netui:anchor></pre>
 *
 * The URL produced appears below:
 *
 * <pre>      http://www.google.com/search?q=Socrates</pre>
 * @netui:tag name="parameter" body-content="scriptless" description="Writes a URL parameter to a URL on its parent tag."
 */
public class Parameter extends AbstractSimpleTag
{
    private String _name = null;
    private Object _value = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Parameter";
    }

    /**
     * Sets the name of the URL parameter.
     * @param name the parameter name.
     * @jsptagref.attributedescription The name of the parameter.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The name of the parameter."
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets the value of the URL parameter.  This can be an expression.
     * @param value the parameter value.
     * @jsptagref.attributedescription The value of the parameter.  May be a literal or a data binding expression.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute required="true" rtexprvalue="true" type="java.lang.Object"
     * description="The value of the parameter."
     */
    public void setValue(Object value)
    {
        _value = value;
    }

    /**
     * Add the URL parameter to the Parameter's parent.
     */
    public void doTag()
    {
        TagContext tagCtxt = ContextUtils.getTagContext();
        BehaviorStack behaviors = tagCtxt.getBehaviorStack();
        IUrlParams urlParams = (IUrlParams) behaviors.findAncestorWithClass(null, IUrlParams.class);
        // @todo: how do we report an error here if the urlParams is null?
        if (urlParams != null) {
            urlParams.addParameter(_name, _value, null);
        }
    }
}
