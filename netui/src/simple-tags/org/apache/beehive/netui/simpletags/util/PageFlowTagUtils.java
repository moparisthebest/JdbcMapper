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
package org.apache.beehive.netui.simpletags.util;

import org.apache.beehive.netui.pageflow.FlowController;
import org.apache.beehive.netui.pageflow.PageFlowConstants;
import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.config.PageFlowActionMapping;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ActionConfig;
import org.apache.struts.util.TokenProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * This is a utility class for the beehive tags with routines for helping with URL rewriting.
 *
 * <p> Includes methods to create a fully-rewritten url based on an initial url with query
 * parameters and an anchor (location on page), checking if it needs to be secure and
 * rewriting. There's also a method to check if a url is an action. </p>
 */
public class PageFlowTagUtils
{
    /**
     * Create a fully-rewritten url from an initial action url with query parameters
     * and an anchor (location on page), checking if it needs to be secure then call
     * the rewriter service using a type of {@link org.apache.beehive.netui.core.urls.URLType#ACTION}.
     * @param action      the action url to rewrite.
     * @param params      the query parameters for this url.
     * @param location    the location (anchor or fragment) for this url.
     * @return a uri that has been run through the URL rewriter service.
     */
    public static String rewriteActionURL(String action, Map params, String location)
            throws URISyntaxException
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        boolean forXML = TagRenderingBase.Factory.isXHTML();
        if (action.length() > 0 && action.charAt(0) == '/')
            action = action.substring(1);
        return PageFlowUtils.getRewrittenActionURI(pfCtxt.getServletContext(), pfCtxt.getRequest(),
                pfCtxt.getResponse(), action, params, location, forXML);
    }

    /**
     * Create a fully-rewritten url from an initial href url with query parameters
     * and an anchor (location on page), checking if it needs to be secure then call
     * the rewriter service using a type of {@link org.apache.beehive.netui.core.urls.URLType#ACTION}.
     * @param url         the href url to rewrite.
     * @param params      the query parameters for this url.
     * @param location    the location (anchor or fragment) for this url.
     * @return a url that has been run through the URL rewriter service.
     * @see PageFlowUtils#getRewrittenHrefURI(javax.servlet.ServletContext, javax.servlet.http.HttpServletRequest,
     *              javax.servlet.http.HttpServletResponse, String, java.util.Map, String, boolean)
     */
    public static String rewriteHrefURL(String url, Map params, String location)
            throws URISyntaxException
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        boolean forXML = TagRenderingBase.Factory.isXHTML();
        return PageFlowUtils.getRewrittenHrefURI(pfCtxt.getServletContext(), pfCtxt.getRequest(),
                pfCtxt.getResponse(), url, params, location, forXML);
    }

    /**
     * Create a fully-rewritten url from an initial resource url with query parameters
     * and an anchor (location on page), checking if it needs to be secure then call
     * the rewriter service using a type of {@link org.apache.beehive.netui.core.urls.URLType#RESOURCE}.
     * @param url         the resource url to rewrite.
     * @param params      the query parameters for this url.
     * @param location    the location (anchor or fragment) for this url.
     * @return a url that has been run through the URL rewriter service.
     * @see PageFlowUtils#getRewrittenResourceURI(javax.servlet.ServletContext, javax.servlet.http.HttpServletRequest,
     *              javax.servlet.http.HttpServletResponse, String, java.util.Map, String, boolean)
     */
    public static String rewriteResourceURL(String url, Map params, String location)
            throws URISyntaxException
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        boolean forXML = TagRenderingBase.Factory.isXHTML();
        return PageFlowUtils.getRewrittenResourceURI(pfCtxt.getServletContext(), pfCtxt.getRequest(),
                pfCtxt.getResponse(), url, params, location, forXML);
    }

    /**
     * Determine whether a given URI is an Action.
     * @param action  the URI to check.
     * @return <code>true</code> if the action is defined in the current page flow
     *         or in a shared flow. Otherwise, return <code>false</code>.
     */
    public static boolean isAction(String action)
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        HttpServletRequest request = pfCtxt.getRequest();
        ServletContext servletCtxt = pfCtxt.getServletContext();
        FlowController flowController = PageFlowUtils.getCurrentPageFlow(request,servletCtxt);

        if (flowController != null) {
            if (action.endsWith(PageFlowConstants.ACTION_EXTENSION)) {
                action = action.substring(0, action.length() - PageFlowConstants.ACTION_EXTENSION.length());
            }

            if (getActionMapping(flowController, action) != null)
                return true;
            FlowController globalApp = PageFlowUtils.getSharedFlow(InternalConstants.GLOBALAPP_CLASSNAME, request, servletCtxt);
            return getActionMapping(globalApp, action) != null;
        }

        return true;
    }

    /**
     * Get or generate a token used to prevent double submits to an action.  The token is stored in the session,
     * and checked (and removed) when processing an action with the <code>preventDoubleSubmit</code> attribute
     * set to <code>true</code>.
     */
    public static String getToken(String action)
    {
        PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
        FlowController flowController = PageFlowUtils.getCurrentPageFlow(pfCtxt.getRequest(),pfCtxt.getServletContext());

        if (flowController != null) {
            MappingAndController mac = getActionMapping(flowController, action);
            if (mac != null)
                return getToken(mac.mapping);
        }

        return null;
    }

    /**
     * Get or generate a token used to prevent double submits to an action.  The token is stored in the session,
     * and checked (and removed) when processing an action with the <code>preventDoubleSubmit</code> attribute
     * set to <code>true</code>.
     */
    public static String getToken(ActionMapping mapping)
    {
        if (mapping instanceof PageFlowActionMapping && ((PageFlowActionMapping) mapping).isPreventDoubleSubmit()) {
            PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
            HttpServletRequest request = pfCtxt.getRequest();
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute(Globals.TRANSACTION_TOKEN_KEY);
            if (token != null)
                return token;
            token = TokenProcessor.getInstance().generateToken(request);
            session.setAttribute(Globals.TRANSACTION_TOKEN_KEY, token);
            return token;
        }

        return null;
    }

    public static class MappingAndController
    {
        public ActionMapping mapping;
        public FlowController controller;
    }

    public static MappingAndController getActionMapping(FlowController flowController, String action)
    {
        ActionConfig mapping = null;
        FlowController fc = null;

        if (flowController != null) {
            //
            // If there's a '.' delimiter, it's a shared flow action.
            //
            int dot = action.indexOf('.');
            PageFlowContext pfCtxt = ContextUtils.getPageFlowContext();
            HttpServletRequest request = pfCtxt.getRequest();
            ServletContext servletCtxt = pfCtxt.getServletContext();

            if (dot == -1) {
                //
                // It's an action in the current page flow, or in the (deprecated) Global.app.
                //
                if (action.charAt(0) != '/') action = '/' + action;
                mapping = flowController.getModuleConfig().findActionConfig(action);
                fc = flowController;
                
                //
                // If we don't find it in the current page flow, look in Global.app.
                //
                if (mapping == null) {
                    FlowController globalApp =
                            PageFlowUtils.getSharedFlow(InternalConstants.GLOBALAPP_CLASSNAME, request,servletCtxt);
                    if (globalApp != null) {
                        mapping = globalApp.getModuleConfig().findActionConfig(action);
                        fc = globalApp;
                    }
                }
            }
            else if (dot < action.length() - 1) {
                //
                // It's an action in a shared flow.
                //
                String sharedFlowName = action.substring(0, dot);
                if (sharedFlowName.length() > 0 && sharedFlowName.charAt(0) == '/') {
                    sharedFlowName = sharedFlowName.substring(1);
                }

                FlowController sharedFlow = (FlowController) PageFlowUtils.getSharedFlows(request).get(sharedFlowName);

                if (sharedFlow != null) {
                    String actionPath = '/' + action.substring(dot + 1);
                    mapping = sharedFlow.getModuleConfig().findActionConfig(actionPath);
                    fc = sharedFlow;
                }
            }
        }

        assert (mapping == null || mapping instanceof ActionMapping) : "Mapping not instance of ActionMapping:" +
                mapping.getClass().getName();

        if (mapping != null) {
            MappingAndController mac = new MappingAndController();
            mac.mapping = (ActionMapping) mapping;
            mac.controller = fc;
            return mac;
        }

        return null;
    }
}
