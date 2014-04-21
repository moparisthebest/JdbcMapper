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
package org.apache.beehive.netui.tags.databinding.repeater.pad;

import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.tags.databinding.repeater.RepeaterComponent;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * Sets the number of items rendered by a {@link org.apache.beehive.netui.tags.databinding.repeater.Repeater} tag.
 * The &lt;netui-data:pad> tag has the ability to turn an irregular data set in the &lt;netui-data:repeater> tag
 * into a regular data set through the use of the three attributes:
 * <blockquote>
 * <ul>
 * <li><code>maxRepeat</code> - truncates the rendering of the data set</li>
 * <li><code>minRepeat</code> - forces the &lt;netui-data:repeater> to render
 * a minimum number of elements</li>
 * <li><code>padText</code> - default text to render if the data set does not have the
 * minimum number of elements</li>
 * </ul>
 * </blockquote>
 * If the <code>padText</code> attribute is unset, the body of the &lt;netui-data:pad> tag is used as the default text.
 * </p>
 * <p>
 * Note, the <code>container</code> data binding context should not be used inside of the Pad's <code>padText</code>
 * attribute as binding to both the <code>item</code> and <code>index</code> could product unexpected results.
 * </p>
 * <p>
 * In the following example, assume that the &lt;netui-data:repeater> tag iterates over a {@link java.util.List} object.
 * The &lt;netui-data:pad> limits the iteration to three cycles, no matter how many elements are actually in the List.
 * <pre>
 *     &lt;netui-data:repeater dataSource="pageFlow.myList">
 *         &lt;netui-data:pad maxRepeat="3"/>
 *             &lt;netui-data:repeaterItem>
 *                 &lt;netui:span value="${container.item}" />
 *             &lt;/netui-data:repeaterItem>
 *     &lt;/netui-data:repeater>
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription <p>
 * Sets the number of items rendered by a {@link org.apache.beehive.netui.tags.databinding.repeater.Repeater} tag.
 * The &lt;netui-data:pad> tag has the ability to turn an irregular data set in the &lt;netui-data:repeater> tag
 * into a regular data set through the use of the three attributes:
 * <blockquote>
 * <ul>
 * <li><code>maxRepeat</code> - truncates the rendering of the data set</li>
 * <li><code>minRepeat</code> - forces the &lt;netui-data:repeater> to render
 * a minimum number of elements</li>
 * <li><code>padText</code> - default text to render if the data set does not have the
 * minimum number of elements</li>
 * </ul>
 * </blockquote>
 * If the <code>padText</code> attribute is unset, the body of the &lt;netui-data:pad> tag is used as the default text.
 * </p>
 * <p>
 * Note, the <code>container</code> data binding context should not be used inside of the Pad's <code>padText</code>
 * attribute as binding to both the <code>item</code> and <code>index</code> could product unexpected results.
 * </p>
 *
 * @example
 * <p>
 * In the following example, assume that the &lt;netui-data:repeater> tag iterates over a {@link java.util.List} object.
 * The &lt;netui-data:pad> limits the iteration to three cycles, no matter how many elements are actually in the List.
 * <pre>
 *     &lt;netui-data:repeater dataSource="pageFlow.myList">
 *         &lt;netui-data:pad maxRepeat="3"/>
 *             &lt;netui-data:repeaterItem>
 *                 &lt;netui:span value="${container.item}" />
 *             &lt;/netui-data:repeaterItem>
 *     &lt;/netui-data:repeater>
 * </pre>
 * </p>
 *
 * @netui:tag name="pad"
 *            description="Repeater tag set tag that can pad an irregularly side data set in order to force the repeater to render a regular number of rows"
 */
