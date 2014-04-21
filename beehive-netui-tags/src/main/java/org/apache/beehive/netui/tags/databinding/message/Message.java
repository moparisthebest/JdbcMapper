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

import org.apache.beehive.netui.tags.AbstractClassicTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This tag provides a message schema, which can be parameterized to construct customizable messages.
 * Curly-braces are used to identify argument place holders in the schema:
 * <p/>
 * For example, the following will format a message and place the result in a {@link javax.servlet.jsp.PageContext}
 * attribute named <code>message</code>.
 * <pre>
 * &lt;%
 *    pageContext.setAttribute("msgSkeleton", new String("Hello {0}. {1} {2}, the current date and time are {3}."));
 * %>
 * &lt;netui-data:message value="${pageScope.msgSkeleton}" resultId="message">
 * </pre>
 * </p>
 * <p>
 * The followingn example defines a message schema, while the {@link MessageArg} tags provide the parameters that
 * plug values into the schema.  In the following example, the &lt;netui-data:message> tag uses the <code>value</code>
 * attribute to bind to the message schema (which was earlier added to the
 * {@link javax.servlet.jsp.PageContext javax.servlet.jsp.PageContext} object. The two &lt;netui-data:messageArg>
 * tags provide the parameters to plug into the schema.
 * <pre>
 *    &lt;%
 *        pageContext.setAttribute("msgSkeleton", new String("To read about {0}, go to {1}."));
 *    %&gt;
 *    ...
 *    &lt;netui-data:message value="${pageScope.msgSkeleton}" resultId="message"&gt;
 *        &lt;netui-data:messageArg value="messaging"/&gt;
 *        &lt;netui-data:messageArg value="my web page"/&gt;
 *    &lt;/netui-data:message&gt;
 *    ...
 *    &lt;netui:span value="${pageScope.message}"/&gt;</pre>
 * <p/>
 * <p>The following message is output to the JSP page:<p>
 * <p/>
 * <pre>
 *     To read about messaging, go to my web page.
 * </pre>
 * </p>
 *
 * @jsptagref.tagdescription
 * <p>
 * This tag provides a message schema, which can be parameterized to construct customizable messages.
 * Curly-braces are used to identify argument place holders in the schema:
 * <p/>
 * <pre>
 * &lt;%
 *    pageContext.setAttribute("msgSkeleton", new String("Hello {0}. {1} {2}, the current date and time are {3}."));
 *    %>
 *    &lt;netui-data:message value="${pageScope.msgSkeleton}" resultId="message"></pre>
 * @example
 * <p>
 * The followingn example defines a message schema, while the {@link MessageArg} tags provide the parameters that
 * plug values into the schema.  In the following example, the &lt;netui-data:message> tag uses the <code>value</code>
 * attribute to bind to the message schema (which was earlier added to the
 * {@link javax.servlet.jsp.PageContext javax.servlet.jsp.PageContext} object. The two &lt;netui-data:messageArg>
 * tags provide the parameters to plug into the schema.
 * <pre>
 *    &lt;%
 *        pageContext.setAttribute("msgSkeleton", new String("To read about {0}, go to {1}."));
 *    %&gt;
 *    ...
 *    &lt;netui-data:message value="${pageScope.msgSkeleton}" resultId="message"&gt;
 *        &lt;netui-data:messageArg value="messaging"/&gt;
 *        &lt;netui-data:messageArg value="my web page"/&gt;
 *    &lt;/netui-data:message&gt;
 *    ...
 *    &lt;netui:span value="${pageScope.message}"/&gt;</pre>
 * <p/>
 * <p>The following message is output to the JSP page:<p>
 * <p/>
 * <pre>
 *     To read about messaging, go to my web page.
 * </pre>
 * </p>
 *
 * @deprecated This tag has been deprecated in favor of the i18n tags available in JSTL.
 * @netui:tag name="message"
 *            deprecated="true"
 *            description="Allows you to format messages according to any sequence you want, using one or more values from arguments defined in MessageArg tag(s). The results are available to the page context."
 */
public class Message
    extends AbstractClassicTag {

    public static final String MESSAGE_ARG_KEY = "netui_bundleMessageArguments";

    private String _resultId = null;
    private Object _value = null;
    private List _argList = null;

    public String getTagName() {
        return "Message";
    }

    /**
     * Set the attribute name under which the output formatted message will be available.  The message
     * will be stored in the JSP EL implicit object <code>pageScope</code>.  If the value of this attribute
     * is <code>foo</code>, the resulting message will be available with <code>${pageScope.foo}</code>.
     *
     * @jsptagref.attributedescription
     * Set the attribute name under which the output formatted message will be available.  The message
     * will be stored in the JSP EL implicit object <code>pageScope</code>.  If the value of this attribute
     * is <code>foo</code>, the resulting message will be available with <code>${pageScope.foo}</code>.
     * @jsptagref.attributesyntaxvalue <i>string_result</i>
     * @netui:attribute required="true"
     */
    public void setResultId(String resultId) {
        _resultId = resultId;
    }

    /**
     * <p>
     * Set the object to use when formatting a message.  This value should be either a String or be convertable
     * to a String via its {@link Object#toString()} method.  In ordet for format the message, this value
     * should appear as:
     * <pre>
     *     Hello, {0}!
     * </pre>
     * where the <code>{0}</code> can be filled in during formatting via the {@link MessageArg} tag.
     * </p>
     * @jsptagref.attributedescription
     * <p>
     * Set the object to use when formatting a message.  This value should be either a String or be convertable
     * to a String via its {@link Object#toString()} method.  In ordet for format the message, this value
     * should appear as:
     * <pre>
     *     Hello, {0}!
     * </pre>
     * where the <code>{0}</code> can be filled in during formatting via the {@link MessageArg} tag.
     * </p>
     * @jsptagref.attributesyntaxvalue <i>expression_value</i>
     * @netui:attribute required="true" rtexprvalue="true"
     */
    public void setValue(Object value) {
        _value = value;
    }

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag()
            throws JspException {
        Object[] args = (_argList != null ? _argList.toArray() : null);

        if(hasErrors()) {
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }

        if(_value == null) {
            localRelease();
            return EVAL_PAGE;
        }

        Object exprVal = null;
        try {
            exprVal = java.text.MessageFormat.format(_value.toString(), args);
        }
        catch(Exception e) {
            String msg = "Error formatting message \"" + _value.toString() + "\".  Cause: " + e.getLocalizedMessage();
            registerTagError(msg, null);
        }

        if(hasErrors()) {
            reportErrors();
            localRelease();
            return EVAL_PAGE;
        }

        Message msgParent = null;
        // if nested in a <netui-data:message ... /> tag, add the result of this tag as an argument.
        if((msgParent = (Message)SimpleTagSupport.findAncestorWithClass(this, Message.class)) != null) {
            msgParent.addMessageArgument(exprVal);
        }
        else
            pageContext.setAttribute(_resultId, exprVal);

        localRelease();
        return EVAL_PAGE;
    }

    public void addMessageArgument(Object messageArgument) {
        if(_argList == null)
            _argList = new ArrayList();

        _argList.add(messageArgument);
    }

    protected void localRelease() {
        super.localRelease();
        _argList = null;
        _value = null;
        _resultId = null;
    }
}
