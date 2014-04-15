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

import org.apache.beehive.netui.simpletags.core.Behavior;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.core.services.IdScopeStack;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.core.services.BehaviorStack;
import org.apache.beehive.netui.simpletags.html.HtmlConstants;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlControlState;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;

abstract public class HtmlBaseBehavior extends Behavior implements HtmlConstants
{
    /**
     * This method will return the state associated with the tag.  This is used by this
     * base class to access the individual state objects created by the tags.
     * @return a subclass of the <code>AbstractHtmlState</code> class.
     */
    abstract protected AbstractHtmlState getState();

    //***************************** The IHtmlCore properties *********************************/

    /**
     * Sets the style of the rendered html tag.
     * @param style the html style.
     * @jsptagref.attributedescription Specifies style information for the current element.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_style</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Specifies style information for the current element."
     */
    public void setStyle(String style)
    {
        if ("".equals(style))
            return;
        AbstractHtmlState tsh = getState();
        tsh.style = style;
    }

    /**
     * Sets the style class of the rendered html tag.
     * @param styleClass the html style class.
     * @jsptagref.attributedescription The style class (a style sheet selector).
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_styleClass</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The style class (a style sheet selector)."
     */
    public void setStyleClass(String styleClass)
    {
        if ("".equals(styleClass))
            return;
        AbstractHtmlState tsh = getState();
        tsh.styleClass = styleClass;
    }

    /**
     * Set the ID of the tag.
     * @param tagId the tagId. A value is required.
     * @jsptagref.attributedescription <p>String value. Sets the <code>id</code> (or <code>name</code>) attribute of the rendered HTML tag.
     * Note that the real id attribute rendered in the browser may be
     * changed by the application container (for example, Portal containers may change
     * the rendered id value to ensure the uniqueness of
     * id's on the page). In this case, the real id rendered
     * in the browser may be looked up
     * through the JavaScript function <code>lookupIdByTagId( tagId, tag )</code>.
     *
     * <p>For example, assume that some tag's <code>tagId</code> attribute is set to <code>foo</code>.
     *
     * <pre>    &lt;netui:textBox <b>tagId="foo"</b> /></pre>
     *
     * <p>Then the following JavaScript function will return the real id attribute rendered in the browser:
     *
     * <pre>    lookupIdByTagId( "foo", this )</pre>
     *
     * <p>To get a &lt;netui:form> element and all of its children elements in JavaScript, use
     * the same JavaScript function <code>lookupIdByTagId( tagId, tag )</code>.  For example,
     * assume that there is a &lt;netui:form> whose
     * tagId attribute is set to <code>bar</code>.
     *
     * <pre>    &lt;netui:form <b>tagId="bar"</b> ></pre>
     *
     * <p>Then the following JavaScript function will return the &lt;netui:form> element
     * and its children (packaged as an array).
     *
     * <pre>    document[lookupIdByTagId( "bar", this )]</pre>
     *
     * <p>To retreive the value entered into a &lt;netui:textBox> within the &lt;netui:form> tag, use the following
     * JavaScript expression.
     *
     * <pre>    document[lookupIdByTagId("bar", this)][lookupIdByTagId("foo", this)].value</pre>
     *
     * <p>The second parameter ensures that the JavaScript function
     * begins its search within the correct Portlet scope.  Pass the
     * JavaScript keyword <code>this</code> as the second parameter.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tagId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="String value. Sets the id (or name) attribute of the rendered HTML tag. "
     */
    public void setTagId(String tagId)
            throws JspException
    {
        // JSP 2.0 EL will convert a null into a empty string "".
        // If we get a "" we will display an error.
        AbstractHtmlState tsh = getState();
        tsh.id = tagId;
    }

    /**
     * Return the ID of the tag.  The id may be rewritten by the container (such
     * as a portal) to make sure it is unique.  JavaScript my lookup the actual id
     * of the element by looking it up in the <code>netui_names</code> table written
     * into the HTML.
     * @return the tagId.
     */
    public String getTagId()
    {
        AbstractHtmlState tsh = getState();
        return tsh.id;
    }

