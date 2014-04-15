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

import org.apache.beehive.netui.core.urls.*;
import org.apache.beehive.netui.pageflow.FlowController;
import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.URIContextFactory;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.simpletags.util.PageFlowTagUtils;
import org.apache.beehive.netui.simpletags.core.services.ScriptReporter;
import org.apache.beehive.netui.simpletags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.simpletags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.simpletags.rendering.FormTag;
import org.apache.beehive.netui.simpletags.rendering.InputHiddenTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.simpletags.util.ContextUtils;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.ParamHelper;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.html.Constants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import java.net.URISyntaxException;
import java.util.*;


public class FormBehavior extends HtmlBaseBehavior
{
    private static final Logger logger = Logger.getInstance(FormBehavior.class);

    // unique name of the form
    private static String FORM_ID = "Netui_Form_";

    // The form state
    private FormTag.State _state = new FormTag.State();

    // state variable for any hidden values
    private InputHiddenTag.State _hiddenState = new InputHiddenTag.State();

    private String _focus = null;                  // The name of the field to receive focus, if any.
    private String _location = null;               // The location hash to append to the url.

    private String _beanName = null;               // The name of the form bean to (create and) use.
    private boolean _explicitBeanType = false;     // Flag indicating that the BeanType was set by setting the attribute
    private String _beanType = null;               // The type of the form bean to (create and) use.
    private String _beanScope = null;              // The scope of the form bean to (create and) use.

    private String _realName = null;               // This is the real name of the form if it is to be output
    private boolean _setRealName = false;          // Force the RealName to be output.  Contained tags can cause this

    private String _targetScope;                   // target page flow scope; see comments on setTargetScope()

    private ActionMapping _mapping = null;         // The ActionMapping defining where we will be submitting this form
    private ActionServlet _servlet = null;         // The ActionServlet instance we are associated with
    private HashMap _focusMap;                     // Map allowing setting focus on one of the controls
    private ModuleConfig _appConfig = null;        // The application configuration for our module.
    private FlowController _flowController = null; // The flow controller (page flow or shared flow).
    private boolean _formSubmit = false;
    private Map _params;
    private String _actionUrl;                     // The generated action URL.
    private LinkedHashMap/*<String, List<String>>*/ _extraHiddenParams = null; // hidden form fields based on URL parameters
    private PageContext _pageContext;
    private TagRenderingBase _br;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "Form";
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
     * Base support for the attribute tag.  This is overridden to prevent setting the <code>action</code>,
     * and <code>method</code> attributes.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     */
    public void setAttribute(String name, String value, String facet)
    {
        if (name != null) {
            if (name.equals(ACTION) || name.equals(METHOD)) {
                String s = Bundle.getString("Tags_AttributeMayNotBeSet", new Object[]{name});
                registerTagError(s, null);
            }
        }
        super.setAttribute(name, value, facet);
    }

    /**
     * Return the action of the Form.
     * @return a String representing the action name of the Form.
     */
    public String getAction()
    {
        return _state.action;
    }

    /**
     * Set the name of the action for the Form.
     * @param action the name of the action to set for the Form.
     * @jsptagref.attributedescription Required. The action method invoked on form submit.  Form data is passed to this method.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_action</i>
     * @netui:attribute required="true" rtexprvalue="true"
     * description="The action method invoked on form submit.  Form data is passed to this method."
     */
    public void setAction(String action)
    {
        _state.action = action;
    }

