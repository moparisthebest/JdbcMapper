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

import org.apache.beehive.netui.pageflow.ProcessPopulate;
import org.apache.beehive.netui.script.common.DataAccessProviderStack;
import org.apache.beehive.netui.tags.naming.FormDataNameInterceptor;
import org.apache.beehive.netui.tags.naming.IndexedNameInterceptor;
import org.apache.beehive.netui.tags.naming.PrefixNameInterceptor;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.iterator.IteratorFactory;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.util.tags.GroupOption;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.util.*;

/**
 * Groups a collection of CheckBoxOptions, and handles databinding of their values.
 *
 * CheckBoxGroup binds to an Iterator of Strings.
 *
 * If CheckBoxGroup uses any Format tags, it must have those tags come before any nested
 * CheckBoxOption tags.
 * @jsptagref.tagdescription Renders a collection of checkbox options as &lt;input type="checkbox">
 * and handles the data binding.
 *
 * <p><b>Submitting Data</b></p>
 * <p>The &lt;netui:checkBoxGroup> submits data in the form of a String[] object.
 * For example, if the &lt;netui:checkBoxGroup> submits data to a Form Bean field...
 *
 * <pre>    &lt;netui:checkBoxGroup
 *            dataSource="actionForm.userSelections"
 *            optionsDataSource="${pageFlow.availableSelections}" /></pre>
 *
 * ...then the Form Bean field must be a String[] object...
 *
 * <pre>    public static class SubmitForm extends FormData
 *    {
 *        private String[] userSelections;
 *
 *        public void setUserSelections(String[] userSelections)
 *        {
 *            this.userSelections = userSelections;
 *        }
 *
 *        public String[] getUserSelections()
 *        {
 *            return this.userSelections;
 *        }
 *    }</pre>
 *
 * <p><b>Dynamically Defined Checkboxes</b></p>
 * You can dynamically define a set of checkboxes by pointing the <code>optionsDataSource</code> attribute
 * at a String[] object.  When the &lt;netui:checkBoxGroup> is rendered in the browser, a
 * corresponding set of
 * checkboxes will be genereated from the String[] object.
 *
 * <p>For example, if you define a String[] object and get method in the Controller file...
 *
 * <pre>    public String[] availableSelections = {"option1", "option2", "option3"};
 *
 *    public String[] getavailableSelections()
 *    {
 *        return this.availableSelections;
 *    }</pre>
 *
 * ...and reference this String[] from the <code>optionsDataSource</code> attribute...
 *
 * <pre>    &lt;netui:checkBoxGroup
 *            dataSource="actionForm.userSelections"
 *            optionsDataSource="${pageFlow.availableSelections}" /></pre>
 *
 * ...then the appropriate checkboxes will be rendered in the browser.
 *
 * <pre>    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.userSelections}" value="option1">option1&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.userSelections}" value="option2">option2&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.userSelections}" value="option3">option3&lt;/input></pre>
 *
 * For checkboxes to be rendered, either the <code>optionsDataSource</code> attribute must be provided
 * (and point to a String[] object) or the &lt;netui:checkBoxGroup> must have children
 * &lt;netuiCheckBoxOption> tags.
 *
 * <p><b>Setting Default Options</b></p>
 * <p>The <code>defaultValue</code> attribute can be used to determine which checkboxs are checked
 * when they are first rendered in the browser.  The <code>defaultValue</code> attribute
 * should point to a String, if only one checkbox should appear checked, or to a String[] object,
 * if multiple checkboxes should appear checked.
 * @example In this first sample, the &lt;netui:checkBoxGroup>
 * submits data to the Form Bean field <code>preferredColors</code>.
 *
 * <pre>    &lt;netui:checkBoxGroup
 *            dataSource="actionForm.preferredColors"
 *            optionsDataSource="${pageFlow.colors}" /></pre>
 *
 * The <code>optionsDataSource</code> attribute points to a get method for a String[] on the Controller file:
 *
 * <pre>    String[] colors = new String[] {"Red", "Blue", "Green", "Yellow", "White", "Black"};
 *
 *    public String[] getColors()
 *    {
 *        return colors;
 *    }</pre>
 *
 * This automatically renders the appropriate set of checkbox options within the &lt;checkBoxGroup>:
 *
 * <pre>    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Red">Red&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Blue">Blue&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Green">Green&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Yellow">Yellow&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="White">White&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Black">Black&lt;/input></pre>
 *
 * The <code>defaultValue</code> attribute may point to a String or a String[].
 *
 * <pre>    &lt;netui:checkBoxGroup
 *            dataSource="actionForm.preferredColors"
 *            optionsDataSource="${pageFlow.colors}"
 *            defaultValue="${pageFlow.defaultColor}" /></pre>
 *
 * And in the Controller:
 * <pre>    String defaultColor = new String ("Blue");
 *    ...</pre>
 * or
 * <pre>    String[] defaultColor = new String[] {"Red", "Blue"};
 *    ...</pre>
 *
 * In either case, the appropriate
 * checkbox options will appear checked in the browser.
 *
 * <pre>    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Red" checked="true">Red&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Blue" checked="true">Blue&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Green">Green&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Yellow">Yellow&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="White">White&lt;/input>
 *    &lt;input type="checkbox" name="wlw-checkbox_group_key:{actionForm.preferredColors}" value="Black">Black&lt;/input></pre>
 * @netui:tag name="checkBoxGroup" description="Groups a collection of CheckBoxOptions, and handles databinding of their values."
 */
