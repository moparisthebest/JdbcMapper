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

// java imports

import org.apache.beehive.netui.tags.IHtmlAccessable;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

// external imports

/**
 * A checkbox whose state is determined by its enclosing CheckBoxGroup.
 * @jsptagref.tagdescription Renders a single HTML checkbox within a group of checkboxes.
 *
 * The &lt;netui:checkBoxOption> tags must have a parent
 * {@link CheckBoxGroup} tag (which
 * determines the data binding for the &lt;netui:checkBoxOption> tags).
 * @example In this sample, a set of &lt;checkBoxOption> tags are submitted
 * to the <code>processData</code> action method.
 *
 * <pre>     &lt;netui:form action="processData">
 *          &lt;netui:checkBoxGroup dataSource="actionForm.data" defaultValue="${actionForm.defaultChoices}">
 *              &lt;netui:checkBoxOption value="wantSpecialCDOffers">Do you want to be notified of special CD offers?&lt;/netui:checkBoxOption>&lt;br>
 *              &lt;netui:checkBoxOption value="wantSpecialDVDOffers">Do you want to be notified of special DVD offers?&lt;/netui:checkBoxOption>&lt;br>
 *          &lt;/netui:checkBoxGroup>
 *          &lt;netui:button value="Submit" type="submit"/>
 *      &lt;/netui:form></pre>
 * @netui:tag name="checkBoxOption" description="A checkbox whose state is determined by its enclosing CheckBoxGroup."
 */
