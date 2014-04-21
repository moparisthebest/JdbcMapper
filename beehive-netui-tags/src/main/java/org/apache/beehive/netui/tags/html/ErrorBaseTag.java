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

import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.pageflow.config.PageFlowControllerConfig;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.InternalExpressionUtils;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import java.text.MessageFormat;
import java.util.Locale;

abstract public class ErrorBaseTag extends AbstractSimpleTag
{
    private static final Logger LOGGER = Logger.getInstance(ErrorBaseTag.class);

    /**
     * The default locale on our server.
     */
    protected static Locale defaultLocale = Locale.getDefault();

    /**
     * The name of the message bundle, as defined in the page flow's
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.MessageBundle} annotation,
     * where the error messages can be found. This defaults to org.apache.struts.action.Action.MESSAGES_KEY.
     */
    protected String _bundleName = null;

    /**
     * The session attribute key for the locale.
     * This defaults to org.apache.struts.action.Action.LOCALE_KEY.
     */
    protected String _locale = Globals.LOCALE_KEY;
    
    /**
     * The isResource method on ActionMessage isn't present in Struts 1.1.
     */
    private static boolean _isStruts11 = false;
    
    static
    {
        try
        {
            ActionMessage.class.getMethod( "isResource", null );
        }
        catch ( NoSuchMethodException e )
        {
            _isStruts11 = true;
        }
    }
    
    

    /**
     * Set the name of the message bundle, as defined in the page flow's
     * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.MessageBundle} annotation, where the error messages
     * can be found. If this attribute is not set, the page flow's default message bundle is used.
     * @param bundleName the bundle name
     * @jsptagref.attributedescription The name of the message bundle, as defined in the page flow's
     * Jpf.MessageBundle annotation. This defaults to org.apache.struts.action.Action.MESSAGES_KEY.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_bundleName</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The name of the message bundle, as defined in the page flow's Jpf.MessageBundle annotation."
     */
    public final void setBundleName(String bundleName)
            throws JspException
    {
        _bundleName = setRequiredValueAttribute(bundleName, "bundleName");
    }

    /**
     * Set the name of the locale attribute.
     * @param locale the locale attribute name
     * @jsptagref.attributedescription The name of the session attribute key for the user's locale.
     * This defaults to org.apache.struts.action.Action.LOCALE_KEY.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_locale</i>
     * @netui:attribute required="false"  rtexprvalue="true"
     * description="The name of the session attribute key for the user's locale"
     */
    public final void setLocale(String locale)
    {
        _locale = setNonEmptyValueAttribute(locale);
    }


    /**
     * @param report
     * @param bundleName
     * @return message
     * @throws JspException
     */
    protected String getErrorMessage(ActionMessage report, String bundleName)
            throws JspException
    {
        String key = report.getKey();
        Object[] messageArgs = report.getValues();
        PageContext pageContext = getPageContext();

        String message = null;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (key.length() == 0) {
            return "";
        }
        else if (! _isStruts11 && !report.isResource()) {
            //
            // This covers the case where the validator has already used MessageResources
            // with an alternate resource bundle to format the message before
            // creating the ActionMessage. Just return the already formatted message.
            //
            message = report.getKey();
        }
        else {
            ModuleConfig curModuleConfig = RequestUtils.getModuleConfig(pageContext);
            ServletContext servletContext = pageContext.getServletContext();
            
            // First, look in the message bundle for a shared flow that was involved in this request.
            String sharedFlowModulePath = InternalUtils.getForwardingModule(pageContext.getRequest());
            if (sharedFlowModulePath != null &&
                (curModuleConfig == null || !sharedFlowModulePath.equals(curModuleConfig.getPrefix()))) {
                ModuleConfig sfModule = InternalUtils.getModuleConfig(sharedFlowModulePath, servletContext);
                if (bundleName != null || !isMissingUserDefaultMessages(sfModule)) {
                    String msgAttr = (bundleName != null ? bundleName : Globals.MESSAGES_KEY) + sfModule.getPrefix();
                    MessageResources resources = (MessageResources) servletContext.getAttribute(msgAttr);
                    message = getMessage(resources, key, messageArgs, pageContext);
                }
            }
                
            // Next look in the default message bundle for the page flow.
            boolean missingUserDefaultMessages = isMissingUserDefaultMessages(pageContext);
            if (message == null && (bundleName != null || !missingUserDefaultMessages)) {
                MessageResources resources =
                        InternalUtils.getMessageResources(bundleName != null ? bundleName : Globals.MESSAGES_KEY, 
                                                          request, servletContext);
                message = getMessage(resources, key, messageArgs, pageContext);
            }
            
            // If we still didn't find it, try the default validation message bundle (in beehive-netui-pageflow.jar).
            if (message == null && bundleName == null) {
                MessageResources resources = InternalUtils.getMessageResources("_defaultMsgs", request, servletContext);
                message = getMessage(resources, key, messageArgs, pageContext);
            }
            
            //
            // We've run out of options -- the message simply doesn't exist.  If the user didn't specify a default
            // message bundle in the page flow, that's the problem; otherwise, it's simply a missing message.
            // Register a tag error for either case.
            //
            if (message == null) {
                if (bundleName == null && missingUserDefaultMessages) {
                    String s = Bundle.getString("Tags_ErrorsBundleMissing", key);
                    registerTagError(s, null);
                    return null;
                }
                else {
                    String s = Bundle.getString("Tags_ErrorsMessageMissing", key);
                    registerTagError(s, null);
                    return null;
                }
            }
        }

        return message;
    }

    private String getMessage(MessageResources resources, String key, Object[] messageArgs, PageContext pageContext)
    {
        if (resources != null) {
            Locale userLocale = RequestUtils.retrieveUserLocale( pageContext, _locale );
            if ( messageArgs == null ) {
                return resources.getMessage( userLocale, key );
            }
            else {
                return resources.getMessage( userLocale, key, messageArgs );
            }
        }
        
        return null;
    }
    
    /**
     * Tell whether the given Struts module has no default message bundle defined.
     * @return <code>true</code> if the given Struts module has no user-specified default message bundle.
     */
    protected static boolean isMissingUserDefaultMessages(PageContext pageContext)
    {
        return isMissingUserDefaultMessages(RequestUtils.getModuleConfig(pageContext))
               && pageContext.getRequest().getAttribute(Globals.MESSAGES_KEY) == null;
    }

    /**
     * Tell whether the given Struts module has no default message bundle defined.
     * @return <code>true</code> if the given Struts module has no user-specified default message bundle.
     */
    protected static boolean isMissingUserDefaultMessages(ModuleConfig mc)
    {
        if (mc != null) {
            ControllerConfig cc = mc.getControllerConfig();

            if (cc instanceof PageFlowControllerConfig) {
                return ((PageFlowControllerConfig) cc).isMissingDefaultMessages();
            }
        }

        return false;
    }
}
