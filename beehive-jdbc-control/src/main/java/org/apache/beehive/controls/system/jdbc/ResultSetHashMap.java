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
package org.apache.beehive.controls.system.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * The ResultSetHashMap class extends a standard HashMap and populates it with data derived from a JDBC ResultSet.
 * <p/>
 * Note: the keys are treated case-insensitively, and therefore requests made on the map are
 * case-insensitive.  Any direct access to the keys will yield uppercase keys.
 * <p/>
 * Note: only the row associated with the current cursor position is used.
 */
public class ResultSetHashMap extends HashMap<String, Object> {

    /**
     * Default constructor.
     */
    ResultSetHashMap() {
        super();
    }

    /**
     * Constructor that initializes the map to a specific size.
     * @param size the size
     */
    ResultSetHashMap(int size) {
        super(size);
    }

    /**
     * This constructor is optimized for being called in a loop.  It also canonicalizes the
     * column names into upper case so that values in a map can be looked up using either
     * upper, lower, or mixed case strings.
     *
     * @param rs the ResultSet to map
     * @param keys an array of key objects to store in the map
     * @throws SQLException if an error occurs while reading from the ResultSet
     */
    ResultSetHashMap(ResultSet rs, String[] keys) throws SQLException {
        super(keys.length);

        assert keys.length == rs.getMetaData().getColumnCount() + 1;

        for (int i = 1; i < keys.length; i++) {
            assert keys[i].equals(keys[i].toUpperCase());
            put(keys[i], rs.getObject(i));
        }
    }

    ResultSetHashMap(ResultSet rs) throws SQLException {
        super();
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++) {
            put(md.getColumnName(i), rs.getObject(i));
        }
    }

    public boolean containsKey(Object key) {
        return super.containsKey(canonicalizeKey(key));
    }

    public Object get(Object key) {
        return super.get(canonicalizeKey(key));
    }

    public Object put(String key, Object value) {
        return super.put(canonicalizeKey(key), value);
    }

    public Object remove(Object key) {
        return super.remove(canonicalizeKey(key));
    }

    private String canonicalizeKey(Object object) {
        return object == null ? null : object.toString().toUpperCase();
    }
}

