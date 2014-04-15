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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.tags.HtmlUtils;
import org.apache.beehive.netui.tags.IHtmlAccessable;
import org.apache.beehive.netui.tags.internal.PageFlowTagUtils;
import org.apache.beehive.netui.tags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.tags.javascript.IScriptReporter;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.*;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.ParamHelper;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.taglib.html.Constants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates a button on the page with the specified attributes.  The
 * &lt;netui:button> tag must be enclosed in &lt;netui:form...> ...
 * &lt;/netui:form> tags. You can specify the action that the form will
 * raise on the &lt;netui:form> tag.
 * @jsptagref.tagdescription Renders an &lt;input type="button"> tag with the specified attributes.
 * To submit data or invoke a method on the Controller file, the
 * &lt;netui:button> tag must have a parent {@link Form} tag.
 *
 * <p>The action attribute on the &lt;netui:button> is for the purpose
 * of overriding the action attribute on the enclosing &lt;netui:form> tag.
 * If no action attribute is specified on the &lt;netui:button> tag,
 * the action attribute on the &lt;netui:form> tag will determine which
 * action method is invoked.
 * </p>
 *
 * <p>This tag can also render a &lt;button> control to allow richer
 * rendering possibilities. When the <code>renderAsButton</code>
 * attribute is set to <code>true</code>, we render the markup with
 * an HTML &lt;button> tag, using the value attribute, and placing
 * the evaluated body content as the content between the begin and
 * end HTML button tags.
 * </p>
 *
 * <p>Please note that using the action override and rendering the
 * markup with a &lt;button> control will not work in Internet Explorer.
 * Internet Explorer includes the name and value of every &lt;button>
 * control in the HTML form as parameters in the request query,
 * regardless of whether the button has been clicked or not. The
 * action override attribute renders a name attribute on the HTML tag.
 * This name is used to identified by the NetUI request processor to
 * determine if an action method other than the one on the
 * &lt;netui:form> tag should be invoked. With IE, the name attribute
 * of the &lt;button> control will always be included in the parameters
 * or the request query and NetUI will assume an action override button
 * was clicked, leading to incorrect behavior. 
 * </p>
 * @example In this sample, the &lt;netui:button> submits data to
 * the Controller file's <code>processData</code> action method (specified on the &lt;netui:form>'s action
 * attribute).
 * <pre>
 *     &lt;netui:form action="processData">
 *        &lt;!--
 *        input elements here
 *        -->
 *        &lt;netui:button value="Submit" type="submit"/>
 *     &lt;/netui:form></pre>
 * @netui:tag name="button" description="Create a button on your JSP page."
 */
