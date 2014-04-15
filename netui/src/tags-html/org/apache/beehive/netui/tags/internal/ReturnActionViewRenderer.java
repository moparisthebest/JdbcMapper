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
package org.apache.beehive.netui.tags.internal;

import org.apache.beehive.netui.pageflow.internal.InternalConstants;
import org.apache.beehive.netui.pageflow.internal.InternalExpressionUtils;
import org.apache.beehive.netui.pageflow.internal.ViewRenderer;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.rendering.ResponseRenderAppender;
import org.apache.beehive.netui.tags.rendering.ScriptTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.el.ELException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Renderer for the "automatic" script-only page that is produced to send a map of values from a popup window
 * back to the opener, and to close the popup window.
 */
public class ReturnActionViewRenderer
        implements ViewRenderer, Serializable
{
    private static final Logger _log = Logger.getInstance(ReturnActionViewRenderer.class);

    private static char DELIM = ':';
    private static String ITEM_PARAM = InternalConstants.ATTR_PREFIX + "retrieveItem";
    private static String CALLBACK_PARAM = InternalConstants.ATTR_PREFIX + "returnActionCallback";

    private HashMap _retrieveMap;
    private String _callbackFunc;

    public static char getDelim()
    {
        return DELIM;
    }

    public static String getMapItemParamName()
    {
        return ITEM_PARAM;
    }

    public static String getCallbackParamName()
    {
        return CALLBACK_PARAM;
    }

    /**
     * Initialize, based on request parameters we're looking for.
     */
    public void init(ServletRequest request)
    {
        String[] vals = request.getParameterValues(ITEM_PARAM);

        if (vals != null) {
            _retrieveMap = new HashMap();

            for (int i = 0; i < vals.length; i++) {
                String val = vals[i];
                int delimPos = val.indexOf(DELIM);
                if (delimPos != -1) {
                    String expressionToRetrieve = val.substring(0, delimPos);
                    String fieldID = val.substring(delimPos + 1);
                    _retrieveMap.put(fieldID, expressionToRetrieve);
                }
            }
        }

        _callbackFunc = request.getParameter(CALLBACK_PARAM);
    }

    /**
     * Render the JavaScript for this framework response to a popup window.
     * The JavaScript passes a map of field values from the popup back to
     * a JavaScript routine on the originating page updating its form fields.
     * (See {@link org.apache.beehive.netui.tags.html.ConfigurePopup})
     * The javascript also closes the popup window.
     */
    public void renderView(ServletRequest request, ServletResponse response, ServletContext servletContext)
            throws IOException
    {
        ResponseRenderAppender appender = new ResponseRenderAppender(response);
        ScriptTag.State state = new ScriptTag.State();
        ScriptTag br = (ScriptTag) TagRenderingBase.Factory.getRendering(TagRenderingBase.SCRIPT_TAG, request);
        state.suppressComments = false;
        br.doStartTag(appender, state);

        appender.append(ScriptRequestState.getString("popupReturn_begin", null));
        assert request instanceof HttpServletRequest : request.getClass().getName();
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (_retrieveMap != null) {
            for (Iterator/*<Map.Entry>*/ i = _retrieveMap.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                String fieldID = (String) entry.getKey();
                String expressionToRetrieve = "${" + (String) entry.getValue() + '}';
                try {
                    String value =
                            InternalExpressionUtils.evaluateMessage(expressionToRetrieve, null, httpRequest, servletContext);
                    String item =
                            ScriptRequestState.getString("popupReturn_item", new Object[]{fieldID, value});
                    appender.append(item);
                }
                catch (ELException e) {
                    _log.error("Error evaluating expression " + expressionToRetrieve, e);
                }
            }
        }

        appender.append(ScriptRequestState.getString("popupReturn_end", new Object[]{_callbackFunc}));
        br.doEndTag(appender, false);
    }
}
