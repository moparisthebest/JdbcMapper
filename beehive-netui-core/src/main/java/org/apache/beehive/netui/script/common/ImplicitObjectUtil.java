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
package org.apache.beehive.netui.script.common;

import java.util.Map;
import java.util.Collections;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.beehive.netui.pageflow.FacesBackingBean;
import org.apache.beehive.netui.pageflow.GlobalApp;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.internal.AnyBeanActionForm;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.script.el.NetUIUpdateVariableResolver;
import org.apache.beehive.netui.script.el.NetUIReadVariableResolver;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * Utilities for loading NetUI implicit objects into various data binding scopes such as the
 * PageContext, ServletRequest, HttpSession, and ServletContext.
 */
public final class ImplicitObjectUtil {

    private static final Logger LOGGER = Logger.getInstance(ImplicitObjectUtil.class);

    private static final String PAGE_FLOW_IMPLICIT_OBJECT_KEY = "pageFlow";
    private static final String SHARED_FLOW_IMPLICIT_OBJECT_KEY = "sharedFlow";
    private static final String GLOBAL_APP_IMPLICIT_OBJECT_KEY = "globalApp";
    private static final String BUNDLE_IMPLICIT_OBJECT_KEY = "bundle";
    private static final String BACKING_IMPLICIT_OBJECT_KEY = "backing";
    private static final String PAGE_INPUT_IMPLICIT_OBJECT_KEY = "pageInput";
    private static final String ACTION_FORM_IMPLICIT_OBJECT_KEY = "actionForm";
    private static final String OUTPUT_FORM_BEAN_OBJECT_KEY = "outputFormBean";

    /* do not construct */
    private ImplicitObjectUtil() {}

    /**
     * Load the NetUI framework's implicit objects into the request.
     * @param request the request
     * @param response the response
     * @param servletContext the servlet context
     * @param currentPageFlow the current page flow
     */
    public static void loadImplicitObjects(HttpServletRequest request,
                                           HttpServletResponse response,
                                           ServletContext servletContext,
                                           PageFlowController currentPageFlow) {
        // @todo: add an interceptor chain here for putting implicit objects into the request
        loadPageFlow(request, currentPageFlow);

        // @todo: need to move bundleMap creation to a BundleMapFactory
        BundleMap bundleMap = new BundleMap(request, servletContext);
        loadBundleMap(request, bundleMap);
    }

    /**
     * Load the given <code>form</code> into the {@link JspContext} object.  Because the
     * framework supports any bean action forms, the type of the form is {@link Object}
     * @param jspContext the jsp context
     * @param form the form object
     */
    public static void loadActionForm(JspContext jspContext, Object form) {
        jspContext.setAttribute(ACTION_FORM_IMPLICIT_OBJECT_KEY, unwrapForm(form));
    }

    /**
     * Remove any action form present in the {@link JspContext}.
     * @param jspContext the jsp context
     */
    public static void unloadActionForm(JspContext jspContext) {
        jspContext.removeAttribute(ACTION_FORM_IMPLICIT_OBJECT_KEY);
    }

    /**
     * Load Page Flow related implicit objects into the request.  This method will set the
     * Page Flow itself and any available page inputs into the request.
     * @param request the request
     * @param pageFlow the current page flow
     */
    public static void loadPageFlow(ServletRequest request, PageFlowController pageFlow) {
        if(pageFlow != null)
            request.setAttribute(PAGE_FLOW_IMPLICIT_OBJECT_KEY, pageFlow);

        Map map = InternalUtils.getPageInputMap(request);
        request.setAttribute(PAGE_INPUT_IMPLICIT_OBJECT_KEY, map != null ? map : Collections.EMPTY_MAP);
    }

    /**
     * Load the JSF backing bean into the request.
     * @param request the request
     * @param facesBackingBean the JSF backing bean
     */
    public static void loadFacesBackingBean(ServletRequest request, FacesBackingBean facesBackingBean) {
        if(facesBackingBean != null)
            request.setAttribute(BACKING_IMPLICIT_OBJECT_KEY, facesBackingBean);
    }

    /**
     * Unload the JSF backing bean from the request
     * @param request the request
     */
    public static void unloadFacesBackingBean(ServletRequest request) {
        request.removeAttribute(BACKING_IMPLICIT_OBJECT_KEY);
    }

    /**
     * Load the shared flow into the request.
     * @param request the request
     * @param sharedFlows the current shared flows
     */
    public static void loadSharedFlow(ServletRequest request, Map/*<String, SharedFlowController>*/ sharedFlows) {
        if(sharedFlows != null)
            request.setAttribute(SHARED_FLOW_IMPLICIT_OBJECT_KEY, sharedFlows);
    }

