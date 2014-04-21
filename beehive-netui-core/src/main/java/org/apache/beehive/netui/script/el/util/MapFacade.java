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

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;

/**
 * Facade used to put a {@link Map} API atop another object.  For example, a sublcass might allow
 * the {@link javax.servlet.http.HttpSession} to be treated like a Map for purposes of referencing session
 * attributes from expressions.
 */
public abstract class MapFacade
    implements Map {

    private Map _map = null;

    /**
     * Subclasses should implement this method in order to get a value from the
     * backing object.
     * @param key the key of the object
     * @return the value associated with the <code>key</code>.
     */
    protected abstract Object getValue(Object key);

    /**
     * Subclasses should implement this method in order to set a value in
     * the backing object.
     * @param key the key of the object
     * @param value the value to associate with the key
     * @return the previous value associated with this key or <code>null</code> if no value was associated
     */
    protected abstract Object putValue(Object key, Object value);

    /**
     * Get an {@link Enumeration} of the keys associated with a backing object.  Note,
     * an {@link Enumeration} is used here because many web-based attribute maps
     * expose their list of keys only as enumeration objects.
     * @return the {@link Enumeration} of keys available from the backing object
     */
    protected abstract Iterator getKeys();

    /*--------------------------------------------------------------------------

    Implementation of the {@link Map} interface.

    -------------------------------------------------------------------------- */
    public void clear() {
    }

    public boolean containsKey(Object key) {
        return getValue(key) != null;
    }

    public Object get(Object key) {
        return getValue(key);
    }

    public Object put(Object key, Object value) {
        return putValue(key, value);
    }

    public boolean containsValue(Object key) {
        assert getMap() != null;
        return getMap().containsValue(key);
    }

    public Set entrySet() {
        assert getMap() != null;
        return Collections.unmodifiableSet(getMap().entrySet());
    }

    public boolean isEmpty() {
        assert getMap() != null;
        return getMap().isEmpty();
    }

    public Set keySet() {
        assert getMap() != null;
        return Collections.unmodifiableSet(getMap().keySet());
    }

    public void putAll(Map t) {
        assert getMap() != null;
        getMap().putAll(t);
    }

    public Object remove(Object key) {
        assert getMap() != null;
        return getMap().remove(key);
    }

    public int size() {
        assert getMap() != null;
        return getMap().size();
    }

    public Collection values() {
        assert getMap() != null;
        return Collections.unmodifiableCollection(getMap().values());
    }

    private Map getMap() {
        if(_map == null) {
            _map = new HashMap();

            Iterator keys = getKeys();
            assert keys != null;
            while(keys.hasNext()) {
                Object key = keys.next();
                _map.put(key, getValue(key));
            }
        }
        return _map;
    }
}
