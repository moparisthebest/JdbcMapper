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
package org.apache.beehive.netui.tags.javascript;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.rendering.ScriptTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * ScriptHeader will write the &lt;script> and JavaScript includes into the HTML
 * &lt;head> tag. It can also be used to write the &lt;script> and JavaScript
 * includes in the body given a JSP fragment that uses the {@link ScriptContainer}.
 * @jsptagref.tagdescription ScriptHeader will write the &lt;script> and JavaScript
 * includes into the HTML &lt;head> tag. It can also be used to write the
 * &lt;script> and JavaScript includes in the body given a JSP fragment that
 * uses the {@link ScriptContainer}.
 *
 * <p>This tag is required to be empty. It is also required when using the
 * &lt;netui:tree> element with the attribute runAtClient="true".
 *
 * <p>The &lt;scriptHeader> tag should have a parent &lt;netui:html> or
 * &lt;netui:scriptContainer> tag.
 *
 * @example In this example the &lt;netui:scriptHeader/> tag has a parent
 * &lt;netui:html> tag.
 * <pre>    &lt;netui:html>
 *        &lt;head>
 *            &lt;title>Page Title&lt;/title>
 *            &lt;netui:scriptHeader/>
 *        &lt;/head>
 *        &lt;netui:body>
 *            ...
 * </pre>
 *
 * <p>In this example the &lt;netui:scriptHeader/> tag is enclosed in the
 * &lt;netui:scriptContainer> tag.
 * <pre>    &lt;netui:body>
 *        &lt;netui:scriptContainer>
 *            &lt;netui:scriptHeader/>
 *            &lt;netui:tree runAtClient="true"
 *                        dataSource="pageFlow.root"
 *                        selectionAction="postback"
 *                        tagId="testTree"/>
 *            ...
 *        &lt;/netui:scriptContainer>
 *    &lt;/netui:body></pre>
 * @netui:tag name="scriptHeader" body-content="empty"
 * description="ScriptHeader will write the &lt;script> and JavaScript includes."
 */
public class ScriptHeader extends AbstractClassicTag
{
    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "ScriptHeader";
    }

    /**
     * Process the start of the Button.
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        IScriptReporter sr = getScriptReporter();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);

        // write out the include
        ScriptTag.State state = new ScriptTag.State();
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.SCRIPT_TAG, pageContext.getRequest());

        state.src = req.getContextPath() + "/resources/beehive/version1/javascript/netui-tree.js";
        br.doStartTag(writer, state);
        br.doEndTag(writer);

        // write out the dynamic content
        ScriptRequestState srs = ScriptRequestState.getScriptRequestState(req);
        srs.writeFeature(sr, writer, CoreScriptFeature.DYNAMIC_INIT, true, false, new Object[]{req.getContextPath()});

        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
            throws JspException
    {
        localRelease();
        return EVAL_PAGE;
    }

    protected void localRelease()
    {
        super.localRelease();
    }
}
