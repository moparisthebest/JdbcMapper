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

import org.apache.beehive.netui.pageflow.ProcessPopulate;
import org.apache.beehive.netui.pageflow.RequestParameterHandler;
import org.apache.beehive.netui.tags.ByRef;
import org.apache.beehive.netui.tags.IHtmlAccessable;
import org.apache.beehive.netui.tags.naming.FormDataNameInterceptor;
import org.apache.beehive.netui.tags.naming.IndexedNameInterceptor;
import org.apache.beehive.netui.tags.naming.PrefixNameInterceptor;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates a checkbox which binds to a form bean property or databound expression.
 * CheckBox should be used on its own and not within a CheckBoxGroup.  CheckBox ignores its
 * body content.
 *
 * CheckBoxes can bind to boolean, Boolean, and Strings.
 * @jsptagref.tagdescription <p>Generates a single HTML checkbox.  The &lt;netui:checkBox> tag should be used on its own, not within a
 * {@link CheckBoxGroup}.
 *
 * <p>The &lt;netui:checkBox> tag can be data bound to a boolean or Boolean type.  For instance,
 * the following &lt;netui:checkBox> tag...
 *
 * <pre>    &lt;netui:checkBox dataSource="actionForm.checkBoxValue"/></pre>
 *
 * ...must be bound to a boolean or Boolean field in the Form Bean...
 *
 * <pre>    public static class ProcessDataForm extends FormData
 *    {
 *        private boolean checkBoxValue;
 *
 *        public void setCheckBoxValue(boolean checkBoxValue)
 *        {
 *            this.checkBoxValue = checkBoxValue;
 *        }
 *
 *        public boolean isCheckBoxValue()
 *        {
 *            return this.checkBoxValue;
 *        }
 *    }</pre>
 * @example In this sample, the &lt;netui:checkBox reads it initial value from the
 * Form Bean field <code>wantSpecialOffers</code>.  Upon submission, the user specified value is
 * loaded into the same Form Bean field.  The data is submitted to the
 * action method <code>processData</code>.
 * <pre>    &lt;netui:form action="processData">
 *        Do you want to be notified of special offers?
 *        &lt;netui:checkBox dataSource="actionForm.wantsSpecialOffers"/>&lt;br>
 *        &lt;netui:button value="Submit" type="submit"/>
 *    &lt;/netui:form></pre>
 * @netui:tag name="checkBox" description="Generates a checkbox that binds to a form bean property or databound expression."
 */
