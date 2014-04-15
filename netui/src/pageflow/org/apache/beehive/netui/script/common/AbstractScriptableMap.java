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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.beehive.netui.util.iterator.IteratorFactory;

/**
 * Base {@link java.util.Map} implementation that can be used by
 * clients that need to expose implicit objects to an expression
 * language through a Map.  This Map implementation is read-only.
 */
public abstract class AbstractScriptableMap
    extends AbstractMap {

    /**
     * Default implementation of a {@link java.util.Set} that can be returned by the
     * entrySet method of {@link java.util.Map}.  This implementation simply takes an
     * array of entries and implements iterator() and size().
     */
    class EntrySet
        extends AbstractSet {

        private Entry[] _entries = null;

        public EntrySet(Entry[] entries) {
            _entries = entries;
        }

        public Iterator iterator() {
            return IteratorFactory.createIterator(_entries);
        }

        public int size() {
            return _entries != null ? _entries.length : 0;
        }

        public boolean add(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection coll) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection coll) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection coll) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Default implementation of {@link java.util.Map.Entry} that handles
     * key / value pairs in a very basic way.  This is meant as a convenience
     * to subclasses that need to provide an entrySet() to satisfy the
     * {@link java.util.AbstractMap} contract.
     */
    class Entry
        implements Map.Entry {

        private final Object _key;
        private final Object _value;

        Entry(Object key, Object value) {
            _key = key;
            _value = value;
        }

        public Object getKey() {
            return _key;
        }

        public Object getValue() {
            return _value;
        }

        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }

        public int hashCode() {
            return ((_key == null ? 0 : _key.hashCode()) ^ (_value == null ? 0 : _value.hashCode()));
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final Entry entry = (Entry) o;

            if (_key != null ? !_key.equals(entry._key) : entry._key != null) return false;
            if (_value != null ? !_value.equals(entry._value) : entry._value != null) return false;

            return true;
        }
    }
}
