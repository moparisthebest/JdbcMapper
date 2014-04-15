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
import org.apache.beehive.netui.tags.rendering.LabelTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * @jsptagref.tagdescription Associates text with an input element in a {@link Form}.
 * @netui:tag name="label" description="a <label> element which may point to a form control."
 */
public class Label extends LabelBase
        implements IFormattable
{
    private static final Logger logger = Logger.getInstance(Label.class);

    private LabelTag.State _state = new LabelTag.State();

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
     * Set the <code>for</code> attribute.
     * @param forAttr the for attribute.
     * @jsptagref.attributedescription The value of this attribute matches a tagId on an form input and links the value to that control.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_forAttr</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The value of this attribute matches a tagId on an form input and links the value to that control."
     */
    public void setFor(String forAttr)
    {
        _state.forAttr = forAttr;
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

        String labelValue = null;

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
        labelValue = (usingDefault && !_formatDefaultValue) ?
                labelObject.toString() : formatText(labelObject);

        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        // fully qualify the for attribute if it exists
        if (_state.forAttr != null) {
            _state.forAttr = getIdForTagId(_state.forAttr);

        }

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.LABEL_TAG, req);
        br.doStartTag(writer, _state);

        // push the evaluated expression when we are not client side bound...
        //if (!usingDefault)
        //    labelValue = formatText(labelValue);

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
