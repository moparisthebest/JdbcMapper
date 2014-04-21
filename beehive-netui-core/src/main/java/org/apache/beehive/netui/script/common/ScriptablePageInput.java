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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * Provide a {@link java.util.Map} object that exposes a set of page inputs to
 * expression language clients.  Access to these page inputs is based on the
 * name of the page input.
 * </p>
 * <p>
 * Access is optimized for read in that the "get" method is fast.  The entrySet()
 * method is only used if needed, which is generally to toString the Map.
 * </p>
 * @deprecated
 */
public class ScriptablePageInput
    extends AbstractScriptableMap {

    private static final Logger LOGGER = Logger.getInstance(ScriptablePageInput.class);

    private HttpServletRequest _request = null;
    private Set _entrySet = null;

    public ScriptablePageInput(ServletRequest request) {
        assert request instanceof HttpServletRequest;

        _request = (HttpServletRequest)request;
    }

    public Object get(Object name) {
        if(LOGGER.isDebugEnabled()) LOGGER.debug("page input get: " + name);

        assert name instanceof String;

        return PageFlowUtils.getActionOutput((String)name, _request);
    }

    /**
     * Create a {@link java.util.Set} of page input entries.  This implementation
     * assumes that the page input set does not change, which is acceptable for
     * JSP clients as the page inputs have been specified when the JSP starts
     * to render.
     *
     * @return Set
     */
    public Set entrySet() {
        if(_entrySet == null) {
            Map piMap = InternalUtils.getPageInputMap(_request);
            ArrayList list = new ArrayList();
            if(piMap != null) {
                Iterator iterator = piMap.keySet().iterator();
                while(iterator.hasNext()) {
                    Object name = iterator.next();
                    Object value = piMap.get(name);
                    list.add(new Entry(name, value));
                }
            }

            _entrySet = new EntrySet((Entry[])list.toArray(new Entry[]{}));
        }

        return _entrySet;
    }

    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof ScriptablePageInput))
            return false;
        return super.equals(obj);
    }

    public boolean containsKey(Object key) {
        Map piMap = InternalUtils.getPageInputMap(_request);
        return (piMap != null && piMap.containsKey(key));
    }
}
