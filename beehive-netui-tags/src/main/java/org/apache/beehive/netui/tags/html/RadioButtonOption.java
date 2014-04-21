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

import org.apache.beehive.netui.tags.IHtmlAccessable;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * A radio button whose state is determined by its enclosing RadioButtonGroup.
 * @jsptagref.tagdescription Generates a single radiobutton option in a group of options.
 *
 * <p>The &lt;radioButtonOption> tag must have a parent {@link RadioButtonGroup} tag.
 * @example In this example, three radiobuttons are generated in the browser.
 *
 * <pre>    &lt;netui:form action="processData">
 *        &lt;netui:radioButtonGroup dataSource="actionForm.selection">
 *            &lt;netui:radioButtonOption value="value1">Display Text 1&lt;/netui:radioButtonOption>&lt;br>
 *            &lt;netui:radioButtonOption value="value2">Display Text 2&lt;/netui:radioButtonOption>&lt;br>
 *            &lt;netui:radioButtonOption value="value3">Display Text 3&lt;/netui:radioButtonOption>&lt;br>
 *        &lt;/netui:radioButtonGroup>
 *        &lt;netui:button value="Submit" />
 *    &lt;/netui:form></pre>
 * @netui:tag name="radioButtonOption" description="A radio button whose state is determined by its enclosing netui:RadioButtonGroup."
 */
public class RadioButtonOption
        extends HtmlFocusBaseTag
        implements IHtmlAccessable
{
    private InputBooleanTag.State _state = new InputBooleanTag.State();
    private SpanTag.State _spanState = new SpanTag.State();
    private String _text;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "RadioButtonOption";
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
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>type</code>
     * and <code>value</code> attribute.
     * @param name  The name of the attribute
     * @param value The value of the attribute
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
     * Set the label style for each contained RadioButtonOption.
     * The label style here will override a labelStyle at the RadioButtonGroup level.
     * @param labelStyle the label style
     * @jsptagref.attributedescription The style of the label for each contained &lt;netui:radioButtonOption> tag.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_labelStyle</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The style of the label for each contained <netui:radioButtonOption> tag."
     */
    public void setLabelStyle(String labelStyle)
    {
        _spanState.style = setNonEmptyValueAttribute(labelStyle);
    }

    /**
     * Set the label style class for each contained RadioButtonOption.
     * The label style class here will override a labelStyleClass at the RadioButtonGroup level.
     * @param labelStyleClass the label style
     * @jsptagref.attributedescription The class of the labels for each contained &lt;netui:radioButtonOption> tag.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_class</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The class of the labels for each contained <netui:radioButtonOption> tag."
     */
    public void setLabelStyleClass(String labelStyleClass)
    {
        _spanState.styleClass = setNonEmptyValueAttribute(labelStyleClass);
    }

    /**
     * Set the value of this RadioButtonOption.
     * @param value the RadioButtonOption value
     * @jsptagref.attributedescription A literal or data binding expression.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_value</i>
     * @netui:attribute required="true" rtexprvalue="true" type="java.lang.Object"
     * description="The value of the option."
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
     * Process the start of this tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {

        Tag parentTag = findAncestorWithClass(this, RadioButtonGroup.class);
        if (parentTag == null) {
            String s = Bundle.getString("Tags_RadioButtonOptionNoRadioButtonGroup");
            registerTagError(s, null);
            return SKIP_BODY;
        }

        RadioButtonGroup parent = (RadioButtonGroup) parentTag;
        if ((parent.getOptionsDataSource() != null) && !parent.isRepeater()) {
            String s = Bundle.getString("Tags_RadioOptionParentHasOptionsDataSource");
            parent.registerTagError(s, null);
            return SKIP_BODY;
        }

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
            text = text.trim();
            bodyContent.clearBody();
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

        ServletRequest req = pageContext.getRequest();
        ConstantRendering cr = TagRenderingBase.Factory.getConstantRendering(req);

        // this was verified in doBeginTag
        RadioButtonGroup parent = (RadioButtonGroup) findAncestorWithClass(this, RadioButtonGroup.class);
        assert(parent != null);
        boolean repeat = parent.isRepeater();

        // Generate an HTML <input type='radio'> element
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        if (parent.isVertical()) {
            cr.TR_TD(writer);
        }
        _state.type = INPUT_RADIO;
        _state.name = parent.getQualifiedDataSourceName();
        if (_state.id != null) {
            String tagId = _state.id;
            _state.id = getIdForTagId(_state.id);
            String script = renderDefaultNameAndId((HttpServletRequest) req, _state, tagId, _state.name);
            if (script != null) {
                write(script);
            }
        }

        // set the checked and disabled states
        _state.checked = new Boolean(parent.isMatched(_state.value, null)).booleanValue();
        _state.disabled = isDisabled();
        if (!_state.disabled)
            _state.disabled = parent.isDisabled();

        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_BOOLEAN_TAG, req);
        br.doStartTag(writer, _state);

        // if this is in a repeater, then we only output the <input tag>
        if (repeat) {
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
     * description=" The keyboard navigation key for the element.
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
     * @netui:attribute required="false" rtexprvalue="true" type="char"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }
}
