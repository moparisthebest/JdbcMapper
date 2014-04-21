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
package org.apache.beehive.netui.script.el;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.script.IllegalExpressionException;
import org.apache.beehive.netui.script.common.ImplicitObjectUtil;
import org.apache.beehive.netui.script.el.util.RequestAttributeMapFacade;
import org.apache.beehive.netui.script.el.util.SessionAttributeMapFacade;
import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public class NetUIUpdateVariableResolver
    extends NetUIVariableResolver {

    private static final Logger LOGGER = Logger.getInstance(NetUIVariableResolver.class);

    private boolean _requestParameter = true;
    private Object _form = null;
    private ServletRequest _request = null;
    private ServletResponse _response = null;

    public NetUIUpdateVariableResolver(Object form,
                                       ServletRequest request,
                                       ServletResponse response,
                                       boolean requestParameter) {
        super();

        _requestParameter = requestParameter;
        _form = form;
        _request = request;
        _response = response;
    }

    public Object resolveVariable(String name) {
        if(name.equals("actionForm"))
            return _form;
        else if(name.equals("pageFlow"))
            return ImplicitObjectUtil.getPageFlow(_request, _response);
        else if(name.equals("globalApp"))
            return ImplicitObjectUtil.getGlobalApp(_request);
        else if(name.equals("sharedFlow"))
            return ImplicitObjectUtil.getSharedFlow(_request);
        else if(name.equals("requestScope")) {
            if(!_requestParameter)
                return new RequestAttributeMapFacade(_request);
            else
                throw new IllegalExpressionException("The request data binding context can not be updated from a request parameter.");
        } else if(name.equals("sessionScope")) {
            if(!_requestParameter)
                return new SessionAttributeMapFacade(((HttpServletRequest)_request).getSession());
            else throw new IllegalExpressionException("The session data binding context can not be updated from a request parameter.");
        }
        // @bug: need to get the ServletContext from somewhere
        else if(name.equals("applicationScope")) {
            if(!_requestParameter)
                return null;
            else throw new IllegalExpressionException("The application data binding context can not be updated from a request parameter.");
        }
        else {
            String msg = "Could not resolve variable named \"" + name + "\" for an expression update.";
            LOGGER.error(msg);
            throw new IllegalExpressionException(msg);
        }
    }

    public String[] getAvailableVariables() {
        if(_requestParameter)
            return new String[]{"actionForm", "pageFlow", "globalApp"};
        else return new String[]{"actionForm", "pageFlow", "globalApp", "request", "session", "application"};
    }
}