    /**
     * Load the global app into the request
     * @param request the request
     * @param globalApp the global app
     */
    public static void loadGlobalApp(ServletRequest request, GlobalApp globalApp) {
        if(globalApp != null)
            request.setAttribute(GLOBAL_APP_IMPLICIT_OBJECT_KEY, globalApp);
    }

    /**
     * Load the resource bundle binding map into the request.
     * @param request the request
     * @param bundleMap the {@link java.util.Map} of resource bundles
     */
    public static void loadBundleMap(ServletRequest request, BundleMap bundleMap) {
        request.setAttribute(BUNDLE_IMPLICIT_OBJECT_KEY, bundleMap);
    }

    /**
     * Load the output form bean into the request.
     * @param request the request
     * @param bean the output form bean
     */
    public static void loadOutputFormBean(ServletRequest request, Object bean) {
        if(bean != null)
            request.setAttribute(OUTPUT_FORM_BEAN_OBJECT_KEY, bean);
    }

    /**
     * If applicable, unwrap the given <code>form</code> object to its native backing object.  If
     * the type of this form is a {@link AnyBeanActionForm}, the type returned will be the
     * native object backing the wrapper.
     * @param form the form
     * @return the unwrapped form
     */
    public static Object unwrapForm(Object form) {
        if(LOGGER.isDebugEnabled() && form instanceof AnyBeanActionForm)
            LOGGER.debug("using form of type: " + form.getClass().getName());

        if(form instanceof AnyBeanActionForm)
            return ((AnyBeanActionForm)form).getBean();
        else return form;
    }

    /**
     * Get the {@link Map} of shared flow objects from the request.
     *
     * @param request
     * @return the shared flows
     */
    public static Map/*<String, SharedFlowController>*/ getSharedFlow(ServletRequest request) {
        return (Map)request.getAttribute(SHARED_FLOW_IMPLICIT_OBJECT_KEY);
    }

    /**
     * Internal method!
     *
     * This method is used by the expression engine to get the current page flow.  If no page flow is
     * found, an exception will be thrown.
     * @param request the request
     * @param response the response
     * @return the page flow
     */
    public static PageFlowController getPageFlow(ServletRequest request, ServletResponse response) {
        assert request instanceof HttpServletRequest;

        PageFlowController jpf = PageFlowUtils.getCurrentPageFlow((HttpServletRequest)request);
        if(jpf != null)
            return jpf;
        else {
            String message = "There is no current Page Flow!";
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Internal method!
     *
     * This method is used by the expression engine to get the current global app.  If no global app
     * is found, an exception will be thrown.
     * @param request the request
     * @return the global app
     */
    public static GlobalApp getGlobalApp(ServletRequest request) {
        assert request instanceof HttpServletRequest;
        GlobalApp ga = PageFlowUtils.getGlobalApp((HttpServletRequest)request);
        if(ga == null) {
            String message = "There is no current GlobalApp!";
            LOGGER.error(message);
            throw new RuntimeException(message);
        }
        return ga;
    }

    /**
     * Internal method!
     *
     * Create a {@link VariableResolver} that contains the implicit objects available for expression
     * updates.
     */
    public static VariableResolver getUpdateVariableResolver(ServletRequest request,
                                                             ServletResponse response,
                                                             boolean isHandlingPost) {
        Object form = ImplicitObjectUtil.unwrapForm(getActionForm(request));
        return new NetUIUpdateVariableResolver(form, request, response, isHandlingPost);
    }

    /**
     * Internal method!
     *
     * Create a {@link VariableResolver} that contains the implicit objects available for expression
     * updates.
     */
    public static VariableResolver getUpdateVariableResolver(Object form,
                                                             ServletRequest request,
                                                             ServletResponse response,
                                                             boolean isHandlingPost) {
        Object realForm = ImplicitObjectUtil.unwrapForm(form);
        return new NetUIUpdateVariableResolver(realForm, request, response, isHandlingPost);
    }

    /**
     * Internal method!
     *
     * Create a {@link VariableResolver} that contains the implicit objects available for
     * expression reads.
     *
     * @param jspContext the jsp context
     * @return the variable resolver
     */
    public static VariableResolver getReadVariableResolver(JspContext jspContext) {
        assert jspContext != null;

        /* todo: ugly ugly ugly...getting the appropriate variable resolver should be easier than this */
        Object actionForm = getActionForm(((PageContext)jspContext).getRequest());

        NetUIReadVariableResolver netuiVariableResolver =
            new NetUIReadVariableResolver(jspContext.getVariableResolver());
        netuiVariableResolver.setActionForm(unwrapForm(actionForm));

        return netuiVariableResolver;
    }

    private static Object getActionForm(ServletRequest request) {
        return request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY);
    }
}
