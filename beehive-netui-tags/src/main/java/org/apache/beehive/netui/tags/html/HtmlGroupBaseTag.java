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

import org.apache.beehive.netui.script.common.IDataAccessProvider;
import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.ExpressionHandling;
import org.apache.beehive.netui.tags.IAttributeConsumer;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.iterator.IteratorFactory;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class that provides the <code>dataSource</code>, <code>defaultValue</code>, and
 * <code>optionsDataSource</code> attributes.
 */
abstract public class HtmlGroupBaseTag
        extends AbstractClassicTag implements IDataAccessProvider, HtmlConstants, IAttributeConsumer
{
    private static final Logger logger = Logger.getInstance(HtmlGroupBaseTag.class);

    /**
     * Constant defining a horizontal layout of the options.
     */
    public final String HORIZONTAL_VALUE = "horizontal";

    /**
     * Constant defining a vertical layout of the options.
     */
    public final String VERTICAL_VALUE = "vertical";

    private InputBooleanTag.State _inputState = new InputBooleanTag.State();
    private SpanTag.State _spanState = new SpanTag.State();
    protected ConstantRendering _cr;

    private HashMap _attrs;

    protected String _dataSource;             // The attribute value for the dataSource
    protected Object _defaultValue;           // A String that contains or references the data to render for the default value of this tag.
    protected Object _optionsDataSource;      // The value of the options data source.
    protected boolean _disabled;

    private String _orientation;            // legal values "horizontal, vertical"
    private Boolean _orientVal;             // Three state boolean if we are doing virtical layout

    private String _realName;                // The name that is the result of do naming

    private String _style;                  // The style attribute
    private String _class;                  // The class attribute

    protected String _labelStyle = null;
    protected String _labelStyleClass = null;

    protected boolean _repeater;          // Boolean flag indicating if this is a repeater or not

    // These variables are protected explicitly so they can be accessed by subclasses
    protected int _repIdx = 0;            // The current index for repeating over the optionsDataSource
    protected Object _repCurItem;         // The current item access by the IDataAccessProvider

    public HtmlGroupBaseTag()
    {
        super();
    }

    /**
     * @param value
     * @return boolean
     */
    public abstract boolean isMatched(String value, Boolean defaultValue);

    /**
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>href</code>
     * attribute.  The <code>checkBoxGroup</code> and <code>radioButtonGroup</code> support two facets,
     * <code>input</code> and <code>span</code>.  The <code>input</code> is the default and will attach
     * attributes to the &lt;input> element.  The <code>span</code> facet will attach attributes to the
     * &lt;span> elements which represents the label.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
    {
        // validate the name attribute, in the case of an error simply return.
        if (name == null || name.length() <= 0) {
            String s = Bundle.getString("Tags_AttributeNameNotSet");
            registerTagError(s, null);
            return;
        }

        // it's not legal to set the id or name attributes this way
        if (name.equals(ID) || name.equals(NAME)) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
            return;
        }

        // handle the facet.  Span will place stuff into the spanState input is the default
        // so we don't need to do anything.
        if (facet != null) {
            if (facet.equals("span")) {
                _spanState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, name, value);
                return;
            }
            else if (facet.equals("input")) {
                // do nothing...
            }
            else {
                String s = Bundle.getString("Tags_AttributeFacetNotSupported", new Object[]{facet});
                registerTagError(s, null);
                return;
            }
        }

        // don't set the value on the input
        if (name.equals(VALUE)) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
            return;
        }

        // we place the state into the special attribute map.
        if (_attrs == null) {
            _attrs = new HashMap();
        }
        _attrs.put(name, value);
    }

    /**
     * Return the qualified name of the checkBoxGroup.  The qualified name is the created
     * by calling <code>doNaming()</code>.
     * @return the qualified name based upon the <code>dataSource</code> name.
     * @throws JspException
     */
    public final String getQualifiedDataSourceName()
            throws JspException
    {
        if (_realName == null)
            _realName = doNaming();
        return _realName;
    }

    /**
     * Returns the boolean value or expression indicating the
     * disable state of the RadioButtonGroup.
     * @return the disabled state (true or false) or an expression
     */
    public boolean isDisabled()
    {
        return _disabled;
    }

    /**
     * Set the disable state either with the literal "true" or "false"
     * or with an expression.
     * @param disabled true or false or an expression
     * @jsptagref.attributedescription Set the disable state either with the literal "true"
     * or "false" or with an expression.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_disabled</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="boolean"
     * description="Set the disable state either with the literal "true" or "false"
     * or with an expression."
     */
    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /**
     * Set the orientation of the resulting options group.  The layout will be either <code>horizontal</code>
     * or <code>vertical</code>.  The default is <code>horizontal</code>.
     * @param orientation "vertical" or "horizontal"
     * @jsptagref.attributedescription Set the orientation of the resulting options group. Either "<code>horizontal</code>" or "<code>vertical</code>".  The default is <code>horizontal</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_orientation</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Set the orientation of the resulting options group. Either
     * <code>horizontal</code> or <code>vertical</code>.  The default is <code>horizontal</code>."
     */
    public void setOrientation(String orientation)
    {
        _orientation = setNonEmptyValueAttribute(orientation);
    }

    /**
     * Returns <code>true</code> if vertical layout is set.
     * @return <code>true</code> if vertical layout is set
     */
    public boolean isVertical()
    {
        if (_orientVal == null) {
            boolean val = VERTICAL_VALUE.equalsIgnoreCase(_orientation);
            _orientVal = new Boolean(val);
        }
        return _orientVal.booleanValue();
    }

    /**
     * Set whether repeating of contained options is on.
     * @param repeater the repeater value ("true" or "false")
     * @jsptagref.attributedescription Set whether repeating of contained options is on.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_repeater</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="boolean"
     * description="Set whether repeating of contained options is on."
     */
    public void setRepeater(boolean repeater)
    {
        _repeater = repeater;
    }

    /**
     * Gets whether a repeating contained options is on.
     * @return the repeater value
     */
    public boolean isRepeater()
    {
        return _repeater;
    }

    /**
     * Sets the style of the rendered html tag.
     * @param style the html style.
     * @jsptagref.attributedescription Specifies style information for the current element.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the style of the rendered html tag."
     */
    public void setStyle(String style)
    {
        _style = setNonEmptyValueAttribute(style);
    }

    /**
     * Sets the style class of the rendered html tag.
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class (a style sheet selector).
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_styleClass</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Sets the style class of the rendered html tag."
     */
    public void setStyleClass(String styleClass)
    {
        _class = setNonEmptyValueAttribute(styleClass);
    }

    /**
     * Return the label style for each contained CheckBoxOption..
     * @return the label style
     */
    public String getLabelStyle()
    {
        return _labelStyle;
    }

    /**
     * Set the label style for each contained CheckBoxOption.
     * @param labelStyle the label style
     * @jsptagref.attributedescription Set the label style for each contained CheckBoxOption.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_labelStyle</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Set the label style for each contained CheckBoxOption."
     */
    public void setLabelStyle(String labelStyle)
    {
        _labelStyle = setNonEmptyValueAttribute(labelStyle);
    }

    /**
     * Return the label style class for each contained CheckBoxOption..
     * @return the label style
     */
    public String getLabelStyleClass()
    {
        return _labelStyleClass;
    }

    /**
     * Set the label style class for each contained CheckBoxOption.
     * @param labelStyleClass the label style
     * @jsptagref.attributedescription Set the label style class for each contained CheckBoxOption.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_labelStyleClass</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Set the label style class for each contained CheckBoxOption."
     */
    public void setLabelStyleClass(String labelStyleClass)
    {
        _labelStyleClass = setNonEmptyValueAttribute(labelStyleClass);
    }


    /**
     * Sets the tag's data source (can be an expression).
     * @param dataSource the data source
     * @jsptagref.attributedescription An expression to be evaluated. It determines
     * the source of populating data for the tag
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_dataSource</i>
     * @netui:attribute required="true"
     * description="Sets the tag's data source."
     */
    public void setDataSource(String dataSource)
            throws JspException
    {
        _dataSource = dataSource;
    }

    /**
     * Gets the tag's data source (can be an expression).
     * @return the data source
     */
    public String getDataSource()
    {
        return "{" + _dataSource + "}";
    }

    //********************************** IDataAccessProvider Interface  ******************************
    // getDataSource is implemented above

    /**
     * Get the current index in this iteration.  This should be a
     * zero based integer that increments after each iteration.
     * @return the current index of iteration or 0
     */
    public int getCurrentIndex()
    {
        return _repIdx;
    }

    /**
     * Get the current data item in this IDataAccessProvider.
     * @return the current data item or <code>null</code>
     */
    public Object getCurrentItem()
    {
        return _repCurItem;
    }

    /**
     * Get a metadata object for the current item.  This interface
     * is optional, and implementations of this interface are
     * provided by the IDataAccessProvider interface.  See these
     * implementations for information about their support for
     * current item metadata.
     * @return the current metadata or <code>null</code> if no metadata can be
     *         found or metadata is not supported by a IDataAccessProvider implementation
     */
    public Object getCurrentMetadata()
    {
        return null;
    }

    /**
     * Get the parent IDataAccessProvider of a IDataAccessProvider.  A IDataAccessProvider
     * implementation may be able to nest IDataAccessProviders.  In this case,
     * it can be useful to be able to also nest access to data from parent
     * providers.  Implementations of this interface are left with having
     * to discover and export parents.  The return value from this call
     * on an implementing Object can be <code>null</code>.
     * @return the parent IDataAccessProvider or <code>null</code> if this method
     *         is not supported or the parent can not be found.
     */
    public IDataAccessProvider getProviderParent()
    {
        return (IDataAccessProvider) findAncestorWithClass(this, IDataAccessProvider.class);
    }

    /**
     * Return an <code>ArrayList</code> which represents a chain of <code>INameInterceptor</code>
     * objects.  This method by default returns <code>null</code> and should be overridden
     * by objects that support naming.
     * @return an <code>ArrayList</code> that will contain <code>INameInterceptor</code> objects.
     */
    protected List getNamingChain()
    {
        return AbstractClassicTag.DefaultNamingChain;
    }

    /**
     * Return the Object that is represented by the specified data source.
     * @return Object
     * @throws JspException
     */
    protected Object evaluateDataSource()
            throws JspException
    {
        ExpressionHandling expr = new ExpressionHandling(this);
        String dataSource = "{" + _dataSource + "}";
        String ds = expr.ensureValidExpression(dataSource, "dataSource", "DataSourceError");
        if (ds == null)
            return null;

        // have a valid expression
        return expr.evaluateExpression(dataSource, "dataSource", pageContext);
    }

    protected String doNaming()
            throws JspException
    {
        String dataSource = "{" + _dataSource + "}";
        return applyNamingChain(dataSource);
    }

    /**
     * Sets the default value (can be an expression).
     * @param defaultValue the default value
     * @jsptagref.attributedescription The String literal or expression to be used as the default value.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_default</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The String literal or expression to be used as the default value."
     */
    public void setDefaultValue(Object defaultValue)
            throws JspException
    {
        if (defaultValue == null) {
            String s = Bundle.getString("Tags_AttrValueRequired", new Object[]{"defaultValue"});
            registerTagError(s, null);
            return;
        }
        _defaultValue = defaultValue;
    }

    /**
     * Gets the options datasource value (an expression).
     * @return the options datasource
     */
    public Object getOptionsDataSource()
    {
        return _optionsDataSource;
    }

    /**
     * Sets the options datasource value (an expression).
     * @param optionsDataSource the options datasource
     * @jsptagref.attributedescription Sets the options datasource value (an expression).
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>expression_optionsDataSource</i>
     * @netui:attribute required="false" rtexprvalue="true" type="java.lang.Object"
     * description="Sets the options datasource value."
     */
    public void setOptionsDataSource(Object optionsDataSource)
            throws JspException
    {
        if (optionsDataSource == null) {
            String s = Bundle.getString("Tags_AttrValueRequired", new Object[]{"optionsDataSource"});
            registerTagError(s, null);
            return;
        }
        _optionsDataSource = optionsDataSource;
    }

    /**
     * Return the real value of the <code>optionDataSource</code> attribute.  The value returned will
     * always be an instance of <code>Iterator</code> This value reflects the
     * result of expression evaluation on the options data source.
     * @return the object that represents the options data source.
     * @throws JspException when something bad happens
     */
    protected Object evaluateOptionsDataSource()
            throws JspException
    {
        if (_optionsDataSource == null) {
            // optionsDataSource is null, so provide an informational message.  This isn't an error because it's
            // possible for tags to list their options inside of their bodies rather than via an optionsDataSource
            logger.info(Bundle.getString("Tags_IteratorError",
                    new Object[]{getTagName(), "optionsDataSource", _optionsDataSource}));
            return IteratorFactory.EMPTY_ITERATOR;
        }

        if (_optionsDataSource instanceof Map)
            return _optionsDataSource;

        Iterator it;
        it = IteratorFactory.createIterator(_optionsDataSource);
        if (it == null)
            it = IteratorFactory.EMPTY_ITERATOR;

        assert (it != null && it instanceof Iterator);
        return it;
    }

    /**
     * This will create a new option in the HTML.
     */
    protected void addOption(AbstractRenderAppender buffer, String type, String optionValue,
                             String optionDisplay, int idx, String altText, char accessKey, boolean disabled)
            throws JspException
    {
        ServletRequest req = pageContext.getRequest();
        if (_cr == null)
            _cr = TagRenderingBase.Factory.getConstantRendering(req);

        assert(buffer != null);
        assert(optionValue != null);
        assert(optionDisplay != null);
        assert(type != null);

        if (_orientation != null && isVertical()) {
            _cr.TR_TD(buffer);
        }

        _inputState.clear();
        _inputState.type = type;
        _inputState.name = getQualifiedDataSourceName();
        _inputState.value = optionValue;
        _inputState.style = _style;
        _inputState.styleClass = _class;

        if (isMatched(optionValue, null)) {
            _inputState.checked = true;
        }
        _inputState.disabled = isDisabled();
        _inputState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, altText);
        if (accessKey != 0x00)
            _inputState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCESSKEY, Character.toString(accessKey));

        // if there are attributes defined push them to the options.
        if (_attrs != null && _attrs.size() > 0) {
            Iterator iterator = _attrs.keySet().iterator();
            for (; iterator.hasNext();) {
                String key = (String) iterator.next();
                if (key == null)
                    continue;

                String value = (String) _attrs.get(key);
                _inputState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, key, value);
            }
        }

        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_BOOLEAN_TAG, req);
        br.doStartTag(buffer, _inputState);
        br.doEndTag(buffer);

        String ls = _labelStyle;
        String lsc = _labelStyleClass;

        _spanState.style = ls;
        _spanState.styleClass = lsc;

        br = TagRenderingBase.Factory.getRendering(TagRenderingBase.SPAN_TAG, req);
        br.doStartTag(buffer, _spanState);
        buffer.append(optionDisplay);
        br.doEndTag(buffer);

        // backward compatibility this is now overridden by the _orientation
        if (_orientation == null) {
            _cr.BR(buffer);
        }
        else {
            if (isVertical()) {
                _cr.TR_TD(buffer);
            }
            else {
                _cr.NBSP(buffer);
            }
        }
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();
        if (_attrs != null)
            _attrs.clear();
        _cr = null;
        _dataSource = null;
        _defaultValue = null;
        _optionsDataSource = null;
        _disabled = false;
        _orientation = null;
        _orientVal = null;
        _realName = null;
        _style = null;
        _class = null;
        _labelStyle = null;
        _labelStyleClass = null;

        _repIdx = 0;
        _repCurItem = null;
    }

}
