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

import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.el.ELException;

import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public class NetUIReadVariableResolver
    extends NetUIVariableResolver {

    private static final Logger LOGGER = Logger.getInstance(NetUIReadVariableResolver.class);

    private VariableResolver _vr = null;
    private Object _actionForm = null;

    public NetUIReadVariableResolver(VariableResolver vr) {
        assert vr != null;
        _vr = vr;
    }

    public void setActionForm(Object actionForm) {
        _actionForm = actionForm;
    }

    public Object resolveVariable(String name) {
        if(name == null)
            return null;

        try {
            if(name.equals("actionForm"))
                return _actionForm;
            else return _vr.resolveVariable(name);
        }
        catch(ELException e) {
            String message = "Could not resolve variable named \"" + name + "\".  Cause: " + e;
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public String[] getAvailableVariables() {
        return new String[]{
            "actionForm", "pageFlow", "globalApp", "request", "session", "application",
            "pageContext", "bundle", "container", "url", "pageInput"};
    }
}