public class Pad
    extends RepeaterComponent {

    private static final Logger LOGGER = Logger.getInstance(Pad.class);

    private String _padText = null;
    private Integer _maxRepeat = null;
    private Integer _minRepeat = null;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "Pad";
    }

    /**
     * Set the text that will be used when padding a {@link org.apache.beehive.netui.tags.databinding.repeater.Repeater}
     * that renders its body too few times.
     *
     * @param padText the text that is used to pad a repeater's content
     * @jsptagref.attributedescription
     * Set the text that will be used when padding a {@link org.apache.beehive.netui.tags.databinding.repeater.Repeater}
     * that renders its body too few times.
     * @jsptagref.attributesyntaxvalue <i>string_padText</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setPadText(String padText) {
        _padText = padText;
    }

    /**
     * Set the maximum number of items that will be rendered by a &lt;netui-data:repeater> tag.  If the size
     * of the data set is greater than this, only this many items will be rendered.  This value can be an integer
     * or can be an expression that resolves to an integer.
     *
     * @param maxRepeat an integer or an expression that references an integer for the maximum number of items to render
     * @jsptagref.attributedescription
     * Set the maximum number of items that will be rendered by a &lt;netui-data:repeater> tag.  If the size
     * of the data set is greater than this, only this many items will be rendered.  This value can be an integer
     * or can be an expression that resolves to an integer.
     * @jsptagref.attributesyntaxvalue <i>integer_or_expression_maxRepeat</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setMaxRepeat(int maxRepeat) {
        _maxRepeat = new Integer(maxRepeat);
    }

    /**
     *
     * @param minRepeat an integer or an expression that references an integer for the minimum number of items to render
     * @jsptagref.attributedescription
     * Set the minimum number of items that will be rendered by a &lt;netui-data:repeater> tag.  If the size of
     * the data set is smaller than this, the data set will be padded with the value of the <code>padText</code>
     * attribute. This value can be an integer or can be an expression that resolves to an integer.
     * @jsptagref.attributesyntaxvalue <i>integer_or_expression_minRepeat</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setMinRepeat(int minRepeat) {
        _minRepeat = new Integer(minRepeat);
    }

    protected boolean shouldRender() {
        if(_padText == null)
            return true;
        else
            return false;
    }

    public int doAfterBody()
            throws JspException {
        return SKIP_BODY;
    }

    /**
     * Complete rendering the body of this tag.  If the padText property was unset,
     * the body of the tag is used as the pad text.
     *
     * @param state the current render state of the Repeater
     * @return EVAL_PAGE
     * @throws JspException if an error occurs that can not be reported on the page
     */
    protected int renderEndTag(int state)
            throws JspException {

        if(_padText == null && bodyContent != null) {
            _padText = bodyContent.getString();
        }

        if(hasErrors()) {
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }

        PadContext pc = new PadContext(_padText,
                (_minRepeat != null ? _minRepeat.intValue() : PadContext.DEFAULT_VALUE),
                (_maxRepeat != null ? _maxRepeat.intValue() : PadContext.DEFAULT_VALUE));

        getRepeater().setPadContext(pc);

        return EVAL_PAGE;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        if(bodyContent != null)
            bodyContent.clearBody();
        _padText = null;
        _maxRepeat = null;
        _minRepeat = null;
    }

    /**
     * Verify that the attributes set on the tag are correct.  Check:
     * <ul>
     * <li>The minRepeat value can be converted into an integer</li>
     * <li>The maxRepeat value can be converted into an integer</li>
     * <li>The minRepeat value is less than or equal to the maxRepeat value</li>
     * </ul>
     *
     * @throws JspException if an error occurs that can not be reported on the page
     */
    protected void verifyAttributes()
            throws JspException {
        if(_maxRepeat != null && _maxRepeat.intValue() <= 0) {
            String msg = Bundle.getErrorString("Tags_Pad_intTooSmall", new Object[]{_maxRepeat, "maxRepeat"});
            getRepeater().registerTagError(msg, null);
        }

        if(_minRepeat != null && _minRepeat.intValue() <= 0) {
            String msg = Bundle.getErrorString("Tags_Pad_minRepeatIntTooSmall", new Object[]{_minRepeat, "minRepeat"});
            getRepeater().registerTagError(msg, null);
        }

        // check composite properties
        if(_maxRepeat != null &&
                _minRepeat != null &&
                _minRepeat.intValue() > _maxRepeat.intValue()) {
            if(LOGGER.isWarnEnabled()) {
                String msg = "The value of maxRepeat (" + _maxRepeat + ") must be greater than the value of minRepeat (" +
                        _minRepeat + ") on the Repeater Pad tag.";
                LOGGER.warn(msg);
            }

            String msg = Bundle.getErrorString("Tags_Pad_minGreaterThanMax", new Object[]{_maxRepeat, _minRepeat});
            getRepeater().registerTagError(msg, null);
        }
    }
}

