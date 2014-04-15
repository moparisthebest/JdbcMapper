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

import org.apache.beehive.netui.simpletags.behaviors.AnchorBaseBehavior;

import javax.servlet.jsp.JspException;

/**
 * This is the base class that provides most of the features necessary to create an anchor and an area. The Anchor
 * and Area tags are created as subclasses of this tag.  The Area tag is really a subset of the features, so certain
 * attributes are not defined here, even though the backing fields are defined here and the utility code knows them.
 * This may not be the best OO design, but the design is optimized for performance of rendering anchor elements.
 */
abstract public class AnchorBase extends HtmlBaseTag
{
    /**
     * Sets <code>shape</code> attribute for the area.
     * @param shape the window target.
     * @jsptagref.attributedescription The shape.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_shape</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The shape."
     */
    public void setShape(String shape)
    {
        ((AnchorBaseBehavior) _behavior).setShape(shape);
    }

    /**
     * Sets <code>coords</code> attribute for the area.
     * @param coords the window target.
     * @jsptagref.attributedescription The coordinates.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_coordinates</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The coordinates."
     */
    public void setCoords(String coords)
    {
        ((AnchorBaseBehavior) _behavior).setCoords(coords);
    }

    /**
     * Set the name of the action for the Area.
     * @param action the name of the action to set for the Area.
     * @jsptagref.attributedescription The action method to invoke.  The action method must be in the Controller file
     * of the Page Flow directory.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The action method to invoke.  The action method must be in the Controller file of the Page Flow directory."
     */
    public void setAction(String action)
            throws JspException
    {
        ((AnchorBaseBehavior) _behavior).setAction(action);
    }

    /**
     * Sets the href of the Anchor. This attribute will accept the empty String as a legal value.
     * @param href the hyperlink URI for the Area.
     * @jsptagref.attributedescription The URL to go to.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_href</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The URL to go to."
     * reftype="url"
     */
    public void setHref(String href)
            throws JspException
    {
        ((AnchorBaseBehavior) _behavior).setHref(href);
    }

    /**
     * Sets the anchor to be added to the end of the generated hyperlink.
     * @param location the name of the location anchor.
     * @jsptagref.attributedescription Location within the URI to visit.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_location</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="Location within the URI to visit."
     */
    public void setLocation(String location)
    {
        ((AnchorBaseBehavior) _behavior).setLocation(setNonEmptyValueAttribute(location));
    }

    /**
     * Set the target "scope" for the anchor's action.  Multiple active page flows may exist concurrently within named
     * scopes.  This attribute selects which named scope to use.  If omitted, the default scope is assumed.
     * @param targetScope the name of the target scope in which the associated action's page flow resides.
     * @jsptagref.attributedescription The target scope in which the associated action's page flow resides.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_targetScope</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The target scope in which the associated action's page flow resides"
     */
    public void setTargetScope(String targetScope)
    {
        ((AnchorBaseBehavior) _behavior).setTargetScope(targetScope);
    }

    /**
     * Sets the formSubmit indicator.
     * @param formSubmit whether or not the enclosing Form should be submitted.
     * @jsptagref.attributedescription Boolean.  If <code>formSubmit</code> is set to true, and the &lt;netui:anchor> tag
     * is within a &lt;netui:form> tag,
     * then the form data will be submitted to the method named in the
     * &lt;netui:form> tag's <code>action</code> attribute.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_formSubmit</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="If formSubmit is set to true, and the <netui:anchor> tag
     * is within a <netui:form> tag,
     * then the form data will be submitted to the method named in the
     * <netui:form> tag's action attribute."
     */
    public void setFormSubmit(boolean formSubmit)
    {
        ((AnchorBaseBehavior) _behavior).setFormSubmit(formSubmit);
    }

    /**
     * Sets the popup indicator.
     * @param popup whether or not the anchor should open a popup window.
     * @jsptagref.attributedescription Boolean.  If <code>popup</code> is set to true,
     * the anchor will open a popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_popup</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="If popup is set to true, the anchor will open a popup window.
     */
    public void setPopup(boolean popup)
    {
        ((AnchorBaseBehavior) _behavior).setPopup(popup);
    }


    /**
     * When true, this anchor will disable itself after being clicked.
     * @param disableSecondClick when true, this anchor will disable itself after being clicked.
     * @jsptagref.attributedescription Boolean.  If <code>disableSecondClick</code> is set to true,
     * the anchor will disable itself after being clicked.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_disableSecondClick</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="When true, this anchor will disable itself after being clicked."
     */
    public void setDisableSecondClick(boolean disableSecondClick)
    {
        ((AnchorBaseBehavior) _behavior).setDisableSecondClick(disableSecondClick);
    }


    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false"  rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * rendered HTML tag in the sequence of tags that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        ((AnchorBaseBehavior) _behavior).setTabindex(tabindex);
    }


    /**
     * Sets the accessKey attribute value.  This should key value of the
     * keyboard navigation key.  It is recommended not to use the following
     * values because there are often used by browsers <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>.
     * @param accessKey the accessKey value.
     * @jsptagref.attributedescription The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_accessKey</i>
     * @netui:attribute required="false" rtexprvalue="true"  type="char"
     * description=" The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow."
     */
    public void setAccessKey(char accessKey)
    {
        ((AnchorBaseBehavior) _behavior).setAccessKey(accessKey);
    }

    /**
     * Sets the onBlur javascript event.
     * @param onblur the onBlur event.
     * @jsptagref.attributedescription The onBlur JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onBlur</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onBlur JavaScript event."
     */
    public void setOnBlur(String onblur)
    {
        ((AnchorBaseBehavior) _behavior).setOnBlur(onblur);
    }

    /**
     * Sets the onFocus javascript event.
     * @param onfocus the onFocus event.
     * @jsptagref.attributedescription The onFocus JavaScript event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onFocus</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The onFocus JavaScript event."
     */
    public void setOnFocus(String onfocus)
    {
        ((AnchorBaseBehavior) _behavior).setOnFocus(onfocus);
    }
}
