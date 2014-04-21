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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.OptionTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * An option whose state is determined by its enclosing SelectOption.
 * @jsptagref.tagdescription Renders a single &lt;option> tag.
 *
 * <p>The &lt;netui:selectOption> must have a parent {@link Select} tag.
 *
 * <p>To dynamically generate a set of &lt;option> tags, point the {@link Select}
 * tag at a String[], {@link java.util.HashMap java.util.HashMap},
 * or any object that implements {@link java.util.Map java.util.Map}.
 * @example The following sample generates a set of &lt;option> tags.
 * <pre>    &lt;netui:form action="submit">
 *        &lt;netui:select dataSource="actionForm.selections" size="5">
 *            &lt;netui:selectOption value="red" />
 *            &lt;netui:selectOption value="blue" />
 *            &lt;netui:selectOption value="green" />
 *            &lt;netui:selectOption value="yellow" />
 *            &lt;netui:selectOption value="orange" />
 *        &lt;/netui:select>
 *        &lt;netui:button type="submit" value="Submit"/>
 *    &lt;/netui:form></pre>
 * @netui:tag name="selectOption" description="An option whose state is determined by its enclosing netui:selectOption."
 */
public class SelectOption extends HtmlBaseTag
{
    private OptionTag.State _state = new OptionTag.State();

    // this has it's own version of disabled in it...
    private String _text;               // The message text to be displayed to the user for this tag (if any)
    private boolean _disabled;          // Is this option disabled?
    private String _value;              // The server value for this option
    private Select.RepeatingStages _repeatingType;       // The type of the repeater.
    private boolean _hasError = false;   // Hack to avoid registering the same error in the SelectOption AND the Select.

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "SelectOption";
    }

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>value</code>
     * attribute.
     * @param name  The name of the attribute
     * @param value The value of the attribute
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        if (name != null) {
            if (name.equals(VALUE)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
        }
        super.setAttribute(name, value, facet);
    }

    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    protected AbstractHtmlState getState()
    {
        return _state;
    }

    /**
     * Gets if this option is disabled or not.
     * @return the disabled state ("true" or "false")
     */
    public boolean getDisabled()
    {
        return _disabled;
    }

    /**
     * Set if this option is disabled or not.
     * @param disabled "true" or "false"
     * @jsptagref.attributedescription Boolean value that determines whether the &lt;option> is disabled.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_disabled</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Determines whether the <option> is disabled."
     */
    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /**
     * This method will a boolean indicating if the control is disabled or not.  This will cause the
     * disable attribute to be evaluated which may result in a runtime error or a JspException.
     * @return <code>true</code> if the control is disabled.
     * @throws JspException on an exception.
     */
    protected boolean isDisabled()
            throws JspException
    {
        return _disabled;
    }

    /**
     * If the selectOption is being used inside a repeating {@link Select}, this defines the Select
     * attribute used to generate the option elements.
     * @param repeatingType Value of "option", "dataSource", "default", (for optionsDataSource, dataSource,
     * and defaultValue attributes respectively) or "null"
     * @jsptagref.attributedescription If the selectOption tag is being used inside a repeating Select, this
     * defines the Select attribute used to generate the option elements. Values are "option", "dataSource", "default",
     * (for optionsDataSource, dataSource, and defaultValue attributes respectively) and "null".
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_repeatingType</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="If the selectOption is being used inside a repeating Select, this defines the Select attribute used to generate the option elements."
     */
    public void setRepeatingType(String repeatingType)
    {
        _repeatingType = Select.RepeatingStages.parseString(repeatingType);
    }

    /**
     * Set the value of this SelectOption.
     * @param value the SelectOption value
     * @jsptagref.attributedescription A literal or a data binding expression that determines the value submitted by the
     * &lt;option> tag.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="A literal or a data binding expression that determines the value submitted by the
     * <option> tag."
     */
    public void setValue(String value)
            throws JspException
    {
        _value = value;
    }

    /**
     * Process the start of this tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {

        Tag parentTag = getParent();
        while (!(parentTag instanceof Select)) {
            parentTag = parentTag.getParent();
        }

        if (parentTag == null) {
            //throw error
            String s = Bundle.getString("Tags_SelectOptionNoSelect");
            registerTagError(s, null);
            return SKIP_BODY;
        }

        Select parentSelect = (Select) parentTag;
        boolean repeating = parentSelect.isRepeater();

        // if we find an option inside a select and it's not a repeating select report the error
        if ((parentSelect.getOptionsDataSource() != null) && !repeating) {
            String s = Bundle.getString("Tags_SelectOptionParentHasOptionsDataSource");
            _hasError = true;
            parentSelect.registerTagError(s, null);
            return SKIP_BODY;
        }

        // if we are an option inside a repeating select, we must specify the type of repeater we are.
        if (repeating && _repeatingType == null) {
            String s = Bundle.getString("Tags_SelectRepeatingOptionType");
            _hasError = true;
            parentSelect.registerTagError(s, null);
            return SKIP_BODY;
        }

        if (repeating && !isRenderable(parentSelect)) {
            return SKIP_BODY;
        }

        // Do nothing until doEndTag() is called
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Process the body text of this tag (if any).
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException
    {
        String text = bodyContent.getString();
        if (text != null) {
            bodyContent.clearBody();
            text = text.trim();
            if (text.length() > 0)
                _text = text;
        }
        return SKIP_BODY;
    }

    /**
     * Process the end of this tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        String scriptId = null;

        ServletRequest req = pageContext.getRequest();

        if ((hasErrors()) || _hasError) {
            localRelease();
            return EVAL_PAGE;
        }

        // the parent was validated in the doStartTag call
        Tag parentTag = getParent();
        while (!(parentTag instanceof Select)) {
            parentTag = parentTag.getParent();
        }

        assert (parentTag instanceof Select);
        Select parentSelect = (Select) parentTag;
        if (parentSelect.isRepeater()) {
            if (!isRenderable(parentSelect))
                return EVAL_PAGE;
        }


        // Generate an HTML <option> element
        //InternalStringBuilder results = new InternalStringBuilder(128);
        _state.value = _value;

        // we assume that tagId will over have override id if both
        // are defined.
        if (_state.id != null) {
            scriptId = renderNameAndId((HttpServletRequest) req, _state, null);
        }

        _state.disabled = _disabled;

        if (parentSelect.isMatched(_value))
            _state.selected = true;

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.OPTION_TAG, req);
        br.doStartTag(writer, _state);


        if (_text == null)
            write(parentSelect.formatText(_value));
        else {
            //@TODO: How should we report errors
            write(parentSelect.formatText(_text));
        }
        br.doEndTag(writer);

        parentSelect.addOptionToList(_value);

        if (scriptId != null)
            write(scriptId);

        // Continue evaluating this page
        localRelease();
        return EVAL_PAGE;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _state.clear();

        _text = null;
        _disabled = false;
        _value = null;
        _hasError = false;
    }

    private boolean isRenderable(Select sel)
    {
        assert(sel != null);

        if (_repeatingType == null)
            return true;

        if (sel.isNullStage())
            return _repeatingType == Select.RepeatingStages.NULL;
        if (sel.isOptionStage() && _repeatingType == Select.RepeatingStages.OPTION)
            return true;
        if (sel.isDataSourceStage() && _repeatingType == Select.RepeatingStages.DATASOURCE)
            return true;
        if (sel.isDefaultStage() && _repeatingType == Select.RepeatingStages.DEFAULT)
            return true;
        return false;
    }
}