public class Button
        extends HtmlFocusBaseTag
        implements IUrlParams, IHtmlAccessable, IHasPopupSupport
{
    private static final Logger logger = Logger.getInstance(Button.class);

    public static final String ACTION_OVERRIDE = "actionOverride:";

    private InputSubmitTag.State _state = new ButtonTag.State();

    private String _action;                      // The action which will override the action on the form
    private String _value;                       // The text of the button (this will override any body text).
    private String _text;                        // The body content of this tag (if any).
    private Map _params;                         // Any parameters to the submit
    private String _targetScope;                 // Target page flow scope; see comments on setTargetScope()
    private PopupSupport _popupSupport = null;   // popup support, if the popup attribute is set to true
    private boolean _disableSecondClick = false; // When clicked, this button will disable itself before submitting.
    private boolean _renderAsButton = false;     // Write the HTML markup using the <button> element.

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
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    public void setAttribute(String name, String value, String facet)
            throws JspException
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
            throws JspException
    {
        _action = setRequiredValueAttribute(action, "action");
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
        if (INPUT_SUBMIT.equals(type) || INPUT_BUTTON.equals(type) || INPUT_RESET.equals(type)) {
            _state.type = type;
            return;
        }
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
            throws JspException
    {
        _value = setNonEmptyValueAttribute(value);
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
        _popupSupport = (popup ? new PopupSupport() : null);
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
     * When set to true, this tag will render the &lt;button> control in the HTML markup,
     * rather than the &lt;input type="..."> element. The evaluated content between
     * the start and end tags will be written out as the content between the rendered
     * HTML &lt;button> and &lt;/button>.
     *
     * <p>Please note that using an action override and rendering the
     * markup with a &lt;button> control will not work in Internet Explorer.
     * Internet Explorer includes the name and value of every &lt;button>
     * control in the HTML form as parameters in the request query,
     * regardless of whether the button has been clicked or not.
     * </p>
     * @param renderAsButton whether or not to render a &lt;button> rather than an &lt;input> tag.
     * @jsptagref.attributedescription Boolean.  If <code>renderAsButton</code> is set to true,
     * render a &lt;button> rather than an &lt;input> tag.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_renderAsButton</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="When true, render a &lt;button> rather than an &lt;input> tag."
     */
    public void setRenderAsButton(boolean renderAsButton)
    {
        _renderAsButton = renderAsButton;
    }

    /**
     * Adds a URL parameter to the generated hyperlink.
     * @param name  the name of the parameter to be added.
     * @param value the value of the parameter to be added (a String or String[]).
     * @param facet
     */
    public void addParameter(String name, Object value, String facet)
            throws JspException
    {
        assert(name != null) : "Parameter 'name' must not be null";

        if (_params == null) {
            _params = new HashMap();
        }
        ParamHelper.addParam(_params, name, value);
    }

    /**
     * Process the start of the Button.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag()
            throws JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Save the associated button label from the body content (if any).
     * By default, we render the HTML markup with an &lt;input> tag and
     * if the value attribute is null then we will use the body content
     * as the value attribute.
     *
     * <p>However, if the <code>renderAsButton</code> attribute
     * is set to true, then we render the HTML markup with an HTML
     * &lt;button> tag, using the value attribute, and placing the
     * evaluated body content as the content between the begin and
     * end HTML button tags.
     * </p>
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody()
            throws JspException
    {
        if (bodyContent != null) {
            String text = bodyContent.getString().trim();
            if (text.length() <= 0)
                text = null;

            if (text != null) {
                if (_renderAsButton) {
                    _text = text;
                }
                else {
                    if (_value == null) {
                        // use the <input> element with text as the value
                        _value = text;
                    }
                }
            }
            bodyContent.clearBody();
        }
        return SKIP_BODY;
    }

    /**
     * Render the button.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag()
            throws JspException
    {
        String idScript = null;

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        // Acquire the label value we will be generating
        if (_value == null)
            _value = Bundle.getString("Tags_ButtonText", null);
        _state.value = _value;

        // Generate an HTML element
        _state.disabled = isDisabled();

        // Add parameters for popup window support.
        if (_popupSupport != null) {
            _popupSupport.addParams(this, request);
        }

        // targetScope implies an extra parameter.  If there's no action on this button, get the action from the
        // nearest form, so we can construct an action url with our extra parameter.
        if (_targetScope != null && _action == null) {
            Form parentForm = getNearestForm();
            if (parentForm != null) {
                _action = parentForm.getAction();
            }
        }

        if (_action == null && _params != null && _popupSupport == null) {
            Form parentForm = getNearestForm();
            if (parentForm != null) {
                _action = parentForm.getAction();
            }
        }

        if (_action != null) {
            boolean isAction = PageFlowTagUtils.isAction(request, _action);
            if (isAction) {

                // If the action we're submitting to is checking for double-submits, save a token in the session.
                // This will be written out as a param (below), and will be checked in PageFlowRequestProcessor.
                String token = PageFlowTagUtils.getToken(request, _action);
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
                overrideAction = HtmlUtils.addParams(overrideAction, _params, response.getCharacterEncoding());
                String buttonOutput = URLRewriterService.getNamePrefix(pageContext.getServletContext(),
                        pageContext.getRequest(), overrideAction) + overrideAction;
                if (buttonOutput.indexOf(";") > -1) {
                    buttonOutput = buttonOutput.substring(0, buttonOutput.indexOf(";"));
                }
                _state.name = buttonOutput;

                // don't write the id attribute
                Form parentForm = getNearestForm();
                idScript = renderNameAndId(request, _state, parentForm);
            }
            else {
                // set the error because the action is invalid
                registerTagError(Bundle.getString("Tags_BadAction", _action), null);
            }
        }
        else {
            Form parentForm = getNearestForm();
            idScript = renderNameAndId(request, _state, parentForm);
        }

        boolean buttonDisableAndSubmit = false;
        boolean buttonDisable = false;
        
        // if the user overrides the onclick we will ignore this
        if (getOnClick() == null) {
            if (_disableSecondClick) {
                Form parentForm = getNearestForm();
                String href = getActionUrl(request, response);
                String entry;
                if (parentForm != null && href != null && (_state.type == null || _state.type == INPUT_SUBMIT)) {
                    entry = ScriptRequestState.getString("buttonDisableAndSubmitFormAction",
                            new Object[]{parentForm.getRealFormId(), href});
                    buttonDisableAndSubmit = true;
                }
                else {
                    entry = ScriptRequestState.getString("buttonDisableAction", null);
                    buttonDisable = true;
                }
                _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONCLICK, entry);
                if (parentForm != null)
                    parentForm.insureRealId();
            }
            else if (_popupSupport != null) {
                String href = getActionUrl(request, response);

                if (href != null) {
                    href = response.encodeURL(href);
                    setOnClick(_popupSupport.getOnClick(request,href));
                }
            }
        }
        
        // report any errors that may have been generated.
        if (hasErrors())
            return reportAndExit(EVAL_PAGE);

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        TagRenderingBase br = null;
        if (_renderAsButton) {
            br = TagRenderingBase.Factory.getRendering(TagRenderingBase.BUTTON_TAG, request);
            br.doStartTag(writer, _state);
            // add the body content
            if (_text != null)
                write(_text);
            else if (_value != null)
                write(_value);
        }
        else {
            br = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_SUBMIT_TAG, request);
            br.doStartTag(writer, _state);
        }
        br.doEndTag(writer);

        //Emit javascript if this button needs to sumbit the form or open a popup window
        if (idScript != null || _popupSupport != null || buttonDisable || buttonDisableAndSubmit) {
            ScriptRequestState srs = ScriptRequestState.getScriptRequestState(request);
            InternalStringBuilder script = new InternalStringBuilder(32);
            StringBuilderRenderAppender scriptWriter = new StringBuilderRenderAppender(script);
            IScriptReporter sr = getScriptReporter();

            if (buttonDisableAndSubmit)
                srs.writeFeature(sr, scriptWriter, CoreScriptFeature.BUTTON_DISABLE_AND_SUBMIT, true, false, null);
            if (buttonDisable)
                srs.writeFeature(sr, scriptWriter, CoreScriptFeature.BUTTON_DISABLE, true, false, null);
            if (_popupSupport != null)
                _popupSupport.writeScript(request, srs, getScriptReporter(), scriptWriter);
            if (idScript != null)
                scriptWriter.append(idScript);
            write(script.toString());
        }
        
        // Evaluate the remainder of this page
        localRelease();
        return EVAL_PAGE;
    }

    private String getActionUrl(HttpServletRequest request, HttpServletResponse response)
            throws JspException
    {
        String href = null;

        if (_action != null) {
            ServletContext servletContext = pageContext.getServletContext();
            boolean forXML = TagRenderingBase.Factory.isXHTML(request);
            try {
                href = PageFlowUtils.getRewrittenActionURI(servletContext, request, response, _action, _params, null, forXML);
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
            Form parentForm = getNearestForm();
            if (parentForm != null)
                href = HtmlUtils.addParams(parentForm.getActionUrl(), _params, response.getCharacterEncoding());
        }

        return href;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _state.clear();
        _action = null;
        _value = null;
        _text = null;
        _params = null;
        _targetScope = null;
        _popupSupport = null;
        _disableSecondClick = false;
        _renderAsButton = false;
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

    public PopupSupport getPopupSupport()
    {
        return _popupSupport;
    }
}
