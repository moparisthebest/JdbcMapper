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

package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Abstract base class for all row mappers.
 *
 * RowMappers are used to map the contents of a row in a ResultSet to the return type of an annotated method.
 * Supported RowMapper types include: HashMap, Map, Object, XmlObject.  When a ResultSetMapper is ready to
 * map a ResultSet row to an object, it requests a RowMapper for the return type of the method from the
 * RowMapperFactory.
 *
 */
public abstract class AbstractRowMapper<K, T> implements RowMapper<K,T> {

    /** ResultSet to map. */
    protected final ResultSet _resultSet;

    /** Calendar instance for date/time mappings. */
    protected final Calendar _cal;

    /** Class to map ResultSet Rows to. */
    protected final Class<T> _returnTypeClass;

    protected final Class<K> _mapKeyType;

    protected final int _columnCount;

    protected final boolean mapOnlySecondColumn;

    protected String[] keys = null; // for caching if we must generate class

    /**
     * Create a new RowMapper for the specified ResultSet and return type Class.
     * @param resultSet ResultSet to map
     * @param returnTypeClass Class to map ResultSet rows to.
     * @param cal Calendar instance for date/time values.
     */
    protected AbstractRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<K> mapKeyType) {
        _resultSet = resultSet;
        _returnTypeClass = returnTypeClass;
        _cal = cal;
        _mapKeyType = mapKeyType;

        try {
            _columnCount = resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new MapperException("RowToObjectMapper: SQLException: " + e.getMessage(), e);
        }

        mapOnlySecondColumn = _mapKeyType != null && _columnCount == 2;
    }

    protected AbstractRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal) {
        this(resultSet, returnTypeClass, cal, null);
    }

    protected AbstractRowMapper(String[] keys, Class<T> returnTypeClass, Calendar cal, Class<K> mapKeyType) {
        _resultSet = null;
        _returnTypeClass = returnTypeClass;
        _cal = cal;
        _mapKeyType = mapKeyType;
        this.keys = keys;
        _columnCount = keys.length - 1;

        mapOnlySecondColumn = _mapKeyType != null && _columnCount == 2;
    }

    /**
     * Build a String array of column names from the ResultSet.
     * @return A String array containing the column names contained within the ResultSet.
     * @throws java.sql.SQLException on error
     */
    protected String[] getKeysFromResultSet() throws SQLException {

        if(this.keys != null)
            return this.keys;

        String[] keys;
        final ResultSetMetaData md = _resultSet.getMetaData();

        keys = new String[_columnCount + 1];
        for (int i = 1; i <= _columnCount; i++) {
            //keys[i] = md.getColumnName(i).toUpperCase();
            keys[i] = md.getColumnLabel(i).toUpperCase();
        }
        return this.keys = keys;
    }
}
