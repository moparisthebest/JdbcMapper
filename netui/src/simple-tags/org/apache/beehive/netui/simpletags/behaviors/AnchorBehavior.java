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
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;

import javax.servlet.jsp.JspException;

public class AnchorBehavior extends AnchorBaseBehavior
{
    private String _text;         // The body content of this tag (if any).
    private String _value;

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "Anchor";
    }

    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    public AbstractHtmlState getState()
    {
        return _state;
    }

    /**
     * Sets the onClick javascript event.
     * @param onclick the onClick event.
     * @jsptagref.attributedescription The onClick JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onClick</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onClick JavaScript event."
     */
    public void setOnClick(String onclick)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK, onclick);
        // Jira 299
        //_state.onClick = setNonEmptyValueAttribute(onclick);
    }

    /**
     * Set a client action to run on the client.  When set on an anchor, a NetUI JavaScript action
     * will be run.  This attribute may not be set if <code>href</code> or <code>action</code> is set.
     * @param action an action to run on the client.
     * @jsptagref.attributedescription The action (NetUI JavaScript) to run on the client.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_clientAction</i>
     * @netui:attribute required="false" rtexprvalue="true" description="The client action."
     * description="The action (NetUI JavaScript) to run on the client."
     */
    public void setClientAction(String action)
            throws JspException
    {
        _clientAction = action;
    }

    /**
     * Sets the link name of the Anchor.  The link name is treated as a fragment
     * identifier and may or may not contain the "#" character.  If it does, the
     * link name will not be qualified into a ScriptContainer.  If the link name
     * does not contain the "#" the normal tagId qualification will take place
     * to produce the actual fragment identifier.
     * @param linkName the link name for the Anchor.
     * @jsptagref.attributedescription An internal place on the page to go to.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_linkName</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="An internal place on the page to go to."
     */
    public void setLinkName(String linkName)
            throws JspException
    {
        _linkName = linkName;
    }

    /**
     * Sets <code>charset</code> attribute for the anchor.
     * @param charSet the window target.
     * @jsptagref.attributedescription The character set.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_charset</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The character set."
     */
    public void setCharSet(String charSet)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, CHARSET, charSet);
    }

    /**
     * Sets <code>type</code> attribute for the anchor.
     * @param type the window target.
     * @jsptagref.attributedescription The type.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The type."
     */
    public void setType(String type)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TYPE, type);
    }

    /**
     * Sets <code>hreflang</code> attribute for the anchor.
     * @param hreflang the window target.
     * @jsptagref.attributedescription The HREF lang.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_hreflang</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The HREF lang."
     */
    public void setHrefLang(String hreflang)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, HREFLANG, hreflang);
    }

    /**
     * Sets <code>rel</code> attribute for the anchor.
     * @param rel the window target.
     * @jsptagref.attributedescription The relationship between the current document and the target Url.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_rel</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The rel."
     */
    public void setRel(String rel)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, REL, rel);
    }

    /**
     * Sets <code>rev</code> attribute for the anchor.
     * @param rev the window target.
     * @jsptagref.attributedescription Describes a reverse link from the anchor specified to the current document.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_rev</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The rev."
     */
    public void setRev(String rev)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, REV, rev);
    }


    /**
     * Sets the window target.
     * @param target the window target.
     * @jsptagref.attributedescription The window target.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The window target."
     */
    public void setTarget(String target)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TARGET, target);
    }

    /**
     * This will set the text of the anchor.  If there is body content, this
     * will override that value.
     * @param value the text of the anchor.
     * @jsptagref.attributedescription Set the text of the anchor, overriding the body content.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_value</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Set the text of the anchor overriding the body content"
     */
    public void setValue(String value)
    {
        _value = value;
    }

    /**
     * Set the text, this represents the body of the anchor tag.
     * @param text
     */
    public void setText(String text) {
        _text = text;
    }

    public void renderStart(Appender appender)
    {
    }

    public void renderEnd(Appender appender)
    {
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        if (_value != null) {
            _text = _value;
        }

        // build the anchor into the results
        TagRenderingBase trb = TagRenderingBase.Factory.getRendering(TagRenderingBase.ANCHOR_TAG);
        assert(trb != null) : "The TagRenderingBase returned is null";

        if (!createAnchorBeginTag(trb, appender, REQUIRED_ATTR)) {
            reportErrors(appender);
            return;
        }

        if (_text != null)
            appender.append(_text);

        trb.doEndTag(appender);
    }
}
