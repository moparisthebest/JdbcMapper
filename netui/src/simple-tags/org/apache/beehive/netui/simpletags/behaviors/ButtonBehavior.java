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

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.IHasPopupSupport;
import org.apache.beehive.netui.simpletags.core.IUrlParams;
import org.apache.beehive.netui.simpletags.core.PopupSupport;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.util.PageFlowTagUtils;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.html.HtmlUtils;
import org.apache.beehive.netui.simpletags.html.IHtmlAccessable;
import org.apache.beehive.netui.simpletags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.InputSubmitTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.ParamHelper;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.taglib.html.Constants;

import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ButtonBehavior extends HtmlFocusBaseBehavior
        implements IUrlParams, IHtmlAccessable, IHasPopupSupport
{
    private static final Logger logger = Logger.getInstance(ButtonBehavior.class);

    public static final String ACTION_OVERRIDE = "actionOverride:";

    private InputSubmitTag.State _state = new InputSubmitTag.State();

    private String _action;                      // The action which will override the action on the form
    private String _value;                       // The text of the button (this will override any body text).
    private Map _params;                         // Any parameters to the submit
    private String _targetScope;                 // Target page flow scope; see comments on setTargetScope()
    private PopupSupport _popupSupport = null;   // popup support, if the popup attribute is set to true
    private boolean _disableSecondClick = false; // When clicked, this button will disable itself before submitting.
    private boolean _buttonDisableAndSubmit = false;
    private boolean _buttonDisable = false;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Button";
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
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>type</code>
     * and <code>value</code> attributes.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     */
    public void setAttribute(String name, String value, String facet)
    {
        if (name != null && (name.equals(TYPE) || name.equals(VALUE))) {
            String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
            registerTagError(s, null);
        }
        super.setAttribute(name, value, facet);
    }

    /**
     * Set the name of the action for the Button.
     * @param action the name of the action to set for the Button.
     * @jsptagref.attributedescription The action method invoked.  The value of this attribute will override
     * the <code>action</code>
     * attribute of the parent &lt;netui:form> tag.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The action method invoked.  The value of this attribute will override
     * the action attribute of the parent <netui:form> tag."
     */
    public void setAction(String action)
    {
        _action = action;
    }

    /**
     * Set the target "scope" for the button's action.  Multiple active page flows may exist concurrently within named
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
        _targetScope = targetScope;
    }

    /**
     * Set the type of the Button (submit, button, or reset).
     * @param type the type of the Button.
     * @jsptagref.attributedescription The type of the button.  Possible values are <code>submit</code>, <code>button</code>, or <code>reset</code>.
     * The default value is <code>submit</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The type of the button.  Possible values are submit, button, or reset.
     * The default value is submit."
     */
    public void setType(String type)
            throws JspException
    {
        // Verify that this is a legal value
        if (INPUT_SUBMIT.equals(type) || INPUT_BUTTON.equals(type) || INPUT_RESET.equals(type)) {
            _state.type = type;
            return;
        }

        // illegal value report and error
        String s = Bundle.getString("Tags_ButtonTypeError", new Object[]{type});
        registerTagError(s, null);
    }

    /**
     * Set the value of the Button's text.
     * @param value the value of the Button's text.
     * @jsptagref.attributedescription The text displayed by the rendered HTML button.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_value</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The text displayed by the rendered HTML button."
     */
    public void setValue(String value)
    {
        _value = value;
    }

    /**
     * Sets the popup indicator.
     * @param popup whether or not the button should open a popup window.
     * @jsptagref.attributedescription Boolean.  If <code>popup</code> is set to true,
     * the button will open a popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_popup</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="If popup is set to true, the button will open a popup window."
     */
    public void setPopup(boolean popup)
    {
        _popupSupport = (popup) ? new PopupSupport() : null;
    }

    /**
     * When true, this button will disable itself before submitting.
     * @param disableSecondClick when true, this button will disable itself before submitting.
     * @jsptagref.attributedescription Boolean.  If <code>disableSecondClick</code> is set to true,
     * the button will disable itself before submitting.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_disableSecondClick</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="When true, this button will disable itself before submitting."
     */
    public void setDisableSecondClick(boolean disableSecondClick)
    {
        _disableSecondClick = disableSecondClick;
    }

    /**
     * Adds a URL parameter to the generated hyperlink.
     * @param name  the name of the parameter to be added.
     * @param value the value of the parameter to be added (a String or String[]).
     * @param facet
     */
    public void addParameter(String name, Object value, String facet)
    {
        assert(name != null) : "Parameter 'name' must not be null";

        if (_params == null) {
            _params = new HashMap();
        }
        ParamHelper.addParam(_params, name, value);
    }

    //******************* Lifecycle Methods ************************************

    public void preRender()
    {
        super.preRender();

        // Acquire the label value we will be generating
        if (_value == null)
            _value = Bundle.getString("Tags_ButtonText", null);
        _state.value = _value;

        // Generate an HTML element
        _state.disabled = isDisabled();

        // Add parameters for popup window support.
        if (_popupSupport != null) {
            _popupSupport.addParams(this);
        }

        // targetScope implies an extra parameter.  If there's no action on this button, get the action from the
        // nearest form, so we can construct an action url with our extra parameter.
        if (_targetScope != null && _action == null) {
            FormBehavior parentForm = getNearestForm();
            if (parentForm != null) {
                _action = parentForm.getAction();
            }
        }

        if (_action == null && _params != null && _popupSupport == null) {
            FormBehavior parentForm = getNearestForm();
            if (parentForm != null) {
                _action = parentForm.getAction();
            }
        }

        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        if (_action != null) {
            boolean isAction = PageFlowTagUtils.isAction(_action);
            if (isAction) {

                // If the action we're submitting to is checking for double-submits, save a token in the session.
                // This will be written out as a param (below), and will be checked in PageFlowRequestProcessor.
                String token = PageFlowTagUtils.getToken(_action);
                if (token != null) {
                    if (_params == null) {
                        _params = new HashMap();
                    }
                    _params.put(Constants.TOKEN_KEY, token);
                }

                // Add the scope ID parameter if there's one on the tag, or if there's one in the request.
                if (_targetScope != null) {
                    if (_params == null) {
                        _params = new HashMap();
                    }
                    _params.put(ScopedServletUtils.SCOPE_ID_PARAM, _targetScope);
                }

                String overrideAction = ACTION_OVERRIDE + _action;
                overrideAction = HtmlUtils.addParams(overrideAction, _params);
                String buttonOutput = URLRewriterService.getNamePrefix(pfCtxt.getServletContext(),
                        pfCtxt.getRequest(), overrideAction) + overrideAction;
                if (buttonOutput.indexOf(";") > -1) {
                    buttonOutput = buttonOutput.substring(0, buttonOutput.indexOf(";"));
                }
                _state.name = buttonOutput;

                // don't write the id attribute
                FormBehavior parentForm = getNearestForm();
                renderNameAndId(_state, parentForm);
            }
            else {
                // set the error because the action is invalid
                registerTagError(Bundle.getString("Tags_BadAction", _action), null);
            }
        }
        else {
            FormBehavior parentForm = getNearestForm();
            renderNameAndId(_state, parentForm);
        }

        // if the user overrides the onclick we will ignore this
        if (getOnClick() == null) {
            if (_disableSecondClick) {
                FormBehavior parentForm = getNearestForm();
                String href = getActionUrl();
                String entry;
                if (parentForm != null && href != null && (_state.type == null || _state.type == INPUT_SUBMIT)) {
                    entry = ScriptReporter.getString("buttonDisableAndSubmitFormAction",
                            new Object[]{parentForm.getRealFormId(), href});
                    _buttonDisableAndSubmit = true;
                }
                else {
                    entry = ScriptReporter.getString("buttonDisableAction", null);
                    _buttonDisable = true;
                }
                _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK, entry);
                if (parentForm != null)
                    parentForm.insureRealId();
            }
            else if (_popupSupport != null) {
                String href = getActionUrl();

                if (href != null) {
                    href = pfCtxt.getResponse().encodeURL(href);
                    setOnClick(_popupSupport.getOnClick(href));
                }
            }
        }
    }

    public void renderStart(Appender appender)
    {
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_SUBMIT_TAG);
        br.doStartTag(appender, _state);
    }

    public void renderEnd(Appender appender)
    {
        TagRenderingBase br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_SUBMIT_TAG);
        br.doEndTag(appender);

        //Emit javascript if this button needs to sumbit the form or open a popup window
        if (_popupSupport != null || _buttonDisable || _buttonDisableAndSubmit) {
            TagContext tagCtxt = ContextUtils.getTagContext();
            ScriptReporter sr = tagCtxt.getScriptReporter();

            if (_buttonDisableAndSubmit)
                sr.writeFeature(CoreScriptFeature.BUTTON_DISABLE_AND_SUBMIT, true, false, null);
            if (_buttonDisable)
                sr.writeFeature(CoreScriptFeature.BUTTON_DISABLE, true, false, null);
            if (_popupSupport != null)
                _popupSupport.writeScript(sr);
        }
    }

    // @todo: needs a comment
    private String getActionUrl()
    {
        String href = null;

        if (_action != null) {
            boolean forXML = TagRenderingBase.Factory.isXHTML();
            try {
                PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
                href = PageFlowUtils.getRewrittenActionURI(pfCtxt.getServletContext(), pfCtxt.getRequest(),
                        pfCtxt.getResponse(), _action, _params, null, forXML);
            }
            catch (URISyntaxException e) {
                // report the error...
                logger.error(Bundle.getString("Tags_URISyntaxException"));
                String s = Bundle.getString("Tags_Button_URLException",
                        new Object[]{_action, e.getMessage()});
                registerTagError(s, e);
            }
        }
        else {
            FormBehavior parentForm = getNearestForm();
            if (parentForm != null)
                href = HtmlUtils.addParams(parentForm.getActionUrl(), _params);
        }

        return href;
    }

    /* ==================================================================
     *
     * This tag's publically exposed HTML, CSS, and JavaScript attributes
     *
     * ==================================================================
     */

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
     * @netui:attribute required="false"  rtexprvalue="true" type="char"
     * description="The keyboard navigation key for the element.
     * The following values are not recommended because they
     * are often used by browsers: A, C, E, F, G,
     * H, V, left arrow, and right arrow"
     */
    public void setAccessKey(char accessKey)
    {
        if (accessKey == 0x00)
            return;
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ACCESSKEY, Character.toString(accessKey));
    }

    /**
     * Sets the alt attribute value.
     * @param alt the alt value.
     * @jsptagref.attributedescription The alt attribute of the element.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_alt</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The alt attribute of the element."
     */
    public void setAlt(String alt)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt);
    }

    /**
     * Sets the tabIndex of the rendered html tag.
     * @param tabindex the tab index.
     * @jsptagref.attributedescription The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_tabIndex</i>
     * @netui:attribute required="false" rtexprvalue="true" type="int"
     * description="The tabIndex of the rendered HTML tag.  This attribute determines the position of the
     * tag in the sequence of page elements that the user may advance through by pressing the TAB key."
     */
    public void setTabindex(int tabindex)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TABINDEX, Integer.toString(tabindex));
    }

    // @todo: write a comment
    public PopupSupport getPopupSupport()
    {
        return _popupSupport;
    }
}
