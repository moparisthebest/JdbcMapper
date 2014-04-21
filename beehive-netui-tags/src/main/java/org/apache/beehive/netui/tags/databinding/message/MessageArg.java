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
package org.apache.beehive.netui.tags.databinding.message;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.beehive.netui.tags.AbstractClassicTag;

/**
 * <p>
 * This tag should be used in conjunction with the {@link Message} tag to provide a message argument to the
 * message format provided in the {@link Message#setValue(Object)} method.  Before formatting, the
 * value of this tag will be substituted into the message.
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * This tag should be used in conjunction with the {@link Message} tag to provide a message argument to the
 * message format provided in the {@link Message#setValue(Object)} method.  Before formatting, the
 * value of this tag will be substituted into the message.
 * </p>
 * @example
 * <p>For an example that uses the &lt;netui-data:messageArg&gt; tag, see the {@link Message} description.
 * @deprecated This tag has been deprecated in favor of the i18n tags available in JSTL.
 * @netui:tag name="messageArg"
 *            deprecated="true"
 *            description="Allows you to set values that are used as arguments to the Message tag. The formatted message results are available to the page context."
 */
public class MessageArg
        extends AbstractClassicTag {

    private Object _value = null;

    public String getTagName() {
        return "MessageArg";
    }

    /**
     * @jsptagref.attributedescription A string value for the argument.
     * @jsptagref.attributesyntaxvalue <i>string_argument</i>
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setValue(Object value) {
        this._value = value;
    }

    public int doStartTag()
            throws JspException {
        // verify parent tag
        if(!(getParent() instanceof Message)) {
            throw new JspException("Invalid Parent");
        }

        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag()
            throws JspException {
        if(hasErrors())
            reportErrors();
        else
            ((Message)getParent()).addMessageArgument(_value);

        localRelease();

        return EVAL_PAGE;
    }

    protected void localRelease() {
        super.localRelease();
        _value = null;
    }
}
