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
package org.apache.beehive.netui.script.el.util;

import java.util.Iterator;
import javax.servlet.http.HttpSession;

import org.apache.beehive.netui.util.iterator.EnumerationIterator;

/**
 * Class that puts a {@link java.util.Map} facade atop a {@link HttpSession}'s attribute map.
 */
public class SessionAttributeMapFacade
    extends MapFacade
    implements WrappedObject {

    private HttpSession _session = null;

    public SessionAttributeMapFacade(HttpSession session) {
        _session = session;
    }

    /**
     * Implementation of the {@link WrappedObject} API that allows the caller to discover the
     * backing object.
     *
     * @return the backing object
     */
    public Object unwrap() {
        return _session;
    }

    protected Object getValue(Object key) {
        if(key == null)
            return null;

        assert _session != null;
        return _session.getAttribute(key.toString());
    }

    protected Object putValue(Object key, Object value) {
        String strKey = key.toString();
        Object prev = _session.getAttribute(strKey);

        _session.setAttribute(strKey, value);

        return prev;
    }

    protected Iterator getKeys() {
        return new EnumerationIterator(_session.getAttributeNames());
    }
}
