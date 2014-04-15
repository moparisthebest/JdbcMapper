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
import org.apache.beehive.netui.simpletags.behaviors.AnchorBehavior;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.io.IOException;

/**
 * <p>
 * Generates a URL-encoded hyperlink to a specified URI.
 * Also adds support for URL re-writing and JavaScript-based form submission.
 * An anchor must have one of the following attributes to correctly create the hyperlink:
 * <ul>
 * <li>action - an action invoked by clicking the hyperlink.</li>
 * <li>href - a URL to go to</li>
 * <li>linkName - an internal place in the page to move to</li>
 * <li>clientAction - the action to run on the client</li>
 * <li>tagId - the ID of the tag</li>
 * <li>formSubmit - indicates whether or not the enclosing Form should be submitted</li>
 * </ul>
 * </p>
 * @jsptagref.tagdescription <p>
 * Generates an anchor that can link to another document or invoke an action method in the Controller file.
 * The &lt;netui:anchor> tag also supports JavaScript-based form submission and URL re-writing.
 * <p>
 * An anchor must have one of the following attributes to correctly create the hyperlink:
 * <blockquote>
 * <ul>
 * <li><code>action</code> - the action method invoked by clicking the hyperlink</li>
 * <li><code>href</code> - the URL to go to</li>
 * <li><code>linkName</code> - an internal place in the page to move to</li>
 * <li><code>clientAction</code> - the action to run on the client</li>
 * <li><code>tagId</code> - the ID of the tag</li>
 * <li><code>formSubmit</code> - indicates whether or not the enclosing Form should be submitted</li>
 * </ul>
 * </blockquote>
 * </p>
 * @example <b>Submitting Form Data</b>
 * <p>In this sample, clicking on this anchor submits the form data and invokes the method
 * <code>submitForm</code>.
 * <pre>
 *      &lt;netui:form action="formSubmit">
 *          Firstname:
 *          &lt;netui:textBox dataSource="actionForm.firstname"/>
 *          Lastname:
 *          &lt;netui:textBox dataSource="actionForm.lastname"/>
 *          &lt;netui:anchor formSubmit="true">Submit&lt;/netui:anchor>
 *      &lt;/netui:form></pre>
 * <p>If the <code>formSubmit</code> attribute is set to <code>true</code> and no
 * <code>onClick</code> attribute is set, the following JavaScript function will be written to the HTML page.
 * This JavaScript function will be referenced by the <code>onclick</code> attribute of the generated anchor tag.</p>
 * <pre>
 *  function anchor_submit_form(netuiName, newAction)
 *  {
 *    for (var i=0; i&lt;document.forms.length; i++) {
 *       if (document.forms[i].id == netuiName) {
 *          document.forms[i].method = "POST";
 *          document.forms[i].action = newAction;
 *          document.forms[i].submit();
 *       }
 *     }
 *  }</pre>
 * <p> The JavaScript function will be invoked by the generated HTML anchor tag as follows:
 * <pre>
 * &lt;a href="/WebApp/tagSamples/anchor/formSubmit.do"
 *       onClick='anchor_submit_form("Netui_Form_0","/WebApp/tagSamples/anchor/formSubmit.do");return false;'>Submit&lt;/a></pre>
 * <p>
 * <b>Custom JavaScript Functions</b>
 * </p>
 * <p>It is possible to write a custom <code>onClick</code> JavaScript event handler that would
 * do additional work, for example form validation, and still POST the form correctly.  To
 * accomplish this, add the custom JavaScript method to the page:
 * <pre>
 * function SubmitFromAnchor()
 * {
 *   // implement custom logic here
 *
 *   for(var i=0; i&lt;document.forms.length; i++)
 *   {
 *     // submit to the action /aWebapp/formPost.do
 *     if (document.forms[i].action == "/aWebapp/formPost.do")
 *     {
 *       document.forms[i].method="POST";
 *       document.forms[i].action="/aWebapp/formPost.do";
 *       document.forms[i].submit();
 *     }
 *   }
 * }</pre>
 * Then reference the JavaScript method from the &lt;netui:anchor> tag:
 * <pre>
 * &lt;netui:anchor formSubmit="true" onClick="SubmitFromAnchor(); return false;"&gt;Submit&lt;/netui:anchor&gt;</pre>
 * @netui:tag name="anchor" body-content="scriptless" dynamic-attributes="true" description="Generates a URL-encoded hyperlink to a specified URI."
 * @see java.lang.String
 */
public class Anchor extends AnchorBase implements DynamicAttributes
{
    public Anchor() {
        _behavior = new AnchorBehavior();
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
        ((AnchorBehavior) _behavior).setOnClick(setNonEmptyValueAttribute(onclick));
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
        ((AnchorBehavior) _behavior).setClientAction(setRequiredValueAttribute(action, "clientAction"));
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
        ((AnchorBehavior) _behavior).setLinkName(setRequiredValueAttribute(linkName, "linkName"));
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
        ((AnchorBehavior) _behavior).setCharSet(charSet);
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
        ((AnchorBehavior) _behavior).setType(type);
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
        ((AnchorBehavior) _behavior).setHrefLang(hreflang);
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
        ((AnchorBehavior) _behavior).setRel(rel);
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
        ((AnchorBehavior) _behavior).setRev(rev);
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
        ((AnchorBehavior) _behavior).setTarget(target);
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
        ((AnchorBehavior) _behavior).setValue(value);
    }

    /**
     * Prepare the hyperlink for rendering
     * @throws JspException if a JSP exception has occurred
     */
    public void doTag() throws JspException, IOException
    {
        _behavior.start();
        String value = getBufferBody(true);
        ((AnchorBehavior) _behavior).setValue(value);

        Appender appender = new ResponseAppender(getPageContext().getResponse());
        ((AnchorBehavior) _behavior).setText(value);
        _behavior.preRender();
        _behavior.renderStart(appender);
        _behavior.renderEnd(appender);
        _behavior.postRender();
        _behavior.terminate();
    }
}
