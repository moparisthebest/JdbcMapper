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

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 * This class will output a script block into the generated HTML.  The primary reason
 * for this class is to add the ability to add JavaScript either before or after the
 * framework provided JavaScript.
 * @jsptagref.tagdescription <p>Outputs a script block into the generated HTML.  The primary reason
 * for this tag is to add JavaScript either before or after the
 * framework provided JavaScript.</p>
 * @netui:tag name="scriptBlock" body-content="scriptless" description="Used to place JavaScript in relationship to the framework genrated JavaScript"
 */
public class ScriptBlock extends AbstractSimpleTag
{
    private ScriptPlacement _placement = ScriptPlacement.PLACE_INLINE;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Content";
    }

    /**
     * Place the JavaScript inside in relationship to the frameword generated JavaScript.
     * @param placement The placement of the JavaScript
     * @jsptagref.attributedescription String value 'after' or 'before'.  Places the JavaScript
     * before or after the JavaScript provided by the framework.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_or_expression_output</i>
     * @netui:attribute rtexprvalue="true"
     * description="The String literal or expression used to output the content."
     */
    public void setPlacement(String placement)
    {
        if (placement.equals("after"))
            _placement = ScriptPlacement.PLACE_AFTER;
        else if (placement.equals("before"))
            _placement = ScriptPlacement.PLACE_BEFORE;
        else
            _placement = ScriptPlacement.PLACE_INLINE;
    }

    /**
     * Render the content.
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public void doTag()
            throws JspException, IOException
    {
        // report any errors...
        if (hasErrors()) {
            reportErrors();
            return;
        }

        PageContext pageContext = getPageContext();
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        String script = getBufferBody(false);
        if (script == null)
            return;

        // make sure that the last character is a new line
        if (script.length() > 0 && script.charAt(script.length() -1) != '\n') {
            script = script + "\n";
        }

        IScriptReporter sr = getScriptReporter();

        // if we are writting the javaScript inline then do it....
        if (_placement == ScriptPlacement.PLACE_INLINE || sr == null) {
            ScriptRequestState.writeScriptBlock(pageContext.getRequest(), writer, script);
            return;
        }

        sr.addScriptFunction(_placement, script);
    }
}