public class CheckBoxGroup
        extends HtmlGroupBaseTag
{
    // @todo: seems like we should write out the hidden field even if the option is disabled.
    private static final Logger logger = Logger.getInstance(CheckBoxGroup.class);

    /**
     * This is the name of the prefixHandler for the checkbox grup.
     */
    public static final String CHECKBOXGROUP_KEY = "checkbox_group_key";

    private static final String OLDVALUE_SUFFIX = "OldValue";

    private InputHiddenTag.State _state = new InputHiddenTag.State();
    private InputHiddenTag.State _hiddenState = new InputHiddenTag.State();

    private List _defaultSelections;
    private boolean _defaultSingleton = false;
    private boolean _defaultSingleValue = false;

    private String[] _match;                 // The actual values we will match against, calculated in doStartTag().
    private Object _dynamicAttrs;            // the Object
    private InternalStringBuilder _saveBody;

    private static final List _internalNamingChain;
    private WriteRenderAppender _writer;

    static
    {
        List l = new ArrayList(3);
        l.add(new FormDataNameInterceptor());
        l.add(new IndexedNameInterceptor());
        l.add(new PrefixNameInterceptor(CHECKBOXGROUP_KEY));
        _internalNamingChain = Collections.unmodifiableList(l);
    }

    static
    {
        org.apache.beehive.netui.pageflow.ProcessPopulate.registerPrefixHandler(CHECKBOXGROUP_KEY, new CheckboxGroupPrefixHandler());
    }

    /**
     * The handler for naming and indexing the CheckBoxGroup.
     */
    public static class CheckboxGroupPrefixHandler
            implements org.apache.beehive.netui.pageflow.RequestParameterHandler
    {
        /**
         * Determines the current state of the CheckBoxGroup based on the Request.
         */
        public void process(HttpServletRequest request, String key, String expr,
                            ProcessPopulate.ExpressionUpdateNode node)
        {
            String[] returnArray = null;

            if (!key.endsWith(OLDVALUE_SUFFIX)) {
                //This select has values and should stay that way
                returnArray = request.getParameterValues(key);
            }
            else {
                //Check the request to see if select also exists
                String newKey = key.substring(0, key.indexOf(OLDVALUE_SUFFIX));
                String[] select = request.getParameterValues(newKey);
                if (select != null) {
                    returnArray = select;
                }
                else {
                    returnArray = new String[0]; //null;
                }
            }

            if (node.expression.endsWith(OLDVALUE_SUFFIX)) {
                node.expression = node.expression.substring(0, node.expression.indexOf(OLDVALUE_SUFFIX));
            }
            node.values = returnArray;

            if (logger.isDebugEnabled()) {
                logger.debug("\n*********************************************\n" +
                        "process with key \"" + key + "\" and expression \"" + node.expression + "\"" + "and result size: "
                        + (returnArray != null ? "" + returnArray.length : null) + "\n" +
                        "*********************************************\n");
            }
        }
    }

    public CheckBoxGroup()
    {
        super();
    }

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "CheckBoxGroup";
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
     * Overrided method to return a list of the possible default values.  The method always return either
     * a <code>List</code> or null.
     * @return a <code>List</code> that represents the default value.
     */
    private Object evaluateDefaultValue()
    {
        Object val = _defaultValue;

        List defaults = null;
        if (val instanceof String) {
            if ("checked".equals(val)) {
                _defaultSingleton = true;
                _defaultSingleValue = true;
                return null;
            }
            else if ("unchecked".equals(val)) {
                _defaultSingleton = true;
                _defaultSingleValue = false;
                return null;
            }

            defaults = new ArrayList();
            defaults.add(val);
            return defaults;
        }

        Iterator optionsIterator = null;
        optionsIterator = IteratorFactory.createIterator(val);

        // log an error, default value is optional so only warn
        if (optionsIterator == null && _defaultValue != null) {
            logger.warn(Bundle.getString("Tags_IteratorError",
                    new Object[]{getTagName(), "defaultValue", _defaultValue}));
        }
        if (optionsIterator == null)
            optionsIterator = IteratorFactory.EMPTY_ITERATOR;

        defaults = new ArrayList();
        while (optionsIterator.hasNext()) {
            defaults.add(optionsIterator.next());
        }

        return defaults;
    }

    /**
     * Checks whether the given value matches one of the CheckBoxGroup's selected
     * CheckBoxOptions.
     * @param value Value to be compared
     */
    public boolean isMatched(String value, Boolean defaultValue)
    {
        if (value == null)
            return false;

        if (_match != null) {
            for (int i = 0; i < _match.length; i++) {
                if (value.equals(_match[i]))
                    return true;
            }
        }
        else {
            // a provided default value will override the group
            if (defaultValue != null)
                return defaultValue.booleanValue();

            // if we have a singleton definition then use that
            if (_defaultSingleton)
                return _defaultSingleValue;

            // check to see if we have a default arraylist with the value in it
            if (_defaultSelections != null)
                return _defaultSelections.contains(value);
        }

        return false;
    }


    /**
     * Determine the set of matches for the CheckBoxGroup
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        ServletRequest req = pageContext.getRequest();
        if (_cr == null)
            _cr = TagRenderingBase.Factory.getConstantRendering(req);

        // get the evaluated dataSource and default values
        Object val = evaluateDataSource();
        _defaultSelections = (List) evaluateDefaultValue();
        if (hasErrors()) {
            return SKIP_BODY;
        }

        // process the default values and create the matching array...
        if (val != null) {
            buildMatch(val);
            if (hasErrors()) {
                return SKIP_BODY;
            }
        }

        // if the checkbox group is disabled do not write out the
        // hidden field.
        _writer = new WriteRenderAppender(pageContext);
        if (!_repeater && !_disabled) {

            //Create hidden field for state tracking
            _state.clear();
            String hiddenParamName = null;
            hiddenParamName = getQualifiedDataSourceName() + OLDVALUE_SUFFIX;
            _state.name = hiddenParamName;
            _state.value = "true";

            TagRenderingBase hiddenTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_HIDDEN_TAG, req);
            hiddenTag.doStartTag(_writer, _state);
            hiddenTag.doEndTag(_writer);
        }

        if (isVertical())
            _cr.TABLE(_writer);

        // if this is a repeater then we shouid prime the pump...
        _dynamicAttrs = evaluateOptionsDataSource();
        assert (_dynamicAttrs != null);
        assert (_dynamicAttrs instanceof Map ||
                _dynamicAttrs instanceof Iterator);

        if (_repeater) {
            if (_dynamicAttrs instanceof Map) {
                _dynamicAttrs = ((Map) _dynamicAttrs).entrySet().iterator();
            }
            if (!(_dynamicAttrs instanceof Iterator)) {
                String s = Bundle.getString("Tags_OptionsDSIteratorError");
                registerTagError(s, null);
                return SKIP_BODY;
            }
            while (((Iterator) _dynamicAttrs).hasNext()) {
                _repCurItem = ((Iterator) _dynamicAttrs).next();
                if (_repCurItem != null)
                    break;
            }
            if (isVertical())
                _cr.TR_TD(_writer);

            DataAccessProviderStack.addDataAccessProvider(this, pageContext);
        }
        _saveBody = new InternalStringBuilder(128);

        // Continue processing this page
        return EVAL_BODY_BUFFERED;

    }

    /**
     * Save any body content of this tag, which will generally be the
     * option(s) representing the values displayed to the user.
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException
    {
        StringBuilderRenderAppender writer = new StringBuilderRenderAppender(_saveBody);
        if (bodyContent != null) {
            String value = bodyContent.getString();
            bodyContent.clearBody();
            if (value == null)
                value = "";
            _saveBody.append(value);
        }

        if (_repeater) {
            ServletRequest req = pageContext.getRequest();
            if (_cr == null)
                _cr = TagRenderingBase.Factory.getConstantRendering(req);

            if (isVertical())
                _cr.end_TD_TR(writer);

            while (((Iterator) _dynamicAttrs).hasNext()) {
                _repCurItem = ((Iterator) _dynamicAttrs).next();
                if (_repCurItem != null) {
                    _repIdx++;
                    if (isVertical())
                        _cr.TR_TD(writer);

                    return EVAL_BODY_AGAIN;
                }
            }
        }

        return SKIP_BODY;
    }

    /**
     * Render the set of CheckBoxOptions.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag()
            throws JspException
    {
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        ServletRequest req = pageContext.getRequest();
        if (_cr == null)
            _cr = TagRenderingBase.Factory.getConstantRendering(req);

        String idScript = null;
        String altText = null;
        char accessKey = 0x00;


        // Render a tag representing the end of our current form
        if (_saveBody != null)
            write(_saveBody.toString());

        // if this is a repeater then we have created the content in the body so we write that
        if (_repeater) {
            // Render a tag representing the end of our current form
            if (isVertical())
                _cr.end_TABLE(_writer);

            if (idScript != null)
                write(idScript);

            localRelease();
            return EVAL_PAGE;
        }

        // non repeater working against the options data source
        assert(_dynamicAttrs != null);
        if (_dynamicAttrs instanceof Map) {
            Map dynamicCheckboxesMap = (Map) _dynamicAttrs;
            Iterator keyIterator = dynamicCheckboxesMap.keySet().iterator();
            int idx = 0;
            while (keyIterator.hasNext()) {
                Object optionValue = keyIterator.next();
                String optionDisplay = "";
                if (dynamicCheckboxesMap.get(optionValue) != null)
                    optionDisplay = dynamicCheckboxesMap.get(optionValue).toString();
                if (optionValue != null) {
                    addOption(_writer, INPUT_CHECKBOX, optionValue.toString(), optionDisplay, idx++, altText, accessKey, _disabled);
                }

                if (hasErrors()) {
                    reportErrors();
                    if (isVertical()) {
                        _cr.end_TABLE(_writer);
                    }
                    localRelease();
                    return EVAL_PAGE;
                }
                write("\n");
            }
        }
        else {
            assert(_dynamicAttrs instanceof Iterator);
            Iterator it = (Iterator) _dynamicAttrs;
            int idx = 0;
            while (it.hasNext()) {
                Object o = it.next();
                if (o == null)
                    continue;

                if (o instanceof GroupOption) {
                    GroupOption go = (GroupOption) o;
                    addOption(_writer, INPUT_CHECKBOX, go.getValue(), go.getName(), idx++, go.getAlt(), go.getAccessKey(), _disabled);
                }
                else {
                    String checkboxValue = o.toString();
                    addOption(_writer, INPUT_CHECKBOX, checkboxValue, checkboxValue, idx++, altText, accessKey, _disabled);
                }

                if (hasErrors()) {
                    reportErrors();
                    if (isVertical()) {
                        _cr.end_TABLE(_writer);
                    }
                    localRelease();
                    return EVAL_PAGE;
                }
                write("\n");
            }
        }

        if (isVertical())
            _cr.end_TABLE(_writer);

        if (idScript != null)
            write(idScript);

        localRelease();
        return EVAL_PAGE;
    }

    public void createHiddenField(AbstractRenderAppender results)
            throws JspException
    {
        if (_repIdx == 0 && !_disabled) {

            ServletRequest req = pageContext.getRequest();

            //Create hidden field for state tracking
            String hiddenParamName = null;
            hiddenParamName = getQualifiedDataSourceName() + OLDVALUE_SUFFIX;
            _hiddenState.name = hiddenParamName;
            _hiddenState.value = "true";

            TagRenderingBase hiddenTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_HIDDEN_TAG, req);
            hiddenTag.doStartTag(results, _hiddenState);
            hiddenTag.doEndTag(results);
        }

    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        // cleanup the context variables used for binding during repeater
        if (_repeater)
            DataAccessProviderStack.removeDataAccessProvider(pageContext);

        super.localRelease();

        _defaultSelections = null;
        _match = null;
        _dynamicAttrs = null;
        _saveBody = null;
        _defaultSingleton = false;
        _defaultSingleValue = false;
        _writer = null;
        
        _state.clear();
        _hiddenState.clear();
    }

    // This method will build the match list, should this be a hashmap?
    private void buildMatch(Object val)
    {
        if (val instanceof String[]) {
            _match = (String[]) val;
        }
        else {
            Iterator matchIterator = null;
            // this should return null, but we should handle it it does
            matchIterator = IteratorFactory.createIterator(val);
            if (matchIterator == null)
                matchIterator = IteratorFactory.EMPTY_ITERATOR;

            List matchList = new ArrayList();
            while (matchIterator.hasNext()) {
                Object o = matchIterator.next();
                if (o != null)
                    matchList.add(o.toString());
            }
            int size = matchList.size();

            _match = new String[size];
            for (int i = 0; i < size; i++) {
                _match[i] = matchList.get(i).toString();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("****** CheckboxGroup Matches ******");
            if (_match != null) {
                for (int i = 0; i < _match.length; i++) {
                    logger.debug(i + ": " + _match[i]);
                }
            }
        }
    }
}