    /**
     * Set the content encoding to be used on a post submit.
     * @param enctype the content encoding type.
     * @jsptagref.attributedescription The content encoding to be used on a POST submit.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_enctype</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The content encoding to be used on a POST submit."
     */
    public void setEnctype(String enctype)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ENCTYPE, enctype);
    }

    /**
     * This method will return the real id that will be written out by the form.  This is
     * either the current state qualified tagId or the _realName if tagId is not specified.
     * @return the value of the id attribute written by the form.
     */
    public String getRealFormId()
    {
        return _realName;
    }

    /**
     * This method will insure that a real id is written out even if a tagId is not
     * set for the form.
     */
    public void insureRealId()
    {
        _setRealName = true;
    }

    /**
     * Set the name of the field to receive focus.
     * @param focus the focus field name.
     * @jsptagref.attributedescription The <code>tagID</code> of an input field which should receive initial focus.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_focus</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The tagID of an input field which should receive initial focus."
     */
    public void setFocus(String focus)
    {
        _focus = focus;
        insureRealId();
    }

    /**
     * Get the focus flag.
     * @return <code>true</code> if the focus has been set.  <code>false</code> otherwise.
     */
    public boolean isFocusSet() {
        return (_focus != null);
    }

    /**
     * Set the location hash to append to the url.
     * @param location the location hash
     * @jsptagref.attributedescription The location hash to append to the URL.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_location</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The location hash to append to the URL."
     */
    public void setLocation(String location)
    {
        _location = location;
    }

    /**
     * Return the value of the location.
     * @return  The value of the location.
     */
    public String getLocation() {
        return _location;
    }

    /**
     * Set the request method used when submitting this form.
     * @param method the request method
     * @jsptagref.attributedescription The request method used when submitting this form.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_method</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The request method used when submitting this form."
     */
    public void setMethod(String method)
    {
        if (FORM_POST.equals(method) || FORM_GET.equals(method)) {
            _state.method = method;
            return;
        }
        String s = Bundle.getString("Tags_FormMethodError", new Object[]{method});
        registerTagError(s, null);
    }

    /**
     * Set the attribute key under which our associated bean is stored.
     * @param name the attribute key name
     * @jsptagref.attributedescription The attribute key under which the associated Form Bean used to populate the input form is stored.
     * This Form Bean is found in the scope defined by the <code>scope</code> attribute.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The attribute key under which the associated Form Bean used to populate the input form is stored.
     * This Form Bean is found in the scope defined by the scope attribute."
     */
    public void setBeanName(String name)
    {
        // @todo: we need to verify what happens if this is null and type is set.
        _beanName = name;
    }

    /**
     * Set the target "scope" for the form's action.  Multiple active page flows may exist concurrently within named
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
     * Sets the onReset javascript event.
     * @param onReset the onReset event.
     * @jsptagref.attributedescription The JavaScript onReset event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onSubmit</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The JavaScript onReset event."
     */
    public void setOnReset(String onReset)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONRESET, onReset);
    }

    /**
     * Sets the onSubmit javascript event.
     * @param onSubmit the onReset event.
     * @jsptagref.attributedescription The JavaScript onSubmit event.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onSumbit</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The JavaScript onSubmit event."
     */
    public void setOnSubmit(String onSubmit)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_JAVASCRIPT, ONSUBMIT, onSubmit);
    }

    /**
     * Sets the scope (request or session) under which the associated bean
     * is stored.
     * @param scope the scope.
     * @jsptagref.attributedescription The scope (<code>request</code> or <code>session</code>) under which the associated Form Bean
     * used to populate the form input fields is stored.
     * Using the <code>name</code>, <code>type</code>  and <code>scope</code> attributes defines
     * the Form Bean used.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_scope</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The scope (request or session) under which the associated Form Bean
     * used to populate the form input fields is stored.
     * Using the name, type  and scope attributes defines
     * the Form Bean used."
     */
    public void setBeanScope(String scope)
    {
        _beanScope = scope;
    }

    /**
     * Sets the window target.
     * @param target the window target.
     * @jsptagref.attributedescription The window target
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_windowTarget</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The window target"
     */
    public void setTarget(String target)
    {
        _state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, TARGET, target);
    }

    /**
     * Sets the Java class name of the bean to be created, if necessary.
     * @param type the class name
     * @jsptagref.attributedescription The Java class name of the Form Bean to be created, if necessary.
     * This Form Bean will be created if the <code>name</code> and <code>scope</code> attributes are set.
     * The Form Bean is then used to populate the form input fields.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_type</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The Java class name of the Form Bean to be created, if necessary.
     * This Form Bean will be created if the name and scope attributes are set.
     * The Form Bean is then used to populate the form input fields."
     */
    public void setBeanType(String type)
    {
        _beanType = type;
        _explicitBeanType = true;
    }

    /**
     * Generate the JavaScript which will submit a form.  This is usually generated if the
     * form contains anchors that are used to submit the form.  This will make sure the JavaScript is
     * always generated.
     * @param formSubmit boolean value indicating that the form submit JavaScript should be generated.  Default is false.
     * @jsptagref.attributedescription Generate the form submit JavaScript even if the form does not
     * contain anchors. Default is <code>false</code>.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>boolean_formSubmit</i>
     * @netui:attribute required="false" rtexprvalue="true" type="boolean"
     * description="Generate the form submit JavaScript even if the form does not contain anchors."
     */
    public void setGenJavaScriptFormSubmit(boolean formSubmit)
    {
        _formSubmit = formSubmit;
        _setRealName = true;
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

    /**
     * Adds a tagId and name to the Form's focusMap.
     * @param tagID the tagID of a child tag.
     * @param name  the name of a child tag.
     */
    public void addTagID(String tagID, String name)
    {
        if (_focusMap == null) {
            _focusMap = new HashMap();
        }
        _focusMap.put(tagID, name);
    }

    /**
     * Set the PageContext of the tag...
     * @param pageContext
     */
    public void setPageContext(PageContext pageContext)
    {
        // @todo: should we actually be doing this into the request?
        _pageContext = pageContext;
    }

    //******************* Lifecycle Methods ************************************

    public void preRender()
    {
        super.preRender();

        if (hasErrors())
            return;

        // Error out if there is a parent form
        if (getNearestForm() != null) {
            registerTagError(Bundle.getString("Tags_FormParentForm"), null);
        }
        // find the beanName, beanType and beanScope
        lookupBeanScopeAndName();
        if (hasErrors())
            return;

        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        HttpServletRequest request = pfCtxt.getRequest();
        HttpSession session = request.getSession();
        ServletContext servCtxt = pfCtxt.getServletContext();
        TagContext tagCtxt = ContextUtils.getTagContext();


        // Generate the real name.  We generate it here so we can return it to any contained tags if
        // they need it.  If they access it, the most likely will cause the realName to be output
        if (_state.id != null) {
            _realName = getIdForTagId(_state.id);
        }
        else {
            String formId = FORM_ID + tagCtxt.getNextId();
            _realName = getIdForTagId(formId);
        }

        // Store this tag itself as a page attribute
        request.setAttribute(Constants.FORM_KEY, this);

        // Locate or create the bean associated with our form
        int scope = PageContext.SESSION_SCOPE;
        if ("request".equals(_beanScope)) {
            scope = PageContext.REQUEST_SCOPE;
        }

        // setup the Bean
        Object bean = null;
        if (_beanName != null) {
            if (scope == PageContext.REQUEST_SCOPE)
                bean = request.getAttribute(_beanName);
            else if (scope == PageContext.SESSION_SCOPE)
                bean = session.getAttribute(_beanName);
            else
                assert (false) : "Invalid scope:" + scope;
        }
        if (bean == null) {
            if (_explicitBeanType) {
                // Backwards compatibility - use explicitly specified values
                try {
                    Handlers handlers = Handlers.get(servCtxt);
                    bean = handlers.getReloadableClassHandler().newInstance(_beanType);
                    if (bean != null) {
                        ((ActionForm) bean).setServlet(_servlet);
                    }
                }
                catch (Exception e) {
                    registerTagError(Bundle.getString("Tags_FormNameBadType"), e);
                }
            }
            else {
                // New and improved - use the values from the action mapping

                // First, check to see if this is a page flow-scoped form bean.  If so, use the current value
                // from the member field in the page flow (or shared flow).
                if (_flowController != null) {
                    bean = _flowController.getFormBean(_mapping);
                }

                if (bean == null) {
                    bean = InternalUtils.createActionForm(_mapping, _appConfig, _servlet, servCtxt);
                }
            }

            if (hasErrors())
                return;

            // Call the reset method if we have an ActionForm
            if (bean instanceof ActionForm) {
                ((ActionForm) bean).reset(_mapping, request);
            }
            if (bean != null) {
                if (scope == PageContext.REQUEST_SCOPE)
                    request.setAttribute(_beanName, bean);
                else if (scope == PageContext.SESSION_SCOPE)
                    session.setAttribute(_beanName, bean);
                else
                    assert (false) : "Invalid scope [setAttribute]:" + scope;
            }
        }
        if (bean != null) {
            request.setAttribute(Constants.BEAN_KEY, bean);
            // @todo: what should we do about this?  In some ways it seems like this should be in the request
            assert(_pageContext != null) : "The PageContext was not set for this Form meaning it cannot be used as an implict object";
            ImplicitObjectUtil.loadActionForm(_pageContext, bean);
        }

        // Create the action URL here, so child tags can access it.
        try {
            HttpServletResponse response = pfCtxt.getResponse();
            _extraHiddenParams = new LinkedHashMap/*<String, List<String>>*/();
            _actionUrl = rewriteActionURL(servCtxt, request, response, _extraHiddenParams);
        }
        catch (URISyntaxException e) {
            // report the error...
            logger.error(Bundle.getString("Tags_URISyntaxException"));
            String s = Bundle.getString("Tags_Form_URLException",
                    new Object[]{_state.action, e.getMessage()});
            registerTagError(s, e);
        }
    }

    public void renderStart(Appender appender)
    {
        if (hasErrors()) {
            reportErrors(appender);
            return;
        }

        // if we have an Id or a tag is forcing the name, then set the state and generate
        // the JavaScript
        if (_state.id != null || _setRealName) {
            String id = _state.id;
            _state.name = _realName;
            _state.id = _realName;
            renderNameAndId(id);
        }

        if (_state.method == null)
            _state.method = FORM_POST;

        // Encode the action
        // If the rewritten form action contains request parameters, turn them into hidden fields --
        // it's not legal to include them in the action URI on a GET.
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        HttpServletRequest request = pfCtxt.getRequest();
        HttpServletResponse response = pfCtxt.getResponse();
        ServletContext servletCtxt = pfCtxt.getServletContext();
        HttpSession session = request.getSession();

        if (_actionUrl != null) {
            _state.action = response.encodeURL(_actionUrl);
        }

        _br = TagRenderingBase.Factory.getRendering(TagRenderingBase.FORM_TAG);
        _br.doStartTag(appender, _state);

        // If the action we're submitting to is checking for double-submits, save a token in the session.
        // This will be written out as a hidden param (below), and will be checked in PageFlowRequestProcessor.
        String token = PageFlowTagUtils.getToken(_mapping);

        // Add a transaction token (if present in our session)
        if (session != null) {
            if (token == null) {
                token = (String) session.getAttribute(Globals.TRANSACTION_TOKEN_KEY);
            }
            if (token != null) {
                String name = URLRewriterService.getNamePrefix(servletCtxt, request, Constants.TOKEN_KEY)
                              + Constants.TOKEN_KEY;
                writeHiddenParam(name, token, appender, true);
            }
        }

        // add a hidden value for each parameter
        if (_params != null) {
            Iterator paramKeys = _params.keySet().iterator();
            while (paramKeys.hasNext()) {
                Object paramKey = paramKeys.next();
                Object paramValue = _params.get(paramKey);
                if (paramValue instanceof String[]) {
                    String[] paramValArray = (String[]) paramValue;
                    for (int i = 0; i < paramValArray.length; i++) {
                        String name = paramKey.toString();
                        String paramName = URLRewriterService.getNamePrefix(servletCtxt, request, name) + name;
                        writeHiddenParam(paramName, paramValArray[i], appender, true);
                    }
                }
                else {
                    String name = paramKey.toString();
                    String paramName = URLRewriterService.getNamePrefix(servletCtxt, request, name) + name;
                    writeHiddenParam(paramName, paramValue.toString(), appender, true);
                }
            }
        }

        // add the extra hidden parameters
        if (_extraHiddenParams != null && _extraHiddenParams.size() > 0) {
            for (Iterator i = _extraHiddenParams.keySet().iterator(); i.hasNext();) {
                String name = (String) i.next();
                for (Iterator j = ((List)_extraHiddenParams.get(name)).iterator(); j.hasNext();) {
                    String value = (String) j.next();
                    writeHiddenParam(name, value, appender, true);
                }
            }
        }
    }

    public void renderEnd(Appender appender)
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        HttpServletRequest request = pfCtxt.getRequest();

        // Remove the page scope attributes we created
        request.removeAttribute(Constants.BEAN_KEY);
        request.removeAttribute(Constants.FORM_KEY);
        ImplicitObjectUtil.unloadActionForm(_pageContext);

        // Render a tag representing the end of our current form
        _br.doEndTag(appender);

        TagContext tagCtxt = ContextUtils.getTagContext();
        ScriptReporter scriptReporter = tagCtxt.getScriptReporter();

        // Render JavaScript to set the input focus if required
        if ((_focus != null) && (_focusMap != null)) {
            String focusName = (String) _focusMap.get(_focus);

            if (focusName != null) {
                String formName = _realName;
                scriptReporter.writeFeature(CoreScriptFeature.SET_FOCUS, false, true,
                        new Object[]{formName, focusName});
            }
        }

        if (_formSubmit) {
            scriptReporter.writeFeature(CoreScriptFeature.ANCHOR_SUBMIT, true, false, null);
        }
    }

    /**
     * This method registers the tagId, realId and name with the JavaScript services
     * been set.  The name and real id are both set in the state variable.  The
     * <code>id</code> value passed is the tagId set on the Behavior.
     * @param id The tagId value set on the Behavior, may be null
     */
    private void renderNameAndId(String id)
    {
        // if id is not set then there is nothing to do
        if (id == null)
            return;

        TagContext tagCtxt = ContextUtils.getTagContext();
        ScriptReporter scriptReporter = tagCtxt.getScriptReporter();
        scriptReporter.addTagIdMappings(id, _state.id, _state.name);
    }


    /**
     * Write a hidden field for a paramter
     * @param paramName  The name of the parameter
     * @param paramValue The value of the paramter
     * @param results    The InternalStringBuilder to append the result to
     */
    private void writeHiddenParam(String paramName, String paramValue, Appender results, boolean newLine)
    {
        // put each hidden on a new line
        if (newLine)
            results.append("\n");

        // create the state
        _hiddenState.clear();
        _hiddenState.name = paramName;
        _hiddenState.value = paramValue;

        TagRenderingBase hiddenTag = TagRenderingBase.Factory.getRendering(TagRenderingBase.INPUT_HIDDEN_TAG);
        hiddenTag.doStartTag(results, _hiddenState);
        hiddenTag.doEndTag(results);
    }

    /*
     * We have this local method because the Form may have the condition to manipulate the
     * URL and write out action parameters as hidden fields.
     *
     * This method will rewrite the URL via the rewriter service, then if needed, pull off
     * extra parameters from the rewritten URL to be turned into hidden fields, and finally
     * runs the modified URL through the rewriter service's templated URL formatting.
     */
    private String rewriteActionURL(ServletContext servletContext, HttpServletRequest request,
                                    HttpServletResponse response, LinkedHashMap/*<String, List<String>>*/ extraHiddenParams)
            throws URISyntaxException
    {
        String actionName = _state.action;
        if (actionName.length() > 0 && actionName.charAt(0) == '/') {
            actionName = actionName.substring(1);
        }
        MutableURI uri = PageFlowUtils.getActionURI(servletContext, request, response, actionName);
        uri.setFragment(_location);
        uri.setEncoding(response.getCharacterEncoding());

        boolean needsToBeSecure = PageFlowUtils.needsToBeSecure(servletContext, request, uri.getPath(), true);
        URLRewriterService.rewriteURL(servletContext, request, response, uri, URLType.ACTION, needsToBeSecure);

        // Add a scope-ID hidden input, if there's one on this tag, or one in the request.
        String targetScope = (_targetScope != null) ? _targetScope : request.getParameter(ScopedServletUtils.SCOPE_ID_PARAM);
        if (targetScope != null) {
            if (_params == null) {
                _params = new HashMap();
            }
            _params.put(ScopedServletUtils.SCOPE_ID_PARAM, targetScope);
            // If there's one on the URL, we're replacing it with a hidden param.
            if (uri != null) {
                assert uri instanceof FreezableMutableURI : uri.getClass().getName();
                ((FreezableMutableURI) uri).setFrozen(false);
                uri.removeParameter(ScopedServletUtils.SCOPE_ID_PARAM);
            }
        }

        // Check if the rewritten form action contains request parameters that need
        // to be turned into hidden fields -- shouldn't include them in the action
        // URI on a GET.
        boolean forXML = false;
        if (_state.method != null && _state.method.equalsIgnoreCase(FORM_GET)
                && !URLRewriterService.allowParamsOnFormAction(servletContext, request)) {
            extraHiddenParams.putAll(uri.getParameters());
        }
        else {
            // Params are allowed on the form action so see if this is for XHTML
            forXML = TagRenderingBase.Factory.isXHTML();
        }

        String key = PageFlowUtils.getURLTemplateKey(URLType.ACTION, needsToBeSecure);
        URIContext uriContext = URIContextFactory.getInstance(forXML);
        return URLRewriterService.getTemplatedURL(servletContext, request, uri, key, uriContext);
    }

    /**
     * Get the generated action URL, based on the action attribute.
     */
    String getActionUrl()
    {
        return _actionUrl;
    }

    /**
     * Look up values for the <code>name</code>, <code>scope</code>, and
     * <code>type</code> properties if necessary.
     */
    private void lookupBeanScopeAndName()
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        HttpServletRequest request = pfCtxt.getRequest();
        ServletContext servletContext = pfCtxt.getServletContext();

        // Look up the application module configuration information we need
        _appConfig = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        if (_appConfig == null) { // Backwards compatibility hack
            _appConfig = (ModuleConfig) servletContext.getAttribute(Globals.MODULE_KEY);
        }
        if (_appConfig == null) {
            registerTagError(Bundle.getString("Tags_FormNoApplicationConfig"), null);
            return;
        }
        _servlet = (ActionServlet) servletContext.getAttribute(Globals.ACTION_SERVLET_KEY);
        _flowController = PageFlowUtils.getCurrentPageFlow(request, servletContext);

        // check to see if this is a bad action

        PageFlowTagUtils.MappingAndController mac = PageFlowTagUtils.getActionMapping(_flowController, _state.action);
        if (mac == null) {
            FlowController globalApp = PageFlowUtils.getSharedFlow(InternalConstants.GLOBALAPP_CLASSNAME, request, servletContext);
            mac = PageFlowTagUtils.getActionMapping(globalApp, _state.action);
        }
        if (mac == null) {
            registerTagError(Bundle.getString("Tags_BadAction", _state.action), null);
            return;
        }

        _flowController = mac.controller;
        _mapping = mac.mapping;
        assert (_mapping != null) : "Mapping not found for controller";
        _appConfig = _mapping.getModuleConfig();

        // Were the required values already specified?
        if (_beanName != null) {
            if (!_explicitBeanType) {
                registerTagError(Bundle.getString("Tags_FormNameNoType"), null);
                return;
            }
            if (_beanScope == null)
                _beanScope = "session";
            return;
        }

        // Look up the form bean definition
        FormBeanConfig formBeanConfig = _appConfig.findFormBeanConfig(_mapping.getName());
        if (formBeanConfig == null) {
            // clear any _beanType
            _beanType = null;
        }
        else {
            // Calculate the required values
            _beanName = _mapping.getAttribute();
            _beanScope = _mapping.getScope();
            _beanType = formBeanConfig.getType();
        }
    }
}
