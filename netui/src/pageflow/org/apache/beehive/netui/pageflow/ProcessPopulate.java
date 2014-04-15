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
package org.apache.beehive.netui.pageflow;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.beehive.netui.pageflow.internal.AnyBeanActionForm;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.script.Expression;
import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;
import org.apache.beehive.netui.script.ExpressionUpdateException;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;

/**
 * Implement the processPopulate stage of the Struts / PageFlow request
 * processing lifecycle.  The {@link #populate(HttpServletRequest, HttpServletResponse, ActionForm, boolean)} method is
 * invoked in order to take request parameters from the {@link HttpServletRequest}
 * use the key / value pairs from the request to perform an update to the underlying
 * JavaBean objects.
 * <br/>
 * <br/>
 * Updates are performed on a key / value pair if the key is an expression; otherwise,
 * the updates are delegated to the Struts processPopulate infrastructure.
 *
 */
public class ProcessPopulate {
    /**
     * This defines the name of the parameter that will contain the NetUI ID map..
     */
    public static final String IDMAP_PARAMETER_NAME = "netuiIdMap";

    private static final Logger LOG = Logger.getInstance(ProcessPopulate.class);

    private static final String TAG_HANDLER_PREFIX = "wlw-";
    private static final String TAG_HANDLER_SUFFIX = ":";
    private static final Map HANDLER_MAP = new HashMap();

    // these must be kept in sync with the context names specified in the scripting languages
    private static final String PAGE_FLOW_CONTEXT = "pageFlow";
    private static final String GLOBAL_APP_CONTEXT = "globalApp";

    /**
     * An inner class that represnts the data that will be used to
     * perform an update.  If a key has a prefix handler, this
     * node is constructed and passed to the prefix handler
     * so that the prefix handler can change the expression or
     * values that will be used to execute the expression update.
     */
    public final static class ExpressionUpdateNode {

        public String expression = null;
        public String[] values = null;

        /* Prevent construction outside of this class */
        private ExpressionUpdateNode() {}

        public String toString() {
            InternalStringBuilder buf = new InternalStringBuilder();
            buf.append("expression: ").append(expression).append("\n");
            if(values != null)
                for(int i = 0; i < values.length; i++)
                    buf.append("value[").append(i).append("]: ").append(values[i]);
            else buf.append("values are null");

            return buf.toString();
        }
    }

    /**
     * Register a {@link org.apache.beehive.netui.pageflow.RequestParameterHandler} that is added to handle a
     * particular prefix which be present as a prefix to a request parameter
     * key.  For keys that match the prefix, the key / value from the request
     * are put in an {@link ExpressionUpdateNode} struct and handed to the
     * {@link org.apache.beehive.netui.pageflow.RequestParameterHandler} for processing.  The returned {@link ExpressionUpdateNode}
     * is used to perform an expression update.
     *
     * @param prefix the String prefix that will be appended to request paramters that
     * should pass through the {@link RequestParameterHandler} before being updated.
     * @param handler the handler that should handle all request paramters with
     * the given <code>prefix</code>
     */
    public static void registerPrefixHandler(String prefix, RequestParameterHandler handler)
    {
        /*
        This synchronization point should happen very infrequently as this only happens when a prefix handler
        is added to the set.
        */
        synchronized(HANDLER_MAP) {
            String msg = "Register RequestParameterHandler with\n\tprefix: " + prefix + "\n\thandler: " + (handler != null ? handler.getClass().getName(): null);

            LOG.info(msg);

            if (HANDLER_MAP.get(prefix) == null)
                HANDLER_MAP.put(prefix, handler);
        }
    }

    /**
     * Write the handler name specified onto the given expression.
     */
    public static String writeHandlerName(String handler, String expression)
    {
        if(!ExpressionEvaluatorFactory.getInstance().isExpression(expression))
            throw new IllegalArgumentException(Bundle.getErrorString("ProcessPopulate_handler_nonAtomicExpression", new Object[] {expression}));

        if(!HANDLER_MAP.containsKey(handler))
            throw new IllegalStateException(Bundle.getErrorString("ProcessPopulate_handler_notRegistered", new Object[] {handler}));

        InternalStringBuilder buf = new InternalStringBuilder();
        buf.append(TAG_HANDLER_PREFIX);
        buf.append(handler);
        buf.append(TAG_HANDLER_SUFFIX);
        buf.append(expression);

        return buf.toString();
    }