public class CheckBoxOption extends HtmlFocusBaseTag
        implements IHtmlAccessable
{
    private InputBooleanTag.State _state = new InputBooleanTag.State();
    private SpanTag.State _spanState = new SpanTag.State();
    private String _text;        // The message text to be displayed to the user for this tag (if any)
    private Boolean _defaultValue;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "CheckBoxOption";
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
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>type</code>,
     * <code>checked</code>, and <code>value</code> attributes.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        if (name != null) {
            if (name.equals(TYPE) || name.equals(VALUE) || name.equals(CHECKED)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
            else {
                if (name.equals(DISABLED)) {
                    setDisabled(Boolean.parseBoolean(value));
                    return;
                }
            }
        }
        if (facet != null && facet.equals("label")) {
            setStateAttribute(name, value, _spanState);
            return;
        }
        super.setAttribute(name, value, facet);
    }

    /**
     * Set the label style for each contained CheckBoxOption.
     * The label style here will override a labelStyle at the CheckBoxGroup level.
     * @param labelStyle the label style
     * @jsptagref.attributedescription The style of the text displayed by the rendered checkbox.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_label</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The style of the text displayed by the rendered checkbox."
     */
    public void setLabelStyle(String labelStyle)
    {
        _spanState.style = setNonEmptyValueAttribute(labelStyle);
    }

    /**
     * Set the label style class for each contained CheckBoxOption.
     * The label style class here will override a labelStyleClass at the CheckBoxGroup level.
     * @param labelStyleClass the label style
     * @jsptagref.attributedescription The class of the text displayed by the rendered checkbox.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_class</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The class of the text displayed by the rendered checkbox."
     */
    public void setLabelStyleClass(String labelStyleClass)
    {
        _spanState.styleClass = setNonEmptyValueAttribute(labelStyleClass);
    }

    /**
     * Set the value of this CheckBoxOption.
     * @param value the CheckBoxOption value
     * @jsptagref.attributedescription A String literal or a data binding expression.  The value attribute determines the value submitted
     * by the checkbox.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_literal_or_expression_value</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="A String literal or a data binding expression.  The value attribute determines the value submitted
     * by the checkbox."
     */
    public void setValue(Object value)
            throws JspException
    {
        if (value != null)
            _state.value = value.toString();
        else
            _state.value = null;
    }

    /**
     * Sets the default value (can be an expression).
     * @param defaultValue the default value
     * @jsptagref.attributedescription The boolean value or expression to be used as the default value.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_defaultValue</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="boolean"
     * description="Sets the default value."
     */
    public void setDefaultValue(boolean defaultValue)
            throws JspException
    {
        _defaultValue = Boolean.valueOf(defaultValue);
    }

    /**
     * Process the start of this tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        // verify that the parent is a CheckBoxGroup
        Tag parentTag = findAncestorWithClass(this, CheckBoxGroup.class);
        if (parentTag == null) {
            String s = Bundle.getString("Tags_CheckBoxOptionNoCheckBoxGroup");
            registerTagError(s, null);
            return SKIP_BODY;
        }

        CheckBoxGroup parent = (CheckBoxGroup) parentTag;
        if ((parent.getOptionsDataSource() != null && !parent.isRepeater())) {
            String s = Bundle.getString("Tags_CheckBoxOptionParentHasOptionsDataSource");
            parent.registerTagError(s, null);
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
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        // we verified that the parent was a CheckboxGroup in the doBeginTag()
        ServletRequest req = pageContext.getRequest();
        CheckBoxGroup parent = (CheckBoxGroup) findAncestorWithClass(this, CheckBoxGroup.class);
        ConstantRendering cr = TagRenderingBase.Factory.getConstantRendering(req);
        boolean repeat = parent.isRepeater();

        // create the writer
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        if (!repeat && parent.isVertical()) {
            cr.TR_TD(writer);
        }


        _state.type = INPUT_CHECKBOX;
        _state.name = parent.getQualifiedDataSourceName();
        if (_state.id != null) {
            String tagId = _state.id;
            _state.id = getIdForTagId(_state.id);
            /* This has been commented out because we can't set focus on checkbox options
               because they don't have a unique name.
            Form parentForm = getNearestForm();
            if (parentForm != null)
                parentForm.addTagID(tagId, _state.name);
                */
            String script = renderDefaultNameAndId((HttpServletRequest) req, _state, tagId, _state.name);
            if (script != null) {
                write(script);
            }
        }

        // Disabled on the option itself will override the parent setting this.
        // We check to see if the option actuall had disabled set on it, if not then look
        // at the parent.
        if (parent.isMatched(_state.value, _defaultValue))
            _state.checked = true;
        _state.disabled = isDisabled();
        if (!_state.disabled)
            _state.disabled = parent.isDisabled();

        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_BOOLEAN_TAG, req);
        br.doStartTag(writer, _state);
        br.doEndTag(writer);

        if (repeat && !parent.isDisabled()) {
            parent.createHiddenField(writer);
        }

        // if this is in a repeater, then we only output the <input tag>
        if (repeat) {
            //write(results.toString());
            localRelease();
            return EVAL_PAGE;
        }

        if (_spanState.style == null) {
            _spanState.style = parent.getLabelStyle();
        }
        if (_spanState.styleClass == null) {
            _spanState.styleClass = parent.getLabelStyleClass();
        }

        TagRenderingBase spanTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.SPAN_TAG, req);
        spanTag.doStartTag(writer, _spanState);
        if (_text == null)
            write(_state.value);
        else
            write(_text);
        spanTag.doEndTag(writer);

        if (parent.isVertical()) {
            cr.end_TD_TR(writer);
        }

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
        _spanState.clear();
        _text = null;
    }

    /* ==================================================================
     *
     * This tag's publically exposed HTML, CSS, and JavaScript attributes
     *
     * ==================================================================
     */

    /**
     * Sets the accessKey attribute value.  This should key value of the
     * keyboard navigation key.  It is recommended not to use the following
     * values because there are often used by browsers <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>.
     * @param accessKey the accessKey value.
     * @jsptagref.attributedescription The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_accessKey</i>
     * @netui:attribute required="false" rtexprvalue="true" type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCESSKEY, Character.toString(accessKey));
    }

    /**
     * Sets the alt attribute value.
     * @param alt the alt value.
     * @jsptagref.attributedescription The alt attribute of the element.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The alt attribute of the element."
     */
    public void setAlt(String alt)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt);
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }
}