public class CheckBox
        extends HtmlDefaultableDataSourceTag
        implements IHtmlAccessable
{
    private static final Logger logger = Logger.getInstance(CheckBox.class);

    private InputBooleanTag.State _state = new InputBooleanTag.State();
    private InputHiddenTag.State _hiddenState = new InputHiddenTag.State();

    private static final String CHECKBOX_KEY = "checkbox_key";
    private static final String OLDVALUE_SUFFIX = "OldValue";

    private static final List _internalNamingChain;

    static
    {
        List l = new ArrayList(3);
        l.add(new FormDataNameInterceptor());
        l.add(new IndexedNameInterceptor());
        l.add(new PrefixNameInterceptor(CHECKBOX_KEY));
        _internalNamingChain = Collections.unmodifiableList(l);
    }

    static
    {
        org.apache.beehive.netui.pageflow.ProcessPopulate.registerPrefixHandler(CHECKBOX_KEY, new CheckBoxPrefixHandler());
    }

    /**
     * The handler for naming and indexing the CheckBox.
     */
    public static class CheckBoxPrefixHandler
            implements RequestParameterHandler
    {
        /**
         * Determines the current state of the CheckBox (true or false) based on the Request.
         */
        public void process(HttpServletRequest request, String key, String expr,
                            ProcessPopulate.ExpressionUpdateNode node)
        {
            String returnVal = null;
            if (!key.endsWith(OLDVALUE_SUFFIX)) {
                //This checkbox is true and should stay that way
                returnVal = "true";
            }
            else {
                //Check the request to see if checkBox also exists
                String newKey = key.substring(0, key.indexOf(OLDVALUE_SUFFIX));
                String checkBox = request.getParameter(newKey);
                if (checkBox != null) {
                    returnVal = "true";
                }
                else {
                    returnVal = "false";
                }
            }

            if (node.expression.endsWith(OLDVALUE_SUFFIX)) {
                node.expression = node.expression.substring(0, node.expression.indexOf(OLDVALUE_SUFFIX));

            }

            node.values = new String[]{returnVal};

            if (logger.isDebugEnabled()) {
                logger.debug("*********************************************\n" +
                        "process with key \"" + key + "\" and expression \"" + node.expression + "\"" + "and result: " + returnVal + "\n" +
                        "*********************************************\n");
            }
        }
    }

    public CheckBox()
    {
        super();
    }

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "CheckBox";
    }

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>type</code>,
     * <code>checked</code> and <code>value</code> attributes.
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
     * Return an <code>ArrayList</code> which represents a chain of <code>INameInterceptor</code>
     * objects.  This method by default returns <code>null</code> and should be overridden
     * by objects that support naming.
     * @return an <code>ArrayList</code> that will contain <code>INameInterceptor</code> objects.
     */
    protected List getNamingChain()
    {
        return _internalNamingChain;
    }

    /**
     * @return A boolean value.
     */
    private boolean evaluateDefaultValue()
    {
        if (_defaultValue instanceof String)
            return Boolean.valueOf((String) _defaultValue).booleanValue();
        if (_defaultValue instanceof Boolean)
            return ((Boolean) _defaultValue).booleanValue();
        return false;
    }

    /**
     * Render the checkbox.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        // evaluate the body so that we can set the attributes
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Save the body content of the checkbox.
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException
    {
        return SKIP_BODY;
    }

    /**
     * Render the checkbox.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        Tag parent = getParent();
        if (parent instanceof CheckBoxGroup)
            registerTagError(Bundle.getString("Tags_CheckBoxGroupChildError"), null);

        Object val = evaluateDataSource();
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        ByRef ref = new ByRef();
        nameHtmlControl(_state, ref);

        String hiddenParamName = _state.name + OLDVALUE_SUFFIX;
        ServletRequest req = pageContext.getRequest();

        if (val instanceof String) {
            if (val != null && Boolean.valueOf(val.toString()).booleanValue())
                _state.checked = true;
            else
                _state.checked = false;
        }
        else if (val instanceof Boolean) {
            _state.checked = ((Boolean) val).booleanValue();
        }
        else {
            String oldCheckBoxValue = req.getParameter(hiddenParamName);
            if (oldCheckBoxValue != null) {
                _state.checked = new Boolean(oldCheckBoxValue).booleanValue();
            }
            else {
                _state.checked = evaluateDefaultValue();
            }
        }
        _state.disabled = isDisabled();

        //Create a hidden field to store the CheckBox oldValue
        String oldValue = req.getParameter(_state.name);
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);

        // if the checkbox is disabled we need to not write out the hidden
        // field because it can cause the default state to change from
        // true to false.  Disabled check boxes do not postback.
        if (!_state.disabled) {
            _hiddenState.name = hiddenParamName;
            if (oldValue == null) {
                _hiddenState.value = "false";
            }
            else {
                _hiddenState.value = oldValue;
            }
            TagRenderingBase hiddenTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_HIDDEN_TAG, req);
            hiddenTag.doStartTag(writer, _hiddenState);
            hiddenTag.doEndTag(writer);
        }

        _state.type = INPUT_CHECKBOX;

        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_BOOLEAN_TAG, req);
        br.doStartTag(writer, _state);

        if (!ref.isNull())
            write((String) ref.getRef());

        // Continue processing this page
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
        _hiddenState.clear();
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
     * @netui:attribute required="false" rtexprvalue="true"  type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        if (accessKey == 0x00)
            return;
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCESSKEY, Character.toString(accessKey));
    }

    /**
     * Sets the alt attribute value.
     * @param alt the alt value.
     * @jsptagref.attributedescription The alt attribute of the element.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false"  rtexprvalue="true"
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
     * @netui:attribute required="false"  rtexprvalue="true"  type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }
}
