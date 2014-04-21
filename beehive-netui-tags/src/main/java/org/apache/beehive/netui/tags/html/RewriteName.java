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

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.TagConfig;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * Allow a name, typically either an <code>id</code> or <code>name</code> attribute, to participate in URL
 * rewritting.  Some containers rewrite name attributes so they are unique.
 * This tag will cause the name to be made available from <code>lookupIdByTagId</code>
 * JavaScript, which is output from the &lt;netui:html&gt; tag.
 * @jsptagref.tagdescription Allows a name, typically either an <code>id</code> or <code>name</code> attribute,
 * to participate in URL rewritting.  Some containers rewrite name attributes so they are unique.
 * This tag will cause the name to be made available from the <code>lookupIdByTagId( id, tag )</code>
 * JavaScript function.
 * @example In this sample, we are setting the id attribute of the span tag to 'foo'.  The
 * actual value that will be rendered in the HTML may change depending on the
 * container where the web application resides.  For example, a Portal container may
 * render &lt;span name="scope1_foo"> instead of &lt;span name="foo">.
 * But the value 'foo' can be passed to <code>lookupIdByTagId( id, tag )</code> to find the rendered
 * value of the name attribute.
 *
 * <pre>    &lt;span id="&lt;netui:rewriteName name="foo"/&gt;"&gt;</pre>
 * @netui:tag name="rewriteName" description="Allows the URL Rewriter to rewrite the name attribute before it is output into the HTML stream."
 */
public class RewriteName
        extends AbstractClassicTag
{
    private String _name = null;
    private String _resultId = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "RewriteName";
    }

    /**
     * Sets the name to be rewritten.
     * @param name the parameter name.
     * @jsptagref.attributedescription The name which will be rewritten. This value will be output to the page rendered in the browser and may be looked up using 'name'.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The name which will be rewritten. This value will be output to the page rendered in the browser and may be looked up using 'name'."
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets the resultId which, if non-null, will store the real name
     * in the page context under the resultId name;
     * @param resultId the parameter name.
     * @jsptagref.attributedescription A name that will cause the real name to be stored into the page context under this name.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_resultId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="A name that will cause the real name to be stored into the page context under this name."
     */
    public void setResultId(String resultId)
    {
        _resultId = resultId;
    }

    /**
     * Pass the name attribute to the URLRewriter and output the
     * returned value.  Updates the HTML tag to output the mapping.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        String realName = rewriteName(_name);
        if (_resultId != null)
            pageContext.setAttribute(_resultId, realName);

        // @TODO: Is there any way to make this work.  Currently if
        // there is now script container, we will eat the <script> blocks
        // because we cannot write them out in the middle of the tag being
        // written
        IScriptReporter scriptReporter = getScriptReporter();
        ScriptRequestState srs = ScriptRequestState.getScriptRequestState((HttpServletRequest) pageContext.getRequest());
        if (TagConfig.isLegacyJavaScript()) {
            srs.mapLegacyTagId(scriptReporter, _name, realName);
        }
        write(realName);
        localRelease();
        return SKIP_BODY;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();
        _name = null;
        _resultId = null;
    }
}
