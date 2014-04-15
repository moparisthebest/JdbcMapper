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

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.context.ControlBeanContext;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.NoSuchElementException;

/**
 * Used by DefaultIteratorResultSetMapper for mapping a ResultSet to an Iterator type.
 */
public class ResultSetIterator implements java.util.Iterator {

    private final Class _returnClass;
    private final ResultSet _rs;
    private final RowMapper _rowMapper;

    private boolean _primed = false;

    /**
     * Create a new ResultSetIterator.
     * @param context A ControlBeanContext.
     * @param method The annotated method.
     * @param rs The ResultSet to map.
     * @param cal A Calendar instance for mapping date/time values.
     */
    ResultSetIterator(ControlBeanContext context, Method method, ResultSet rs, Calendar cal) {
        _rs = rs;

        JdbcControl.SQL methodSQL = (JdbcControl.SQL) context.getMethodPropertySet(method, JdbcControl.SQL.class);
        _returnClass = methodSQL.iteratorElementType();

        if (_returnClass == null) {
            throw new ControlException("Invalid return class declared for Iterator:" + _returnClass.getName());
        }

        _rowMapper = RowMapperFactory.getRowMapper(rs, _returnClass, cal);
    }

    /**
     * Does this iterater have more elements?
     * @return true if there is another element
     */
    public boolean hasNext() {
        if (_primed) {
            return true;
        }

        try {
            _primed = _rs.next();
        } catch (SQLException sqle) {
            return false;
        }
        return _primed;
    }

    /**
     * Get the next element in the iteration.
     * @return The next element in the iteration.
     */
    public Object next() {
        try {
            if (!_primed) {
                _primed = _rs.next();
                if (!_primed) {
                    throw new NoSuchElementException();
                }
            }
            // reset upon consumption
            _primed = false;
            return _rowMapper.mapRowToReturnType();
        } catch (SQLException e) {
            // Since Iterator interface is locked, all we can do
            // is put the real exception inside an expected one.
            NoSuchElementException xNoSuch = new NoSuchElementException("ResultSet exception: " + e);
            xNoSuch.initCause(e);
            throw xNoSuch;
        }
    }

    /**
     * Remove is currently not supported.
     */
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
}