    /**
     * Sets the value of the title attribute.
     * @param title
     * @jsptagref.attributedescription The title.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_title</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The title. "
     */
    public void setTitle(String title)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TITLE, title);
    }

    /******************** the HtmlI18n properties ******************************************/
    /**
     * Sets the lang attribute for the HTML element.
     * @param lang
     * @jsptagref.attributedescription Sets the language code for the base language of an
     * element's attribute values and text content.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_lang</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Sets the language code for the base language of an element's attribute values and text content."
     */
    public void setLang(String lang)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_GENERAL, LANG, lang);
    }

    /**
     * Sets the dir attribute for the HTML element.
     * @param dir
     * @jsptagref.attributedescription Specifies the direction of text. (<code>LTR | RTL</code>)
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_dir</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Specifies the direction of text. (LTR | RTL)"
     */
    public void setDir(String dir)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_GENERAL, DIR, dir);
    }

    //******************* the HtmlEvent Properties  ****************************************
    /**
     * Gets the onClick javascript event.
     * @return the onClick event.
     */
    public String getOnClick()
    {
        AbstractHtmlState tsh = getState();
        return tsh.getAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK);
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
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK, onclick);
    }

    /**
     * Sets the onDblClick javascript event.
     * @param ondblclick the onDblClick event.
     * @jsptagref.attributedescription The onDblClick JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onDblClick</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onDblClick JavaScript event."
     */
    public void setOnDblClick(String ondblclick)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONDBLCLICK, ondblclick);
    }

    /**
     * Sets the onKeyDown javascript event.
     * @param onkeydown the onKeyDown event.
     * @jsptagref.attributedescription The onKeyDown JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onKeyDown</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onKeyDown JavaScript event."
     */
    public void setOnKeyDown(String onkeydown)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONKEYDOWN, onkeydown);
    }

    /**
     * Sets the onKeyPress javascript event.
     * @param onkeypress the onKeyPress event.
     * @jsptagref.attributedescription The onKeyPress JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onKeyPress</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onKeyPress JavaScript event."
     */
    public void setOnKeyPress(String onkeypress)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONKEYPRESS, onkeypress);
    }

    /**
     * Sets the onKeyUp javascript event.
     * @param onkeyup the onKeyUp event.
     * @jsptagref.attributedescription The onKeyUp JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onKeyUp</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onKeyUp JavaScript event."
     */
    public void setOnKeyUp(String onkeyup)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONKEYUP, onkeyup);
    }

    /**
     * Sets the onMouseDown javascript event.
     * @param onmousedown the onMouseDown event.
     * @jsptagref.attributedescription The onMouseDown JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseDown</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseDown JavaScript event."
     */
    public void setOnMouseDown(String onmousedown)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEDOWN, onmousedown);
    }

    /**
     * Sets the onMouseMove javascript event.
     * @param onmousemove the onMouseMove event.
     * @jsptagref.attributedescription The onMouseMove JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseMove</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseMove JavaScript event."
     */
    public void setOnMouseMove(String onmousemove)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEMOVE, onmousemove);
    }

    /**
     * Sets the onMouseOut javascript event.
     * @param onmouseout the onMouseOut event.
     * @jsptagref.attributedescription The onMouseOut JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOut</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseOut JavaScript event."
     */
    public void setOnMouseOut(String onmouseout)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEOUT, onmouseout);
    }

    /**
     * Sets the onMouseOver javascript event.
     * @param onmouseover the onMouseOver event.
     * @jsptagref.attributedescription The onMouseOver JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseOver</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseOver JavaScript event."
     */
    public void setOnMouseOver(String onmouseover)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEOVER, onmouseover);
    }

    /**
     * Sets the onMouseUp javascript event.
     * @param onmouseup the onMouseUp event.
     * @jsptagref.attributedescription The onMouseUp JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onMouseUp</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onMouseUp JavaScript event."
     */
    public void setOnMouseUp(String onmouseup)
    {
        AbstractHtmlState tsh = getState();
        tsh.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONMOUSEUP, onmouseup);
    }

    /**
     * Base support for the <code>attribute</code> tag.  This requires that the tag buffer their body and
     * write attribute in the end tag.  For the HTML tags it is not legal to set
     * the <code>id</code> or <code>name</code> attributes.  In addition, the base tag does
     * not allow facets to be set.  If the attribute is legal it will be added to the
     * general expression map stored in the <code>AbstractHtmlState</code> of the tag.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     */
    public void setAttribute(String name, String value, String facet)
    {
        if (facet != null) {
            String s = Bundle.getString("Tags_AttributeFacetNotSupported", new Object[]{facet});
            registerTagError(s, null);
        }
        AbstractHtmlState tsh = getState();
        setStateAttribute(name, value, tsh);
    }


    //******************* Helper Routines  *********************************/

    protected String getJavaScriptAttribute(String name)
    {
        AbstractHtmlState tsh = getState();
        return tsh.getAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, name);
    }

    /**
     * Attribute implementation.
     * @param name
     * @param value
     * @param tsh
     */
    protected void setStateAttribute(String name, String value, AbstractHtmlState tsh)
    {
        boolean error = false;

        // validate the name attribute, in the case of an error simply return.
        if (name == null || name.length() <= 0) {
            String s = Bundle.getString("Tags_AttributeNameNotSet");
            registerTagError(s, null);
            error = true;
        }

        // it's not legal to set the id or name attributes this way
        if (name != null && (name.equals(ID) || name.equals(NAME))) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
        }
        if (error)
            return;

        // if there is a style or class we will let them override the base
        if (name.equals(CLASS)) {
            tsh.styleClass = value;
            return;
        }
        else if (name.equals(STYLE)) {
            tsh.style = value;
            return;
        }
        tsh.registerAttribute(AbstractHtmlState.ATTR_GENERAL, name, value);
    }

    /**
     * Assumptions:
     * <ul>
     * <li>The state.name must be fully formed or the "real name" of the form.</li>
     * <li>The state.id is the tagId value set on the tag and <b>has not</b> be rewritten yet to form the "real id"</li>
     * </ul>
     * @param state
     * @ param parentForm
     */
    protected final void renderNameAndId(AbstractHtmlState state , FormBehavior parentForm)
    {
        if (state.id == null)
            return;

        // rewrite the id, save the original value so it can be used in maps
        String id = state.id;
        state.id = getIdForTagId(id);

        // check to see if this is an instance of a HTML Control
        boolean ctrlState = (state instanceof AbstractHtmlControlState);

        // form keeps track of this so that it can add this control to it's focus map
        if (parentForm != null && ctrlState) {
            AbstractHtmlControlState hcs = (AbstractHtmlControlState) state;
            if (hcs.name == null && parentForm.isFocusSet())
                hcs.name = state.id;
            parentForm.addTagID(state.id, ((AbstractHtmlControlState) state).name);
        }

       // map the tagId to the real id
        String name = null;
        if (ctrlState) {
            AbstractHtmlControlState cState = (AbstractHtmlControlState) state;
            name = cState.name;
        }

        TagContext tagCtxt = ContextUtils.getTagContext();
        ScriptReporter sr = tagCtxt.getScriptReporter();
        sr.addTagIdMappings(id, state.id, name);
    }

    /*
    protected void renderDefaultNameAndId(AbstractHtmlState state, String id, String name)
    {
        if (id == null)
            return;
        TagContext tagCtxt = ContextUtils.getTagContext();
        ScriptReporter sr = tagCtxt.getScriptReporter();
        sr.addTagIdMappings(id, state.id, name);
    }
    */

    /**
     * This method will find the ancestor form if there is one.  It will return null if
     * there is not form ancestor.
     * @return The nearest form ancestor or null if this form doesn't exist.
     */
    protected FormBehavior getNearestForm()
    {
        TagContext tagCtxt = ContextUtils.getTagContext();
        BehaviorStack stack = tagCtxt.getBehaviorStack();
        return (FormBehavior) stack.findAncestorWithClass(this,FormBehavior.class);
    }
}
