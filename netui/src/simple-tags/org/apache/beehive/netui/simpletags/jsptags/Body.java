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
package org.apache.beehive.netui.simpletags.jsptags;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.appender.ResponseAppender;
import org.apache.beehive.netui.simpletags.behaviors.BodyBehavior;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

/**
 * @jsptagref.tagdescription <p>
 * Renders an HTML &lt;body> tag with the attributes specified.
 * </p>
 * @netui:tag name="body" body-content="scriptless" dynamic-attributes="true" description="Output the &lt;body> container.  This tag allows the NetUI framework to output script and errors before the page is finished rendering."
 */
public class Body extends HtmlBaseTag
{
    public Body() {
        _behavior = new BodyBehavior();
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
        ((BodyBehavior) _behavior).setOnLoad(onload);
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
        ((BodyBehavior) _behavior).setOnUnload(onunload);
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
        ((BodyBehavior) _behavior).setBackground(background);
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
        ((BodyBehavior) _behavior).setBgcolor(bgcolor);
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
        ((BodyBehavior) _behavior).setText(text);
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
        ((BodyBehavior) _behavior).setLink(link);
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
        ((BodyBehavior) _behavior).setVlink(vlink);
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
        ((BodyBehavior) _behavior).setAlink(alink);
    }

    /**
     * Process the start of the Button.
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();

        ServletResponse response = getPageContext().getResponse();
        Appender appender = new ResponseAppender(response);
        _behavior.preRender();
        _behavior.renderStart(appender);

        JspFragment frag = getJspBody();
        if (frag != null) {
            frag.invoke(response.getWriter());
        }

        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
