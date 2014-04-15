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

import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.SpanTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Generates styled text span based on an expression or literal value.
 *
 * <p>If the resulting value to be output is the <nobr>" "</nobr> String, it will output as
 * the value <nobr><code>"&amp;nbsp;"</code></nobr>.</p>
 * @jsptagref.tagdescription <p>Generates styled text based on a String literal or data binding expression.
 *
 * <p>The &lt;netui:span> tag is similar to the {@link Content} tag,
 * except with respect to the way that it treats
 * characters sensitive to HTML browsers.
 * The &lt;netui:span> tag filters the input string for browser-sensitive characters
 * and replaces these characters
 * with the corresponding entity strings.  For example, if you pass the
 * string '&amp;amp;' to a &lt;netui:span> tag, the string '&amp;amp;amp;' will be written to
 * the HTML source file, and the following will be displayed
 * in the browser: '&amp;amp;'.
 *
 * <p>The following table shows how the &lt;netui:span> and &lt;netui:content> tags treat HTML-sensitive characters.
 * <blockquote>
 * <table border="1">
 * <tr>
 * <td width="30%"><b>tag</b></td>
 * <td width="30%"><b>generated HTML source</b></td>
 * <td width="30%"><b>displayed in browser</b></td>
 * </tr>
 * <tr>
 * <td>&lt;netui:content value="&amp;amp;"/></td>
 * <td>&amp;amp;</td>
 * <td>&</td>
 * </tr>
 * <tr>
 * <td>&lt;netui:span value="&amp;amp;"/></td>
 * <td>&amp;amp;amp;</td>
 * <td>&amp;amp;</td>
 * </tr>
 * </table>
 * </blockquote>
 *
 * <p><b>Note:</b> escaping is <i>not</i> applied to browser-sensitive characters in
 * the <code>defaultValue</code> attribute.
 * @example In this first sample, a &lt;netui:span> tag displays the Form Bean's firstName property.
 * The &lt;netui:span> tag will resolve this data binding expression, and display its value.
 *
 * <pre>    &lt;netui:span value="${actionForm.firstName}" /></pre>
 *
 * <p>In this next sample, the <code>value</code> attribute will resolve to null.
 * This causes the <code>defaultValue</code> to be displayed.  The user will see '&nbsp;'.</p>
 * <pre>    &lt;netui:span value="${pageFlow.somethingNull}" defaultValue="&amp;nbsp;"/></pre>
 *
 * <p>In this next sample, the HTML will contain the text "&amp;amp;nbsp;" and the user will
 * see '&amp;nbsp;'</p>
 * <pre>    &lt;netui:span value="${pageFlow.somethingNull}" defaultValue="&amp;amp;nbsp;"/></pre>
 * @netui:tag name="span" description="Places formatted or dynamically generated text on the page inside an HTML span."
 */
public class Span extends LabelBase
        implements IFormattable
{
    private static final Logger logger = Logger.getInstance(Span.class);

    private SpanTag.State _state = new SpanTag.State();

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Label";
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
     * Prepare the label formatters.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Render the label.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException
    {
        boolean usingDefault = false;
        boolean bypassEscape = false;

        String scriptId = null;
        ServletRequest req = pageContext.getRequest();

        Object labelObject = null;

        // if this is not client side binding, evalute the value
        if (_value != null)
            labelObject = _value;
        else {
            if (_defaultValue != null) {
                labelObject = _defaultValue;
                bypassEscape = HtmlUtils.containsEntity(_defaultValue.toString());
            }
            else {
                logger.warn(Bundle.getString("Tags_LabelExpressionNull", _value));
                labelObject = DEFAULT_NULL_TEXT;
            }
            usingDefault = true;
        }

        // we assume that tagId will over have override id if both
        // are defined.
        if (_state.id != null) {
            scriptId = renderNameAndId((HttpServletRequest) req, _state, null);
        }

        // push the evaluated expression when we are not client side bound...
        String labelValue = (usingDefault && !_formatDefaultValue) ?
                labelObject.toString() : formatText(labelObject);

        // if there were errors in the formatters, report them.
        if (_formatterErrors) {
            if (bodyContent != null) {
                String value = bodyContent.getString().trim();
                bodyContent.clearBody();
                write(value);
            }
        }

        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.SPAN_TAG, req);
        br.doStartTag(writer, _state);

        if (!bypassEscape)
            filter(labelValue, writer, _escapeWhiteSpace);
        else
            write(labelValue);

        br.doEndTag(writer);

        if (scriptId != null)
            write(scriptId);

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
    }
}
