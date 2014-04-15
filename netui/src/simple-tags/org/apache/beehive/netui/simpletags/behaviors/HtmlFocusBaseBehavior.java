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
package org.apache.beehive.netui.simpletags.behaviors;

import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;

abstract public class HtmlFocusBaseBehavior extends HtmlBaseBehavior
{
    private boolean _disabled;              // Is the html control disabled?

    /**
     * Set the disable state either with the literal "true" or "false"
     * or with an expression.
     * @param disabled true or false or an expression
     * @jsptagref.attributedescription Set the disable state either with the literal "true"
     * or "false" or with an expression.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_disabled</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Set the disable state either with the literal "true" or "false"
     * or with an expression."
     */
    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /**
     * This method will a boolean indicating if the control is disabled or not.  This will cause the
     * disable attribute to be evaluated which may result in a runtime error or a JspException.
     * @return <code>true</code> if the control is disabled.
     */
    protected final boolean isDisabled()
    {
        return _disabled;
    }

    /**
     * Sets the onBlur javascript event.
     * @param onblur the onBlur event.
     * @jsptagref.attributedescription The onBlur JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onBlur</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the onBlur javascript event."
     */
    public void setOnBlur(String onblur)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONBLUR, onblur);
    }

    /**
     * Sets the onFocus javascript event.
     * @param onfocus the onFocus event.
     * @jsptagref.attributedescription The onFocus JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onFocus</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the onFocus javascript event."
     */
    public void setOnFocus(String onfocus)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONFOCUS, onfocus);
    }

    /**
     * Sets the onChange javascript event.
     * @param onchange the onChange event.
     * @jsptagref.attributedescription The onChange JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onChange</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the onChange javascript event."
     */
    public void setOnChange(String onchange)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCHANGE, onchange);
    }

    /**
     * Sets the onSelect javascript event.
     * @param onselect the onSelect event.
     * @jsptagref.attributedescription The onSelect JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onSelect</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the onSelect javascript event."
     */
    public void setOnSelect(String onselect)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONSELECT, onselect);
    }
}