    /**
     * Use the request parameters to populate all properties that have expression keys into
     * the underlying JavaBeans.
     * Creates a <code>java.util.Map</code> of objects that will be consumed by
     *         Struts processPopulate.  This includes all request attributes that
     *         were not expressions
     *
     * @param request the current <code>HttpServletRequest</code>
     * @param form if this request references an action and it has an <code>ActionForm</code>
     *             associated with it, then the <code>form</code> parameter is non-null.
     * @throws ServletException when an error occurs in populating data after
     *         the request; failure here can be caused by failures in creating
     *         or executing update expressions.
     */
    public static void populate(HttpServletRequest request,
                                HttpServletResponse response,
                                ActionForm form,
                                boolean requestHasPopulated)
        throws ServletException {
        String key = null;
        Map strutsProperties = null;
        ExpressionEvaluator ee = ExpressionEvaluatorFactory.getInstance();

        /* Boolean used to avoid instanceof check below */
        boolean isMultipart = false;

        // if this returns null, it's not a mulitpart request
        Map params = MultipartRequestUtils.handleMultipartRequest(request, form);

        // make adjustments
        if(params != null)
            isMultipart = true;
        else params = request.getParameterMap();

        if(params == null) {
            LOG.warn("An error occurred checking a request for multipart status.  No model values were updated.");
            return;
        }

        /* explicitly build a variable resolver that is used to provide objects that may be updated to the expression engine */
        VariableResolver variableResolver = ImplicitObjectUtil.getUpdateVariableResolver(form, request, response, true);

        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            key = (String)iterator.next();
            String expr = null;

            // if there is an expression map, lookup the real expression from the name
            expr = key;
            LOG.debug("key: " + key + " value type: " + params.get(key).getClass().getName() + " value: " + params.get(key));

            try {
                Object paramsValue = params.get(key);
                if (ee.containsExpression(expr)) {
                    Object updateValue = null;
                    if (!isMultipart || paramsValue instanceof String[]) {
                        String[] values = (String[]) paramsValue;

                        // the only "contains" case that is accepted
                        if (expr.startsWith(TAG_HANDLER_PREFIX)) {
                            LOG.debug("Found an expression requiring a TAG HANDLER");

                            ExpressionUpdateNode node = doTagHandler(key, expr, values, request);

                            expr = node.expression;
                            values = node.values;
                        }

                        if (values != null && values.length == 1)
                            updateValue = values[0];
                        else
                            updateValue = values;
                    }
                    // handle funky types that Struts returns for a file upload request handler
                    else {
                        updateValue = params.get(key);
                    }

                    try {
                        // trap any bad expressions here
                        if (ee.isExpression(expr)) {
                            // common case, make this fast
                            if (!requestHasPopulated)
                                ee.update(expr, updateValue, variableResolver, true);
                            // must check the expression to make sure pageFlow. and globalApp. don't get executed more than once
                            else {
                                Expression pe = ee.parseExpression(expr);
                                String contextName = pe.getContext();
                                if (!contextName.equals(PAGE_FLOW_CONTEXT) && !contextName.equals(GLOBAL_APP_CONTEXT))
                                    ee.update(expr, updateValue, variableResolver, true);
                            }
                        }
                    }
                    // catch any errors, particularly expression parse failures
                    catch (ExpressionUpdateException e) {
                        String s = Bundle.getString("ExprUpdateError", new Object[]{expr, e});
                        LOG.error(s);

                        // add binding errors via PageFlowUtils
                        InternalUtils.addBindingUpdateError(request, expr, s, e);
                    }
                }
                else {
                    LOG.debug("HTTP request parameter key \"" + key + "\" is not an expression, handle with Struts");

                    if (strutsProperties == null)
                        strutsProperties = new HashMap();

                    strutsProperties.put(key, paramsValue);
                }
            }
            // catch any unexpected exception
            catch (Exception e) {
                String s = Bundle.getString("ProcessPopulate_exprUpdateError", new Object[]{expr, e});

                LOG.warn(s, e);

                // add binding errors via PageFlowUtils
                InternalUtils.addBindingUpdateError(request, expr, s, e);
            }
        }

        handleStrutsProperties(strutsProperties, form);
    }

    /**
     * Execute a NetUI prefix handler.  This method is called when a prefix is encountered in the request that
     * has a "handler" prefix key.  This allows some handler to modify the request's name / value pairs
     * before they are passed to the expression language.
     *
     * @param key the request key that is being processed
     * @param request this request's {@link javax.servlet.ServletRequest}
     */
    private static ExpressionUpdateNode doTagHandler(String key, String expression, String[] values, HttpServletRequest request) {

        LOG.debug("Found prefixed tag; handlerName: " + key.substring(TAG_HANDLER_PREFIX.length(), key.indexOf(TAG_HANDLER_SUFFIX)));

        String handlerName = expression.substring(TAG_HANDLER_PREFIX.length(), expression.indexOf(TAG_HANDLER_SUFFIX));

        // Execute callback to a parameter handler.
        RequestParameterHandler handler = (RequestParameterHandler)HANDLER_MAP.get(handlerName);

        if(handler != null) {
            expression = expression.substring(expression.indexOf(TAG_HANDLER_SUFFIX)+1);

            LOG.debug("found handler for prefix \"" + handlerName + "\" type: " +
                (handler != null ? handler.getClass().getName() : null)
                + "\n\t" + "key: \"" + key + "\" expr: \"" + expression + "\"");
            
            ExpressionUpdateNode node = new ExpressionUpdateNode();
            node.expression = expression;
            node.values = values;
            
            /*
             Call the haneler to process the request's parameter
             */
            handler.process(request, key, expression, node);
            
            return node;
        }
        else throw new IllegalStateException("Request parameter references a tag handler prefix \"" + 
            handlerName + "\" that is not registered for expression \"" + key + "\"");
    }

    /**
     * This code originated from the Struts class
     * {@link org.apache.struts.util.RequestUtils#populate(Object, javax.servlet.http.HttpServletRequest)}
     * and just defers to Struts for Struts's usual request parameter population.
     *
     * @param strutsProperties a Map of properties that should be applied to the form using Strus's algorithm
     * for doing so
     * @param form the form to which properties should be applied
     */
    private static void handleStrutsProperties(Map strutsProperties, ActionForm form) {
        if(strutsProperties != null) {

            LOG.debug("Handle Struts request parameters.");
            Object bean = form;
            if (form instanceof AnyBeanActionForm) {
                bean = ((AnyBeanActionForm) form).getBean();
            }

            /* defer as Struts does to BeanUtils for non-NetUI expression keys */
            try {
                BeanUtils.populate(bean, strutsProperties);
            }
            catch (Exception e) {
                throw new RuntimeException("Exception processing bean and request parameters: ", e);
            }
        }
    }
}
