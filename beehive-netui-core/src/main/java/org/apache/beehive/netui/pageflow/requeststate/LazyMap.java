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
package org.apache.beehive.netui.pageflow.requeststate;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class implements the <code>java.util.Map</code> interface in a lazy way.  It will delegate to the
 * <code>java.util.HashMap</code> for the real implementation.  The HashMap will only be created when it
 * certain methods are called such as <code>put</code>, <code>get</code>, etc.
 */
class LazyMap implements Map
{
    private HashMap _map;

    final boolean isMapCreated() {
        return (_map != null);
    }

    ///*************************  This is the MAP interface implementation *************************

    final public int size() {
        return (_map == null) ? 0 : _map.size();
    }

    final public boolean isEmpty() {
        return (_map == null) || _map.isEmpty();
    }

    final public boolean containsKey(Object key) {
        return (_map != null) && _map.containsKey(key);
    }

    final public boolean containsValue(Object value) {
        return (_map != null) && _map.containsValue(value);
    }

    final public Object get(Object key) {
        Map map = getMap();
        return map.get(key);
    }

    final public Object put(Object key, Object value) {
        Map map = getMap();
        return map.put(key,value);
    }

    final public Object remove(Object key) {
        Map map = getMap();
        return map.remove(key);
    }

    final public void putAll(Map t) {
        Map map = getMap();
        map.putAll(t);
    }

    final public void clear() {
        if (_map != null)
            _map.clear();
    }

    final public Set keySet() {
        Map map = getMap();
        return map.keySet();
    }

    final public Collection values() {
        Map map = getMap();
        return map.values();
    }

    final public Set entrySet() {
        Map map = getMap();
        return map.entrySet();
    }

    /****************************************** End Map Interface **********************************************

    /**
     * This method will return the map.  If the map hasn't been created
     * it will be created.  This is done in a thread safe way.
     * @return the map implementation.
     */
    private Map getMap()
    {
        if (_map != null)
            return _map;

        synchronized (this) {
            if (_map != null)
                return _map;
            _map = new HashMap();
        }
        return _map;
    }
}
