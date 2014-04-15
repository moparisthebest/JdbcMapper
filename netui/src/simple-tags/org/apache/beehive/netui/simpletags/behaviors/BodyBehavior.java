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
package org.apache.beehive.netui.simpletags.behaviors;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.ErrorReporter;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.BodyTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;

public class BodyBehavior extends HtmlBaseBehavior
{
    private BodyTag.State _state = new BodyTag.State();
    private TagRenderingBase _br;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Body";
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
     * Sets the onLoad javascript event.
     * @param onload the onLoad event.
     * @jsptagref.attributedescription The onLoad JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onLoad</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onLoad JavaScript event."
     */
    public void setOnLoad(String onload)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONLOAD, onload);
    }

    /**
     * Sets the onUnload javascript event.
     * @param onunload the onUnload event.
     * @jsptagref.attributedescription The onLoad JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onUnload</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onLoad JavaScript event."
     */
    public void setOnUnload(String onunload)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONUNLOAD, onunload);
    }

    /**
     * Sets the background image of the page.
     * @param background the background image of the page.
     * @jsptagref.attributedescription The background image of the page.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_background</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The background image of the page."
     */
    public void setBackground(String background)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BACKGROUND, background);
    }

    /**
     * Sets the bgcolor of the page.
     * @param bgcolor the background color of the page.
     * @jsptagref.attributedescription The background color of the page.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_bgcolor</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The background color of the page."
     */
    public void setBgcolor(String bgcolor)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BGCOLOR, bgcolor);
    }

    /**
     * Sets the foreground text color of the page.
     * @param text the foreground text color of the page.
     * @jsptagref.attributedescription The foreground text color of the page.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_text</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The foreground text color of the page."
     */
    public void setText(String text)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TEXT, text);
    }

    /**
     * Sets the the color of text marking unvisited hypertext links.
     * @param link the color of text marking unvisited hypertext links of the page.
     * @jsptagref.attributedescription The color of text marking unvisited hypertext links of the page
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_link</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The color of text marking unvisited hypertext links of the page."
     */
    public void setLink(String link)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, LINK, link);
    }

    /**
     * Sets the the color of text marking visited hypertext links.
     * @param vlink the color of text marking visited hypertext links of the page.
     * @jsptagref.attributedescription The color of text marking visited hypertext links of the page
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_vlink</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The color of text marking visited hypertext links of the page."
     */
    public void setVlink(String vlink)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, VLINK, vlink);
    }

    /**
     * Sets the color of text marking hypertext links when selected by the user.
     * @param alink the color of text marking hypertext links when selected by the user.
     * @jsptagref.attributedescription The color of text marking hypertext links when selected by the user
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_alink</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The color of text marking hypertext links when selected by the user."
     */
    public void setAlink(String alink)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALINK, alink);
    }

    //******************* Lifecycle Methods ************************************

    public void preRender()
    {
        super.preRender();

        // we assume that tagId will over have override id if both are defined.
        renderNameAndId(_state, null);
    }

    public void renderStart(Appender appender)
    {
        _br = TagRenderingBase.Factory.getRendering(TagRenderingBase.BODY_TAG);
        _br.doStartTag(appender, _state);
    }

    public void renderEnd(Appender appender)
    {
        // if there were errors lets report them.
        if (hasErrors()) {
            reportErrors(appender);
        }

        // Get the script reporter
        TagContext tagCtxt = ContextUtils.getTagContext();
        ScriptReporter sr = tagCtxt.getScriptReporter();
        sr.writeScript(appender);

        // write out any errors
        ErrorReporter er = tagCtxt.getErrorReporter();
        er.reportCollectedErrors(appender);

        _br.doEndTag(appender);
    }
}
