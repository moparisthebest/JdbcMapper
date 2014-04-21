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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Map a ResultSet row to a java.util.HashMap object
 */
public final class RowToHashMapMapper extends RowMapper {

    private final String[] _keys;


    /**
     * Create a new RowToHashMapMapper.
     * @param resultSet ResultSet to map
     * @param returnTypeClass Class to map to.
     * @param cal Calendar instance for date/time mappings.
     */
    RowToHashMapMapper(ResultSet resultSet, Class returnTypeClass, Calendar cal) {
        super(resultSet, returnTypeClass, cal);
        try {
            _keys = getKeysFromResultSet();
        } catch (SQLException sql) {
            throw new ControlException("RowToHashMapMapper: SQLException: " + sql.getMessage(), sql);
        }
    }

    /**
     * Do the mapping.
     * @return A ResultSetHashMap object.
     * @throws ControlException on error.
     */
    public Object mapRowToReturnType() {
        try {
            return new ResultSetHashMap(_resultSet, _keys);
        } catch (SQLException e) {
            throw new ControlException("Exception creating HashMap return type: ", e);
        }
    }
}
