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

import org.apache.beehive.netui.tags.ByRef;
import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;


/**
 * Generates a hidden tag with a given value.  Hidden ignores its body content.
 * @jsptagref.tagdescription Generates an HTML hidden tag with a given value.
 *
 * <p>The <code>dataInput</code> attribute overrides the <code>dataSource</code> attribute
 * for the input of the value.
 * It allows a &lt;netui:hidden> tag to read it's value from one place (whatever is referenced by
 * the <code>dataInput</code> attribute) and submit the value to a new destination
 * (whatever is referenced by the <code>dataSource</code> attribute).
 * The <code>dataInput</code> attribute may take a String literal or a data binding expression.
 * @example In this sample, the Hidden tag is written using the value from the form bean's status property.
 * <pre>&lt;netui:hidden dataSource="actionForm.status"  /></pre>
 * @netui:tag name="hidden" description="Generates a hidden tag with a given value."
 */
public class Hidden
        extends HtmlDataSourceTag
{
    private InputHiddenTag.State _state = new InputHiddenTag.State();

    private String _value;
    private Object _dataInput;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Hidden";
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
     * or <code>value</code> attributes.
     * @param name  The name of the attribute
     * @param value The value of the attribute
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        if (name != null) {
            if (name.equals(TYPE) || name.equals(VALUE)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
        }
        super.setAttribute(name, value, facet);
    }

    /**
     * Set the data input.  This value will override the <code>
     * dataSource</code> and provide the input value on the select box.
     * @param dataInput the value of the input to the page.  This value
     *                  may contain an expression.
     * @jsptagref.attributedescription The dataInput attribute overrides the dataSource attribute for the input of the value.
     * It allows a &lt;netui:hidden> tag to read it's value from one place (whatever is referenced by
     * the <code>dataInput</code> attribute) and return the value to a new destination (whatever is
     * referenced by the <code>dataSource</code> attribute).
     * This attribute may take a String literal or a data binding expression.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_dataInput</i>
     * @netui:attribute required="false" rtexprvalue="true" type="java.lang.Object"
     * description="The dataInput attribute overrides the dataSource attribute for the input of the value. "
     */
    public void setDataInput(Object dataInput)
    {
        _dataInput = dataInput;
    }

    /**
     * Render the Hidden tag
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Save the body content of the Hidden.
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException
    {

        if (bodyContent != null) {
            bodyContent.clearBody();
        }
        return SKIP_BODY;
    }

    /**
     * Generate the hidden input tag.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        Object val = evaluateDataSource();

        ServletRequest req = pageContext.getRequest();

        if (_dataInput != null) {
            val = _dataInput.toString();
        }

        // if there were expression errors report them
        if (hasErrors())
            return reportAndExit(SKIP_BODY);

        if (val != null) {
            _value = val.toString();
        }

        // Create an appropriate "input" element based on our parameters
        ByRef ref = new ByRef();
        nameHtmlControl(_state, ref);

        if (_value != null) {
            InternalStringBuilder sb = new InternalStringBuilder(_value.length() + 16);
            StringBuilderRenderAppender sbAppend = new StringBuilderRenderAppender(sb);
            HtmlUtils.filter(_value, sbAppend);
            _state.value = sb.toString();
        }

        // correct for null text here
        if (_state.value == null)
            _state.value = "";

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase hiddenTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_HIDDEN_TAG, req);

        hiddenTag.doStartTag(writer, _state);
        hiddenTag.doEndTag(writer);

        if (!ref.isNull())
            write((String) ref.getRef());

        // Continue processing this page
        localRelease();
        return SKIP_BODY;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _state.clear();
        _value = null;
        _dataInput = null;
    }

    /* ==================================================================
     *
     * This tag's publically exposed HTML, CSS, and JavaScript attributes
     *
     * ==================================================================
     */
    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HtmlConstants.TABINDEX, Integer.toString(tabindex));
    }
}
